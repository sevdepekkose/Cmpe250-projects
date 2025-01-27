public class AvlTree {
    public TreeNode root;
    public AvlTree(){
        this.root =null;
    }
    public int getHeight(TreeNode node){
        if (node == null){
            return 0;
        }
        else{
            return node.height;
        }
    }
    public int getBalance(TreeNode node){
        if (node== null){
            return 0;
        }
        else{
            return getHeight(node.left) - getHeight(node.right);
        }
    }
    public TreeNode rightRotation(TreeNode node){
        TreeNode tmp = node.left;
        TreeNode tmpRight = tmp.right;
        TreeNode tmpParent = node.parent;
        tmp.right = node;
        node.parent = tmp;
        node.left = tmpRight;
        if (tmpRight != null){
            tmpRight.parent = node;
        }
        tmp.parent = tmpParent;
        if (tmpParent == null){
            root = tmp;
        }
        else if (node == tmpParent.left){
            tmpParent.left = tmp;
        }
        else if (node == tmpParent.right){
            tmpParent.right = tmp;
        }

        node.height = Math.max(getHeight(node.left),getHeight(node.right))+1;
        tmp.height = Math.max(getHeight(tmp.left),getHeight(tmp.right))+1;
        return tmp;
    }
    public TreeNode leftRotation(TreeNode node){
        TreeNode tmp = node.right;
        TreeNode tmpLeft = tmp.left;
        TreeNode tmpParent = node.parent;
        tmp.left = node;
        node.parent = tmp;
        node.right = tmpLeft;
        if (tmpLeft != null){
            tmpLeft.parent = node;
        }
        tmp.parent = tmpParent;
        if ( tmpParent == null){
            root =tmp;
        }
        else if (node == tmpParent.left){
            tmpParent.left = tmp;
        }
        else if (node == tmpParent.right){
            tmpParent.right = tmp;
        }

        node.height = Math.max(getHeight(node.left),getHeight(node.right))+1;
        tmp.height = Math.max(getHeight(tmp.left),getHeight(tmp.right))+1;
        return tmp;
    }

    public void insert(TreeNode root,TreeNode parkingLot){
        // find the appropriate position for node and insert it
        if (root == null){
            this.root = parkingLot;
            return;
        }
        TreeNode current = root;
        while(true){
            if (parkingLot.element.compareTo(current.element)<0){
                if (current.left == null){
                    current.left = parkingLot;
                    parkingLot.parent = current;
                    break;
                }
                current = current.left;
            }
            else if (parkingLot.element.compareTo(current.element)>0){
                if (current.right == null){
                    current.right = parkingLot;
                    parkingLot.parent = current;
                    break;
                }
                current = current.right;
            }
        }
        // after finding right position rotate the tree if needed
        current = parkingLot.parent;
        while (current != null){
            current.height = (Math.max(getHeight(current.left),getHeight(current.right)))+1;  //calculate new height
            int balancingFactor = getBalance(current);
            if (balancingFactor>1 && parkingLot.element.compareTo(current.left.element)<0){
                current = rightRotation(current);
            }
            if(balancingFactor<-1 && parkingLot.element.compareTo(current.right.element)>0){
                current = leftRotation(current);
            }
            if (balancingFactor>1 && parkingLot.element.compareTo(current.left.element)>0){
                current.left = leftRotation(current.left);
                current = rightRotation(current);
            }
            if (balancingFactor<-1 && parkingLot.element.compareTo(current.right.element)<0){
                current.right = rightRotation(current.right);
                current = leftRotation(current);
            }
            current = current.parent;
        }

    }
    // find the node which is wanted to delete and return it
    public TreeNode delete(TreeNode root, TreeNode node){
        // find the node which will be deleted
        if (root == null){
            return root;
        }
        if (node.element.compareTo(root.element)<0){
            root.left = delete(root.left,node);
        }
        else if (node.element.compareTo(root.element)>0){
            root.right = delete(root.right,node);
        }
        else{
            // after finding,  if it has one or zero child
            if (root.left == null){
                TreeNode tmp = root.right;
                if (tmp != null){
                    tmp.parent = root.parent;
                }
                return tmp;
            }
            else if (root.right ==null){
                TreeNode tmp = root.left;
                tmp.parent = root.parent;
                return tmp;
            }
            // if node has two child
            TreeNode tmp = getMinValueNode(root.right);  // find the smallest node at the right subtree
            root.element = tmp.element;  // replace it to the place of node
            root.right = delete(root.right,tmp);  // update new root of right subtree
        }
        // after deleting the node control the height and rotate the tree if needed
        if (root!= null){
            root.height = Math.max(getHeight(root.left),getHeight(root.right))+1;
            int balancingFactor = getBalance(root);
            if (balancingFactor>1 && getBalance(root.left)>=0){
                return rightRotation(root);
            }
            if (balancingFactor>1 && getBalance(root.left)<0){
                root.left = leftRotation(root.left);
                return rightRotation(root);
            }
            if (balancingFactor<-1 && getBalance(root.right)<=0){
                return leftRotation(root);
            }
            if (balancingFactor<-1 && getBalance(root.right)>0){
                root.right = rightRotation(root.right);
                return leftRotation(root);
            }
        }
        return root;
    }
    // find the minimum node in the tree
    public TreeNode getMinValueNode(TreeNode node){
        TreeNode current = node;
        while(current.left != null){
            current = current.left;
        }
        return current;
    }
    // find the parking lot having given capacity constraint
    public TreeNode findParkingLot(int capacity_constraint,TreeNode root){
        TreeNode current = root;
        while(current != null) {
            if (current.element.capacity == capacity_constraint){
                return current;
            }
            else if (current.element.capacity < capacity_constraint) {
                current = current.right;
            }
            else {
                current = current.left;
            }
        }
        return null;
    }
    // find the largest parking lot smaller than the given node using parent-child relationship
    public TreeNode findLargest(TreeNode node,TreeNode root){
        TreeNode parent = null;
        TreeNode current = root;
        if (node.parent != null&& node.parent.right == node&& node.left!= null){
            parent = node.left;
            while(parent.right != null){
                parent = parent.right;
            }
        }
        else if (node.parent != null&& node.parent.right == node&& node.left== null){
            parent = node.parent;
        }
        else if (node.parent == null && node.left != null){
            parent = node.left;
            while(parent.right != null){
                parent = parent.right;
            }
        }
        else if(node.parent != null && node.parent.left == node&& node.left != null){
            parent = node.left;
            while(parent.right != null){
                parent = parent.right;
            }
        }
        else{
            parent = node.parent;
            while (parent != null && node ==parent.left){
                node = parent;
                parent = parent.parent;
            }
        }
        return parent;
    }
    // find the largest parking lot smaller than the given capacity traversing the tree
    public TreeNode findLargest(TreeNode root,int capacity){
        TreeNode current = root;
        TreeNode candidate = null;
        while(current != null){
            if (current.element.capacity<capacity){
                candidate =current;
                current = current.right;
            }
            else if(current.element.capacity>capacity){
                current = current.left;
            }
        }
        return candidate;
    }
    public void inOrder(TreeNode node){
        if (node!= null){
            inOrder(node.left);
            System.out.print(" "+node);
            inOrder(node.right);
        }
    }
    // find the smallest parking lot greater than the given node using parent-child relationship
    public TreeNode findSmallest(TreeNode node, TreeNode root){
        TreeNode parent = null;
        if (node.parent !=null && node.parent.left == node && node.right !=null){
            parent = node.right;
            while(parent.left!=null){
                parent = parent.left;
            }
        }
        else if (node.parent !=null && node.parent.left == node && node.right ==null){
            parent = node.parent;
        }
        else if (node.parent == null && node.right!= null){
            parent = node.right;
            while(parent.left!=null){
                parent = parent.left;
            }
        }
        else if (node.parent == null && node.right == null){
            parent = null;
        }
        else if (node.parent != null && node.parent.right == node && node.right != null){
            parent = node.right;
            while(parent.left!=null){
                parent = parent.left;
            }
        }
        else {
            parent = node.parent;
            while (parent != null && node == parent.right ){
                node = parent ;
                parent = parent.parent;
            }
        }
        return parent;
    }
    // find the smallest parking lot greater than the given parking lot using capacity and root of tree
    public TreeNode findSmallest(TreeNode root,int capacity){
        TreeNode current = root;
        TreeNode candidate = null;
        while(current != null){
            if (current.element.capacity<=capacity){
                current = current.right;
            }
            else if(current.element.capacity>capacity){
                candidate =current;
                current = current.left;
            }

        }
        return candidate;
    }
}
