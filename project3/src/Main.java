import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static Node[][] nodes;
    public static HashTable knownNodes;
    public static void main(String[] args) throws Exception {
        double startTime = System.currentTimeMillis();
        ArrayList<Objective> objectives  = new ArrayList<>();
        knownNodes = new HashTable();
        int radiusOfSight;

        File nodesFile = new File(args[0]);
        File edgesFile = new File(args[1]);
        File objectivesFile = new File(args[2]);
        File output = new File(args[3]);
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
            scanner = new Scanner(nodesFile);
        }
        catch (FileNotFoundException e) {
            System.out.println("Cannot find input file");
            return;
        }
        // read nodes file, create node and hold them nodes matrix
        String firstLine = scanner.nextLine();
        String[] firstLineSplit = firstLine.split(" ");
        int xSize = Integer.parseInt(firstLineSplit[0]);
        int ySize = Integer.parseInt(firstLineSplit[1]);
        nodes = new Node[xSize][ySize];
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] lineSplit = line.split(" ");
            int x = Integer.parseInt(lineSplit[0]);
            int y = Integer.parseInt(lineSplit[1].trim());
            int type= Integer.parseInt(lineSplit[2].trim());
            Node node = new Node(x,y,type);
            nodes[x][y]= node;
        }
        scanner.close();

        // to read the file control the file whether it exists
        Scanner scanner1;
        try {
            scanner1 = new Scanner(edgesFile);
        }
        catch (FileNotFoundException e) {
            System.out.println("Cannot find input file");
            return;
        }
        // read the second file and create edge and add neighbor to correct node
        while (scanner1.hasNextLine()){
            String line = scanner1.nextLine();
            int comma = line.indexOf(',');
            int x1 = Integer.parseInt(line.substring(0,line.indexOf('-')));
            int y1 = Integer.parseInt(line.substring(line.indexOf('-')+1,comma));
            int space = line.indexOf(' ');
            String secondString = line.substring(comma+1,space);
            int x2 = Integer.parseInt(secondString.substring(0,secondString.indexOf('-')));
            int y2 = Integer.parseInt(secondString.substring(secondString.indexOf('-')+1));
            double weight = Double.parseDouble(line.substring(space+1));
            Node firstNode = nodes[x1][y1];
            Node secondNode = nodes[x2][y2];
            Edge edge1 = new Edge(firstNode,secondNode,weight);
            firstNode.addNeighbor(edge1);
            Edge edge2 = new Edge(secondNode,firstNode,weight);
            secondNode.addNeighbor(edge2);
        }
        scanner1.close();

        // to read the file control the file whether it exists
        Scanner scanner2;
        try {
            scanner2 = new Scanner(objectivesFile);
        }
        catch (FileNotFoundException e) {
            System.out.println("Cannot find input file");
            return;
        }
        // read the objectives file, create objectives and initialize radius of sight
        radiusOfSight = Integer.parseInt(scanner2.nextLine());
        String[] linesplit = scanner2.nextLine().split(" ");
        Node[] startingNode = new Node[1];
        startingNode[0] = nodes[Integer.parseInt(linesplit[0])][Integer.parseInt(linesplit[1])];
        while (scanner2.hasNextLine()){
            String line = scanner2.nextLine();
            String[] lineSplit = line.split(" ");
            if (lineSplit.length==2){
                objectives.add(new Objective(Integer.parseInt(lineSplit[0]),Integer.parseInt(lineSplit[1])));
            }
            else{
                ArrayList<Integer> options = new ArrayList<>();
                for (int i = 2; i < lineSplit.length; i++) {
                    options.add(Integer.parseInt(lineSplit[i]));
                }
                objectives.add(new Objective(Integer.parseInt(lineSplit[0]),Integer.parseInt(lineSplit[1]),options));
            }
        }
        scanner2.close();

        processObjectives(objectives,startingNode,stream,radiusOfSight);

        double endTime = System.currentTimeMillis();  // End time measurement
        double duration = (endTime - startTime);
        System.out.printf("Time it takes to find the shortest path: %.3f seconds.",duration/1000);
    }
    // finding the shortest path from current node to objective using known information about nodes
    public static ArrayList<Node> dijkstra(Node start, Node target) throws Exception {
        BinaryHeap unvisitedNodes =new BinaryHeap();
        // initialize all necessary data fields to default value
        for (int i = 0;i< nodes.length;i++) {
            for (int j = 0; j < nodes[0].length; j++) {
                nodes[i][j].distance = Double.MAX_VALUE;
                nodes[i][j].parent = null;
                nodes[i][j].isVisited = false;
            }
        }
        start.distance = 0;
        unvisitedNodes.insert(start);
        while(!unvisitedNodes.isEmpty()){
            Node current = unvisitedNodes.deleteMin();
            // if node is processed before, pass
            if (current.isVisited){
                continue;
            }
            current.isVisited = true;
            for (Edge edge: current.neighbors){
                Node neighbor= edge.second;
                if (!neighbor.isVisited && knownNodes.contains(neighbor) == null && neighbor.type != 1){
                    double newDistance = current.distance + edge.weight;
                    if (newDistance < neighbor.distance){
                        neighbor.distance = newDistance;
                        neighbor.parent = current;
                        unvisitedNodes.updateDistance(neighbor);
                    }
                }
            }
        }
        // create path by going backwards
        ArrayList<Node> path = new ArrayList<>();
        Node current = target;
        while (current!= null){
            path.add(0,current);
            current = current.parent;
        }

        return path;
    }
    // control the path, move until see an impassable node, if there exists impassable node in path return where you are
    public static Node controllingPath(ArrayList<Node> path,PrintStream stream,int radiusOfSight,int index){
        // Traverse all nodes except the last one
        Node firstNode = path.get(0);
        seeNodes(radiusOfSight,firstNode);
        int x= 1;
        while (Math.sqrt(Math.pow(firstNode.x-path.get(x).x,2)+Math.pow(firstNode.y-path.get(x).y,2)) <= radiusOfSight) {
            // Check if the next node within the radius is impassable
            if (path.get(x).type >= 2) {
                knownNodes.insert(path.get(x));
                return firstNode; // Exit and return the current index if path is blocked
            }
            if (x<path.size()-1){
                x++;
            }
            else{
                break;
            }
        }
        // traverse all nodes for node in the path
        for (int i = 1; i < path.size() - 1; i++) {
            Node currentNode = path.get(i);
            seeNodes(radiusOfSight,currentNode);
            stream.println("Moving to " + currentNode.x + "-" + currentNode.y);
            // Check if there's an impassable node within the radius of sight, if there is return where you are
            for (int j=1;j < path.size()-i ; j++){
                if (knownNodes.contains(path.get(i+j))!=null){
                    stream.println("Path is impassable!");
                    return currentNode;
                }
            }
        }
        // Handle the last node (objective)
        Node objective = path.get(path.size() - 1); // Last node in the path
        stream.println("Moving to " + objective.x + "-" + objective.y);
        seeNodes(radiusOfSight,objective);
        index++; // Increment the objective index
        stream.println("Objective " + index + " reached!");
        return objective; // if you reach objective return objective

    }
    public static void processObjectives(ArrayList<Objective> objectives, Node[] startingNode, PrintStream stream, int radiusOfSight) throws Exception {
        int index = 0;  // Objective index, starts from 0
        Node currentStartNode = startingNode[0]; // Track the current start node

        for (int i = 0; i < objectives.size(); i++) {
            // Find the shortest path to the current objective using Dijkstra
            Node targetNode = nodes[objectives.get(i).x][objectives.get(i).y];
            ArrayList<Node> path = dijkstra(currentStartNode, targetNode);

            // Try to control the path until it's feasible
            currentStartNode = controllingPath(path, stream, radiusOfSight,  index);
            while (!currentStartNode.equals(targetNode)) {
                path = dijkstra(currentStartNode, targetNode); // Recalculate path if needed
                currentStartNode = controllingPath(path, stream, radiusOfSight,  index);
            }
            // if wizard offers help choose one option
            if (objectives.get(i).offersHelp){
                Node nextObjective = nodes[objectives.get(i+1).x][objectives.get(i+1).y];
                wizardHelp(targetNode,nextObjective,objectives.get(i).options,stream);
            }
            // After reaching an objective, increment the index (for the next one)
            index++;
        }
    }
    // While moving on the path see nodes inside radius of sight
    public static void seeNodes(int radiusOfSight, Node currentNode){
        int lowerX = currentNode.x-radiusOfSight;
        int lowerY = currentNode.y -radiusOfSight;
        int greaterX = currentNode.x+radiusOfSight;
        int greaterY = currentNode.y+ radiusOfSight;
        if (lowerX<0){
            lowerX =0;
        }
        if (lowerY<0){
            lowerY = 0;
        }
        if (greaterX>nodes.length-1){
            greaterX = nodes.length-1;
        }
        if (greaterY>nodes[0].length-1){
            greaterY = nodes[0].length-1;
        }
        for (int j = lowerX; j<=greaterX; j++){
            for (int k = lowerY ; k <= greaterY;k++){
                double distance = Math.sqrt(Math.pow(currentNode.x-nodes[j][k].x,2)+Math.pow(currentNode.y-nodes[j][k].y,2));
                if (distance <= radiusOfSight && nodes[j][k].type >= 2){
                    knownNodes.insert(nodes[j][k]);
                }
            }
        }
    }
    // if wizard offers help, calculate distance for these options and choose best one
    public static void wizardHelp(Node current,Node nextObjective, ArrayList<Integer> options,PrintStream stream) throws Exception {
        double minDistance = Double.MAX_VALUE;
        int[][] types = copyMap();
        int bestChoice = -1;
        ArrayList<Node> known;
        for (int i = 0;i<options.size();i++){
            known = modifyMap(options.get(i));
            ArrayList<Node> path = dijkstra(current,nextObjective);
            double distance= path.get(path.size()-1).distance;
            if (distance<minDistance){
                minDistance = distance;
                bestChoice = options.get(i);
            }
            restoreMap(known,types);
        }
        stream.println("Number "+bestChoice+" is chosen!");
        modifyMap(bestChoice);
        //According to chosen option modify map

    }
    // change the types of nodes which have specific type to zero
    public static ArrayList<Node> modifyMap(int oldType){
        ArrayList<Node> known = new ArrayList<>();
        for(int i =0; i<nodes.length; i++){
            for (int j =0 ; j<nodes[0].length; j++){
                if(nodes[i][j].type == oldType){
                    if(knownNodes.contains(nodes[i][j])!=null){
                        known.add(nodes[i][j]);
                        knownNodes.remove(nodes[i][j]);
                    }
                    nodes[i][j].type = 0;
                }
            }
        }
        return known;
    }
    // To hold the initial type of nodes copy their type to new matrix
    public static int[][] copyMap(){
        int[][] types = new int[nodes.length][nodes[0].length];
        for (int i =0;i<nodes.length;i++){
            for (int j = 0;j<nodes[0].length;j++){
                types[i][j] = nodes[i][j].type;
            }
        }
        return types;
    }
    // after trying one option restore map to try other option
    public static void restoreMap(ArrayList<Node> known, int[][] types){
        for (int i =0;i<nodes.length;i++){
            for (int j =0;j<nodes[0].length;j++){
                nodes[i][j].type = types[i][j];
            }
        }
        for (Node node : known) {
            knownNodes.insert(node);
        }
    }
}
