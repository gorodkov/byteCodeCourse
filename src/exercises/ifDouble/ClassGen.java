package exercises.ifDouble;

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
        generateDMethod(cw);
        generateAbsMethod(cw);
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

    private void generateDMethod(final ClassWriter cw) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "d", // method name
                "(DDD)D", // method descriptor
                null,    // exceptions
                null);   // method attributes
        mv.visitCode();
        // BEGIN (write your solution here)

        // END
        mv.visitEnd();
    }

    private void generateAbsMethod(final ClassWriter cw ) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "abs",    // method name
                "(D)D", // method descriptor
                null,     // exceptions
                null);    // method attributes
        mv.visitCode();
        final Label elseLable = new Label();
        // BEGIN (write your solution here)

        // END
        mv.visitEnd();
    }

}

/*

Предисловие

Несмотря на то, что работа с double может на первый взгляд показаться очень похожей на работу с int,
на самом деле они достаточно различны.

Задание

Java Код, который Вам предстоит преобразить в байт-код:

public class Math {

    public static double d(final double a, final double b, final double c) {
        return b * b - 4 * a * c;
    }

    public static double abs(final double a) {
        if (a >= 0) {
            return a;
        } else {
            return -a;
        }
    }

}

Как обычно весь костяк уже реализован за Вас. Обратите внимание на то, что:

методы статические, а это значит, что в качестве первой локальной переменной в массиве локальных
переменных (по индексу 0) не будет располагаться указатель this!
переменные имеют тип double, - каждая переменная занимает место двух переменных в стеке метода
(и/или в массиве локальных констант);
указывая размер стека и массива локальных констант, не забывайте о прошлом пункте.
Детали решения

В задании Вам предстоит решить довольно трудную задачу генерации байт-кода для подсчета дискриминанта.
Для успешного решени этой задачи Вам понадобится работа с инструкцией LDC. Пример:

mv.visitLdcInsn(4.0);
Если Вы не помните для чего она используется, то настоятельно рекомендуем ознакомиться с этим.

Для умножения используется инструкция: DMUL, для вычитания: DSUB.

*/
