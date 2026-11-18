#!/usr/bin/env bash
# Regenerates the Java sources from the original jars (Server/gameServer.jar, Server/worldServer.jar).
# Requires: JDK 11+ (Vineflower needs 11+; set VF_JAVA to pick a different java for it), python3, unzip, and vineflower.jar (https://github.com/Vineflower/vineflower, used: 1.11.1).
# Usage: tools/decompile/decompile.sh <vineflower.jar> <gameServer.jar> <worldServer.jar> <out-dir>
set -euo pipefail
VF=$1; GJ=$2; WJ=$3; OUT=$4
HERE=$(cd "$(dirname "$0")" && pwd)
W=$(mktemp -d)
mkdir -p "$W/g" "$W/w" "$W/cls/common" "$W/cls/game" "$W/cls/world" "$W/libs"
(cd "$W/g" && unzip -q "$GJ"); (cd "$W/w" && unzip -q "$WJ")
COMMON="com/zhonglian BaseCommon BaseServer BaseTask BaseThread ConsoleTask proto"
(cd "$W/g" && cp -r --parents $COMMON "$W/cls/common/" && cp -r --parents core business d.class "$W/cls/game/")
(cd "$W/w" && cp -r --parents core business d.class "$W/cls/world/")
# everything else in gameServer.jar is third-party library code: used only as decompiler context
(cd "$W/g" && rm -rf $COMMON core business d.class && zip -qr "$W/libs/libs.jar" . -i '*.class')
for m in common game world; do
  mkdir -p "$OUT/$m"
  ctx="-e=$W/libs/libs.jar"; [ $m != common ] && ctx="$ctx -e=$W/cls/common"
  "${VF_JAVA:-java}" -jar "$VF" -log=ERROR -ind="    " -dgs=1 -ump=1 $ctx "$W/cls/$m" "$OUT/$m"
done
# 1) ecj-style try-with-resources -> try (...) {}
python3 "$HERE/fix_twr.py" $(grep -rl 'addSuppressed\|\$VF' "$OUT" || true)
# 2) hand fixes for spots javac rejects
python3 "$HERE/common_fixes.py" "$OUT/common"
python3 "$HERE/server_fixes.py" "$OUT/game"
python3 "$HERE/server_fixes.py" "$OUT/world"
# 3) synthetic `null` argument of private inner-class constructors (driven by javac errors)
CP="$W/libs/libs.jar"
mkdir -p "$W/out/common"; javac -nowarn -encoding UTF-8 -cp "$CP" -d "$W/out/common" $(find "$OUT/common" -name '*.java')
for m in game world; do
  for i in 1 2 3; do
    mkdir -p "$W/out/$m"
    if javac -nowarn -encoding UTF-8 -Xmaxerrs 5000 -cp "$CP:$W/out/common" -d "$W/out/$m" $(find "$OUT/$m" -name '*.java') > "$W/err_$m.txt" 2>&1; then break; fi
    python3 "$HERE/fix_synth_ctor.py" "$W/err_$m.txt"
  done
done
echo "done: sources in $OUT/{common,game,world}"
rm -rf "$W"
