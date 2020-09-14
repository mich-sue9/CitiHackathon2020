# Dummy Trade Fulfillment Engine

### Outline
This is a simple spring-boot app that will simulate a system that sends trade requests to be executed at an exchange. This system does not actually send the reuests to an exchange, it is just a SIMULATOR for testing.

On a timed interval this service will read from a MongoDB database. It will look for records that represent Trade objects. Each object represents a request to make a trade i.e. a BUY or a SELL.

Each trade request is expected to move through the following states:
1. CREATED : All trade records are initially marked with this state to indicate the request has just been created.
2. PROCESSING : When trade requests have been sent to a simulated exchange they will be marked as PROCESSING to indicate they are currently being handled.
3. FILLED : A trade request that was successfull will be marked as FILLED.
4. REJECTED : A trade requests that was unsuccessfull will be marked as REJECTED.

It will mark all records that it finds with state "CREATED" as "PROCESSING".

It will mark all records that it finds with state "PROCESSING" as either "FILLED" or "REJECTED". The decision between "FILLED" and "REJECTED" is completely random.

You may edit the source code as you require to meet the needs of testing and demonstrating your system.

The two areas you will initially need to update are:
1. The Trade model (com.conygre.training.tradesimulator.model). This should be changed to match your design from the Java Hackathon Trade REST API.
2. You may want to update the application.properties file so that this service uses the same mongodb host and database as your Trade REST API.

### Run
Build on the command line with gradle:

```./gradlew build```

This will put a jar in build/libs/trade-simulator-0.0.1-SNAPSHOT.jar

Run that jar with:

```java -jar build/libs/trade-simulator-0.0.1-SNAPSHOT.jar```

OR for example with DEBUG logging and on a different port (8089):

```java -DLOG_LEVEL=DEBUG -DSERVER_PORT=8089 -jar build/libs/trade-simulator-0.0.1-SNAPSHOT.jar```


OR just run in VSCode or any other IDE for development

### Docker
There is a Dockerfile included to run as a Docker container.

e.g. to build a container:

```docker build -t trade-sim:0.0.1 .```

e.g. to run the built container with DEBUG logging:

```docker run --name trade-sim -e LOG_LEVEL=DEBUG trade-sim:0.0.1```

### Configuration
See the properties file in src/main/resources/application.properties for configurable properties.

Most properties can be overridden by environmental variables.

For Example:

```${DB_HOST:localhost}```
is saying that it'll connect to mongodb at localhost, unless there is an environmental variable called DB_HOST - in which case it'll use the value of that env variable instead

```${DB_NAME:tradedb}```
is saying it'll connect to a database called tradedb unless there is an environmental variable called DB_NAME - in which case it'll use the value of that env variable instead
