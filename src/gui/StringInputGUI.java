package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

                if (str1.length() != str2.length()) {
                    JOptionPane.showMessageDialog(null, "Strings must have the same length!");
                } else {
                    // Print something or perform any action you want here
                    System.out.println("Strings submitted successfully!");
                    System.out.println("Selected Option: " + selectedOption);
                }
            }
        });
    }
}
