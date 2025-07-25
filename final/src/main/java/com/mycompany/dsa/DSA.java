package com.mycompany.dsa;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;


public class DSA extends JFrame {

        static java.util.List<Node> nodes = new ArrayList<>();
    static java.util.List<Edge> edges = new ArrayList<>(); // Define edges here

    static class GraphPanel extends JPanel {
        private final java.util.List<Node> nodes;
        private final java.util.List<Edge> edges;
        private java.util.List<Integer> shortestPath;

        GraphPanel(java.util.List<Node> nodes, java.util.List<Edge> edges, java.util.List<Integer> shortestPath) {
            this.nodes = nodes;
            this.edges = edges;
            this.shortestPath = shortestPath;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Draw edges
            for (Edge edge : edges) {
                if (!shortestPath.contains(edge.u) || !shortestPath.contains(edge.v)
                        || (shortestPath.indexOf(edge.u) != shortestPath.indexOf(edge.v) - 1
                                && shortestPath.indexOf(edge.v) != shortestPath.indexOf(edge.u) - 1))
                    g2d.setColor(Color.BLACK);
                else
                    g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(nodes.get(edge.u).x, nodes.get(edge.u).y, nodes.get(edge.v).x, nodes.get(edge.v).y);
            }

            // Draw nodes
            for (Node node : nodes) {
            	int size = 40;
                Color color = Color.GRAY;
                if (node.id >= 0 && node.id <= 14) {
                    size = 60; // Increased size for four-wheeler nodes
                }
                if (node.isEntry) {
                    color = Color.PINK;
                } else if (node.isExit) {
                    color = Color.PINK;
                    g2d.setColor(Color.PINK);
                    g2d.fillRect(node.x - 20, node.y - 20, 80, 50); // Draw rectangle parking lot
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("EXIT", node.x - 15, node.y);
                    continue;
                } else if (node.isAllocated) {
                    color = Color.GREEN; // Color for allocated nodes
                    g2d.setColor(color);
                    g2d.fillRect(node.x - size / 2, node.y - size, size, size); // Draw rectangle parking lot
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(Integer.toString(node.id), node.x - 4, node.y + 4);
                    continue;
                }
                
                g2d.setColor(color);
                g2d.fillRect(node.x - size / 2, node.y - size / 2, size, size); // Draw rectangle parking lot
                g2d.setColor(Color.WHITE);
                g2d.drawString(Integer.toString(node.id), node.x - 4, node.y + 4);
            }
            
            // Draw shortest path
            if (shortestPath != null) {
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(3));
                for (int i = 0; i < shortestPath.size() - 1; i++) {
                    int u = shortestPath.get(i);
                    int v = shortestPath.get(i + 1);
                    // Exclude edges directly connecting to the exit node (node 26)
                    if (!((u == 26 && v == 20) || (u == 20 && v == 26) || (u == 26 && v == 21) || (u == 21 && v == 26)
                            || (u == 26 && v == 22) || (u == 22 && v == 26) || (u == 26 && v == 23)
                            || (u == 23 && v == 26)
                            || (u == 26 && v == 24) || (u == 24 && v == 26)))
                        g2d.drawLine(nodes.get(u).x, nodes.get(u).y, nodes.get(v).x, nodes.get(v).y);
                }
            }
        }
    }
    public static void main(String[] args) {

         List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();  

        VehicleSelection vehicleSelection = new VehicleSelection(nodes,edges);
        vehicleSelection.setVisible(true); // Show the vehicle selection window

        int V = 29; // Total nodes in a 5x5 grid plus entry and exit nodes
        int gridSize = 100; // Grid size for node placement
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;

        // Calculate the total width and height of the grid
        int totalWidth = gridSize * 5;
        int totalHeight = gridSize * 5;

        // Calculate the starting position of the grid to center it
        int startX = centerX - totalWidth / 2;
        int startY = centerY - totalHeight / 2;

        // Create nodes and edges for a 5x5 grid
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                nodes.add(new Node(i * 5 + j, (j + 1) * gridSize, (i + 1) * gridSize, false, false)); // Adjust
                // positions for
                // grid layout

                // Add horizontal edges
                if (j < 4) {
                    edges.add(new Edge(i * 5 + j, i * 5 + j + 1, 1)); // Default weight is 1
                    edges.add(new Edge(i * 5 + j + 1, i * 5 + j, 1));
                }

                // Add vertical edges
                if (i < 4) {
                    edges.add(new Edge(i * 5 + j, (i + 1) * 5 + j, 1));
                    edges.add(new Edge((i + 1) * 5 + j, i * 5 + j, 1));
                }
            }
        }

        // Add entry node to the right of the edge between nodes 9 and 14
        nodes.add(new Node(25, (nodes.get(9).x + nodes.get(14).x) / 2 + gridSize,
                (nodes.get(9).y + nodes.get(14).y) / 2, true, false));
        edges.add(new Edge(9, 25, 1));
        edges.add(new Edge(25, 9, 1));

        // Add edges from node 25 to nodes 4, 9, 14, 19, and 21 with weights
        edges.add(new Edge(25, 4, 4));
        edges.add(new Edge(25, 9, 2));
        edges.add(new Edge(25, 14, 3));
        edges.add(new Edge(25, 19, 5));
        edges.add(new Edge(25, 24, 6));
        
        int exitX = startX - gridSize * 2; // Adjust x position for the exit node
        int exitY = centerY + gridSize * 2; // Adjust y position for the exit node
        nodes.add(new Node(26, exitX, exitY, false, true)); // Exit node positioned away from the parking lot
        

        int source = 25; // Example source node

        Graph graph = new Graph(V); // Increase V for entry and exit nodes
        for (Edge edge : edges) {
            graph.addEdge(edge.u, edge.v, edge.weight); // Use edge weight
        }
    }
 
}