package com.paolocosentino;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class IntroGUI extends JFrame {
    private JComboBox categoryCB;
    private JComboBox difficultyCB;
    private JComboBox typeCB;
    private JComboBox numCB;
    private JPanel mainPanel;
    private JSplitPane buttonSplitPane;
    private JButton cancelButton;
    private JButton okayButton;
    private JTextPane errorPane;

    public IntroGUI() {
        super("Getting Started");
        this.setSize(300, 300);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);

        /* Set all defaults default to null */
        categoryCB.setSelectedIndex(-1);
        difficultyCB.setSelectedIndex(-1);
        typeCB.setSelectedIndex(-1);
        numCB.setSelectedIndex(-1);

        /* Error Pane */
        errorPane.setVisible(false);
        errorPane.setBackground(null);

        /* Button Split Pane Settings */
        buttonSplitPane.setDividerSize(1);
        buttonSplitPane.setContinuousLayout(true);
        buttonSplitPane.setBorder(null);

        /* Button Functionality */
        cancelButton.addActionListener(ActionListener -> System.exit(0));
        okayButton.addActionListener(ActionListener -> {
            try {
                String numberOfQuestions = Objects.requireNonNull(numCB.getSelectedItem()).toString();
                int categoryOfQuestions = categoryCB.getSelectedIndex();
//                System.out.println(categoryOfQuestions);
                int difficultyOfQuestions = difficultyCB.getSelectedIndex();
//                System.out.println(difficultyOfQuestions);
                int typeOfQuestions = typeCB.getSelectedIndex();
//                System.out.println(typeOfQuestions);
                dispose();

                /* GameGUI Creation */
                URL url = new URL(createURL(numberOfQuestions, categoryOfQuestions, difficultyOfQuestions, typeOfQuestions));
                List<Question> listOfQuestions = loadQuestions(url);

                if (listOfQuestions != null) {
                    System.out.println(url);
                    new GameGUI(listOfQuestions);
                } else {
                    System.out.println("Sorry, TriviaDB API doesn't have any questions for the following: " + url);
                    new IntroGUI();
                }
            } catch (Exception e) {
//                e.printStackTrace();
                errorPane.setVisible(true);
                this.pack();
            }
        });

        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public List<Question> loadQuestions(URL url) throws IOException, ParseException {
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

        if (jsonArr.size() == 0)
            return null;

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
        return listOfQuestions;
    }

    public static String createURL(String numQs, int catQs, int difQs, int typeQs) {
        StringBuilder url = new StringBuilder();
        url.append("https://opentdb.com/api.php?");

        /* Number of Questions */
        url.append("amount=").append(numQs);

        /* Category of Questions, if category = 0 (Any), query is left blank. */
        if (catQs > 0) {
            catQs += 8;
            url.append("&category=").append(catQs);
        }

        /* Difficult of Questions */
        if (difQs > 0) {
            String difficulty = switch (difQs) {
                case 1 -> "easy";
                case 2 -> "medium";
                case 3 -> "hard";
                default -> "";
            };
            url.append("&difficulty=").append(difficulty);
        }

        /* Type of Questions */
        if (typeQs > 0) {
            String type = switch (typeQs) {
                case 1 -> "multiple";
                case 2 -> "boolean";
                default -> "";
            };
            url.append("&type=").append(type);
        }

        return url.toString();
    }

    public static void main(String[] args) {
        new IntroGUI();
    }
}
