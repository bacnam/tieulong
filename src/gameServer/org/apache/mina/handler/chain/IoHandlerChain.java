/*     */ package org.apache.mina.handler.chain;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.mina.core.session.IoSession;
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
/*     */ public class IoHandlerChain
/*     */   implements IoHandlerCommand
/*     */ {
/*  36 */   private static volatile int nextId = 0;
/*     */   
/*  38 */   private final int id = nextId++;
/*     */   
/*  40 */   private final String NEXT_COMMAND = IoHandlerChain.class.getName() + '.' + this.id + ".nextCommand";
/*     */   
/*  42 */   private final Map<String, Entry> name2entry = new ConcurrentHashMap<String, Entry>();
/*     */ 
/*     */   
/*     */   private final Entry head;
/*     */ 
/*     */   
/*     */   private final Entry tail;
/*     */ 
/*     */   
/*     */   public IoHandlerChain() {
/*  52 */     this.head = new Entry(null, null, "head", createHeadCommand());
/*  53 */     this.tail = new Entry(this.head, null, "tail", createTailCommand());
/*  54 */     this.head.nextEntry = this.tail;
/*     */   }
/*     */   
/*     */   private IoHandlerCommand createHeadCommand() {
/*  58 */     return new IoHandlerCommand() {
/*     */         public void execute(IoHandlerCommand.NextCommand next, IoSession session, Object message) throws Exception {
/*  60 */           next.execute(session, message);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private IoHandlerCommand createTailCommand() {
/*  66 */     return new IoHandlerCommand() {
/*     */         public void execute(IoHandlerCommand.NextCommand next, IoSession session, Object message) throws Exception {
/*  68 */           next = (IoHandlerCommand.NextCommand)session.getAttribute(IoHandlerChain.this.NEXT_COMMAND);
/*  69 */           if (next != null) {
/*  70 */             next.execute(session, message);
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Entry getEntry(String name) {
/*  77 */     Entry e = this.name2entry.get(name);
/*  78 */     if (e == null) {
/*  79 */       return null;
/*     */     }
/*  81 */     return e;
/*     */   }
/*     */   
/*     */   public IoHandlerCommand get(String name) {
/*  85 */     Entry e = getEntry(name);
/*  86 */     if (e == null) {
/*  87 */       return null;
/*     */     }
/*     */     
/*  90 */     return e.getCommand();
/*     */   }
/*     */   
/*     */   public IoHandlerCommand.NextCommand getNextCommand(String name) {
/*  94 */     Entry e = getEntry(name);
/*  95 */     if (e == null) {
/*  96 */       return null;
/*     */     }
/*     */     
/*  99 */     return e.getNextCommand();
/*     */   }
/*     */   
/*     */   public synchronized void addFirst(String name, IoHandlerCommand command) {
/* 103 */     checkAddable(name);
/* 104 */     register(this.head, name, command);
/*     */   }
/*     */   
/*     */   public synchronized void addLast(String name, IoHandlerCommand command) {
/* 108 */     checkAddable(name);
/* 109 */     register(this.tail.prevEntry, name, command);
/*     */   }
/*     */   
/*     */   public synchronized void addBefore(String baseName, String name, IoHandlerCommand command) {
/* 113 */     Entry baseEntry = checkOldName(baseName);
/* 114 */     checkAddable(name);
/* 115 */     register(baseEntry.prevEntry, name, command);
/*     */   }
/*     */   
/*     */   public synchronized void addAfter(String baseName, String name, IoHandlerCommand command) {
/* 119 */     Entry baseEntry = checkOldName(baseName);
/* 120 */     checkAddable(name);
/* 121 */     register(baseEntry, name, command);
/*     */   }
/*     */   
/*     */   public synchronized IoHandlerCommand remove(String name) {
/* 125 */     Entry entry = checkOldName(name);
/* 126 */     deregister(entry);
/* 127 */     return entry.getCommand();
/*     */   }
/*     */   
/*     */   public synchronized void clear() throws Exception {
/* 131 */     Iterator<String> it = (new ArrayList<String>(this.name2entry.keySet())).iterator();
/* 132 */     while (it.hasNext()) {
/* 133 */       remove(it.next());
/*     */     }
/*     */   }
/*     */   
/*     */   private void register(Entry prevEntry, String name, IoHandlerCommand command) {
/* 138 */     Entry newEntry = new Entry(prevEntry, prevEntry.nextEntry, name, command);
/* 139 */     prevEntry.nextEntry.prevEntry = newEntry;
/* 140 */     prevEntry.nextEntry = newEntry;
/*     */     
/* 142 */     this.name2entry.put(name, newEntry);
/*     */   }
/*     */   
/*     */   private void deregister(Entry entry) {
/* 146 */     Entry prevEntry = entry.prevEntry;
/* 147 */     Entry nextEntry = entry.nextEntry;
/* 148 */     prevEntry.nextEntry = nextEntry;
/* 149 */     nextEntry.prevEntry = prevEntry;
/*     */     
/* 151 */     this.name2entry.remove(entry.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Entry checkOldName(String baseName) {
/* 160 */     Entry e = this.name2entry.get(baseName);
/* 161 */     if (e == null) {
/* 162 */       throw new IllegalArgumentException("Unknown filter name:" + baseName);
/*     */     }
/* 164 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkAddable(String name) {
/* 171 */     if (this.name2entry.containsKey(name)) {
/* 172 */       throw new IllegalArgumentException("Other filter is using the same name '" + name + "'");
/*     */     }
/*     */   }
/*     */   
/*     */   public void execute(IoHandlerCommand.NextCommand next, IoSession session, Object message) throws Exception {
/* 177 */     if (next != null) {
/* 178 */       session.setAttribute(this.NEXT_COMMAND, next);
/*     */     }
/*     */     
/*     */     try {
/* 182 */       callNextCommand(this.head, session, message);
/*     */     } finally {
/* 184 */       session.removeAttribute(this.NEXT_COMMAND);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void callNextCommand(Entry entry, IoSession session, Object message) throws Exception {
/* 189 */     entry.getCommand().execute(entry.getNextCommand(), session, message);
/*     */   }
/*     */   
/*     */   public List<Entry> getAll() {
/* 193 */     List<Entry> list = new ArrayList<Entry>();
/* 194 */     Entry e = this.head.nextEntry;
/* 195 */     while (e != this.tail) {
/* 196 */       list.add(e);
/* 197 */       e = e.nextEntry;
/*     */     } 
/*     */     
/* 200 */     return list;
/*     */   }
/*     */   
/*     */   public List<Entry> getAllReversed() {
/* 204 */     List<Entry> list = new ArrayList<Entry>();
/* 205 */     Entry e = this.tail.prevEntry;
/* 206 */     while (e != this.head) {
/* 207 */       list.add(e);
/* 208 */       e = e.prevEntry;
/*     */     } 
/* 210 */     return list;
/*     */   }
/*     */   
/*     */   public boolean contains(String name) {
/* 214 */     return (getEntry(name) != null);
/*     */   }
/*     */   
/*     */   public boolean contains(IoHandlerCommand command) {
/* 218 */     Entry e = this.head.nextEntry;
/* 219 */     while (e != this.tail) {
/* 220 */       if (e.getCommand() == command) {
/* 221 */         return true;
/*     */       }
/* 223 */       e = e.nextEntry;
/*     */     } 
/* 225 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(Class<? extends IoHandlerCommand> commandType) {
/* 229 */     Entry e = this.head.nextEntry;
/* 230 */     while (e != this.tail) {
/* 231 */       if (commandType.isAssignableFrom(e.getCommand().getClass())) {
/* 232 */         return true;
/*     */       }
/* 234 */       e = e.nextEntry;
/*     */     } 
/* 236 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 241 */     StringBuilder buf = new StringBuilder();
/* 242 */     buf.append("{ ");
/*     */     
/* 244 */     boolean empty = true;
/*     */     
/* 246 */     Entry e = this.head.nextEntry;
/* 247 */     while (e != this.tail) {
/* 248 */       if (!empty) {
/* 249 */         buf.append(", ");
/*     */       } else {
/* 251 */         empty = false;
/*     */       } 
/*     */       
/* 254 */       buf.append('(');
/* 255 */       buf.append(e.getName());
/* 256 */       buf.append(':');
/* 257 */       buf.append(e.getCommand());
/* 258 */       buf.append(')');
/*     */       
/* 260 */       e = e.nextEntry;
/*     */     } 
/*     */     
/* 263 */     if (empty) {
/* 264 */       buf.append("empty");
/*     */     }
/*     */     
/* 267 */     buf.append(" }");
/*     */     
/* 269 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class Entry
/*     */   {
/*     */     private Entry prevEntry;
/*     */ 
/*     */     
/*     */     private Entry nextEntry;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final IoHandlerCommand command;
/*     */     
/*     */     private final IoHandlerCommand.NextCommand nextCommand;
/*     */ 
/*     */     
/*     */     private Entry(Entry prevEntry, Entry nextEntry, String name, IoHandlerCommand command) {
/* 289 */       if (command == null) {
/* 290 */         throw new IllegalArgumentException("command");
/*     */       }
/* 292 */       if (name == null) {
/* 293 */         throw new IllegalArgumentException("name");
/*     */       }
/*     */       
/* 296 */       this.prevEntry = prevEntry;
/* 297 */       this.nextEntry = nextEntry;
/* 298 */       this.name = name;
/* 299 */       this.command = command;
/* 300 */       this.nextCommand = new IoHandlerCommand.NextCommand() {
/*     */           public void execute(IoSession session, Object message) throws Exception {
/* 302 */             IoHandlerChain.Entry nextEntry = IoHandlerChain.Entry.this.nextEntry;
/* 303 */             IoHandlerChain.this.callNextCommand(nextEntry, session, message);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 312 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public IoHandlerCommand getCommand() {
/* 319 */       return this.command;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public IoHandlerCommand.NextCommand getNextCommand() {
/* 326 */       return this.nextCommand;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/handler/chain/IoHandlerChain.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */