# Lab 5: Web-based Movie Recommender using Jetty/Servlets.

Due: November 19, 11:59pm

The goal of this lab is to turn your movie recommender into a simple web-based application. 
This lab builds on top of lab 3, and is an important first step towards the final project. 
You will learn:
- How to use Jetty and servlets to create a web application
- HTTP get and post requests, and parameters of HTTP request
- HTML forms and how to process them using servlets

Start by clicking on the link with the starter code on Canvas (under Assignments/lab5). The pdf description of the lab is also
under Assignments/lab5 on Canvas.

The starter code contains:
- input folder that contains the smallSet subfolder that contains movies.csv and ratings.csv.
- lib folder that contains all the libraries required for the project. 
- src folder contains two folders that correspond to two packages: server and recommender. The server package contains classes
MovieServer, RecommenderServlet and ProfileBasedRecommenderServlet. Fill in code in classes RecommenderServlet and ProfileBasedRecommenderServlet per instructions in lab5.pdf.
The recommender package should contain classes from lab 3 (Movie, MovieRecommender etc.).

## Requirements
You need to write two separate servlets: RecommenderServlet and ProfileBasedRecommenderServlet.
- Recommender Servlet

This servlet is mapped to the "/rec" path (see MovieServer code). 
When this servlet receives a get request that contains the user id and the number of recommendations as parameters (for example: http://localhost:8080/rec?user=3&num=5), it returns an
html page that contains a table with movie recommendations for the given user. You are expected to use lab 3 algorithm for generating
recommendations. The table should display title and year of each recommended movie.

- ProfileBasedRecommenderServlet

This servlet is mapped to the "/profile" path (see MovieServer code). When this servlet receives a get request, it needs to display
an page that asks the user to create a profile by rating some movies, and lists the first N movies from the dataset and a textfield next to each of them, that allows the user to enter the rating. 
It should also display the submit button for submitting the ratings.
Since entering the rating of each movie is time consuming, your servlet should initialize each rating randomly in the range from 0.5 to 5.0, and the user
can change these ratings as needed. Once the user submits the ratings by clicking the button, the form should be processed by the POST request of the same servlet,
and display the table with movie recommendations for this user based on the ratings they entered. The table should display title and year of each recommended movie.
Change the code of lab 3 so that it allows to add a new user  and their ratings to your data structure that stores all ratings. 
Then you can use the same algorithm as in lab 3 to recommend movies to the user.
lab5 pdf shows screenshots of the browser as a user interacts with the web application.

## Policies

I expect to see at least 5 meaningful commits (not counting my commits) for this lab, made over the course of at least 3 days.

Late submissions are not accepted.

The lab must be completed individually, no collaboration is allowed.

You should be able to complete the lab without using the web. Instead, please use lecture notes, examples provided by the instructor, as well as instructor's and TA's office hours.

Each person might be asked to come in for a code review of the lab. Not being able to answer questions about the code or change it per requirements of the instructor, will result in a 0 for the assignment.

The lab is graded based on the functionality and design. Also, your code must adhere to style guidelines posted on Canvas.








