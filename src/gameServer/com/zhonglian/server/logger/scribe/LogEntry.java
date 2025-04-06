/*     */ package com.zhonglian.server.logger.scribe;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.thrift.TBase;
/*     */ import org.apache.thrift.TBaseHelper;
/*     */ import org.apache.thrift.TException;
/*     */ import org.apache.thrift.TFieldIdEnum;
/*     */ import org.apache.thrift.meta_data.FieldMetaData;
/*     */ import org.apache.thrift.meta_data.FieldValueMetaData;
/*     */ import org.apache.thrift.protocol.TField;
/*     */ import org.apache.thrift.protocol.TProtocol;
/*     */ import org.apache.thrift.protocol.TProtocolUtil;
/*     */ import org.apache.thrift.protocol.TStruct;
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
/*     */ public class LogEntry
/*     */   implements TBase<LogEntry, LogEntry._Fields>, Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 3121543719267680503L;
/*  33 */   private static final TStruct STRUCT_DESC = new TStruct("LogEntry");
/*     */   
/*  35 */   private static final TField CATEGORY_FIELD_DESC = new TField("category", (byte)11, (short)1);
/*  36 */   private static final TField MESSAGE_FIELD_DESC = new TField("message", (byte)11, (short)2);
/*     */   public String category;
/*     */   public String message;
/*     */   public static final Map<_Fields, FieldMetaData> metaDataMap;
/*     */   
/*     */   public enum _Fields
/*     */     implements TFieldIdEnum {
/*  43 */     CATEGORY((short)1, "category"), MESSAGE((short)2, "message");
/*     */     
/*  45 */     private static final Map<String, _Fields> byName = new ConcurrentHashMap<>(); private final short _thriftId;
/*     */     
/*     */     static {
/*  48 */       for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/*  49 */         byName.put(field.getFieldName(), field);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private final String _fieldName;
/*     */     
/*     */     public static _Fields findByThriftId(int fieldId) {
/*  57 */       switch (fieldId) {
/*     */         case 1:
/*  59 */           return CATEGORY;
/*     */         case 2:
/*  61 */           return MESSAGE;
/*     */       } 
/*  63 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static _Fields findByThriftIdOrThrow(int fieldId) {
/*  72 */       _Fields fields = findByThriftId(fieldId);
/*  73 */       if (fields == null)
/*  74 */         throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/*  75 */       return fields;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static _Fields findByName(String name) {
/*  82 */       return byName.get(name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     _Fields(short thriftId, String fieldName) {
/*  89 */       this._thriftId = thriftId;
/*  90 */       this._fieldName = fieldName;
/*     */     }
/*     */     
/*     */     public short getThriftFieldId() {
/*  94 */       return this._thriftId;
/*     */     }
/*     */     
/*     */     public String getFieldName() {
/*  98 */       return this._fieldName;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 106 */     Map<_Fields, FieldMetaData> tmpMap = new EnumMap<>(_Fields.class);
/* 107 */     tmpMap.put(_Fields.CATEGORY, new FieldMetaData("category", (byte)3, new FieldValueMetaData((byte)11)));
/* 108 */     tmpMap.put(_Fields.MESSAGE, new FieldMetaData("message", (byte)3, new FieldValueMetaData((byte)11)));
/* 109 */     metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 110 */     FieldMetaData.addStructMetaDataMap(LogEntry.class, metaDataMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public LogEntry() {}
/*     */   
/*     */   public LogEntry(String category, String message) {
/* 117 */     this();
/* 118 */     this.category = category;
/* 119 */     this.message = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEntry(LogEntry other) {
/* 126 */     if (other.isSetCategory()) {
/* 127 */       this.category = other.category;
/*     */     }
/* 129 */     if (other.isSetMessage()) {
/* 130 */       this.message = other.message;
/*     */     }
/*     */   }
/*     */   
/*     */   public LogEntry deepCopy() {
/* 135 */     return new LogEntry(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 140 */     this.category = null;
/* 141 */     this.message = null;
/*     */   }
/*     */   
/*     */   public String getCategory() {
/* 145 */     return this.category;
/*     */   }
/*     */   
/*     */   public LogEntry setCategory(String category) {
/* 149 */     this.category = category;
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   public void unsetCategory() {
/* 154 */     this.category = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSetCategory() {
/* 159 */     return (this.category != null);
/*     */   }
/*     */   
/*     */   public void setCategoryIsSet(boolean value) {
/* 163 */     if (!value) {
/* 164 */       this.category = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getMessage() {
/* 169 */     return this.message;
/*     */   }
/*     */   
/*     */   public LogEntry setMessage(String message) {
/* 173 */     this.message = message;
/* 174 */     return this;
/*     */   }
/*     */   
/*     */   public void unsetMessage() {
/* 178 */     this.message = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSetMessage() {
/* 183 */     return (this.message != null);
/*     */   }
/*     */   
/*     */   public void setMessageIsSet(boolean value) {
/* 187 */     if (!value) {
/* 188 */       this.message = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setFieldValue(_Fields field, Object value) {
/* 193 */     switch (field) {
/*     */       case null:
/* 195 */         if (value == null) {
/* 196 */           unsetCategory(); break;
/*     */         } 
/* 198 */         setCategory((String)value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case MESSAGE:
/* 203 */         if (value == null) {
/* 204 */           unsetMessage(); break;
/*     */         } 
/* 206 */         setMessage((String)value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getFieldValue(_Fields field) {
/* 214 */     switch (field) {
/*     */       case null:
/* 216 */         return getCategory();
/*     */       
/*     */       case MESSAGE:
/* 219 */         return getMessage();
/*     */     } 
/*     */     
/* 222 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSet(_Fields field) {
/* 227 */     if (field == null) {
/* 228 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 231 */     switch (field) {
/*     */       case null:
/* 233 */         return isSetCategory();
/*     */       case MESSAGE:
/* 235 */         return isSetMessage();
/*     */     } 
/* 237 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object that) {
/* 242 */     if (that == null)
/* 243 */       return false; 
/* 244 */     if (that instanceof LogEntry)
/* 245 */       return equals((LogEntry)that); 
/* 246 */     return false;
/*     */   }
/*     */   
/*     */   public boolean equals(LogEntry that) {
/* 250 */     if (that == null) {
/* 251 */       return false;
/*     */     }
/* 253 */     boolean this_present_category = isSetCategory();
/* 254 */     boolean that_present_category = that.isSetCategory();
/* 255 */     if (this_present_category || that_present_category) {
/* 256 */       if (!this_present_category || !that_present_category)
/* 257 */         return false; 
/* 258 */       if (!this.category.equals(that.category)) {
/* 259 */         return false;
/*     */       }
/*     */     } 
/* 262 */     boolean this_present_message = isSetMessage();
/* 263 */     boolean that_present_message = that.isSetMessage();
/* 264 */     if (this_present_message || that_present_message) {
/* 265 */       if (!this_present_message || !that_present_message)
/* 266 */         return false; 
/* 267 */       if (!this.message.equals(that.message)) {
/* 268 */         return false;
/*     */       }
/*     */     } 
/* 271 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 276 */     return 0;
/*     */   }
/*     */   
/*     */   public int compareTo(LogEntry other) {
/* 280 */     if (!getClass().equals(other.getClass())) {
/* 281 */       return getClass().getName().compareTo(other.getClass().getName());
/*     */     }
/*     */     
/* 284 */     int lastComparison = 0;
/* 285 */     LogEntry typedOther = other;
/*     */     
/* 287 */     lastComparison = Boolean.valueOf(isSetCategory()).compareTo(Boolean.valueOf(typedOther.isSetCategory()));
/* 288 */     if (lastComparison != 0) {
/* 289 */       return lastComparison;
/*     */     }
/* 291 */     if (isSetCategory()) {
/* 292 */       lastComparison = TBaseHelper.compareTo(this.category, typedOther.category);
/* 293 */       if (lastComparison != 0) {
/* 294 */         return lastComparison;
/*     */       }
/*     */     } 
/* 297 */     lastComparison = Boolean.valueOf(isSetMessage()).compareTo(Boolean.valueOf(typedOther.isSetMessage()));
/* 298 */     if (lastComparison != 0) {
/* 299 */       return lastComparison;
/*     */     }
/* 301 */     if (isSetMessage()) {
/* 302 */       lastComparison = TBaseHelper.compareTo(this.message, typedOther.message);
/* 303 */       if (lastComparison != 0) {
/* 304 */         return lastComparison;
/*     */       }
/*     */     } 
/* 307 */     return 0;
/*     */   }
/*     */   
/*     */   public _Fields fieldForId(int fieldId) {
/* 311 */     return _Fields.findByThriftId(fieldId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(TProtocol iprot) throws TException {
/* 316 */     iprot.readStructBegin();
/*     */     while (true) {
/* 318 */       TField field = iprot.readFieldBegin();
/* 319 */       if (field.type == 0) {
/*     */         break;
/*     */       }
/* 322 */       switch (field.id) {
/*     */         case 1:
/* 324 */           if (field.type == 11) {
/* 325 */             this.category = iprot.readString(); break;
/*     */           } 
/* 327 */           TProtocolUtil.skip(iprot, field.type);
/*     */           break;
/*     */         
/*     */         case 2:
/* 331 */           if (field.type == 11) {
/* 332 */             this.message = iprot.readString(); break;
/*     */           } 
/* 334 */           TProtocolUtil.skip(iprot, field.type);
/*     */           break;
/*     */         
/*     */         default:
/* 338 */           TProtocolUtil.skip(iprot, field.type); break;
/*     */       } 
/* 340 */       iprot.readFieldEnd();
/*     */     } 
/* 342 */     iprot.readStructEnd();
/*     */ 
/*     */     
/* 345 */     validate();
/*     */   }
/*     */   
/*     */   public void write(TProtocol oprot) throws TException {
/* 349 */     validate();
/*     */     
/* 351 */     oprot.writeStructBegin(STRUCT_DESC);
/* 352 */     if (this.category != null) {
/* 353 */       oprot.writeFieldBegin(CATEGORY_FIELD_DESC);
/* 354 */       oprot.writeString(this.category);
/* 355 */       oprot.writeFieldEnd();
/*     */     } 
/* 357 */     if (this.message != null) {
/* 358 */       oprot.writeFieldBegin(MESSAGE_FIELD_DESC);
/* 359 */       oprot.writeString(this.message);
/* 360 */       oprot.writeFieldEnd();
/*     */     } 
/* 362 */     oprot.writeFieldStop();
/* 363 */     oprot.writeStructEnd();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 368 */     StringBuilder sb = new StringBuilder("LogEntry(");
/* 369 */     boolean first = true;
/*     */     
/* 371 */     sb.append("category:");
/* 372 */     if (this.category == null) {
/* 373 */       sb.append("null");
/*     */     } else {
/* 375 */       sb.append(this.category);
/*     */     } 
/* 377 */     first = false;
/* 378 */     if (!first)
/* 379 */       sb.append(", "); 
/* 380 */     sb.append("message:");
/* 381 */     if (this.message == null) {
/* 382 */       sb.append("null");
/*     */     } else {
/* 384 */       sb.append(this.message);
/*     */     } 
/* 386 */     first = false;
/* 387 */     sb.append(")");
/* 388 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public void validate() throws TException {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/logger/scribe/LogEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */