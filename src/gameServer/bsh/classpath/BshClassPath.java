/*     */ package bsh.classpath;
/*     */ 
/*     */ import bsh.ClassPathException;
/*     */ import bsh.NameSource;
/*     */ import bsh.StringUtil;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BshClassPath
/*     */   implements ClassPathListener, NameSource
/*     */ {
/*     */   String name;
/*     */   private List path;
/*     */   private List compPaths;
/*     */   private Map packageMap;
/*     */   private Map classSource;
/*     */   private boolean mapsInitialized;
/*     */   private UnqualifiedNameTable unqNameTable;
/*     */   private boolean nameCompletionIncludesUnqNames = true;
/*  91 */   Vector listeners = new Vector();
/*     */   static URL[] userClassPathComp;
/*     */   static BshClassPath userClassPath;
/*     */   
/*     */   public BshClassPath(String name) {
/*  96 */     this.name = name;
/*  97 */     reset();
/*     */   }
/*     */   static BshClassPath bootClassPath; List nameSourceListeners; static MappingFeedback mappingFeedbackListener;
/*     */   public BshClassPath(String name, URL[] urls) {
/* 101 */     this(name);
/* 102 */     add(urls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPath(URL[] urls) {
/* 110 */     reset();
/* 111 */     add(urls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComponent(BshClassPath bcp) {
/* 119 */     if (this.compPaths == null)
/* 120 */       this.compPaths = new ArrayList(); 
/* 121 */     this.compPaths.add(bcp);
/* 122 */     bcp.addListener(this);
/*     */   }
/*     */   
/*     */   public void add(URL[] urls) {
/* 126 */     this.path.addAll(Arrays.asList(urls));
/* 127 */     if (this.mapsInitialized)
/* 128 */       map(urls); 
/*     */   }
/*     */   
/*     */   public void add(URL url) throws IOException {
/* 132 */     this.path.add(url);
/* 133 */     if (this.mapsInitialized) {
/* 134 */       map(url);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public URL[] getPathComponents() {
/* 141 */     return (URL[])getFullPath().toArray((Object[])new URL[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Set getClassesForPackage(String pack) {
/* 149 */     insureInitialized();
/* 150 */     Set set = new HashSet();
/* 151 */     Collection c = (Collection)this.packageMap.get(pack);
/* 152 */     if (c != null) {
/* 153 */       set.addAll(c);
/*     */     }
/* 155 */     if (this.compPaths != null)
/* 156 */       for (int i = 0; i < this.compPaths.size(); i++) {
/* 157 */         c = ((BshClassPath)this.compPaths.get(i)).getClassesForPackage(pack);
/*     */         
/* 159 */         if (c != null)
/* 160 */           set.addAll(c); 
/*     */       }  
/* 162 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized ClassSource getClassSource(String className) {
/* 174 */     ClassSource cs = (ClassSource)this.classSource.get(className);
/* 175 */     if (cs != null) {
/* 176 */       return cs;
/*     */     }
/* 178 */     insureInitialized();
/*     */     
/* 180 */     cs = (ClassSource)this.classSource.get(className);
/* 181 */     if (cs == null && this.compPaths != null)
/* 182 */       for (int i = 0; i < this.compPaths.size() && cs == null; i++)
/* 183 */         cs = ((BshClassPath)this.compPaths.get(i)).getClassSource(className);  
/* 184 */     return cs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setClassSource(String className, ClassSource cs) {
/* 194 */     this.classSource.put(className, cs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insureInitialized() {
/* 222 */     insureInitialized(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void insureInitialized(boolean topPath) {
/* 233 */     if (topPath && !this.mapsInitialized) {
/* 234 */       startClassMapping();
/*     */     }
/*     */     
/* 237 */     if (this.compPaths != null) {
/* 238 */       for (int i = 0; i < this.compPaths.size(); i++) {
/* 239 */         ((BshClassPath)this.compPaths.get(i)).insureInitialized(false);
/*     */       }
/*     */     }
/* 242 */     if (!this.mapsInitialized) {
/* 243 */       map((URL[])this.path.toArray((Object[])new URL[0]));
/*     */     }
/* 245 */     if (topPath && !this.mapsInitialized) {
/* 246 */       endClassMapping();
/*     */     }
/* 248 */     this.mapsInitialized = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List getFullPath() {
/* 258 */     List<Object> list = new ArrayList();
/* 259 */     if (this.compPaths != null)
/* 260 */       for (int i = 0; i < this.compPaths.size(); i++) {
/* 261 */         List l = ((BshClassPath)this.compPaths.get(i)).getFullPath();
/*     */ 
/*     */         
/* 264 */         Iterator it = l.iterator();
/* 265 */         while (it.hasNext()) {
/* 266 */           Object o = it.next();
/* 267 */           if (!list.contains(o)) {
/* 268 */             list.add(o);
/*     */           }
/*     */         } 
/*     */       }  
/* 272 */     list.addAll(this.path);
/* 273 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassNameByUnqName(String name) throws ClassPathException {
/* 286 */     insureInitialized();
/* 287 */     UnqualifiedNameTable unqNameTable = getUnqualifiedNameTable();
/*     */     
/* 289 */     Object obj = unqNameTable.get(name);
/* 290 */     if (obj instanceof AmbiguousName) {
/* 291 */       throw new ClassPathException("Ambigous class names: " + ((AmbiguousName)obj).get());
/*     */     }
/*     */     
/* 294 */     return (String)obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UnqualifiedNameTable getUnqualifiedNameTable() {
/* 302 */     if (this.unqNameTable == null)
/* 303 */       this.unqNameTable = buildUnqualifiedNameTable(); 
/* 304 */     return this.unqNameTable;
/*     */   }
/*     */ 
/*     */   
/*     */   private UnqualifiedNameTable buildUnqualifiedNameTable() {
/* 309 */     UnqualifiedNameTable unqNameTable = new UnqualifiedNameTable();
/*     */ 
/*     */     
/* 312 */     if (this.compPaths != null) {
/* 313 */       for (int i = 0; i < this.compPaths.size(); i++) {
/* 314 */         Set s = ((BshClassPath)this.compPaths.get(i)).classSource.keySet();
/* 315 */         Iterator<String> iterator = s.iterator();
/* 316 */         while (iterator.hasNext()) {
/* 317 */           unqNameTable.add(iterator.next());
/*     */         }
/*     */       } 
/*     */     }
/* 321 */     Iterator<String> it = this.classSource.keySet().iterator();
/* 322 */     while (it.hasNext()) {
/* 323 */       unqNameTable.add(it.next());
/*     */     }
/* 325 */     return unqNameTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAllNames() {
/* 330 */     insureInitialized();
/*     */     
/* 332 */     List<K> names = new ArrayList();
/* 333 */     Iterator<String> it = getPackagesSet().iterator();
/* 334 */     while (it.hasNext()) {
/* 335 */       String pack = it.next();
/* 336 */       names.addAll(removeInnerClassNames(getClassesForPackage(pack)));
/*     */     } 
/*     */ 
/*     */     
/* 340 */     if (this.nameCompletionIncludesUnqNames) {
/* 341 */       names.addAll(getUnqualifiedNameTable().keySet());
/*     */     }
/* 343 */     return names.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void map(URL[] urls) {
/* 351 */     for (int i = 0; i < urls.length; i++) {
/*     */       try {
/* 353 */         map(urls[i]);
/* 354 */       } catch (IOException e) {
/* 355 */         String s = "Error constructing classpath: " + urls[i] + ": " + e;
/* 356 */         errorWhileMapping(s);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void map(URL url) throws IOException {
/* 363 */     String name = url.getFile();
/* 364 */     File f = new File(name);
/*     */     
/* 366 */     if (f.isDirectory()) {
/* 367 */       classMapping("Directory " + f.toString());
/* 368 */       map(traverseDirForClasses(f), new DirClassSource(f));
/* 369 */     } else if (isArchiveFileName(name)) {
/* 370 */       classMapping("Archive: " + url);
/* 371 */       map(searchJarForClasses(url), new JarClassSource(url));
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 378 */       String s = "Not a classpath component: " + name;
/* 379 */       errorWhileMapping(s);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void map(String[] classes, Object source) {
/* 384 */     for (int i = 0; i < classes.length; i++)
/*     */     {
/* 386 */       mapClass(classes[i], source);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void mapClass(String className, Object source) {
/* 393 */     String[] sa = splitClassname(className);
/* 394 */     String pack = sa[0];
/* 395 */     String clas = sa[1];
/* 396 */     Set<String> set = (Set)this.packageMap.get(pack);
/* 397 */     if (set == null) {
/* 398 */       set = new HashSet();
/* 399 */       this.packageMap.put(pack, set);
/*     */     } 
/* 401 */     set.add(className);
/*     */ 
/*     */     
/* 404 */     Object obj = this.classSource.get(className);
/*     */ 
/*     */     
/* 407 */     if (obj == null) {
/* 408 */       this.classSource.put(className, source);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void reset() {
/* 415 */     this.path = new ArrayList();
/* 416 */     this.compPaths = null;
/* 417 */     clearCachedStructures();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void clearCachedStructures() {
/* 424 */     this.mapsInitialized = false;
/* 425 */     this.packageMap = new HashMap<Object, Object>();
/* 426 */     this.classSource = new HashMap<Object, Object>();
/* 427 */     this.unqNameTable = null;
/* 428 */     nameSpaceChanged();
/*     */   }
/*     */   
/*     */   public void classPathChanged() {
/* 432 */     clearCachedStructures();
/* 433 */     notifyListeners();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String[] traverseDirForClasses(File dir) throws IOException {
/* 450 */     List list = traverseDirForClassesAux(dir, dir);
/* 451 */     return (String[])list.toArray((Object[])new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static List traverseDirForClassesAux(File topDir, File dir) throws IOException {
/* 457 */     List<String> list = new ArrayList();
/* 458 */     String top = topDir.getAbsolutePath();
/*     */     
/* 460 */     File[] children = dir.listFiles();
/* 461 */     for (int i = 0; i < children.length; i++) {
/* 462 */       File child = children[i];
/* 463 */       if (child.isDirectory()) {
/* 464 */         list.addAll(traverseDirForClassesAux(topDir, child));
/*     */       } else {
/* 466 */         String name = child.getAbsolutePath();
/* 467 */         if (isClassFileName(name)) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 472 */           if (name.startsWith(top)) {
/* 473 */             name = name.substring(top.length() + 1);
/*     */           } else {
/* 475 */             throw new IOException("problem parsing paths");
/*     */           } 
/* 477 */           name = canonicalizeClassName(name);
/* 478 */           list.add(name);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 484 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String[] searchJarForClasses(URL jar) throws IOException {
/* 493 */     Vector<String> v = new Vector();
/* 494 */     InputStream in = jar.openStream();
/* 495 */     ZipInputStream zin = new ZipInputStream(in);
/*     */     
/*     */     ZipEntry ze;
/* 498 */     while ((ze = zin.getNextEntry()) != null) {
/* 499 */       String name = ze.getName();
/* 500 */       if (isClassFileName(name))
/* 501 */         v.addElement(canonicalizeClassName(name)); 
/*     */     } 
/* 503 */     zin.close();
/*     */     
/* 505 */     String[] sa = new String[v.size()];
/* 506 */     v.copyInto((Object[])sa);
/* 507 */     return sa;
/*     */   }
/*     */   
/*     */   public static boolean isClassFileName(String name) {
/* 511 */     return name.toLowerCase().endsWith(".class");
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isArchiveFileName(String name) {
/* 516 */     name = name.toLowerCase();
/* 517 */     return (name.endsWith(".jar") || name.endsWith(".zip"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String canonicalizeClassName(String name) {
/* 528 */     String classname = name.replace('/', '.');
/* 529 */     classname = classname.replace('\\', '.');
/* 530 */     if (classname.startsWith("class "))
/* 531 */       classname = classname.substring(6); 
/* 532 */     if (classname.endsWith(".class"))
/* 533 */       classname = classname.substring(0, classname.length() - 6); 
/* 534 */     return classname;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] splitClassname(String classname) {
/*     */     String classn, packn;
/* 541 */     classname = canonicalizeClassName(classname);
/*     */     
/* 543 */     int i = classname.lastIndexOf(".");
/*     */     
/* 545 */     if (i == -1) {
/*     */       
/* 547 */       classn = classname;
/* 548 */       packn = "<unpackaged>";
/*     */     } else {
/* 550 */       packn = classname.substring(0, i);
/* 551 */       classn = classname.substring(i + 1);
/*     */     } 
/* 553 */     return new String[] { packn, classn };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Collection removeInnerClassNames(Collection col) {
/* 560 */     List list = new ArrayList();
/* 561 */     list.addAll(col);
/* 562 */     Iterator<String> it = list.iterator();
/* 563 */     while (it.hasNext()) {
/* 564 */       String name = it.next();
/* 565 */       if (name.indexOf("$") != -1)
/* 566 */         it.remove(); 
/*     */     } 
/* 568 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL[] getUserClassPathComponents() throws ClassPathException {
/* 580 */     if (userClassPathComp != null) {
/* 581 */       return userClassPathComp;
/*     */     }
/* 583 */     String cp = System.getProperty("java.class.path");
/* 584 */     String[] paths = StringUtil.split(cp, File.pathSeparator);
/*     */     
/* 586 */     URL[] urls = new URL[paths.length];
/*     */     try {
/* 588 */       for (int i = 0; i < paths.length; i++)
/*     */       {
/*     */ 
/*     */         
/* 592 */         urls[i] = (new File((new File(paths[i])).getCanonicalPath())).toURL();
/*     */       }
/* 594 */     } catch (IOException e) {
/* 595 */       throw new ClassPathException("can't parse class path: " + e);
/*     */     } 
/*     */     
/* 598 */     userClassPathComp = urls;
/* 599 */     return urls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set getPackagesSet() {
/* 607 */     insureInitialized();
/* 608 */     Set set = new HashSet();
/* 609 */     set.addAll(this.packageMap.keySet());
/*     */     
/* 611 */     if (this.compPaths != null)
/* 612 */       for (int i = 0; i < this.compPaths.size(); i++) {
/* 613 */         set.addAll(((BshClassPath)this.compPaths.get(i)).packageMap.keySet());
/*     */       } 
/* 615 */     return set;
/*     */   }
/*     */   
/*     */   public void addListener(ClassPathListener l) {
/* 619 */     this.listeners.addElement(new WeakReference<ClassPathListener>(l));
/*     */   }
/*     */   public void removeListener(ClassPathListener l) {
/* 622 */     this.listeners.removeElement(l);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void notifyListeners() {
/* 628 */     for (Enumeration<WeakReference> e = this.listeners.elements(); e.hasMoreElements(); ) {
/* 629 */       WeakReference<ClassPathListener> wr = e.nextElement();
/* 630 */       ClassPathListener l = wr.get();
/* 631 */       if (l == null) {
/* 632 */         this.listeners.removeElement(wr); continue;
/*     */       } 
/* 634 */       l.classPathChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BshClassPath getUserClassPath() throws ClassPathException {
/* 646 */     if (userClassPath == null) {
/* 647 */       userClassPath = new BshClassPath("User Class Path", getUserClassPathComponents());
/*     */     }
/* 649 */     return userClassPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BshClassPath getBootClassPath() throws ClassPathException {
/* 659 */     if (bootClassPath == null) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 664 */         String rtjar = getRTJarPath();
/* 665 */         URL url = (new File(rtjar)).toURL();
/* 666 */         bootClassPath = new BshClassPath("Boot Class Path", new URL[] { url });
/*     */       }
/* 668 */       catch (MalformedURLException e) {
/* 669 */         throw new ClassPathException(" can't find boot jar: " + e);
/*     */       } 
/*     */     }
/* 672 */     return bootClassPath;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getRTJarPath() {
/* 678 */     String urlString = Class.class.getResource("/java/lang/String.class").toExternalForm();
/*     */ 
/*     */     
/* 681 */     if (!urlString.startsWith("jar:file:")) {
/* 682 */       return null;
/*     */     }
/* 684 */     int i = urlString.indexOf("!");
/* 685 */     if (i == -1) {
/* 686 */       return null;
/*     */     }
/* 688 */     return urlString.substring("jar:file:".length(), i);
/*     */   }
/*     */   
/*     */   public static abstract class ClassSource {
/*     */     Object source;
/*     */     
/*     */     abstract byte[] getCode(String param1String); }
/*     */   
/*     */   public static class JarClassSource extends ClassSource {
/* 697 */     JarClassSource(URL url) { this.source = url; } public URL getURL() {
/* 698 */       return (URL)this.source;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte[] getCode(String className) {
/* 705 */       throw new Error("Unimplemented");
/*     */     } public String toString() {
/* 707 */       return "Jar: " + this.source;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DirClassSource extends ClassSource {
/* 712 */     DirClassSource(File dir) { this.source = dir; }
/* 713 */     public File getDir() { return (File)this.source; } public String toString() {
/* 714 */       return "Dir: " + this.source;
/*     */     }
/*     */     public byte[] getCode(String className) {
/* 717 */       return readBytesFromFile(getDir(), className);
/*     */     }
/*     */     
/*     */     public static byte[] readBytesFromFile(File base, String className) {
/*     */       byte[] bytes;
/* 722 */       String n = className.replace('.', File.separatorChar) + ".class";
/* 723 */       File file = new File(base, n);
/*     */       
/* 725 */       if (file == null || !file.exists()) {
/* 726 */         return null;
/*     */       }
/*     */       
/*     */       try {
/* 730 */         FileInputStream fis = new FileInputStream(file);
/* 731 */         DataInputStream dis = new DataInputStream(fis);
/*     */         
/* 733 */         bytes = new byte[(int)file.length()];
/*     */         
/* 735 */         dis.readFully(bytes);
/* 736 */         dis.close();
/* 737 */       } catch (IOException ie) {
/* 738 */         throw new RuntimeException("Couldn't load file: " + file);
/*     */       } 
/*     */       
/* 741 */       return bytes;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GeneratedClassSource
/*     */     extends ClassSource {
/*     */     GeneratedClassSource(byte[] bytecode) {
/* 748 */       this.source = bytecode;
/*     */     } public byte[] getCode(String className) {
/* 750 */       return (byte[])this.source;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 755 */     URL[] urls = new URL[args.length];
/* 756 */     for (int i = 0; i < args.length; i++)
/* 757 */       urls[i] = (new File(args[i])).toURL(); 
/* 758 */     BshClassPath bcp = new BshClassPath("Test", urls);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 762 */     return "BshClassPath " + this.name + "(" + super.toString() + ") path= " + this.path + "\n" + "compPaths = {" + this.compPaths + " }";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class UnqualifiedNameTable
/*     */     extends HashMap
/*     */   {
/*     */     void add(String fullname) {
/* 773 */       String name = BshClassPath.splitClassname(fullname)[1];
/* 774 */       Object have = get(name);
/*     */       
/* 776 */       if (have == null) {
/* 777 */         put((K)name, (V)fullname);
/*     */       }
/* 779 */       else if (have instanceof BshClassPath.AmbiguousName) {
/* 780 */         ((BshClassPath.AmbiguousName)have).add(fullname);
/*     */       } else {
/*     */         
/* 783 */         BshClassPath.AmbiguousName an = new BshClassPath.AmbiguousName();
/* 784 */         an.add((String)have);
/* 785 */         an.add(fullname);
/* 786 */         put((K)name, (V)an);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AmbiguousName {
/* 792 */     List list = new ArrayList();
/*     */     public void add(String name) {
/* 794 */       this.list.add(name);
/*     */     }
/*     */     
/*     */     public List get() {
/* 798 */       return this.list;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void nameSpaceChanged() {
/* 807 */     if (this.nameSourceListeners == null) {
/*     */       return;
/*     */     }
/* 810 */     for (int i = 0; i < this.nameSourceListeners.size(); i++) {
/* 811 */       ((NameSource.Listener)this.nameSourceListeners.get(i)).nameSourceChanged(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNameSourceListener(NameSource.Listener listener) {
/* 821 */     if (this.nameSourceListeners == null)
/* 822 */       this.nameSourceListeners = new ArrayList(); 
/* 823 */     this.nameSourceListeners.add(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addMappingFeedback(MappingFeedback mf) {
/* 833 */     if (mappingFeedbackListener != null)
/* 834 */       throw new RuntimeException("Unimplemented: already a listener"); 
/* 835 */     mappingFeedbackListener = mf;
/*     */   }
/*     */   
/*     */   void startClassMapping() {
/* 839 */     if (mappingFeedbackListener != null) {
/* 840 */       mappingFeedbackListener.startClassMapping();
/*     */     } else {
/* 842 */       System.err.println("Start ClassPath Mapping");
/*     */     } 
/*     */   }
/*     */   void classMapping(String msg) {
/* 846 */     if (mappingFeedbackListener != null) {
/* 847 */       mappingFeedbackListener.classMapping(msg);
/*     */     } else {
/* 849 */       System.err.println("Mapping: " + msg);
/*     */     } 
/*     */   }
/*     */   void errorWhileMapping(String s) {
/* 853 */     if (mappingFeedbackListener != null) {
/* 854 */       mappingFeedbackListener.errorWhileMapping(s);
/*     */     } else {
/* 856 */       System.err.println(s);
/*     */     } 
/*     */   }
/*     */   void endClassMapping() {
/* 860 */     if (mappingFeedbackListener != null) {
/* 861 */       mappingFeedbackListener.endClassMapping();
/*     */     } else {
/* 863 */       System.err.println("End ClassPath Mapping");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface MappingFeedback {
/*     */     void startClassMapping();
/*     */     
/*     */     void classMapping(String param1String);
/*     */     
/*     */     void errorWhileMapping(String param1String);
/*     */     
/*     */     void endClassMapping();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/classpath/BshClassPath.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */