package io.eol.echo.skill.tinkerforge;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import io.eol.echo.skill.tinkerforge.mqtt.MqttPublisher;

/**
 * The {@link TinkerforgeBrickletsManager} receives various events and intents and manages the interaction flow.
 */
public class TinkerforgeBrickletsManager {
	private static final String SLOT_DEVICE_NAME = "DeviceName";

	private static final String SLOT_NUMBER = "Number";

	public TinkerforgeBrickletsManager() {
	}

	public SpeechletResponse getLaunchResponse(LaunchRequest request, Session session) {
		// Speak welcome message and ask user questions
		// based on whether there are registered devices or not.
		String speechText, repromptText;
//        if (getRegisteredDevices().size() > 0) {
//            repromptText = TinkerforgeBrickletsTextUtil.COMPLETE_HELP;
//        } else {
		speechText = "Tinkerforge Bricklets, What can I do for you?";
		repromptText = TinkerforgeBrickletsTextUtil.NEXT_HELP;
//        }

		return getAskSpeechletResponse(speechText, repromptText);
	}

	public SpeechletResponse getNewControlIntentResponse(Session session, SkillContext skillContext) {
		String speechText = "New control started with devices.";

		if (skillContext.needsMoreHelp()) {
			String repromptText =
					"You can move a motor, count to a number, display some text or exit. What would you like?";
			speechText += repromptText;
			return getAskSpeechletResponse(speechText, repromptText);
		} else {
			return getTellSpeechletResponse(speechText);
		}
	}

	public SpeechletResponse getAddDeviceIntentResponse(Intent intent, Session session,
														SkillContext skillContext) {
		// add a device to the current active list of devices
		// terminate or continue the conversation based on whether the intent
		// is from a one shot command or not.
		String newDeviceName =
				TinkerforgeBrickletsTextUtil.getDeviceName(intent.getSlot(SLOT_DEVICE_NAME).getValue());
		if (newDeviceName == null) {
			String speechText = "OK. What do you want to so?";
			return getAskSpeechletResponse(speechText, speechText);
		}

		String speechText = newDeviceName + " has been registered. ";
		String repromptText = null;

		if (skillContext.needsMoreHelp()) {
			speechText += "You can say, move motor, count to a number, display a text?";
			speechText += "What is your next action?";
			repromptText = TinkerforgeBrickletsTextUtil.NEXT_HELP;
		}

		if (repromptText != null) {
			return getAskSpeechletResponse(speechText, repromptText);
		} else {
			return getTellSpeechletResponse(speechText);
		}
	}

	public SpeechletResponse getDeviceControlIntentResponse(Intent intent, Session session,
															SkillContext skillContext) {
		String deviceName =
				TinkerforgeBrickletsTextUtil.getDeviceName(intent.getSlot(SLOT_DEVICE_NAME).getValue());
		if (deviceName == null) {
			String speechText = "Sorry, I did not hear the device name. Please say again?";
			return getAskSpeechletResponse(speechText, speechText);
		}

		int number = 0;
		try {
			number = Integer.parseInt(intent.getSlot(SLOT_NUMBER).getValue());
		} catch (NumberFormatException e) {
			String speechText = "Sorry, I did not hear the number. Please say again?";
			return getAskSpeechletResponse(speechText, speechText);
		}

		publishMessage(deviceName, number);

		String speechText = number + " for " + deviceName + ". ";
		speechText += deviceName + " has been triggered.";

		return getTellSpeechletResponse(speechText);
	}

	private void publishMessage(String device, int number) {

		// TODO use lambda env variable to configure broker and topics
		MqttPublisher publisher = new MqttPublisher();

		if (device.contains("motor") || device.contains("stepper")) {
			String topic = "echo/move";
			int payload = number;
			publisher.publish(topic, String.valueOf(payload));
		} else {
			String topic = "echo/move";
			int payload = number;
			publisher.publish(topic, String.valueOf(payload));
		}
	}

	public SpeechletResponse getTellWeightIntentResponse(Intent intent, Session session) {
		SortedMap<String, Long> sortedWeights = new TreeMap<>();
		String speechText = "overweight";
		Card weightCard = getWeightCard(sortedWeights);

		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		return SpeechletResponse.newTellResponse(speech, weightCard);
	}

	public SpeechletResponse getResetDevicesIntentResponse(Intent intent, Session session) {
		String speechText = "New control started without devices. What do you want to do first?";
		return getAskSpeechletResponse(speechText, speechText);
	}

	public SpeechletResponse getHelpIntentResponse(Intent intent, Session session,
												   SkillContext skillContext) {
		return skillContext.needsMoreHelp() ? getAskSpeechletResponse(
				TinkerforgeBrickletsTextUtil.COMPLETE_HELP + " So, how can I help?",
				TinkerforgeBrickletsTextUtil.NEXT_HELP)
				: getTellSpeechletResponse(TinkerforgeBrickletsTextUtil.COMPLETE_HELP);
	}

	public SpeechletResponse getExitIntentResponse(Intent intent, Session session,
												   SkillContext skillContext) {
		return skillContext.needsMoreHelp() ? getTellSpeechletResponse("Okay. Whenever you're "
				+ "ready, you can start controlling tinkerforge actuators.")
				: getTellSpeechletResponse("");
	}

	/**
	 * Returns an ask Speechlet response for a speech and reprompt text.
	 */
	private SpeechletResponse getAskSpeechletResponse(String speechText, String repromptText) {
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Session");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		// Create reprompt
		PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
		repromptSpeech.setText(repromptText);
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromptSpeech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	/**
	 * Returns a tell Speechlet response for a speech and reprompt text.
	 */
	private SpeechletResponse getTellSpeechletResponse(String speechText) {
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Session");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		return SpeechletResponse.newTellResponse(speech, card);
	}

	private Card getWeightCard(Map<String, Long> weights) {
		StringBuilder weight = new StringBuilder();
		int index = 0;
		for (Entry<String, Long> entry : weights.entrySet()) {
			index++;
			weight
					.append("No. ")
					.append(index)
					.append(" - ")
					.append(entry.getKey())
					.append(" : ")
					.append(entry.getValue())
					.append("\n");
		}

		SimpleCard card = new SimpleCard();
		card.setTitle("Weight");
		card.setContent(weight.toString());
		return card;
	}
}
