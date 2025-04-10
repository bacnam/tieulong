package bsh.classpath;

import bsh.ClassPathException;
import bsh.NameSource;
import bsh.StringUtil;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class BshClassPath
implements ClassPathListener, NameSource
{
String name;
private List path;
private List compPaths;
private Map packageMap;
private Map classSource;
private boolean mapsInitialized;
private UnqualifiedNameTable unqNameTable;
private boolean nameCompletionIncludesUnqNames = true;
Vector listeners = new Vector();
static URL[] userClassPathComp;
static BshClassPath userClassPath;

public BshClassPath(String name) {
this.name = name;
reset();
}
static BshClassPath bootClassPath; List nameSourceListeners; static MappingFeedback mappingFeedbackListener;
public BshClassPath(String name, URL[] urls) {
this(name);
add(urls);
}

public void setPath(URL[] urls) {
reset();
add(urls);
}

public void addComponent(BshClassPath bcp) {
if (this.compPaths == null)
this.compPaths = new ArrayList(); 
this.compPaths.add(bcp);
bcp.addListener(this);
}

public void add(URL[] urls) {
this.path.addAll(Arrays.asList(urls));
if (this.mapsInitialized)
map(urls); 
}

public void add(URL url) throws IOException {
this.path.add(url);
if (this.mapsInitialized) {
map(url);
}
}

public URL[] getPathComponents() {
return (URL[])getFullPath().toArray((Object[])new URL[0]);
}

public synchronized Set getClassesForPackage(String pack) {
insureInitialized();
Set set = new HashSet();
Collection c = (Collection)this.packageMap.get(pack);
if (c != null) {
set.addAll(c);
}
if (this.compPaths != null)
for (int i = 0; i < this.compPaths.size(); i++) {
c = ((BshClassPath)this.compPaths.get(i)).getClassesForPackage(pack);

if (c != null)
set.addAll(c); 
}  
return set;
}

public synchronized ClassSource getClassSource(String className) {
ClassSource cs = (ClassSource)this.classSource.get(className);
if (cs != null) {
return cs;
}
insureInitialized();

cs = (ClassSource)this.classSource.get(className);
if (cs == null && this.compPaths != null)
for (int i = 0; i < this.compPaths.size() && cs == null; i++)
cs = ((BshClassPath)this.compPaths.get(i)).getClassSource(className);  
return cs;
}

public synchronized void setClassSource(String className, ClassSource cs) {
this.classSource.put(className, cs);
}

public void insureInitialized() {
insureInitialized(true);
}

protected synchronized void insureInitialized(boolean topPath) {
if (topPath && !this.mapsInitialized) {
startClassMapping();
}

if (this.compPaths != null) {
for (int i = 0; i < this.compPaths.size(); i++) {
((BshClassPath)this.compPaths.get(i)).insureInitialized(false);
}
}
if (!this.mapsInitialized) {
map((URL[])this.path.toArray((Object[])new URL[0]));
}
if (topPath && !this.mapsInitialized) {
endClassMapping();
}
this.mapsInitialized = true;
}

protected List getFullPath() {
List<Object> list = new ArrayList();
if (this.compPaths != null)
for (int i = 0; i < this.compPaths.size(); i++) {
List l = ((BshClassPath)this.compPaths.get(i)).getFullPath();

Iterator it = l.iterator();
while (it.hasNext()) {
Object o = it.next();
if (!list.contains(o)) {
list.add(o);
}
} 
}  
list.addAll(this.path);
return list;
}

public String getClassNameByUnqName(String name) throws ClassPathException {
insureInitialized();
UnqualifiedNameTable unqNameTable = getUnqualifiedNameTable();

Object obj = unqNameTable.get(name);
if (obj instanceof AmbiguousName) {
throw new ClassPathException("Ambigous class names: " + ((AmbiguousName)obj).get());
}

return (String)obj;
}

private UnqualifiedNameTable getUnqualifiedNameTable() {
if (this.unqNameTable == null)
this.unqNameTable = buildUnqualifiedNameTable(); 
return this.unqNameTable;
}

private UnqualifiedNameTable buildUnqualifiedNameTable() {
UnqualifiedNameTable unqNameTable = new UnqualifiedNameTable();

if (this.compPaths != null) {
for (int i = 0; i < this.compPaths.size(); i++) {
Set s = ((BshClassPath)this.compPaths.get(i)).classSource.keySet();
Iterator<String> iterator = s.iterator();
while (iterator.hasNext()) {
unqNameTable.add(iterator.next());
}
} 
}
Iterator<String> it = this.classSource.keySet().iterator();
while (it.hasNext()) {
unqNameTable.add(it.next());
}
return unqNameTable;
}

public String[] getAllNames() {
insureInitialized();

List<K> names = new ArrayList();
Iterator<String> it = getPackagesSet().iterator();
while (it.hasNext()) {
String pack = it.next();
names.addAll(removeInnerClassNames(getClassesForPackage(pack)));
} 

if (this.nameCompletionIncludesUnqNames) {
names.addAll(getUnqualifiedNameTable().keySet());
}
return names.<String>toArray(new String[0]);
}

synchronized void map(URL[] urls) {
for (int i = 0; i < urls.length; i++) {
try {
map(urls[i]);
} catch (IOException e) {
String s = "Error constructing classpath: " + urls[i] + ": " + e;
errorWhileMapping(s);
} 
} 
}

synchronized void map(URL url) throws IOException {
String name = url.getFile();
File f = new File(name);

if (f.isDirectory()) {
classMapping("Directory " + f.toString());
map(traverseDirForClasses(f), new DirClassSource(f));
} else if (isArchiveFileName(name)) {
classMapping("Archive: " + url);
map(searchJarForClasses(url), new JarClassSource(url));

}
else {

String s = "Not a classpath component: " + name;
errorWhileMapping(s);
} 
}

private void map(String[] classes, Object source) {
for (int i = 0; i < classes.length; i++)
{
mapClass(classes[i], source);
}
}

private void mapClass(String className, Object source) {
String[] sa = splitClassname(className);
String pack = sa[0];
String clas = sa[1];
Set<String> set = (Set)this.packageMap.get(pack);
if (set == null) {
set = new HashSet();
this.packageMap.put(pack, set);
} 
set.add(className);

Object obj = this.classSource.get(className);

if (obj == null) {
this.classSource.put(className, source);
}
}

private synchronized void reset() {
this.path = new ArrayList();
this.compPaths = null;
clearCachedStructures();
}

private synchronized void clearCachedStructures() {
this.mapsInitialized = false;
this.packageMap = new HashMap<Object, Object>();
this.classSource = new HashMap<Object, Object>();
this.unqNameTable = null;
nameSpaceChanged();
}

public void classPathChanged() {
clearCachedStructures();
notifyListeners();
}

static String[] traverseDirForClasses(File dir) throws IOException {
List list = traverseDirForClassesAux(dir, dir);
return (String[])list.toArray((Object[])new String[0]);
}

static List traverseDirForClassesAux(File topDir, File dir) throws IOException {
List<String> list = new ArrayList();
String top = topDir.getAbsolutePath();

File[] children = dir.listFiles();
for (int i = 0; i < children.length; i++) {
File child = children[i];
if (child.isDirectory()) {
list.addAll(traverseDirForClassesAux(topDir, child));
} else {
String name = child.getAbsolutePath();
if (isClassFileName(name)) {

if (name.startsWith(top)) {
name = name.substring(top.length() + 1);
} else {
throw new IOException("problem parsing paths");
} 
name = canonicalizeClassName(name);
list.add(name);
} 
} 
} 

return list;
}

static String[] searchJarForClasses(URL jar) throws IOException {
Vector<String> v = new Vector();
InputStream in = jar.openStream();
ZipInputStream zin = new ZipInputStream(in);

ZipEntry ze;
while ((ze = zin.getNextEntry()) != null) {
String name = ze.getName();
if (isClassFileName(name))
v.addElement(canonicalizeClassName(name)); 
} 
zin.close();

String[] sa = new String[v.size()];
v.copyInto((Object[])sa);
return sa;
}

public static boolean isClassFileName(String name) {
return name.toLowerCase().endsWith(".class");
}

public static boolean isArchiveFileName(String name) {
name = name.toLowerCase();
return (name.endsWith(".jar") || name.endsWith(".zip"));
}

public static String canonicalizeClassName(String name) {
String classname = name.replace('/', '.');
classname = classname.replace('\\', '.');
if (classname.startsWith("class "))
classname = classname.substring(6); 
if (classname.endsWith(".class"))
classname = classname.substring(0, classname.length() - 6); 
return classname;
}

public static String[] splitClassname(String classname) {
String classn, packn;
classname = canonicalizeClassName(classname);

int i = classname.lastIndexOf(".");

if (i == -1) {

classn = classname;
packn = "<unpackaged>";
} else {
packn = classname.substring(0, i);
classn = classname.substring(i + 1);
} 
return new String[] { packn, classn };
}

public static Collection removeInnerClassNames(Collection col) {
List list = new ArrayList();
list.addAll(col);
Iterator<String> it = list.iterator();
while (it.hasNext()) {
String name = it.next();
if (name.indexOf("$") != -1)
it.remove(); 
} 
return list;
}

public static URL[] getUserClassPathComponents() throws ClassPathException {
if (userClassPathComp != null) {
return userClassPathComp;
}
String cp = System.getProperty("java.class.path");
String[] paths = StringUtil.split(cp, File.pathSeparator);

URL[] urls = new URL[paths.length];
try {
for (int i = 0; i < paths.length; i++)
{

urls[i] = (new File((new File(paths[i])).getCanonicalPath())).toURL();
}
} catch (IOException e) {
throw new ClassPathException("can't parse class path: " + e);
} 

userClassPathComp = urls;
return urls;
}

public Set getPackagesSet() {
insureInitialized();
Set set = new HashSet();
set.addAll(this.packageMap.keySet());

if (this.compPaths != null)
for (int i = 0; i < this.compPaths.size(); i++) {
set.addAll(((BshClassPath)this.compPaths.get(i)).packageMap.keySet());
} 
return set;
}

public void addListener(ClassPathListener l) {
this.listeners.addElement(new WeakReference<ClassPathListener>(l));
}
public void removeListener(ClassPathListener l) {
this.listeners.removeElement(l);
}

void notifyListeners() {
for (Enumeration<WeakReference> e = this.listeners.elements(); e.hasMoreElements(); ) {
WeakReference<ClassPathListener> wr = e.nextElement();
ClassPathListener l = wr.get();
if (l == null) {
this.listeners.removeElement(wr); continue;
} 
l.classPathChanged();
} 
}

public static BshClassPath getUserClassPath() throws ClassPathException {
if (userClassPath == null) {
userClassPath = new BshClassPath("User Class Path", getUserClassPathComponents());
}
return userClassPath;
}

public static BshClassPath getBootClassPath() throws ClassPathException {
if (bootClassPath == null) {

try {

String rtjar = getRTJarPath();
URL url = (new File(rtjar)).toURL();
bootClassPath = new BshClassPath("Boot Class Path", new URL[] { url });
}
catch (MalformedURLException e) {
throw new ClassPathException(" can't find boot jar: " + e);
} 
}
return bootClassPath;
}

private static String getRTJarPath() {
String urlString = Class.class.getResource("/java/lang/String.class").toExternalForm();

if (!urlString.startsWith("jar:file:")) {
return null;
}
int i = urlString.indexOf("!");
if (i == -1) {
return null;
}
return urlString.substring("jar:file:".length(), i);
}

public static abstract class ClassSource {
Object source;

abstract byte[] getCode(String param1String); }

public static class JarClassSource extends ClassSource {
JarClassSource(URL url) { this.source = url; } public URL getURL() {
return (URL)this.source;
}

public byte[] getCode(String className) {
throw new Error("Unimplemented");
} public String toString() {
return "Jar: " + this.source;
}
}

public static class DirClassSource extends ClassSource {
DirClassSource(File dir) { this.source = dir; }
public File getDir() { return (File)this.source; } public String toString() {
return "Dir: " + this.source;
}
public byte[] getCode(String className) {
return readBytesFromFile(getDir(), className);
}

public static byte[] readBytesFromFile(File base, String className) {
byte[] bytes;
String n = className.replace('.', File.separatorChar) + ".class";
File file = new File(base, n);

if (file == null || !file.exists()) {
return null;
}

try {
FileInputStream fis = new FileInputStream(file);
DataInputStream dis = new DataInputStream(fis);

bytes = new byte[(int)file.length()];

dis.readFully(bytes);
dis.close();
} catch (IOException ie) {
throw new RuntimeException("Couldn't load file: " + file);
} 

return bytes;
}
}

public static class GeneratedClassSource
extends ClassSource {
GeneratedClassSource(byte[] bytecode) {
this.source = bytecode;
} public byte[] getCode(String className) {
return (byte[])this.source;
}
}

public static void main(String[] args) throws Exception {
URL[] urls = new URL[args.length];
for (int i = 0; i < args.length; i++)
urls[i] = (new File(args[i])).toURL(); 
BshClassPath bcp = new BshClassPath("Test", urls);
}

public String toString() {
return "BshClassPath " + this.name + "(" + super.toString() + ") path= " + this.path + "\n" + "compPaths = {" + this.compPaths + " }";
}

static class UnqualifiedNameTable
extends HashMap
{
void add(String fullname) {
String name = BshClassPath.splitClassname(fullname)[1];
Object have = get(name);

if (have == null) {
put((K)name, (V)fullname);
}
else if (have instanceof BshClassPath.AmbiguousName) {
((BshClassPath.AmbiguousName)have).add(fullname);
} else {

BshClassPath.AmbiguousName an = new BshClassPath.AmbiguousName();
an.add((String)have);
an.add(fullname);
put((K)name, (V)an);
} 
}
}

public static class AmbiguousName {
List list = new ArrayList();
public void add(String name) {
this.list.add(name);
}

public List get() {
return this.list;
}
}

void nameSpaceChanged() {
if (this.nameSourceListeners == null) {
return;
}
for (int i = 0; i < this.nameSourceListeners.size(); i++) {
((NameSource.Listener)this.nameSourceListeners.get(i)).nameSourceChanged(this);
}
}

public void addNameSourceListener(NameSource.Listener listener) {
if (this.nameSourceListeners == null)
this.nameSourceListeners = new ArrayList(); 
this.nameSourceListeners.add(listener);
}

public static void addMappingFeedback(MappingFeedback mf) {
if (mappingFeedbackListener != null)
throw new RuntimeException("Unimplemented: already a listener"); 
mappingFeedbackListener = mf;
}

void startClassMapping() {
if (mappingFeedbackListener != null) {
mappingFeedbackListener.startClassMapping();
} else {
System.err.println("Start ClassPath Mapping");
} 
}
void classMapping(String msg) {
if (mappingFeedbackListener != null) {
mappingFeedbackListener.classMapping(msg);
} else {
System.err.println("Mapping: " + msg);
} 
}
void errorWhileMapping(String s) {
if (mappingFeedbackListener != null) {
mappingFeedbackListener.errorWhileMapping(s);
} else {
System.err.println(s);
} 
}
void endClassMapping() {
if (mappingFeedbackListener != null) {
mappingFeedbackListener.endClassMapping();
} else {
System.err.println("End ClassPath Mapping");
} 
}

public static interface MappingFeedback {
void startClassMapping();

void classMapping(String param1String);

void errorWhileMapping(String param1String);

void endClassMapping();
}
}

