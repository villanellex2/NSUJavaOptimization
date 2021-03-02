package nsu.ru.edubinskaya.ConstantPool;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    ArrayList<ConstantPoolLine> lines = new ArrayList<>();

    try (FileInputStream in = new FileInputStream(fileName)) {
      DataInputStream is = new DataInputStream(in);
      is.skipBytes(8); // skip magic and versions.
      int constPoolCount = is.readUnsignedShort();

      for (int i = 0; i < constPoolCount - 1; ++i) {
        int tag = is.readUnsignedByte();
        if (constants.get(tag) == null) continue;
        ConstantPoolLine line = new ConstantPoolLine();
        line.tag = i;
        line.type = constants.get(tag);
        switch (tag) {
          case 15:
            is.skipBytes(1);
          case 16:
          case 8:
          case 7:
          case 19:
          case 20:
            line.links.add(is.readUnsignedShort());
            break;
          case 9:
          case 10:
          case 11:
          case 17:
          case 18:
            line.links.add(is.readUnsignedShort());
            line.links.add(is.readUnsignedShort());
            line.del = '.';
            break;
          case 4:
            line.value = String.valueOf(is.readFloat());
            break;
          case 3:
            line.value = String.valueOf(is.readInt());
            break;
          case 5:
            line.value = String.valueOf(is.readLong());
            break;
          case 6:
            line.value = String.valueOf(is.readDouble());
            break;
          case 12:
            line.links.add(is.readUnsignedShort());
            line.links.add(is.readUnsignedShort());
            line.del = ':';
            break;
          case 1:
            line.value = new String(is.readNBytes(is.readUnsignedShort()), StandardCharsets.UTF_8);
            break;
        }
        lines.add(line);
      }
      for (int i = lines.size() - 1; i >= 0; i--) {
        findValue(lines, i, 0);
      }

      for (ConstantPoolLine line:lines){
        System.out.println(line.toString());
      }
    } catch (FileNotFoundException exception) {
      System.err.println("incorrect file name.");
    } catch (IOException ioException) {
      System.err.println(ioException.getMessage());
    }
  }

  static String findValue(ArrayList<ConstantPoolLine> lines, int index, int counter) {
    if (counter >= 11) return " ";
    if (lines.get(index).value == null) {
      ConstantPoolLine indexLine = lines.get(index);
      StringBuilder res = new StringBuilder();
      for (int i = 0; i < indexLine.links.size(); ++i) {
        int currLine = indexLine.links.get(i);
        indexLine.linkValues.add(findValue(lines, currLine, counter + 1));
        if (i != 0) res.append(indexLine.del);
        res.append(indexLine.linkValues);
      }
      indexLine.value = res.toString();
    }
    return lines.get(index).value;
  }
}

class ConstantPoolLine {

  public ArrayList<String> linkValues = new ArrayList<>();
  public ArrayList<Integer> links = new ArrayList<>();
  public String value;
  public Character del;
  public String type;
  public int tag;

  @Override
  public String toString() {
    if (links.size() == 0) {
      return String.format("#%d %s %s", tag, type, value);
    }
    if (links.size() == 1) {
      String res = String.format("#%d %s #%d", tag, type, links.get(0));
      String tabs = res + " ".repeat(Math.max(0, 70 - res.length())) + linkValues.get(0);
      return tabs;
    }
    if (links.size() == 2) {
      String res = String.format("#%d %s #%d%c%d", tag, type, links.get(0), del, links.get(1));
      String tabs =
          res
              + " ".repeat(Math.max(0, 70 - res.length()))
              + linkValues.get(0)
              + del
              + linkValues.get(1);
      return tabs;
    } else return null;
  }
}
