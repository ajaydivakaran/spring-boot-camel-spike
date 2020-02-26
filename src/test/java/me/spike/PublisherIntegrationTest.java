package me.spike;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(CamelSpringBootRunner.class)
@UseAdviceWith
@MockEndpoints("jms:queue:hello")
public class PublisherIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:jms:queue:hello")
    private MockEndpoint mockEndpoint;

    @Test
    public void shouldPublishToAuditRoute() throws InterruptedException {
        final String url = "http://localhost:" + port + "/hello";
        final Map<String, String> body = Collections.singletonMap("foo", "bar");
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedBodiesReceived("{foo=bar}");
        camelContext.start();

        final ResponseEntity<Void> response = this.restTemplate.postForEntity(url, body, Void.class);

        camelContext.stop();
        assertEquals(response.getStatusCodeValue(), 200);

        mockEndpoint.assertIsSatisfied();

    }

}
