#!/usr/bin/env bash
# Tieu Long - Docker setup & operations
#
#   ./tieulong.sh setup             first install: create .env, build images, start, wait until ready
#   ./tieulong.sh start             start all services and wait until the game server is ready
#   ./tieulong.sh stop [--force]    safe stop: block logins, kick players, wait for DB writes, stop
#   ./tieulong.sh restart           safe stop + start
#   ./tieulong.sh deploy            rebuild images from the current code, safe stop, start
#   ./tieulong.sh status            containers, online players, pending DB writes
#   ./tieulong.sh logs [service]    follow logs (game, world, client, mysql)
#   ./tieulong.sh backup            dump all databases to backups/
#   ./tieulong.sh restore <file>    restore a dump made by "backup" (stops game/world first)
#   ./tieulong.sh console [game|world]  attach to the BeanShell console (detach: Ctrl-P Ctrl-Q)
#   ./tieulong.sh gm '<beanshell>'  run one BeanShell statement on the game server, print the result
#
# Options: -y / --yes  answer yes to every question (unattended use)
set -euo pipefail

cd "$(dirname "$0")"

GAME_GM_URL=${GAME_GM_URL:-http://127.0.0.1:7501}
WORLD_GM_URL=${WORLD_GM_URL:-http://127.0.0.1:9003}
GM_SIGN_KEY=${GM_SIGN_KEY:-svGVs2JLvp00lW2IfmIKcpB38Zbgmhnv}   # see HttpUtils.SIGN_KEY
STATE_FILE=.tieulong-state
WAIT_READY_SECONDS=${WAIT_READY_SECONDS:-600}
WAIT_DRAIN_SECONDS=${WAIT_DRAIN_SECONDS:-300}
ASSUME_YES=0

# ---------------------------------------------------------------- helpers
if [ -t 1 ]; then B=$'\e[1m'; G=$'\e[32m'; Y=$'\e[33m'; R=$'\e[31m'; N=$'\e[0m'; else B='' G='' Y='' R='' N=''; fi
info() { echo "${B}==>${N} $*"; }
ok()   { echo "${G}✔${N} $*"; }
warn() { echo "${Y}!${N} $*" >&2; }
die()  { echo "${R}✘ $*${N}" >&2; exit 1; }

confirm() {   # confirm "question" -> 0 = yes
  [ "$ASSUME_YES" = 1 ] && return 0
  local a; read -r -p "$1 [y/N] " a; [[ "$a" =~ ^[YyCc] ]]
}

DC=()
detect_compose() {
  command -v docker >/dev/null 2>&1 || die "Chưa cài Docker. Xem https://docs.docker.com/engine/install/"
  docker info >/dev/null 2>&1 || die "Docker daemon chưa chạy, hoặc user hiện tại không có quyền (thêm vào group docker)."
  if docker compose version >/dev/null 2>&1; then DC=(docker compose)
  elif command -v docker-compose >/dev/null 2>&1; then DC=(docker-compose)
  else die "Chưa có Docker Compose v2 (plugin 'docker compose')."; fi
}
dc() { "${DC[@]}" "$@"; }

random_hex() {  # random_hex <bytes>
  if command -v openssl >/dev/null 2>&1; then openssl rand -hex "$1"
  else head -c "$1" /dev/urandom | od -An -tx1 | tr -d ' \n'; fi
}

# java.net.URLEncoder.encode(value, "UTF-8"): keeps a-zA-Z0-9 . - * _, space -> +, rest %XX
urlencode() {
  local LC_ALL=C s=$1 out='' c i
  for ((i = 0; i < ${#s}; i++)); do
    c=${s:i:1}
    case "$c" in
      [a-zA-Z0-9.*_-]) out+=$c ;;
      ' ') out+='+' ;;
      *) printf -v c '%%%02X' "'$c"; out+=$c ;;
    esac
  done
  printf '%s' "$out"
}
json_escape() { local s=$1; s=${s//\\/\\\\}; s=${s//\"/\\\"}; s=${s//$'\n'/\\n}; s=${s//$'\t'/\\t}; printf '%s' "$s"; }
md5() { if command -v md5sum >/dev/null 2>&1; then md5sum | cut -d' ' -f1; else md5 -q; fi; }

# gm_call <base url> <path> key value [key value ...]  -> raw JSON response
# Builds the JSON body and the "sign" exactly like the GM tool expects (sorted keys, k=urlencode(v)&..., MD5).
gm_call() {
  local base=$1 path=$2; shift 2
  local -a keys=() ; local -A vals=()
  while [ $# -ge 2 ]; do keys+=("$1"); vals[$1]=$2; shift 2; done
  local sorted src='' body='{' k
  sorted=$(printf '%s\n' "${keys[@]}" | LC_ALL=C sort)
  while IFS= read -r k; do
    src+="$k=$(urlencode "${vals[$k]}")&"
    body+="\"$k\":\"$(json_escape "${vals[$k]}")\","
  done <<< "$sorted"
  body+="\"sign\":\"$(printf '%s%s' "$src" "$GM_SIGN_KEY" | md5)\"}"
  curl -sS --max-time 30 -H 'Content-Type: application/json' --data-binary "$body" "$base$path"
}
json_field() {  # json_field <name>  (reads JSON on stdin, prints a top-level string/number field)
  sed -n "s/.*\"$1\":\(\"\([^\"\\\\]*\(\\\\.[^\"\\\\]*\)*\)\"\|\([0-9-]*\)\).*/\2\4/p" | head -1
}
gm_script() {   # gm_script <base url> <beanshell>  -> msg of the response
  gm_call "$1" /game/control/cmd mode script cmd "$2" | json_field msg
}

service_id() { dc ps -q "$1" 2>/dev/null; }
is_running() { local id; id=$(service_id "$1"); [ -n "$id" ] && [ "$(docker inspect -f '{{.State.Running}}' "$id")" = true ]; }

wait_healthy() {  # wait_healthy <service> <seconds>
  local svc=$1 limit=$2 t=0 id st
  while :; do
    id=$(service_id "$svc")
    st=$( [ -n "$id" ] && docker inspect -f '{{if .State.Health}}{{.State.Health.Status}}{{else}}{{.State.Status}}{{end}}' "$id" || echo missing)
    case "$st" in healthy) ok "$svc: healthy"; return 0 ;; exited|dead|missing) [ $t -gt 20 ] && { warn "$svc: $st"; return 1; } ;; esac
    [ $t -ge "$limit" ] && { warn "$svc chưa sẵn sàng sau ${limit}s (trạng thái: $st)"; return 1; }
    sleep 5; t=$((t + 5))
    [ $((t % 30)) -eq 0 ] && info "đang chờ $svc ($st, ${t}s)..."
  done
}

wait_game_ready() {  # wait_game_ready <since: UTC timestamp taken before "up">
  local since=${1:-}
  info "Chờ GameServer khởi động xong (lần đầu có thể lâu: tạo DB, tạo 1.110 robot)..."
  wait_healthy mysql "$WAIT_READY_SECONDS" || die "MySQL không lên. Xem: ./tieulong.sh logs mysql"
  wait_healthy world "$WAIT_READY_SECONDS" || die "WorldServer không lên. Xem: ./tieulong.sh logs world"
  wait_healthy game  "$WAIT_READY_SECONDS" || die "GameServer không lên. Xem: ./tieulong.sh logs game"
  local t=0
  # only logs of this start: a restarted container keeps the lines of its previous runs
  until dc logs --no-color ${since:+--since "$since"} game 2>/dev/null | grep -q '服务器启动完毕'; do
    [ $t -ge "$WAIT_READY_SECONDS" ] && die "Không thấy log khởi động xong của GameServer."
    sleep 3; t=$((t + 3))
  done
  ok "GameServer đã sẵn sàng"
}

env_get() { [ -f .env ] && sed -n "s/^$1=//p" .env | tail -1; }
print_access() {
  local host port web
  host=$(env_get GAME_PUBLIC_HOST); port=$(env_get GAME_CLIENT_PORT); web=$(env_get WEB_PORT)
  echo
  ok "Truy cập game:   http://${host:-localhost}:${web:-8080}"
  echo "  WebSocket game:  ${host:-localhost}:${port:-8501}  (phải mở firewall cho người chơi)"
  echo "  GM API (local):  $GAME_GM_URL  ·  $WORLD_GM_URL   (trang thử: $GAME_GM_URL/game/test/index)"
  echo "  Console GM:      ./tieulong.sh console"
}

# ------------------------------------------------- online limit (block / unblock logins)
save_online_limit() {
  local cur; cur=$(gm_script "$GAME_GM_URL" 'core.server.DynamicConfig.get("OnlineLimit", 2000)' 2>/dev/null || true)
  [[ "$cur" =~ ^[0-9]+$ ]] || cur=2000
  [ "$cur" -gt 0 ] && echo "ONLINE_LIMIT=$cur" > "$STATE_FILE"
}
restore_online_limit() {
  local limit=2000
  [ -f "$STATE_FILE" ] && limit=$(sed -n 's/^ONLINE_LIMIT=//p' "$STATE_FILE")
  local r; r=$(gm_call "$GAME_GM_URL" /game/control/set key OnlineLimit value "${limit:-2000}" | json_field state || true)
  [ "$r" = 1000 ] && ok "Mở đăng nhập (OnlineLimit=${limit:-2000})" || warn "Không đặt lại được OnlineLimit; làm tay: ./tieulong.sh gm 'core.server.DynamicConfig.set(\"OnlineLimit\",\"2000\")'"
}

pending_writes() {  # pending_writes <gm url> -> total queued async DB tasks, or empty on error
  local info; info=$(gm_script "$1" 'BaseTask.AsynTask.AsyncTaskManager.getInstance().toString()' 2>/dev/null || true)
  [ -z "$info" ] && return 0
  printf '%s' "$info" | grep -oE '\{[0-9]+:[0-9]+\}' | sed -E 's/.*:([0-9]+)\}/\1/' | awk '{s+=$1} END {print s+0}'
}

wait_drained() {  # wait_drained <name> <gm url>: three consecutive zero readings
  local name=$1 url=$2 t=0 zeros=0 n
  while :; do
    n=$(pending_writes "$url")
    [ -z "$n" ] && { warn "$name: không đọc được hàng đợi ghi DB (server không phản hồi?)"; return 1; }
    if [ "$n" -eq 0 ]; then zeros=$((zeros + 1)); [ $zeros -ge 3 ] && { ok "$name: hàng đợi ghi DB đã trống"; return 0; }
    else zeros=0; info "$name: còn $n tác vụ ghi DB..."; fi
    [ $t -ge "$WAIT_DRAIN_SECONDS" ] && { warn "$name: hàng đợi chưa trống sau ${WAIT_DRAIN_SECONDS}s"; return 1; }
    sleep 3; t=$((t + 3))
  done
}

# ---------------------------------------------------------------- commands
cmd_setup() {
  detect_compose
  [ -f docker-compose.yml ] || die "Không thấy docker-compose.yml (chạy script ở thư mục gốc repo)."
  if [ -f .env ]; then
    ok ".env đã có, giữ nguyên"
  else
    info "Tạo .env"
    local host default_host
    default_host=$(hostname -I 2>/dev/null | awk '{print $1}'); default_host=${default_host:-localhost}
    if [ "$ASSUME_YES" = 1 ]; then host=${GAME_PUBLIC_HOST:-$default_host}
    else read -r -p "Domain/IP người chơi dùng để truy cập [$default_host]: " host; host=${host:-$default_host}; fi
    sed -e "s/^MYSQL_ROOT_PASSWORD=.*/MYSQL_ROOT_PASSWORD=$(random_hex 16)/" \
        -e "s/^APP_KEY=.*/APP_KEY=$(random_hex 16)/" \
        -e "s/^GAME_PUBLIC_HOST=.*/GAME_PUBLIC_HOST=$host/" .env.example > .env
    chmod 600 .env
    ok ".env đã tạo (mật khẩu MySQL và APP_KEY sinh ngẫu nhiên; giữ file này cẩn thận)"
  fi
  mkdir -p backups
  info "Build image (lần đầu mất vài phút: tải Maven, Composer, Docker image)..."
  dc build
  info "Khởi động"
  local since; since=$(date -u +%Y-%m-%dT%H:%M:%SZ)
  dc up -d
  wait_game_ready "$since"
  print_access
}

cmd_start() {
  detect_compose
  [ -f .env ] || die "Chưa có .env. Chạy: ./tieulong.sh setup"
  local since; since=$(date -u +%Y-%m-%dT%H:%M:%SZ)
  dc up -d
  wait_game_ready "$since"
  restore_online_limit
  print_access
}

cmd_stop() {
  detect_compose
  local force=0; [ "${1:-}" = "--force" ] && force=1
  if is_running game && [ $force = 0 ]; then
    info "Chặn đăng nhập mới"
    save_online_limit
    gm_call "$GAME_GM_URL" /game/control/set key OnlineLimit value 0 >/dev/null || warn "Không chặn được đăng nhập"
    info "Kick toàn bộ người chơi"
    gm_script "$GAME_GM_URL" 'business.player.PlayerMgr.getInstance().kickoutAllPlayer()' >/dev/null || warn "Kick thất bại"
    sleep 3
    wait_drained GameServer "$GAME_GM_URL" || confirm "Vẫn dừng GameServer (có thể mất dữ liệu chưa ghi)?" || die "Huỷ dừng."
  fi
  info "Dừng GameServer"; dc stop game
  if is_running world && [ $force = 0 ]; then
    wait_drained WorldServer "$WORLD_GM_URL" || confirm "Vẫn dừng WorldServer?" || die "Huỷ dừng."
  fi
  info "Dừng WorldServer, web, MySQL"; dc stop world client mysql
  ok "Đã dừng. Lần start sau sẽ tự mở lại đăng nhập."
}

cmd_deploy() {
  detect_compose
  info "Build image mới (server vẫn chạy trong lúc build)"
  dc build
  confirm "Build xong. Dừng server để cập nhật?" || die "Huỷ."
  cmd_backup
  cmd_stop
  cmd_start
}

cmd_status() {
  detect_compose
  dc ps
  if is_running game; then
    local online; online=$(gm_script "$GAME_GM_URL" 'business.player.PlayerMgr.getInstance().getOnlinePlayers().size()' 2>/dev/null || true)
    echo
    echo "Người chơi online:   ${online:-?}"
    echo "Hàng đợi ghi DB:     game=$(pending_writes "$GAME_GM_URL")  world=$(pending_writes "$WORLD_GM_URL")"
    echo "Giới hạn online:     $(gm_script "$GAME_GM_URL" 'core.server.DynamicConfig.get("OnlineLimit", 2000)' 2>/dev/null || echo '?')"
  fi
}

cmd_logs() { detect_compose; dc logs -f --tail=200 "$@"; }

DATABASES="tieulong_game_server1 tieulong_game_server1_log tieulong_dbhunkftalk tieulong_dbhunkftalk_log tieulong_web"
cmd_backup() {
  detect_compose
  is_running mysql || die "MySQL không chạy."
  mkdir -p backups
  local f
  f="backups/tieulong_$(date +%F_%H%M%S).sql.gz"
  info "Sao lưu ra $f (khoá bảng trong lúc dump)"
  # shellcheck disable=SC2016
  dc exec -T mysql sh -c 'exec mysqldump -uroot -p"$MYSQL_ROOT_PASSWORD" --lock-all-tables --routines --databases '"$DATABASES" | gzip > "$f"
  [ -s "$f" ] || die "Sao lưu thất bại."
  ok "Đã sao lưu: $f ($(du -h "$f" | cut -f1))"
}

cmd_restore() {
  detect_compose
  local f=${1:-}; [ -f "$f" ] || die "Cách dùng: ./tieulong.sh restore backups/<file>.sql.gz"
  confirm "Ghi đè toàn bộ DB bằng $f?" || die "Huỷ."
  is_running game && cmd_stop
  dc up -d mysql; wait_healthy mysql 300 || die "MySQL không lên."
  info "Khôi phục $f"
  # shellcheck disable=SC2016
  { case "$f" in *.gz) gunzip -c "$f" ;; *) cat "$f" ;; esac; } | dc exec -T mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"'
  ok "Đã khôi phục"
  cmd_start
}

cmd_console() {
  detect_compose
  local svc=${1:-game} id; id=$(service_id "$svc"); [ -n "$id" ] || die "$svc không chạy."
  echo "Console BeanShell của $svc. Gõ lệnh rồi Enter (vd: print(business.player.PlayerMgr.getInstance().getOnlinePlayers().size());)"
  echo "${Y}Thoát: Ctrl-P rồi Ctrl-Q. KHÔNG nhấn Ctrl-C (sẽ dừng server).${N}"
  docker attach --sig-proxy=false "$id"
}

cmd_gm() {
  [ $# -ge 1 ] || die "Cách dùng: ./tieulong.sh gm '<câu lệnh BeanShell>'"
  gm_call "$GAME_GM_URL" /game/control/cmd mode script cmd "$*"; echo
}

usage() { sed -n '2,17p' "$0" | sed 's/^# \{0,1\}//'; }

# ---------------------------------------------------------------- main
args=()
for a in "$@"; do case "$a" in -y|--yes) ASSUME_YES=1 ;; *) args+=("$a") ;; esac; done
set -- "${args[@]+"${args[@]}"}"
cmd=${1:-help}; shift || true
case "$cmd" in
  setup) cmd_setup ;;
  start) cmd_start ;;
  stop) cmd_stop "$@" ;;
  restart) cmd_stop "$@"; cmd_start ;;
  deploy) cmd_deploy ;;
  status) cmd_status ;;
  logs) cmd_logs "$@" ;;
  backup) cmd_backup ;;
  restore) cmd_restore "$@" ;;
  console) cmd_console "$@" ;;
  gm) cmd_gm "$@" ;;
  help|-h|--help) usage ;;
  *) usage; exit 1 ;;
esac
