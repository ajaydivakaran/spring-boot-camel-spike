package me.spike.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class GreetingRoute extends RouteBuilder {

    public void configure() {
        from("jms:queue:hello").id("hello")
                .to("jms:queue:hello-audit").id("hello-audit");
    }
}
