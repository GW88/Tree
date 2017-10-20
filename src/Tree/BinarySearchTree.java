package Tree;


import java.util.*;

/**
 * Generic Binary Tree with Key-Value pair.
 * Key is type of Comparable
 *
 */
public class BinarySearchTree<K extends Comparable<K>, V> {

    public static class SearchNode<K, V>{
        private K key;
        private V value;

        public SearchNode(K key, V value){
            this.key = key;
            this.value = value;

        }
        public K getKey(){
            return key;
        }
        public V getValue(){
            return value;
        }
        public void setKey(K key){
            this.key= key;
        }
        public void setValue(V value){
            this.value = value;
        }
        SearchNode<K, V> leftChild;
        SearchNode<K, V> rightChild;


        public SearchNode<K,V> getLeftChild(){
            return leftChild;
        }
        public SearchNode<K,V> getRightChild(){
            return rightChild;
        }
        public void setLeftChild(SearchNode<K, V> leftChild){
            this.leftChild = leftChild;
        }
        public void setRightChild(SearchNode<K, V> rightChild){
            this.rightChild=rightChild;
        }
    }
    private SearchNode<K, V> root;
    private int size;

    /**
     * Key can not be null, if you try to add null,  null pointer exception will be thrown,
     * method do not accept duplicate values
     */
    public void add(K key, V value){
        if (key == null){
            throw new NullPointerException();
        }
        SearchNode<K, V> node = new SearchNode<>(key, value);
        if (root == null){
            root = node;
            size++;
        }else{
            SearchNode<K, V> current = root;
            SearchNode<K, V> parent;
            while (true){
                parent = current;
                if (current.key.equals(node.key)){
                    return;
                }
                if (key.compareTo(current.key)<0){
                    current = current.leftChild;
                    if (current == null){
                        parent.leftChild = node;
                        size++;
                        return;
                    }
                }else{
                    current = current.rightChild;
                    if (current == null){
                        parent.rightChild = node;
                        size++;
                        return;
                    }
                }
            }

        }

    }

    /**
     * better to search boolean value, who knows, maybe V is null.
     * but who cares return the null. suggestion is to change it to boolean.
     * @param key
     * @return
     */
    public V search(K key) {
        SearchNode<K, V> current = root;
        while (!key.equals(current.key)){
            if (key.compareTo(current.key)<0){
                current  = current.leftChild;
            }else{
                current = current.rightChild;
            }
            if (current == null){
                return  null;
            }
        }
        return current.getValue();
    }
    public int size(){
        return size;
    }


