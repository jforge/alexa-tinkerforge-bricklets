# AWS Lambda function for Alexa Skill Tinkerforge Bricklets

A [AWS Lambda](http://aws.amazon.com/lambda) function that instruments tinkerforge actuators for use with Amazon Echo Voice UI.

## Concepts
This sample shows how to create a Lambda function for handling Alexa Skill requests that:

- Multiple slots: has 2 slots (name and number)
- NUMBER slot: demonstrates how to handle number slots.
- Custom slot type: demonstrates using custom slot types to handle a finite set of known values
- Dialog and Session state: Handles two models, both a one-shot ask and tell model, and a multi-turn dialog model.
    If the user provides an incorrect slot in a one-shot model, it will direct to the dialog model. 
    See the examples section for sample interactions of these models.

## Setup
This skill required a deployment as a AWS Lambda Service and an Alexa Skill to use this service.

### AWS Lambda Setup
1. Go to the AWS Console and click on the Lambda link. Note: ensure you are in eu-west-1 oder us-east-1 (otherwise a Alexa skill can't be used as a trigger for the Lambda service).
2. Click on the Create a Lambda Function or Get Started Now button.
3. Skip the blueprint
4. Name the Lambda Function "Tinkerforge-Bricklets-Skill".
5. Select the runtime as Java 8
6. Go to the the root directory containing pom.xml, and run 'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'. 
This will generate a zip file named "alexa-skill-tinkerforge-bricklets-1.0-jar-with-dependencies.jar" in the target directory.
7. Select Code entry type as "Upload a .ZIP file" and then upload the "alexa-skill-tinkerforge-bricklets-1.0-jar-with-dependencies.jar" file from the build directory to Lambda
8. Set the Handler as io.eol.echo.skill.tinkerforge.TinkerforgeBrickletsSpeechletRequestStreamHandler (this refers to the Lambda RequestStreamHandler file in the zip).
9. Create or use the "basic-lambda-execution" role and click create.
10. Leave the Advanced settings as the defaults.
11. Click "Next" and review the settings then click "Create Function"
12. Click the "Event Sources" tab and select "Add event source"
13. Set the Event Source type as Alexa Skills kit and Enable it now. Click Submit.
14. Copy the ARN from the top right to be used later in the Alexa Skill Setup.

### Alexa Skill Setup
1. Go to the [Alexa Console](https://developer.amazon.com/edw/home.html) and click Add a New Skill.
2. Set "Tinkerforge Bricklets" as the skill name and "tinkerforge bricklets" as the invocation name, this is what is used to activate your skill. For example you would say: "Alexa, Ask tinkerforge bricklets for the current weight."
3. Select the Lambda ARN for the skill Endpoint and paste the ARN copied from above. Click Next.
4. Copy the custom slot types from the customSlotTypes folder. Each file in the folder represents a new custom slot type. The name of the file is the name of the custom slot type, and the values in the file are the values for the custom slot.
5. Copy the Intent Schema from the included IntentSchema.json.
6. Copy the Sample Utterances from the included SampleUtterances.txt. Click Next.
7. Go back to the skill Information tab and copy the appId. Paste the appId into the TinkerforgeBrickletsSpeechletRequestStreamHandler.java file for the variable supportedApplicationIds,
   then update the lambda source zip file with this change and upload to lambda again, this step makes sure the lambda function only serves request from authorized source.
8. The skill is now available on the [Echo webpage](http://echo.amazon.com/#skills) and see your skill enabled.
9. In order to test it, try to say some of the Sample Utterances from the Examples section below.
10. Your skill is now saved and once you are finished testing you can continue to publish your skill.

## Examples
### Dialog model:
    User: "Alexa, tell tinkerforge bricklets to reset."
    Alexa: "Actuators reset. Where do you want to go today?"
    User: "Count from 100 to zero on display 1"
    Alexa: "Counter started, current value is three"
    User: "Display weight"
    Alexa: "Weight is shown on display"

    (skill ends)

    User: "Alexa, tell tinkerforge bricklets to rotate 120 degress."
    Alexa: "Rotating compoonents ends in 10 seconds"

    (skill ends)

### One-shot model:
    User: "Alexa, ask tinkerforge bricklets to count from 100 to zero.
    Alexa: "Counter started, current value is five."
