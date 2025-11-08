
***

### Bugs and Conceptual Difficulties Encountered

One of the primary conceptual difficulties was understanding how to properly implement recursive backtracking for maze solving. The key challenge was managing the breadcrumb marking to prevent the patronus from revisiting the same spot and causing infinite loops. Initially, there was confusion about when to set and remove these breadcrumbs, but through iterative debugging and visualization (drawing the maze and patronus movements), I learned the order of operations needed: drop a breadcrumb as soon as the patronus enters a square, and remove it when backtracking.

Another challenge was returning the correct solution path in the directional solver while handling dead ends gracefully. It took effort to convert boolean recursive calls into string concatenations that represent valid directional moves (N, S, E, W) plus handling failure ("X") cases correctly to backtrack.

Managing file I/O to read the maze properly was moderately tricky, specifically ensuring the maze dimensions matched the loaded data and that Harry's and the Cup's positions were accurately detected.

### How I Overcame These Difficulties

I overcame these challenges by:

- Carefully reading and understanding the recursive approach and base/recursive cases.
- Using numerous print statements to trace the patronus's path and backtracking.
- Visualizing the maze and patronus movements on the SimpleCanvas to observe breadcrumbs and correct behavior interactively.
- Writing helper functions like `canMove()` to cleanly isolate legal moves.
- Testing incrementally: first reading and printing the maze, then visual drawing, then boolean solver, and finally directional solver with path reconstruction.
- Referring to example outputs and ensuring my program matched expected behaviors.

### Serious Problems

A more serious problem was synchronizing the graphical updates with recursion pauses to provide a visually understandable exploration animation. It required careful placement of `canvas.pause()` calls and ensuring the canvas was properly refreshed at each move.

Additionally, handling edge cases like dead ends and unsolvable mazes required careful return values and breadcrumb management to avoid endless recursion or wrong conclusions.

### Help Received

I mainly developed this program independently. To better understand backtracking recursion principles, I referred to online articles about backtracking and recursive maze solvers on educational websites.

No direct code was copied; all solutions are original other than conceptual guidance from publicly available explanations on backtracking.

## Challenges Attempted

### Challenge Completion Statement

I successfully completed the challenge problem by implementing an advanced maze solver that finds the **shortest path** when multiple solutions exist. I created three new files for this enhancement:

1. **`MazeChallenge.java`** - An enhanced version of the Maze class that includes a `findShortestPath()` method which explores All possible paths through the maze using exhaustive recursive backtracking, stores each discovered path in an ArrayList, and determines which path has the minimum length.

2. **`RunTournamentChallenge.java`** - A specialized driver class that allows users to choose between the standard solver (which finds the first available path) and the challenge solver (which finds all paths and identifies the shortest one). It also includes path visualization capabilities.

3. **`maze6-challenge.txt`** - A custom-designed maze with multiple valid solutions of varying lengths. I created this maze by strategically removing walls from one of the larger sample mazes to open up alternative pathways from Harry's starting position to the Triwizard Cup.

The challenge implementation differs from the standard solver in several key ways:
- **Complete exploration**: Instead of returning immediately upon finding the first solution, the algorithm continues exploring all branches to discover every possible path
- **Path storage**: Uses an ArrayList to collect all valid solution paths
- **Length comparison**: After exhaustive exploration, compares path lengths to identify the optimal (shortest) solution
- **Visualization**: Provides an option to animate the shortest path on the canvas after all paths have been discovered

Testing with `maze6-challenge.txt` confirmed that my solver correctly identified 4 different paths with lengths ranging from 32 to 40 steps, successfully determining and visualizing the shortest 32-step solution. This demonstrates proper handling of mazes with multiple solutions and validates the shortest-path selection algorithm.

### Feedback

I learned significantly about recursive problem-solving, state management, and graphical debugging through this assignment. The incremental instructions and test scaffolds provided helped me to build and verify each stage reliably.

I enjoyed the combination of textual and graphical outputs to visualize recursive backtracking, making an otherwise abstract algorithm tangible. It was gratifying when the recursion and path tracking finally worked correctly, and the patronus navigated the maze visually.

The assignment was challenging but extremely rewarding, and the blend of recursion, file parsing, and interactive graphics made it a comprehensive learning experience.

***
