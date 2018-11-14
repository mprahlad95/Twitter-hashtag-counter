
public class Node {
	Node left, right, child, parent;
	int degree = 0;
	boolean mark = false;
	private String hash;
	int key;

	// Properties of Node
	Node(String hash, int key) {
		this.left = this;
		this.right = this;
		this.parent = null;
		this.degree = 0;
		this.hash = hash;
		this.key = key;

	}

	public int getKey() {
		return this.key;
	}

	public String getHashTag() {
		return this.hash;
	}
}
