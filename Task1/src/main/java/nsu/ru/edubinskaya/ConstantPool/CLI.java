package nsu.ru.edubinskaya.ConstantPool;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CLI {
  public static final Map<Integer, String> constants = new HashMap<>();

  static {
    constants.put(7, "Class               ");
    constants.put(9, "Fieldref            ");
    constants.put(10, "Methodref           ");
    constants.put(11, "InterfaceMethodref  ");
    constants.put(8, "String              ");
    constants.put(3, "Integer             ");
    constants.put(4, "Float               ");
    constants.put(5, "Long                ");
    constants.put(6, "Double              ");
    constants.put(12, "NameAndType         ");
    constants.put(1, "Utf8                ");
    constants.put(15, "MethodHandle        ");
    constants.put(16, "MethodType          ");
    constants.put(17, "Dynamic             ");
    constants.put(18, "InvokeDynamic       ");
    constants.put(19, "Module              ");
    constants.put(20, "Package             ");
  }

  public static void main(String[] args) {
    String fileName;

    if (args.length < 1) {
      System.out.print("Enter file name: ");
      Scanner scanner = new Scanner(System.in);
      fileName = scanner.nextLine();
      scanner.close();
    } else {
      fileName = args[0];
    }

    try (FileInputStream in = new FileInputStream(fileName)) {
      DataInputStream is = new DataInputStream(in);
      is.skipBytes(8); // skip magic and versions.
      int constPoolCount = is.readUnsignedShort();

      for (int i = 0; i < constPoolCount - 1; ++i) {
        int tag = is.readUnsignedByte();
        if (constants.get(tag) == null) continue;
        System.out.print("#" + (i + 1) + " = " + constants.get(tag));

        switch (tag) {
          case (15):
            is.skipBytes(1);
          case (16):
          case (8):
          case (7):
          case (19):
          case (20):
            System.out.println("#" + is.readUnsignedShort());
            break;
          case (9):
          case (10):
          case (11):
          case (17):
          case (18):
            System.out.println("#" + is.readUnsignedShort() + ".#" + is.readUnsignedShort());
            break;
          case (4):
            System.out.println(is.readFloat());
            break;
          case (3):
            System.out.println(is.readInt());
            break;
          case (5):
            System.out.println(is.readLong());
            break;
          case (6):
            System.out.println(is.readDouble());
            break;
          case (12):
            System.out.println("#" + is.readUnsignedShort() + ":#" + is.readUnsignedShort());
            break;
          case (1):
            System.out.println(
                new String(is.readNBytes(is.readUnsignedShort()), StandardCharsets.UTF_8));
            break;
        }
      }
    } catch (FileNotFoundException exception) {
      System.err.println("incorrect file name.");
    } catch (IOException ioException) {
      System.err.println(ioException.getMessage());
    }
  }
}
