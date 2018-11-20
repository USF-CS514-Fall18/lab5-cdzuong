package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Ratings {

    private int userId;
    private int movieId;
    private double rating;

    public Ratings(){}

    public Ratings(int userId, int movieId, double rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public Ratings(String filename) {
        try {
            File file = new File(filename);
            Scanner input = new Scanner(file);
            input.nextLine();
            RecommenderServlet addRating = new RecommenderServlet();
            while (input.hasNextLine()) {
                String[] splitLine = input.nextLine().split(",");
                userId = Integer.parseInt(splitLine[0]);
                movieId = Integer.parseInt(splitLine[1]);
                rating = Double.parseDouble(splitLine[2]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }


    }

    public int getUserId() {return userId;}

    public int getMovieId() {return movieId;}

    public double getRating() {return rating;}


}
