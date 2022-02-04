#!/bin/bash

# Entry point script for the Docker Kafka image. This script starts up 
# Kafka with a single topic.
# 
# Environment variables expected:
#  - KAFKA_DIR - directory where Kafka has been installed.
#  - KAFKA_TOPIC - kafka topic name to use.

cd $KAFKA_DIR
echo "Starting Zookeper..."
$KAFKA_DIR/bin/zookeeper-server-start.sh config/zookeeper.properties &
sleep 1

echo "Starting Kafka Broker..."
$KAFKA_DIR/bin/kafka-server-start.sh config/server.properties &
sleep 3

echo "Creating topic "$KAFKA_TOPIC"..."
$KAFKA_DIR/bin/kafka-topics.sh --create --topic $KAFKA_TOPIC --bootstrap-server localhost:9092

read -p "Press any key to exit the container\n"

