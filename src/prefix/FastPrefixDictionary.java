package prefix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Interface for a Prefix Dictionary. The dictionary will be created from a CSV
 * file of (Key<String>, Value<Integer>) pairs.
 * 
 * @author John Paul Welsh
 */
public class FastPrefixDictionary implements PrefixDictionary {
	private TreeMap<String, Integer> tm;
	private NavigableMap<String, Integer> submap;
	private String lexNext;

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
	 * @param prefix
	 *            the prefix string
	 * @return the sum of all matching values
	 */
	public long sum(String prefix) {
		prefix = prefix.trim();
		long sum = 0;
		
		// Gets the last character in the prefix string
		char lastPrefixChar = prefix.charAt(prefix.length() - 1);
		// Copies the lastPrefixChar so we can ++ it
		char lpcCopy = lastPrefixChar;
		// Sets the incremented character to another variable
		char lastLexNextChar = ++lpcCopy;
		
		// Finds the index of the last occurrence of lastPrefixChar in prefix
		int step1 = prefix.lastIndexOf(lastPrefixChar);
		// Sets lexNext to be the prefix, with its last character replaced with lastLexNextChar
		lexNext = prefix.substring(0, step1) + lastLexNextChar;

		// Makes a new submap that starts from the prefix itself (inclusive) and ends
		// at the next string in lexographic order (exclusive) 
		submap = tm.subMap(prefix, true, lexNext, false);
		
		// This method only takes longer when there is no sum for the given prefix,
		// so I hard-coded in the case where the submap is empty
		if (submap.size() == 0) {
			return 0;
		// Otherwise, loop through the collection, add the values, and return it
		} else {
			// Gets all of the values out of the subtree and puts them into a Collection
			Collection<Integer> valColl = submap.values();
			for (int val : valColl)
				sum += val;
			return sum;
		}
	}
}