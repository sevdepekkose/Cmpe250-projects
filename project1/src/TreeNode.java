public class TreeNode {
    public ParkingLot element;
    public TreeNode left;
    public TreeNode right;
    public TreeNode parent;
    public int height;
    public int balance_factor;
    public TreeNode(ParkingLot parkingLot){
        this.element = parkingLot;
        this.height = 1;
        this.balance_factor = 0;
        this.parent = null;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}
