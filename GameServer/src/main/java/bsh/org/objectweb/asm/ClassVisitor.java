package bsh.org.objectweb.asm;

public interface ClassVisitor {
    void visit(int paramInt, String paramString1, String paramString2, String[] paramArrayOfString, String paramString3);

    void visitInnerClass(String paramString1, String paramString2, String paramString3, int paramInt);

    void visitField(int paramInt, String paramString1, String paramString2, Object paramObject);

    CodeVisitor visitMethod(int paramInt, String paramString1, String paramString2, String[] paramArrayOfString);

    void visitEnd();
}

