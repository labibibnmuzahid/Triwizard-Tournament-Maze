/*
 * MazeChallenge.java
 * CS 142 Project 4: Challenge - Find Shortest Path in Mazes with Multiple Solutions
 * 
 * This enhanced version finds the SHORTEST path when multiple solutions exist.
 * 
 * I have neither given nor received unauthorized aid on this program.
 */

import java.awt.*;
import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;

public class MazeChallenge {
    
    private SimpleCanvas canvas;
    private char[][] maze;
    private int numRows;
    private int numCols;
    private int harryRow;
    private int harryCol;
    private int cupRow;
    private int cupCol;
    private int pauseTime;
    private int callCount;
    private final int SQUARE_SIZE = 30;
    
    // For shortest path tracking
    private ArrayList<String> allPaths; // stores all valid paths found
    private String shortestPath; // the shortest path found
    
    public MazeChallenge(String filename, int pauseTime) {
        this.pauseTime = pauseTime;
        this.callCount = 0;
        this.allPaths = new ArrayList<>();
        this.shortestPath = null;
        loadMaze(filename);
        canvas = new SimpleCanvas(numCols * SQUARE_SIZE, numRows * SQUARE_SIZE);
        canvas.show();
    }
    
    private void loadMaze(String filename) {
        InputStream is = MazeChallenge.class.getResourceAsStream(filename);
        if (is == null) {
            System.err.println("Could not open file: " + filename);
            System.exit(1);
        }
        
        Scanner scan = new Scanner(is);
        numRows = scan.nextInt();
        numCols = scan.nextInt();
        scan.nextLine();
        
        maze = new char[numRows][numCols];
        
        for (int row = 0; row < numRows; row++) {
            String line = scan.nextLine();
            for (int col = 0; col < numCols && col < line.length(); col++) {
                maze[row][col] = line.charAt(col);
                
                if (maze[row][col] == 'H') {
                    harryRow = row;
                    harryCol = col;
                }
                
                if (maze[row][col] == 'C') {
                    cupRow = row;
                    cupCol = col;
                }
            }
        }
        scan.close();
    }
    
