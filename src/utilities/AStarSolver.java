package utilities;

import java.util.*;

public class AStarSolver {

    private int visitedNodesCount;

    public int getVisitedNodesCount() {
        return visitedNodesCount;
    }

    public List<String> findShortestLadder(String start, String end, Set<String> wordList) {
        visitedNodesCount = 0;

        // Check
        if (!wordList.contains(start) || !wordList.contains(end)) {
            System.out.println("Start or end word is not in the word list.");
            return null;
        }

        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(start, null, 0, calculateHeuristic(start, end)));

        Map<String, Integer> visited = new HashMap<>();
        visited.put(start, 0);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visitedNodesCount++;
            if (current.word.equals(end)) {
                return reconstructLadder(current);
            }

            for (String neighbor : getNeighbors(current.word, wordList)) {
                int newCost = current.cost + 1;
                if (!visited.containsKey(neighbor) || newCost < visited.get(neighbor)) {
                    visited.put(neighbor, newCost);
                    int priority = newCost + calculateHeuristic(neighbor, end);
                    queue.add(new Node(neighbor, current, newCost, priority));
                }
            }
        }

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

    public static void main(String[] args) {
        Set<String> wordList = FileReader.readStringsFromFile("./words.txt");
        AStarSolver solver = new AStarSolver();
        List<String> ladder = solver.findShortestLadder("charge", "comedo", wordList);
        if (ladder != null) {
            System.out.println("Shortest ladder: " + ladder);
            System.out.println("Visited nodes count: " + solver.getVisitedNodesCount());
        }
    }
}
