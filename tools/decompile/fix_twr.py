"""Rewrite ecj-compiled try-with-resources patterns (decompiled by Vineflower) back to try (...) {}."""
import re, sys

def block_end(lines, i):
    """lines[i] ends with '{'; return index of the line closing that brace."""
    depth = 0
    for j in range(i, len(lines)):
        s = re.sub(r'"(\\.|[^"\\])*"', '""', lines[j])
        if j == i:
            s = s[s.rindex('{'):]
        s = re.sub(r"'(\\.|[^'\\])*'", "''", s)
        for ch in s:
            if ch == '{':
                depth += 1
            elif ch == '}':
                depth -= 1
                if depth == 0:
                    return j
    raise ValueError('unbalanced')

def dedent(ls, n):
    return [l[n:] if l.startswith(' ' * n) else l for l in ls]

DECL = re.compile(r'^([\w.<>\[\], ?]+?) (\w+) = (.*);$')

def first_stmt(lines, a, b):
    j = a
    while j < b and lines[j].strip() == '':
        j += 1
    return j

def is_close_if(ls, r):
    return [l.strip() for l in ls] == [f'if ({r} != null) {{', f'{r}.close();', '}']

def parse_resources(lines, a, b, ind, ex):
    """lines[a:b] is the body of a try whose catch(Throwable) rethrows `ex`.
    Return (resources, body_lines, rest_lines) or None."""
    j = first_stmt(lines, a, b)
    md = DECL.match(lines[j].strip())
    if not md:
        return None
    rtype, rname, init = md.groups()
    k = first_stmt(lines, j + 1, b)
    if lines[k] != ind + 'try {':
        return None
    k_end = block_end(lines, k)
    tail = lines[k_end].strip()
    if tail == '} finally {':
        f_end = block_end(lines, k_end)
        if not is_close_if(lines[k_end + 1:f_end], rname):
            return None
        return [(rtype, rname, init)], lines[k + 1:k_end], lines[f_end + 1:b], ind + '    '
    mc = re.match(r'^\} catch \(Throwable (\w+)\) \{$', tail)
    if mc:
        c_end = block_end(lines, k_end)
        cbody = [l.strip() for l in lines[k_end + 1:c_end] if l.strip()]
        if cbody[-1] != f'throw {ex};' or cbody[-4:-1] != [f'if ({rname} != null) {{', f'{rname}.close();', '}']:
            return None
        nxt = first_stmt(lines, c_end + 1, b)
        if not is_close_if(lines[nxt:nxt + 3], rname):
            return None
        inner = parse_resources(lines, k + 1, k_end, ind + '    ', ex)
        if inner is None:
            return None
        res, body, rest_inner, body_ind = inner
        if any(l.strip() for l in rest_inner):
            return None
        return [(rtype, rname, init)] + res, body, lines[nxt + 3:b], body_ind
    return None

def transform(lines):
    for i in range(len(lines) - 4):
        m = re.match(r'^(\s*)Throwable (\w+) = null;$', lines[i])
        if not m or not re.match(r'^\s*Object \w+ = null;$', lines[i + 1]):
            continue
        ind, ex = m.group(1), m.group(2)
        k = first_stmt(lines, i + 2, len(lines))
        if lines[k] != ind + 'try {':
            continue
        try_end = block_end(lines, k)
        if not re.match(r'^\s*\} catch \(Throwable (\w+)\) \{$', lines[try_end]):
            continue
        catch_end = block_end(lines, try_end)
        if lines[catch_end - 1].strip() != f'throw {ex};':
            continue
        parsed = parse_resources(lines, k + 1, try_end, ind + '    ', ex)
        if parsed is None:
            print('  unmatched pattern at line', i + 1)
            continue
        res, body, rest, body_ind = parsed
        head = f'{ind}try (' + ('; '.join(f'{t} {n} = {v}' for t, n, v in res)) + ') {'
        rest = dedent(rest, 4)
        while rest and rest[0].strip() == '':
            rest = rest[1:]
        new = [head] + dedent(body, len(body_ind) - len(ind) - 4) + [ind + "}"]
        if rest:
            new += [''] + rest
        return lines[:i] + new + lines[catch_end + 1:], True
    return lines, False

for path in sys.argv[1:]:
    src = open(path, encoding='utf-8').read().split('\n')
    n = 0
    changed = True
    while changed:
        src, changed = transform(src)
        n += changed
    text = '\n'.join(src)
    text = re.sub(r'    // \$VF: Could not inline inconsistent finally blocks\n    // Please report this[^\n]*\n', '', text)
    open(path, 'w', encoding='utf-8').write(text)
    left = text.count('addSuppressed')
    print(f'{path}: {n} rewritten, {left} addSuppressed left')
