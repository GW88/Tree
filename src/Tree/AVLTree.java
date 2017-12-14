package Tree;

/**
 *AVL Tree. Self balancing Binary Search tree. Formal Parameter K is type of Comparable,
 *it can not be null or duplicate. Parameter V can carry anything. No stricts around it.
 */
public class AVLTree<K extends Comparable<K>, V> {

    private static class AVLNode<K,V>{
        private K key;
        private V value;
        private AVLNode<K,V> leftChild;
        private AVLNode<K,V> rightChild;
        private AVLNode<K,V> parent;

        AVLNode(K keyObject, V valueObject){
            key = keyObject;
            value = valueObject;
            leftChild = rightChild = parent = null;
        }

        private void setKey(K keyObject){
            key = keyObject;
        }
        private void setValue(V valueObject){
            value = valueObject;
        }
        private void setLefChild(AVLNode<K,V> left){
            leftChild = left;
        }
        private void setRightChild(AVLNode<K,V> right){
            rightChild = right;
        }
        private K getKey(){
            return key;
        }
        private V getValue(){
            return value;
        }
        private AVLNode<K,V> getLeftChild(){
            return leftChild;
        }
        private AVLNode<K,V> getRightChild(){
            return rightChild;
        }
    }

    private AVLNode<K,V> root;
    private int size = 0;


    /**
     * @param key K
     * @param value  V
     * throw IllegalArgumentException if Key is null
     * return if Key is Duplicate
     * checkBalance after every insertion.
     *
     */
    public void add(K key, V value){
        if (key == null){
            throw new IllegalArgumentException("Key can not be null");
        }
        AVLNode<K,V> data = new AVLNode<>(key, value);
        if (root == null){
            root = data;
            size++;
        }else{
            AVLNode<K,V> current = root;
            AVLNode<K,V> temp;
            while (true){
                temp = current;
                if (current.key.equals(data.key)){
                    return;
                }
                if (key.compareTo(current.key)<0){
                    current = current.leftChild;
                    if (current == null){
                        temp.setLefChild(data);
                        data.parent = temp;
                        checkBalance(data);
                        size++;
                        return;
                    }
                }else{
                    current = current.rightChild;
                    if (current == null){
                        temp.setRightChild(data);
                        data.parent = temp;
                        checkBalance(data);
                        size++;
                        return;
                    }
                }

            }

        }
    }

    public boolean isEmpty(){
        return root == null;
    }

    public void clear(){
        root = null;
    }

    public String search(K key){
        AVLNode<K,V> obj = contains(key);
        return "Key :" + obj.getKey() + " Value " + obj.getValue();
    }

    /**
     *
     * @param key K
     * @return true
     * checkbalance after every deletion on parent node that has been deleted
     */
    public  boolean remove(K key){
        AVLNode<K,V> object = contains(key);
        boolean bo = false;
        if (object.leftChild !=null && object.rightChild !=null){
            if (object.key.equals(root.key) || isLeft(object)){
                AVLNode<K,V> predecessor = predecessor(object.leftChild);
                object.setKey(predecessor.getKey());
                object.setValue(predecessor.getValue());
                object = predecessor;
            }else {
                AVLNode<K,V> successor = successor(object.rightChild);
                object.setKey(successor.getKey());
                object.setValue(successor.getValue());
                object = successor;
            }
        }
        if (object.equals(root)){
            if (object.leftChild!=null){
                object.setKey(object.leftChild.getKey());
                object.setValue(object.leftChild.getValue());
                object.setLefChild(null);
                size--;
                bo = true;
            }else if (object.rightChild!=null){
                object.setKey(object.rightChild.getKey());
                object.setValue(object.rightChild.getValue());
                object.setRightChild(null);
                size--; bo = true;
            }else{
                root = null;
                size--; bo=true;
            }
        }else if (object.leftChild == null && object.rightChild == null){
            if (isLeft(object)){
                avlNodeParent(object).setLefChild(null);
                checkBalance(avlNodeParent(object));
                size--;
                bo = true;
            }else {
                avlNodeParent(object).setRightChild(null);
                checkBalance(avlNodeParent(object));
                size--;
                bo = true;
            }
        }else if (object.leftChild !=null){
            object.setKey(object.leftChild.getKey());
            object.setValue(object.leftChild.getValue());
            object.setLefChild(null);
            checkBalance(object.parent);
            size--;
            bo=true;

        }else if (object.rightChild !=null){
            object.setKey(object.rightChild.getKey());
            object.setValue(object.rightChild.getValue());
            object.setRightChild(null);
            checkBalance(object.parent);
            size--;
            bo = true;
        }

        return bo;
    }

