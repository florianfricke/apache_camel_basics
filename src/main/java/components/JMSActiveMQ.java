package components;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

// 1. Schritt
// C:\apache-activemq-5.15.12\bin\win64 -> activemq.bat starten

// 2. Schritt Weboberfläche von ActiveMQ öffnen
// http://localhost:8161/index.html
// klick auf: Manage ActiveMQ broker -> admin:admin (user:pw)
// klick im Menü auf Queue

public class JMSActiveMQ {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:src/data?noop=true&fileName=message-koeln.json").to("jms:incomingFile");
                from("jms:incomingFile").process((exchange) -> {
                    System.out.println("JSON File erhalten: " + exchange.getIn().getHeader("CamelFileName"));
                });
            }
        });

        context.start();
        Thread.sleep(5000);
        context.stop();
    }
}
