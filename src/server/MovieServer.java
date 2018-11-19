package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class MovieServer {
    public static final int PORT = 8080;

    public static void main(String[] args) throws Exception {

        Server server = new Server(PORT);

        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(RatingsCollection.class, "/rec");
        handler.addServletWithMapping(ProfileBasedRecommenderServlet.class, "/profile");

        server.setHandler(handler);

        server.start();
        server.join();
    }
}
