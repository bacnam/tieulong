/*    */ package jsc.swt.datatable;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import jsc.mathfunction.MathFunctionVariables;
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
/*    */ public class CalculatorVariables
/*    */   implements MathFunctionVariables
/*    */ {
/*    */   private int calcRow;
/*    */   DataTable dataTable;
/*    */   Vector names;
/*    */   
/*    */   public CalculatorVariables(DataTable paramDataTable) {
/* 29 */     this.names = paramDataTable.getColumnNames(true, true, false, 1);
/* 30 */     this.dataTable = paramDataTable;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Vector getNames() {
/* 38 */     return this.names;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumberOfVariables() {
/* 45 */     return this.names.size();
/*    */   }
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
/*    */   public String getVariableName(int paramInt) {
/* 58 */     return "\"" + (String)this.names.elementAt(paramInt) + "\"";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getVariableValue(int paramInt) {
/* 68 */     int i = this.dataTable.getColumnIndex(this.names.elementAt(paramInt));
/* 69 */     return this.dataTable.getNumericalValueAt(this.calcRow, i);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRowIndex(int paramInt) {
/* 79 */     this.calcRow = paramInt;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/CalculatorVariables.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */