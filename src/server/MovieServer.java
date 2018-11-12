package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class MovieServer {
    public static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        RatingsCollection collection = new RatingsCollection();
        collection.addRatings(args[0]);
        collection.rValue(Integer.parseInt(args[2]));
        collection.rankList(args[0]);
        collection.makeStarMovieList(Integer.parseInt(args[2]), Integer.parseInt(args[3]), args[0], args[1]);

        Server server = new Server(PORT);

        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(RecommenderServlet.class, "/rec");
        handler.addServletWithMapping(ProfileBasedRecommenderServlet.class, "/profile");

        server.setHandler(handler);

        server.start();
        server.join();
    }
}
