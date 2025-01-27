import java.util.ArrayList;

public class QueueWithArray {
    public ArrayList<Truck> list;
    public int size ;
    public int numberOfElements = 0;

    public QueueWithArray(int truckLimit){
        size = truckLimit;
        list = new ArrayList<>();

    }
    public int size(){
        return numberOfElements;
    }
    public boolean isEmpty(){
        return list.isEmpty();
    }
    public void enqueue(Truck truck) {
        if (size> list.size()){
            numberOfElements++;
            list.add(truck);
        }

    }
    public Truck dequeue(){
        if (isEmpty()){
            return null;
        }
        Truck truck = list.get(0);
        list.remove(0);
        numberOfElements--;
        return truck;
    }
    public Truck getFirst(){
        if (isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
