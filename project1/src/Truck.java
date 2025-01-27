public class Truck {
    public int ID;
    public int maxCapacity;
    public int remainingLoadCapacity;
    public int load;
    public Truck(int ID,int maxCapacity){
        this.ID = ID;
        this.maxCapacity= maxCapacity;
        remainingLoadCapacity = maxCapacity;
    }

    @Override
    public String toString() {
        return ""+ID;
    }
}
