package me.spike.route;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DestinationRoute extends RouteBuilder {

    @Override
    public void configure() {
        onException(RuntimeException.class)
                .maximumRedeliveries(2)
                .log("In exception handler")
                .to("jms:queue:destination.dlq");

        from("direct:destination").routeId("destination")
                .log("Received message at destination")
                .process(exchange -> {
                    final Message message = exchange.getIn();
                    final Map body = message.getBody(Map.class);
                    final Boolean shouldModifyRequest =
                            exchange.getProperty("modifyRequest", false, Boolean.class);
                    if ("fail".equals(body.get("action")) && !shouldModifyRequest) {
                        throw new RuntimeException("bad action");
                    }

                    if ("unknown".equals(body.get("action"))) {
                        throw new RuntimeException("unknown action");
                    }
                })
                .to("blah blah")
                .errorHandler(defaultErrorHandler().onRedelivery(exchange -> {
                    RuntimeException e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                    if("bad action".equals(e.getMessage())) {
                        exchange.setProperty("modifyRequest", true);
                    }
                }))
                .log("Message processing completed at destination");

    }
}
