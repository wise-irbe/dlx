# dlx
These are my experiments with dancing links algorithm along with implementations for solving different combinatorial search problems 
and is able to solve and store results for problems with billions of solutions, provided one has the storage, or count after storage limits have exceeded.

This library provides fluent interfaces for building exact cover problems and contains generators for several exact cover problems.
It also contains extensive tracing, solution tree, memory state examination fascilities. 

Most available open source versions use reference based implementations of dancing links algorithm and do not solve the more generic versions of exact covering problems.
Compared to reference based versions, static array implementation are around 50-100x faster and significantly more memory efficient.

It is based on the latest innovations, published in *Fascicle 5c of Knuth, Donald E. The art of computer programming, volume 4B: combinatorial algorithms, part 1*.
Motivation for writing this library was to solve and validate exercises in Donald Knuths fascicle 5c.

**Contents**
* Technical documentation
* Description of exact cover problems, its different flavors, and relevant concepts.

*Quick intro*

**Algorithms** 
Features following implementations of the dancing links algorithm:
* algorithm X 
* algorithm C for colored exact cover problem 

**Implemented exact cover problem features:**
* Secondary items
* Colors
* Sharp preference items
* Ensures that items and options are unique

**Implemented heuristics:**
* Minimum randomized heuristic (choose a random item between items with minimum number of uncovered options)
* Minimum reachable value heuristic (MRV) (choose first item from items with minimum number of uncovered options)
* Sharp preference heuristic (version MRV heuristic, where items prefixed with '#', will be preferred, when we have several alternatives to choose from)

**Current implemented combinatorial search problems:**
* sudoku (see SudokuCommand and SudokuBoardBuilder)
* langford pair (see LangfordPairBuilder)
* NQueen problem
* polyominos (generic implementation with pentomino example)

**Prerequisite:** 
Java 11

**Usage:**

*Building and running the application is simple:*
* In the terminal enter the root path of the project
* Run command `./gradlew` or `gradle.bat` depending MacOs or WindowsOS. It should download any necessary packages and install the gradle tool locally in the project.
* Run command `./gradlew build`. It will build and run tests.
* Solve a sudoku 
```./gradlew run <<< 000000013000700060000509000000400900106000000000000200740000050080000400000010000```, where the sudoku board has been described row by row and unknown cells are replaced with value 0.

*Solving an example exact cover problem*

*Solving sudoku*

*Counting solutions to NQueens problem*

*Tracing*

*Solution tree*

Printing as text

Printing in DOT format

*Fluent API description*

