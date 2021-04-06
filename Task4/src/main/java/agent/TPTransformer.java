package agent;

import javassist.*;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class TPTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (className.equals("TransactionProcessor")) {
            ClassPool classPool = ClassPool.getDefault();
            try {
                classPool.insertClassPath(new LoaderClassPath(loader));
                CtClass ctClass = classPool.get("TransactionProcessor");

                //Adding counting fields
                ctClass.addField(CtField.make("private static float minTime = Float.MAX_VALUE;", ctClass));
                ctClass.addField(CtField.make("private static float maxTime = -1f;", ctClass));
                ctClass.addField(CtField.make("private static float totalTime = 0f;", ctClass));
                ctClass.addField(CtField.make("private static int opCount = 0;", ctClass));

                CtMethod ctMethod = ctClass.getDeclaredMethod("processTransaction");
                //Changing number of transaction
                ctMethod.insertBefore("{ $1 += 99; }");

                makeMethod(ctClass, ctMethod);

                CtMethod main = ctClass.getDeclaredMethod("main");
                //Print counting fields
                main.insertAfter(
                        "{ System.out.println(\"\\nMax time = \" + maxTime + \"\\nMin time = \" + minTime + \"\\nAverage time = \" + totalTime / opCount); }");

                byte[] newClassFileBuffer = ctClass.toBytecode();
                ctClass.detach();
                writeByte("hi.class", newClassFileBuffer);
                return newClassFileBuffer;
            } catch (IOException | NotFoundException | CannotCompileException e) {
                e.printStackTrace();
            }
        }
        return classfileBuffer;
    }

    private void makeMethod(final CtClass ctClass, final CtMethod ctMethod) throws CannotCompileException {

        ctMethod.addLocalVariable("startTime", CtClass.longType);
        ctMethod.addLocalVariable("endTime", CtClass.longType);
        ctMethod.addLocalVariable("opTime", CtClass.floatType);
        ctMethod.insertBefore("startTime = System.currentTimeMillis();");
        //adding func counting transaction time
        String method =
                "endTime = System.currentTimeMillis();" +
                "opTime = (endTime - startTime) / 1000.f;" +
                "if (opTime <= minTime) { minTime = opTime; }" +
                "if (opTime > maxTime) { maxTime = opTime; }" +
                "totalTime += opTime;" +
                "opCount += 1;";
        ctMethod.insertAfter(method);
    }

    public static void writeByte(String filename, byte[] data){

        BufferedOutputStream bos = null;

        try
        {
            //create an object of FileOutputStream
            FileOutputStream fos = new FileOutputStream(new File(filename));

            //create an object of BufferedOutputStream
            bos = new BufferedOutputStream(fos);


            /*
             * To write byte array to file use,
             * public void write(byte[] b) method of BufferedOutputStream
             * class.
             */
            System.out.println("Writing byte array to file");

            bos.write(data);
            bos.flush();
            bos.close();

            System.out.println("File written");
        }
        catch(Exception fnfe)
        {
            System.out.println("exception");
        }
    }
}