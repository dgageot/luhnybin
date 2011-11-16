package net.gageot;

import static java.lang.Character.isDigit;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;

public class LuhnyFilter {
	final LinkedList<Integer> in = new LinkedList<Integer>();
	final StringBuilder out = new StringBuilder();
	final static int[][] VALUE = { { 0, 2, 4, 6, 8, 1, 3, 5, 7, 9 }, { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 } };

	public static void main(String[] args) throws Exception {
		new LuhnyFilter().anonymize(System.in, System.out);
	}

	void anonymize(InputStream input, PrintStream output) throws IOException {
		while (input.available() > 0) {
			char c = (char) input.read();

			out.append(c);

			if (isDigit(c)) {
				in.add(c - '0');
				replaceLongestLuhny();
			} else if ((' ' != c) && ('-' != c)) {
				output.append(out);
				out.setLength(0);
				in.clear();
			}
		}

		output.append(out);
	}

	void replaceLongestLuhny() {
		int max = 0;
		int sum = 0;

		for (int i = 1; i <= in.size() && i <= 16; i++) {
			if (0 == ((sum += VALUE[i % 2][in.get(in.size() - i)]) % 10) && i >= 14) {
				max = i;
			}
		}

		for (int i = out.length() - 1; max > 0;) {
			char c = out.charAt(i--);
			if (isDigit(c) || 'X' == c) {
				out.setCharAt(i + 1, 'X');
				max--;
			}
		}
	}
}
