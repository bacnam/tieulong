import sys
ROOT = sys.argv[1]
def sub(path, old, new, count=1):
    path = f'{ROOT}/{path}'
    s = open(path, encoding='utf-8').read()
    n = s.count(old)
    assert n >= 1, (path, old)
    s = s.replace(old, new) if count == 0 else s.replace(old, new, count)
    open(path, 'w', encoding='utf-8').write(s)
B = 'com/zhonglian/server/'
sub(B+'common/data/AbstractRefDataMgr.java', "for (Field f : keyvalue = clazz.getFields()) {", "for (Field f : clazz.getFields()) {")
sub(B+'common/data/AbstractRefDataMgr.java', "return Enum.valueOf(field, value);", "return Enum.valueOf((Class) field, value);")
sub(B+'common/db/BM.java', "new BM.AsynCall_Find<>(key, value)", "this.new AsynCall_Find<M>(key, value)", 0)
sub(B+'common/db/BM.java', "            return BM.this.findAll(this.key, this.value);", "            return (List<M>) (List<?>) BM.this.findAll(this.key, this.value);")
sub(B+'logger/scribe/Scribe.java', "import com.facebook.fb303.FacebookService.Processor.ProcessFunction;\n", "")
sub(B+'logger/scribe/Scribe.java', "new Scribe.Processor.Log(null)", "new Log()")
print('common fixes ok')
