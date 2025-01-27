public class HashTable {
    public int size =0;
    public int currentSize= 0;
    public SinglyLinkedList[] theLists;
    public HashTable(){
        size = 100001;
        theLists = new SinglyLinkedList[size];
        for( int i = 0; i < theLists.length; i++ )
            theLists[ i ] = new SinglyLinkedList( );
    }
    public boolean isEmpty(){
        return currentSize==0;
    }
    public int myHash(Node node) {
        // Prime numbers are often used to reduce collisions
        final int PRIME1 = 31;  // A prime number for mixing
        final int PRIME2 = 37;  // Another prime number for further mixing

        // Combine x and y in a way that helps avoid collisions
        int hashValue = PRIME1 * node.x + PRIME2 * node.y;

        // Ensure a non-negative hash value by using Math.abs() to avoid negative indices
        return Math.abs(hashValue % theLists.length);  // Ensure it fits within the number of buckets
    }
    public Node contains(Node node){
        SinglyLinkedList whichList = theLists[myHash(node)];
        // Call the contains method on the list to check if the user exists by their ID
        Node foundNode = whichList.contains(node);
        // If foundEdge is not null, it means the user exists in the list
        if (foundNode != null) {
            return node;
        } else {
            return null;
        }
    }
    public void insert(Node node){
        SinglyLinkedList whichList = theLists[myHash(node)];
        // If it's not already on the list insert
        if (whichList.contains(node)==null){
            whichList.addFirst(node);
            currentSize = currentSize+1;
            // Rehash
            if (currentSize > 2*theLists.length){
                rehash();
            }
        }
    }
    public void remove(Node node){
        SinglyLinkedList whichList = theLists[myHash(node)];
        if (whichList.contains(node)!= null){
            whichList.remove(node);
            currentSize--;
        }
    }
    public void rehash(){
        int newSize = size*2+1;
        SinglyLinkedList[] newList = new SinglyLinkedList[newSize];
        for (int i = 0; i < newSize; i++) {
            newList[i] = new SinglyLinkedList();
        }
        for (int i = 0; i < theLists.length; i++) {
            SinglyLinkedList currentList = theLists[i];
            ListNode current = currentList.head;
            // Traverse the linked list at each index

            while (current!=null) {
                //  Compute new index using the new table size
                Node node = current.element;
                final int PRIME1 = 31;  // A prime number for mixing
                final int PRIME2 = 37;  // Another prime number for further mixing

                // Combine x and y in a way that helps avoid collisions
                int hashValue = PRIME1 * node.x + PRIME2 * node.y;
                int newIndex = Math.abs(hashValue % newSize);
                //  Insert the user into the new table
                newList[newIndex].addFirst(current.element);
                current = current.next;
            }
        }
        // update table size and table's lists
        theLists = newList;
        size = size*2+1;
    }

}
