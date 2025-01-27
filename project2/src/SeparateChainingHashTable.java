public class SeparateChainingHashTable<E> {
    public int size =0;
    public int currentSize= 0;
    public SinglyLinkedList<E>[] theLists;
    public SeparateChainingHashTable(){
        size = 97;
        theLists = new SinglyLinkedList[size];
        for( int i = 0; i < theLists.length; i++ )
            theLists[ i ] = new SinglyLinkedList<>( );
    }
    // Hash function
    public int myHash(String x){
        int hashValue = x.hashCode();
        hashValue = hashValue % theLists.length;
        if(hashValue<0){
            hashValue = hashValue+ theLists.length;
        }
        return hashValue;
    }
    // control user has seen before that post
    public boolean hasUserSeenPost(E e){
        if (e instanceof Entry){
            int index = myHash(((Entry) e).userId);
            SinglyLinkedList<E> list = theLists[index];
            return list.contains(e);
        }
        return false;
    }
    public void markPostAsSeen(E e){
        if (e instanceof Entry){
            int index = myHash(((Entry) e).userId);
            SinglyLinkedList<E> list = theLists[index];
            // If post has not been seen by that user before
            if (!contains(e)) {
                list.addFirst(e);
                currentSize++;
                if (currentSize > 2 * theLists.length) {
                    rehash();
                }
            }
        }
    }
    public boolean contains(E e){
        if (e instanceof User){
            User user = (User) e;
            SinglyLinkedList<E> whichList = theLists[myHash(user.ID)];

            // Call the contains method on the linked list to check if the user exists by their ID
            User foundUser = (User) whichList.contains(user.ID);

            // If foundUser is not null, it means the user exists in the list
            if (foundUser != null) {
                return true;
            } else {
                return false;
            }
        }
        if (e instanceof Post){
            Post post = (Post) e;
            SinglyLinkedList<E> whichList = theLists[myHash(post.ID)];

            // Call the contains method on the linked list to check if the post exists by their ID
            Post foundPost = (Post) whichList.contains(post.ID);

            // If foundPost is not null, it means the post exists in the list
            if (foundPost != null) {
                return true;
            } else {
                return false;
            }
        }
        if (e instanceof Entry){
            Entry entry = (Entry) e;
            int index = myHash(entry.userId);
            SinglyLinkedList<E> list = theLists[index];
            // traverse the linked list until  finding given entry
            Node<E> current = list.head;
            while (current != null) {
                if (current.element.equals(entry)) {
                    return true;
                }
                current = current.next;
            }
            return false;
        }
        return false;
    }
    public void insert(E e){
        if (e instanceof User){
            User user = (User) e;
            SinglyLinkedList<E> whichList = theLists[myHash(user.ID)];
            // If it's not already on the list insert
            if (whichList.contains(user.ID)==null){
                whichList.addFirst(e);

                // Rehash
                if (++currentSize > 2*theLists.length){
                    rehash();
                }
            }
        }
        if (e instanceof Post){
            Post post = (Post) e;
            SinglyLinkedList<E> whichList = theLists[myHash(post.ID)];
            // If it's not already on the list insert
            if (whichList.contains(post.ID)==null){
                whichList.addFirst(e);

                // Rehash
                if (++currentSize > 2*theLists.length){
                    rehash();
                }
            }
        }
        if (e instanceof Entry){
            SinglyLinkedList<E> whichList = theLists[myHash(((Entry) e).userId)];
            // If it's not already on the list insert
            if (!whichList.contains(e)){
                whichList.addFirst(e);

                // Rehash
                if (++currentSize > 2*theLists.length){
                    rehash();
                }
            }
        }
    }
    public void remove(E e){
        if (e instanceof User){
            User user = (User) e;
            SinglyLinkedList<E> whichList = theLists[myHash(user.ID)];
            if (whichList.contains(user.ID)!=null){  // if it is in list
                whichList.remove(e);
                currentSize--;
            }
        }
        if (e instanceof Post){
            Post post = (Post) e;
            SinglyLinkedList<E> whichList = theLists[myHash(post.ID)];
            if (whichList.contains(post.ID)!=null){ // if it is in list
                whichList.remove(e);
                currentSize--;
            }
        }
    }
    public void rehash(){
        int newSize = size*2+1;
        SinglyLinkedList<E>[] newList = new SinglyLinkedList[newSize];
        for (int i = 0; i < newSize; i++) {
            newList[i] = new SinglyLinkedList<>();
        }
        for (int i = 0; i < theLists.length; i++) {
            SinglyLinkedList<E> currentList = theLists[i];

            // Traverse the linked list at each index
            Node<E> current = currentList.head;
            while (current!=null) {
                if (current.element instanceof User){
                    //  Compute new index using the new table size
                    User user = (User)current.element;
                    int newIndex = user.ID.hashCode() % newSize;
                    if(newIndex<0){
                        newIndex = newIndex+ newSize;
                    }
                    //  Insert the user into the new table
                    newList[newIndex].addFirst(current.element);


                }
                if (current.element instanceof Post){
                    //  Compute new index using the new table size
                    Post post = (Post)current.element;
                    int newIndex = post.ID.hashCode() % newSize;
                    if(newIndex<0){
                        newIndex = newIndex+ newSize;
                    }
                    //  Insert the post into the new table
                    newList[newIndex].addFirst(current.element);

                }
                if (current.element instanceof Entry){
                    Entry entry = (Entry)current.element;
                    //  Compute new index using the new table size
                    int newIndex = entry.userId.hashCode() % newSize;
                    if(newIndex<0){
                        newIndex = newIndex+ newSize;
                    }
                    //  Insert the entry into the new table
                    newList[newIndex].addFirst(current.element);
                }
                current = current.next;
            }
        }
        // update table size and table's lists
        theLists = newList;
        size = size*2+1;
    }

}
