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
        // BEGIN (write your solution here)

        // END
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

 */
