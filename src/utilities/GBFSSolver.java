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

        if (!wordList.contains(start) || !wordList.contains(end)) {
            System.out.println("Start or end word is not in the word list.");
            return null;
        }

        runtime.gc();
        beforeMemory = runtime.totalMemory() - runtime.freeMemory();
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(start, null, calculateHeuristic(start, end)));

        Set<String> visited = new HashSet<>();
        visited.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visitedNodesCount++;

            if (current.word.equals(end)) {
                runtime.gc();
                afterMemory = runtime.totalMemory() - runtime.freeMemory();
                usedMemory = afterMemory - beforeMemory;
                return reconstructLadder(current);
            }

            for (String neighbor : getNeighbors(current.word, wordList)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(new Node(neighbor, current, calculateHeuristic(neighbor, end)));
                }
            }
        }
        runtime.gc();
        afterMemory = runtime.totalMemory() - runtime.freeMemory();
        usedMemory = afterMemory - beforeMemory;
        System.out.println("No ladder found.");
        return null;
    }

    private int calculateHeuristic(String word, String target) {
        int distance = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != target.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    private List<String> getNeighbors(String word, Set<String> wordList) {
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

    // public static void main(String[] args) {
    // Set<String> wordList = FileReader.readStringsFromFile("./words.txt");
    // GBFSSolver solver = new GBFSSolver();
    // List<String> ladder = solver.findShortestLadder("hello", "check", wordList);
    // if (ladder != null) {
    // System.out.println("Shortest ladder: " + ladder);
    // System.out.println("Visited nodes count: " + solver.getVisitedNodesCount());
    // }
    // }
}
