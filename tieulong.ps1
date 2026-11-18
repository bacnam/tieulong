<#
Tieu Long - Docker setup & operations (Windows / Docker Desktop)

  .\tieulong.ps1 setup             first install: create .env, build images, start, wait until ready
  .\tieulong.ps1 start             start all services and wait until the game server is ready
  .\tieulong.ps1 stop [-Force]     safe stop: block logins, kick players, wait for DB writes, stop
  .\tieulong.ps1 restart           safe stop + start
  .\tieulong.ps1 deploy            rebuild images from the current code, safe stop, start
  .\tieulong.ps1 status            containers, online players, pending DB writes
  .\tieulong.ps1 logs [service]    follow logs (game, world, client, mysql)
  .\tieulong.ps1 backup            dump all databases to backups\
  .\tieulong.ps1 restore <file>    restore a dump made by "backup" (stops game/world first)
  .\tieulong.ps1 console [game|world]  attach to the BeanShell console (detach: Ctrl-P Ctrl-Q)
  .\tieulong.ps1 gm '<beanshell>'  run one BeanShell statement on the game server, print the result

Options: -Yes  answer yes to every question (unattended use)
If scripts are blocked: powershell -ExecutionPolicy Bypass -File .\tieulong.ps1 setup
Docker Desktop is for dev/test only; run production on Linux with tieulong.sh.
#>
param(
    [Parameter(Position = 0)][string]$Command = 'help',
    [Parameter(Position = 1, ValueFromRemainingArguments = $true)][string[]]$Rest = @(),
    [switch]$Force,
    [switch]$Yes
)
$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath $PSScriptRoot
try { [Console]::OutputEncoding = [System.Text.Encoding]::UTF8 } catch { }

$GameGmUrl = if ($env:GAME_GM_URL) { $env:GAME_GM_URL } else { 'http://127.0.0.1:7501' }
$WorldGmUrl = if ($env:WORLD_GM_URL) { $env:WORLD_GM_URL } else { 'http://127.0.0.1:9003' }
$GmSignKey = if ($env:GM_SIGN_KEY) { $env:GM_SIGN_KEY } else { 'svGVs2JLvp00lW2IfmIKcpB38Zbgmhnv' }   # HttpUtils.SIGN_KEY
$StateFile = '.tieulong-state'
$WaitReadySeconds = 600
$WaitDrainSeconds = 300
$Databases = 'tieulong_game_server1 tieulong_game_server1_log tieulong_dbhunkftalk tieulong_dbhunkftalk_log tieulong_web'

# ---------------------------------------------------------------- helpers
function Info($m) { Write-Host "==> $m" -ForegroundColor Cyan }
function Ok($m) { Write-Host "OK  $m" -ForegroundColor Green }
function Warn($m) { Write-Host "!   $m" -ForegroundColor Yellow }
function Die($m) { Write-Host "ERR $m" -ForegroundColor Red; exit 1 }
function Confirm-Action($q) {
    if ($Yes) { return $true }
    $a = Read-Host "$q [y/N]"
    return ($a -match '^[YyCc]')
}

$script:Dc = $null
function Initialize-Compose {
    if (-not (Get-Command docker -ErrorAction SilentlyContinue)) { Die 'Chưa cài Docker Desktop: https://docs.docker.com/desktop/install/windows-install/' }
    & docker info *> $null
    if ($LASTEXITCODE -ne 0) { Die 'Docker chưa chạy. Mở Docker Desktop rồi chạy lại.' }
    & docker compose version *> $null
    if ($LASTEXITCODE -ne 0) { Die "Chưa có Docker Compose v2 ('docker compose')." }
    $script:Dc = 'compose'
}
function Invoke-Dc {
    & docker compose @args
    if ($LASTEXITCODE -ne 0) { Die "docker compose $($args -join ' ') thất bại" }
}
function Get-ServiceId($svc) { (& docker compose ps -q $svc 2>$null | Select-Object -First 1) }
function Test-Running($svc) {
    $id = Get-ServiceId $svc
    if (-not $id) { return $false }
    return ((& docker inspect -f '{{.State.Running}}' $id) -eq 'true')
}

