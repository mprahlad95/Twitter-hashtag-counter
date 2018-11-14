import java.util.*;

public class FibonacciHeap {

	private Node maxNode;
	@SuppressWarnings("unused")
	private int numberOfNodes;

	// Insert new node in the heap
	public void insert(Node node) {

		// Check if max node is not null
		if (maxNode != null) {

			// Add to the right of max node
			node.left = maxNode;
			node.right = maxNode.right;
			maxNode.right = node;

			// Check if node.right is not null
			if (node.right != null)
				node.right.left = node;

			if (node.right == null) {
				node.right = maxNode;
				maxNode.left = node;
			}
			if (node.key > maxNode.key)
				maxNode = node;
		} else
			maxNode = node;

		numberOfNodes++;
	}

	// Cut x from y
	public void cut(Node x, Node y) {
		// Remove x from y.child and decrease degree of y
		x.left.right = x.right;
		x.right.left = x.left;
		y.degree--;

		// Reset y.child if necessary
		if (y.child == x)
			y.child = x.right;

		if (y.degree == 0)
			y.child = null;

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

	// Recursively Perform cascading cut on the node
	public void cascadingCut(Node y) {
		Node x = y.parent;

		// If there is a parent
		if (x != null) {
			// If y is unmarked, set it marked
			if (!y.mark) {
				y.mark = true;
			} else {
				// If it's marked, recursively cut it from all predecessors
				cut(y, x);
				cascadingCut(x);
			}
		}
	}

	// Increase value of key for the given node in heap
	public void increaseKey(Node x, int k) {
		if (k < x.key) {
			// Error - should not happen if input is according to the assumptions
		}

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

	// Remove maximum from heap
	public Node extractMax() {
		Node z = maxNode;
		if (z != null) {
			int numberofChildren = z.degree;
			Node x = z.child;
			Node tempRight;

			// While children of maxNode isn't empty
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

				// Decrease number of children of max
				numberofChildren--;

			}

			// Remove z from root list of heap
			z.left.right = z.right;
			z.right.left = z.left;

			if (z == z.right) {
				maxNode = null;

			} else {
				maxNode = z.right;
				pairwiseCombine();
			}
			numberOfNodes--;
			return z;
		}
		return null;
	}

	// Performs pair-wise merge
	public void pairwiseCombine() {
		// Arbitrarily chosen
		int sizeofDegreeTable = 45;

		List<Node> degreeTable = new ArrayList<>(sizeofDegreeTable);

		// Initialize degree table
		for (int i = 0; i < sizeofDegreeTable; i++) {
			degreeTable.add(null);
		}

		// Find number of root nodes
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

			// If degree is present in degree table, add, else combine and merge
			while (true) {
				Node y = degreeTable.get(d);
				if (y == null)
					break;

				// Check for greater key
				if (x.key < y.key) {
					Node temp = y;
					y = x;
					x = temp;
				}

				// Make y the child of x if x.key is greater
				makeChild(y, x);

				// Set degree to null after x and y are merged
				degreeTable.set(d, null);
				d++;
			}

			// Store new degree in degree table
			degreeTable.set(d, x);

			// Move forward through list.
			x = next;
			numRoots--;
		}

		// Deleting max node
		maxNode = null;

		// Combine entries of degree table
		for (int i = 0; i < sizeofDegreeTable; i++) {
			Node y = degreeTable.get(i);
			if (y == null)
				continue;

			// If max node is not null
			if (maxNode != null) {

				// Remove node from root list
				y.left.right = y.right;
				y.right.left = y.left;

				// Add to root list
				y.left = maxNode;
				y.right = maxNode.right;
				maxNode.right = y;
				y.right.left = y;

				// Check for new maximum
				if (y.key > maxNode.key)
					maxNode = y;
			} else
				maxNode = y;
		}
	}

	// Make y child of x
	public void makeChild(Node y, Node x) {
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

		// Make mark of y as false
		y.mark = false;
	}

}