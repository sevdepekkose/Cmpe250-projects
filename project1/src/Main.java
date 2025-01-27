import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        AvlTree tree = new AvlTree();
        File input = new File(args[0]);
        File output = new File(args[1]);
        // to write a file control the file whether it exists
        PrintStream stream;
        try {
            stream = new PrintStream(output);
        }catch(FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        // to read the file control the file whether it exists
        Scanner scanner;
        try {
            scanner = new Scanner(input);
        }
        catch (FileNotFoundException e) {
            System.out.println("Cannot find input file");
            return;
        }
        // While there is a line in input file, read the file. According to the reading call the appropriate method
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] lineSplit = line.split(" ");
            if (lineSplit[0].equals("create_parking_lot")){
                create_parking_lot(Integer.parseInt(lineSplit[1]),Integer.parseInt(lineSplit[2]),tree);
            }
            if (lineSplit[0].equals("add_truck")){
                add_truck(Integer.parseInt(lineSplit[1]),Integer.parseInt(lineSplit[2]),tree,stream);
                stream.println();
            }
            if (lineSplit[0].equals("ready")){
                ready(Integer.parseInt(lineSplit[1]),tree,stream);
            }
            if (lineSplit[0].equals("load")){
                load(Integer.parseInt(lineSplit[1]),Integer.parseInt(lineSplit[2]),tree,stream);
            }
            if (lineSplit[0].equals("delete_parking_lot")){
                delete_parking_lot(Integer.parseInt(lineSplit[1]),tree);
            }
            if (lineSplit[0].equals("count")){
                count(Integer.parseInt(lineSplit[1]),tree,stream);
            }
        }
    }

    //Create parking lot with desired capacity and truck limit and insert it to Avl tree
    public static void create_parking_lot(int capacity_constraint,int truck_limit,AvlTree tree){
        ParkingLot parkingLot = new ParkingLot(capacity_constraint,truck_limit);
        TreeNode node = new TreeNode(parkingLot);
        tree.insert(tree.root,node);
    }

    // Add the truck to the suitable parking lot and write the truck ID to the file if truck can add correctly else -1
    public static void add_truck(int truck_id,int capacity,AvlTree tree,PrintStream stream){
        Truck truck = new Truck(truck_id,capacity);
        TreeNode parkingLot = tree.findParkingLot(capacity,tree.root);
        if (parkingLot == null){
            parkingLot = tree.findLargest(tree.root,capacity);
        }
        while(parkingLot != null && parkingLot.element.truck_limit<=parkingLot.element.waitingQueue.size()+parkingLot.element.readyQueue.size()){
            parkingLot = tree.findLargest(parkingLot,tree.root);
        }
        if (parkingLot == null){
            stream.print(-1);
        }
        else{
            truck.remainingLoadCapacity = parkingLot.element.capacity;
            parkingLot.element.addTruckToWaiting(truck);  // Before add the waiting queue
            stream.print(parkingLot.element.capacity);
        }
    }
    // Be ready the first truck in the waiting queue in the appropriate parking lot
    public static void ready(int capacity,AvlTree tree,PrintStream stream){
        TreeNode parkingLot = tree.findParkingLot(capacity,tree.root);
        if (parkingLot == null){
            parkingLot = tree.findSmallest(tree.root,capacity);
        }
        while (parkingLot != null) {
            if(!parkingLot.element.waitingQueue.isEmpty()){
                Truck truck = parkingLot.element.waitingQueue.getFirst();
                parkingLot.element.removeTruckFromWaiting();    // Remove truck from waiting and add to ready queue
                parkingLot.element.addTruckToReady(truck);
                stream.println(truck.ID+" "+parkingLot.element);  // if we can find appropriate truck, write the file truck ID and capacity of parking lot
                break;
            }
            else{
                parkingLot = tree.findSmallest(parkingLot,tree.root);
            }
        }
        if (parkingLot == null) {
            stream.println( -1);   // if we cannot find truck, write -1 to the file
        }
    }
    // Find the appropriate parking lot and distribute the load to trucks
    public static void load(int capacity,int load_amount,AvlTree tree,PrintStream stream){
        ArrayList<Integer> list = new ArrayList<>();
        int initialLoadAmount = load_amount;
        TreeNode parkingLot = tree.findParkingLot(capacity,tree.root);
        if (parkingLot == null){
            parkingLot = tree.findSmallest(tree.root,capacity);
        }
        while (parkingLot!= null && load_amount>0){
            int loadedTruckAmount = 0;
            int initialQueueSize = parkingLot.element.readyQueue.size();
            while(loadedTruckAmount < initialQueueSize && load_amount>0 && loadedTruckAmount<=parkingLot.element.truck_limit){
                Truck truck = parkingLot.element.readyQueue.getFirst();
                // load the truck and move it to the right parking lot according to its remaining capacity
                if (truck.remainingLoadCapacity>0) {
                    int loadedToTruck = Math.min(parkingLot.element.capacity, load_amount);  // find how much is loaded to the truck
                    truck.remainingLoadCapacity = truck.remainingLoadCapacity - loadedToTruck;
                    truck.load = truck.load + loadedToTruck;
                    load_amount = load_amount - loadedToTruck;
                    parkingLot.element.removeTruckFromReady();

                    if (truck.load == truck.maxCapacity) {
                        truck.load = 0;
                        truck.remainingLoadCapacity= truck.maxCapacity;
                        moveTruck(truck,truck.maxCapacity,tree,stream,list);
                    }
                    else {
                        moveTruck(truck, truck.maxCapacity - truck.load, tree, stream,list);
                    }
                }
                loadedTruckAmount = loadedTruckAmount +1;
            }
            if (load_amount>0){
                parkingLot = tree.findSmallest(parkingLot,tree.root);
            }
        }
        // Write the output to the file
        if (load_amount>0 && load_amount==initialLoadAmount){
            stream.print(-1);
        }
        else{
            for (int i= 0;i<list.size();i++){
                stream.print(list.get(i));
                if(i!= list.size()-1){
                    stream.print(" ");
                }

                if(i%2 == 1){
                    if(i!=list.size()-1){
                        stream.print("-");
                        stream.print(" ");
                    }
                }
            }
        }
        stream.println();
    }
    // Find the giving parking lot and then delete it from the tree
    public static void delete_parking_lot(int capacity_constraint,AvlTree tree){
        TreeNode parkingLot = tree.findParkingLot(capacity_constraint,tree.root);
        tree.delete(tree.root,parkingLot);
    }
    // count the number of trucks in parking lot which  is greater than the giving capacity constraint
    public static void count(int capacity,AvlTree tree,PrintStream stream){
        TreeNode parkingLot = tree.findSmallest(tree.root,capacity);
        int count = 0;
        while (parkingLot !=null){
            count = count + parkingLot.element.readyQueue.size()+parkingLot.element.waitingQueue.size();
            parkingLot = tree.findSmallest(parkingLot,tree.root);
        }
        stream.println(count);  // write the number found
    }
    // Move truck to the appropriate parking lot according to remaining capacity
    public static void moveTruck(Truck truck,int remaining_capacity,AvlTree tree,PrintStream stream,ArrayList<Integer> list){
        TreeNode parkingLot = tree.findParkingLot(remaining_capacity,tree.root);
        if (parkingLot == null){
            parkingLot = tree.findLargest(tree.root,remaining_capacity);
        }
        while(parkingLot != null){
            if(parkingLot.element.truck_limit>parkingLot.element.readyQueue.size()+parkingLot.element.waitingQueue.size()){
                break;
            }
            else{
                parkingLot = tree.findLargest(parkingLot,tree.root);
            }
        }
        // write -1 if there is no appropriate parking lot
        if(parkingLot == null){
            list.add(truck.ID);
            list.add(-1);
        }
        // Write truck id and capacity of parking lot if they can be placed
        else{
            truck.remainingLoadCapacity = parkingLot.element.capacity;
            parkingLot.element.addTruckToWaiting(truck);
            list.add(truck.ID);
            list.add(parkingLot.element.capacity);
        }
    }
}
