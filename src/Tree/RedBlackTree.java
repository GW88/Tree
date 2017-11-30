package Tree;

/**
 * Red Black Tree With Key, Value pair. K is Type of Comparable
 * No duplicate values are allowed in Tree.
 * For Color is used Enumeration. You can Change it with boolean if you need.
 * Node has 3 pointer and one additional info >> Color
 * Pointers: Left Child, Right Child, Parent.
 * Every new node added to tree is automatically Red. no need to paint new node in Red
 * Code is written for beginners with the manner that even Dummies Can understand it.
 * if any bug occured while testing it, please let me know, i will fix the problem and
 * will write the Name of Person in description who has pointed to a mistake
 *
 * p.s i'm not using NIL nodes, i don't like them, use your imagination.
 */
public class RedBlackTree<K extends Comparable<K>,V> {

    public enum Color {RED, BLACK}

    private static class RedBlackNode<K, V> {
        private K key;
        private V value;
        private RedBlackNode<K, V> leftChild;
        private RedBlackNode<K, V> rightChild;
        private RedBlackNode<K, V> parent;
        private Color color;

        private RedBlackNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.leftChild = this.rightChild = this.parent = null;
            this.color = Color.RED;
        }

        private RedBlackNode() {

        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public RedBlackNode<K, V> getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(RedBlackNode<K, V> leftChild) {
            this.leftChild = leftChild;
        }

        public RedBlackNode<K, V> getRightChild() {
            return rightChild;
        }

        public void setRightChild(RedBlackNode<K, V> rightChild) {
            this.rightChild = rightChild;
        }

        public RedBlackNode<K, V> getParent() {
            return parent;
        }

//        public void setParent(RedBlackNode<K, V> parent) {
//            this.parent = parent;
//        }
//
//        public void setColor(Color color) {
//            this.color = color;
//        }

        public Color getColor() {
            return color;
        }
    }

    private RedBlackNode<K, V> root;
    private int size = 0;

