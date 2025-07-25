/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author everybody
 */
public class Graph {
        private final int V;
        private final Map<Integer, java.util.List<Edge>> adj;

        Graph(int V) {
            this.V = V;
            adj = new HashMap<>();
            for (int i = 0; i < V; ++i)
                adj.put(i, new LinkedList<>());
        }

        void addEdge(int u, int v, int weight) {
            adj.get(u).add(new Edge(u, v, weight));
            adj.get(v).add(new Edge(v, u, weight)); // For undirected graph
        }

        java.util.List<Integer> shortestPath(int src, int dest) {
            int[] dist = new int[V];
            int[] prev = new int[V];
            Arrays.fill(dist, Integer.MAX_VALUE);
            Arrays.fill(prev, -1);
            dist[src] = 0;

            PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.x + a.y)); // Adjust priority
            // based on node
            // position

            pq.add(new Node(src, 0, 0, false, false));

            while (!pq.isEmpty()) {
                int u = pq.poll().id;

                for (Edge e : adj.get(u)) {
                    int v = e.v;
                    int weight = e.weight; // Get edge weight

                    if (dist[v] > dist[u] + weight) {
                        dist[v] = dist[u] + weight;
                        prev[v] = u;
                        pq.add(new Node(v, 0, 0, false, false));
                    }
                }
            }

            return reconstructPath(src, dest, prev);
        }

        java.util.List<Integer> reconstructPath(int src, int dest, int[] prev) {
            java.util.List<Integer> path = new ArrayList<>();
            for (int at = dest; at != -1; at = prev[at]) {
                path.add(at);
                if (at == src)
                    break;
            }
            Collections.reverse(path);
            return path;
        }
/*
    private static class color {

        public color() {
        }
    }
*/
    }

