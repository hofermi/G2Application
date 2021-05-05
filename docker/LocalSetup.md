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


    CONTAINER ID   IMAGE                                   COMMAND                  CREATED         STATUS                   PORTS                                        NAMES
    16833e1f3648   mariadb:10.5.9                          "docker-entrypoint.s…"   2 minutes ago   Up 2 minutes             0.0.0.0:3306->3306/tcp                       g2_mariadb
    f89e102ae3e8   confluentinc/cp-zookeeper:6.0.2         "/etc/confluent/dock…"   2 minutes ago   Up 2 minutes (healthy)   2888/tcp, 0.0.0.0:2181->2181/tcp, 3888/tcp   g2_zookeeper
    28f790911692   bitnami/kafka:2.8.0                     "/opt/bitnami/script…"   2 minutes ago   Up About a minute        0.0.0.0:9092->9092/tcp                       g2_kafka-0
    38bb75b89756   bitnami/kafka:2.8.0                     "/opt/bitnami/script…"   2 minutes ago   Up About a minute        9092/tcp, 0.0.0.0:9093->9093/tcp             g2_kafka-1
    a681d5abdae5   bitnami/kafka:2.8.0                     "/opt/bitnami/script…"   2 minutes ago   Up About a minute        9092/tcp, 0.0.0.0:9094->9094/tcp             g2_kafka-2
    caf36caa3185   confluentinc/cp-schema-registry:6.0.2   "/etc/confluent/dock…"   2 minutes ago   Up About a minute        0.0.0.0:8081->8081/tcp                       g2_schema-registry
    6d0e949e842f   quay.io/cloudhut/kowl:v1.3.1            "./kowl"                 2 minutes ago   Up About a minute        0.0.0.0:9000->8080/tcp                       g2_kowl

### MariaDB
#### MariaDB connection string

    jdbc:mariadb://localhost:3306/

#### MariaDB login

    username: root
    password: g2-prototype-pw

### Kafka
#### Kafka Broker

| Kafka-Broker | Docker container | Access from within docker | Access from host |
| ------------ | ---------------- | ------------------------- | ---------------- |
| kafka-0      | g2_kafka-0       | kafka-0:29092             | localhost:9092   |
| kafka-1      | g2_kafka-1       | kafka-1:29092             | localhost:9093   |
| kafka-2      | g2_kafka-2       | kafka-2:29092             | localhost:9094   |

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
