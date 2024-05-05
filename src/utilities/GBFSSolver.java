package utilities;

import java.util.*;

public class GBFSSolver {

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
        queue.add(new Node(start, null, calculateHeuristic(start, end)));

        // untuk menyimpan text yang pernah dikunjungi
        Set<String> visited = new HashSet<>();
        visited.add(start);

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

            // pengecekan hasil neighbour jika blm pernah di
            // cek maka masuk ke queue
            for (String neighbor : getNeighbors(current.word, wordList)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(new Node(neighbor, current, calculateHeuristic(neighbor, end)));
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
        int heuristic;

        public Node(String word, Node parent, int heuristic) {
            this.word = word;
            this.parent = parent;
            this.heuristic = heuristic;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.heuristic, other.heuristic);
        }
    }

}
