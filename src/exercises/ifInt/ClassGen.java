package exercises.ifInt;

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
        generateMinMethod(cw);
        generateMin2Method(cw);
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

    private void generateMinMethod(final ClassWriter cw ) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "min", // method name
                "(II)I", // method descriptor
                null,    // exceptions
                null);   // method attributes
        mv.visitCode();
        final Label elseLabel = new Label();
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel);
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitLabel(elseLabel);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
    }

    private void generateMin2Method(final ClassWriter cw ) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "min",    // method name
                "(III)I", // method descriptor
                null,     // exceptions
                null);    // method attributes
        mv.visitCode();
        final Label elseLabel = new Label();
        final Label elseLabel2 = new Label();
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel);
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel);
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitLabel(elseLabel);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel3);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitLabel(elseLabel3);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(3, 3);
        mv.visitEnd();
    }

}


/*

Предисловие

Приветствую Вам, о ветераны битв с байт-кодом. В этом задании Вам предстоит столкнуться уже с более сложной генерацией байт-кода для не линейной программы (т.е. с "бранчами" и условными операторами).

Задание

Java Код, который Вам предстоит преобразить в байткод:

public class Math {

    public static int min(int a, int b) {
        if (a <= b)
            return a;
        else
            return b;
    }

    public static int min(int a, int b, int c) {
        if (a < b && a < c) return a;
        if (b < c) return b;
        return c;
    }

}

Детали решения

Не смотря на похожесть задания, есть некоторые новые элементы, которые Вам необходимо знать для его
успешного решения. В частности в Вашем генерируемом байткоде нужно будет создавать метки.
Для того что бы сгенерировать метку нужно создать экземпляр класса Label. Это уже сделано в коде за Вас:

final Label elseLable = new Label();
Там где потребуется две метки (или больше):

final Label elseLable = new Label();
final Label elseLable2 = new Label();
Во втором методе задачу можно решить с использованием двух меток, однако Вы вольны создавать
большее их число самостоятельно, если потребуется.

Для того что бы добавить созданную метку в какое либо конкретное место байт-кода, необходимо лишь
воспользоваться методом класса MethodVisitor - visitLabel. Например вот так:

mv.visitLabel(elseLable);

Так же, из ранее не встречавшегося Вам понадобиться метод visitJumpInsn (все того же класса MethodVisitor),
для генерации условных инструкций, на пример:

mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLable);
Данный код добавляет инструкцию IF_ICMPLE с меткой "elseLable".


Hexlet solution

// BEGIN
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel);
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitLabel(elseLabel);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(2, 2);
// END

// BEGIN
        // comparing local variable 0 with 1 and saving min to the 0
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitVarInsn(Opcodes.ISTORE, 0);
        mv.visitLabel(elseLabel);

        // comparing local variable 0 with 2 and saving min to the 0
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel2);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitVarInsn(Opcodes.ISTORE, 0);
        mv.visitLabel(elseLabel2);

        // returning local variable 0 (since it's the minimum)
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(2, 2);
// END


*/
