package me.spike;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
public class Main {

    public Main(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    private ProducerTemplate producerTemplate;

    @Value("${topic.destination}")
    private String destinationRoute;

    @PostMapping(value = "/hello", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void hello(@RequestBody Map<String, String> message) {
        producerTemplate.sendBody(destinationRoute, message);
    }
}
