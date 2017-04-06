package io.eol.echo.skill.tinkerforge;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Speechlet for a Lambda function handling Alexa Skill requests to control Tinkerforge hardware.
 * <p>
 * <ul>
 * <li><b>Multiple slots</b>: has 2 slots (device name and number)</li>
 * <li><b>NUMBER slot</b>: demonstrates how to handle number slots.</li>
 * <li><b>Custom slot type</b>: demonstrates using custom slot types to handle a finite set of known values</li>
 * <li><b>Dialog and Session state</b>: Handles two models, both a one-shot ask and tell model, and
 * a multi-turn dialog model. If the user provides an incorrect slot in a one-shot model, it will
 * direct to the dialog model. See sample interactions for these models below.</li>
 * </ul>
 * <p>
 * <h2>Examples</h2>
 * <p>
 * <b>Dialog model</b>
 * <p>
 * User: "Alexa, tell tinkerforge bricklets to display text."
 * <p>
 * Alexa: "text displayed. Who do you want to do first?"
 * <p>
 * User: "Count to 100."
 * <p>
 * Alexa: "Counter started"
 * <p>
 * User: "Move motor"
 * <p>
 * Alexa: "StepperMotor started"
 * <p>
 * (skill ends)
 * <p>
 * User: "Alexa, tell tinkerforge bricklet to count to 100."
 * <p>
 * Alexa: "Counting to 100"
 * <p>
 * (skill ends)
 * <p>
 * <b>One-shot model</b>
 * <p>
 * User: "Alexa, move the motor"
 * <p>
 * Alexa: "Stepper motor started"
 */
public class TinkerforgeBrickletsSpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(TinkerforgeBrickletsSpeechlet.class);

	private TinkerforgeBrickletsManager tinkerforgeBrickletsManager;

	private SkillContext skillContext;

	@Override
	public void onSessionStarted(final SessionStartedRequest request, final Session session)
			throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());

		initializeComponents();

		// if user said a one shot command that triggered an intent event,
		// it will start a new session, and then we should avoid speaking too many words.
		skillContext.setNeedsMoreHelp(false);
	}

	@Override
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
			throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());

		skillContext.setNeedsMoreHelp(true);
		return tinkerforgeBrickletsManager.getLaunchResponse(request, session);
	}

	@Override
	public SpeechletResponse onIntent(IntentRequest request, Session session)
			throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());
		initializeComponents();

		Intent intent = request.getIntent();

		if ("NewControlIntent".equals(intent.getName())) {
			return tinkerforgeBrickletsManager.getNewControlIntentResponse(session, skillContext);

		} else if ("AddDeviceIntent".equals(intent.getName())) {
			return tinkerforgeBrickletsManager.getAddDeviceIntentResponse(intent, session, skillContext);

		} else if ("DeviceControlIntent".equals(intent.getName())) {
			return tinkerforgeBrickletsManager.getDeviceControlIntentResponse(intent, session, skillContext);

		} else if ("TellWeightIntent".equals(intent.getName())) {
			return tinkerforgeBrickletsManager.getTellWeightIntentResponse(intent, session, request.getLocale());

		} else if ("ResetDevicesIntent".equals(intent.getName())) {
			return tinkerforgeBrickletsManager.getResetDevicesIntentResponse(intent, session);

		} else if ("AMAZON.HelpIntent".equals(intent.getName())) {
			return tinkerforgeBrickletsManager.getHelpIntentResponse(intent, session, skillContext);

		} else if ("AMAZON.CancelIntent".equals(intent.getName())) {
			return tinkerforgeBrickletsManager.getExitIntentResponse(intent, session, skillContext);

		} else if ("AMAZON.StopIntent".equals(intent.getName())) {
			return tinkerforgeBrickletsManager.getExitIntentResponse(intent, session, skillContext);

		} else {
			throw new IllegalArgumentException("Unrecognized intent: " + intent.getName());
		}
	}

	@Override
	public void onSessionEnded(final SessionEndedRequest request, final Session session)
			throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());
		// any cleanup logic goes here
	}

	/**
	 * Initializes the instance components if needed.
	 */
	private void initializeComponents() {
		tinkerforgeBrickletsManager = new TinkerforgeBrickletsManager();
		skillContext = new SkillContext();
	}
}
