import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class EDBinaryNode<T> {
	private T data = null;
	private EDBinaryNode<T> left = null;
	private EDBinaryNode<T> right = null;
	
	// constructors
	public EDBinaryNode( T item ) {
		setData(item);
	}

	public EDBinaryNode( T item, EDBinaryNode<T> l, EDBinaryNode<T> r ) {
		setData(item);
		setLeft(l);
		setRight(r);
	}

    // Properties
    public boolean containsNull() {
        return data == null;
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }

    public boolean hasBoth() {
        return hasLeft() && hasRight();
    }

    public boolean isLeaf() {
        return !hasLeft() && !hasRight();
    }

	// Getters
	public T data() {
		return data;
	}

	public EDBinaryNode<T> right() {
		return right;
	}

	public EDBinaryNode<T> left() {
		return left;
	}

    // Setters and modifiers
	public T setData(T data) {
		T old = this.data;
		this.data = data;
		return old;
	}

    /**
     * Sets a node as the left subtree. Sets the parent reference.
     *
     * @param left The new left binary node
     * @return The old left binary node
     */
	public EDBinaryNode<T> setLeft(EDBinaryNode<T> left) {
	    if (this.left == left)
	        return this.left;

		EDBinaryNode old = this.left;
	    this.left = left;

		return  old;
	}

    /**
     * Sets a node as the rigth subtree. Sets the parent reference.
     *
     * @param right The new right binary node
     * @return The old right binary node
     */
	public EDBinaryNode<T> setRight(EDBinaryNode<T> right) {
        if (this.right == right)
            return this.left;

	    EDBinaryNode<T> old = this.right;
		this.right = right;

		return old;
	}


    // Equals and hashCode, BOTH ARE RECURSIVE
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        EDBinaryNode<?> that = (EDBinaryNode<?>) o;

        if (data != null ? !data.equals(that.data) : that.data != null)
            return false;

        if (left != null && !left.equals(that.left))
            return false;

        if (left == null && that.left != null)
            return false;

        if (right != null && !right.equals(that.right))
            return false;

        if (right == null && that.right != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = data != null ? data.hashCode() : 0;

        if (left != null)
            hash = (hash << 3) & left.hashCode();

        if (right != null)
            hash = (hash << 7) & right.hashCode();

        return hash;
	}

	// to string
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("EDBinaryNode{");
        sb.append("data=").append(data).append(", has: ");
        if (hasLeft())
            sb.append(" left");
        if (hasRight())
            sb.append(" right");
        sb.append("}");

        return sb.toString();
    }
}
