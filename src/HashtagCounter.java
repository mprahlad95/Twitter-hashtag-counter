import java.io.*;
import java.util.*;
import java.util.regex.*;

//TODO

//Check for Upper-case input
//First char $
//Negative count for input

public class HashtagCounter {
	public void main(String[] args) {

		// Timer **TEST**
		long startTime = System.currentTimeMillis();

		String pathtofile = args[0];

		// Hashmap to store hashtag and node
		HashMap<String, Node> hm = new HashMap<>();

		// Fibonacci heap instance
		FibonacciHeap fh = new FibonacciHeap();

		// Output File & writer pointer
		File file = new File("output_file.txt");
		BufferedWriter writer = null;

		// try IOException and other unchecked exceptions
		try {
			BufferedReader br = new BufferedReader(new FileReader(pathtofile));
			String s = br.readLine();

			// Input validation
			Pattern p = Pattern.compile("([$])([[a-z_]+)(\\s)(\\d+)");
			Pattern p1 = Pattern.compile("(\\d+)");

			writer = new BufferedWriter(new FileWriter(file));

			while (s != null) {
				Matcher m = p.matcher(s);
				Matcher m1 = p1.matcher(s);

				// Insert, increase-key operations
				if (m.find()) {
					String hashTag = m.group(2);
					int key = Integer.parseInt(m.group(4));

					// If it doesn't contain key, create new node and insert into heap and hashmap
					if (!hm.containsKey(hashTag)) {
						Node node = new Node(hashTag, key);
						fh.insert(node);
						hm.put(hashTag, node);
					}

					// If already in hashmap, increase key
					else {
						int increaseKey = hm.get(hashTag).key + key;
						fh.increaseKey(hm.get(hashTag), increaseKey);
					}
				}

				// Check for top n hashtags
				else if (m1.find()) {

					// Nodes to be removed
					int removeNumber = Integer.parseInt(m1.group(1));
					ArrayList<Node> removedNodes = new ArrayList<Node>(removeNumber);

					// Extract max node and remove from hashmap according to input
					for (int i = 0; i < removeNumber; i++) {
						Node node = fh.extractMax();
						hm.remove(node.getHashTag());

						// Create new node for insertion
						Node newNode = new Node(node.getHashTag(), node.key);

						// Add the new node for insertion into removed nodes list and "," until the last
						// hashtag
						removedNodes.add(newNode);
						if (i < removeNumber - 1)
							writer.write(node.getHashTag() + ",");
						else
							writer.write(node.getHashTag());
					}

					// Insertion
					for (Node iterate : removedNodes) {
						fh.insert(iterate);
						hm.put(iterate.getHashTag(), iterate);
					}

					// Move to new line in the writer pointer
					writer.newLine();
				}
				s = br.readLine();
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

		// Print the time required **TEST**

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(" Time in ms: " + totalTime);
	}
}
