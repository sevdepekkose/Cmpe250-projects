public class Node<E> {
    public E element;
    public Node<E> next;
    public Node(E e, Node<E> next){
        element = e;
        this.next = next;
    }
}
