package me.spike;

import org.apache.camel.ProducerTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/hello")
    public String hello() {
        producerTemplate.sendBody("jms:queue:hello", "Hello World");
        return "hello";
    }
}
