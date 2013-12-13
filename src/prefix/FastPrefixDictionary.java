package prefix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Class to store a table of (Key<String>, Value<Integer>) pairs.
 * 
 * My approach was to store the dictionaries in a TreeMap, which utilizes a
 * red-black tree for storage and supplies fast retrieval times.
 * 
 * To find all matching keys for a given prefix, I used the TreeMap's function
 * 'subMap', which has two versions. I used the one that takes in two binding
 * strings and a boolean to determine whether the bounds were included in the
 * submap. I made the submap range from the prefix itself, inclusive, to the
 * next string that would not match prefix (the next lexographically greatest),
 * exclusive. Since I could not anticipate how long a prefix could be, I found
 * the first string that would not match and set the bound from there.
 * 
 * I then exported the values in that submap to a Collection using the function
 * 'values', and iterating over the values, summed them together.
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
		prefix = prefix.trim();
		long sum = 0;

		// First we need the last character in the prefix string
		char lastPrefixChar = prefix.charAt(prefix.length() - 1);
		char lpcCopy = lastPrefixChar;
		// Incrementing a char sets it to the next char in lexographic order
		char lastLexNextChar = ++lpcCopy;

		// The index of the last occurrence of lastPrefixChar in prefix (this
		// ends up being the last character in prefix anyway, but this way I
		// don't need to worry about out-of-bounds errors)
		int step1 = prefix.lastIndexOf(lastPrefixChar);
		// We replace the last character of prefix with the incremented character
		String lexNext = prefix.substring(0, step1) + lastLexNextChar;

		// Makes a new submap that starts from the prefix itself (inclusive) and
		// ends at the next string in lexographic order (exclusive)
		NavigableMap<String, Integer> submap = tm.subMap(prefix, true, lexNext, false);

		// Loop through the collection and sum the values
		Collection<Integer> valColl = submap.values();
		for (int val : valColl)
			sum += val;
		return sum;
	}
}