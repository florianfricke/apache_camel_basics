package components;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class FTPComponent {
// FileComponent:
// https://camel.apache.org/components/latest/file-component.html

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                Endpoint fileDest = endpoint("file:src/data/destVZ");

                from("ftp://127.0.0.1/Test?username=user&password=12345")
                    .process(new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                                System.out.println(exchange.getMessage());
                                System.out.println(exchange.getIn().getBody(String.class));
                                System.out.println(exchange.getIn().getHeader("CamelFileName"));
                        }
                    })
                    .to(fileDest);
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
