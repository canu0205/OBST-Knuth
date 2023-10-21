// AVL Binary Search Tree
// Bongki Moon (bkmoon@snu.ac.kr)

public class AVL extends BST {
  public AVL() {
    super();
  }

  @Override
  public void insert(String key) {
    this.root = insertRec(root, key);
  }

  private Node insertRec(Node node, String key) {
    if (node == null)
      return new Node(key);

    int cmp = key.compareTo(node.key);
    if (cmp < 0) {
      node.left = insertRec(node.left, key);
      int leftHeight = getHeight(node.left);
      int rightHeight = getHeight(node.right);
      if (leftHeight - rightHeight == 2) {
        if (key.compareTo(node.left.key) < 0) {
          node = rotateRight(node);
        } else {
          node = rotateLeftRight(node);
        }
      }
    } else if (cmp > 0) {
      node.right = insertRec(node.right, key);
      int leftHeight = getHeight(node.left);
      int rightHeight = getHeight(node.right);
      if (rightHeight - leftHeight == 2) {
        if (key.compareTo(node.right.key) > 0) {
          node = rotateLeft(node);
        } else {
          node = rotateRightLeft(node);
        }
      }
    } else {
      node.frequency++;
      return node;
    }

    int leftHeight = getHeight(node.left);
    int rightHeight = getHeight(node.right);
    node.height = Math.max(leftHeight, rightHeight) + 1;
    return node;
  }

  private int getHeight(Node node) {
    return node == null ? 0 : node.height;
  }

  private Node rotateRight(Node node) {
    Node left = node.left;
    node.left = left.right;
    left.right = node;

    int nodeLeftHeight = getHeight(node.left);
    int nodeRightHeight = getHeight(node.right);
    int leftLeftHeight = getHeight(left.left);
    int leftRightHeight = getHeight(node);
    node.height = Math.max(nodeLeftHeight, nodeRightHeight) + 1;
    left.height = Math.max(leftLeftHeight, leftRightHeight) + 1;

    return left;
  }

  private Node rotateLeft(Node node) {
    Node right = node.right;
    node.right = right.left;
    right.left = node;

    int nodeLeftHeight = getHeight(node.left);
    int nodeRightHeight = getHeight(node.right);
    int rightLeftHeight = getHeight(node);
    int rightRightHeight = getHeight(right.right);
    node.height = Math.max(nodeLeftHeight, nodeRightHeight) + 1;
    right.height = Math.max(rightLeftHeight, rightRightHeight) + 1;

    return right;
  }

  private Node rotateLeftRight(Node node) {
    node.left = rotateLeft(node.left);
    return rotateRight(node);
  }

  private Node rotateRightLeft(Node node) {
    node.right = rotateRight(node.right);
    return rotateLeft(node);
  }
}