package com.mycompany.dsa;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AllocationPanel extends JPanel {
    private List<Node> nodes;
    private List<Edge> edges;
    private List<Integer> shortestPath;
    private int allocatedNode;
    private Color allocatedColor;

    public AllocationPanel(List<Node> nodes, List<Edge> edges, List<Integer> shortestPath, int allocatedNode, Color allocatedColor) {
        this.nodes = nodes;
        this.edges = edges;
        this.shortestPath = shortestPath;
        this.allocatedNode = allocatedNode;
        this.allocatedColor = allocatedColor;
    }

    public void updateAllocation(List<Node> nodes, List<Edge> edges, List<Integer> shortestPath, int allocatedNode, Color allocatedColor) {
        this.nodes = nodes;
        this.edges = edges;
        this.shortestPath = shortestPath;
        this.allocatedNode = allocatedNode;
        this.allocatedColor = allocatedColor;
        repaint(); // Repaint the panel to reflect the changes
    }
    
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Set border based on allocation type
        



        // Draw edges
        for (Edge edge : edges) {
            if (!shortestPath.contains(edge.u) || !shortestPath.contains(edge.v)
                    || (shortestPath.indexOf(edge.u) != shortestPath.indexOf(edge.v) - 1
                    && shortestPath.indexOf(edge.v) != shortestPath.indexOf(edge.u) - 1))
                g2d.setColor(Color.BLACK);
            else
                g2d.setColor(Color.RED); // Highlight shortest path edges
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(nodes.get(edge.u).x, nodes.get(edge.u).y, nodes.get(edge.v).x, nodes.get(edge.v).y);
        }

        // Draw nodes
        for (Node node : nodes) {
        	int size = 50;
            Color color;
            
            if (node.id >= 0 && node.id <= 14) {
                size = 80; // Increase size for four-wheeler nodes
            }
            
            if(node.id == 25 || node.id == 26) {
            	size = 40;
            }
            if (node.isStay) {
                color = Color.YELLOW;// Use yellow color for nodes in stay state
                g2d.setStroke(new BasicStroke(6));
                g2d.drawRect(node.x - size / 2, node.y - size / 2, size, size);
            }
            else if(node.isLeave)
            {
                color = Color.GRAY;
                // node.isLeave = false;
            }
            else if(!node.isAllocated)
            {
                color = Color.GRAY;
            }
            else if (node.id == allocatedNode) {
                color = allocatedColor;// Use allocated color
            } 
            else {
                if (node.isLongTerm) {
                    color = Color.PINK;// Assign pink for long-term parking  
                } else {
                    color = node.isAllocated ? Color.GREEN : Color.GRAY;// Use green for allocated nodes, blue for others
                     
                }
            }
            
            g2d.setColor(color);
            g2d.fillRect(node.x - size / 2, node.y - size / 2, size, size);
            // modified if and else
            if(color == Color.PINK)
            {
                float[] dashPattern = {10.0f, 10.0f}; // 10 pixels on, 10 pixels off
                 BasicStroke dashedStroke = new BasicStroke(
                3.0f,                      // Line width
                BasicStroke.CAP_BUTT,      // End-cap style
                BasicStroke.JOIN_MITER,    // Line join style
                10.0f,                     // Miter limit
                dashPattern,               // Dash pattern
                0.0f                       // Dash phase
            );
             g2d.setStroke(dashedStroke);
             g2d.setColor(Color.BLACK);
             g2d.drawRect(node.x - size / 2, node.y - size / 2, size, size);
            }
            
            else if(color == Color.GREEN)
            {
              g2d.setStroke(new BasicStroke(2));
              g2d.setColor(Color.BLACK);
              g2d.drawRect(node.x - size / 2, node.y - size / 2, size, size);
              
            }
         
            
            
            g2d.setColor(Color.BLACK);
            g2d.drawString(Integer.toString(node.id), node.x - 4, node.y + 4);
            if (node.id == 25) {
                g2d.drawString("Entry", node.x + 25, node.y + 2);
            }
            if (node.id == 26) {
                g2d.drawString("Exit", node.x - 10, node.y + 35);
            }
        }
    }
}
