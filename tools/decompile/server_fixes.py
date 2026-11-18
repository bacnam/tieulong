"""Manual fixes for decompiled game/world server sources (javac-incompatible spots)."""
import re, sys, glob, os
ROOT = sys.argv[1]
def sub(path, old, new, count=1, regex=False):
    path = f'{ROOT}/{path}'
    if not os.path.exists(path):
        return
    s = open(path, encoding='utf-8').read()
    if regex:
        s2, n = re.subn(old, new, s)
    else:
        n = s.count(old); s2 = s.replace(old, new) if count == 0 else s.replace(old, new, count)
    assert n >= 1, (path, old)
    open(path, 'w', encoding='utf-8').write(s2)

# RefContainer<?> iteration in AssertAll(): restore the element type
for p in glob.glob(f'{ROOT}/core/config/refdata/ref/Ref*.java'):
    s = open(p, encoding='utf-8').read()
    s2 = re.sub(r'for \((Ref\w+) (\w+) : all\.values\(\)\)', r'for (\1 \2 : ((RefContainer<\1>) all).values())', s)
    if s2 != s:
        open(p, 'w', encoding='utf-8').write(s2)

# Enum.valueOf on a Class<?> (gm command param parsing)
sub('business/gmcmd/cmd/Command.java', 'return Enum.valueOf(clazz, param);', 'return Enum.valueOf((Class) clazz, param);')

if os.path.exists(f'{ROOT}/business/player/feature/PlayerItem.java'):
    sub('business/player/feature/PlayerItem.java', 'IUniItemContainer iContainer = this.player.getFeature(clazz);',
        'IUniItemContainer iContainer = (IUniItemContainer) this.player.getFeature(clazz);', 0)
    sub('business/player/feature/PlayerItem.java', 'IUniItemContainer<?> iContainer = this.player.getFeature((Class<IUniItemContainer<?>>)clazz);',
        'IUniItemContainer<?> iContainer = (IUniItemContainer<?>) this.player.getFeature(clazz);')
    # duplicate local name inside switch cases
    sub('business/global/chat/ChatMgr.java', '''                case CHATTYPE_COMPANY:
                    Player playerx = PlayerMgr.getInstance().getOnlinePlayerByCid(bo.getReceiveCId());
                    if (playerx != null) {
                        playerx.pushProto(''', '''                case CHATTYPE_COMPANY:
                    Player receiver = PlayerMgr.getInstance().getOnlinePlayerByCid(bo.getReceiveCId());
                    if (receiver != null) {
                        receiver.pushProto(''')
    # raw locals produced by synchronized-block copies
    sub('business/global/gmmail/TimerMailMgr.java', '        ArrayList var8;\n', '        ArrayList<FixedTimeMailBO> var8;\n')
    sub('business/global/notice/NoticeMgr.java', '        LinkedList var8;\n', '        LinkedList<Notice> var8;\n')
    # sun.* internal class is not part of the Java 8 public API (--release 8); it subclasses java.net.HttpURLConnection
    sub('core/logger/flow/TDRechargeLogger.java', 'import sun.net.www.protocol.http.HttpURLConnection;', 'import java.net.HttpURLConnection;')
print('server fixes ok:', ROOT)
