/**
 * This Class creates min heap, modified from
 * max heap from OpenDSA
 * @author Daniel Vasquez, OpenDSA
 * @version 11/15/2021
 */
public class MinHeap {
    private Participant[] heap; // Pointer to the heap array
    private int size;          // Maximum size of the heap
    private int n;             // Number of things now in heap
    private int hidden;         // index for hidden records
    private int total;         // Number total records including hidden ones

    /**
     * Constructor for the min heap
     */
    public MinHeap(int max)
    { 
        heap = new Participant[max]; 
        n = 0; 
        total = n;  
        hidden = max;
        size = max;
        buildheap(); 
    }
    
    /**
     * Returns current size of the heap
     * @return current size of the heap
     */
    public int heapsize() 
    { 
        return n; 
    }
    
    /**
     * Return current size of the hidden heap
     * @return current size of the hidden heap
     */
    public int hiddensize() 
    { 
        return (size - hidden); 
    }
    
    /**
     * Returns total size of min heap (hidden + visable)
     * @return total size of min heap
     */
    public int total() 
    { 
        return total; 
    }
    
    /**
     * Returns if value is leaf
     * @param pos the index of the value
     * @return true if pos a leaf position, false otherwise
     */
    public boolean isLeaf(int pos)
    { 
        return (pos >= n / 2) && (pos < n); 
    }

    /**
     * Returns the values left child
     * @param pos the index of the value
     * @return position for left child of pos
     */
    public int leftchild(int pos) {
        if (pos >= n / 2)
        {
            return -1;
        }
        return 2 * pos + 1;
    }

    /**
     * Returns the values right child
     * @param pos the index of the value
     * @return position for right child of pos
     */
    public int rightchild(int pos) {
        if (pos >= (n - 1) / 2)
        {
            return -1;
        }
        return 2 * pos + 2;
    }

    /**
     * Returns the values parent
     * @param pos the index of the value
     * @return position for parent of pos
     */
    public int parent(int pos) {
        if (pos <= 0)
        {
            return -1;
        }
        return (pos - 1) / 2;
    }

    /**
     * Inserts a record into min heap
     * @param key to be insered
     * @return true if successfull, false if not
     */
    public boolean insert(Participant key) {
        if (n >= size || total >= size) {
            System.out.println("heap is full");
            return false;
        }
        int curr = n++;
        total++;
        heap[curr] = key;  // Start at end of heap
        // Now sift up until curr's parent's key > curr's key
        while ((curr != 0) && (heap[curr].compareTo(heap[parent(curr)]) < 0)) {
            this.swap(heap, curr, parent(curr));
            curr = parent(curr);
        }
        return true;
    }

    /**
     * Heapifies the min heap
     */
    public void buildheap()
    { 
        for (int i = (n / 2) - 1; i >= 0; i--) 
        {
            siftdown(i);
        }
    }
    
    /**
     * Sifts down the heap, puts
     * element in current position
     * @param pos to be sifted
     */
    public void siftdown(int pos) {
        if ((pos < 0) || (pos >= n)) 
        {
            return; // Illegal position 
        }
        while (!isLeaf(pos)) {
            int j = leftchild(pos);
            if ((j < (n - 1)) && (heap[j].compareTo(heap[j + 1]) > 0))
            {
                j++; // j is now index of child with greater value
            }
            if (heap[pos].compareTo(heap[j]) <= 0)
            {
                return;
            }
            this.swap(heap, pos, j);
            pos = j;  // Move down
        }
    }
    
    /**
     * Returns and removes the smallest element in the
     * min heap, then heapifies
     * @return the smallest record in the min heap
     */
    public Participant removemin() {
        if (n == 0)
        {
            return null;  // Removing from empty heap
        }
        this.swap(heap, 0, --n); // Swap maximum with last value
        if (n != 0)      // Not on last element
        {
            siftdown(0);   // Put new heap root val in correct place
        }
        Participant temp = heap[n];
        heap[n] = null;
        total--;
        return temp;
    }

    /**
     * Removes the record at position pos
     * @param pos the position to be targeted
     * @return the record removed
     */
    public Participant remove(int pos) {
        if ((pos < 0) || (pos >= n))
        {
            return null; // Illegal heap position
        }
        if (pos == (n - 1)) 
        {
            n--; // Last element, no work to be done
        }
        else {
            this.swap(heap, pos, --n); // Swap with last value
            // If we just swapped in a big value, push it up
            while ((pos > 0) && (heap[pos].compareTo(heap[parent(pos)]) < 0)) 
            {
                this.swap(heap, pos, parent(pos));
                pos = parent(pos);
            }
            if (n != 0)
            {
                siftdown(pos); // If it is little, push down
            }
        }
        Participant temp = heap[n];
        heap[n] = null;
        total--;
        return temp;
    }
    
    /**
     * Hides a record, aka it will not appear
     * in the min heap as visable or accessable
     * @param key to be hidden
     * @return true if sucessfull, false if not
     */
    public boolean hide(Participant key)
    {
        if (total < size)
        {
            hidden--;
            heap[hidden] = key;
            total++;
           // n--;
            return true;
        }
        System.out.println("Heap is full to the brim, max reached");
        return false;
    }
    
    /**
     * Releases the hidden records and puts it
     * into the min heap, making them visible
     */
    public void releaseHidden()
    {
        n = size - hidden;
        hidden = size;
        for (int i = 0; i < n; i++)
        {
            heap[i] = heap[size - 1 - i];
        }
        buildheap();
    }
    
    /**
     * Swaps the records with the indexes provided
     * @param h the heap used
     * @param first the first record to be swaped
     * @param second the second one to also do so
     */
    private void swap(Participant[] h, int first, int second) {
        Participant temp = h[first];
        h[first] = h[second];
        h[second] = temp;
    }
    
    /**
     * Makes a string representation of the min heap
     * @return a string representation of the min heap
     */
    public String toString()
    {
        String s = "[";
        for (int i = 0; i < n; i++)
        {
            s += heap[i].toString();
            if (i == n - 1)
            {
                break;
            }
            s += ", ";
        }
        s += "]";
        return s;
    }
    
    /**
     * Makes a string representation of the min heap, hidden values
     * @return a string representation of the min heap, hidden values
     */
    public String showHidden()
    {
        String s = "[";
        int loop = size - hidden;
        if (loop == 0)
        {
            s += "None hidden";
        }
        else
        {
            for (int i = 0; i < loop; i++)
            {
                s += heap[size - 1 - i].toString();
                if (i == loop - 1)
                {
                    break;
                }
                s += ", ";
            }
        }
        s += "]";
        return s;
    }
}
