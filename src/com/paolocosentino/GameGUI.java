package com.paolocosentino;

import javax.swing.*;
import java.util.List;

public class GameGUI extends JFrame {
    private final List<Question> listOfQuestions;
    private int currentQuestion = 0;
    private int score = 0;

    private JPanel mainPanel;
    private JTextArea questionPanel;
    private JSplitPane topSplitPane;
    private JSplitPane bottomSplitPane;
    private JButton topLeftButton;
    private JButton topRightButton;
    private JButton bottomLeftButton;
    private JButton bottomRightButton;
    private JLabel questionLabel;

    public GameGUI(List<Question> listOfQuestions) {
        super("Trivia App");
        this.listOfQuestions = listOfQuestions;

        this.setSize(1200, 400);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);


        /* Answer Panel */
        topSplitPane.setResizeWeight(.5d);
        topSplitPane.setDividerSize(1);
        topSplitPane.setContinuousLayout(true);
        topSplitPane.setBorder(null);
        bottomSplitPane.setResizeWeight(.5d);
        bottomSplitPane.setDividerSize(1);
        bottomSplitPane.setContinuousLayout(true);
        bottomSplitPane.setBorder(null);


        /* Question Panel */
        questionPanel.setBackground(null);
        questionPanel.setBorder(null);

        /* Initialize Game */
        setNextQuestion(currentQuestion);

        /* Button Functionality */
        topLeftButton.addActionListener(ActionListener -> {
            if (isCorrect(0, listOfQuestions.get(currentQuestion)))
                score++;

            topLeftButton.setFocusPainted(false);
            currentQuestion += 1;
            setNextQuestion(currentQuestion);
        });

        topRightButton.addActionListener(ActionListener -> {
            if (isCorrect(1, listOfQuestions.get(currentQuestion)))
                score++;

            topRightButton.setFocusPainted(false);
            currentQuestion += 1;
            setNextQuestion(currentQuestion);
        });

        bottomLeftButton.addActionListener(ActionListener -> {
            if (isCorrect(2, listOfQuestions.get(currentQuestion)))
                score++;

            bottomLeftButton.setFocusPainted(false);
            currentQuestion += 1;
            setNextQuestion(currentQuestion);
        });

        bottomRightButton.addActionListener(ActionListener -> {
            if (isCorrect(3, listOfQuestions.get(currentQuestion)))
                score++;

            bottomRightButton.setFocusPainted(false);
            currentQuestion += 1;
            setNextQuestion(currentQuestion);
        });

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private boolean isCorrect(int answer, Question currentQuestion) {
        return currentQuestion.getCorrectAnswer().equalsIgnoreCase(currentQuestion.getAnswers().get(answer));
    }

    private void setNextQuestion(int curr) {
        if (curr == listOfQuestions.size()) {
            dispose();
            new ResultsGUI(score, listOfQuestions.size());
            return;
        }

        //Set question
        questionLabel.setText("Question: " + (curr + 1));
        questionPanel.setText(listOfQuestions.get(curr).getQuestion());

        //Set answer choices based on question type (Multiple or Boolean)
        if (listOfQuestions.get(currentQuestion).getQuestionType().equalsIgnoreCase("boolean")) {
            topSplitPane.setVisible(true);
            bottomSplitPane.setVisible(false);

            topLeftButton.setText("True");
            topRightButton.setText("False");
        } else {
            topSplitPane.setVisible(true);
            bottomSplitPane.setVisible(true);

            topLeftButton.setText(listOfQuestions.get(curr).getAnswers().get(0));
            topRightButton.setText(listOfQuestions.get(curr).getAnswers().get(1));
            bottomLeftButton.setText(listOfQuestions.get(curr).getAnswers().get(2));
            bottomRightButton.setText(listOfQuestions.get(curr).getAnswers().get(3));
        }
    }
}
