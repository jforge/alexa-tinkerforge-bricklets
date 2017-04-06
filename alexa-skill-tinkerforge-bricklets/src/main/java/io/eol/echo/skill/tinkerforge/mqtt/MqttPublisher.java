package io.eol.echo.skill.tinkerforge.mqtt;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttPublisher {
	private static Logger log = Logger.getLogger(MqttPublisher.class);

	// TODO externalize in lambda settings.

	public String brokerUri = "tcp://things.online:1883";

	public String clientId = "TinkerForge_Skill";

	private String brokerUsername = "public";

	private String brokerPassword = "0xp4yr";

	public void publish(String topic, String payload) {
		try {
			MqttClient client = connectBroker();
			publishMqttMessage(client, topic, payload.getBytes(), false);
			client.disconnect();
			client.close();
		} catch (MqttException e) {
			log.error("", e);
		}
	}

	public static void publishMqttMessage(IMqttClient client, String topic, byte[] payload, boolean retained) {
		int qosLevel = 0;
		if (client != null) {
			try {
				// self.client.publish(GLOBAL_TOPIC_PREFIX + topic, json.dumps(payload, separators=(',',':')), *args, **kwargs)
				client.publish(topic, payload, qosLevel, retained);
			} catch (MqttException e) {
				e.printStackTrace(); // TODO aws compatible logging
			}
		}
	}

	public MqttClient connectBroker() throws MqttException {
		MqttClient client = new MqttClient(brokerUri, clientId + "_publisher");
		MqttConnectOptions options = new MqttConnectOptions();
		options.setKeepAliveInterval(0);
		options.setConnectionTimeout(1);
		options.setCleanSession(true);
		options.setUserName(brokerUsername);
		options.setPassword(brokerPassword.toCharArray());
		client.setCallback(null);
		client.connect(options);
		return client;
	}

	public void disconnectBroker(MqttClient client) throws MqttException {
		client.disconnect();
		client.close();
	}
}
