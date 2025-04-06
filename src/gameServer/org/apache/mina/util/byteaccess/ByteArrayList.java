/*     */ package org.apache.mina.util.byteaccess;
/*     */ 
/*     */ import java.util.NoSuchElementException;
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
/*     */ class ByteArrayList
/*     */ {
/*  54 */   private final Node header = new Node();
/*     */ 
/*     */   
/*     */   private int firstByte;
/*     */ 
/*     */   
/*     */   private int lastByte;
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastByte() {
/*  65 */     return this.lastByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int firstByte() {
/*  76 */     return this.firstByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  87 */     return (this.header.next == this.header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getFirst() {
/*  97 */     return this.header.getNextNode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getLast() {
/* 107 */     return this.header.getPreviousNode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFirst(ByteArray ba) {
/* 118 */     addNode(new Node(ba), this.header.next);
/* 119 */     this.firstByte -= ba.last();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLast(ByteArray ba) {
/* 130 */     addNode(new Node(ba), this.header);
/* 131 */     this.lastByte += ba.last();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node removeFirst() {
/* 141 */     Node node = this.header.getNextNode();
/* 142 */     this.firstByte += node.ba.last();
/* 143 */     return removeNode(node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node removeLast() {
/* 153 */     Node node = this.header.getPreviousNode();
/* 154 */     this.lastByte -= node.ba.last();
/* 155 */     return removeNode(node);
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
/*     */   protected void addNode(Node nodeToInsert, Node insertBeforeNode) {
/* 168 */     nodeToInsert.next = insertBeforeNode;
/* 169 */     nodeToInsert.previous = insertBeforeNode.previous;
/* 170 */     insertBeforeNode.previous.next = nodeToInsert;
/* 171 */     insertBeforeNode.previous = nodeToInsert;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Node removeNode(Node node) {
/* 181 */     node.previous.next = node.next;
/* 182 */     node.next.previous = node.previous;
/* 183 */     node.removed = true;
/* 184 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class Node
/*     */   {
/*     */     private Node previous;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Node next;
/*     */ 
/*     */ 
/*     */     
/*     */     private ByteArray ba;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean removed;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Node() {
/* 212 */       this.previous = this;
/* 213 */       this.next = this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Node(ByteArray ba) {
/* 222 */       if (ba == null) {
/* 223 */         throw new IllegalArgumentException("ByteArray must not be null.");
/*     */       }
/*     */       
/* 226 */       this.ba = ba;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Node getPreviousNode() {
/* 235 */       if (!hasPreviousNode()) {
/* 236 */         throw new NoSuchElementException();
/*     */       }
/* 238 */       return this.previous;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Node getNextNode() {
/* 247 */       if (!hasNextNode()) {
/* 248 */         throw new NoSuchElementException();
/*     */       }
/* 250 */       return this.next;
/*     */     }
/*     */     
/*     */     public boolean hasPreviousNode() {
/* 254 */       return (this.previous != ByteArrayList.this.header);
/*     */     }
/*     */     
/*     */     public boolean hasNextNode() {
/* 258 */       return (this.next != ByteArrayList.this.header);
/*     */     }
/*     */     
/*     */     public ByteArray getByteArray() {
/* 262 */       return this.ba;
/*     */     }
/*     */     
/*     */     public boolean isRemoved() {
/* 266 */       return this.removed;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/ByteArrayList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */