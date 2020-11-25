import java.io.*;
import java.util.*;

public class encoder {
    public static String inputFile = null;
    public static void compress() throws IOException {
        /* read input */
        Map<Integer, Integer> input = new HashMap<>(); // input, freq
        try {
            Scanner scanner = new Scanner(new File(inputFile));
            while (scanner.hasNextLine() && scanner.hasNextInt()) {
                int line = scanner.nextInt();
                if (!input.containsKey(line)) {
                    input.put(line, 1);
                } else {
                    input.put(line, input.get(line) + 1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /* build Huffman Tree */
        HuffmanNode root = buildTrie_dAryHeap(input);

        /* build Tree Table */
        Map<String, String> codeTable = new HashMap<>(); // input, code
        buildCodeTable(codeTable, root, "");
        outputCodeTable(codeTable);
        // print encoded data
        outputEncodedFile(codeTable);
    }

    private static HuffmanNode buildTrie_dAryHeap (Map<Integer,Integer> input){
        FourWayHeap dh = new FourWayHeap(input.keySet().size());
        for (Integer i : input.keySet()) {
            dh.insert(new HuffmanNode(i, input.get(i), null, null));
        }
        while(dh.size() > 1) {
            HuffmanNode left = dh.removeMin(3);
            HuffmanNode right = dh.removeMin(3);
            HuffmanNode parent = new HuffmanNode('\0', left.freq + right.freq, left, right);
            dh.insert(parent);
        }
        return dh.removeMin(3);
    }

    private static void buildCodeTable (Map<String, String> codeTable, HuffmanNode x, String path) {
        if (!x.isLeaf()) {
            buildCodeTable(codeTable, x.left, path + '0');
            buildCodeTable(codeTable, x.right, path + '1');
        } else {
            codeTable.put(Integer.toString(x.data),path);
        }
    }

    private static void outputCodeTable(Map<String, String> codeTable) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter("code_table.txt");
            for (String s : codeTable.keySet()) {
                pw.print(s+ " " +codeTable.get(s) + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (pw != null){
                pw.close();
            }
        }
    }

    private static void outputEncodedFile(Map<String, String> codeTable) throws IOException {
        DataOutputStream out = new DataOutputStream(new FileOutputStream("encoded.bin"));
        int buffer = 0;
        int n = 0;
        // get entire string, convert it to char array
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(inputFile));
            while (scanner.hasNextLine() && scanner.hasNextInt()) {
                String line = scanner.next();
                sb.append(codeTable.get(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        char[] ch = sb.toString().toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == '0') {
                buffer <<= 1;
            } else {
                buffer <<= 1;
                buffer |= 1;
            }
            n++;
            if (n == 8) {
                // write buffer to char
                assert buffer >= 0 && buffer < 256; // 8-bit buffer
                out.write(buffer);
                //clearBuffer();
                n = 0;
                buffer = 0;
            }

        }
        // flux buffer with 0 if not fully filled
        if (n != 0) {
            buffer <<= (8 - n);
            out.write(buffer);
            n = 0;
            buffer = 0;
        }
        out.close();
    }

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("The Input File name is not specified!");
                System.out.println("path:[input.txt]");
                return;
            }
            inputFile = args[0];
            encoder encoder = new encoder();
            encoder.compress();
        } catch (Exception e) {
            System.out.println("File not found at: " + args[0]);
        }
    }

}
