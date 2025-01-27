public class SinglyLinkedList {
    public ListNode head = null;
    public  ListNode tail = null;
    public int size = 0;
    public SinglyLinkedList(){}
    public boolean isEmpty(){
        return size== 0;
    }
    public void addFirst(Node node){
        head = new ListNode(node,head);
        if (size==0){
            tail = head;  // if list is empty node is tail and head
        }
        size ++;
    }
    public void remove(Node node){
        if (node ==null) return;
        if (head == null) return;  // If the list is empty

        if (head.element.equals(node)) {  // If node which is wanted to remove is head
            head = head.next;  // Remove head node
            return;
        }

        ListNode current = head;
        while (current.next != null) {
            if (current.next.element.equals(node)) {
                current.next = current.next.next;  // Remove the node by bypassing it
                return;
            }
            current = current.next;
        }
    }
    public Node contains(Node element){
        ListNode current = head;
        while (current != null) {
            if (current.element.equals(element)) {
                return current.element;
            }
            current = current.next;
        }
        return null;
    }
}
