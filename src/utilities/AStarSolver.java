package utilities;

import java.util.*;

public class AStarSolver {

    private int visitedNodesCount;
    private long usedMemory;

    public int getVisitedNodesCount() {
        return visitedNodesCount;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public List<String> findShortestLadder(String start, String end, Set<String> wordList) {
        visitedNodesCount = 1;
        usedMemory = 0;
        Runtime runtime = Runtime.getRuntime();
        long beforeMemory, afterMemory;

        // cek apakah start dan target ada di kamus tidak
        if (!wordList.contains(start) || !wordList.contains(end)) {
            System.out.println("Start or end word is not in the word list.");
            return null;
        }

        // untuk pengecekan memory (kondisi memori awall)
        runtime.gc();
        beforeMemory = runtime.totalMemory() - runtime.freeMemory();

        // deklarasi queue
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(start, null, 0, calculateHeuristic(start, end)));

        // untuk menyimpan text yang pernah dikunjungi
        Map<String, Integer> visited = new HashMap<>();
        visited.put(start, 0);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visitedNodesCount++;
            if (current.word.equals(end)) {
                // saat ketemu stop, dan hitung memory kondisi akhir
                runtime.gc();
                afterMemory = runtime.totalMemory() - runtime.freeMemory();
                usedMemory = afterMemory - beforeMemory;

                // return berupa list dari hasil construct ladder
                return reconstructLadder(current);
            }

            // pengecekan hasil neighbour jika memiliki cost lebih rendah atau blm pernah di
            // cek maka masuk ke queue
            for (String neighbor : getNeighbors(current.word, wordList)) {
                int newCost = current.cost + 1;
                if (!visited.containsKey(neighbor) || newCost < visited.get(neighbor)) {
                    visited.put(neighbor, newCost);
                    int priority = newCost + calculateHeuristic(neighbor, end);
                    queue.add(new Node(neighbor, current, newCost, priority));
                }
            }
        }

        // hitung kondisi memory akhir (jika tidak ketemu solusi)
        runtime.gc();
        afterMemory = runtime.totalMemory() - runtime.freeMemory();
        usedMemory = afterMemory - beforeMemory;

        System.out.println("No ladder found.");
        return null;
    }

    private int calculateHeuristic(String word, String target) {
        // hitung jml char yang beda
        int distance = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != target.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    private List<String> getNeighbors(String word, Set<String> wordList) {
        // mencari semua list text yang beda 1 char dengan word saat itu
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();
        for (int i = 0; i < word.length(); i++) {
            char oldChar = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c != oldChar) {
                    chars[i] = c;
                    String newWord = new String(chars);
                    if (wordList.contains(newWord)) {
                        neighbors.add(newWord);
                    }
                }
            }
            chars[i] = oldChar;
        }
        return neighbors;
    }

    private List<String> reconstructLadder(Node endNode) {
        // membangun ladder dengan iterasi dari target manggil parent sampe ke start
        LinkedList<String> ladder = new LinkedList<>();
        for (Node current = endNode; current != null; current = current.parent) {
            ladder.addFirst(current.word);
        }
        return ladder;
    }

    private class Node implements Comparable<Node> {
        String word;
        Node parent;
        int cost;
        int priority;

        public Node(String word, Node parent, int cost, int priority) {
            this.word = word;
            this.parent = parent;
            this.cost = cost;
            this.priority = priority;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.priority, other.priority);
        }
    }
}
