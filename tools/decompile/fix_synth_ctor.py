"""Remove the trailing synthetic `null` argument that ecj adds when calling private
inner-class constructors (Vineflower keeps it). Driven by javac error output."""
import re, sys
errfile = sys.argv[1]
txt = open(errfile, encoding='utf-8').read()
pat = re.compile(r'^(\S+\.java):(\d+): error: (?:constructor (\w+) in class [\w.$]+ cannot be applied to given types;|no suitable constructor found for (\w+)\(.*<null>\))', re.M)
fixed = 0
cache = {}
for m in pat.finditer(txt):
    path, line, name = m.group(1), int(m.group(2)), m.group(3) or m.group(4)
    # only when the call has an extra trailing <null>
    tail = txt[m.end():m.end() + 600]
    if m.group(3) and not re.search(r'found:\s+.*<null>\n', tail.split('reason:')[0]):
        continue
    src = cache.setdefault(path, open(path, encoding='utf-8').read())
    lines = src.split('\n')
    off = sum(len(l) + 1 for l in lines[:line - 1])
    def is_new(i):
        return re.search(r'new\s+[\w.]*$', src[max(0, i - 200):i]) is not None
    line_end = src.find('\n', off)
    # javac reports the line of `new` (JDK 8) or of the argument list (newer JDKs): look on the line first, then backwards
    idx = src.find(name + '(', off, line_end)
    while idx != -1 and not is_new(idx):
        idx = src.find(name + '(', idx + 1, line_end)
    if idx == -1:
        idx = src.rfind(name + '(', 0, off)
        while idx != -1 and not is_new(idx):
            idx = src.rfind(name + '(', 0, idx)
    assert idx != -1, (path, line, name)
    i = idx + len(name)
    depth = 0
    for j in range(i, len(src)):
        c = src[j]
        if c in '([{': depth += 1
        elif c in ')]}':
            depth -= 1
            if depth == 0:
                break
    args = src[i + 1:j]
    new_args = re.sub(r',\s*(\([\w.$]+\)\s*)?null\s*$', '', args)
    if new_args == args:
        new_args = re.sub(r'^\s*(\([\w.$]+\)\s*)?null\s*$', '', args)
    assert new_args != args, (path, line, args[-80:])
    src = src[:i + 1] + new_args + src[j:]
    cache[path] = src
    fixed += 1
for p, s in cache.items():
    open(p, 'w', encoding='utf-8').write(s)
print('synthetic ctor args removed:', fixed)
