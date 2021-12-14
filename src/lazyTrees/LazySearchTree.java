package lazyTrees;
import java.util.*;

/**
 * Implementation of a binary search tree as implemented in the modules with modifications to
 * to use lazy deletion rather than explicit hard deletion
 * Updated to use garbage collection on lazily deleted nodes
 *
 * @author Foothill College, Michael Bernal
 */
public class LazySearchTree<E extends Comparable< ? super E > >
        implements Cloneable
{
    /**
     * Class attributes.
     */
    protected int mSize;
    protected LazySTNode mRoot;
    protected int mSizeHard;

    /**
     * Remove all deleted nodes by calling private method.
     * @return Remove the deleted nodes from the tree.
     */
    public boolean collectGarbage()
    {
        int oldSize = mSize;
        mRoot = collectGarbage(mRoot);
        return (mSize != oldSize);
    }

    /**
     * Accessor method for hard size.
     * @return Hard size
     */
    public int sizeHard()
    {
        return mSizeHard;
    }

    /**
     * LazyTreeSearch object constructor.
     */
    public LazySearchTree()
    {
        clear();
    }

    /**
     * Checks if the tree is empty.
     * @return Soft size == 0
     */
    public boolean empty()
    {
        return (mSize == 0);
    }

    /**
     * Accessor method for soft size.
     * @return Soft Size.
     */
    public int size()
    {
        return mSize;
    }

    /**
     * Sets defaults for class members.
     */
    public void clear()
    {
        mSize = 0;
        mSizeHard = 0;
        mRoot = null;
    }

    /**
     * Method will return height of the tree.
     * @return Tree height
     */
    public int showHeight()
    {
        return findHeight(mRoot, -1);
    }

    /**
     * Soft method for finding smallest data in the tree,
     * @return smallest value
     */
    public E findMin()
    {
        if (mRoot == null)
            throw new NoSuchElementException();
        return findMin(mRoot).data;
    }

    /**
     * Hard method for finding smallest data in the tree.
     * @return smallest value
     */
    public E findMinHard()
    {
        if(mRoot == null)
            throw new NoSuchElementException();
        return findMinHard(mRoot).data;
    }

    /**
     * Soft method for finding largest data in the tree.
     * @return Largest data
     */
    public E findMax()
    {
        if (mRoot == null)
            throw new NoSuchElementException();
        return findMax(mRoot).data;
    }

    /**
     * Hard method for finding largest data in the tree.
     * @return Largest data
     */
    public E findMaxHard()
    {
        if(mRoot == null)
            throw new NoSuchElementException();
        return findMaxHard(mRoot).data;
    }

    /**
     * Soft method to find a value.
     * @param x data to find
     * @return data
     */
    public E find( E x )
    {
        LazySTNode resultNode;
        resultNode = find(mRoot, x);
        if (resultNode == null)
            throw new NoSuchElementException();
        return resultNode.data;
    }

    /**
     * Hard method to find a value.
     * @param x data to find
     * @return data
     */
    public E findHard(E x)
    {
        LazySTNode resultNode;
        resultNode = findHard(mRoot, x);
        if(resultNode == null)
            throw new NoSuchElementException();
        return resultNode.data;
    }

    /**
     * Soft method to check if item is in tree.
     * @param x data to find
     * @return item
     */
    public boolean contains(E x)
    { return find(mRoot, x) != null; }

    /**
     * Hard method to check if item is in tree.
     * @param x data to find
     * @return item
     */
    public boolean containsHard(E x)
    {
        return findHard(mRoot, x) != null;
    }

    /**
     * Insert item into tree.
     * @param x data to insert
     * @return True on success
     */
    public boolean insert( E x )
    {
        int oldSize = mSize;
        mRoot = insert(mRoot, x);
        return (mSize != oldSize);
    }

    /**
     * Soft method to remove item from tree.
     * @param x data to remove
     * @return True on success
     */
    public boolean remove( E x )
    {
        int oldSize = mSize;
        remove(mRoot, x);
        return (mSize != oldSize);
    }

    /**
     * Hard method to remove item from tree.
     * @param x data to remove
     * @return True on success
     */
    public boolean removeHard(E x)
    {
        int oldSize = mSize;
        mRoot = removeHard(mRoot, x);
        return  (mSize != oldSize);
    }

    /**
     * The hard traverser method.
     * @param func printObject
     * @param <F>
     */
    public < F extends Traverser<? super E > >
    void traverseHard(F func)
    {
        traverseHard(func, mRoot);
    }

    /**
     * The soft traverser method.
     * @param func printObject
     * @param <F>
     */
    public<F extends Traverser<? super E>>
    void traverseSoft(F func)
    {
        traverseSoft(func, mRoot);
    }

    /**
     * Clone method.
     * @return New cloned object
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException
    {
        LazySearchTree<E> newObject = (LazySearchTree<E>)super.clone();
        newObject.clear();  // can't point to other's data

        newObject.mRoot = cloneSubtree(mRoot);
        newObject.mSize = mSize;

        return newObject;
    }

    // private helper methods ----------------------------------------

    /**
     * Call removeHard to remove soft deleted nodes.
     * @param root LazySTNode
     * @return LazySTNode object.
     */
    protected LazySTNode collectGarbage(LazySTNode root)
    {
        if(root == null)
            return null;

        root.lftChild = collectGarbage(root.lftChild);
        root.rtChild = collectGarbage(root.rtChild);

        if(root.deleted)
            root = removeHard(root, root.data);

        return root;
    }
    /**
     * Soft method to find minimum value.
     * @param root LazySTNode
     * @return Smallest value
     */
    protected LazySTNode findMin( LazySTNode root )
    {
        if (root == null)
            return null;

        LazySTNode tmp = findMin(root.lftChild);

        if(tmp != null)
            return tmp;
        if(!root.deleted)
            return root;

        return findMin(root.rtChild);
    }

    /**
     * Soft method to find maximum value.
     * @param root LazySTNode
     * @return Maximum value
     */
    protected LazySTNode findMax( LazySTNode root )
    {
        if(root == null)
            return null;

        LazySTNode tmp = findMax(root.rtChild);

        if( tmp != null)
            return tmp;
        if(!root.deleted)
            return root;

        return findMax(root.lftChild);
    }

    /**
     * Hard method to find smallest value.
     * @param root LazySTNode
     * @return Smallest value
     */
    protected LazySTNode findMinHard(LazySTNode root)
    {
        if(root == null)
            return null;
        if(root.lftChild == null)
            return root;

        return findMinHard(root.lftChild);
    }

    /**
     * Hard method to find maximum value.
     * @param root LazySTNode
     * @return Largest value
     */
    protected LazySTNode findMaxHard(LazySTNode root)
    {
        if(root == null)
            return null;
        if(root.rtChild == null)
            return root;

        return findMaxHard(root.rtChild);
    }

    /**
     * Insert data into tree.
     * @param root LazySTNode
     * @param x
     * @return root object
     */
    protected LazySTNode insert( LazySTNode root, E x )
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
        {
            mSize++;
            mSizeHard++;
            return new LazySTNode(x, null, null);
        }

        compareResult = x.compareTo(root.data);
        if ( compareResult < 0 )
            root.lftChild = insert(root.lftChild, x);
        else if ( compareResult > 0 )
            root.rtChild = insert(root.rtChild, x);
        else if(root.deleted)
        {
            root.deleted = false;
            mSize++;
        }

        return root;
    }

    /**
     * Hard method to remove data from tree.
     * @param root LazySTNode
     * @param x
     * @return root
     */
    protected LazySTNode removeHard( LazySTNode root, E x  )
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
            return null;

        compareResult = x.compareTo(root.data);
        if ( compareResult < 0 )
            root.lftChild = removeHard(root.lftChild, x);
        else if ( compareResult > 0 )
            root.rtChild = removeHard(root.rtChild, x);

            // found the node
        else if (root.lftChild != null && root.rtChild != null)
        {
            root.data = findMin(root.rtChild).data;
            root.rtChild = removeHard(root.rtChild, root.data);
        }
        else
        {
            root =
                    (root.lftChild != null)? root.lftChild : root.rtChild;
            mSizeHard--;
        }
        return root;
    }

    /**
     * Soft method to remove data from tree.
     * @param root LazySTNode
     * @param x
     */
    private void remove(LazySTNode root, E x)
    {
        int compareResult;

        if(root == null)
            return;

        try
        {
            find(x);
        }
        catch (NoSuchElementException e)
        {
            return;
        }

        compareResult = x.compareTo(root.data);
        if(compareResult < 0)
            remove(root.lftChild, x);
        else if(compareResult > 0)
            remove(root.rtChild, x);
        else if((root.lftChild!= null && root.rtChild != null &&
                (!root.lftChild.deleted && !root.rtChild.deleted)))
        {
            root.data = findMin(root.rtChild).data;
            remove(root.rtChild, root.data);
        }
        else
        {
            root.deleted = true;
            root = (root.lftChild != null)? root.lftChild:root.rtChild;
            mSize--;
        }
    }

    /**
     * Traverse hard.
     * @param func
     * @param treeNode
     * @param <F>
     */
    protected <F extends Traverser<? super E>>
    void traverseHard(F func, LazySTNode treeNode)
    {
        if (treeNode == null)
            return;

        traverseHard(func, treeNode.lftChild);
        func.visit(treeNode.data);
        traverseHard(func, treeNode.rtChild);
    }

    /**
     * Traverse soft.
     * @param func
     * @param treeNode
     * @param <F>
     */
    protected <F extends Traverser<? super E>>
    void traverseSoft(F func, LazySTNode treeNode)
    {
        if (treeNode == null)
            return;

        traverseSoft(func, treeNode.lftChild);

        if(!treeNode.deleted)
            func.visit(treeNode.data);

        traverseSoft(func, treeNode.rtChild);
    }

    /**
     * Soft method to find data.
     * @param root LazySTNode
     * @param x
     * @return data
     */
    protected LazySTNode find( LazySTNode root, E x )
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
            return null;

        compareResult = x.compareTo(root.data);
        if (compareResult < 0)
            return find(root.lftChild, x);
        if (compareResult > 0)
            return find(root.rtChild, x);
        if(!root.deleted)
            return root;   // found

        return null;
    }

    /**
     * Hard method to find data in tree.
     * @param root LazySTNode
     * @param x
     * @return data
     */
    protected LazySTNode findHard(LazySTNode root, E x)
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
            return null;

        compareResult = x.compareTo(root.data);

        if (compareResult < 0)
            return find(root.lftChild, x);
        if (compareResult > 0)
            return find(root.rtChild, x);

        return root;   // found
    }

    /**
     * Clone  node method.
     * @param root LazySTNode
     * @return Cloned node
     */
    protected LazySTNode cloneSubtree(LazySTNode root)
    {
        LazySTNode newNode;
        if (root == null)
            return null;

        // does not set myRoot which must be done by caller
        newNode = new LazySTNode
                (
                        root.data,
                        cloneSubtree(root.lftChild),
                        cloneSubtree(root.rtChild)
                );
        return newNode;
    }

    /**
     * Find the height of the tree.
     * @param treeNode
     * @param height
     * @return height
     */
    protected int findHeight( LazySTNode treeNode, int height )
    {
        int leftHeight, rightHeight;
        if (treeNode == null)
            return height;
        height++;
        leftHeight = findHeight(treeNode.lftChild, height);
        rightHeight = findHeight(treeNode.rtChild, height);
        return (leftHeight > rightHeight)? leftHeight : rightHeight;
    }

    /**
     * LazySTNode inner class.
     */
    public class LazySTNode
    {
        // use public access so the tree or other classes can access members
        public LazySTNode lftChild, rtChild;
        public E data;
        public LazySTNode myRoot;  // needed to test for certain error
        public boolean deleted;

        public LazySTNode( E d, LazySTNode lft, LazySTNode rt )
        {
            lftChild = lft;
            rtChild = rt;
            data = d;
            deleted = false;
        }

        public LazySTNode()
        {
            this(null, null, null);
        }
    }
}

/**
 * Traverser interface.
 * @param <E>
 */
interface Traverser<E>
{
    public void visit(E x);
}

/**
 * PrintObject class.
 * @param <E>
 */
class PrintObject<E> implements Traverser<E>
{
    public void visit(E x)
    {
        System.out.println(x + " ");
    }
};