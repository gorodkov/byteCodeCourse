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
        // 1. calculate 4 * a
        // 1.1 push "a" to stack
        mv.visitIntInsn(Opcodes.DLOAD, 0);
        // 1.2 push 4.0 to stack
        mv.visitLdcInsn(4.0);
        // 1.3 mult 4.0 * a
        mv.visitInsn(Opcodes.DMUL);

        // 2. calculate (4 * a) * c
        // 2.1 push c to stack
        mv.visitIntInsn(Opcodes.DLOAD, 4);
        // 2.2 mult (4 * a) * c
        mv.visitInsn(Opcodes.DMUL);
        // 2.2 save to a
        mv.visitIntInsn(Opcodes.DSTORE, 0);

        // 3. calculate b * b
        // 3.1 push b to stack
        mv.visitIntInsn(Opcodes.DLOAD, 2);
        // 3.2 push b to stack
        mv.visitIntInsn(Opcodes.DLOAD, 2);
        // 3.2 mult b * b
        mv.visitInsn(Opcodes.DMUL);

        // 4. substract b * b - (4 * a * c)
        // 4. load (4 * a * c)
        mv.visitIntInsn(Opcodes.DLOAD, 0);
        mv.visitInsn(Opcodes.DSUB);
        mv.visitInsn(Opcodes.DRETURN);
        mv.visitMaxs(4, 6);
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
        mv.visitIntInsn(Opcodes.DLOAD, 0);
        mv.visitInsn(Opcodes.DCONST_0);
        // compare variable with 0
        mv.visitInsn(Opcodes.DCMPL);
        // compare result of double comparation with 0
        mv.visitJumpInsn(Opcodes.IFLT, elseLable);

        // if the result of comparation has been greate then 0 then the values if postitive
        mv.visitIntInsn(Opcodes.DLOAD, 0);
        mv.visitInsn(Opcodes.DRETURN);

        // if the result of comparation has been less then 0 then the values if negative
        mv.visitLabel(elseLable);
        mv.visitIntInsn(Opcodes.DLOAD, 0);
        mv.visitInsn(Opcodes.DNEG);
        mv.visitInsn(Opcodes.DRETURN);

        mv.visitMaxs(4, 2);
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
