public class BinaryHeap {
    public int currentSize= 0 ;
    public Node[] array;
    public BinaryHeap(){
        array = new Node[10];
    }
    public Node findMin(){
        return array[1];
    }
    public void insert(Node node){
        if (currentSize==array.length-1){
            enlargeArray(array.length*2+1);
        }
        currentSize = currentSize+1;
        //Percolate Up
        int hole = currentSize;
        for (array[0] = node ; node.compareTo(array[hole/2]) < 0 ; hole = hole/2){
            array[hole] = array[hole/2];
        }
        array[hole] = node;
        array[0] =null;
    }
    public void percolateUp(int hole){
        Node node = array[hole];
        for (array[0] = node ; node.compareTo(array[hole/2])<0 ; hole = hole/2){
            array[hole] = array[hole/2];
        }
        array[hole] = node;
        array[0] =null;
    }
    public Node deleteMin() throws Exception{
        if (isEmpty()){
            throw new Exception("Heap is empty.");
        }
        Node minNode = findMin();
        array[1]= array[currentSize];
        array[currentSize]= null;
        currentSize = currentSize-1;
        percolateDown(1);
        return minNode;

    }
    public boolean isEmpty(){
        return currentSize==0;
    }
    private void percolateDown(int hole){
        int child;
        Node tmp = array[hole];
        for (;hole*2<= currentSize;hole =child){
            child = hole*2;
            if (child != currentSize && array[child+1].compareTo(array[child]) < 0) {
                child++;
            }
            if (array[child].compareTo(tmp)<0){
                array[hole] = array[child];
            }
            else{
                break;
            }
        }
        array[hole] = tmp;

    }
    private void enlargeArray(int size){
        Node[] newList = new Node[size];
        for(int i =1; i< array.length;i++){
            newList[i] = array[i];
        }
        array= newList;
    }

    public int findNode(Node node) {
        for (int i = 1; i <= currentSize; i++) {
            if (array[i].equals(node)) {
                return i;
            }
        }
        return -1;  // Return -1 if the node is not found
    }
    public void updateDistance(Node node) {
        int index = findNode(node);
        if (index == -1) {
            insert(node);
        }
        else{
            percolateUp(index);  // Try to percolate up to maintain heap property
            percolateDown(index);  // If percolating up does not work, percolate down
        }

    }

}
