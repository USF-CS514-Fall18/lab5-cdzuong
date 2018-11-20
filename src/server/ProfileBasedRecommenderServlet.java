package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Math.sqrt;

public class ProfileBasedRecommenderServlet extends HttpServlet {
    private TreeMap<Integer, TreeMap<Integer, Double>> ratingsMap;
    private int userMax;
    private TreeMap<Double, TreeMap<Integer, Movie>> rankMovies;
    private TreeMap<Double, ArrayList<Movie>> movieMap;
    private ArrayList<Integer> highRUsers;
    private ArrayList<Movie> antiMovies;
    private ArrayList<Movie> highMovies;
    private ArrayList<Movie> compareMovies;
    private ArrayList<Movie> movieRecs;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //GET /echo
        System.out.println("here");
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        out.println("<html><title>MovieServlet</title><body>");

        out.println("<form action=\"rec\" method=\"post\">");
        out.println("User ID<br/>");

        // notice that the message will be stored in the parameter "usermsg"
        out.println("<input type=\"text\" name=\"userID\"><br/>");
        out.println("Number of recommendations<br/>");

        // notice that the message will be stored in the parameter "usermsg"
        out.println("<input type=\"text\" name=\"numRecs\"><br/>");

        out.println("<input type=\"submit\" value=\"Enter\"></form>");

