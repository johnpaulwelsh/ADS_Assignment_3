package prefix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Class to store a table of (Key<String>, Value<Integer>) pairs.
 * 
 * @author John Paul Welsh
 */
public class FastPrefixDictionary implements PrefixDictionary {
	// A TreeMap, which implements a red-black tree
	private TreeMap<String, Integer> tm;

	/**
	 * Constructor to build the dictionary from a CSV file.
	 * 
	 * @param filename
	 *            the path of the CSV file
	 */
	public FastPrefixDictionary(String filename) {
		tm = new TreeMap<String, Integer>();
		String key;
		int value;
		try {
			BufferedReader file = new BufferedReader(new FileReader(filename));
			String line;
			String[] lineList;
			while ((line = file.readLine()) != null) {
				lineList = line.split(",");
				key = lineList[0].trim();
				value = Integer.parseInt(lineList[1].trim());
				tm.put(key, value);
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
	 * We make a submap containing all the possible matching keys for the prefix,
	 * and iterate through them to get the sum.
	 * 
	 * @param prefix
	 *            the prefix string
	 * @return the sum of all matching values
	 */
	public long sum(String prefix) {
		// Trim the prefix to get rid of leading and trailing whitespace
		prefix = prefix.trim();
		long sum = 0;

		// First we need the last character in the prefix string
		char lastPrefixChar = prefix.charAt(prefix.length() - 1);
		// Copy of the lastPrefixChar
		char lpcCopy = lastPrefixChar;
		// Increment the copy and set it to another variable. Incrementing a
		// char sets it to the next char in lexographic order
		char lastLexNextChar = ++lpcCopy;

		// The index of the last occurrence of lastPrefixChar in prefix (this
		// ends up being the last character in prefix anyway, but this way I
		// don't need to worry about out-of-bounds errors)
		int step1 = prefix.lastIndexOf(lastPrefixChar);
		// We replace the last character of prefix with the incremented character
		String lexNext = prefix.substring(0, step1) + lastLexNextChar;

		// Makes a new submap that starts from the prefix itself (inclusive) and
		// ends at the next string in lexographic order (exclusive). This puts an
		// bound on the new map so it will only contain keys that begin with prefix 
		NavigableMap<String, Integer> submap = tm.subMap(prefix, true, lexNext, false);

		// This method only takes longer when there is no sum for the given
		// prefix, so I hard-coded in the case where the submap is empty
		if (submap.size() == 0) {
			return 0;
		// Otherwise, loop through the collection and sum the values
		} else {
			Collection<Integer> valColl = submap.values();
			for (int val : valColl)
				sum += val;
			return sum;
		}
	}
}