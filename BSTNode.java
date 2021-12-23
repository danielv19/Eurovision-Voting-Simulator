// Binary tree node implementation: supports comparable objects
public class BSTNode{
  private Country element; // Element for this node
  private BSTNode left;          // Pointer to left child
  private BSTNode right;         // Pointer to right child

  // Constructors
  BSTNode() {left = right = null; }
  BSTNode(Country val) { left = right = null; element = val;}
  BSTNode(Country val, BSTNode l, BSTNode r)
    { left = l; right = r; element = val;}

  // Get and set the element value
  public Country value() { return element; }
  public void setValue(Country v) { element = v; }
  public void setValue(Object v) { // We need this one to satisfy BinNode interface
    if (!(v instanceof Country))
      throw new ClassCastException("A Country object is required.");
    element = (Country)v;
  }

  // Get and set the left child
  public BSTNode left() { return left; }
  public void setLeft(BSTNode p) { left = p; }

  // Get and set the right child
  public BSTNode right() { return right; }
  public void setRight(BSTNode p) { right = p; }

  // return TRUE if a leaf node, FALSE otherwise
  public boolean isLeaf() { return (left == null) && (right == null); }
}