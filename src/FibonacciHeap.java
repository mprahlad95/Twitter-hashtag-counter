
public class FibonacciHeap {
	
	private Node maxNode;
    private int numberOfNodes;
	
    // Insert a new node in the heap
	public void insert(Node x) {

		// Check if max node is not null
		if (maxNode != null) {

			// Add to the right of max node
			x.left = maxNode;
			x.right = maxNode.right;
			maxNode.right = x;

			// Check if node right is not null
			if (x.right != null) {
				x.right.left = x;
			}
			if (x.right == null) {
				x.right = maxNode;
				maxNode.left = x;
			}
			if (x.key > maxNode.key) {
				maxNode = x;
			}
		} else 
			maxNode = x;
		
		numberOfNodes++;
	}

	// Perform a cut; x from y
	public void cut(Node x, Node y) {

	}

	// Perform cascading cut on the given node
	public void cascadingCut(Node x) {

	}

	// Increase value of key for the input node
	public void increaseKey(Node x, int k) {

	}

	// Extract max from the heap
	public Node extractMax() {
		return null;
	}

	// Perform degree-wise merge
	public void pairWiseCombine() {

	}

	// Make y the child of node x
	public void makeChild(Node y, Node x) {

	}

}
