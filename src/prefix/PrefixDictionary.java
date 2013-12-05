package prefix;

/**
 * Interface for a Prefix Dictionary. The dictionary will be created from a CSV
 * file of (Key<String>, Value<Integer>) pairs.
 * 
 * @author Benjamin Carle
 */
public interface PrefixDictionary {
	/**
	 * Compute the sum of values for all entries in the dictionary with the
	 * prefix string as a prefix of the key.
	 * 
	 * @param prefix
	 *            the prefix string to match return the sum of matching values
	 */
	public long sum(String prefix);
}