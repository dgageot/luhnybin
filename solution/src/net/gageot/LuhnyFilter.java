package net.gageot;

import static java.lang.Character.getNumericValue;
import static java.lang.Character.isDigit;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class LuhnyFilter {
	private final LinkedList<Character> inBuffer = new LinkedList<Character>();
	private final LinkedList<Character> outBuffer = new LinkedList<Character>();

	public static void main(String[] args) throws Exception {
		new LuhnyFilter().obfuscate(System.in, System.out);
	}

	void obfuscate(InputStream input, PrintStream output) throws IOException {
		while (input.available() > 0) {
			char c = (char) input.read();

			inBuffer.add(c);
			outBuffer.add(c);

			if ('\n' == c) {
				inBuffer.clear();
				flushOutput(output);
			} else if (isLuhny(16) && replaceWithX(16) || isLuhny(15) && replaceWithX(15) || isLuhny(14) && replaceWithX(14)) {
			}
		}

		flushOutput(output);
	}

	private void flushOutput(PrintStream output) {
		for (char c : outBuffer) {
			output.write(c);
		}
		outBuffer.clear();
	}

	private boolean replaceWithX(int n) {
		int count = 0;

		ListIterator<Character> iterator = outBuffer.listIterator(outBuffer.size());
		while (iterator.hasPrevious()) {
			if (isDigit(iterator.previous())) {
				iterator.set('X');
				if (++count == n) {
					return true;
				}
			}
		}
		return false;
	}

	private static int[][] value = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, { 0, 2, 4, 6, 8, 1, 3, 5, 7, 9 } };

	private boolean isLuhny(int n) {
		int sum = 0;
		int count = 0;

		Iterator<Character> fromEnd = inBuffer.descendingIterator();
		while (fromEnd.hasNext()) {
			Character c = fromEnd.next();
			if (isDigit(c)) {
				sum += value[count % 2][getNumericValue(c)];
				if (++count == n) {
					return 0 == (sum % 10);
				}
			} else if (' ' != c && '-' != c) {
				return false;
			}
		}

		return false;
	}
}