    public AVLNode<K,V> contains(K key){
        AVLNode<K,V> current = root;
        while (!key.equals(current.key)) {
            if (key.compareTo(current.key) < 0) {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }
        }
        return current;
    }

    private void rotateLeft(AVLNode<K,V> value) {
        if (value != null) {
            AVLNode<K,V> temporary = value.rightChild;
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
    private void rotateRight(AVLNode<K,V> value) {
        if (value != null) {
            AVLNode<K,V> temporary = value.leftChild;
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
    private void rotateLeftRight(AVLNode<K,V> value){
        rotateLeft(value.leftChild);
        rotateRight(value);

    }

    private void rotateRightLeft(AVLNode<K,V> value){
        rotateRight(value.rightChild);
        rotateLeft(value);
    }

    public void inOrder(){
        inOrder(root);
    }
    private void inOrder(AVLNode<K,V> value){
        if (value != null){
            inOrder(value.leftChild);
            System.out.println("Key "  + value.getKey() + " Value " + value.getValue());
            inOrder(value.rightChild);
        }
    }

    /**
     * calculate height
     * @param value
     * @return
     */
    private int treeHeight(AVLNode<K,V> value){
        int s;
        if (value == null){
            return -1;
        }else {

            s = 1+ Math.max(treeHeight(value.leftChild), treeHeight(value.rightChild));
        }

        return s;
    }

    /**
     * check the balance of tree on current node, move up to tree until you hit the root
     * @param value
     */
    private void checkBalance(AVLNode<K,V> value){
        if (treeHeight(value.leftChild) - treeHeight(value.rightChild) >1
                || treeHeight(value.leftChild) - treeHeight(value.rightChild) <-1){
            rebalance(value);
        }
        if (value.equals(root)){
            return;
        }
        checkBalance(value.parent);
    }

    /**
     * method balances tree.
     * @param value
     */
    private void rebalance(AVLNode<K,V> value){
        if (treeHeight(value.leftChild) - treeHeight(value.rightChild) >1){
            if (treeHeight(value.leftChild.leftChild) > treeHeight(value.leftChild.rightChild) ||
                    treeHeight(value.leftChild.leftChild) > treeHeight(value.rightChild)){
                rotateRight(value);
            }else {
                rotateLeftRight(value);
            }
        }else{
            if (treeHeight(value.rightChild.rightChild)> treeHeight(value.rightChild.leftChild) ||
                    treeHeight(value.rightChild.rightChild)> treeHeight(value.leftChild)){
                rotateLeft(value);
            }else {
                rotateRightLeft(value);
            }
        }


    }

    private AVLNode<K,V> predecessor(AVLNode<K,V> value){
        if (value.rightChild == null){
            return value;
        }

        return predecessor(value.rightChild);
    }

    private AVLNode<K,V> successor(AVLNode<K,V> value){
        if (value.leftChild == null){
            return value;
        }

        return successor(value.rightChild);
    }

    private static <K,V> AVLNode<K,V> avlNodeParent(AVLNode<K,V> value){
        return value == null ? null : value.parent;
    }
    private static <K,V> boolean isLeft(AVLNode<K,V> value){
        boolean bo;
        AVLNode<K,V> current = avlNodeParent(value);
        if (current.getLeftChild()!=null && current.getLeftChild().equals(value)){
            bo = true;
        }else {
            bo = false;
        }

        return bo;
    }

    public String getRoot(){
        String s;
        if(root == null){
            s = "[]";
        }else{
            s = "Key : " + root.getKey() + " Value :" + root.getValue();

        }

        return s;
    }


}
