import java.util.ArrayList;
public class Objective {
    public int x ;
    public int y;
    public ArrayList<Integer> options;
    public boolean offersHelp;
    public Objective(int x, int y) {
        this.x = x;
        this.y = y;
        this.offersHelp = false; // Default is no help
        this.options = null;     // No options if there's no help
    }
    public Objective(int x, int y, ArrayList<Integer> options) {
        this.x = x;
        this.y = y;
        this.offersHelp = true;  // Indicates that wizard offers help
        this.options = options;  // List of available options
    }
    @Override
    public String toString(){
        return x+" "+y + " "+options;
    }

}