    /**
     * Add Method. Key can not be null, and Key can not be null.
     * balanceAfterInsertion(RedBlackTree</K,V> value) is helping add method to balance the tree
     * @param key ..
     * @param value ..
     */
    public void add(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key Can't be Null, Illegal Argument ");
        }
        RedBlackNode<K, V> node = new RedBlackNode<>(key, value);
        if (root == null) {
            root = node;
            root.color = Color.BLACK;
            size++;
        } else {
            RedBlackNode<K, V> current = root;
            RedBlackNode<K, V> temporary;
            while (true) {
                temporary = current;
                if (current.key.equals(node.key)) {
                    return;
                }
                if (key.compareTo(current.key) < 0) {
                    current = current.leftChild;
                    if (current == null) {
                        temporary.leftChild = node;
                        node.parent = temporary;
                        size++;
                        balanceAfterInsertion(node);
                        return;
                    }
                } else {
                    current = current.rightChild;
                    if (current == null) {
                        temporary.rightChild = node;
                        node.parent = temporary;
                        size++;
                        balanceAfterInsertion(node);
                        return;
                    }
                }
            }
        }


    }

    /**
     * As usual, We are Deleting successor or predecessor, which can be Leaf node
     * or Parent with At list one child. Red Nodes are Deleted Directly, Black Node
     * with Red child are Deleted with assigning Red child Data to Black parent,
     * and again Deleting Re leaf, atherwise if we delete Black Leaf Node we Have
     * Double Black Violation and Tree need to be balances, dirty job is assigned
     * to >>balanceAfterRemoval(currentNode); Method. wich has 5 more helper methods.
     * Deletion Method,
     * @param key ..
     * @return value
     */
    public V delete(K key){
        RedBlackNode<K,V> curentValue = contains(key);
        RedBlackNode<K,V> valueToReturn = curentValue;
        if (curentValue.leftChild!=null && curentValue.rightChild !=null){
            if (curentValue.key.equals(root.key) || isLeftNode(curentValue)){
                RedBlackNode<K,V> pro = predecessor(curentValue.leftChild);
                curentValue.setKey(pro.getKey());
                curentValue.setValue(pro.getValue());
                curentValue = pro;

            }else{
                RedBlackNode<K,V> suc = successor(curentValue.rightChild);
                curentValue.setKey(suc.getKey());
                curentValue.setValue(suc.getValue());
                curentValue = suc;
            }

        }
        if (curentValue.leftChild == null && curentValue.rightChild == null && curentValue.color.equals(Color.RED)) {
            if (curentValue.equals(parentNode(curentValue).leftChild)) {
                parentNode(curentValue).setLeftChild(null);
                size--;
            } else {
                parentNode(curentValue).setRightChild(null);
                size--;
            }
        }else if (curentValue.leftChild!=null && curentValue.leftChild.color.equals(Color.RED) ){
            curentValue.setKey(curentValue.leftChild.getKey());
            curentValue.setValue(curentValue.leftChild.getValue());
            curentValue.setLeftChild(null);
            size--;
        }else if (curentValue.rightChild!=null && curentValue.rightChild.color.equals(Color.RED)){
            curentValue.setKey(curentValue.rightChild.getKey());
            curentValue.setValue(curentValue.rightChild.getValue());
            curentValue.setRightChild(null);
            size--;
        } else if (curentValue.leftChild == null && curentValue.rightChild == null
                && curentValue.color.equals(Color.BLACK) || curentValue.key.equals(root.key)){
            if (curentValue.equals(root)){
                setRoot(null);
                size--;
            }else if (curentValue.equals(parentNode(curentValue).leftChild)){
                balanceAfterRemoval(curentValue);
                parentNode(curentValue).setLeftChild(null);
                size--;
            }else{
                balanceAfterRemoval(curentValue);
                parentNode(curentValue).setRightChild(null);
                size--;
            }
        }

        if (valueToReturn == null) {
            return null;
        }
        return valueToReturn.getValue();
    }

    /**
     *
     * @return size
     */
    public int size() {
        return size;
    }

    /**
     *
     * @return boolean value, if empty true else false
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * clear the tree, set root to null and tree will be Garbage Collected.
     */
    public void clear()
    {
        size = 0;
        root = null;
    }

    public String getRoot() {
        return "Key " + root.getKey() + " Value " + root.getValue() + " Color " + root.getColor();
    }
    private void setRoot(RedBlackNode<K,V> root){
        this.root = root;
    }

    /**
     *
     * @param key if Key is in tree
     * @return value
     */
    public V search(K key){
        RedBlackNode<K,V> temp = root;
        while (!key.equals(temp.key)){
            if (key.compareTo(temp.key)<0){
                temp = temp.leftChild;
            }else {
                temp = temp.rightChild;
            }
            if (temp == null){
                return null;
            }
        }
        return temp.getValue();

    }

    /**
     *
     * @return true is balanced
     */
    public boolean isBlanaced(){
        if (root == null){
            return true;
        }

        int balance = Math.abs(redBlackTreeHeight(root.leftChild) - redBlackTreeHeight(root.rightChild));
        if (balance > 1){
            return false;
        }
        return true;
    }


    /**
     * in Order DFS Deapth First Traversal
     */
    public void inOrder() {

        inOrderTraversal(root);
    }

    private void inOrderTraversal(RedBlackNode<K, V> value) {
        if (value != null) {
            inOrderTraversal(value.leftChild);
            System.out.println("Key " + value.getKey() + " Value " + value.getValue() + " Color " + value.getColor());
            inOrderTraversal(value.rightChild);
        }
    }


    /**
     *
     * @param value if its red and parent is red correct tree, call method recursivly until it hits the
     *        root or null.
     */
    private void balanceAfterInsertion(RedBlackNode<K,V> value){
        if (value == root || value == null){
            return;
        }

        if (isRedNode(value) && isRedNode(parentNode(value))){
            correctTree(value);

        }

        balanceAfterInsertion(value.parent);
    }


    /**
     * mathod that does the dirty job, balance the tree after insertion.
     * with a lot of helper methods.
     * @param value ....
     */
    private void correctTree(RedBlackNode<K,V> value){
        if (isLeftNode(parentNode(value))){
            if (rightSiblingIsRed(value)){
                flipColors(parentNode(value), Color.BLACK);
                flipColors(grandParentNode(value), Color.RED);
                flipColors(grandParentNode(value).getRightChild(), Color.BLACK);
            }else{
                if (isLeftNode(value)){
                    rotateRight(grandParentNode(value));
                    flipColors(value, Color.RED);
                    flipColors(parentNode(value), Color.BLACK);
                    if (value.parent.rightChild !=null){
                        value.parent.rightChild.color = Color.RED;
                    }
                }else if (!isLeftNode(value) && isLeftNode(parentNode(value))){
                    rotateLeftRight(grandParentNode(value));
                    flipColors(value, Color.BLACK);
                    flipColors(value.rightChild, Color.RED);
                    flipColors(value.leftChild, Color.RED);
                }
            }
        }else if (isRightNode(parentNode(value))){
            if (leftSiblingIsRed(value)){
                flipColors(parentNode(value), Color.BLACK);
                flipColors(grandParentNode(value), Color.RED);
                flipColors(grandParentNode(value).getLeftChild(), Color.BLACK);
            }else{
                if (isRightNode(value)){
                    rotateLeft(grandParentNode(value));
                    flipColors(value, Color.RED);
                    flipColors(parentNode(value), Color.BLACK);
                    if (value.parent.leftChild !=null){
                        value.parent.leftChild.color = Color.RED;
                    }
                }else if (!isRightNode(value) && isRightNode(parentNode(value))){
                    rotateRightLeft(grandParentNode(value));
                    flipColors(value, Color.BLACK);
                    flipColors(value.leftChild, Color.RED);
                    flipColors(value.rightChild, Color.RED);
                }
            }

        }

        flipColors(root, Color.BLACK);
    }

    /**
     *
     * @param value calls it self until hits the null, this method has 5 more hellper method.
     *
     */
    private void balanceAfterRemoval(RedBlackNode<K,V> value){
        if (value.parent == null){
            return;
        }
        balanceAfterRemoval1(value);
    }
    private void balanceAfterRemoval1(RedBlackNode<K,V> value){
        RedBlackNode<K,V> sibling;
        if (isLeftNode(value)){
            sibling = parentNode(value).getRightChild();
        }else{
            sibling = parentNode(value).getLeftChild();
        }
        if (colorOFNode(sibling).equals(Color.RED)){
            if (isLeftNode(sibling)){
                flipColors(parentNode(sibling), Color.RED);
                flipColors(sibling, Color.BLACK);
                rotateRight(parentNode(sibling));
            }else {
                flipColors(parentNode(sibling), Color.RED);
                flipColors(sibling, Color.BLACK);
                rotateLeft(parentNode(sibling));
            }
        }
        balanceAferRemoval2(value);
    }
    private void balanceAferRemoval2(RedBlackNode<K,V> value){
        RedBlackNode<K,V> sibiling;
        if (isLeftNode(value)) {
            sibiling = parentNode(value).getRightChild();
        }else{
            sibiling = parentNode(value).getLeftChild();
        }
        if (value.parent.color.equals(Color.BLACK) && sibiling.color.equals(Color.BLACK) &&
                colorOFNode(sibiling.leftChild).equals(Color.BLACK) &&
                colorOFNode(sibiling.rightChild).equals(Color.BLACK)){
            flipColors(sibiling, Color.RED);
            balanceAfterRemoval(value.parent);
        }else{
            balanceAferRemoval3(value);
        }
    }
    private void balanceAferRemoval3(RedBlackNode<K,V> value){
        RedBlackNode<K,V> sibling;
        if (isLeftNode(value)){
            sibling = parentNode(value).getRightChild();
        }else {
            sibling = parentNode(value).getLeftChild();
        }
        if (value.parent.color.equals(Color.RED) && sibling.color.equals(Color.BLACK)
                && colorOFNode(sibling.leftChild).equals(Color.BLACK)
                && colorOFNode(sibling.rightChild).equals(Color.BLACK)){
            flipColors(parentNode(value), Color.BLACK);
            flipColors(sibling, Color.RED);
            return;
        }else{
            balanceAfterRemoval4(value);

        }

    }
    private void balanceAfterRemoval4(RedBlackNode<K,V> value){
        RedBlackNode<K,V> sibling;
        if (isLeftNode(value)){
            sibling = parentNode(value).getRightChild();
        }else{
            sibling = parentNode(value).getLeftChild();
        }
        if (sibling.color.equals(Color.BLACK)){
            if (colorOFNode(sibling.leftChild).equals(Color.RED) && isRightNode(sibling)){
                flipColors(sibling, Color.RED);
                flipColors(sibling.leftChild, Color.BLACK);
                rotateRight(sibling);
            }else if (colorOFNode(sibling.rightChild).equals(Color.RED) && isLeftNode(sibling)){
                flipColors(sibling, Color.RED);
                flipColors(sibling.rightChild, Color.BLACK);
                rotateLeft(sibling);
            }
        }

        balanceAfterRemoval5(value);
    }
    private void balanceAfterRemoval5(RedBlackNode<K,V> value){
        RedBlackNode<K,V> sibling = null;
        if (isLeftNode(value)){
            sibling = parentNode(value).getRightChild();
        }else {
            sibling = parentNode(value).getLeftChild();
        }
        if (sibling !=null) {
            sibling.color = sibling.parent.color;
            sibling.parent.color = Color.BLACK;
        }

        if (isLeftNode(value)){
            if (sibling!=null) {
                sibling.rightChild.color = Color.BLACK;
                rotateLeft(parentNode(sibling));
            }
        }else{
            if (sibling!=null) {
                sibling.leftChild.color = Color.BLACK;
                rotateRight(parentNode(sibling));
            }
        }
        flipColors(root, Color.BLACK);
    }

    /**
     * You guess what it does, rotates tree to the left. for example
     *      50                                 60
     *        \                               /   \
     *         60   --> after left rotation    50     70
     *          \
     *           70
     * @param value
     */
    private void rotateLeft(RedBlackNode<K,V> value) {
        if (value != null) {
            RedBlackNode<K,V>  temporary = value.rightChild;
            value.rightChild = temporary.leftChild;
            if (temporary.leftChild != null)
                temporary.leftChild.parent = value;
            temporary.parent = value.parent;
            if (value.parent == null)
                root = temporary;
            else if (value.parent.leftChild == value)
                value.parent.leftChild = temporary;
            else
                value.parent.rightChild = temporary;
            temporary.leftChild = value;
            value.parent = temporary;
        }
    }


    /**
     * rotates tree to the right
     *         50            40
     *        /            /    \
     *      40  -->      30     50
     *     /
     *    30
     *
     * @param value
     */
    private void rotateRight(RedBlackNode<K,V> value) {
        if (value != null) {
            RedBlackNode<K,V> temporary = value.leftChild;
            value.leftChild = temporary.rightChild;
            if (temporary.rightChild != null) temporary.rightChild.parent = value;
            temporary.parent = value.parent;
            if (value.parent == null)
                root = temporary;
            else if (value.parent.rightChild == value)
                value.parent.rightChild = temporary;
            else value.parent.leftChild =temporary;
            temporary.rightChild = value;
            value.parent = temporary;
        }

    }

    /**
     * left right rotation. first it rotates value left child and the value to the right.
     * @param value
     */
    private void rotateLeftRight(RedBlackNode<K,V> value){
        rotateLeft(value.leftChild);
        rotateRight(value);

    }

    /**
     *
     * @param value
     */
    private void rotateRightLeft(RedBlackNode<K,V> value){
        rotateRight(value.rightChild);
        rotateLeft(value);
    }

    /**
     *
     * @param value
     * @param <K>
     * @param <V>
     * @return Black or value or the node
     */
    private static <K,V> Color colorOFNode(RedBlackNode<K,V> value){
        return value == null ? Color.BLACK : value.color;
    }

    private static <K,V> boolean isRedNode(RedBlackNode<K,V> value){
        return value == null || colorOFNode(value).equals(Color.RED);
    }

    private static <K,V> RedBlackNode<K,V> parentNode(RedBlackNode<K,V> value){
        return value == null ? null : value.getParent();
    }
    private static <K,V> RedBlackNode<K,V> grandParentNode(RedBlackNode<K,V> value){
        return value == null ? null : value.getParent().getParent();
    }

    private static<K,V> void flipColors(RedBlackNode<K,V> value, Color color){
        if (value !=null){
            value.color = color;
        }
    }
    private static <K,V> boolean isLeftNode(RedBlackNode<K,V> value){
//        return value != null && value.key.equals(parentNode(value).getLeftChild().key);
        boolean bo = false;
        RedBlackNode<K,V> temp = parentNode(value);
        if (temp.getLeftChild() !=null && temp.getLeftChild().equals(value)){
            bo = true;
        }else{
            bo = false;
        }
        return bo;
    }
    private static <K,V> boolean isRightNode(RedBlackNode<K,V> value){
//        return value != null && value.key.equals(parentNode(value).getRightChild().key);
        boolean bo = false;
        RedBlackNode<K,V> temp = parentNode(value);
        if (temp.getRightChild() !=null && temp.getRightChild().equals(value)) {
            bo = true;
        }else {
            bo = false;
        }
        return bo;
    }
    private static <K,V> boolean rightSiblingIsRed(RedBlackNode<K,V> value){
        return value.parent.parent.rightChild !=null && value.parent.parent.rightChild.color.equals(Color.RED);
    }
    private static <K,V> boolean leftSiblingIsRed(RedBlackNode<K,V> value){
        return value.parent.parent.leftChild !=null && value.parent.parent.leftChild.color.equals(Color.RED);
    }

    private int redBlackTreeHeight(RedBlackNode<K,V> value){
        if (value == null){
            return 0;
        }

        int height = 1 + Math.max(redBlackTreeHeight(value.leftChild), redBlackTreeHeight(value.rightChild));

        return height;
    }
    private RedBlackNode<K,V> contains(K value){
        RedBlackNode<K,V> temporary = root;
        while (!value.equals(temporary.key)){
            if (value.compareTo(temporary.key)<0){
                temporary = temporary.leftChild;
            }else{
                temporary = temporary.rightChild;
            }
        }

        return temporary;
    }


    private RedBlackNode<K,V> predecessor(RedBlackNode<K,V> value){
        if (value.rightChild == null){
            return value;
        }
        return predecessor(value.rightChild);
    }
    private RedBlackNode<K,V> successor(RedBlackNode<K,V> value){
        if (value.leftChild == null){
            return value;
        }
        return successor(value.leftChild);
    }

}
