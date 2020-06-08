package eip;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class MessageFilter {

    public static class FileProcessor implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            System.out.println("_________________________");
            System.out.println("File erfüllt die Bed: " + exchange.getIn().getHeader("CamelFilename"));
            System.out.println(exchange.getIn().getBody(String.class));
        }
    }

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                Processor processor = new FileProcessor();
                from("file:src/data/?noop=true")
                    .filter(jsonpath("$.test_file").isGreaterThan("5"))
                    .process(processor);

                from("file:src/data/xml/?noop=true")
                    .filter(xpath("/order[not(@test)]")) // <order test="false"> --> test-Attr darf nicht vorhanden sein
                    .filter(xpath("/order[@name = 'motor']"))
                    .filter(xpath("/order/prioritaet = '1'"))
                    .process(processor);
                // Ausgabe:
                // <order name="motor" amount="1" customer="honda">
                //    <article>Motor Benzin</article>
                //    <prioritaet>1</prioritaet>
                // </order>
            }
        });

        context.start();
        Thread.sleep(5000);
        context.stop();
    }



}
