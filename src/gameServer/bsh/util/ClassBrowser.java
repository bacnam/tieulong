/*     */ package bsh.util;
/*     */ 
/*     */ import bsh.BshClassManager;
/*     */ import bsh.ClassPathException;
/*     */ import bsh.StringUtil;
/*     */ import bsh.classpath.BshClassPath;
/*     */ import bsh.classpath.ClassManagerImpl;
/*     */ import bsh.classpath.ClassPathListener;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Insets;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.MatteBorder;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import javax.swing.event.TreeSelectionEvent;
/*     */ import javax.swing.event.TreeSelectionListener;
/*     */ import javax.swing.plaf.SplitPaneUI;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneUI;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.DefaultTreeModel;
/*     */ import javax.swing.tree.MutableTreeNode;
/*     */ import javax.swing.tree.TreeModel;
/*     */ import javax.swing.tree.TreeNode;
/*     */ import javax.swing.tree.TreePath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassBrowser
/*     */   extends JSplitPane
/*     */   implements ListSelectionListener, ClassPathListener
/*     */ {
/*     */   BshClassPath classPath;
/*     */   BshClassManager classManager;
/*     */   JFrame frame;
/*     */   JInternalFrame iframe;
/*     */   JList classlist;
/*     */   JList conslist;
/*     */   JList mlist;
/*     */   JList fieldlist;
/*     */   PackageTree ptree;
/*     */   JTextArea methodLine;
/*     */   JTree tree;
/*     */   String[] packagesList;
/*     */   String[] classesList;
/*     */   Constructor[] consList;
/*     */   Method[] methodList;
/*     */   Field[] fieldList;
/*     */   String selectedPackage;
/*     */   Class selectedClass;
/*  82 */   private static final Color LIGHT_BLUE = new Color(245, 245, 255);
/*     */   
/*     */   public ClassBrowser() {
/*  85 */     this(BshClassManager.createClassManager(null));
/*     */   }
/*     */   
/*     */   public ClassBrowser(BshClassManager classManager) {
/*  89 */     super(0, true);
/*  90 */     this.classManager = classManager;
/*     */     
/*  92 */     setBorder((Border)null);
/*  93 */     SplitPaneUI ui = getUI();
/*  94 */     if (ui instanceof BasicSplitPaneUI) {
/*  95 */       ((BasicSplitPaneUI)ui).getDivider().setBorder(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   String[] toSortedStrings(Collection<?> c) {
/* 101 */     List l = new ArrayList(c);
/* 102 */     String[] sa = (String[])l.toArray((Object[])new String[0]);
/* 103 */     return StringUtil.bubbleSort(sa);
/*     */   }
/*     */   
/*     */   void setClist(String packagename) {
/* 107 */     this.selectedPackage = packagename;
/*     */     
/* 109 */     Set set = this.classPath.getClassesForPackage(packagename);
/* 110 */     if (set == null) {
/* 111 */       set = new HashSet();
/*     */     }
/*     */     
/* 114 */     List<String> list = new ArrayList();
/* 115 */     Iterator<String> it = set.iterator();
/* 116 */     while (it.hasNext()) {
/* 117 */       String cname = it.next();
/* 118 */       if (cname.indexOf("$") == -1) {
/* 119 */         list.add(BshClassPath.splitClassname(cname)[1]);
/*     */       }
/*     */     } 
/* 122 */     this.classesList = toSortedStrings(list);
/* 123 */     this.classlist.setListData(this.classesList);
/*     */   }
/*     */ 
/*     */   
/*     */   String[] parseConstructors(Constructor[] constructors) {
/* 128 */     String[] sa = new String[constructors.length];
/* 129 */     for (int i = 0; i < sa.length; i++) {
/* 130 */       Constructor con = constructors[i];
/* 131 */       sa[i] = StringUtil.methodString(con.getName(), con.getParameterTypes());
/*     */     } 
/*     */ 
/*     */     
/* 135 */     return sa;
/*     */   }
/*     */   
/*     */   String[] parseMethods(Method[] methods) {
/* 139 */     String[] sa = new String[methods.length];
/* 140 */     for (int i = 0; i < sa.length; i++) {
/* 141 */       sa[i] = StringUtil.methodString(methods[i].getName(), methods[i].getParameterTypes());
/*     */     }
/*     */     
/* 144 */     return sa;
/*     */   }
/*     */   
/*     */   String[] parseFields(Field[] fields) {
/* 148 */     String[] sa = new String[fields.length];
/* 149 */     for (int i = 0; i < sa.length; i++) {
/* 150 */       Field f = fields[i];
/* 151 */       sa[i] = f.getName();
/*     */     } 
/* 153 */     return sa;
/*     */   }
/*     */   
/*     */   Constructor[] getPublicConstructors(Constructor[] constructors) {
/* 157 */     Vector<Constructor> v = new Vector();
/* 158 */     for (int i = 0; i < constructors.length; i++) {
/* 159 */       if (Modifier.isPublic(constructors[i].getModifiers()))
/* 160 */         v.addElement(constructors[i]); 
/*     */     } 
/* 162 */     Constructor[] ca = new Constructor[v.size()];
/* 163 */     v.copyInto((Object[])ca);
/* 164 */     return ca;
/*     */   }
/*     */   
/*     */   Method[] getPublicMethods(Method[] methods) {
/* 168 */     Vector<Method> v = new Vector();
/* 169 */     for (int i = 0; i < methods.length; i++) {
/* 170 */       if (Modifier.isPublic(methods[i].getModifiers()))
/* 171 */         v.addElement(methods[i]); 
/*     */     } 
/* 173 */     Method[] ma = new Method[v.size()];
/* 174 */     v.copyInto((Object[])ma);
/* 175 */     return ma;
/*     */   }
/*     */   
/*     */   Field[] getPublicFields(Field[] fields) {
/* 179 */     Vector<Field> v = new Vector();
/* 180 */     for (int i = 0; i < fields.length; i++) {
/* 181 */       if (Modifier.isPublic(fields[i].getModifiers()))
/* 182 */         v.addElement(fields[i]); 
/*     */     } 
/* 184 */     Field[] fa = new Field[v.size()];
/* 185 */     v.copyInto((Object[])fa);
/* 186 */     return fa;
/*     */   }
/*     */   
/*     */   void setConslist(Class clas) {
/* 190 */     if (clas == null) {
/* 191 */       this.conslist.setListData(new Object[0]);
/*     */       
/*     */       return;
/*     */     } 
/* 195 */     this.consList = getPublicConstructors((Constructor[])clas.getDeclaredConstructors());
/* 196 */     this.conslist.setListData(parseConstructors(this.consList));
/*     */   }
/*     */ 
/*     */   
/*     */   void setMlist(String classname) {
/* 201 */     if (classname == null) {
/*     */       
/* 203 */       this.mlist.setListData(new Object[0]);
/* 204 */       setConslist((Class)null);
/* 205 */       setClassTree((Class)null);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/* 211 */       if (this.selectedPackage.equals("<unpackaged>")) {
/* 212 */         this.selectedClass = this.classManager.classForName(classname);
/*     */       } else {
/* 214 */         this.selectedClass = this.classManager.classForName(this.selectedPackage + "." + classname);
/*     */       } 
/* 216 */     } catch (Exception e) {
/* 217 */       System.err.println(e);
/*     */       return;
/*     */     } 
/* 220 */     if (this.selectedClass == null) {
/*     */       
/* 222 */       System.err.println("class not found: " + classname);
/*     */       return;
/*     */     } 
/* 225 */     this.methodList = getPublicMethods(this.selectedClass.getDeclaredMethods());
/* 226 */     this.mlist.setListData(parseMethods(this.methodList));
/*     */     
/* 228 */     setClassTree(this.selectedClass);
/* 229 */     setConslist(this.selectedClass);
/* 230 */     setFieldList(this.selectedClass);
/*     */   }
/*     */   
/*     */   void setFieldList(Class clas) {
/* 234 */     if (clas == null) {
/* 235 */       this.fieldlist.setListData(new Object[0]);
/*     */       
/*     */       return;
/*     */     } 
/* 239 */     this.fieldList = getPublicFields(clas.getDeclaredFields());
/* 240 */     this.fieldlist.setListData(parseFields(this.fieldList));
/*     */   }
/*     */   
/*     */   void setMethodLine(Object method) {
/* 244 */     this.methodLine.setText((method == null) ? "" : method.toString());
/*     */   }
/*     */   
/*     */   void setClassTree(Class clas) {
/* 248 */     if (clas == null) {
/* 249 */       this.tree.setModel((TreeModel)null);
/*     */       
/*     */       return;
/*     */     } 
/* 253 */     MutableTreeNode bottom = null, top = null;
/*     */     
/*     */     while (true) {
/* 256 */       DefaultMutableTreeNode up = new DefaultMutableTreeNode(clas.toString());
/* 257 */       if (top != null) {
/* 258 */         up.add(top);
/*     */       } else {
/* 260 */         bottom = up;
/* 261 */       }  top = up;
/* 262 */       if ((clas = clas.getSuperclass()) == null) {
/* 263 */         this.tree.setModel(new DefaultTreeModel(top));
/*     */         
/* 265 */         TreeNode tn = bottom.getParent();
/* 266 */         if (tn != null) {
/* 267 */           TreePath tp = new TreePath((Object[])((DefaultTreeModel)this.tree.getModel()).getPathToRoot(tn));
/*     */           
/* 269 */           this.tree.expandPath(tp);
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     }  } JPanel labeledPane(JComponent comp, String label) {
/* 274 */     JPanel jp = new JPanel(new BorderLayout());
/* 275 */     jp.add("Center", comp);
/* 276 */     jp.add("North", new JLabel(label, 0));
/* 277 */     return jp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() throws ClassPathException {
/* 284 */     this.classPath = ((ClassManagerImpl)this.classManager).getClassPath();
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
/* 302 */     this.classPath.addListener(this);
/*     */     
/* 304 */     Set pset = this.classPath.getPackagesSet();
/*     */     
/* 306 */     this.ptree = new PackageTree(pset);
/* 307 */     this.ptree.addTreeSelectionListener(new TreeSelectionListener() {
/*     */           public void valueChanged(TreeSelectionEvent e) {
/* 309 */             TreePath tp = e.getPath();
/* 310 */             Object[] oa = tp.getPath();
/* 311 */             StringBuffer selectedPackage = new StringBuffer();
/* 312 */             for (int i = 1; i < oa.length; i++) {
/* 313 */               selectedPackage.append(oa[i].toString());
/* 314 */               if (i + 1 < oa.length)
/* 315 */                 selectedPackage.append("."); 
/*     */             } 
/* 317 */             ClassBrowser.this.setClist(selectedPackage.toString());
/*     */           }
/*     */         });
/*     */     
/* 321 */     this.classlist = new JList();
/* 322 */     this.classlist.setBackground(LIGHT_BLUE);
/* 323 */     this.classlist.addListSelectionListener(this);
/*     */     
/* 325 */     this.conslist = new JList();
/* 326 */     this.conslist.addListSelectionListener(this);
/*     */     
/* 328 */     this.mlist = new JList();
/* 329 */     this.mlist.setBackground(LIGHT_BLUE);
/* 330 */     this.mlist.addListSelectionListener(this);
/*     */     
/* 332 */     this.fieldlist = new JList();
/* 333 */     this.fieldlist.addListSelectionListener(this);
/*     */     
/* 335 */     JSplitPane methodConsPane = splitPane(0, true, labeledPane(new JScrollPane(this.conslist), "Constructors"), labeledPane(new JScrollPane(this.mlist), "Methods"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     JSplitPane rightPane = splitPane(0, true, methodConsPane, labeledPane(new JScrollPane(this.fieldlist), "Fields"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 346 */     JSplitPane sp = splitPane(1, true, labeledPane(new JScrollPane(this.classlist), "Classes"), rightPane);
/*     */ 
/*     */ 
/*     */     
/* 350 */     sp = splitPane(1, true, labeledPane(new JScrollPane(this.ptree), "Packages"), sp);
/*     */ 
/*     */ 
/*     */     
/* 354 */     JPanel bottompanel = new JPanel(new BorderLayout());
/* 355 */     this.methodLine = new JTextArea(1, 60);
/* 356 */     this.methodLine.setBackground(LIGHT_BLUE);
/* 357 */     this.methodLine.setEditable(false);
/* 358 */     this.methodLine.setLineWrap(true);
/* 359 */     this.methodLine.setWrapStyleWord(true);
/* 360 */     this.methodLine.setFont(new Font("Monospaced", 1, 14));
/* 361 */     this.methodLine.setMargin(new Insets(5, 5, 5, 5));
/* 362 */     this.methodLine.setBorder(new MatteBorder(1, 0, 1, 0, LIGHT_BLUE.darker().darker()));
/*     */     
/* 364 */     bottompanel.add("North", this.methodLine);
/* 365 */     JPanel p = new JPanel(new BorderLayout());
/*     */     
/* 367 */     this.tree = new JTree();
/* 368 */     this.tree.addTreeSelectionListener(new TreeSelectionListener() {
/*     */           public void valueChanged(TreeSelectionEvent e) {
/* 370 */             ClassBrowser.this.driveToClass(e.getPath().getLastPathComponent().toString());
/*     */           }
/*     */         });
/*     */     
/* 374 */     this.tree.setBorder(BorderFactory.createRaisedBevelBorder());
/* 375 */     setClassTree((Class)null);
/* 376 */     p.add("Center", this.tree);
/* 377 */     bottompanel.add("Center", p);
/*     */ 
/*     */     
/* 380 */     bottompanel.setPreferredSize(new Dimension(150, 150));
/*     */     
/* 382 */     setTopComponent(sp);
/* 383 */     setBottomComponent(bottompanel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JSplitPane splitPane(int orientation, boolean redraw, JComponent c1, JComponent c2) {
/* 392 */     JSplitPane sp = new JSplitPane(orientation, redraw, c1, c2);
/* 393 */     sp.setBorder((Border)null);
/* 394 */     SplitPaneUI ui = sp.getUI();
/* 395 */     if (ui instanceof BasicSplitPaneUI) {
/* 396 */       ((BasicSplitPaneUI)ui).getDivider().setBorder(null);
/*     */     }
/*     */     
/* 399 */     return sp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 405 */     ClassBrowser cb = new ClassBrowser();
/* 406 */     cb.init();
/*     */     
/* 408 */     JFrame f = new JFrame("BeanShell Class Browser v1.0");
/* 409 */     f.getContentPane().add("Center", cb);
/* 410 */     cb.setFrame(f);
/* 411 */     f.pack();
/* 412 */     f.setVisible(true);
/*     */   }
/*     */   
/*     */   public void setFrame(JFrame frame) {
/* 416 */     this.frame = frame;
/*     */   }
/*     */   public void setFrame(JInternalFrame frame) {
/* 419 */     this.iframe = frame;
/*     */   }
/*     */ 
/*     */   
/*     */   public void valueChanged(ListSelectionEvent e) {
/* 424 */     if (e.getSource() == this.classlist) {
/*     */       
/* 426 */       String methodLineString, classname = this.classlist.getSelectedValue();
/* 427 */       setMlist(classname);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 432 */       if (classname == null) {
/* 433 */         methodLineString = "Package: " + this.selectedPackage;
/*     */       } else {
/*     */         
/* 436 */         String fullClassName = this.selectedPackage.equals("<unpackaged>") ? classname : (this.selectedPackage + "." + classname);
/*     */ 
/*     */         
/* 439 */         methodLineString = fullClassName + " (from " + this.classPath.getClassSource(fullClassName) + ")";
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 444 */       setMethodLine(methodLineString);
/*     */     
/*     */     }
/* 447 */     else if (e.getSource() == this.mlist) {
/*     */       
/* 449 */       int i = this.mlist.getSelectedIndex();
/* 450 */       if (i == -1) {
/* 451 */         setMethodLine((Object)null);
/*     */       } else {
/* 453 */         setMethodLine(this.methodList[i]);
/*     */       }
/*     */     
/* 456 */     } else if (e.getSource() == this.conslist) {
/*     */       
/* 458 */       int i = this.conslist.getSelectedIndex();
/* 459 */       if (i == -1) {
/* 460 */         setMethodLine((Object)null);
/*     */       } else {
/* 462 */         setMethodLine(this.consList[i]);
/*     */       }
/*     */     
/* 465 */     } else if (e.getSource() == this.fieldlist) {
/*     */       
/* 467 */       int i = this.fieldlist.getSelectedIndex();
/* 468 */       if (i == -1) {
/* 469 */         setMethodLine((Object)null);
/*     */       } else {
/* 471 */         setMethodLine(this.fieldList[i]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void driveToClass(String classname) {
/* 477 */     String[] sa = BshClassPath.splitClassname(classname);
/* 478 */     String packn = sa[0];
/* 479 */     String classn = sa[1];
/*     */ 
/*     */     
/* 482 */     if (this.classPath.getClassesForPackage(packn).size() == 0) {
/*     */       return;
/*     */     }
/* 485 */     this.ptree.setSelectedPackage(packn);
/*     */     
/* 487 */     for (int i = 0; i < this.classesList.length; i++) {
/* 488 */       if (this.classesList[i].equals(classn)) {
/* 489 */         this.classlist.setSelectedIndex(i);
/* 490 */         this.classlist.ensureIndexIsVisible(i);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void toFront() {
/* 497 */     if (this.frame != null) {
/* 498 */       this.frame.toFront();
/*     */     }
/* 500 */     else if (this.iframe != null) {
/* 501 */       this.iframe.toFront();
/*     */     } 
/*     */   }
/*     */   
/*     */   class PackageTree extends JTree {
/*     */     TreeNode root;
/*     */     DefaultTreeModel treeModel;
/* 508 */     Map nodeForPackage = new HashMap<Object, Object>();
/*     */     
/*     */     PackageTree(Collection packages) {
/* 511 */       setPackages(packages);
/*     */       
/* 513 */       setRootVisible(false);
/* 514 */       setShowsRootHandles(true);
/* 515 */       setExpandsSelectedPaths(true);
/*     */     }
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
/*     */     public void setPackages(Collection packages) {
/* 529 */       this.treeModel = makeTreeModel(packages);
/* 530 */       setModel(this.treeModel);
/*     */     }
/*     */ 
/*     */     
/*     */     DefaultTreeModel makeTreeModel(Collection packages) {
/* 535 */       Map<Object, Object> packageTree = new HashMap<Object, Object>();
/*     */       
/* 537 */       Iterator<String> it = packages.iterator();
/* 538 */       while (it.hasNext()) {
/* 539 */         String pack = it.next();
/* 540 */         String[] sa = StringUtil.split(pack, ".");
/* 541 */         Map<Object, Object> level = packageTree;
/* 542 */         for (int i = 0; i < sa.length; i++) {
/* 543 */           String name = sa[i];
/* 544 */           Map<Object, Object> map = (Map)level.get(name);
/*     */           
/* 546 */           if (map == null) {
/* 547 */             map = new HashMap<Object, Object>();
/* 548 */             level.put(name, map);
/*     */           } 
/* 550 */           level = map;
/*     */         } 
/*     */       } 
/*     */       
/* 554 */       this.root = makeNode(packageTree, "root");
/* 555 */       mapNodes(this.root);
/* 556 */       return new DefaultTreeModel(this.root);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     MutableTreeNode makeNode(Map map, String nodeName) {
/* 562 */       DefaultMutableTreeNode root = new DefaultMutableTreeNode(nodeName);
/*     */       
/* 564 */       Iterator<String> it = map.keySet().iterator();
/* 565 */       while (it.hasNext()) {
/* 566 */         String name = it.next();
/* 567 */         Map val = (Map)map.get(name);
/* 568 */         if (val.size() == 0) {
/* 569 */           DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(name);
/*     */           
/* 571 */           root.add(leaf); continue;
/*     */         } 
/* 573 */         MutableTreeNode node = makeNode(val, name);
/* 574 */         root.add(node);
/*     */       } 
/*     */       
/* 577 */       return root;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void mapNodes(TreeNode node) {
/* 586 */       addNodeMap(node);
/*     */       
/* 588 */       Enumeration<TreeNode> e = node.children();
/* 589 */       while (e.hasMoreElements()) {
/* 590 */         TreeNode tn = e.nextElement();
/* 591 */         mapNodes(tn);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void addNodeMap(TreeNode node) {
/* 600 */       StringBuffer sb = new StringBuffer();
/* 601 */       TreeNode tn = node;
/* 602 */       while (tn != this.root) {
/* 603 */         sb.insert(0, tn.toString());
/* 604 */         if (tn.getParent() != this.root)
/* 605 */           sb.insert(0, "."); 
/* 606 */         tn = tn.getParent();
/*     */       } 
/* 608 */       String pack = sb.toString();
/*     */       
/* 610 */       this.nodeForPackage.put(pack, node);
/*     */     }
/*     */     
/*     */     void setSelectedPackage(String pack) {
/* 614 */       DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.nodeForPackage.get(pack);
/*     */       
/* 616 */       if (node == null) {
/*     */         return;
/*     */       }
/* 619 */       TreePath tp = new TreePath((Object[])this.treeModel.getPathToRoot(node));
/* 620 */       setSelectionPath(tp);
/* 621 */       ClassBrowser.this.setClist(pack);
/*     */       
/* 623 */       scrollPathToVisible(tp);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void classPathChanged() {
/* 629 */     Set pset = this.classPath.getPackagesSet();
/* 630 */     this.ptree.setPackages(pset);
/* 631 */     setClist((String)null);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/ClassBrowser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */