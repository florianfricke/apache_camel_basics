package eip;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ContentBasedRouter {
    public static void main(String[] args) throws Exception {
        CamelContext ct = new DefaultCamelContext();

        ct.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:src/data?noop=true")
                .choice()
                        .when(header("CamelFileName").endsWith(".json"))
                            .to("file:src/data/destVZ/json")
                        .when(header("CamelFileName").endsWith(".txt"))
                            .to("file:src/data/destVZ/txt")
                        // 2. Möglichkeit über Regex
                        .when(header("CamelFileName").regex("^.*(csv|csl)$"))
                            .to("file:src/data/destVZ/csv")
                        // check if file starts with { and ends with } --> valid json
                        .when(body().regex("^\\{.*\\}$"))
                            .to("file:src/data/destVZ/json")
                        .otherwise()
                            .to("file:src/data/destVZ/other")
                        //beenden des choice möglich um weiter Operationen durchzuführen
                .endChoice();
            }
        });
        ct.start();
        Thread.sleep(5000);
        ct.stop();
    }
}
