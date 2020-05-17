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
import java.util.Scanner;

public class Main {

    public static ArrayList<Question> listOfQuestions = new ArrayList<>();

    public static void main(String[] args) throws IOException, ParseException {
        int numberOfQuestions;
        int category;
        String difficulty;
        String questionType = "multiple";

        URL url = new URL("https://opentdb.com/api.php?amount=10&category=15&difficulty=easy&type=multiple");
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
        JSONObject jobj = (JSONObject) parse.parse(inline.toString());
        JSONArray jsonarr = (JSONArray) jobj.get("results");

        for (Object o : jsonarr) {
            JSONObject jsonobj = (JSONObject) o;
            String question = (String) jsonobj.get("question");
            String correctAnswer = (String) jsonobj.get("correct_answer");

            ArrayList<String> allAnswers = new ArrayList<>();
            for (Object i : (JSONArray) jsonobj.get("incorrect_answers"))
                allAnswers.add((String) i);

            allAnswers.add(correctAnswer);
            Collections.shuffle(allAnswers);
            Question temp = new Question(question, correctAnswer, allAnswers);
            listOfQuestions.add(temp);
        }

        /////////////////Game///////////////////
        int score = 0;
        Scanner scan = new Scanner(System.in);
        for (Question q : listOfQuestions) {
            System.out.println(q);
            System.out.print("Pick an option: ");
            char userInput = scan.next().charAt(0);

            int selection = -1;
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

            if (q.getCorrectAnswer().equals(q.getAnswers().get(selection))) {
                score++;
                System.out.println("Correct!");
            } else {
                System.out.println("Incorrect, '" + q.getCorrectAnswer() + "' is the correct answer.");
            }
            System.out.println();
        }

        System.out.println("Score: " + score + "/" + listOfQuestions.size());

    }
}
