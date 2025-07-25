/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsa;

import java.awt.Color;
import javax.swing.JOptionPane;

public class Node {
    public int id;
    public int x;
    public int y;
    public boolean isAllocated;
    public boolean isLongTerm;
    public boolean isStay;
    public boolean isEntry;
    public boolean isExit;
    private Color color;
    public boolean isLeave;
    public int overstayedTime;

    public Node(int id, int x, int y,boolean isAllocated, boolean isLongTerm) {
        this.id = id;
        this.x = x;
        this.y = y;
       this.isEntry = isEntry;
        this.isExit = isExit;
        this.color = Color.GRAY; // Default color
        this.overstayedTime = 0;
        
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return 40; // Assuming a fixed width for simplicity
    }

    public int getHeight() {
        return 40; // Assuming a fixed height for simplicity
    }
   


}
