package exercises.forLoop;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;
import java.io.FileOutputStream;

public class ClassGen {

    public static void main(final String... args) throws Exception {
        final String path = args[0];
        final byte[] byteCode = new ClassGen().generateMathClass();
        try (FileOutputStream stream = new FileOutputStream(path)) {
            stream.write(byteCode);
        }
    }

    private byte[] generateMathClass() {
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(51,
                Opcodes.ACC_PUBLIC,
                "Math",
                null,
                "java/lang/Object",
                null);
        generateDefaultConstructor(cw);
        generateSumMethod(cw);
        cw.visitEnd();
        return cw.toByteArray();
    }

    private void generateDefaultConstructor(final ClassWriter cw) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private void generateSumMethod(final ClassWriter cw) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "sum", // method name
                "(I)I", // method descriptor
                null,    // exceptions
                null);   // method attributes

        Label forLabel = new Label();
        Label label = new Label();
        mv.visitCode();
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitVarInsn(Opcodes.ISTORE, 1);
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitVarInsn(Opcodes.ISTORE, 2);
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitVarInsn(Opcodes.ISTORE, 3);

        mv.visitLabel(forLabel);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitJumpInsn(Opcodes.IF_ICMPGE, label);

        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitInsn(Opcodes.IADD);
        mv.visitVarInsn(Opcodes.ILOAD, 3);
        mv.visitInsn(Opcodes.IADD);
        mv.visitVarInsn(Opcodes.ISTORE, 1);
        mv.visitIincInsn(2, 1);
        mv.visitJumpInsn(Opcodes.GOTO, forLabel);

        mv.visitLabel(label);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(4, 4);
    }

}


/*

Предисловие

Так как Вам уже не в первой, то текущее задание будет содержать минимум подсказок, поехали ;)

Задание

Java Код, который Вам предстоит преобразить в байт-код:

public static int sum(final int a) {
    int result = 0;
    for (int i = 0; i < a; i++) {
        result += (i + 1);
    }
    return result;
}
Собственно и все =) Остальное Вам уже должно быт известно. Задание не из легких и колличество байт-кода может
быть больше чем в прошлых работах, но мы верим в то что все получиться. До встречи в следующем уроке!

Hexlet solution:

// BEGIN
        // int result = 0;
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitVarInsn(Opcodes.ISTORE, 1);

        // int i = 0;
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitVarInsn(Opcodes.ISTORE, 2);

        // i <= a;
        mv.visitLabel(forLabel);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitJumpInsn(Opcodes.IF_ICMPGE, label);


        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitInsn(Opcodes.ICONST_1);
        // (i + 1)
        mv.visitInsn(Opcodes.IADD);
        // result + (i + 1)
        mv.visitInsn(Opcodes.IADD);
        // result = result + (i + 1);
        mv.visitVarInsn(Opcodes.ISTORE, 1);

        // i++;
        mv.visitIincInsn(2, 1);
        mv.visitJumpInsn(Opcodes.GOTO, forLabel);

        mv.visitLabel(label);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(3, 3);
        // END


*/