function New-RandomHex([int]$bytes) {
    $b = New-Object byte[] $bytes
    [System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($b)
    return -join ($b | ForEach-Object { $_.ToString('x2') })
}

# java.net.URLEncoder.encode(value, "UTF-8"): keeps a-zA-Z0-9 . - * _, space -> +, rest %XX
function ConvertTo-JavaUrlEncoded([string]$s) {
    $sb = New-Object System.Text.StringBuilder
    foreach ($byte in [System.Text.Encoding]::UTF8.GetBytes($s)) {
        $c = [char]$byte
        if (($byte -lt 128) -and ($c -match '[A-Za-z0-9.*_\-]')) { [void]$sb.Append($c) }
        elseif ($byte -eq 32) { [void]$sb.Append('+') }
        else { [void]$sb.Append('%' + $byte.ToString('X2')) }
    }
    return $sb.ToString()
}
function Get-Md5Hex([string]$s) {
    $md5 = [System.Security.Cryptography.MD5]::Create()
    return -join ($md5.ComputeHash([System.Text.Encoding]::UTF8.GetBytes($s)) | ForEach-Object { $_.ToString('x2') })
}

# Invoke-Gm <base url> <path> @{key=value}: signs like the GM tool (sorted keys, k=urlencode(v)&..., MD5)
function Invoke-Gm([string]$base, [string]$path, [hashtable]$params) {
    $keys = [string[]]$params.Keys
    [Array]::Sort($keys, [System.StringComparer]::Ordinal)
    $src = ''
    $body = [ordered]@{}
    foreach ($k in $keys) {
        $src += "$k=$(ConvertTo-JavaUrlEncoded ([string]$params[$k]))&"
        $body[$k] = [string]$params[$k]
    }
    $body['sign'] = Get-Md5Hex ($src + $GmSignKey)
    $json = $body | ConvertTo-Json -Compress
    return Invoke-RestMethod -Uri "$base$path" -Method Post -ContentType 'application/json; charset=utf-8' `
        -Body ([System.Text.Encoding]::UTF8.GetBytes($json)) -TimeoutSec 30
}
function Invoke-GmScript([string]$base, [string]$code) {
    try { return [string](Invoke-Gm $base '/game/control/cmd' @{ mode = 'script'; cmd = $code }).msg } catch { return $null }
}

function Wait-Healthy([string]$svc, [int]$limit) {
    $t = 0
    while ($true) {
        $id = Get-ServiceId $svc
        $st = if ($id) { & docker inspect -f '{{if .State.Health}}{{.State.Health.Status}}{{else}}{{.State.Status}}{{end}}' $id } else { 'missing' }
        if ($st -eq 'healthy') { Ok "${svc}: healthy"; return $true }
        if (($st -in 'exited', 'dead', 'missing') -and $t -gt 20) { Warn "${svc}: $st"; return $false }
        if ($t -ge $limit) { Warn "$svc chưa sẵn sàng sau ${limit}s (trạng thái: $st)"; return $false }
        Start-Sleep -Seconds 5; $t += 5
        if ($t % 30 -eq 0) { Info "đang chờ $svc ($st, ${t}s)..." }
    }
}

function Wait-GameReady([string]$since) {
    Info 'Chờ GameServer khởi động xong (lần đầu có thể lâu: tạo DB, tạo 1.110 robot)...'
    if (-not (Wait-Healthy 'mysql' $WaitReadySeconds)) { Die 'MySQL không lên. Xem: .\tieulong.ps1 logs mysql' }
    if (-not (Wait-Healthy 'world' $WaitReadySeconds)) { Die 'WorldServer không lên. Xem: .\tieulong.ps1 logs world' }
    if (-not (Wait-Healthy 'game' $WaitReadySeconds)) { Die 'GameServer không lên. Xem: .\tieulong.ps1 logs game' }
    $t = 0
    # only logs of this start: a restarted container keeps the lines of its previous runs
    while (-not ((& docker compose logs --no-color --since $since game 2>$null) -match '服务器启动完毕')) {
        if ($t -ge $WaitReadySeconds) { Die 'Không thấy log khởi động xong của GameServer.' }
        Start-Sleep -Seconds 3; $t += 3
    }
    Ok 'GameServer đã sẵn sàng'
}

function Get-EnvValue($name) {
    if (-not (Test-Path .env)) { return $null }
    $line = Get-Content .env | Where-Object { $_ -match "^$name=" } | Select-Object -Last 1
    if ($line) { return $line.Substring($name.Length + 1) } else { return $null }
}
function Show-Access {
    $hostName = Get-EnvValue 'GAME_PUBLIC_HOST'; if (-not $hostName) { $hostName = 'localhost' }
    $port = Get-EnvValue 'GAME_CLIENT_PORT'; if (-not $port) { $port = '8501' }
    $web = Get-EnvValue 'WEB_PORT'; if (-not $web) { $web = '8080' }
    Write-Host ''
    Ok "Truy cập game:   http://${hostName}:$web"
    Write-Host "  WebSocket game:  ${hostName}:$port  (phải mở firewall cho người chơi)"
    Write-Host "  GM API (local):  $GameGmUrl  ·  $WorldGmUrl   (trang thử: $GameGmUrl/game/test/index)"
    Write-Host '  Console GM:      .\tieulong.ps1 console'
}
function Get-UtcNow { (Get-Date).ToUniversalTime().ToString('yyyy-MM-ddTHH:mm:ssZ') }

# ------------------------------------------------- online limit (block / unblock logins)
function Save-OnlineLimit {
    $cur = Invoke-GmScript $GameGmUrl 'core.server.DynamicConfig.get("OnlineLimit", 2000)'
    if (-not ($cur -match '^\d+$')) { $cur = '2000' }
    if ([int]$cur -gt 0) { Set-Content -Path $StateFile -Value "ONLINE_LIMIT=$cur" -Encoding ASCII }
}
function Restore-OnlineLimit {
    $limit = '2000'
    if (Test-Path $StateFile) {
        $l = (Get-Content $StateFile | Where-Object { $_ -match '^ONLINE_LIMIT=' }) -replace '^ONLINE_LIMIT=', ''
        if ($l) { $limit = $l }
    }
    try {
        $r = Invoke-Gm $GameGmUrl '/game/control/set' @{ key = 'OnlineLimit'; value = $limit }
        if ($r.state -eq 1000) { Ok "Mở đăng nhập (OnlineLimit=$limit)"; return }
    } catch { }
    Warn "Không đặt lại được OnlineLimit; làm tay: .\tieulong.ps1 gm 'core.server.DynamicConfig.set(""OnlineLimit"",""2000"")'"
}

function Get-PendingWrites([string]$url) {
    $info = Invoke-GmScript $url 'BaseTask.AsynTask.AsyncTaskManager.getInstance().toString()'
    if ($null -eq $info) { return $null }
    $sum = 0
    foreach ($m in [regex]::Matches($info, '\{\d+:(\d+)\}')) { $sum += [int]$m.Groups[1].Value }
    return $sum
}
function Wait-Drained([string]$name, [string]$url) {
    $t = 0; $zeros = 0
    while ($true) {
        $n = Get-PendingWrites $url
        if ($null -eq $n) { Warn "${name}: không đọc được hàng đợi ghi DB (server không phản hồi?)"; return $false }
        if ($n -eq 0) { $zeros++; if ($zeros -ge 3) { Ok "${name}: hàng đợi ghi DB đã trống"; return $true } }
        else { $zeros = 0; Info "${name}: còn $n tác vụ ghi DB..." }
        if ($t -ge $WaitDrainSeconds) { Warn "${name}: hàng đợi chưa trống sau ${WaitDrainSeconds}s"; return $false }
        Start-Sleep -Seconds 3; $t += 3
    }
}

# ---------------------------------------------------------------- commands
function Invoke-Setup {
    Initialize-Compose
    if (-not (Test-Path docker-compose.yml)) { Die 'Không thấy docker-compose.yml (chạy script ở thư mục gốc repo).' }
    if (Test-Path .env) { Ok '.env đã có, giữ nguyên' }
    else {
        Info 'Tạo .env'
        $defaultHost = 'localhost'
        $hostName = if ($Yes) { if ($env:GAME_PUBLIC_HOST) { $env:GAME_PUBLIC_HOST } else { $defaultHost } }
                    else { $h = Read-Host "Domain/IP người chơi dùng để truy cập [$defaultHost]"; if ($h) { $h } else { $defaultHost } }
        $lines = Get-Content .env.example -Encoding UTF8 | ForEach-Object {
            if ($_ -match '^MYSQL_ROOT_PASSWORD=') { "MYSQL_ROOT_PASSWORD=$(New-RandomHex 16)" }
            elseif ($_ -match '^APP_KEY=') { "APP_KEY=$(New-RandomHex 16)" }
            elseif ($_ -match '^GAME_PUBLIC_HOST=') { "GAME_PUBLIC_HOST=$hostName" }
            else { $_ }
        }
        # UTF-8 without BOM, LF: docker compose reads it as-is
        [System.IO.File]::WriteAllText((Join-Path $PWD '.env'), (($lines -join "`n") + "`n"), (New-Object System.Text.UTF8Encoding $false))
        Ok '.env đã tạo (mật khẩu MySQL và APP_KEY sinh ngẫu nhiên; giữ file này cẩn thận)'
    }
    New-Item -ItemType Directory -Force -Path backups | Out-Null
    Warn 'Docker Desktop chỉ dùng cho dev/test: kết nối đi qua NAT 192.168.65.x, mà server hiện cho mọi IP 192.168.* chạy lệnh GM.'
    Info 'Build image (lần đầu mất vài phút: tải Maven, Composer, Docker image)...'
    Invoke-Dc build
    Info 'Khởi động'
    $since = Get-UtcNow
    Invoke-Dc up -d
    Wait-GameReady $since
    Show-Access
}

function Invoke-Start {
    Initialize-Compose
    if (-not (Test-Path .env)) { Die 'Chưa có .env. Chạy: .\tieulong.ps1 setup' }
    $since = Get-UtcNow
    Invoke-Dc up -d
    Wait-GameReady $since
    Restore-OnlineLimit
    Show-Access
}

function Invoke-Stop([bool]$force) {
    Initialize-Compose
    if ((Test-Running 'game') -and -not $force) {
        Info 'Chặn đăng nhập mới'
        Save-OnlineLimit
        try { Invoke-Gm $GameGmUrl '/game/control/set' @{ key = 'OnlineLimit'; value = '0' } | Out-Null } catch { Warn 'Không chặn được đăng nhập' }
        Info 'Kick toàn bộ người chơi'
        if ($null -eq (Invoke-GmScript $GameGmUrl 'business.player.PlayerMgr.getInstance().kickoutAllPlayer()')) { Warn 'Kick thất bại' }
        Start-Sleep -Seconds 3
        if (-not (Wait-Drained 'GameServer' $GameGmUrl)) {
            if (-not (Confirm-Action 'Vẫn dừng GameServer (có thể mất dữ liệu chưa ghi)?')) { Die 'Huỷ dừng.' }
        }
    }
    Info 'Dừng GameServer'; Invoke-Dc stop game
    if ((Test-Running 'world') -and -not $force) {
        if (-not (Wait-Drained 'WorldServer' $WorldGmUrl)) {
            if (-not (Confirm-Action 'Vẫn dừng WorldServer?')) { Die 'Huỷ dừng.' }
        }
    }
    Info 'Dừng WorldServer, web, MySQL'; Invoke-Dc stop world client mysql
    Ok 'Đã dừng. Lần start sau sẽ tự mở lại đăng nhập.'
}

function Invoke-Backup {
    Initialize-Compose
    if (-not (Test-Running 'mysql')) { Die 'MySQL không chạy.' }
    New-Item -ItemType Directory -Force -Path backups | Out-Null
    $f = "backups\tieulong_$(Get-Date -Format 'yyyy-MM-dd_HHmmss').sql.gz"
    Info "Sao lưu ra $f (khoá bảng trong lúc dump)"
    # compress inside the container: PowerShell pipes would re-encode the binary dump.
    # No double quotes inside 'sh -c': Windows PowerShell 5.1 mangles them for native commands.
    $id = Get-ServiceId 'mysql'
    & docker exec $id sh -c "MYSQL_PWD=`$MYSQL_ROOT_PASSWORD mysqldump -uroot --lock-all-tables --routines --databases $Databases | gzip > /tmp/tieulong_backup.sql.gz"
    if ($LASTEXITCODE -ne 0) { Die 'Sao lưu thất bại.' }
    & docker cp "${id}:/tmp/tieulong_backup.sql.gz" $f
    & docker exec $id rm -f /tmp/tieulong_backup.sql.gz
    if (-not (Test-Path $f) -or (Get-Item $f).Length -eq 0) { Die 'Sao lưu thất bại.' }
    Ok "Đã sao lưu: $f ($([math]::Round((Get-Item $f).Length / 1MB, 2)) MB)"
}

function Invoke-Restore([string]$file) {
    Initialize-Compose
    if (-not $file -or -not (Test-Path $file)) { Die 'Cách dùng: .\tieulong.ps1 restore backups\<file>.sql.gz' }
    if (-not (Confirm-Action "Ghi đè toàn bộ DB bằng $file?")) { Die 'Huỷ.' }
    if (Test-Running 'game') { Invoke-Stop $false }
    Invoke-Dc up -d mysql
    if (-not (Wait-Healthy 'mysql' 300)) { Die 'MySQL không lên.' }
    Info "Khôi phục $file"
    $id = Get-ServiceId 'mysql'
    $name = Split-Path $file -Leaf
    & docker cp $file "${id}:/tmp/$name"
    $reader = if ($name -like '*.gz') { "gunzip -c /tmp/$name" } else { "cat /tmp/$name" }
    & docker exec $id sh -c "$reader | MYSQL_PWD=`$MYSQL_ROOT_PASSWORD mysql -uroot"
    $rc = $LASTEXITCODE
    & docker exec $id rm -f "/tmp/$name"
    if ($rc -ne 0) { Die 'Khôi phục thất bại.' }
    Ok 'Đã khôi phục'
    Invoke-Start
}

function Invoke-Status {
    Initialize-Compose
    & docker compose ps
    if (Test-Running 'game') {
        $online = Invoke-GmScript $GameGmUrl 'business.player.PlayerMgr.getInstance().getOnlinePlayers().size()'
        $limit = Invoke-GmScript $GameGmUrl 'core.server.DynamicConfig.get("OnlineLimit", 2000)'
        Write-Host ''
        Write-Host "Người chơi online:   $online"
        Write-Host "Hàng đợi ghi DB:     game=$(Get-PendingWrites $GameGmUrl)  world=$(Get-PendingWrites $WorldGmUrl)"
        Write-Host "Giới hạn online:     $limit"
    }
}

function Invoke-Console([string]$svc) {
    Initialize-Compose
    if (-not $svc) { $svc = 'game' }
    $id = Get-ServiceId $svc
    if (-not $id) { Die "$svc không chạy." }
    Write-Host "Console BeanShell của $svc. Gõ lệnh rồi Enter."
    Write-Host 'Thoát: Ctrl-P rồi Ctrl-Q. KHÔNG nhấn Ctrl-C (sẽ dừng server).' -ForegroundColor Yellow
    & docker attach --sig-proxy=false $id
}

function Show-Usage {
    $text = Get-Content -LiteralPath $PSCommandPath -Encoding UTF8 -TotalCount 19 | Select-Object -Skip 1
    $text | ForEach-Object { Write-Host $_ }
}

# ---------------------------------------------------------------- main
switch ($Command.ToLower()) {
    'setup' { Invoke-Setup }
    'start' { Invoke-Start }
    'stop' { Invoke-Stop ($Force -or ($Rest -contains '--force')) }
    'restart' { Invoke-Stop ($Force -or ($Rest -contains '--force')); Invoke-Start }
    'deploy' {
        Initialize-Compose
        Info 'Build image mới (server vẫn chạy trong lúc build)'
        Invoke-Dc build
        if (-not (Confirm-Action 'Build xong. Dừng server để cập nhật?')) { Die 'Huỷ.' }
        Invoke-Backup; Invoke-Stop $false; Invoke-Start
    }
    'status' { Invoke-Status }
    'logs' { Initialize-Compose; & docker compose logs -f --tail=200 @Rest }
    'backup' { Invoke-Backup }
    'restore' { Invoke-Restore ($Rest | Select-Object -First 1) }
    'console' { Invoke-Console ($Rest | Select-Object -First 1) }
    'gm' {
        if ($Rest.Count -lt 1) { Die "Cách dùng: .\tieulong.ps1 gm '<câu lệnh BeanShell>'" }
        Invoke-Gm $GameGmUrl '/game/control/cmd' @{ mode = 'script'; cmd = ($Rest -join ' ') } | ConvertTo-Json -Compress
    }
    default { Show-Usage }
}
