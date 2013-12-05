package prefix;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.text.*;

/**
 * Class to compare search and sum times for NaivePrefixDictionary and
 * FastPrefixDictionary.
 * 
 * @author Benjamin Carle
 */
public class Test {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java prefix.Test DICTIONARY_FILENAME");
		} else {
			// --- Create the dictionary objects ---//
			System.out.println();
			System.out.println(" . . . Loading NaivePrefixDictionary");
			PrefixDictionary npd = new NaivePrefixDictionary(args[0]);
			System.out.println(" . . . Loading FastPrefixDictionary");
			PrefixDictionary fpd = new FastPrefixDictionary(args[0]);
			System.out.println();

			Scanner scn = new Scanner(System.in);
			String prefix;
			long start, end;
			int naiveDelta, fastDelta;
			double speedup;
			long sum;
			DecimalFormat deltaDF = new DecimalFormat("###,###");

			// loop to accept prefix tests
			while (true) {
				// request input
				System.out.print("Prefix:  ");
				prefix = scn.nextLine().trim();

				// end on empty
				if (prefix.equals(""))
					break;

				// test naive dictionary
				System.out.print(" - Naive Sum = ");
				start = System.nanoTime();
				sum = npd.sum(prefix);
				end = System.nanoTime();
				System.out.print(sum);
				naiveDelta = (int) (end - start);
				System.out.println(" . . . "
						+ String.format("%15s", deltaDF.format(naiveDelta))
						+ " ns");

				// test fast dictionary
				System.out.print(" - Fast  Sum = ");
				start = System.nanoTime();
				sum = fpd.sum(prefix);
				end = System.nanoTime();
				System.out.print(sum);
				fastDelta = (int) (end - start);
				System.out.println(" . . . "
						+ String.format("%15s", deltaDF.format(fastDelta))
						+ " ns");

				// calculate speedup
				speedup = (double) naiveDelta / fastDelta;
				DecimalFormat speedupDF = new DecimalFormat("###.##");
				System.out.println(" - Speedup = " + speedupDF.format(speedup));
				System.out.println();
			}
		}
	}
}