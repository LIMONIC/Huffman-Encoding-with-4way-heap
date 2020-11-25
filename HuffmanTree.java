import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class HuffmanTree {
    public static String inputFile = null;
    public static void compress(int type) {
        // read input
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

        // build Huffman Tree
        HuffmanNode root = null;
        long startTime, stopTime;
        switch (type) {
            case 0:
                startTime = System.nanoTime();
                for (int i = 0; i < 10; i++) {
                    root = buildTrieBinaryHeap(input);
                }
                stopTime = System.nanoTime();
                System.out.println("Time using binary heap (microsecond): " + ((stopTime - startTime) / 10 / 1000));
                break;
            case 1:
                startTime = System.nanoTime();
                for (int i = 0; i < 10; i++) {
                    root = buildTrieFourWayHeap(input);
                }
                stopTime = System.nanoTime();
                System.out.println("Time using 4-way heap (microsecond): " + ((stopTime - startTime) / 10 / 1000));
                break;
            case 2:
                startTime = System.nanoTime();
                for (int i = 0; i < 10; i++) {
                    root = buildTriePairingHeap(input);
                }
                stopTime = System.nanoTime();
                System.out.println("Time using pairing heap (microsecond): " + ((stopTime - startTime) / 10 / 1000));
                break;
        }
    }

    private static HuffmanNode buildTrieBinaryHeap (Map<Integer,Integer> input){
        BinaryHeap bh = new BinaryHeap(input.keySet().size());
        for (Integer i : input.keySet()) {
            bh.insert(new HuffmanNode(i, input.get(i), null, null));
        }
        while(bh.size() > 1) {
            HuffmanNode left = bh.removeMin(0);
            HuffmanNode right = bh.removeMin(0);
            HuffmanNode parent = new HuffmanNode('\0', left.freq + right.freq, left, right);
            bh.insert(parent);
        }
        return bh.removeMin(0);
    }

    private static HuffmanNode buildTrieFourWayHeap (Map<Integer,Integer> input){
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

    private static HuffmanNode buildTriePairingHeap (Map<Integer,Integer> input){
        PairingHeap ph = new PairingHeap();
        for (Integer i : input.keySet()) {
            ph.insert(new HuffmanNode(i, input.get(i), null, null));
        }
        while(ph.size() > 1) {
            HuffmanNode left = ph.deleteMin();
            HuffmanNode right = ph.deleteMin();
            HuffmanNode parent = new HuffmanNode('\0', left.freq + right.freq, left, right);
            ph.insert(parent);
        }
        return ph.deleteMin();
    }

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("The Input File name is not specified!");
                System.out.println("path:[input.txt]");
                return;
            }
            inputFile = args[0];
            for (int i = 0; i < 3; i++) {
                HuffmanTree HTree = new HuffmanTree();
                HTree.compress(i);
            }
        } catch (Exception e) {
            System.out.println("File not found at: " + args[0]);
        }
    }

}