    /**
     * delete method deletes any give element from tree.
     * case 1, we have to deleter leafe, its prety straight forward and prety simple like in linkedlist.
     * case 2. we are deleting parent node with 2 child. to be more correct, we are replacing parent node with best given
     * option. best option in left subtree from root or any parent node is, left sub trees right most child. (bigest node)
     * is right subtree best option is right sebtree's left most child. (smallest node) , after  we replace it with best option
     * then we have to delete replaced node which may be leaf or parent with one left or rught child. depends in which tree
     * we are deleting node. for this particular example root is deleted separately, because i forgot root has no parent
     * and did not felt motivated enough to rewrite the code. so we roll as it is
     * case 3. well if we know node which has to be deleted in case tree is not leaf or parent with 2 child, then we exacly can say
     * that node which will be killed in case 3 is a node with one child. check if it has left or right child. force parent
     * to point that left or right and done. GC gonna do its work later.
     * @param key
     * @return
     */
   public boolean delete(K key){
       SearchNode<K,V> current = root;
       SearchNode<K, V> parent;
       if (key.equals(root.key)){
           deleteRoot(root);
           size--;
           return true;
       }
       if (current !=null) {
           while (!key.equals(current.key)) {
               parent = current;
               if (key.compareTo(current.key) < 0) {
                   current = current.leftChild;
                   if (current == null){
                       return false;
                   }
                   // case 1 deleting leaf
                   if (current.leftChild == null && current.rightChild == null) {
                       parent.setLeftChild(null);
                       size--;
                       return true;
                       // case 2 deleting parent with 2 child
                   }else if (current.leftChild !=null  && current.rightChild != null && key.equals(current.key)){
                       if (current.leftChild.rightChild == null){
                           SearchNode<K,V> p = current.getLeftChild();
                           p.rightChild = current.rightChild;
                           parent.setLeftChild(p);
                           size--;
                           return true;
                       }else {
                           SearchNode<K, V> leftcontainer = rightTraversal(current.leftChild);
                           case2LeftNode(current.leftChild);
                           SearchNode<K, V> temporary = current.leftChild;
                           SearchNode<K, V> temporaty1 = current.rightChild;
                           current = leftcontainer;
                           current.leftChild = temporary;
                           current.rightChild = temporaty1;
                           parent.setLeftChild(current);
                           size--;
                           return true;
                       }
                   }
                   // case 3 deleting parent with at list 1 child. if we jump over case 1 and 2
                   // we know exactly that node has only 1 child
                   else if (current.leftChild !=null && key.equals(current.key)){
                       parent.setLeftChild(current.getLeftChild());
                       size--;
                       return true;
                   }else if (current.rightChild !=null && key.equals(current.key)){
                       parent.setLeftChild(current.getRightChild());
                       size--;
                       return true;
                   }

               } else {
                   current = current.rightChild;
                   if (current == null){
                       return false;

                   }
                   //case 1
                   if (current.leftChild == null && current.rightChild == null) {
                       parent.setRightChild(null);
                       size--;
                       return true;

                   }
                   // case 2
                   else if (current.leftChild !=null && current.rightChild !=null){
                       if (key.equals(current.key)) {
                           // igive gaq gasaketebeli marcxena xeze
                           if (current.rightChild.leftChild == null) {
                               SearchNode<K,V> p1 = current.getRightChild();
                               p1.leftChild = current.leftChild;
                               parent.setRightChild(p1);
                               size--;
                               return true;
                           } else{
                               SearchNode<K, V> container = leftTraversal(current.rightChild);
                               case2Node(current.rightChild);
                               SearchNode<K, V> temp = current.leftChild;
                               SearchNode<K, V> temp1 = current.rightChild;
                               current = container;
                               current.leftChild = temp;
                               current.rightChild = temp1;
                               parent.setRightChild(current);
                               size--;
                               return true;
                          }
                       }
                   }
                   //case 3
                   else if (current.rightChild!=null && key.equals(current.key)){
                       parent.setRightChild(current.getRightChild());
                       size--;
                       return true;
                   }else if (current.leftChild !=null && key.equals(current.key)){
                       parent.setRightChild(current.getLeftChild());
                       size--;
                       return true;
                   }

               }
           }
       }
       return false; // we will never reach this field.
   }

    /**
     *
     * @param value
     * @return
     * deleting root node separately, not the best way
     * to do it but for sake of sample i will do it that way.
     * first condition: if root has no children then null the root;
     * second condition root has right child. if it does, then swap right child at root position and delete rightchild
     * third condition root has no rightchild, then we have to swap it with left child or with left child's
     * leftsubtree's right most leaf. same logic is used in second condition.
     * if right child and left child has not right most leaf then we have to use it as it is without swaping
     *
     */