    public void printMaze() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                System.out.print(maze[row][col]);
            }
            System.out.println();
        }
    }
    
    public void drawMaze() {
        canvas.clear();
        
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int x = col * SQUARE_SIZE;
                int y = row * SQUARE_SIZE;
                
                if (maze[row][col] == '#') {
                    canvas.setPenColor(Color.GRAY);
                    canvas.drawFilledRectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                } else if (maze[row][col] == 'C') {
                    canvas.setPenColor(Color.WHITE);
                    canvas.drawFilledRectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                    canvas.setPenColor(Color.ORANGE);
                    canvas.drawFilledCircle(x + SQUARE_SIZE/2, y + SQUARE_SIZE/2, SQUARE_SIZE/3);
                } else if (maze[row][col] == 'H') {
                    canvas.setPenColor(Color.WHITE);
                    canvas.drawFilledRectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                    canvas.setPenColor(Color.RED);
                    canvas.drawFilledCircle(x + SQUARE_SIZE/2, y + SQUARE_SIZE/2, SQUARE_SIZE/3);
                } else if (maze[row][col] == '.') {
                    canvas.setPenColor(Color.WHITE);
                    canvas.drawFilledRectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                    canvas.setPenColor(Color.BLUE);
                    canvas.drawFilledCircle(x + SQUARE_SIZE/2, y + SQUARE_SIZE/2, SQUARE_SIZE/6);
                } else {
                    canvas.setPenColor(Color.WHITE);
                    canvas.drawFilledRectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                }
                
                canvas.setPenColor(Color.LIGHT_GRAY);
                canvas.drawRectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
        
        canvas.update();
    }
    
    public void drawMazeWithPatronus(int patronusRow, int patronusCol) {
        drawMaze();
        
        int x = patronusCol * SQUARE_SIZE;
        int y = patronusRow * SQUARE_SIZE;
        canvas.setPenColor(Color.GREEN);
        canvas.drawFilledCircle(x + SQUARE_SIZE/2, y + SQUARE_SIZE/2, SQUARE_SIZE/4);
        
        canvas.update();
    }
    
    /**
     * Original directional solver (finds first solution, not necessarily shortest)
     */
    public String directionalSolve() {
        System.out.println("Harry is located at row " + harryRow + ", col " + harryCol);
        drawMaze();
        canvas.pause(pauseTime);
        
        callCount = 0;
        String result = directionalSolveHelper(harryRow, harryCol);
        
        System.out.println("\nTotal calls to directionalSolve: " + callCount);
        
        if (!result.equals("X") && result.endsWith("C")) {
            result = result.substring(0, result.length() - 1);
        }
        
        return result;
    }
    
    private String directionalSolveHelper(int patronusRow, int patronusCol) {
        callCount++;
        
        System.out.println("Patronus enters row " + patronusRow + ", col " + patronusCol);
        drawMazeWithPatronus(patronusRow, patronusCol);
        canvas.pause(pauseTime);
        
        if (patronusRow == cupRow && patronusCol == cupCol) {
            return "C";
        }
        
        char originalChar = maze[patronusRow][patronusCol];
        maze[patronusRow][patronusCol] = '.';
        
        // Try NORTH
        if (canMove(patronusRow - 1, patronusCol)) {
            String result = directionalSolveHelper(patronusRow - 1, patronusCol);
            if (!result.equals("X")) {
                maze[patronusRow][patronusCol] = originalChar;
                return "N" + result;
            }
        }
        
        // Try SOUTH
        if (canMove(patronusRow + 1, patronusCol)) {
            String result = directionalSolveHelper(patronusRow + 1, patronusCol);
            if (!result.equals("X")) {
                maze[patronusRow][patronusCol] = originalChar;
                return "S" + result;
            }
        }
        
        // Try EAST
        if (canMove(patronusRow, patronusCol + 1)) {
            String result = directionalSolveHelper(patronusRow, patronusCol + 1);
            if (!result.equals("X")) {
                maze[patronusRow][patronusCol] = originalChar;
                return "E" + result;
            }
        }
        
        // Try WEST
        if (canMove(patronusRow, patronusCol - 1)) {
            String result = directionalSolveHelper(patronusRow, patronusCol - 1);
            if (!result.equals("X")) {
                maze[patronusRow][patronusCol] = originalChar;
                return "W" + result;
            }
        }
        
        maze[patronusRow][patronusCol] = originalChar;
        
        System.out.println("Patronus backtracks from row " + patronusRow + ", col " + patronusCol);
        drawMazeWithPatronus(patronusRow, patronusCol);
        canvas.pause(pauseTime);
        
        return "X";
    }
    
    /**
     * CHALLENGE: Find the SHORTEST path when multiple solutions exist
     */
    public String findShortestPath() {
        System.out.println("Finding ALL possible paths to determine the shortest...");
        System.out.println("Harry is located at row " + harryRow + ", col " + harryCol);
        drawMaze();
        canvas.pause(pauseTime);
        
        allPaths.clear();
        shortestPath = null;
        callCount = 0;
        
        // Find ALL paths
        findAllPaths(harryRow, harryCol, "");
        
        System.out.println("\n=== RESULTS ===");
        System.out.println("Total calls made: " + callCount);
        System.out.println("Total paths found: " + allPaths.size());
        
        if (allPaths.size() > 0) {
            System.out.println("\nAll paths discovered:");
            for (int i = 0; i < allPaths.size(); i++) {
                String path = allPaths.get(i);
                System.out.println("  Path " + (i+1) + ": " + path + " (length: " + path.length() + ")");
            }
            
            // Find shortest
            shortestPath = allPaths.get(0);
            for (String path : allPaths) {
                if (path.length() < shortestPath.length()) {
                    shortestPath = path;
                }
            }
            
            System.out.println("\nShortest path: " + shortestPath);
            System.out.println("Shortest path length: " + shortestPath.length());
            
            return shortestPath;
        } else {
            System.out.println("No solution found!");
            return "X";
        }
    }
    
    /**
     * Recursive helper that finds ALL possible paths to the cup
     */
    private void findAllPaths(int patronusRow, int patronusCol, String pathSoFar) {
        callCount++;
        
        // Visualize (optional - can be slow for complex mazes)
        if (pauseTime > 0) {
            drawMazeWithPatronus(patronusRow, patronusCol);
            canvas.pause(pauseTime);
        }
        
        // Base case: found the cup!
        if (patronusRow == cupRow && patronusCol == cupCol) {
            allPaths.add(pathSoFar);
            System.out.println("Found path: " + pathSoFar + " (length: " + pathSoFar.length() + ")");
            return;
        }
        
        // Drop breadcrumb
        char originalChar = maze[patronusRow][patronusCol];
        maze[patronusRow][patronusCol] = '.';
        
        // Try all four directions and continue exploring even after finding a path
        if (canMove(patronusRow - 1, patronusCol)) {
            findAllPaths(patronusRow - 1, patronusCol, pathSoFar + "N");
        }
        
        if (canMove(patronusRow + 1, patronusCol)) {
            findAllPaths(patronusRow + 1, patronusCol, pathSoFar + "S");
        }
        
        if (canMove(patronusRow, patronusCol + 1)) {
            findAllPaths(patronusRow, patronusCol + 1, pathSoFar + "E");
        }
        
        if (canMove(patronusRow, patronusCol - 1)) {
            findAllPaths(patronusRow, patronusCol - 1, pathSoFar + "W");
        }
        
        // Pick up breadcrumb (backtrack)
        maze[patronusRow][patronusCol] = originalChar;
    }
    
    private boolean canMove(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return false;
        }
        
        if (maze[row][col] == '#' || maze[row][col] == '.') {
            return false;
        }
        
        return true;
    }
    
    /**
     * Visualize the shortest path on the maze
     */
    public void visualizeShortestPath(String path) {
        if (path == null || path.equals("X")) {
            System.out.println("No path to visualize!");
            return;
        }
        
        // Reset maze to clear old breadcrumbs
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (maze[row][col] == '.') {
                    maze[row][col] = ' ';
                }
            }
        }
        
        System.out.println("\nVisualizing shortest path...");
        
        int currentRow = harryRow;
        int currentCol = harryCol;
        
        for (int i = 0; i < path.length(); i++) {
            char direction = path.charAt(i);
            
            // Mark current position with breadcrumb
            if (maze[currentRow][currentCol] != 'H' && maze[currentRow][currentCol] != 'C') {
                maze[currentRow][currentCol] = '.';
            }
            
            drawMazeWithPatronus(currentRow, currentCol);
            canvas.pause(pauseTime * 2);
            
            // Move in the direction
            switch (direction) {
                case 'N': currentRow--; break;
                case 'S': currentRow++; break;
                case 'E': currentCol++; break;
                case 'W': currentCol--; break;
            }
        }
        
        // Final position
        drawMazeWithPatronus(currentRow, currentCol);
        canvas.pause(pauseTime * 2);
        
        System.out.println("\nFinal maze with shortest path:");
        printMaze();
    }
}
