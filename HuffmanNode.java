public class HuffmanNode implements Comparable<HuffmanNode>{
    int data;
    int freq;
    HuffmanNode left;
    HuffmanNode right;

    HuffmanNode(int data, int freq, HuffmanNode left, HuffmanNode right) {
        this.data = data;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }

    public int compareTo(HuffmanNode that) {
        return this.freq - that.freq;
    }

    // leaf node
    public boolean isLeaf() {
        assert ((left == null) && (right == null)) || ((left != null) && (right != null));
        return (left == null) && (right == null);
    }
}
