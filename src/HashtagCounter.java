import java.io.*;
import java.util.*;
import java.util.regex.*;

//TODO

//Check for Upper-case input
//First char $

public class HashtagCounter {
	public void main(String[] args) {
		long startTime = System.currentTimeMillis(); // Timer **TEST**
		String pathtofile = args[0];
		HashMap<String, Node> hm = new HashMap<>(); // Hash Map for Storing the hashTag and the node
		FibonacciHeap fh = new FibonacciHeap();
		File file = new File("output_file.txt"); // Output File & writer pointer
		BufferedWriter writer = null;
		try { // try IOException and other unchecked exceptions
			BufferedReader br = new BufferedReader(new FileReader(pathtofile));
			String s = br.readLine();
			Pattern p = Pattern.compile("([#])([a-z_]+)(\\s)(\\d+)");
			Pattern p1 = Pattern.compile("(\\d+)");
			writer = new BufferedWriter(new FileWriter(file));
			while (s != null) {
				Matcher m = p.matcher(s);
				Matcher m1 = p1.matcher(s);
				if (m.find()) {
					String hashTag = m.group(2);
					int key = Integer.parseInt(m.group(4));
					if (!hm.containsKey(hashTag)) // Check if it contains the key
					{
						Node node = new Node(hashTag, key); // Create new node and insert in heap and hash map
						fh.insert(node);
						hm.put(hashTag, node);
					} else {
						int increaseKey = hm.get(hashTag).key + key; // if already in hashmap, increase key
						fh.increaseKey(hm.get(hashTag), increaseKey);
					}
				} else if (m1.find()) {
					int removeNumber = Integer.parseInt(m1.group(1)); // Number of Nodes to be removed
					ArrayList<Node> removedNodes = new ArrayList<Node>(removeNumber); // Removed Nodes
					for (int i = 0; i < removeNumber; i++) {
						Node node = fh.extractMax(); // Extract max Node
						hm.remove(node.getHashTag()); // Remove from hashmap
						Node newNode = new Node(node.getHashTag(), node.key); // Create new node for insertion
						removedNodes.add(newNode); // Add the new node for insertion into removed nodes list
						if (i < removeNumber - 1) // Add the , until the last hashTag
							writer.write(node.getHashTag() + ",");
						else
							writer.write(node.getHashTag());
					}
					for (Node iterate : removedNodes) { // Insertion step
						fh.insert(iterate);
						hm.put(iterate.getHashTag(), iterate);
					}
					writer.newLine(); // Move to new line in writer pointer
				}
				s = br.readLine(); // Move to Next Line
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ioe2) {
				}
			}
		}
		long endTime = System.currentTimeMillis(); // Print the time required **TEST**
		long totalTime = endTime - startTime;
		System.out.println(" Time in ms: " + totalTime);
	}
}