    private boolean deleteRoot(SearchNode<K,V> value){
        SearchNode<K,V> current = value;
        if (value.leftChild == null && value.rightChild == null){
            value = null;
            root = value;
        }else if (value.rightChild !=null){
            if (value.rightChild.leftChild == null){
                current = value.rightChild;
                SearchNode<K,V> cont = current;
                cont.leftChild = value.leftChild;
                value = cont;
                root = value;
                return true;
            }else{
                SearchNode<K, V> conte = leftTraversal(value.rightChild);
                case2Node(value.rightChild);
                conte.rightChild = value.rightChild;
                conte.leftChild = value.leftChild;
                value = conte;
                root = value;
                return true;

            }
        }else if (value.leftChild !=null && value.rightChild == null){
            if (value.leftChild.rightChild == null){
                current = value.leftChild;
                SearchNode<K,V> cont = current;
                cont.rightChild = value.rightChild;
                value = cont;
                root = value;
                return true;
            }else{
                SearchNode<K, V> conte = rightTraversal(value.leftChild);
                case2LeftNode(value.leftChild);
                conte.leftChild = value.leftChild;
                conte.rightChild = value.rightChild;
                value = conte;
                root = value;
                return true;

            }
        }
        return false;
    }

    /**
     *
     * @param value
     * helper method deletes leftmost child in right subtree.
     */
    private void case2Node(SearchNode<K,V> value){
        SearchNode<K,V> previous;
        while (value.leftChild !=null){
            previous = value;
            value = value.leftChild;

            if (value.leftChild == null && value.rightChild == null){
                previous.setLeftChild(null);
            }else if (value.rightChild !=null){
                previous.setLeftChild(value.getRightChild());
            }
        }

    }

    /**
     *
     * @param value
     * helper method that deletes parent node in left sub tree.
     */
    private void case2LeftNode(SearchNode<K,V> value){
        SearchNode<K,V> previous;
        while (value.rightChild !=null){
            previous = value;
            value = value.rightChild;

            if (value.leftChild == null && value.rightChild == null){
                previous.setRightChild(null);
            }else if (value.leftChild !=null){
                previous.setRightChild(value.getLeftChild());
            }
        }
    }

    /**
     *
     * @param value
     * @return
     * this method returns node which have to replace parent node with 2 children,
     * best replacement for parent node in right subtree is leftmost node of right
     * subtree of parent that have to be deleted.
     *             70
     *            /  \
     *          50    80
     *              /   \
     *           75      90
     *                 /   \
     *               85     95
     *              /
     *             81 <<---replace's
     *     for example if we want to delete node 80, best replacement is node 81,
     *     which is located at leftmost from right tree
     */
    private SearchNode<K, V> leftTraversal(SearchNode<K, V> value){
        if (value.leftChild == null){
            return value;
        }
        return leftTraversal(value.leftChild);
    }

    /**
     *
     * @param value
     * @return
     * this method returns node which have to replace parent node with 2 children,
     * best replacement for parent node in left subtree is rightmost node of left
     * subtree of parent that have to be deleted.
     *             70
     *            /  \
     *          50    80
     *         /  \
     *        40  55
     *       /  \
     *      35   42  <<---- replace's
     *      42 can be best replacement for 50, 55 can be best replacement for 70 etc. etc
     */
    private SearchNode<K,V> rightTraversal(SearchNode<K,V> value){
        if (value.rightChild ==null){
            return value;
        }
        return rightTraversal(value.rightChild);
    }

    /**
     * Deapth First Traversal || In - Order Traversal.
     * LefChild >> Root >> RightChild
     * time complexity O(n)
     */
    public void inOrder(){
        inOrderPtintOut(root);
    }
    /**
     * Deapth First Traversal || pre  - Order Traversal.
     * root >> leftChild >> RightChild
     * Time complexity O(n)
     */
    public void preOrder(){
        preOrderPtintOut(root);
    }

    /**
     * Deapth First Traversal || post  - Order Traversal.
     * leftChild >> rightChild >> root
     * time comlexity O(n)
     */
    public void postOrder(){
        postOrderPtintOut(root);
    }
    private void inOrderPtintOut(SearchNode<K, V> value){
        if (value !=null){
            inOrderPtintOut(value.leftChild);
            System.out.println("Key " +  value.getKey() + " has Value " + value.getValue());
            inOrderPtintOut(value.rightChild);
        }

    }
    private void preOrderPtintOut(SearchNode<K,V> value){
        if (value !=null){
            System.out.println("key " + value.getKey() + " has value " + value.getValue());
            preOrderPtintOut(value.leftChild);
            preOrderPtintOut(value.rightChild);
        }
    }
    private void postOrderPtintOut(SearchNode<K, V> value){
        if (value !=null){
            postOrderPtintOut(value.leftChild);
            postOrderPtintOut(value.rightChild);
            System.out.println("key " + value.getKey() + " has value " + value.getValue());
        }
    }

