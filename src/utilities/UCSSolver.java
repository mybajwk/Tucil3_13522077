package utilities;

import java.util.*;

public class UCSSolver {

    private int visitedNodesCount;

    public int getVisitedNodesCount() {
        return visitedNodesCount;
    }

    public List<String> findShortestLadder(String start, String end, Set<String> wordList) {
        visitedNodesCount = 0;

        if (!wordList.contains(start) || !wordList.contains(end)) {
            System.out.println("Start or end word is not in the word list.");
            return null;
        }

        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(start, null, 0));
        Map<String, Integer> costs = new HashMap<>();
        costs.put(start, 0);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.word.equals(end)) {
                return reconstructLadder(current);
            }

            if (costs.get(current.word) < current.cost) {
                continue;
            }

            visitedNodesCount++;
            for (String neighbor : getNeighbors(current.word, wordList)) {
                int newCost = current.cost + 1;
                if (!costs.containsKey(neighbor) || newCost < costs.get(neighbor)) {
                    costs.put(neighbor, newCost);
                    queue.add(new Node(neighbor, current, newCost));
                }
            }
        }

        System.out.println("No ladder found.");
        return null;
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

    // Main
    public static void main(String[] args) {
        Set<String> wordList = FileReader.readStringsFromFile("./words.txt");
        UCSSolver solver = new UCSSolver();
        List<String> ladder = solver.findShortestLadder("east", "love", wordList);
        if (ladder != null) {
            System.out.println("Shortest ladder: " + ladder);
            System.out.println("Visited nodes count: " + solver.getVisitedNodesCount());
        }
    }
}
