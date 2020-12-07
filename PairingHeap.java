//Reference: https://titanwolf.org/Network/Articles/Article?AID=4b6231b8-fa25-45d2-af09-187230afff58#gsc.tab=0

class PairNode {
    HuffmanNode element;
    PairNode leftChild;
    PairNode nextSibling;
    PairNode prev;

    public PairNode(HuffmanNode x) {
        element = x;
        leftChild = null;
        nextSibling = null;
        prev = null;
    }
}

public class PairingHeap {
    private PairNode root;
    private PairNode[] treeArray= new PairNode[2];
    private int heapSize;

    public PairingHeap(){
        root = null;
        heapSize = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void makeEmpty(){
        root = null;
        heapSize = 0;
    }

    public PairNode insert (HuffmanNode x) {
        PairNode newNode = new PairNode(x);
        if (root == null) {
            root = newNode;
        } else {
            root = compareAndLink(root, newNode);
        }
        heapSize ++;
        return newNode;
    }

    private PairNode compareAndLink(PairNode first, PairNode second) {
        if (second == null) {
            return first;
        }
        if (second.element.compareTo(first.element) < 0) {
            // attach first as leftmost child of second
            second.prev = first.prev;
            first.prev = second;
            first.nextSibling = second.leftChild;
            if (first.nextSibling != null) {
                first.nextSibling.prev = first;
            }
            second.leftChild = first;
            return second;
        } else {
            // attach second to first
            second.prev = first;
            first.nextSibling = second.nextSibling;
            if (first.nextSibling != null) {
                first.nextSibling.prev = first;
            }
            second.nextSibling = first.leftChild;
            if (second.nextSibling != null) {
                second.nextSibling.prev = second;
            }
            first.leftChild = second;
            return first;
        }
    }

    private PairNode combineSiblings(PairNode firstSibling) {
        if(firstSibling.nextSibling == null )
            return firstSibling;
        /* Store the subtrees in an array */
        int numSiblings = 0;
        while(firstSibling != null) {
            treeArray = doubleIfFull( treeArray, numSiblings );
            treeArray[ numSiblings ] = firstSibling;
            /* break links */
            firstSibling.prev.nextSibling = null;
            firstSibling = firstSibling.nextSibling;
            numSiblings++;
        }
        treeArray = doubleIfFull( treeArray, numSiblings );
        treeArray[ numSiblings ] = null;
        /* Combine subtrees two at a time, going left to right */
        int i = 0;
        while(i + 1 < numSiblings) {
            treeArray[i] = compareAndLink(treeArray[i], treeArray[i + 1]);
            i += 2;
        }
        int j = i - 2;
        /* j has the result of last compareAndLink */
        /* If an odd number of trees, get the last one */
        if (j == numSiblings - 3)
            treeArray[ j ] = compareAndLink( treeArray[ j ], treeArray[ j + 2 ] );
        /* Now go right to left, merging last tree with */
        /* next to last. The result becomes the new last */
        while(j >= 2) {
            treeArray[j - 2] = compareAndLink(treeArray[j - 2], treeArray[j]);
            j -= 2;
        }
        return treeArray[0];
    }

    private PairNode[] doubleIfFull(PairNode[] array, int index) {
        if (index == array.length) {
            PairNode[] oldArray = array;
            array = new PairNode[index * 2];
            for( int i = 0; i < index; i++ )
                array[i] = oldArray[i];
        }
        return array;
    }

    public int size(){
        return heapSize;
    }

    /* Delete min element */
    public HuffmanNode deleteMin() {
        if (isEmpty( ) )
            return null;
        HuffmanNode x = root.element;
        if (root.leftChild == null)
            root = null;
        else
            root = combineSiblings( root.leftChild );
        heapSize --;
        return x;
    }

}
