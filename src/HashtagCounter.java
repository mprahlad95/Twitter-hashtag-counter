import java.io.*;
import java.util.*;

public class HashtagCounter {
	private static BufferedReader br;

	public static void main(String[] args) {
		// Timer **TEST**
		long startTime = System.currentTimeMillis();

		String filePath = args[0];

		// Hashmap to store hashtag and node
		HashMap<String, Node> hm = new HashMap<>();

		// Fibonacci heap instance
		FibonacciHeap fh = new FibonacciHeap();

		// Output File & writer pointer
		File file = new File("output_file.txt");
		BufferedWriter writer = null;

		// try IOException and other unchecked exceptions
		try {
			br = new BufferedReader(new FileReader(filePath));
			String s = br.readLine().trim();

			// Input validation
			// Pattern p = Pattern.compile("([\\$])([0-9A-Za-z_]+)(\\s)(\\d+)");

			writer = new BufferedWriter(new FileWriter(file));

			while (s != null) {
				s = s.trim();
				System.out.println(s);
				// Matcher m = p.matcher(s);

				// Insert, increase-key operations
				if (s.charAt(0) == '$') {
					String hashTag = s.substring(1, s.indexOf(' '));
					int key = Integer.parseInt(s.substring(s.indexOf(' ') + 1));

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
				else if (s.matches("(\\d+)")) {

					// Nodes to be removed
					int removeNumber = Integer.parseInt(s);
					System.out.println(removeNumber);
					ArrayList<Node> removedNodes = new ArrayList<>(removeNumber);

					// Extract max node and remove from hashmap according to input
					for (int i = 0; i < removeNumber; i++) {
						Node node = fh.extractMax();
						hm.remove(node.getHashTag());

						// Create new node for insertion
						Node newNode = new Node(node.getHashTag(), node.key);

						// Insertion into removed nodes list, add "," until last hashtag
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

				} else if (s.equals("stop"))
					break;

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
