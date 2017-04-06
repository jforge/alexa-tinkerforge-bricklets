package io.eol.echo.skill.tinkerforge.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttPublisher {
	private static final Logger log = LoggerFactory.getLogger(MqttPublisher.class);

	// TODO externalize in lambda settings.

	public String brokerUri = "tcp://things.online:1883";

	public String clientId = "TinkerForge_Skill";

	private String brokerUsername = "public";

	private String brokerPassword = "0xp4yr";

	public void publish(String topic, String payload) {
		log.info("publish on topic=%s, payload=%s", topic, payload);
		int qosLevel = 0;
		try {
			MqttClient client = connectBroker();
			client.publish(topic, payload.getBytes(), qosLevel, false);
			client.disconnect();
			client.close();
		} catch (MqttException e) {
			log.error("Error publishing mqtt message", e);
		}
	}

	public MqttClient connectBroker() throws MqttException {
		MqttClient client = new MqttClient(brokerUri, clientId + "_publisher", null);
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
