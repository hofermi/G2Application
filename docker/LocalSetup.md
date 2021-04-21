# Local Setup

## Create Directories
Create the following directories:

    ~/docker/volumes/mariadb
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


    CONTAINER ID   IMAGE                                 COMMAND                  CREATED         STATUS         PORTS                                                  NAMES
    65755f97155b   mariadb:10.5.9                        "docker-entrypoint.s…"   9 minutes ago   Up 9 minutes   0.0.0.0:3306->3306/tcp                                 docker_mariadb_1
    a60b3313f136   bitnami/zookeeper:3                   "/opt/bitnami/script…"   9 minutes ago   Up 9 minutes   2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp, 8080/tcp   docker_zookeeper_1
    b079fb65c58c   bitnami/kafka:2                       "/opt/bitnami/script…"   9 minutes ago   Up 8 minutes   0.0.0.0:9092->9092/tcp                                 docker_kafka-0_1
    f88eac9a8274   bitnami/kafka:2                       "/opt/bitnami/script…"   9 minutes ago   Up 8 minutes   9092/tcp, 0.0.0.0:9093->9093/tcp                       docker_kafka-1_1
    95bc1e8403d2   bitnami/kafka:2                       "/opt/bitnami/script…"   9 minutes ago   Up 8 minutes   9092/tcp, 0.0.0.0:9094->9094/tcp                       docker_kafka-2_1
    88adb314a201   obsidiandynamics/kafdrop:latest       "/kafdrop.sh"            9 minutes ago   Up 8 minutes   0.0.0.0:9000->9000/tcp                                 docker_kafdrop_1

### MariaDB
#### MariaDB connection string

    jdbc:mariadb://localhost:3306/

#### MariaDB login

    username: root
    password: g2-prototype-pw

### Kafka
There are three Kafka-Broker.

| Kafka-Broker | Docker container | Port | Access URL (via hostname) |
| ------------ | ---------------- | ---- | ------------------------- |
| kafka-0      | docker_kafka-0_1 | 9092 | kafka-0:29092             |
| kafka-1      | docker_kafka-1_1 | 9093 | kafka-1:29092             |
| kafka-2      | docker_kafka-2_1 | 9094 | kafka-2:29092             |

### Kafdrop
Access [Kafdrop GUI](http://localhost:9000/) via  

    http://localhost:9000/

### Kafka Topics
On first startup, topics must be created:
1. Open a terminal


    % docker exec -it docker_kafka-0_1 /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server kafka-0:29092,kafka-1:29092,kafka-2:29092 --create --partitions 4 --replication-factor 2 --topic g2-booking

#### Topics Overview
| Topic name    | Partitions | Replication factor |
| ------------- | ---------: | -----------------: |
| g2-booking    | 4          | 2                  |
