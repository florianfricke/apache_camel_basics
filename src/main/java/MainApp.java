import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

// FileComponent:
// https://camel.apache.org/components/latest/file-component.html
public class MainApp {

    public class MyFirstProcessorFileHandling implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            System.out.println(exchange.getIn().getHeader("CamelFileName")); // Dateiname
            System.out.println(exchange.getIn().getBody(String.class)); // Inhalt der Datei
        }
    }

    public void simpleFileRoute(CamelContext context) throws Exception {
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                Endpoint fileDest = endpoint("file:src/data/destVZ");

                // Auswahl eines einzelnen Files
                from("file:src/data/orders?noop=true&fileName=message1.xml")
                        .to(fileDest);

                //alle Dateien eines VZ
                from("file://src/data?noop=true") // option: noop, tells Camel to leave the source file as is, false: file would be moved
                        .log("${body}")
                        .process(exchange -> System.out.println(exchange.getIn().getBody(String.class)))
                        .process(new MyFirstProcessorFileHandling())
                        .to(fileDest);

                // String Vars in Route
                String vzXMLIn = "src/data/xml2/";
                String vzXMLOut = "src/data/xmlDest/";
                fromF("file:%s?noop=true", vzXMLIn)
                        .toF("file:%s?noop=true", vzXMLOut);

                // Multiple Destinations
                vzXMLIn = "src/data/xml/";
                from(String.format("file:%s?noop=true", vzXMLIn))
                        .process(new MyFirstProcessorFileHandling())
                        .multicast()
                        .stopOnException() //handle the exception coming back from this route,
                        .parallelProcessing() //parallel processing -> default thread pool size of 10
                        .to("file:src/data/destVZ","file:src/data/destVZ2");
            }
        });
    }

    public static void main(String[] args) throws Exception {
        MainApp m = new MainApp();
        CamelContext context = new DefaultCamelContext();
        m.simpleFileRoute(context);
//        context.addRoutes(new MyRouteBuilder());
        context.start();
        Thread.sleep(10000); //allow Camel application time to copy the files
        context.stop();
    }
}
