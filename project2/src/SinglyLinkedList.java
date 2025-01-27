public class SinglyLinkedList<E> {
    public Node<E> head = null;
    public  Node<E> tail = null;
    private int size = 0;
    public SinglyLinkedList(){}
    public boolean isEmpty(){
        return size== 0;
    }
    public void addFirst(E e){
        head = new Node<>(e,head);
        if (size==0){
            tail = head;  // if list is empty node is tail and head
        }
        size ++;
    }
    public void remove(E e){
        if (head == null) return;  // If the list is empty

        if (head.element.equals(e)) {
            head = head.next;  // Remove head node
            return;
        }

        Node<E> current = head;
        while (current.next != null) {
            if (current.next.element.equals(e)) {
                current.next = current.next.next;  // Remove the node by bypassing it
                return;
            }
            current = current.next;
        }
    }
    public E contains(String Id) {
        if (isEmpty()) {
            return null;
        }

        Node<E> current = head;
        while (current != null) {
            if (current.element instanceof User) {
                User user = (User) current.element;
                if (user.ID.equals(Id)) {
                    return (E) user; // Return the element if the ID matches
                }
            }
            if (current.element instanceof Post) {
                Post post = (Post) current.element;
                if (post.ID.equals(Id)) {
                    return (E) post; // Return the element ( if the ID matches
                }
            }

            current = current.next;
        }

        return null;  // If no E was found with the given Id
    }
    // contain method for Entry class
    public boolean contains(E element){
        Node<E> current = head;
        while (current != null) {
            if (current.element.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}
