//package server;
//
//import org.apache.commons.text.StringEscapeUtils;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///** Responds to get requests that have user and num as parameters:
// * for example: http://localhost:8080/rec?user=3&num=5
// * where user is the user id, and num is the number of movie recommendations to return.
// * Returns movie recommendations for the given user id presenting them in the html table.
// */
//public class RecommenderServlet extends HttpServlet {
//    // FILL IN CODE
//
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        //GET /echo
//        System.out.println("here");
//        response.setContentType("text/html");
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        PrintWriter out = response.getWriter();
//        out.println("<html><title>MovieServlet</title><body>");
//
//        out.println("<form action=\"rec\" method=\"post\">");
//        out.println("User ID<br/>");
//
//        // notice that the message will be stored in the parameter "usermsg"
//        out.println("<input type=\"text\" name=\"userID\"><br/>");
//        out.println("Number of recommendations<br/>");
//
//        // notice that the message will be stored in the parameter "usermsg"
//        out.println("<input type=\"text\" name=\"numRecs\"><br/>");
//
//        out.println("<input type=\"submit\" value=\"Enter\"></form>");
//
//        out.println("</body></html>");
//    }
//
//    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        //POST /echo
//        response.setContentType("text/html");
//        response.setStatus(HttpServletResponse.SC_OK);
//
//
//
//        String genre = request.getParameter("userchoice");
//        genre = StringEscapeUtils.escapeHtml4(genre); // clean up whatever the user typed
//
//        //out.println("<html><title>EchoServlet</title><body>Printer works!</body></html>");
//        System.out.println("sout works!");
//
//      String dir = "/Users/caseydzuong/IdeaProjects/lab5-cdzuong/input/smallSet";
//
//        int user = Integer.parseInt(request.getParameter("userchoice"));
//        int numberRecs = Integer.parseInt(request.getParameter("numRecs"));
//
//        RatingsCollection collection = new RatingsCollection();
//        collection.addRatings(dir);
//        collection.rValue(user);
//        collection.rankList(dir);
//        collection.makeStarMovieList(user, numberRecs, dir);
//
//
//
////        out.println("<html><title>EchoServlet</title><body>Movie Recommendation: " + rec + "</body></html>");
//
//    }
//}
