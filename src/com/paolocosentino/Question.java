package com.paolocosentino;

import java.util.List;

public class Question {
    private final String question;
    private final String correctAnswer;
    private final String questionType;
    private final List<String> answers;

    public Question(String question, String questionType, String correctAnswer, List<String> answers) {
        this.question = question;
        this.questionType = questionType;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getQuestionType() {
        return questionType;
    }

    @Override
    public String toString() {
        if (this.getQuestionType().equalsIgnoreCase("multiple")) {
            return question + '\n'
                    + "a) " + answers.get(0) + "\n"
                    + "b) " + answers.get(1) + "\n"
                    + "c) " + answers.get(2) + "\n"
                    + "d) " + answers.get(3) + "\n";
        } else {
            return question + '\n'
                    + "a) " + answers.get(0) + "\n"
                    + "b) " + answers.get(1) + "\n";
        }
    }
}
