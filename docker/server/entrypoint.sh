#!/usr/bin/env bash
# Renders conf/*.properties from environment variables, then starts the requested server.
# Usage: entrypoint.sh game|world
set -euo pipefail

ROLE=${1:-game}
case "$ROLE" in
  game)  JAR=gameServer.jar ;;
  world) JAR=worldServer.jar ;;
  *) exec "$@" ;;   # anything else: run it (e.g. bash)
esac

cd /app

# ${VAR} / ${VAR:-default} -> value from the environment
render() {
  perl -pe 's/\$\{(\w+)(?::-([^}]*))?\}/defined $ENV{$1} && $ENV{$1} ne "" ? $ENV{$1} : (defined $2 ? $2 : "")/ge' "$1" > "$2"
}
for t in /app/templates/*.properties; do
  render "$t" "conf/$(basename "$t")"
done

# Wait for MySQL (the servers exit immediately if the DB is not reachable)
DB_HOST=${DB_HOST:-mysql}; DB_PORT=${DB_PORT:-3306}
for i in $(seq 1 60); do
  (echo > "/dev/tcp/$DB_HOST/$DB_PORT") 2>/dev/null && break
  echo "waiting for MySQL $DB_HOST:$DB_PORT ($i)"; sleep 2
done

# GameServer needs the WorldServer up first when the world link is on
if [ "$ROLE" = game ] && [ "${WORLD_OPEN:-on}" = on ]; then
  WH=${WORLD_HOST:-world}; WP=${WORLD_PORT:-8003}
  for i in $(seq 1 60); do
    (echo > "/dev/tcp/$WH/$WP") 2>/dev/null && break
    echo "waiting for WorldServer $WH:$WP ($i)"; sleep 2
  done
fi

# shellcheck disable=SC2086
exec java -Dfile.encoding=UTF-8 -Duser.timezone="${TZ:-Asia/Ho_Chi_Minh}" $JAVA_OPTS -jar "$JAR" conf
