/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsa;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

/**
 *
 * @author everybody
 */
public class VehicleSelection extends JFrame {
    
    JLabel label,termLabel;
    JRadioButton fourWheeler, twoWheeler,longTerm,shortTerm;
    JButton submit;
    private final List<Node> nodes ;
    private final List<Edge> edges ; // Define nodes as a field in the class


    public VehicleSelection(List<Node> nodes, List<Edge> edges) {
        setLayout(new FlowLayout());
        label = new JLabel("Select Vehicle Type:");
        fourWheeler = new JRadioButton("Four-Wheeler");
        twoWheeler = new JRadioButton("Two-Wheeler");
        termLabel = new JLabel("Select Term of Parking ");
        longTerm = new JRadioButton("Long-Term Parking");
        shortTerm = new JRadioButton("Short-Term Parking");
        submit = new JButton("Submit");
        this.nodes = nodes;
        this.edges = edges;

    ButtonGroup group = new ButtonGroup();
        group.add(fourWheeler);
        group.add(twoWheeler);

    ButtonGroup parkinggroup = new ButtonGroup();
        parkinggroup.add(longTerm);
        parkinggroup.add(shortTerm);

        add(label);
        add(fourWheeler);
        add(twoWheeler);
        add(termLabel);
        add(longTerm); // Add long-term parking checkbox
        add(shortTerm); // Add short-term parking checkbox
        add(submit);
        

        submit.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                if (!fourWheeler.isSelected() && !twoWheeler.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Please select a vehicle type.");
                } else if (!longTerm.isSelected() && !shortTerm.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Please select the term of parking.");
                } else {
                    int selectedNode = -1;
                    Color allocatedColor = null;
                    boolean isLongTermParking = false;
                    if (fourWheeler.isSelected()) {
                        // Allocate nearest available nodes for four-wheelers (nodes 0 to 14)
                        selectedNode = allocateNearestNode(nodes, 0, 14);
                    } else if (twoWheeler.isSelected()) {
                        // Allocate nearest available nodes for two-wheelers (nodes 16 to 24)
                        selectedNode = allocateNearestNode(nodes, 15, 24);
                    }

                    if (selectedNode != -1) {
                        // Mark the selected node as allocated
                        nodes.get(selectedNode).isAllocated = true;
                        
                        if (shortTerm.isSelected()) {
                        allocatedColor = Color.GREEN;
                    } else if (longTerm.isSelected()) {
                        allocatedColor = Color.PINK;// Purple
                        nodes.get(selectedNode).isLongTerm = true;
                        isLongTermParking = true;
                    }

                        // Find the shortest path from entry to the allocated slot
                        int source = 25; // Entry node
                        Graph graph = new Graph(nodes.size());
                        for (Edge edge : edges) {
                            graph.addEdge(edge.u, edge.v, edge.weight); // Use edge weight
                        }
                        java.util.List<Integer> shortestPath = graph.shortestPath(source, selectedNode);

                        // Display the shortest path
                        JOptionPane.showMessageDialog(null,
                                "Node " + selectedNode + " allocated.\nShortest Path: " + shortestPath.toString());

                                                
                        AllocationDisplay allocationDisplay = AllocationDisplay.getInstance(nodes, edges, shortestPath,
                                selectedNode, allocatedColor, isLongTermParking);
                         allocationDisplay.setVisible(true); 
                    } else {
                        JOptionPane.showMessageDialog(null, "No available nodes for allocation.");
                    }
                }
            }
            
           
        });

        setSize(200,300); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Vehicle Selection");
        setVisible(true);
    }
    
    public static int allocateNearestNode(List<Node> nodes, int start, int end) {
            int minDistance = Integer.MAX_VALUE;
            int nearestNode = -1;
            for (int i = start; i <= end; i++) {
                if (!nodes.get(i).isAllocated) {
                    int distance = Math.abs(nodes.get(i).x - nodes.get(25).x) +
                            Math.abs(nodes.get(i).y - nodes.get(25).y);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestNode = i;
                    }
                }
            }
            return nearestNode;
        }
}
