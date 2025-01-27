public class ParkingLot implements Comparable<ParkingLot>{
    public int capacity;
    public int truck_limit;
    public QueueWithArray waitingQueue;

    public QueueWithArray readyQueue;

    public int readyTruckNumber ;
    public ParkingLot(int capacity,int truck_limit){
        this.readyQueue = new QueueWithArray(truck_limit);
        this.waitingQueue = new QueueWithArray(truck_limit);
        this.capacity = capacity;
        this.truck_limit = truck_limit;
        readyTruckNumber =0;
    }
    @Override
    public int compareTo(ParkingLot parkingLot) {
        if (capacity > parkingLot.capacity) {
            return 1;
        }
        else if (capacity < parkingLot.capacity){
            return -1;
        }
        else{
            return 0;
        }
    }

    @Override
    public String toString() {
        return ""+capacity;
    }
    public void addTruckToWaiting(Truck truck){
        if(waitingQueue.numberOfElements+readyQueue.numberOfElements<truck_limit){
            waitingQueue.enqueue(truck);
        }

    }
    public void addTruckToReady(Truck truck){
        if(waitingQueue.numberOfElements+readyQueue.numberOfElements<truck_limit){
            readyQueue.enqueue(truck);
        }

    }
    public Truck removeTruckFromWaiting(){
        return waitingQueue.dequeue();
    }
    public Truck removeTruckFromReady(){
        return readyQueue.dequeue();
    }
}
