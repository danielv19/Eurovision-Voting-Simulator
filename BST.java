// Binary Search Tree implementation
public class BST {
  private BSTNode root; // Root of the BST
  private int nodecount; // Number of nodes in the BST

  // constructor
  BST() { root = null; nodecount = 0; }
  
  // Reinitialize tree
  public void clear() { root = null; nodecount = 0; }

  // Insert a record into the tree.
  // Records can be anything, but they must be Country
  // e: The record to insert.
  public void insert(Country e) {
    root = inserthelp(root, e);
    nodecount++;
  }

  // Return the record with key value k, null if none exists
  // key: The key value to find  
  public BSTNode find(Country key) {return findhelp(root, key); }

  public BSTNode root() { return root; }
  
  private BSTNode deletemax(BSTNode rt) {
    if (rt.right() == null) return rt.left();
    rt.setRight(deletemax(rt.right()));
    return rt;
  }
  
  // Return the number of records in the dictionary
  public int size() { return nodecount; }
  
  // Get the maximum valued element in a subtree
  private BSTNode getmax(BSTNode rt) {
    if (rt.right() == null) return rt;
    return getmax(rt.right());
  }
  
  private BSTNode inserthelp(BSTNode rt, Country e) {
      if (rt == null) return new BSTNode(e);
      if (rt.value().getName().compareTo(e.getName()) >= 0)
          rt.setLeft(inserthelp(rt.left(), e));
      else
          rt.setRight(inserthelp(rt.right(), e));
      return rt;
  }
  
  private BSTNode findhelp(BSTNode rt, Country key) {
      if (rt == null) return null;
      if (rt.value().getName().compareTo(key.getName()) > 0)
        return findhelp(rt.left(), key);
      else if (rt.value().getName().compareTo(key.getName()) == 0)
      {
        return rt;
      }
      else return findhelp(rt.right(), key);
  }
  // Remove a record from the tree
  // key: The key value of record to remove
  // Returns the record removed, null if there is none.
  public BSTNode remove(Country key) {
    BSTNode temp = findhelp(root, key); // First find it
    if (temp != null) {
      root = removehelp(root, key); // Now remove it
      nodecount--;
    }
    return temp;
  }

  public void printhelp(BSTNode rt) {
      if (rt == null) return;
      printhelp(rt.left());
      printVisit(rt.value(), rt.value().getAcronym());
      printhelp(rt.right());
    }
  
  //Used for testing

  void printVisit(Country e, String a) { System.out.println(e + "|" + a); }
 
  private BSTNode removehelp(BSTNode rt, Country key)
  {
      if (rt == null) return null; 
      if (rt.value().getName().compareTo(key.getName()) > 0) 
          rt.setLeft(removehelp(rt.left(), key));
      else if (rt.value().getName().compareTo(key.getName()) < 0)
          rt.setRight(removehelp(rt.right(), key));
      else { // Found it
          if (rt.left() == null) return rt.right();
          else if (rt.right() == null) return rt.left();
          else { // Two children
              BSTNode temp = getmax(rt.left());
              rt.setValue(temp.value());
              rt.setLeft(deletemax(rt.left()));
          }
      }
      return rt;
  }
}