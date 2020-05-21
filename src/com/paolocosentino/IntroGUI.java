package com.paolocosentino;

import javax.swing.*;
import java.util.Objects;

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

    private String numberOfQuestions;
    private String categoryOfQuestions;
    private String difficultyOfQuestions;
    private String typeOfQuestions;

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
                numberOfQuestions = Objects.requireNonNull(numCB.getSelectedItem()).toString();
                categoryOfQuestions = Objects.requireNonNull(categoryCB.getSelectedItem()).toString();
                difficultyOfQuestions = Objects.requireNonNull(difficultyCB.getSelectedItem()).toString();
                typeOfQuestions = Objects.requireNonNull(typeCB.getSelectedItem()).toString();

//                System.out.println(numberOfQuestions + '\n' + categoryOfQuestions + '\n' + difficultyOfQuestions + '\n' + typeOfQuestions);
                dispose();
            } catch (Exception e) {
                errorPane.setVisible(true);
                //e.printStackTrace();
                System.out.println("All FIELDS REQUIRED");
            }
        });

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public String getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public String getCategoryOfQuestions() {
        return categoryOfQuestions;
    }

    public String getDifficultyOfQuestions() {
        return difficultyOfQuestions;
    }

    public String getTypeOfQuestions() {
        return typeOfQuestions;
    }

//    public static void main(String[] args) {
//        JFrame frame = new IntroGUI();
//        frame.setVisible(true);
//    }
}
