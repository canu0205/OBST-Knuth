
// (Nearly) Optimal Binary Search Tree
// Bongki Moon (bkmoon@snu.ac.kr)

public class BST { // Binary Search Tree implementation

  protected boolean NOBSTified = false;
  protected boolean OBSTified = false;
  protected Node root;

  private int[][] cost;
  private int[][] rootIndex;
  private String[] keyArr;
  private int[] sum;

  public BST() {
    this.root = null;
  }

  public int size() {
    return sizeRec(root);
  }

  private int sizeRec(Node node) {
    if (node == null)
      return 0;
    else {
      return (1 + sizeRec(node.left) + sizeRec(node.right));
    }
  }

  public void insert(String key) {
    root = insertRec(root, key);
  }

  private Node insertRec(Node node, String key) {
    if (node == null) {
      node = new Node(key);
      return node;
    }

    if (key.compareTo(node.key) < 0)
      node.left = insertRec(node.left, key);
    else if (key.compareTo(node.key) > 0)
      node.right = insertRec(node.right, key);
    else {
      node.frequency2++;
      node.frequency++;
    }

    return node;
  }

  public boolean find(String key) {
    return findRec(root, key);
  }

  private boolean findRec(Node node, String key) {
    if (node == null)
      return false;

    node.accessCount++;

    if (key.compareTo(node.key) < 0) {
      return findRec(node.left, key);
    } else if (key.compareTo(node.key) > 0) {
      return findRec(node.right, key);
    } else {
      return true;
    }
  }

  public int sumFreq() {
    return sumFreqRec(root);
  }

  private int sumFreqRec(Node node) {
    if (node == null)
      return 0;
    else {
      return (node.frequency + sumFreqRec(node.left) + sumFreqRec(node.right));
    }
  }

  public int sumProbes() {
    return sumProbesRec(root);
  }

  private int sumProbesRec(Node node) {
    if (node == null)
      return 0;
    else {
      return (node.accessCount + sumProbesRec(node.left) + sumProbesRec(node.right));
    }
  }

  public int sumWeightedPath() {
    return sumWeightedPathRec(root, 1);
  }

  private int sumWeightedPathRec(Node node, int depth) {
    if (node == null)
      return 0;
    else {
      return (depth * node.frequency + sumWeightedPathRec(node.left, depth + 1)
          + sumWeightedPathRec(node.right, depth + 1));
    }
  }

  public void resetCounters() {
    resetCountersRec(root);
  }

  private void resetCountersRec(Node node) {
    if (node == null)
      return;
    else {
      node.accessCount = 0;
      node.frequency = 0;
      resetCountersRec(node.left);
      resetCountersRec(node.right);
    }
  }

  public void nobst() {
    NOBSTified = true;
    nobstify();
  } // Set NOBSTified to true.

  public void obst() {
    OBSTified = true;
    obstify();
  } // Set OBSTified to true.

  public void print() {
    printRec(root);
  }

  private void printRec(Node node) {
    if (node == null)
      return;

    printRec(node.left);
    System.out.println("[" + node.key + ":" + node.frequency + ":" + node.accessCount + "]");
    printRec(node.right);
  }

  // Build a nearly optimal binary search tree from the given keys.
  public void nobstify() {
    // 1. inorder traverse bst and store the result in an array
    // 2. build a new bst with the inorder traversal result
    int n = size();
    String[] keyArr = new String[n];
    int[] freqArr = new int[n];
    int[] index = { 0 };

    inorderTraverse(root, keyArr, freqArr, index);

    this.root = constructNOBST(keyArr, freqArr, 0, n - 1);
  }

  private void inorderTraverse(Node node, String[] keyArr, int[] freqArr, int[] index) {
    if (node == null)
      return;

    inorderTraverse(node.left, keyArr, freqArr, index);
    keyArr[index[0]] = node.key;
    freqArr[index[0]] = node.frequency;
    index[0]++;
    inorderTraverse(node.right, keyArr, freqArr, index);
  }

  private Node constructNOBST(String[] keyArr, int[] freqArr, int start, int end) {
    if (start > end)
      return null;

    int mid = findWeightedMid(keyArr, freqArr, start, end);
    Node node = new Node(keyArr[mid]);
    node.frequency = freqArr[mid];

    node.left = constructNOBST(keyArr, freqArr, start, mid - 1);
    node.right = constructNOBST(keyArr, freqArr, mid + 1, end);

    return node;
  }

  private int findWeightedMid(String[] keyArr, int[] freqArr, int start, int end) {
    int totalSum = 0;
    for (int i = start; i <= end; i++) {
      totalSum += freqArr[i];
    }

    int accumulatedSum = 0;
    for (int i = start; i <= end; i++) {
      accumulatedSum += freqArr[i];
      if (accumulatedSum > totalSum / 2) {
        return i;
      }
    }

    return end;
  }

  // Build a optimal binary search tree from the given keys.
  public void obstify() {
    int n = size();
    cost = new int[n + 2][n + 1];
    rootIndex = new int[n + 2][n + 1];
    keyArr = new String[n];
    sum = new int[n + 1];

    // Convert BST to array and compute prefix sum
    toArray(this.root, keyArr, 0);

    // Initialize sum, cost, and rootIndex
    sum[0] = 0;
    for (int i = 1; i <= n; i++) {
      sum[i] = sum[i - 1] + findNode(keyArr[i - 1]).frequency;
      cost[i][i] = findNode(keyArr[i - 1]).frequency;
      rootIndex[i][i] = i;
    }

    // Compute cost and rootIndex using dynamic programming
    for (int low = n - 1; low >= 1; low--) {
      for (int high = low + 1; high <= n; high++) {
        if (low > high) {
          cost[low][high] = 0;
          rootIndex[low][high] = 0;
        } else {
          cost[low][high] = Integer.MAX_VALUE;

          for (int r = rootIndex[low][high - 1]; r <= rootIndex[low + 1][high]; r++) {
            int c = (r > low ? cost[low][r - 1] : 0) + (r < high ? cost[r + 1][high] : 0) + sum[high] - sum[low - 1];
            if (c < cost[low][high]) {
              cost[low][high] = c;
              rootIndex[low][high] = r;
            }
          }
        }
      }
    }

    // Construct a tree from rootIndex
    this.root = constructOBST(1, n);
  }

  private int toArray(Node node, String[] keyArr, int idx) {
    if (node == null)
      return idx;

    idx = toArray(node.left, keyArr, idx);
    keyArr[idx++] = node.key;
    return toArray(node.right, keyArr, idx);
  }

  private Node findNode(String key) {
    return findNodeRec(root, key);
  }

  private Node findNodeRec(Node node, String key) {
    if (node == null)
      return null;

    if (key.compareTo(node.key) < 0)
      return findNodeRec(node.left, key);
    else if (key.compareTo(node.key) > 0)
      return findNodeRec(node.right, key);
    else
      return node;
  }

  private Node constructOBST(int i, int j) {
    if (i > j)
      return null;

    Node node = new Node(keyArr[rootIndex[i][j] - 1]);
    node.frequency = findNode(keyArr[rootIndex[i][j] - 1]).frequency;
    node.left = constructOBST(i, rootIndex[i][j] - 1);
    node.right = constructOBST(rootIndex[i][j] + 1, j);

    return node;
  }

  protected class Node {
    String key;
    Node left, right;
    int frequency;
    int frequency2;
    int accessCount;
    int height;

    public Node(String item) {
      this.key = item;
      left = right = null;
      frequency = 1;
      frequency2 = 1;
      accessCount = 0;
      height = 1;
    }
  }
}
