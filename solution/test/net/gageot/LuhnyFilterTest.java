package net.gageot;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.Test;

public class LuhnyFilterTest {
	@Test
	public void dontObfuscateLetters() {
		check("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		check("LF only ->\n<- LF only", "LF only ->\n<- LF only");
	}

	@Test
	public void canObfuscateLuhnyNumbers() {
		check("XXXXXXXXXXXXXX", "56613959932537");
		check("XXXXXXXXXXXXXX", "50873374014065");
		check("XXXXXXXXXXXXXXX", "508733740140655");
		check("XXXXXXXXXXXXXXXX", "5610591081018250");
		check("XXXXXXXXXXXXXX", "00000000000000");
		check("XXXXXXXXXXXXXXX", "000000000000000");
		check("XXXXXXXXXXXXXXXX", "0000000000000000");
	}

	@Test
	public void canObfuscateContinuousLuhnyNumbers() {
		check("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "00000000000000000000000000000000");
		check("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "0000000000000000000000000000000000");
	}

	@Test
	public void dontObfuscateNoneLuhnyNumbers() {
		check("11111111111111", "11111111111111");
		check("49536290423965", "49536290423965");
	}

	@Test
	public void dontObfuscateNumbersTooLong() {
		check("99929316122852072", "99929316122852072");
	}

	@Test
	public void obfuscateNumbersFlankedByNonMatchingDigits() {
		check("987XXXXXXXXXXXXXXXX321", "9875610591081018250321");
	}

	@Test
	public void canObfuscateOnlyDigits() {
		check("XXXX XXXX XXXX XXXX", "4352 7211 4223 5131");
		check("BEFORE XXXXXXXXXXXXXX AFTER", "BEFORE 56613959932537 AFTER");
		check("BEFORE XX-XXXX-XXXX-XXXX AFTER", "BEFORE 56-6139-5993-2537 AFTER");
	}

	@Test
	public void dontObfuscateLuhnyNumbersTooSmall() {
		check("5678", "5678");
	}

	private static void check(String expected, String actual) {
		assertEquals(expected, obfuscate(actual));
	}

	private static String obfuscate(String log) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ByteArrayInputStream input = new ByteArrayInputStream(log.getBytes());

		try {
			new LuhnyFilter().obfuscate(input, new PrintStream(output));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return new String(output.toByteArray());
	}
}