    /**
     * (BFS)Breadth First Traversal ||  Level Order Traversal
     * use's Queue structure, first added element have to left is first.
     * root, left child, right child, etc etc
     */
    public void levelOrder(){
        levelOrderPrintOut(root);
    }

    private void levelOrderPrintOut(SearchNode<K,V> value){
        if (value == null){
            return;
        }
        Queue<SearchNode<K,V>> nodes = new LinkedList<>();
        nodes.add(value);
        while (!nodes.isEmpty()){
            SearchNode<K,V> element = (SearchNode<K,V>) nodes.poll();
            System.out.println("Key " +element.getKey()+ " Value " + element.getValue());
            if (element.leftChild !=null)
                nodes.add(element.leftChild);
            if (element.rightChild !=null)
                nodes.add(element.rightChild);
        }
    }

    public K getRoot(){
        return root.getKey();
    }
    public boolean isEmpty(){
        return root == null;
    }


    public int leafs(){
        return countLeafs(root);
    }

    /**
     *
     * @param value
     * @return
     * calculate leaf nodes, and return them in above method.
     */
    private int countLeafs(SearchNode<K,V> value){
        if (value == null)
            return 0;
        if (value.leftChild == null && value.rightChild == null)
            return 1;
        else
            return countLeafs(value.leftChild) + countLeafs(value.rightChild);
    }

    public void printLeafs(){
        printAllLeafs(root);
    }

    /**
     * this method simply prints all leafs to console.
     * @param value
     */
    private void printAllLeafs(SearchNode<K,V> value){
        if (value == null){
            return;
        }
        Queue<SearchNode<K,V>> queue = new LinkedList<>();
        queue.add(value);
        while (!queue.isEmpty()){
            SearchNode<K,V> leafs = (SearchNode<K,V>)queue.poll();
            if (leafs.leftChild == null && leafs.rightChild == null){
                System.out.println(leafs.getKey() + " " + leafs.getValue());
            }
            if (leafs.leftChild != null){
                queue.add(leafs.leftChild);
            }
            if (leafs.rightChild != null){
                queue.add(leafs.rightChild);
            }
        }
    }

    /**
     * method print all parent to console with help of helper method
     */
    public void printParents(){
        printAllParents(root);
    }

    /**
     * this method simply prints all parents to console
     */
    private void printAllParents(SearchNode<K,V> value){
        if (value == null){
            return;
        }
        // have no idea why i'm using stack explicitly, when
        // system use's it implicitly, but let's roll this way
        Stack<SearchNode<K,V>> stack = new Stack<>();
        stack.push(value);
        while (!stack.isEmpty()){
            SearchNode<K,V> parents =(SearchNode<K, V>) stack.pop();
            if (parents.leftChild !=null || parents.rightChild !=null){
                System.out.println(parents.getKey() + " > " + parents.getValue());
            }
            if (parents.leftChild !=null){
                stack.push(parents.leftChild);
            }
            if (parents.rightChild !=null){
                stack.push(parents.rightChild);
            }
        }
    }

    /**
     * method which return actual number of parent , with help of helper method.
     * @return
     */
    public int countParent(){

        return countAllParent(root);
    }

    /**
     * helper method which counts how many parent is in tree.
     * if element we pushed to stack has both children then increment the total.
     * we pop the element from stack at every iteration.
     *
     *
     */
    private int countAllParent(SearchNode<K,V> value){
        int total = 0;
        if (value == null) {
            return 0;
        }
        Stack<SearchNode<K,V>> stack = new Stack<>();
        stack.push(value);
        while (!stack.isEmpty()){
            SearchNode<K,V> count = (SearchNode<K, V>) stack.pop();
            if (count.leftChild !=null || count.rightChild !=null){
                total++;
            }
            if (count.leftChild !=null){
                stack.push(count.leftChild);
            }
            if (count.rightChild !=null){
                stack.push(count.rightChild);
            }
        }

        return total;
    }

    /**
     * method which prints all the siblings in the tree
     */
    public void printAllSiblings(){
         printSiblings(root);
    }

    private int number = 1;

    /**
     * helper method which prints all siblings in the tree
     * @param value
     */
    private void printSiblings(SearchNode<K,V> value){
        if (value == null){
            return;
        }
        Queue<SearchNode<K,V>> levels = new LinkedList<>();
        levels.add(value);
        while (!levels.isEmpty()){
            SearchNode<K,V> siblings = (SearchNode<K, V>) levels.poll();
            if (siblings.leftChild != null || siblings.rightChild !=null){
                if (siblings.leftChild!=null && siblings.rightChild !=null) {
                    System.out.println("Siblings " + number + " Parent Key " + siblings.getKey());
                    System.out.println("Key " + siblings.leftChild.getKey() + " Value " + siblings.leftChild.getValue());
                    System.out.println("Key " + siblings.rightChild.getKey() + " Value " + siblings.rightChild.getValue());
                    number++;
                }

            }
            if (siblings.leftChild != null){
                levels.add(siblings.leftChild);
            }
            if (siblings.rightChild !=null){
                levels.add(siblings.rightChild);
            }
        }
    }

    /**
     * prints nodes without siblings
     */
    public void printAllNodeWithoutSiblings(){
        nodeWithoutSiblings(root);
    }

    /**
     * Helper Method for printAllNodeWithoutSiblings(). it simply prints all nodes without sibling
     *
     */
    private void nodeWithoutSiblings(SearchNode<K,V> value){
        if (value == null){
            return;
        }

        System.out.println("Nodes Without Siblings ");
        System.out.println("Key " + root.getKey()  + " Value " + root.getValue());
        Queue<SearchNode<K,V>> levels = new LinkedList<>();
        levels.add(value);
        while (!levels.isEmpty()){
            SearchNode<K,V> _no_siblings = (SearchNode<K, V>) levels.poll();
            if (_no_siblings.leftChild == null || _no_siblings.rightChild == null){
                if (_no_siblings.leftChild != null){
                    System.out.println("Parent " + _no_siblings.getKey());
                    System.out.println("Key " + _no_siblings.leftChild.getKey() + " Value " + _no_siblings.leftChild.getValue());
                }
                if (_no_siblings.rightChild !=null){
                    System.out.println("Parent " + _no_siblings.getKey());
                    System.out.println("Key " + _no_siblings.rightChild.getKey() + " Value " + _no_siblings.rightChild.getValue());
                }

            }
            if (_no_siblings.leftChild != null){
                levels.add(_no_siblings.leftChild);
            }
            if (_no_siblings.rightChild !=null){
                levels.add(_no_siblings.rightChild);
            }
        }
    }
    /**
     * method that displays number of edges in the tree
     * Edge as usual is 1 less then number of elements in the tree
     * so we will return size -1;
     */
    public int edgeCount(){
        return size() -1;
    }


    /**
     * helper method to determine what is a height of subtrees.
     * if subtree has no children return 0, if it has children then find the total heigh of sub -tree
     * iteration proceeds recursivly until  value hits null. first it reads left subtree of left sub tree, then right
     * one. then assign value to variable +1, + one because we start iteraiton below the root eleemnt. from it left
     * or right child.
     * @param value
     * @return
     */
    private  int findHeightOfSubTree(SearchNode<K,V> value){
        if (value == null){
            return 0;
        }

        int s  = 1 + Math.max(findHeightOfSubTree(value.leftChild), findHeightOfSubTree(value.rightChild));

        return s;
    }

