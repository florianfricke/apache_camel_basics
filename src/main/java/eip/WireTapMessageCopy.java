package eip;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class WireTapMessageCopy {
    // Creates a copy of the message to destination
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:src/data?noop=true")
                        .wireTap("file:src/data/messageCopy") //Nachricht wird an den Pfad kopiert, nach Fire&Forget, keine Bestätigung ob Nachricht erfolgreich kopiert wurde
                        .to("file:src/data/destVZ"); //Orginal wird hier gespeichert
            }
        });

        context.start();
        Thread.sleep(8000);
        context.stop();
    }
}
