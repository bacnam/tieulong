/*     */ package com.mchange.v2.ser;
/*     */ 
/*     */ import com.mchange.v1.io.InputStreamUtils;
/*     */ import com.mchange.v1.io.OutputStreamUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
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
/*     */ public final class SerializableUtils
/*     */ {
/*  44 */   static final MLogger logger = MLog.getLogger(SerializableUtils.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] toByteArray(Object paramObject) throws NotSerializableException {
/*  51 */     return serializeToByteArray(paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] toByteArray(Object paramObject, Indirector paramIndirector, IndirectPolicy paramIndirectPolicy) throws NotSerializableException {
/*     */     try {
/*  57 */       if (paramIndirectPolicy == IndirectPolicy.DEFINITELY_INDIRECT) {
/*     */         
/*  59 */         if (paramIndirector == null) {
/*  60 */           throw new IllegalArgumentException("null indirector is not consistent with " + paramIndirectPolicy);
/*     */         }
/*  62 */         IndirectlySerialized indirectlySerialized = paramIndirector.indirectForm(paramObject);
/*  63 */         return toByteArray(indirectlySerialized);
/*     */       } 
/*  65 */       if (paramIndirectPolicy == IndirectPolicy.INDIRECT_ON_EXCEPTION) {
/*     */         
/*  67 */         if (paramIndirector == null)
/*  68 */           throw new IllegalArgumentException("null indirector is not consistent with " + paramIndirectPolicy); 
/*     */         try {
/*  70 */           return toByteArray(paramObject);
/*  71 */         } catch (NotSerializableException notSerializableException) {
/*  72 */           return toByteArray(paramObject, paramIndirector, IndirectPolicy.DEFINITELY_INDIRECT);
/*     */         } 
/*  74 */       }  if (paramIndirectPolicy == IndirectPolicy.DEFINITELY_DIRECT) {
/*  75 */         return toByteArray(paramObject);
/*     */       }
/*  77 */       throw new InternalError("unknown indirecting policy: " + paramIndirectPolicy);
/*     */     }
/*  79 */     catch (NotSerializableException notSerializableException) {
/*  80 */       throw notSerializableException;
/*  81 */     } catch (Exception exception) {
/*     */ 
/*     */       
/*  84 */       if (logger.isLoggable(MLevel.WARNING))
/*  85 */         logger.log(MLevel.WARNING, "An Exception occurred while serializing an Object to a byte[] with an Indirector.", exception); 
/*  86 */       throw new NotSerializableException(exception.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] serializeToByteArray(Object paramObject) throws NotSerializableException {
/*     */     try {
/*  97 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*  98 */       ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
/*  99 */       objectOutputStream.writeObject(paramObject);
/* 100 */       return byteArrayOutputStream.toByteArray();
/*     */     }
/* 102 */     catch (NotSerializableException notSerializableException) {
/*     */ 
/*     */ 
/*     */       
/* 106 */       notSerializableException.fillInStackTrace();
/* 107 */       throw notSerializableException;
/*     */     }
/* 109 */     catch (IOException iOException) {
/*     */ 
/*     */       
/* 112 */       if (logger.isLoggable(MLevel.SEVERE))
/* 113 */         logger.log(MLevel.SEVERE, "An IOException occurred while writing into a ByteArrayOutputStream?!?", iOException); 
/* 114 */       throw new Error("IOException writing to a byte array!");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object fromByteArray(byte[] paramArrayOfbyte) throws IOException, ClassNotFoundException {
/* 123 */     Object object = deserializeFromByteArray(paramArrayOfbyte);
/* 124 */     if (object instanceof IndirectlySerialized) {
/* 125 */       return ((IndirectlySerialized)object).getObject();
/*     */     }
/* 127 */     return object;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object fromByteArray(byte[] paramArrayOfbyte, boolean paramBoolean) throws IOException, ClassNotFoundException {
/* 132 */     if (paramBoolean) {
/* 133 */       return deserializeFromByteArray(paramArrayOfbyte);
/*     */     }
/* 135 */     return fromByteArray(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object deserializeFromByteArray(byte[] paramArrayOfbyte) throws IOException, ClassNotFoundException {
/* 143 */     ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(paramArrayOfbyte));
/* 144 */     return objectInputStream.readObject();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object testSerializeDeserialize(Object paramObject) throws IOException, ClassNotFoundException {
/* 149 */     return deepCopy(paramObject);
/*     */   }
/*     */   
/*     */   public static Object deepCopy(Object paramObject) throws IOException, ClassNotFoundException {
/* 153 */     byte[] arrayOfByte = serializeToByteArray(paramObject);
/* 154 */     return deserializeFromByteArray(arrayOfByte);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Object unmarshallObjectFromFile(File paramFile) throws IOException, ClassNotFoundException {
/* 160 */     ObjectInputStream objectInputStream = null;
/*     */     
/*     */     try {
/* 163 */       objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(paramFile)));
/* 164 */       return objectInputStream.readObject();
/*     */     } finally {
/*     */       
/* 167 */       InputStreamUtils.attemptClose(objectInputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void marshallObjectToFile(Object paramObject, File paramFile) throws IOException {
/* 173 */     ObjectOutputStream objectOutputStream = null;
/*     */     
/*     */     try {
/* 176 */       objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(paramFile)));
/* 177 */       objectOutputStream.writeObject(paramObject);
/*     */     } finally {
/*     */       
/* 180 */       OutputStreamUtils.attemptClose(objectOutputStream);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/ser/SerializableUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */