package Tree;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Binary Heap Tree, By default Tree is Max Heap, But with an help of Comparator and Comparable
 * You have ability to sort the elements as you wish. Class got Default operations. Add, Remove and Search.
 * + 4 Constructors: Default constuctor creates heap with size of 15; constuctor number 1 creates heap with
 * the size you wish, constructor number 2 takes comparator as argument and gives you ability to sort heap as
 * you wish, constructor number 3 creates heap with the size and sorting ability you wish.
 */
public class HeapTree<E> {

    private int size;
    private E [] elements;
    private int lastElement;
    private final Comparator<? super E> comparator;
    private static final int DEFAULT_SIZE =15;


    public HeapTree(){
        this(DEFAULT_SIZE);
    }

    public HeapTree(int intitialSize){
        this(intitialSize, null);
    }

    public HeapTree(Comparator<? super E> comparator){
        this(DEFAULT_SIZE, comparator);
    }

    public HeapTree(int initialSize, Comparator<? super E> comparator){
        if (initialSize <1){
            throw new IllegalArgumentException();
        }
        elements = (E[])new Object[initialSize];
        this.comparator = comparator;
    }

    private void shrink(int newSize){
        int oldSize = elements.length;
        int shrinkSize = newSize + ((oldSize<100)? (oldSize+2) : (oldSize>>1));

        elements = Arrays.copyOf(elements,shrinkSize);
    }

    private void replace(int from, int to){
        E temporary = elements[from];
        elements[from] = elements[to];
        elements[to] = temporary;
    }

    /**
     * method swap's up new element in Array until it hits array[0], if you are using comparator if field will work , by default
     * it is using Comparable.  parent Object is (index-1)/2
     */
    private void swapUp(int lastPosition){
        if (lastPosition == 0){
            return;
        }
        int parent =(int) Math.floor((lastPosition-1)/2);
        if (comparator !=null){
            E lastObject = elements[lastPosition];
            E parentObject = elements[parent];
            if (comparator.compare(parentObject, lastObject)>=0){
                replace(lastPosition, parent);
            }
        }else{
            Comparable<? super E> last = (Comparable<?super E>)elementAt(lastPosition);
            Comparable<? super E> preLast = (Comparable<? super E>)elementAt(parent);
            if (last.compareTo((E)preLast)>=0){
                replace(lastPosition, parent);
            }
        }
        swapUp(parent);

    }

    /**
     *
     * method swaps down element after deletion, children of deleted element are:
     * left: 2 * index +1
     * right 2 * index +2
     * as an in above method you can use Comparator or Default Comparable.
     */
    private void swapDown(int root){
        int leftChild = 2 * root + 1;
        int rightChild = 2 * root + 2;
        if (comparator!=null){
            E leftObject = elementAt(leftChild);
            E rightObject = elementAt(rightChild);
            E parentObject = elementAt(root);
            if (rightChild == lastElement && comparator.compare(leftObject, rightObject)<0
                    && comparator.compare(parentObject,leftObject)>0){
                replace(root, leftChild);
                return;
            }
            if (leftChild == lastElement && comparator.compare(parentObject, leftObject)>0){
                replace(root, leftChild);
                return;
            }
            if (rightChild == lastElement && comparator.compare(parentObject, rightObject)>0){
                replace(root, rightChild);
                return;
            }
            if (rightChild == lastElement && comparator.compare(parentObject, rightObject)>0
                    && comparator.compare(parentObject, leftObject)<0){
                replace(root, leftChild);
                return;
            }


            if (leftChild >= lastElement || rightChild >= lastElement){
                return;
            }

            if (comparator.compare(leftObject, rightObject)<0 && comparator.compare(parentObject, leftObject)>0){
                replace(root, leftChild);
                swapDown(leftChild);

            }else if (comparator.compare(rightObject, leftObject)<0 && comparator.compare(parentObject, rightObject)>0){
                replace(root, rightChild);
                swapDown(rightChild);
            }
        }else {
            Comparable<? super E> left = (Comparable<? super E>)elementAt(leftChild);
            Comparable<? super E> righ = (Comparable<? super E>)elementAt(rightChild);
            Comparable<? super E> parent = (Comparable<? super E>)elementAt(root);
            if (rightChild == lastElement && left.compareTo((E)righ)>0){
                replace(root, leftChild);
                return;
            }
            if (leftChild == lastElement && parent.compareTo((E)left)<0){
                replace(root, leftChild);
                return;
            }
            if (rightChild == lastElement && parent.compareTo((E)righ)<0){
                replace(root, rightChild);
                return;
            }
            if (rightChild == lastElement && parent.compareTo((E)righ)>0 && parent.compareTo((E)left)<0){
                replace(root, leftChild);
                return;
            }

            if (leftChild >= lastElement || rightChild >= lastElement){
                return;
            }
            if (left.compareTo((E)righ)>=0 && parent.compareTo((E)left)<=0){
                replace(root, leftChild);
                swapDown(leftChild);
            }else if (righ.compareTo((E)left)>0 && parent.compareTo((E)righ)<=0){
                replace(root, rightChild);
                swapDown(rightChild);
            }
        }
    }


    /**
     * No Restrictions to add duplicate elements.
     * if null will be passed to method, it will throw illegal argument exception
     * after every addition we have to swap up the element
     * @param value
     * @return
     */
    public boolean add(E value){
        if (value ==  null){
            throw new IllegalArgumentException();
        }
        int c = size;
        if (c>=elements.length){
            shrink(c+1);
        }
        if(c == 0){
            elements[lastElement] = value;
        }else {
            elements[++lastElement] = value;
            swapUp(lastElement);
        }
        size++;

        return true;
    }

    /**
     * As usual in Heap we are deleting root element, but in fact we replace
     * last element with root and swap it down, class does not have ability to
     * remove any element from structure, but you can easily add it, it is even more
     * simple to remove any element because you have swapUp and SwapDown method;s
     * if you decide to add such method Ready:
     1. find the element that has to be removed. for example element at index 9:
     2. replace it with last element,
     3. swap index 9  up or down.
     * @return deleted element
     */
    public E remove(){
        E temporary = elementAt(0);
        replace(0, lastElement);
        elements[lastElement--] = null;
        swapDown(0);
        size--;

        return temporary;

    }

    /**
     * @return true || false
     */
    public boolean search(E value){
        return objectIndex(value) != -1;
    }

    /**
     * @return element at index 0
     */
    public E peek(){
        return size == 0 ? null : elements[0];
    }


    public int size(){
        return size;
    }



    private E elementAt(int index) {
        if (index>=elements.length){
            return null;
        }
        return elements[index];
    }
    private int objectIndex(E value){
        if (value == null){
            for (int i =0; i<size;i++){
                if (elements[i]==null){
                    return i;
                }
            }
        }else{
            for (int s =0; s<size; s++){
                if (elements[s].equals(value))
                    return s;
            }
        }
        return -1;
    }

    public String toString(){
        return Arrays.toString(Arrays.copyOf(elements, size));
    }
}
