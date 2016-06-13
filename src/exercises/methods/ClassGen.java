package exercises.methods;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import java.io.FileOutputStream;

public class ClassGen {

    public static void main(final String... args) throws Exception {
        final String path = args[0];
        final byte[] byteCode = new ClassGen().generateSummatorClass();
        try (FileOutputStream stream = new FileOutputStream(path)) {
            stream.write(byteCode);
        }
    }

    private byte[] generateSummatorClass() {
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(51,
                Opcodes.ACC_PUBLIC,
                "Summator",
                null,
                "java/lang/Object",
                null);
        generateDefaultConstructor(cw);
        generateSummMethod(cw);
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

    private void generateSummMethod(final ClassWriter cw ) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC,
                "sum", // method name
                "(II)I", // method descriptor
                null,    // exceptions
                null);   // method attributes
        mv.visitCode();
        // BEGIN (write your solution here)

        // END
        mv.visitEnd();
    }

}

/*
Предисловие

Это задание является первым настоящим практическим заданием. Поэтому может показаться немного сложнее,
чем оно есть на самом деле. Однако, как только его решите, остальные задания будут легче.
В дальнейшем мы будем использовать библиотеку, с которой и познакомим Вас сегодня.

Задание

Вам предстоит выступить в роле компилятора! Само собой еще рано писать полноценный и настоящий компилятор,
так что сегодня мы напишем программу, которая будет генерировать байт-код следующего класса:

public class Summator{

    public int sum(int a, int b) {
        return a + b;
    }

}
Т.е. она умеет компилировать только этот класс =) Тем не менее, написать свой байт-код генератор это
очень интересная затея. Перед тем, как мы перейдем к исполнению задания,
давайте немного поговорим как мы тестируем его.

Тестирование задания

Тестовый файл уже скомпилирован и доступен только в виде байт-кода
(соотвественно ничего в нем поменять, как было ранее, не выйдет;)).
Для того, чтобы он смог нормально отработать, необходимо наличие байт-кода класса Summator.
Тест в момента запуска будет искать байт-код класса Summator в папке out. Собственно вот весь код,
которым происходит запуск теста:

    javac src/ClassGen.java -cp libs/asm-5.0.4.jar -d out
    cd out
    java -cp ../libs/asm-5.0.4.jar:. ClassGen Summator.class
    java -cp . SummatorTest
В примере мы используем библиотеку asm-5.0.4.jar, которая содержит необходимые классы для
манипулирования байт-кодом. Если что-то на данный момент является непонятным, то настоятельно
рекомендуем вернуться к курсу Java101 и пересмотреть соответствующие лекции.

Костяк решения

Как Вы знаете из прошлых занятий, байт-код класса состоит из довольно объемной
мета-информации (информации о файле, пуле констант, и т.д.). Логика, которая генерирует всю эту ересь,
уже сделана за Вас, например, метод, который сгенерирует "шапку" файла байт-кода:

final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
cw.visit(51,
    Opcodes.ACC_PUBLIC,     // флаги (атрибуты) класса
    "Summator",             // имя класса
    null,
    "java/lang/Object",     // класс, от которого наследуемся
    null);
    generateDefaultConstructor(cw); // генерируем байт-код конструктора по умолчанию
    generateSummMethod(cw);         // генерируем метод sum
    cw.visitEnd();                  // заканчиваем генерацию класса
    return cw.toByteArray();        // возвращаем массив, который содержит байт код
Как видите, этот метод уже реализован за Вас, как и метод, который добавляет конструктор по умолчанию.
Надеюсь Вы помните, что компилятор автоматом добавит конструктор за Вас к любому классу.
В данном задании компилятор это Вы - Вам и добавлять конструктор самостоятельно.

Давайте теперь рассмотрим метод, который генерирует этот самый конструктор:

final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, // флаги (атрибуты) метода, который мы генерируем
    "<init>", // имя метода; все помним почему имя init? если нет, то бегом слушать прошлые лекции!
    "()V", // тип возвращаемых данных; конструктор ничего не возвращает.
    null, null);
mv.visitCode();

Метод visitVarInsn используется для добавления инструкции, которая работает с локальными переменными.
К таким инструкциям относятся: ILOAD, LLOAD, FLOAD, DLOAD, ALOAD, ISTORE, LSTORE, FSTORE, DSTORE, ASTORE, RET
Первым аргументом передается инструкция, а вторым номер переменной, над которой нужно выполнить инструкцию.

mv.visitVarInsn(Opcodes.ALOAD, 0); // добавляем в стек локальную переменную 0 (это указатель на this).
mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V"); // это мы еще не знаем, посему пропускаем в качестве магии =)

Метод visitInsn используется для генерации инструкций байт-кода, которые не требуют никаких аргументов на вход.
К ним относятся: IRETURN/RETURN и многие другие.

mv.visitInsn(Opcodes.RETURN); // заканчиваем метод
mv.visitMaxs(1, 1); // устанавливаем размер стека в 1 и количество локальных перменных 1
mv.visitEnd();
Генерация конструктора - довольно страшная вещь и использует некоторые инструкции, которые мы с Вами еще
не проходили, однако Вам предстоит сгенерировать куда более простой метод, для которого достаточно
лишь 3х инструкций (какие именно читайте в подсказке).

метод sum

Вот мы и добрались до метода “sum", байт-код которого и предстоит сгенерировать Вам (говоря правильно - написать генератор байт-кода для метода "sum"). Его костяк уже готов для Вас:

    private void generateSummMethod(final ClassWriter cw ) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC,
            "sum", // method name
            "(II)I", // method descriptor
            null,    // exceptions
            null);   // method attributes
        mv.visitCode();
        // BEGIN

        // END
        mv.visitEnd();
    }
Вам необходимо правильно дописать код, который генерирует байт-код! Если задание покажется трудным - у нас есть
для Вас несколько подсказок, но обращайтесь к ним только в крайней необходимости и да пребудет с Вами сила компилятора!


Подсказки

для выполнения этого задания Вам необходимы Всего три инструкции: ** Opcodes.ILOAD - для того, чтобы загрузить
локальную переменную в стек; ** Opcodes.IADD - для добавления двух переменных на стеке; ** Opcodes.IRETURN - для
возвращения результата;
Не забывайте, что ILOAD - это инструкция, которая требует на вход номер локальной переменной, а соотвественно
для ее генерации нужно использовать метод visitVarInsn
IADD/IRETURN - это инструкции, которые не требуют на вход ничего, а соотвественно для их генерации нужно
использовать метод visitInsn
особое внимание уделите корректному определению размеров стека и количеству локальных переменных вызывая
метод visitMaxs; не забывайте, что раз метод не статический, то первым аргументом всегда передается
указатель "this", а уже потом "a" и "b"

**/
