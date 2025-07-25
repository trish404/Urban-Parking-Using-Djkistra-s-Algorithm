package com.mycompany.dsa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AllocationDisplay extends JFrame {

    private final Map<Integer, Timer> timers = new HashMap<>();
    private final Map<Integer, Timer> overstayTimers = new HashMap<>();
    private int timeLimit; // Time limit for each parking space in seconds
    private JButton submitButton;
    private ButtonGroup buttonGroup;
    private JRadioButton stayButton;
    private JRadioButton leaveButton;
    private AllocationPanel allocationPanel;
    private Color allocatedColor;
    private boolean isLongTermParking;
    private int allocatedNode;
    private List<Node> nodes;
    private static AllocationDisplay instance = null;

    private AllocationDisplay(List<Node> nodes, List<Edge> edges, List<Integer> shortestPath, int allocatedNode, Color allocatedColor, boolean isLongTermParking) {
        this.nodes = nodes;
        this.allocatedColor = allocatedColor;
        this.isLongTermParking = isLongTermParking;
        this.allocatedNode = allocatedNode;

        setTitle("Allocation Display");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());

        // Create an empty panel for the left margin
        JPanel leftMarginPanel = new JPanel();
        leftMarginPanel.setPreferredSize(new Dimension(425, getHeight())); // Set preferred size for the left margin
        
        JPanel topMarginPanel = new JPanel();
        topMarginPanel.setPreferredSize(new Dimension(getWidth(), 50));

        // Create the allocation panel

        allocationPanel = new AllocationPanel(nodes, edges, shortestPath, allocatedNode, allocatedColor);
        add(allocationPanel);
        

        add(leftMarginPanel, BorderLayout.WEST);
        add(topMarginPanel, BorderLayout.NORTH);
        //add(entryButton, BorderLayout.SOUTH);
        add(allocationPanel, BorderLayout.CENTER);

        updateTimeLimit();
        createTimer(allocatedNode);
    }

    public static AllocationDisplay getInstance(List<Node> nodes, List<Edge> edges, List<Integer> shortestPath, int allocatedNode, Color allocatedColor, boolean isLongTermParking) {
        if (instance == null) {
            instance = new AllocationDisplay(nodes, edges, shortestPath, allocatedNode, allocatedColor, isLongTermParking);
        } else {
            instance.updateParameters(nodes, edges, shortestPath, allocatedNode, allocatedColor, isLongTermParking);
        }
        return instance;
    }

    private void updateParameters(List<Node> nodes, List<Edge> edges, List<Integer> shortestPath, int allocatedNode, Color allocatedColor, boolean isLongTermParking) {
        this.nodes = nodes;
        this.allocatedColor = allocatedColor;
        this.isLongTermParking = isLongTermParking;
        this.allocatedNode = allocatedNode;

        // Update the panel
        allocationPanel.updateAllocation(nodes, edges, shortestPath, allocatedNode, allocatedColor);

        updateTimeLimit();
        // Restart the timer for the new node allocation
        createTimer(allocatedNode);
    }

    private void updateTimeLimit() {
        if (isLongTermParking) {
            timeLimit = 20; // Long term parking time limit (20 seconds)
        } else {
            timeLimit = 10; // Short term parking time limit (10 seconds)
        }
    }

    private void createTimer(int allocatedNode) {
        if (timers.containsKey(allocatedNode)) {
            timers.get(allocatedNode).stop();
        }

        Timer timer = new Timer(1000, new ActionListener() {
            int timeLeft = timeLimit; // Initial time left in seconds

            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                if (timeLeft == 0) {
                    Timer currentTimer = timers.get(allocatedNode);
                    if (currentTimer != null) {
                        currentTimer.stop();
                        showTimesUpDialog(allocatedNode, nodes);
                    }
                }
            }
        });

        timers.put(allocatedNode, timer);
        timer.start();

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> showExitDialog());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void showTimesUpDialog(int allocatedNode, List<Node> nodes) {
        JDialog dialog = new JDialog(this, "Time's Up", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(new JLabel("Time's up for car in parking lot number " + allocatedNode), BorderLayout.CENTER);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel();
        stayButton = new JRadioButton("Stay");
        leaveButton = new JRadioButton("Leave");
        submitButton = new JButton("Submit");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(stayButton);
        buttonGroup.add(leaveButton);

        dialogPanel.add(stayButton);
        dialogPanel.add(leaveButton);
        dialogPanel.add(submitButton);

        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stayButton.setSelected(true);
                leaveButton.setSelected(false);
                nodes.get(allocatedNode).isStay = true;
                nodes.get(allocatedNode).isAllocated = true;
                

                // Start or reset the overstay timer for this node
                if (overstayTimers.containsKey(allocatedNode)) {
                    overstayTimers.get(allocatedNode).stop();
                }

                Timer overstayTimer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        nodes.get(allocatedNode).overstayedTime++;
                    }
                });

                overstayTimers.put(allocatedNode, overstayTimer);
                overstayTimer.start();

                SwingUtilities.invokeLater(() -> allocationPanel.repaint());
            }
        });

        leaveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stayButton.setSelected(false);
                leaveButton.setSelected(true);
                nodes.get(allocatedNode).isStay = false;
                nodes.get(allocatedNode).isAllocated = false;
                nodes.get(allocatedNode).isLeave = true;

                if (overstayTimers.containsKey(allocatedNode)) {
                    overstayTimers.get(allocatedNode).stop();
                }

                SwingUtilities.invokeLater(() -> allocationPanel.repaint());
            }
        });

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!stayButton.isSelected() && !leaveButton.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Please select an option.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (stayButton.isSelected()) {
                        nodes.get(allocatedNode).isStay = true;

                        // Start or reset the overstay timer for this node
                        if (overstayTimers.containsKey(allocatedNode)) {
                            overstayTimers.get(allocatedNode).stop();
                        }

                        Timer overstayTimer = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                nodes.get(allocatedNode).overstayedTime++;
                            }
                        });

                        overstayTimers.put(allocatedNode, overstayTimer);
                        overstayTimer.start();

                    } else if (leaveButton.isSelected()) {
                        nodes.get(allocatedNode).isStay = false;
                        nodes.get(allocatedNode).isLeave = false;
                        deallocateParkingSpace(allocatedNode);
                    }
                    dialog.dispose(); // Close the dialog
                }
            }
        });

        dialog.add(dialogPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deallocateParkingSpace(int deallocatedNode) {
        int overstayedTime = nodes.get(deallocatedNode).overstayedTime; // Get the overstayed time for the node

        // Reset node properties
        nodes.get(deallocatedNode).isAllocated = false;
        nodes.get(deallocatedNode).isStay = false;
        nodes.get(deallocatedNode).overstayedTime = 0;
       
        
        
        // Reset overstayed time

        // Stop the overstay timer for this node if it exists
        if (overstayTimers.containsKey(deallocatedNode)) {
            overstayTimers.get(deallocatedNode).stop();
            overstayTimers.remove(deallocatedNode);
        }
         if (timers.containsKey(deallocatedNode)) {
            timers.get(deallocatedNode).stop();
            timers.remove(deallocatedNode);
        }
        nodes.get(deallocatedNode).isLeave = true;

        // Restore node color
        Color originalColor = nodes.get(deallocatedNode).isLongTerm ? Color.PINK : Color.BLUE;
        allocationPanel.repaint();

        JOptionPane.showMessageDialog(null, "Parking space deallocated successfully. Overstayed time: " + overstayedTime + " seconds.");
          nodes.get(deallocatedNode).isLeave = false;
          nodes.get(deallocatedNode).isLongTerm = false;
        
    }

    private void showExitDialog() {
        JTextField deallocationField = new JTextField(5);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Enter parking lot number to deallocate:"));
        panel.add(deallocationField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Exit Parking Lot",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int deallocatedNode = Integer.parseInt(deallocationField.getText());
                if (nodes.get(deallocatedNode).isAllocated) {
                    if (deallocatedNode == 26) {
                        JOptionPane.showMessageDialog(null, "Cannot deallocate the exit node.");
                    } else {
                        nodes.get(deallocatedNode).isLeave = true;
                        deallocateParkingSpace(deallocatedNode);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "The entered parking space is not allocated.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
            }
        }
    }
}

