package me.spike.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class PeriodicGreetingRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("timer://simpleTimer?period=1000")
                .setBody(simple("Hello from timer at ${header.firedTime}"))
                .log("Timer triggered")
                .to("jms:queue:hello-audit");
    }
}