    /**
     * method which returns true if tree is height balanced, first we check root element, if its empty we return true,
     * true because empty tree is balanced tree. then we find left subtree height of root and right subtree height.
     * if there diff is more then one tree is unblanaced and we return false.
     * @return
     */
    public boolean isBalanced(){
        if (root == null){
            return true;
        }
        int hj = Math.abs(findHeightOfSubTree(root.leftChild) - findHeightOfSubTree(root.rightChild));
        if (hj > 1){
            return false;
        }
        return true;
    }

    /**
     * method checks if binary tree is full binary tree. if yes return true, else return false.
     *
     * full binary tree is tree where nodes has 2 children or no children at all.
     *                50
     *             /    \
     *           40     60   <--- Full Binary tree
     *         /   \
     *       20    45
     *
     *
     *
     */
    public boolean isFull(){
        if (root == null){
            return true;
        }
        boolean bo = false;
        Queue<SearchNode<K,V>> full = new LinkedList<>();
        full.offer(root);
        while (!full.isEmpty()){
            SearchNode<K,V> get = (SearchNode<K, V>) full.poll();
            if (get.leftChild !=null && get.rightChild !=null){
                bo = true;
            }else if ( get.leftChild == null && get.rightChild == null){
                bo = true;
            }else{
                bo = false;
                break;
            }
            if (get.leftChild!=null){
                full.offer(get.leftChild);
            }
            if (get.rightChild !=null){
                full.offer(get.rightChild);
            }
        }
        return bo;
    }

    /**
     * method checks if binary tree is perfect binary tree. return true if yes, else return false
     * perfect binary tree is tree where all nodes are at same level and has 2 children
     *
     *               50
     *             /    \
     *           40     60   <--- Perfect Binary tree
     *         /   \   /   \
     *       20    45 55    70
     * @return
     */
    public boolean isPerfect(){
        if (root == null){
            return true;
        }
        boolean bo = false;
        int height = Math.abs(findHeightOfSubTree(root.leftChild) - findHeightOfSubTree(root.rightChild));
        Queue<SearchNode<K,V>> perfect = new LinkedList<>();
        perfect.offer(root);
        while (!perfect.isEmpty()){
            SearchNode<K,V> dec = (SearchNode<K, V>) perfect.poll();
            if (height == 0 && (dec.leftChild !=null && dec.rightChild !=null) ||(dec.leftChild ==null && dec.rightChild ==null)){
                bo = true;
            }else {
                bo = false;
                break;
            }
            if (dec.leftChild !=null){
                perfect.offer(dec.leftChild);
            }
            if (dec.rightChild!=null){
                perfect.offer(dec.rightChild);
            }
        }

        return bo;
    }


    /**
     * Method finds and prints path from root node to any node in the tree,
     * if root is empty sprint Tree is empty. or throw exception or return whatever you want.
     * afr that step start iteration until you reach the correct key, all the path store in to list
     * and then print it out.
     * if searched Key is not in tree Print out that there is no such key and return.
     */

    private List finPathFromRootToAnyNode(SearchNode<K,V>value, K one){
        if (value == null){
            System.out.println("Tree is empty");
        }
        List elems = new LinkedList();
        Object obj = (value.key + " : " + value.value);
        elems.add(obj);
        while (!one.equals(value.key)){
            if (one.compareTo(value.key)< 0){
                value = value.leftChild;
                if (value == null){
                    return null;
                }
            }else{
                value = value.rightChild;
                if (value == null){
                    return null;
                }
            }
            obj = (value.key + " : " + value.value);
            elems.add(obj);
        }
        return elems;
    }

    public List rootToNodePath(K key){
        List of = finPathFromRootToAnyNode(root, key);
        return of;
    }


}
