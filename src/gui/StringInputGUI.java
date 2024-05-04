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
        setSize(800, 600);
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
                UCSSolver ucsSolver = new UCSSolver();
                ladder = ucsSolver.findShortestLadder(str1, str2, wordList);
                endTime = System.nanoTime();
                displayResults(ucsSolver.getVisitedNodesCount(), ladder, startTime, endTime);
                break;
            case "A*":
                AStarSolver aStarSolver = new AStarSolver();
                ladder = aStarSolver.findShortestLadder(str1, str2, wordList);
                endTime = System.nanoTime();
                displayResults(aStarSolver.getVisitedNodesCount(), ladder, startTime, endTime);
                break;
            case "GBFS":
                GBFSSolver gbfsSolver = new GBFSSolver();
                ladder = gbfsSolver.findShortestLadder(str1, str2, wordList);
                endTime = System.nanoTime();
                displayResults(gbfsSolver.getVisitedNodesCount(), ladder, startTime, endTime);
                break;
            default:
                resultArea.setText("Please select a valid option.");
        }
    }

    private void displayResults(int visitedNodesCount, List<String> ladder, long startTime, long endTime) {
        long duration = (endTime - startTime) / 1000000; // Convert to milliseconds

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family: Arial;'>");

        if (ladder != null) {
            sb.append("<p>Shortest ladder: <span style='color: blue;'>");
            for (String word : ladder) {
                sb.append(word).append(" ");
            }
            sb.append("</span></p>");
        } else {
            sb.append("<p>No ladder found.</p>");
        }

        sb.append("<p>Visited nodes count: ").append(visitedNodesCount).append("</p>");
        sb.append("<p>Time execution: ").append(duration).append(" ms</p>");
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
