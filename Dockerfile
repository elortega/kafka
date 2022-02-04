FROM openjdk:11

USER root

# System enviroment variables.
ENV KAFKA_DIR /tmp/vo-assessment/kafka_2.13-3.1.0
ENV KAFKA_TOPIC assessmentVO

WORKDIR /tmp
RUN mkdir vo-assessment
WORKDIR /tmp/vo-assessment

# Download and install kafka.
RUN wget --quiet https://www.apache.org/dist/kafka/3.1.0/kafka_2.13-3.1.0.tgz
RUN tar zxf kafka_2.13-3.1.0.tgz
WORKDIR $KAFKA_DIR

#  Transfer the entrypoint script used to start kafka zookeeper, server & topic.
COPY startKafka.sh ./startKafka.sh
RUN chmod 744 startKafka.sh

ENTRYPOINT ${KAFKA_DIR}"/startKafka.sh"