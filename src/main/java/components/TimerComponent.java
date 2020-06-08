package components;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class TimerComponent {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                // the set up a route that generates an event every 60 seconds
                from("timer://testTimer?fixedRate=true&period=3s")
                        .setBody()
                        .simple("This is a test message")
                        .to("stream:out");
//                        .process(exchange -> System.out.println(exchange.getIn().getBody(String.class)));
            }
        });
        context.start();
        Thread.sleep(15000);
        context.stop();
    }
}
