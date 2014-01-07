package it.unibz.inf.aa.ajrep.ui;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class UI extends JFrame {
    public JButton submit;
    public JTextPane variablesTextArea, functionTextArea, constraintsTextArea;
    public JComboBox choice;
    
    public UI() {
        this.setSize(340, 370);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("AA Linear Programming");
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        
        //Panel for text fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(null);
        fieldsPanel.setBounds(0, 0, 340, 300);
        //fieldsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        //Variables input
        JLabel variablesLabel = new JLabel("Variables:");
        variablesLabel.setBounds(10, 10, 100, 30);
        fieldsPanel.add(variablesLabel);
        
        variablesTextArea = new JTextPane();
        variablesTextArea.setBounds(10, 40, 300, 30);
        
        JScrollPane variablesScrollPane = new JScrollPane();
        variablesScrollPane.setBounds(10, 40, 310, 40);
        variablesScrollPane.setViewportView(variablesTextArea);
        fieldsPanel.add(variablesScrollPane);
        
        //Objective function input
        JLabel functionLabel = new JLabel("Objective function:");
        functionLabel.setBounds(10, 100, 150, 30);
        fieldsPanel.add(functionLabel);
        
        functionTextArea = new JTextPane();
        functionTextArea.setBounds(10, 130, 300, 30);
        
        JScrollPane functionScrollPane = new JScrollPane();
        functionScrollPane.setBounds(10, 130, 310, 40);
        functionScrollPane.setViewportView(functionTextArea);
        fieldsPanel.add(functionScrollPane);
        
        //Constraints input
        JLabel constraintsLabel = new JLabel("Constraints:");
        constraintsLabel.setBounds(10, 180, 150, 30);
        fieldsPanel.add(constraintsLabel);
        
        constraintsTextArea = new JTextPane();
        constraintsTextArea.setBounds(10, 210, 300, 70);
        
        JScrollPane constraintsScrollPane = new JScrollPane();
        constraintsScrollPane.setBounds(10, 210, 310, 80);
        constraintsScrollPane.setViewportView(constraintsTextArea);
        fieldsPanel.add(constraintsScrollPane);
        
        //Submit panel
        JPanel submitPanel = new JPanel();
        submitPanel.setLayout(null);
        //submitPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        submitPanel.setBounds(0, 300, 340, 30);
        
        //choice of max/min 
        String[] choices = {"Maximize", "Minimize"};
        choice = new JComboBox(choices);
        choice.setSelectedIndex(0);
        choice.setBounds(10, 10, 80, 20);
        submitPanel.add(choice);
        
        //submit button
        submit = new JButton("Submit");
        submit.setBounds(100, 10, 100, 20);
        submitPanel.add(submit);
        
        mainPanel.add(fieldsPanel);
        mainPanel.add(submitPanel);
        this.add(mainPanel);
    }
}
