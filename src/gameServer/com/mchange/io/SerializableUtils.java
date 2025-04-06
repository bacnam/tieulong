/*    */ package com.mchange.io;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SerializableUtils
/*    */ {
/*    */   public static final Object unmarshallObjectFromFile(File paramFile) throws IOException, ClassNotFoundException {
/* 47 */     ObjectInputStream objectInputStream = null;
/*    */     
/*    */     try {
/* 50 */       objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(paramFile)));
/* 51 */       return objectInputStream.readObject();
/*    */     } finally {
/*    */       
/* 54 */       InputStreamUtils.attemptClose(objectInputStream);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final void marshallObjectToFile(Object paramObject, File paramFile) throws IOException {
/* 59 */     ObjectOutputStream objectOutputStream = null;
/*    */     
/*    */     try {
/* 62 */       objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(paramFile)));
/* 63 */       objectOutputStream.writeObject(paramObject);
/*    */     } finally {
/*    */       
/* 66 */       OutputStreamUtils.attemptClose(objectOutputStream);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/SerializableUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */