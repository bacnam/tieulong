package bsh.org.objectweb.asm;

public interface CodeVisitor {
  void visitInsn(int paramInt);

  void visitIntInsn(int paramInt1, int paramInt2);

  void visitVarInsn(int paramInt1, int paramInt2);

  void visitTypeInsn(int paramInt, String paramString);

  void visitFieldInsn(int paramInt, String paramString1, String paramString2, String paramString3);

  void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3);

  void visitJumpInsn(int paramInt, Label paramLabel);

  void visitLabel(Label paramLabel);

  void visitLdcInsn(Object paramObject);

  void visitIincInsn(int paramInt1, int paramInt2);

  void visitTableSwitchInsn(int paramInt1, int paramInt2, Label paramLabel, Label[] paramArrayOfLabel);

  void visitLookupSwitchInsn(Label paramLabel, int[] paramArrayOfint, Label[] paramArrayOfLabel);

  void visitMultiANewArrayInsn(String paramString, int paramInt);

  void visitTryCatchBlock(Label paramLabel1, Label paramLabel2, Label paramLabel3, String paramString);

  void visitMaxs(int paramInt1, int paramInt2);

  void visitLocalVariable(String paramString1, String paramString2, Label paramLabel1, Label paramLabel2, int paramInt);

  void visitLineNumber(int paramInt, Label paramLabel);
}

