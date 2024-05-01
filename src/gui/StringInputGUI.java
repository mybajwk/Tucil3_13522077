package gui;

import javax.swing.*;

import utilities.UCSSolver;
import utilities.AStarSolver;
import utilities.GBFSSolver;

import java.awt.GridLayout;
import java.awt.event.*;
import java.util.*;

public class StringInputGUI extends JFrame {
    private JTextField string1Field, string2Field;
    private JButton submitButton;
    private JComboBox<String> dropdown;

    public StringInputGUI() {
        setTitle("String Input GUI");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel string1Label = new JLabel("String 1:");
        string1Field = new JTextField();
        add(string1Label);
        add(string1Field);

        JLabel string2Label = new JLabel("String 2:");
        string2Field = new JTextField();
        add(string2Label);
        add(string2Field);

        // Adding a dropdown ith some options
        JLabel dropdownLabel = new JLabel("Select Option:");
        String[] options = { "UCS", "A*", "GBFS" };
        dropdown = new JComboBox<>(options);
        add(dropdownLabel);
        add(dropdown);

        submitButton = new JButton("Submit");
        add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String str1 = string1Field.getText();
                String str2 = string2Field.getText();
                String selectedOption = (String) dropdown.getSelectedItem();

                str1.toLowerCase();
                str2.toLowerCase();

                if (str1.length() != str2.length()) {
                    JOptionPane.showMessageDialog(null, "Strings must have the same length!");
                } else {
                    Set<String> wordList = utilities.FileReader.readStringsFromFile("./words.txt");
                    if (selectedOption == "UCS") {
                        // call ucs algo
                        long startTime = System.nanoTime();
                        
                        UCSSolver solver = new UCSSolver();
                        List<String> ladder = solver.findShortestLadder(str1, str2, wordList);
                        
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime) / 1000000; 
                        if (ladder != null) {
                            JOptionPane.showMessageDialog(null,"Shortest ladder: " + ladder);
                        }else{
                            JOptionPane.showMessageDialog(null,"not found dude");
                        }

                        JOptionPane.showMessageDialog(null,"Visited nodes count: " + solver.getVisitedNodesCount());
                        JOptionPane.showMessageDialog(null,"Time execution: " + duration +" ms");
                    } else if (selectedOption == "A*") {
                        // call A* algo
                        long startTime = System.nanoTime();
                        
                        AStarSolver solver = new AStarSolver();
                        List<String> ladder = solver.findShortestLadder(str1, str2, wordList);
                        
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime) / 1000000; 
                        if (ladder != null) {
                            JOptionPane.showMessageDialog(null,"Shortest ladder: " + ladder);
                        }else{
                            JOptionPane.showMessageDialog(null,"not found dude");
                        }

                        JOptionPane.showMessageDialog(null,"Visited nodes count: " + solver.getVisitedNodesCount());
                        JOptionPane.showMessageDialog(null,"Time execution: " + duration +" ms");
                    } else if (selectedOption == "GBFS") {
                        // call GBFS * algo
                        long startTime = System.nanoTime();
                        
                        GBFSSolver solver = new GBFSSolver();
                        List<String> ladder = solver.findShortestLadder(str1, str2, wordList);
                        
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime) / 1000000; 
                        if (ladder != null) {
                            JOptionPane.showMessageDialog(null,"Shortest ladder: " + ladder);
                        }else{
                            JOptionPane.showMessageDialog(null,"not found dude");
                        }
                        
                        JOptionPane.showMessageDialog(null,"Visited nodes count: " + solver.getVisitedNodesCount());
                        JOptionPane.showMessageDialog(null,"Time execution: " + duration +" ms");
                    } else {
                        JOptionPane.showMessageDialog(null, "selectnya diisi dl oy");
                    }

                }
            }
        });
    }
}
