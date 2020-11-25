import java.io.*;
import java.util.*;

public class decoder {
    public static String binPath = null;
    public static String codeTablePath = null;

    public static void  expand() throws IOException {
        Map<String, String> codeTable = new HashMap<>();
        /* Convert code table to Map<code, data>*/
        try {
            Scanner scanner = new Scanner(new File(codeTablePath));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String lineSplit[] = line.split(" ");
                codeTable.put(lineSplit[1], lineSplit[0]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /* Build Huffman Tree with code table */
        HuffmanNode root = buildHTree(codeTable);
        /* Using Huffman Tree to decode encoded.bin */
        writeDecodedFile(root);
    }

    /* Build Huffman Tree with code table */
    private static HuffmanNode buildHTree(Map<String, String> codeTable) {
        HuffmanNode root = new HuffmanNode('\0',-1,null,null);
        HuffmanNode current = root;
        int size = codeTable.keySet().size();
        for (String ct : codeTable.keySet()) {
            char[] ch = ct.toCharArray();
            for (int i = 0; i < ct.length(); i++) {
                if (ch[i] == '0') {
                    if (current.left == null) {
                        current.left = new HuffmanNode('\0',-1,null,null);
                        current = current.left;
                    } else {
                        current = current.left;
                    }
                }
                if (ch[i] == '1') {
                    if (current.right == null) {
                        current.right = new HuffmanNode('\0',-1,null,null);
                        current = current.right;
                    } else {
                        current = current.right;
                    }
                }
            }
            current.data = Integer.parseInt(codeTable.get(ct));
            current = root;
            size--;
            if (size < 0) {
                break;
            }
        }

        return root;
    }

    /* Using Huffman Tree to decode encoded.bin */
    private static void writeDecodedFile (HuffmanNode root) throws IOException {
        HuffmanNode current = root;
        BufferedWriter bw = new BufferedWriter(new FileWriter("decoded.txt"));
        File file = new File(binPath);
        byte[] data = new byte[(int) file.length()];
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        dis.readFully(data);
        dis.close();

        String temp;
        for (int i = 0; i < data.length; i++) {
            temp = Integer.toBinaryString((data[i] & 0xFF) + 0x100).substring(1);
            for (int bit = 0; bit < temp.length(); bit++) {
                if (temp.charAt(bit) == '1') {
                    current = current.right;
                }
                else if (temp.charAt(bit) == '0') {
                    current = current.left;
                }
                if (current.isLeaf()) {
                    bw.write(current.data + "\n");
                    current = root;
                }
            }
        }
        bw.close();
    }

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("The Input File name is not specified!");
                System.out.println("path:[encoded.bin] path:[code_table.txt]");
                return;
            }
            binPath = args[0];
            codeTablePath = args[1];
            decoder.expand();
        } catch (Exception e) {
            System.out.println("File not found at: " + args[0]);
        }
    }
}

