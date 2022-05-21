
public class AVL {
    private AVLNode root;      /* Pointer to the root of the tree */
    private int noOfNodes;     /* No of nodes in the tree */
    private AVLNode nodeToDelete; //created a new field to point the deleted node

    /*******************
     * Constructor: Initializes the AVLTree
     *******************/
    public AVL() {
        root = null;
        noOfNodes = 0;
    }

    /*******************
     * Returns a pointer to the root of the tree
     *******************/
    public AVLNode Root() {
        return root;
    }

    /*******************
     * Returns the number of nodes in the tree
     *******************/
    public int NoOfNodes() {
        return noOfNodes;
    }

    /*******************
     * Inserts the key into the AVLTree. Returns a pointer to
     * the inserted node
     *******************/
    public AVLNode Insert(int key) {
        return addChild(this.root, key);
    } //end-Insert

    //helper insert method
    private AVLNode addChild(AVLNode parent, int data) {
        if (parent == null) {
            parent = new AVLNode(data);
            AVLNode temp = Find(data); //find the key and store the object in temp reference
            if (temp != null && temp.key == data) {
                parent.count++;
            } else noOfNodes++;
        } else if (data < parent.key) {
            parent.left = addChild(parent.left, data);
            if (height(parent.left) - height(parent.right) > 1) //checking the balanceFactor rule after insertion
                parent = (data < parent.left.key) ? rightRotate(parent) : doubleRotateLeft(parent);
        } else {
            if (parent.key == data) {
                parent.count++;
            }
            parent.right = addChild(parent.right, data);
            if (height(parent.left) - height(parent.right) < -1) //checking the balanceFactor rule after insertion
                parent = (data >= parent.right.key) ? leftRotate(parent) : doubleRotateRight(parent);
        }
        if (root == null) {
            root = parent;
        }
        return parent;
    }

    /*******************
     * Deletes the key from the tree (if found). Returns
     * 0 if deletion succeeds, -1 if it fails
     *******************/
    public int Delete(int key) {
        delete(this.root, key);
        return nodeToDelete == null ? -1 : 0;
    } //end-Delete

    //helper delete method
    private AVLNode delete(AVLNode node, int data) {
        AVLNode temp;
        if (node == null)
            return node;
        if (data < node.key)
            node.left = delete(node.left, data);
        else if (data > node.key)
            node.right = delete(node.right, data);
        else {
            if (node.count > 1) {
                node.count--;
                noOfNodes++;
            }
            if ((node.left == null) || (node.right == null)) {
                if (node.left != null) temp = node.left;
                else temp = node.right;
                if (temp == null) {
                    noOfNodes--;
                    temp = node;
                    nodeToDelete = temp;
                    node = null;
                } else {
                    noOfNodes--;
                    nodeToDelete = node;
                    node = temp;
                }
            } else {
                temp = maxValueNode(node.left);
                node.key = temp.key;
                node.count = temp.count;
                temp.count = 1;
                delete(node.left, temp.key);
            }
        }
        // If the tree had only one node then return
        if (node == null)
            return node;

        int balance = balanceFactor(node);

        // If this node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && balanceFactor(node.left) >= 0)
            return rightRotate(node);
        // Left Right Case
        if (balance > 1 && balanceFactor(node.left) < 0) {
            return doubleRotateRight(node);
           /* root.left = leftRotate(root.left);//same with doubleRotateRight but it's longer
            return rightRotate(root);*/
        }
        // Right Right Case
        if (balance < -1 && balanceFactor(node.right) <= 0)
            return leftRotate(node);

        // Right Left Case
        if (balance < -1 && balanceFactor(node.right) > 0) {
            return doubleRotateLeft(node);
            /*root.right = rightRotate(root.right); //same with doubleRotateLeft but it's longer
            return leftRotate(root);*/
        }
        return node;
    }
    /*******************
     * Searches the AVLTree for a key. Returns a pointer to the
     * node that contains the key (if found) or NULL if unsuccessful
     *******************/
    public AVLNode Find(int key) {
        AVLNode temp = root;
        while (temp != null) {
            if (key == temp.key)
                return temp;
            else if (key < temp.key)
                temp = temp.left;
            else
                temp = temp.right;
        }
        return null;
    } //end-Find