        out.println("</body></html>");
    }

    /**
     * RatingsCollection constructor.
     */
    public ProfileBasedRecommenderServlet() {
        ratingsMap = new TreeMap<>();
        userMax = 1;
        rankMovies = new TreeMap<>();
        movieMap = new TreeMap<>();
        highRUsers = new ArrayList<>();
        antiMovies = new ArrayList<>();
        highMovies = new ArrayList<>();
        movieRecs = new ArrayList<>();
    }

    /**
     * Get userID for the user whose movie preference ratings match
     * the most with the user in question.
     *
     * @return
     */
    public int getUserMax() {
        return userMax;
    }

    /**
     * Returns the map containing userIDs, movie IDs, and ratings.
     *
     * @return TreeMap corresponding to userIDs, movie IDs, and ratings.
     */
    public Map getRatingsMap() {
        return ratingsMap;
    }

    /**
     * Adds ratings to the ratingsMap TreeMap. Assigns userIDs as keys
     * and a nested TreeMap with movieID as key and movie rating as a double.
     *
     * @param dir Directory in which the file can be found.
     */
    public void addRatings(String dir) {
        try {
            File file = new File(dir + "/ratings.csv");
            Scanner input = new Scanner(file);
            input.nextLine();
            int userId;
            int movieId;
            double rating;
            while (input.hasNextLine()) {
                String[] splitLine = input.nextLine().split(",");
                userId = Integer.parseInt(splitLine[0]);
                movieId = Integer.parseInt(splitLine[1]);
                rating = Double.parseDouble(splitLine[2]);
                if (!ratingsMap.containsKey(userId)) {
                    ratingsMap.put(userId, new TreeMap<>());
                }
                Map<Integer, Double> ratingsSub;
                ratingsSub = ratingsMap.get(userId);
                ratingsSub.put(movieId, rating);


            }
        } catch (FileNotFoundException e) {
            System.out.println("Ratings file not found." + dir + "/ratings.csv");
        }
    }

    /**
     * Calculates the Pearson correlation coefficient between a given
     * user and all users in the database.
     *
     * @param compare the userID of the user in question.
     * @return The user whose movie preferences match most with the user
     * in question.
     */
    public int rValue(int compare) {

        double xratingUser = 0.0;
        double yratingOthers = 0.0;
        double sumProducts = 0;
        double sumUser = 0;
        double sumOther = 0;
        double sumUserSq = 0;
        double sumOtherSq = 0;
        double numerator;
        double den1 = 0;
        double den2 = 0;
        double denominator = 0;
        double rValue = 0;
        double rMax = -1;

        int count = 0;

        for (Integer userID : ratingsMap.keySet()) {
            if (userID != compare) {
                for (Integer movieID : ratingsMap.get(userID).keySet()) {
                    for (Integer movieID2 : ratingsMap.get(compare).keySet()) {
                        if (movieID.equals(movieID2)) {
                            xratingUser = ratingsMap.get(compare).get(movieID2);
                            yratingOthers = ratingsMap.get(userID).get(movieID);
                            sumProducts += (xratingUser * yratingOthers);
                            sumUser += xratingUser;
                            sumOther += yratingOthers;
                            sumUserSq += Math.pow(xratingUser, 2);
                            sumOtherSq += Math.pow(yratingOthers, 2);
                            count++;
                        }
                    }
                }

                numerator = (count * sumProducts) - (sumUser * sumOther);
                den1 = (count * sumUserSq) - Math.pow(sumUser, 2);
                den2 = (count * sumOtherSq) - Math.pow(sumOther, 2);
                denominator = sqrt(den1) * sqrt(den2);
                rValue = numerator / denominator;

                if (rValue > rMax) {
                    rMax = rValue;
                    this.userMax = userID;
                }

                if (rValue > 0.85) {
                    highRUsers.add(userID);
                }

                sumProducts = 0;
                sumUser = 0;
                sumOther = 0;
                sumUserSq = 0;
                sumOtherSq = 0;
                count = 0;

            }
        }

        return userMax;
    }

    /**
     * Creates a TreeMap containing the movie ratings and movie information
     * of the user whose movie preferences match most with those of the user
     * in question
     *
     * @param dir
     */
    public void rankList(String dir) {
        double rating;
        MovieCollection movieColl = new MovieCollection();
        TreeMap<Integer, Double> userRatingMap = ratingsMap.get(userMax);

        movieColl.addMovie(dir + "/movies.csv");

        for (Integer movieIdRatings : userRatingMap.keySet()) {
            rating = userRatingMap.get(movieIdRatings);
            if (!rankMovies.containsKey(rating)) {
                rankMovies.put(rating, new TreeMap<>());
            } else {
                for (Integer movieIdMovieColl : movieColl.getMap().keySet()) {

                    if (movieIdRatings != movieIdMovieColl) {
                        Map<Integer, Movie> movieInner;
                        movieInner = rankMovies.get(rating);
                        movieInner.put(movieIdRatings, movieColl.getMap().get(movieIdRatings));
                    }

                }

            }


        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //POST /echo
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

//
//        String genre = request.getParameter("userchoice");
//        genre = StringEscapeUtils.escapeHtml4(genre); // clean up whatever the user typed

        //out.println("<html><title>EchoServlet</title><body>Printer works!</body></html>");
        System.out.println("sout works!");

        String dir = "/Users/caseydzuong/IdeaProjects/lab5-cdzuong/input/smallSet";

        int user = Integer.parseInt(request.getParameter("userID"));
        int numberRecs = Integer.parseInt(request.getParameter("numRecs"));


        addRatings(dir);
        rValue(user);
        rankList(dir);
        makeStarMovieList(user, dir);
//
        PrintWriter out = response.getWriter();

        out.println("<html><body><title><h1>Movie Recommendations:</h1></title><table border: 1px solid #e2e2e2");
        for (int i = 0; i < numberRecs; i++) {
            out.println("<tr><td>");
            out.println(movieRecs.get(i).getTitle() + "</td><td>" + movieRecs.get(i).getYear() + "<br>");
            out.println("</td></tr>");
        }

        out.println("</body></html>");
    }

    /**
     * Creates movie recommendations and antirecommendations based on
     * the users whose movie preferences match most with the user in question
     *
     * @param compare userID for the user recommendations are being made for
     * @param dir     directory in which the movie list is found
     */
    public void makeStarMovieList(int compare, String dir) {
        TreeMap<Integer, Double> compareMap = ratingsMap.get(compare);
        for (Double movieIdRatings : rankMovies.keySet()) {
            if (!movieMap.containsKey(movieIdRatings)) {
                movieMap.put(movieIdRatings, new ArrayList<>());
            }
            ArrayList<Movie> movieList;
            movieList = movieMap.get(movieIdRatings);
            for (Integer movieID : rankMovies.get(movieIdRatings).keySet()) {
                for (Integer movieIDCompare : compareMap.keySet()) {
                    if (!movieIDCompare.equals(movieID)) {

                        movieList.add(rankMovies.get(movieIdRatings).get(movieID));
                        break;
                    }
                }
            }
        }

        for (Double rating : movieMap.keySet()) {
            movieMap.get(rating).sort(new MovieYearComparator());
        }

        MovieCollection movieColl = new MovieCollection();
        movieColl.addMovie(dir + "/movies.csv");
        String rec;

        movieRecs.addAll(movieMap.get(5.0));
        movieRecs.addAll(movieMap.get(4.0));
        movieRecs.addAll(movieMap.get(3.0));


//        try (PrintWriter antiPrint = new PrintWriter(new File(write + "antirecs.csv"))) {
//            System.out.println();
//            for (int i = 0; i < highRUsers.size(); i++) {
//                for (Integer movieID : ratingsMap.get(highRUsers.get(i)).keySet()) {
//                    if (ratingsMap.get(highRUsers.get(i)).get(movieID) <= 1.5) {
//                        antiMovies.add(movieColl.getMap().get(movieID));
//                    }
//                }
//            }
//
//            antiMovies.sort(new MovieYearComparator());
//            String antirec;
//            for (int i = 0; i < n; i++) {
//                antirec = antiMovies.get(i).getTitle() + " (" + antiMovies.get(i).getYear() + ")";
//                antiPrint.println(antirec);
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Cannot write anti-recommendations.");
//        }


    }


}