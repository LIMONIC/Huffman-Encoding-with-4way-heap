
import java.util.NoSuchElementException;

// Reference: https://www.edureka.co/blog/binary-heap-in-java/#min


public class BinaryHeap {
    // initialize heap
    private HuffmanNode[] heap;
    private int heapSize;
    public BinaryHeap(int capacity) {
        heapSize = 0;
        heap = new HuffmanNode[capacity + 1];
    }

    public boolean isEmpty() {
        return heapSize == 0;
    }

    public boolean isFull() {
        return heapSize == heap.length;
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int kthChild(int i, int k) {
        return 2 * i + k;
    }

    public int size() {
        return heapSize;
    }

    public void insert (HuffmanNode x) {
        if (isFull()) {
            throw new NoSuchElementException();
        }
        heap[heapSize++] = x;
        heapifyUp(heapSize - 1);
    }

    public HuffmanNode removeMin(int x) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        HuffmanNode key = heap[x];
        heap[x] = heap[heapSize - 1];
        heapSize --;
        heapifyDown(x);
        return key;
    }

    private void heapifyUp(int i) {
        HuffmanNode temp = heap[i];
        while (i > 0 && temp.compareTo(heap[parent(i)]) < 0 ) {
            heap[i] = heap[parent(i)];
            i = parent(i);
        }
        heap[i] = temp;
    }

    private void heapifyDown(int i) {
        int child;
        HuffmanNode temp = heap[i];
        while (kthChild(i, 1) < heapSize) {
            child = minChild(i);
            if(temp.compareTo(heap[child]) > 0) {
                heap[i] = heap[child];
            }else break;
            i = child;
        }
        heap[i] = temp;
    }

    private int minChild(int i) {
        int leftChild = kthChild(i, 1);
        int rightChild = kthChild(i, 2);
        return heap[leftChild].compareTo(heap[rightChild]) < 0 ? leftChild : rightChild;
    }

}
