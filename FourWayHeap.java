import java.util.NoSuchElementException;

public class FourWayHeap {
    private HuffmanNode[] heap;
    private int heapSize;
    private static final int d = 4;
    private static final int SHIFT = 3;
    public FourWayHeap(int capacity) {
        this.heapSize = SHIFT;
        this.heap = new HuffmanNode[capacity + SHIFT];
    }

    public boolean isEmpty() {
        return heapSize - SHIFT == 0;
    }

    public boolean isFull() {
        return heapSize - SHIFT == heap.length ;
    }

    private int parent(int i) {
        return (i - 1 - SHIFT) / d + SHIFT;
    }

    private int kthChild(int i, int k) {
        return d * (i - SHIFT) + k + SHIFT;
    }

    public int size() {
        return heapSize - SHIFT;
    }

    public void insert (HuffmanNode x) {
        if (isFull()) {
            throw new NoSuchElementException();
        }
        heap[heapSize++] = x;
        heapifyUp(heapSize  - 1);
    }

    public HuffmanNode removeMin(int i) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        HuffmanNode key = heap[i];
        heap[i] = heap[heapSize  - 1];
        heapSize --;
        heapifyDown(i);
        return key;
    }

    private void heapifyUp(int i) {
        HuffmanNode temp = heap[i];
        while (i > 3 && temp.compareTo(heap[parent(i)]) < 0 ) {
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
            if(heap[child].compareTo(temp) < 0) {
                heap[i] = heap[child];
            }else {
                break;
            }
            i = child;
        }
        heap[i] = temp;
    }

    private int minChild(int i) {
        int bestChild = kthChild(i, 0);
        int k = 1;
        int position = kthChild(i, k);
        while ((k <= d) && (position < heapSize)) {
            if (heap[position].compareTo(heap[bestChild]) < 0) {
                bestChild = position;
            }
            position = kthChild(i, k++);
        }
        return bestChild;
    }

}