    /*******************
     * Returns a pointer to the node that contains the minimum key
     *******************/
    public AVLNode Min() {
        if (root == null) {
            return null;
        }
        AVLNode min = root;
        while (min.left != null) {
            min = min.left;
        }
        return min;
    } //end-Min

    /*******************
     * Returns a pointer to the node that contains the maximum key
     *******************/
    public AVLNode Max() {
        if (root == null) {
            return null;
        }
        AVLNode max = root;
        while (max.right != null) {
            max = max.right;
        }
        return max;
    } //end-Max

    /*******************
     * Returns the depth of tree. Depth of a tree is defined as
     * the depth of the deepest leaf node. Root is at depth 0
     *******************/
    public int Depth() {
        return height(root) - 1; //if we want to find the deepest leaf node we have to start our recursion from the root node.
    } //end-Depth

    /*******************
     * Performs an inorder traversal of the tree and prints [key, count]
     * pairs in sorted order
     *******************/
    public void Print() {
        System.out.println("The tree is : ");
        inOrder(root);
        System.out.println();
    } //end-Print

    private void inOrder(AVLNode node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.key + " ");
            inOrder(node.right);
        }
    }
    //rotation methods
    private AVLNode doubleRotateLeft(AVLNode node) {
        node.left = leftRotate(node.left);
        return rightRotate(node);
    }

    private AVLNode doubleRotateRight(AVLNode node) {
        node.right = rightRotate(node.right);
        return leftRotate(node);
    }

    private AVLNode leftRotate(AVLNode node) {
        AVLNode temp = node.right;
        if (node == root)
            root = temp;
        node.right = temp.left;
        temp.left = node;

        return temp;
    }

    private AVLNode rightRotate(AVLNode node) {
        AVLNode temp = node.left;
        if (node == root)
            root = temp;
        node.left = temp.right;
        temp.right = node;

        return temp;
    }

   /* public AVLNode deleteNode(AVLNode node, int key) {
        // STEP 1: PERFORM STANDARD BST DELETE

        if (node == null)
            return node;

        // If the value to be deleted is smaller than the root's value,
        // then it lies in left subtree
        if (key < node.key)
            node.left = deleteNode(node.left, key);

            // If the value to be deleted is greater than the root's value,
            // then it lies in right subtree
        else if (key > node.key)
            node.right = deleteNode(node.right, key);

            // if value is same as root's value, then This is the node
            // to be deleted
        else {
            // node with only one child or no child
            if ((node.left == null) || (node.right == null)) {

                AVLNode temp;
                if (node.left != null)
                    temp = node.left;
                else
                    temp = node.right;

                // No child case
                if (temp == null) {
                    temp = node;
                    node = null;
                } else // One child case
                    node = temp; // Copy the contents of the non-empty child

                temp = null;
            } else {
                // node with two children: Get the inorder successor (smallest
                // in the right subtree)
                AVLNode temp = maxValueNode(node.right);

                // Copy the inorder successor's data to this node
                node.key = temp.key;

                // Delete the inorder successor
                node.right = deleteNode(node.right, temp.key);
            }
        }

        // If the tree had only one node then return
        if (node == null)
            return node;


        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        //  this node became unbalanced)
        int balance = balanceFactor(node);

        // If this node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && balanceFactor(node.left) >= 0)
            return rightRotate(node);

        // Left Right Case
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && balanceFactor(node.right) <= 0)
            return leftRotate(node);

        // Right Left Case
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        // loop down to find the leftmost leaf //
        while (current.left != null)
            current = current.left;
        return current;
    }*/



    private int balanceFactor(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);//this is the rule for avl tree (left-right) must be 0,1 or -1
    }

    private int height(AVLNode node) {
        if (node == null)
            return 0;
        else {
            return (height(node.left) > height(node.right)) ? (height(node.left) + 1) : (height(node.right) + 1);
        }
    }

    //helper to use in the delete method
    private AVLNode maxValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.right != null)
            current = current.right;
        return current;
    }

}
