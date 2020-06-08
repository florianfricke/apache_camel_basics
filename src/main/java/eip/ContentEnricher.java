package eip;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

// Hinzufügen eines Headers zur Nachricht

public class ContentEnricher {
    public static void main(String[] args) throws Exception {
        CamelContext contenxt = new DefaultCamelContext();

        contenxt.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:src/data?noop=true")
                        .enrich("direct:geocoder", (oldExchange, newExchange) -> {
                            oldExchange.getIn().setHeader("geo", newExchange.getIn().getBody());
                            return oldExchange;
                        })
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                System.out.println(exchange.getIn().getHeader("geo"));
                                System.out.println(exchange.getIn().getBody());
                            }
                        });

                // direct Endpoint erlaubt es in dem gleichen Camelcontext eine weiter Route anzusprechen
                // mit Zusatzinformationen anreichern
                from("direct:geocoder")
                        .setProperty("nrFromJson", jsonpath("$.nr"))
                        .setBody(constant("8.0000046 78.0004457"))
                        .setBody(body().regexReplaceAll(" ",","));
                // Jetty Komponente um http Abfragen zu machen
            }
        });

        contenxt.start();
        Thread.sleep(8000);
        contenxt.stop();
    }
}
