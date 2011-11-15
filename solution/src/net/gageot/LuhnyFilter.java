package net.gageot;

import static java.lang.Character.isDigit;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class LuhnyFilter {
	final StringBuilder in = new StringBuilder();
	final StringBuilder out = new StringBuilder();
	final static int[][] value = { { 0, 2, 4, 6, 8, 1, 3, 5, 7, 9 }, { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 } };

	public static void main(String[] args) throws Exception {
		new LuhnyFilter().anonymize(System.in, System.out);
	}

	void anonymize(InputStream input, PrintStream output) throws IOException {
		while (input.available() > 0) {
			char c = (char) input.read();

			if (isDigit(c)) {
				in.append(c);
				out.append(c);
				replaceLongestLuhny();
			} else if ((' ' == c) || ('-' == c)) {
				out.append(c);
			} else {
				flush(output);
				output.write(c);
			}
		}

		flush(output);
	}

	void flush(PrintStream output) {
		output.append(out);
		out.setLength(0);
		in.setLength(0);
	}

	void replaceLongestLuhny() {
		int max = 0;
		int sum = 0;

		for (int i = 1; i <= in.length() && i <= 16; i++) {
			sum += value[i % 2][in.charAt(in.length() - i) - '0'];
			if (0 == (sum % 10)) {
				max = i;
			}
		}
		
		if (max < 14)
			return;

		for (int i = out.length() - 1; i >= 0; i--) {
			if (isDigit(out.charAt(i))) {
				out.setCharAt(i, 'X');
				if (--max == 0)
					return;
			}
		}
	}
}
