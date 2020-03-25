package me.spike.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class GreetingRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("jms:queue:hello").routeId("hello")
                .log("In first route")
                .to("direct:destination");
    }
}
