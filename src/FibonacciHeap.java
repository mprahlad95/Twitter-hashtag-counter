import java.util.*;

public class FibonacciHeap {

	private Node maxNode;
	@SuppressWarnings("unused")
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
		// Remove x from child of y and decrease the degree of y
		x.left.right = x.right;
		x.right.left = x.left;
		y.degree--;

		// Reset y.child if needed
		if (y.child == x) {
			y.child = x.right;
		}

		if (y.degree == 0) {
			y.child = null;
		}

		// Add x to root list of heap
		x.left = maxNode;
		x.right = maxNode.right;
		maxNode.right = x;
		x.right.left = x;

		// Set parent of x to nil
		x.parent = null;

		// Set mark to false
		x.mark = false;
	}

	// Perform cascading cut on the given node
	public void cascadingCut(Node y) {
		Node x = y.parent;

		// If there is a parent
		if (x != null) {
			// If y is unmarked, set it marked
			if (!y.mark)
				y.mark = true;
			else {
				// If it's marked, cut it from its parent and recursively cut all predecessors
				cut(y, x);
				cascadingCut(x);
			}
		}
	}

	// Increase value of key for the input node
	public void increaseKey(Node x, int k) {
		if (k < x.key)
			x.key = k;

		Node y = x.parent;

		if ((y != null) && (x.key > y.key)) {
			cut(x, y);
			cascadingCut(y);
		}

		if (x.key > maxNode.key) {
			maxNode = x;
		}
	}

	// Extract max from the heap
	public Node extractMax() {
		Node y = maxNode;
		if (y != null) {
			int numberofChildren = y.degree;
			Node x = y.child;
			Node tempRight;

			// While children are present
			while (numberofChildren > 0) {
				tempRight = x.right;

				// Remove x from child list
				x.left.right = x.right;
				x.right.left = x.left;

				// Add x to root list of heap
				x.left = maxNode;
				x.right = maxNode.right;
				maxNode.right = x;
				x.right.left = x;

				// Set parent to null
				x.parent = null;
				x = tempRight;
				// Decrease number of children of maxNode
				numberofChildren--;

			}

			// Remove y from root list of heap
			y.left.right = y.right;
			y.right.left = y.left;

			if (y == y.right)
				maxNode = null;
			else {
				maxNode = y.right;
				pairWiseCombine();
			}
			numberOfNodes--;
			return y;
		}
		return null;
	}

	// Perform degree-wise merge
	public void pairWiseCombine() {
		// Arbitrarily chosen
		int sizeofDegreeTable = 45;

		List<Node> degreeTable = new ArrayList<>(sizeofDegreeTable);

		// Initialize degree table
		for (int i = 0; i < sizeofDegreeTable; i++) {
			degreeTable.add(null);
		}

		// Find the number of root nodes.
		int numRoots = 0;
		Node x = maxNode;

		if (x != null) {
			numRoots++;
			x = x.right;

			while (x != maxNode) {
				numRoots++;
				x = x.right;
			}
		}

		// For each node in root list
		while (numRoots > 0) {

			int d = x.degree;
			Node next = x.right;

			// Check if degree is in degree table - if not, add. Else combine and merge
			while (true) {
				Node y = degreeTable.get(d);
				if (y == null) {
					break;
				}

				// Check for greater key value
				if (x.key < y.key) {
					Node temp = y;
					y = x;
					x = temp;
				}

				// Make y the child of x if x key value is greater
				makeChild(y, x);

				// Set the degree to null as x and y are combined now
				degreeTable.set(d, null);
				d++;
			}

			// Store the new combined degree in the degree table
			degreeTable.set(d, x);

			// Move forward through list.
			x = next;
			numRoots--;
		}

		// Deleting the max node
		maxNode = null;

		// Combine entries of the degree table
		for (int i = 0; i < sizeofDegreeTable; i++) {
			Node y = degreeTable.get(i);
			if (y == null) {
				continue;
			}

			if (maxNode != null) {

				// Remove node from root list
				y.left.right = y.right;
				y.right.left = y.left;

				// Add to root list, again
				y.left = maxNode;
				y.right = maxNode.right;
				maxNode.right = y;
				y.right.left = y;

				// Check if it's a new maximum
				if (y.key > maxNode.key) {
					maxNode = y;
				}
			} else
				maxNode = y;
		}
	}

	// Make y the child of node x
	public void makeChild(Node y, Node x) {
		{
			// Remove y from root list of heap
			y.left.right = y.right;
			y.right.left = y.left;

			// Make y child of x
			y.parent = x;

			if (x.child == null) {
				x.child = y;
				y.right = y;
				y.left = y;
			} else {
				y.left = x.child;
				y.right = x.child.right;
				x.child.right = y;
				y.right.left = y;
			}

			// Increase degree of x by 1
			x.degree++;

			// Mark y as false
			y.mark = false;
		}
	}

}
