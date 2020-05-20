package com.paolocosentino;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        /////////////Connection///////////////
        URL url = new URL(runPrompt());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        StringBuilder inline = new StringBuilder();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200)
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        else {
            Scanner sc = new Scanner(url.openStream());

            while (sc.hasNext())
                inline.append(sc.nextLine());

            sc.close();
        }

        JSONParser parse = new JSONParser();
        JSONObject jObj = (JSONObject) parse.parse(inline.toString());
        JSONArray jsonArr = (JSONArray) jObj.get("results");
        List<Question> listOfQuestions = new ArrayList<>();
        List<String> trueAndFalseAnswers = new ArrayList<>() {{
            add("True");
            add("False");
        }};

        for (Object o : jsonArr) {
            JSONObject jsonObj = (JSONObject) o;
            String question = (String) jsonObj.get("question");
            String correctAnswer = (String) jsonObj.get("correct_answer");
            String questionType = (String) jsonObj.get("type");
            Question temp;

            if (questionType.equalsIgnoreCase("multiple")) {
                List<String> allAnswers = new ArrayList<>();

                for (Object i : (JSONArray) jsonObj.get("incorrect_answers"))
                    allAnswers.add((String) i);

                allAnswers.add(correctAnswer);
                Collections.shuffle(allAnswers);
                temp = new Question(question, questionType, correctAnswer, allAnswers);
            } else
                temp = new Question(question, questionType, correctAnswer, trueAndFalseAnswers);

            listOfQuestions.add(temp);
        }

        /////////////////Game///////////////////
        int score = 0;
        int count = 1;
        Scanner scan = new Scanner(System.in);

        for (Question q : listOfQuestions) {
            System.out.println(count + ". " + q);
            System.out.print("Pick an option: ");
            char userInput = scan.next().charAt(0);
            int selection = -1;

            if (q.getQuestionType().equalsIgnoreCase("multiple")) {
                while (userInput != 'a' && userInput != 'b' && userInput != 'c' && userInput != 'd') {
                    System.out.print("Invalid entry, try again: ");
                    userInput = scan.next().charAt(0);
                }
                switch (userInput) {
                    case 'a' -> selection = 0;
                    case 'b' -> selection = 1;
                    case 'c' -> selection = 2;
                    case 'd' -> selection = 3;
                }
            } else {
                while (userInput != 'a' && userInput != 'b') {
                    System.out.print("Invalid entry, try again: ");
                    userInput = scan.next().charAt(0);
                }

                switch (userInput) {
                    case 'a' -> selection = 0;
                    case 'b' -> selection = 1;
                }
            }

            if (q.getCorrectAnswer().equals(q.getAnswers().get(selection))) {
                score++;
                System.out.println("Correct!");
            } else
                System.out.println("Incorrect, '" + q.getCorrectAnswer() + "' is the correct answer.");

            count++;
            System.out.println();
        }
        System.out.println("Score: " + score + "/" + listOfQuestions.size());
    }

    public static String runPrompt() {
        Scanner scan = new Scanner(System.in);
        StringBuilder str = new StringBuilder();
        str.append("https://opentdb.com/api.php?");

        System.out.print("Number of Questions (1-50): ");
        int numOfQs = scan.nextInt();

        while (numOfQs < 1 || numOfQs > 50) {
            System.out.print("Number of Questions (1-50): ");
            numOfQs = scan.nextInt();
        }

        str.append("amount=").append(numOfQs);

        System.out.print("Category of Questions (0=Any, 9-32): ");
        int categoryOfQs = scan.nextInt();

        while (categoryOfQs != 0 && categoryOfQs < 9 || categoryOfQs > 32) {
            System.out.print("Category of Questions (0=Any, 9-32): ");
            categoryOfQs = scan.nextInt();
        }

        if (categoryOfQs != 0) {
            str.append("&category=").append(categoryOfQs);
        }

        System.out.print("Difficulty of Questions (0=Any, 1=Easy, 2=Medium, 3=Hard): ");
        int difficultyOfQs = scan.nextInt();

        while (difficultyOfQs < 0 || difficultyOfQs > 3) {
            System.out.print("Difficulty of Questions (0=Any, 1=Easy, 2=Medium, 3=Hard): ");
            difficultyOfQs = scan.nextInt();
        }

        if (categoryOfQs != 0) {
            String difficulty = switch (difficultyOfQs) {
                case 1 -> "easy";
                case 2 -> "medium";
                case 3 -> "hard";
                default -> "";
            };
            str.append("&difficulty=").append(difficulty);
        }

        System.out.print("Type of Questions (0=Any, 1=Multiple Choice, 2=True/False): ");
        int typeOfQuestions = scan.nextInt();

        while (typeOfQuestions < 0 || typeOfQuestions > 2) {
            System.out.print("Type of Questions(0=Any, 1=Multiple Choice, 2=True/False): ");
            typeOfQuestions = scan.nextInt();
        }

        if (typeOfQuestions != 0) {
            String type = switch (typeOfQuestions) {
                case 1 -> "multiple";
                case 2 -> "boolean";
                default -> "";
            };
            str.append("&type=").append(type);
        }

        System.out.println(str.toString());
        return str.toString();
    }
}
