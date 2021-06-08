# Local Setup

## Create Directories
Create the following directories: (__currently not used__)

    ~/docker/volumes/mariadb
    ~/docker/volumes/zookeeper
    ~/docker/volumes/kafka/kafka-0
    ~/docker/volumes/kafka/kafka-1
    ~/docker/volumes/kafka/kafka-2

These directories are used as volumes for the associated docker containers.

## Start Docker Containers
1. Open a terminal


2. Go to the project directory


3.     % cd docker


4.     % docker compose up -d  
   -d is used for starting in background  
   Containers are configured to be restarted automatically, so you don't have to perform this command again.


5. Following docker containers are started


    CONTAINER ID   IMAGE                                   COMMAND                  CREATED          STATUS          PORTS                                                                   NAMES
    93ff61652a48   mariadb:10.5.9                          "docker-entrypoint.s…"   29 seconds ago   Up 24 seconds   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp                               g2_mariadb
    1b827abf8180   wurstmeister/zookeeper:latest           "/bin/sh -c '/usr/sb…"   29 seconds ago   Up 25 seconds   22/tcp, 2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp, :::2181->2181/tcp   g2_zookeeper
    9f1f96085cdf   wurstmeister/kafka:2.13-2.6.0           "start-kafka.sh"         29 seconds ago   Up 22 seconds   0.0.0.0:29092->29092/tcp, :::29092->29092/tcp                           g2_kafka-0
    2387f301593d   wurstmeister/kafka:2.13-2.6.0           "start-kafka.sh"         29 seconds ago   Up 19 seconds   0.0.0.0:29093->29093/tcp, :::29093->29093/tcp                           g2_kafka-1
    3ff4d8068211   wurstmeister/kafka:2.13-2.6.0           "start-kafka.sh"         29 seconds ago   Up 18 seconds   0.0.0.0:29094->29094/tcp, :::29094->29094/tcp                           g2_kafka-2
    d28baec9b502   confluentinc/cp-schema-registry:6.0.2   "/etc/confluent/dock…"   29 seconds ago   Up 14 seconds   0.0.0.0:8081->8081/tcp, :::8081->8081/tcp                               g2_schema-registry
    e2f134d83083   quay.io/cloudhut/kowl:v1.3.1            "./kowl"                 29 seconds ago   Up 10 seconds   0.0.0.0:9000->8080/tcp, :::9000->8080/tcp                               g2_kowl

### MariaDB
#### MariaDB connection string

    jdbc:mariadb://localhost:3306/

#### MariaDB login

    username: root
    password: g2-prototype-pw

### Kafka
#### Kafka Broker

| Kafka-Broker | Docker container | Access from within docker | Access from host  |
| ------------ | ---------------- | ------------------------- | ----------------- |
| kafka-0      | g2_kafka-0       | kafka-0:9092              | localhost:29092   |
| kafka-1      | g2_kafka-1       | kafka-1:9092              | localhost:29093   |
| kafka-2      | g2_kafka-2       | kafka-2:9092              | localhost:29094   |

### Kowl
Access [Kowl GUI](http://localhost:9000/) via  

    http://localhost:9000/


## Stop Docker Containers
1. Open a terminal


2. Go to the project directory


3.     % cd docker


4.     % docker compose down -v  


## G2Application
### Kafka Topics
These kafka topics are automatically created on startup of G2Application:

| Topic name    | Partitions | Replication factor |
| ------------- | ---------: | -----------------: |
| g2-booking    | 4          | 3                  |

### Schema registry
These avro schemas are automatically registered on startup of G2Application:

| Subject          | Topic name |
| ---------------- | ---------- |
| g2-booking-value | g2-booking |
