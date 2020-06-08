import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ReadJsonContent {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:src/data?noop=true")
                        .setProperty("myProperty", constant("test"))
                        .setProperty("plz", jsonpath("$.adresse.plz"))
                        .process((exchange) -> {
                            System.out.println("JSON File erhalten: " + exchange.getIn().getHeader("CamelFileName"));
                            System.out.println("PLZ: " + exchange.getProperty(  "plz"));
                    });

            }
        });

        context.start();
        Thread.sleep(5000);
        context.stop();
    }
}
