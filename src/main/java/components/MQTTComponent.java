package components;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class MQTTComponent {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("paho:flfr?brokerUrl=tcp://test.mosquitto.org:1883")
                    .process(exchange -> System.out.println(exchange.getIn().getBody(String.class)))
                    .to("stream:out");
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
