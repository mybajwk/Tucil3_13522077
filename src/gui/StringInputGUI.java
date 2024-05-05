package gui;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Set;

import utilities.AStarSolver;
import utilities.GBFSSolver;
import utilities.UCSSolver;

public class StringInputGUI extends JFrame {
    private JTextField string1Field, string2Field;
    private JButton submitButton;
    private JComboBox<String> dropdown;
    private JTextPane resultArea;

    public StringInputGUI() {
        setTitle("Words Ladder");
        setSize(500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel string1Label = new JLabel("String 1:");
        string1Field = new JTextField();
        addComponent(string1Label, 0, 0, 1);
        addComponent(string1Field, 0, 1, 1);

        JLabel string2Label = new JLabel("String 2:");
        string2Field = new JTextField();
        addComponent(string2Label, 1, 0, 1);
        addComponent(string2Field, 1, 1, 1);

        JLabel dropdownLabel = new JLabel("Select Option:");
        String[] options = { "UCS", "A*", "GBFS" };
        dropdown = new JComboBox<>(options);
        addComponent(dropdownLabel, 2, 0, 1);
        addComponent(dropdown, 2, 1, 1);

        submitButton = new JButton("Submit");
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 2;
        gbc.gridy = 3;
        add(submitButton, gbc);

        resultArea = new JTextPane();
        resultArea.setEditable(false);
        resultArea.setEditorKit(new HTMLEditorKit());

        JScrollPane scrollPane = new JScrollPane(resultArea);
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(scrollPane, gbc);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        setLocationRelativeTo(null);
    }

    private void addComponent(Component component, int row, int col, int width) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.gridwidth = width;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(component, gbc);
    }

    private void performSearch() {
        Runtime runtime = Runtime.getRuntime();
        long beforeMemory, afterMemory, usedMemory;
        String str1 = string1Field.getText().toLowerCase();
        String str2 = string2Field.getText().toLowerCase();
        String selectedOption = (String) dropdown.getSelectedItem();

        if (str1.length() != str2.length()) {
            resultArea.setText("Strings must have the same length!");
            return;
        }

        Set<String> wordList = utilities.FileReader.readStringsFromFile("./../src/words.txt");
        List<String> ladder;
        long startTime = System.nanoTime();
        long endTime;
        long duration;

        switch (selectedOption) {
            case "UCS":
                runtime.gc();
                beforeMemory = runtime.totalMemory() - runtime.freeMemory();

                UCSSolver ucsSolver = new UCSSolver();
                ladder = ucsSolver.findShortestLadder(str1, str2, wordList);

                runtime.gc();
                afterMemory = runtime.totalMemory() - runtime.freeMemory();
                usedMemory = afterMemory - beforeMemory;

                endTime = System.nanoTime();
                displayResults(ucsSolver.getVisitedNodesCount(), ladder, startTime, endTime, usedMemory);
                break;
            case "A*":
                runtime.gc();
                beforeMemory = runtime.totalMemory() - runtime.freeMemory();

                AStarSolver aStarSolver = new AStarSolver();
                ladder = aStarSolver.findShortestLadder(str1, str2, wordList);

                runtime.gc();
                afterMemory = runtime.totalMemory() - runtime.freeMemory();
                usedMemory = afterMemory - beforeMemory;

                endTime = System.nanoTime();
                displayResults(aStarSolver.getVisitedNodesCount(), ladder, startTime, endTime, usedMemory);
                break;
            case "GBFS":
                runtime.gc();
                beforeMemory = runtime.totalMemory() - runtime.freeMemory();

                GBFSSolver gbfsSolver = new GBFSSolver();
                ladder = gbfsSolver.findShortestLadder(str1, str2, wordList);

                runtime.gc();
                afterMemory = runtime.totalMemory() - runtime.freeMemory();
                usedMemory = afterMemory - beforeMemory;

                endTime = System.nanoTime();
                displayResults(gbfsSolver.getVisitedNodesCount(), ladder, startTime, endTime, usedMemory);
                break;
            default:
                resultArea.setText("Please select a valid option.");
        }
    }

    private String formatWord(String word1, String word2) {
        StringBuilder formatted = new StringBuilder("<table style='margin: auto;'><tr>");
        for (int i = 0; i < word1.length(); i++) {
            if (i < word2.length()) {
                String bgColor = word1.charAt(i) == word2.charAt(i) ? "lightgray" : "red";
                String color = word1.charAt(i) == word2.charAt(i) ? "black" : "white";
                formatted.append("<td style='width:20px; height:20px; background-color:")
                        .append(bgColor)
                        .append("; color:")
                        .append(color)
                        .append("; text-align:center; border:1px solid black;'>")
                        .append(word2.charAt(i))
                        .append("</td>");
            }
        }
        formatted.append("</tr></table>");
        return formatted.toString();
    }

    private void displayResults(int visitedNodesCount, List<String> ladder, long startTime, long endTime, long memory) {
        long duration = (endTime - startTime) / 1000000; // Convert to milliseconds

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family: Arial; text-align:center;'>");

        if (ladder != null && !ladder.isEmpty()) {
            sb.append(
                    "<h2 style='font-size:20px; color: #333; font-weight: bold; margin-bottom: 10px; border-bottom: 2px solid #666; display: inline-block; padding-bottom: 5px;'>Shortest Ladder</h2>");

            sb.append("<div style='margin:auto; width:fit-content;'>");
            sb.append("<table><tr><th style='padding: 5px;'>Step</th><th>Word</th></tr>");
            sb.append("<tr><td>1</td><td>").append(formatWord(ladder.get(0), ladder.get(0))).append("</td></tr>");
            for (int i = 0; i < ladder.size() - 1; i++) {
                sb.append("<tr><td>")
                        .append(i + 2)
                        .append("</td><td>")
                        .append(formatWord(ladder.get(i), ladder.get(i + 1)))
                        .append("</td></tr>");
            }
            sb.append("</table></div>");
        } else {
            sb.append("<p>No ladder found.</p>");
        }

        // Centering visited node count and execution time
        sb.append("<p style='margin-top:20px;'>Visited nodes count: ").append(visitedNodesCount).append("</p>");
        sb.append("<p>Time execution: ").append(duration).append(" ms</p>");
        sb.append("<p>Used memory (bytes): ").append(memory);
        sb.append("</body></html>");

        resultArea.setContentType("text/html");
        resultArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StringInputGUI().setVisible(true);
        });
    }
}
