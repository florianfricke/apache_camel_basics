import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class TxtFileToCsv {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:src/data/txt/?noop=true")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                System.out.println(exchange.getIn().getBody(String.class));
                                exchange.getIn().setBody("Vorname,Name,Alter\n" +
                                        "Florian,Fricke,24");
                                System.out.println(exchange.getIn().getBody(String.class));
                            }
                        })
                        .setHeader("NewFileName", header("CamelFileName").regexReplaceAll(".txt", ".csv"))
//                        .marshal(new CsvDataFormat())
                        .toF("file:src/data/destVZ/?fileName=%s", "${headers.NewFileName}");
            }
        });

        context.start();
        Thread.sleep(5000);
        context.stop();
    }
}
