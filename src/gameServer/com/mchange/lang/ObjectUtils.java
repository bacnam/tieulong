/*    */ package com.mchange.lang;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.NotSerializableException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ObjectUtils
/*    */ {
/* 48 */   public static final Object DUMMY_OBJECT = new Object();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte[] objectToByteArray(Object paramObject) throws NotSerializableException {
/*    */     try {
/* 55 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 56 */       ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
/* 57 */       objectOutputStream.writeObject(paramObject);
/* 58 */       return byteArrayOutputStream.toByteArray();
/*    */     }
/* 60 */     catch (NotSerializableException notSerializableException) {
/*    */ 
/*    */ 
/*    */       
/* 64 */       notSerializableException.fillInStackTrace();
/* 65 */       throw notSerializableException;
/*    */     }
/* 67 */     catch (IOException iOException) {
/*    */       
/* 69 */       iOException.printStackTrace();
/* 70 */       throw new Error("IOException writing to a byte array!");
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Object objectFromByteArray(byte[] paramArrayOfbyte) throws IOException, ClassNotFoundException {
/* 77 */     ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(paramArrayOfbyte));
/* 78 */     return objectInputStream.readObject();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/ObjectUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */