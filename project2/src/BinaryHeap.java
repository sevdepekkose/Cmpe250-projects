import java.util.Arrays;

public class BinaryHeap {
    public int currentSize= 0 ;
    public Post[] array;
    public BinaryHeap(){
        array = new Post[10];
    }
    public Post findMax(){
        return array[1];
    }
    public void insert(Post post){
        if (currentSize==array.length-1){
            enlargeArray(array.length*2+1);
        }
        currentSize = currentSize+1;
        //Percolate Up
        int hole = currentSize;
        for (array[0] = post ; post.compareTo(array[hole/2])>0 ; hole = hole/2){
            array[hole] = array[hole/2];
        }
        array[hole] = post;
        array[0] =null;


    }
    public Post deleteMax() throws Exception{
        if (isEmpty()){
            throw new Exception("Heap is empty.");
        }
        Post maxPost = findMax();
        array[1]= array[currentSize];
        array[currentSize]= null;
        currentSize = currentSize-1;
        percolateDown(1);
        return maxPost;

    }
    public boolean isEmpty(){
        return currentSize==0;
    }
    private void percolateDown(int hole){
        int child;
        Post tmp = array[hole];
        for (;hole*2<= currentSize;hole =child){
            child = hole*2;
            if (child != currentSize && array[child+1].compareTo(array[child])>0) {
                child++;
            }
            if (array[child].compareTo(tmp)>0){
                array[hole] = array[child];
            }
            else{
                break;
            }
        }
        array[hole] = tmp;

    }
    private void enlargeArray(int size){
        Post[] newList = new Post[size];
        for(int i =1; i< array.length;i++){
            newList[i] = array[i];
        }
        array= newList;
    }
    @Override
    public String toString(){
        return Arrays.toString(array);
    }

}
