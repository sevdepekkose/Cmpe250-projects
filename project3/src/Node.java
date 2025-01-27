import java.util.ArrayList;
public class Node implements Comparable<Node>{
    public int x;
    public int y;
    public int type;
    public ArrayList<Edge> neighbors;
    public boolean isVisited;
    public Node parent;
    public double distance;
    public Node(int x, int y,int type){
        this.x = x;
        this.y = y;
        this.type = type;
        parent = null;
        neighbors = new ArrayList<>();
        isVisited = false;

    }
    @Override
    public String toString(){
        return x+ " "+ y;
    }
    public void addNeighbor(Edge edge){
        neighbors.add(edge);
    }
    @Override
    public boolean equals(Object obj) {
        // Check if the object is compared with itself
        if (this == obj) {
            return true;
        }
        // Check if the object is an instance of Node
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        // Cast the object to Node and compare coordinates
        Node other = (Node) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int compareTo(Node node){
        if (node.distance<distance){
            return 1;
        }
        else if (node.distance>distance){
            return -1;
        }
        return 0;

    }
}
