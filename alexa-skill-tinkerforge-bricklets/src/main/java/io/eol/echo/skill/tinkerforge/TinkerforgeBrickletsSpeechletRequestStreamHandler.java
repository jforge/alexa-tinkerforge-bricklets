package io.eol.echo.skill.tinkerforge;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

/**
 * This class is the handler for an AWS Lambda function powering an Alexa Skill.
 * <br/>Set the handler field in the AWS Lambda console to
 * "io.eol.echo.skill.tinkerforge.TinkerforgeBrickletsSpeechletRequestStreamHandler"
 */
public final class TinkerforgeBrickletsSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
	private static final Set<String> supportedApplicationIds;

	static {
		/*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
		supportedApplicationIds = new HashSet<String>();
		supportedApplicationIds.add("amzn1.ask.skill.0cddb736-135b-4c74-abf7-76fa3455e57e");
		// supportedApplicationIds.add("amzn1.echo-sdk-ams.app.[unique-value-here]");
	}

	public TinkerforgeBrickletsSpeechletRequestStreamHandler() {
		super(new TinkerforgeBrickletsSpeechlet(), supportedApplicationIds);
	}
}
