# Tugas Kecil 3 Strategi Algoritma - 13522077

![GitHub last commit](https://img.shields.io/github/last-commit/mybajwk/Tucil3_13522077)

This repository hosts Java code designed to solve the word ladder puzzle. It seeks to find solutions to the game by employing three different algorithms: Uniform Cost Search, Greedy Best First Search, and A\*.

## Table of Contents

- [Features](#features)
- [Usage](#usage)
- [Creator](#creator)

## Features

The key features of this program include:

## Uniform Cost Search (UCS)

Uniform Cost Search identifies the shortest path from the starting word to the ending word by treating each step's cost equally. It methodically assesses all possible routes based on their cumulative costs to determine the most economical path.

## Greedy Best First Search (GBFS)

Greedy Best First Search optimizes the search process towards the goal by favoring paths that seem closest to the target, using a heuristic function. This approach focuses solely on proximity to the goal, often disregarding the actual costs involved.

## A\* Search

A\* Search combines the attributes of both Uniform Cost Search and Greedy Best First Search by considering both the costs of each step and a heuristic that estimates the distance to the goal. This dual approach enables more efficient navigation towards the goal state, providing a balanced and effective search methodology.

## Usage

To use this program, follow these steps:

1. **Ensure Java is Installed:** Make sure you have Java 11+ (not headless) installed on your system. You can download and install Java JDK from the [official Oracle website](https://www.oracle.com/id/java/technologies/downloads/).

- If you are using Linux (Java 19):

  sudo apt install openjdk-19-jdk

2. **Clone the Repository:** Clone the repository to your local machine using the following command:

   git clone https://github.com/mybajwk/Tucil3_13522077.git

3. **Change Directory to the Cloned Repository:** Navigate into the cloned repository:

   cd Tucil3_13522077

4. **Auto-Compile and Run the App:**

- If you are using Windows:

  ./run.bat

- If you are using Linux or macOS:

  ./run.sh

5. **Compile the Program Manually:**

- If you are using Windows:

  ./compile.bat

- If you are using Linux or macOS:

  ./compile.sh

5. **Use the program:** Enter start word and end word (must be the same length). Choose the preferred search algorithm. Enjoy the result!

## Creator

This project is part of the assignments for the Algorithm Strategy course at Institut Teknologi Bandung (ITB).

Creator of this project:

- **Enrique Yanuar** (13522077)
