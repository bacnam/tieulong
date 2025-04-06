/*    */ package javolution.util.internal.comparator;
/*    */ 
/*    */ import java.util.Arrays;
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
/*    */ public class ArrayComparatorImpl
/*    */   extends StandardComparatorImpl<Object>
/*    */ {
/*    */   private static final long serialVersionUID = 4134048629840904441L;
/*    */   
/*    */   public int hashCodeOf(Object array) {
/* 22 */     if (array instanceof Object[])
/* 23 */       return Arrays.deepHashCode((Object[])array); 
/* 24 */     if (array instanceof byte[])
/* 25 */       return Arrays.hashCode((byte[])array); 
/* 26 */     if (array instanceof short[])
/* 27 */       return Arrays.hashCode((short[])array); 
/* 28 */     if (array instanceof int[])
/* 29 */       return Arrays.hashCode((int[])array); 
/* 30 */     if (array instanceof long[])
/* 31 */       return Arrays.hashCode((long[])array); 
/* 32 */     if (array instanceof char[])
/* 33 */       return Arrays.hashCode((char[])array); 
/* 34 */     if (array instanceof float[])
/* 35 */       return Arrays.hashCode((float[])array); 
/* 36 */     if (array instanceof double[])
/* 37 */       return Arrays.hashCode((double[])array); 
/* 38 */     if (array instanceof boolean[])
/* 39 */       return Arrays.hashCode((boolean[])array); 
/* 40 */     if (array != null)
/* 41 */       return array.hashCode(); 
/* 42 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean areEqual(Object array1, Object array2) {
/* 47 */     if (array1 == array2)
/* 48 */       return true; 
/* 49 */     if (array1 == null || array2 == null)
/* 50 */       return false; 
/* 51 */     if (array1 instanceof Object[] && array2 instanceof Object[])
/* 52 */       return Arrays.deepEquals((Object[])array1, (Object[])array2); 
/* 53 */     if (array1 instanceof byte[] && array2 instanceof byte[])
/* 54 */       return Arrays.equals((byte[])array1, (byte[])array2); 
/* 55 */     if (array1 instanceof short[] && array2 instanceof short[])
/* 56 */       return Arrays.equals((short[])array1, (short[])array2); 
/* 57 */     if (array1 instanceof int[] && array2 instanceof int[])
/* 58 */       return Arrays.equals((int[])array1, (int[])array2); 
/* 59 */     if (array1 instanceof long[] && array2 instanceof long[])
/* 60 */       return Arrays.equals((long[])array1, (long[])array2); 
/* 61 */     if (array1 instanceof char[] && array2 instanceof char[])
/* 62 */       return Arrays.equals((char[])array1, (char[])array2); 
/* 63 */     if (array1 instanceof float[] && array2 instanceof float[])
/* 64 */       return Arrays.equals((float[])array1, (float[])array2); 
/* 65 */     if (array1 instanceof double[] && array2 instanceof double[])
/* 66 */       return Arrays.equals((double[])array1, (double[])array2); 
/* 67 */     if (array1 instanceof boolean[] && array2 instanceof boolean[])
/* 68 */       return Arrays.equals((boolean[])array1, (boolean[])array2); 
/* 69 */     return array1.equals(array2);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/comparator/ArrayComparatorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */