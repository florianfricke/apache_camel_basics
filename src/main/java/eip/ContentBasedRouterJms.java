package eip;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class ContentBasedRouterJms {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:src/data?noop=true").to("jms:incomingFile");
                from("jms:incomingFile").choice()
                        .when(header("CamelFileName").endsWith(".json"))
                            .to("jms:jsonFiles")
                        .when(header("CamelFileName").endsWith(".txt"))
                            .to("jms:txtFiles");
                from("jms:jsonFiles").process((exchange) -> {
                    System.out.println("JSON File erhalten: " + exchange.getIn().getHeader("CamelFileName"));
                });
                from("jms:txtFiles").process((exchange) -> {
                    System.out.println("TXT File erhalten: " + exchange.getIn().getHeader("CamelFileName"));
                });
            }
        });

        context.start();
        Thread.sleep(8000);
        context.stop();
    }

}
