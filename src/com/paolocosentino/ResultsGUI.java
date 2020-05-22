package com.paolocosentino;

import javax.swing.*;

public class ResultsGUI extends JFrame {
    private JPanel mainPanel;
    private JTextPane resultLabel;
    private JTextPane playAgainLabel;
    private JButton yesButton;
    private JButton noButton;
    private JSplitPane buttonSplitPane;

    private int score;

    public ResultsGUI(int score, int size) {
        super("Results");
        this.setSize(300, 300);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);

        /* Result Pane */
        resultLabel.setBackground(null);
        resultLabel.setText(score + " / " + size);

        /* Play Again Label Pane */
        playAgainLabel.setBackground(null);

        /* Button splitPane */
        buttonSplitPane.setResizeWeight(.5d);
        buttonSplitPane.setDividerSize(1);
        buttonSplitPane.setContinuousLayout(true);
        buttonSplitPane.setBorder(null);

        /* Button Functionality */
        yesButton.addActionListener(ActionListener -> {
            dispose();
            new IntroGUI();
        });

        noButton.addActionListener(ActionListener -> {
            System.exit(0);
        });


        this.setVisible(true);
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
