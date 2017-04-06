package io.eol.echo.skill.tinkerforge;

import java.util.Arrays;
import java.util.List;

public final class TinkerforgeBrickletsTextUtil {

	public static final String COMPLETE_HELP =
			"Here's some things you can say. Count to 100, Display <this text>, move motor 120 steps, tell weight and exit.";
	public static final String NEXT_HELP = "You can count to a number, display a text, move a stepper motor or say help. What would you like?";

	private static final List<String> NAME_BLACKLIST = Arrays.asList("device", "devices");

	private TinkerforgeBrickletsTextUtil() {
	}

	/**
	 * Cleans up the device name, and sanitizes it against the blacklist.
	 */
	public static String getDeviceName(String recognizedDeviceName) {
		if (recognizedDeviceName == null || recognizedDeviceName.isEmpty()) {
			return null;
		}

		String cleanedName;
		if (recognizedDeviceName.contains(" ")) {
			// the name should only contain a first name, so ignore the second part if any
			cleanedName = recognizedDeviceName.substring(recognizedDeviceName.indexOf(" "));
		} else {
			cleanedName = recognizedDeviceName;
		}

		// if the name is on our blacklist, it must be mis-recognition
		if (NAME_BLACKLIST.contains(cleanedName)) {
			return null;
		}

		return cleanedName;
	}
}
