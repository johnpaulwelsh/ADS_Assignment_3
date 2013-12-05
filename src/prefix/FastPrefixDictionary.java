package prefix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface for a Prefix Dictionary. The dictionary will be created from a CSV
 * file of (Key<String>, Value<Integer>) pairs.
 * 
 * @author John Paul Welsh
 */
public class FastPrefixDictionary implements PrefixDictionary {
	private List<String> keys;
	private List<Integer> values;

	/**
	 * Constructor to build the dictionary from a CSV file.
	 * 
	 * @param filename
	 *            the path of the CSV file
	 */
	public FastPrefixDictionary(String filename) {
		keys = new ArrayList<String>();
		values = new ArrayList<Integer>();
		try {
			BufferedReader file = new BufferedReader(new FileReader(filename));
			String line;
			String[] lineList;
			while ((line = file.readLine()) != null) {
				lineList = line.split(",");
				keys.add(lineList[0].trim());
				values.add(Integer.parseInt(lineList[1].trim()));
			}
			file.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	/**
	 * Method to return the sum of all values with keys containing the given
	 * string as a prefix. Note, sum must be a long since sum of values may
	 * exceed int capacity.
	 * 
	 * @param prefix
	 *            the prefix string
	 * @return the sum of all matching values
	 */
	public long sum(String prefix) {
		prefix = prefix.trim();
		long sum = 0;
		for (int i = 0; i < keys.size(); i++)
			if (keys.get(i).startsWith(prefix))
				sum += values.get(i);
		return sum;
	}
}