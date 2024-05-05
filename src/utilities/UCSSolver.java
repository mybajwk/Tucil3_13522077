package utilities;

import java.util.*;

public class UCSSolver {

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
        queue.add(new Node(start, null, 0));

        // untuk menyimpan text dan costnya
        Map<String, Integer> costs = new HashMap<>();
        costs.put(start, 0);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.word.equals(end)) {
                // saat ketemu stop, dan hitung memory kondisi akhir
                runtime.gc();
                afterMemory = runtime.totalMemory() - runtime.freeMemory();
                usedMemory = afterMemory - beforeMemory;

                // return berupa list dari hasil construct ladder
                return reconstructLadder(current);
            }

            if (costs.get(current.word) < current.cost) {
                continue;
            }

            visitedNodesCount++;
            // pengecekan hasil neighbour jika memiliki cost lebih rendah atau blm pernah di
            // cek maka masuk ke queue
            for (String neighbor : getNeighbors(current.word, wordList)) {
                int newCost = current.cost + 1;
                if (!costs.containsKey(neighbor) || newCost < costs.get(neighbor)) {
                    costs.put(neighbor, newCost);
                    queue.add(new Node(neighbor, current, newCost));
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
        Node current = endNode;
        while (current != null) {
            ladder.addFirst(current.word);
            current = current.parent;
        }
        return ladder;
    }

    private class Node implements Comparable<Node> {
        String word;
        Node parent;
        int cost;

        public Node(String word, Node parent, int cost) {
            this.word = word;
            this.parent = parent;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }
    }
}
