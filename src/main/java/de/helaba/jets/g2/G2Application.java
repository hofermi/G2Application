package de.helaba.jets.g2;

import de.helaba.jets.g2.kafka.avro.model.G2BookingAvroRecord;
import de.helaba.jets.g2.kafka.event.G2BookingPayload;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class G2Application {

    public static void main(String[] args) {
        SpringApplication.run(G2Application.class, args);
    }

}
