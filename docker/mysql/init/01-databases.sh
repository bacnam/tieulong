#!/bin/bash
# Runs once, on the first start of an empty MySQL volume (docker-entrypoint-initdb.d).
# Creates the game databases and loads the schema dumps from Server/database.
set -euo pipefail
SQL=/sql
mysql_root() { mysql -uroot -p"$MYSQL_ROOT_PASSWORD" --default-character-set=utf8mb4 "$@"; }

load() {   # load <database> <dump file>
  echo "[init] $1 <- $2"
  mysql_root -e "CREATE DATABASE IF NOT EXISTS \`$1\` DEFAULT CHARACTER SET utf8mb4;"
  mysql_root "$1" < "$SQL/$2"
}

load "${GAME_DB:-tieulong_game_server1}"         longnu_game_server1.sql
load "${GAME_LOG_DB:-tieulong_game_server1_log}" longnu_game_server1_log.sql
load "${WORLD_DB:-tieulong_dbhunkftalk}"         longnu_dbhunkftalk.sql
load "${WORLD_LOG_DB:-tieulong_dbhunkftalk_log}" longnu_dbhunkftalklog.sql
load "${GM_DB:-tieulong_dbgamemaster}"           longnu_dbgamemaster.sql

# Web portal (Client/) database: accounts + top-up transactions (table prefix baubas_)
WEB_DB=${WEB_DB:-tieulong_web}
echo "[init] $WEB_DB"
mysql_root -e "CREATE DATABASE IF NOT EXISTS \`$WEB_DB\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql_root "$WEB_DB" < /docker-entrypoint-initdb.d/web-schema.sql.in
