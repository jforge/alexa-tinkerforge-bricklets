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


## Existing infrastructure
The existing secure infrastructure should be used by the Lambda service, further specific cloud services should be avoided.
For this infrastructure is up, running and secure, the Alexa skill is just a Voice UI add-on.

![alt text][mqtt-smarthome-setup]

[mqtt-smarthome-setup]: https://github.com/jforge/alexa-tinkerforge-bricklets/raw/master/src/docs/images/mosquitto-setup5.jpg "MQTT Smarthome Setup"

## Infrastructure with the Alexa Skill as a client controlling actuators

![alt text][alexa-mqtt-setup]

[alexa-mqtt-setup]: https://github.com/jforge/alexa-tinkerforge-bricklets/raw/master/src/docs/images/alexa-setup.png "MQTT Setup with Alexa"

## Purpose of this sample

- I did in Java 8 because I'm used to this language, frameworks, tools and huge eco system.
- I wanted to see
  - successful number recognition using internal Number SlotType.
  - sessions and reprompts in user dialog
  - internationalization for english and german skills
  - successful mqtt connections outside the Amazon cloud for real decoupling and a secured and existing tls-bridged smarthome infrastrucutre
  - better logging in CloudWatch
  - successful denial of access without the right skill by using the appId in the request handler. 
  - if i am able to create better utterances than before (seems to be extremely important ;-)
  - if i am able to speed up writing a skill (more or less successful with big java stuff in the medieval internet zone called germany)

- Results:
  - Switch to Node.js (6), because of huge upload size in java packages and the fact, 
  that no required component for my skill is missing in the Node.js eco system (paho mqtt, tinkerforge api).
  - => there is currently no real value in avoiding Node 


## Questions (2017-04-06)

- Why can't one use Alexa Skill as a lambda trigger in EU/Frankfurt (just eu-west-1, Ireland)?
- Why no Enterprise WPA support to connect Echo to a Wifi?
- How-to read dialog history via api, not only the Alexa App
- How-to programmatically create and update skills
- Where is the information about general Timeouts for external api calls?
- To use promises in inline Node.js skills was a problem, is this generally no good idea?
- Is there a tool for Quality management of utterances and story design?
