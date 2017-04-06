# Alexa-tinkerforge-bricklets
Components for the alexa skill to control tinkerforge actuators

## Alexa-skill-tinkerforge-bricklets
Alexa skill lambda component, intent schema and utterances.

## Tinkerforge-mqtt-router
MQTT subscriber and publisher as content-based router and
message translator to create mqtt-brick-proxy compatible 
message structures, when consuming simplified alexa messages.

#### MQTT Brick Proxy
Generally a mqtt broker is used to decouple tinkerforge devices
from applications instrumenting the sensors and actuators.

The proxy is a python program dynamically detecting tinkerforge bricklets
and mapping them to a mqtt broker.

It publishes available bricklet state changes (e.g. temperature value)
and subscribes to mqtt topics for incoming control commands for actuators.

Download: 
https://github.com/Tinkerforge/brick-mqtt-proxy

