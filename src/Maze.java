/*
 * Maze.java
 * CS 142 Project 4: Triwizard Tournament Maze
 * 
 * This class represents a maze that can be solved using recursive backtracking.
 * The maze is loaded from a text file and can be solved in two ways:
 * 1. Boolean solver - determines if the maze can be solved
 * 2. Directional solver - finds the actual path through the maze
 * 
 * I have neither given nor received unauthorized aid on this program.
 */

import java.awt.*;
import java.io.InputStream;
import java.util.Scanner;

public class Maze {
    
    private SimpleCanvas canvas; // the canvas the maze will be drawn on
    private char[][] maze; // 2D array to store the maze
    private int numRows; // number of rows in the maze
    private int numCols; // number of columns in the maze
    private int harryRow; // Harry's starting row
    private int harryCol; // Harry's starting column
    private int cupRow; // Cup's row
    private int cupCol; // Cup's column
    private int pauseTime; // milliseconds to pause between moves
    private int callCount; // number of recursive calls made
    private final int SQUARE_SIZE = 30; // size of each square in pixels
    
    /**
     * Constructor that takes a filename and pause time
     */
    public Maze(String filename, int pauseTime) {
        this.pauseTime = pauseTime;
        this.callCount = 0;
        loadMaze(filename);
        canvas = new SimpleCanvas(numCols * SQUARE_SIZE, numRows * SQUARE_SIZE);
        canvas.show();
    }
    
    /**
     * Load a maze from the given text file and store into appropriate instance variables.
     */
    private void loadMaze(String filename) {
        InputStream is = Maze.class.getResourceAsStream(filename);
        if (is == null) {
            System.err.println("Could not open file: " + filename);
            System.exit(1);
        }
        
        Scanner scan = new Scanner(is);
        
        // Read the number of rows and columns
        numRows = scan.nextInt();
        numCols = scan.nextInt();
        scan.nextLine(); // skip the enter keypress
        
        // Initialize the maze array
        maze = new char[numRows][numCols];
        
        // Read each line of the maze
        for (int row = 0; row < numRows; row++) {
            String line = scan.nextLine();
            for (int col = 0; col < numCols && col < line.length(); col++) {
                maze[row][col] = line.charAt(col);
                
                // Find Harry's location
                if (maze[row][col] == 'H') {
                    harryRow = row;
                    harryCol = col;
                }
                
                // Find the Cup's location
                if (maze[row][col] == 'C') {
                    cupRow = row;
                    cupCol = col;
                }
            }
        }
        scan.close();
    }
    
    /**
     * Print the maze in text format (for debugging)
     */
    public void printMaze() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                System.out.print(maze[row][col]);
            }
            System.out.println();
        }
    }
    
    /**
     * Draw the maze on the canvas.
     */
    public void drawMaze() {
        canvas.clear();
        
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int x = col * SQUARE_SIZE;
                int y = row * SQUARE_SIZE;
                
                // Choose color based on what's in the square
                if (maze[row][col] == '#') {
                    // Hedge - gray
                    canvas.setPenColor(Color.GRAY);
                    canvas.drawFilledRectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                } else if (maze[row][col] == 'C') {
                    // Cup - orange
                    canvas.setPenColor(Color.WHITE);
                    canvas.drawFilledRectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                    canvas.setPenColor(Color.ORANGE);
                    canvas.drawFilledCircle(x + SQUARE_SIZE/2, y + SQUARE_SIZE/2, SQUARE_SIZE/3);
                } else if (maze[row][col] == 'H') {
                    // Harry's starting position - red
                    canvas.setPenColor(Color.WHITE);
                    canvas.drawFilledRectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                    canvas.setPenColor(Color.RED);
                    canvas.drawFilledCircle(x + SQUARE_SIZE/2, y + SQUARE_SIZE/2, SQUARE_SIZE/3);
                } else if (maze[row][col] == '.') {
                    // Breadcrumb - white square with blue dot
                    canvas.setPenColor(Color.WHITE);
                    canvas.drawFilledRectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                    canvas.setPenColor(Color.BLUE);
                    canvas.drawFilledCircle(x + SQUARE_SIZE/2, y + SQUARE_SIZE/2, SQUARE_SIZE/6);
                } else {
                    // Open space - white
                    canvas.setPenColor(Color.WHITE);
                    canvas.drawFilledRectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                }
                
                // Draw grid lines
                canvas.setPenColor(Color.LIGHT_GRAY);
                canvas.drawRectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
        
        canvas.update();
    }
    
    /**
     * Draw the maze on the canvas, including the current position of the patronus.
     */
    public void drawMazeWithPatronus(int patronusRow, int patronusCol) {
        drawMaze();
        
        // Draw the patronus as a green dot
        int x = patronusCol * SQUARE_SIZE;
        int y = patronusRow * SQUARE_SIZE;
        canvas.setPenColor(Color.GREEN);
        canvas.drawFilledCircle(x + SQUARE_SIZE/2, y + SQUARE_SIZE/2, SQUARE_SIZE/4);
        
        canvas.update();
    }
    
    /**
     * Initial function to get the recursion started for recursive formulation 1:
     * Is it possible to solve this maze?
     */
    public boolean canSolve() {
        System.out.println("Harry is located at row " + harryRow + ", col " + harryCol);
        drawMaze();
        canvas.pause(pauseTime);
        
        callCount = 0; // reset call count
        boolean result = canSolve(harryRow, harryCol);
        
        System.out.println("\nTotal calls to canSolve: " + callCount);
        return result;
    }
    
    /**
     * Helper function for canSolve that takes the current position of the patronus as parameters.
     */
    private boolean canSolve(int patronusRow, int patronusCol) {
        callCount++;
        
        System.out.println("Patronus enters row " + patronusRow + ", col " + patronusCol);
        drawMazeWithPatronus(patronusRow, patronusCol);
        canvas.pause(pauseTime);
        
        // Base case: Are we at the cup?
        if (patronusRow == cupRow && patronusCol == cupCol) {
            return true;
        }
        
        // Drop a breadcrumb
        char originalChar = maze[patronusRow][patronusCol];
        maze[patronusRow][patronusCol] = '.';
        
        // Try NORTH
        if (canMove(patronusRow - 1, patronusCol)) {
            if (canSolve(patronusRow - 1, patronusCol)) {
                return true;
            }
        }
        
        // Try SOUTH
        if (canMove(patronusRow + 1, patronusCol)) {
            if (canSolve(patronusRow + 1, patronusCol)) {
                return true;
            }
        }
        
        // Try EAST
        if (canMove(patronusRow, patronusCol + 1)) {
            if (canSolve(patronusRow, patronusCol + 1)) {
                return true;
            }
        }
        
        // Try WEST
        if (canMove(patronusRow, patronusCol - 1)) {
            if (canSolve(patronusRow, patronusCol - 1)) {
                return true;
            }
        }
        
        // Dead end - pick up breadcrumb
        maze[patronusRow][patronusCol] = originalChar;
        
        System.out.println("Patronus backtracks from row " + patronusRow + ", col " + patronusCol);
        drawMazeWithPatronus(patronusRow, patronusCol);
        canvas.pause(pauseTime);
        
        return false;
    }
    
    /**
     * Check if the patronus can move to a given position
     */
    private boolean canMove(int row, int col) {
        // Check bounds
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return false;
        }
        
        // Check if it's a wall or breadcrumb
        if (maze[row][col] == '#' || maze[row][col] == '.') {
            return false;
        }
        
        // Can move to open space, Harry's position, or Cup
        return true;
    }
    
    /**
     * Initial function to get the recursion started for recursive formulation 2:
     * What are the directions (sequence of N/S/E/W steps) to solve the maze?
     */
    public String directionalSolve() {
        System.out.println("Harry is located at row " + harryRow + ", col " + harryCol);
        drawMaze();
        canvas.pause(pauseTime);
        
        callCount = 0; // reset call count
        String result = directionalSolve(harryRow, harryCol);
        
        System.out.println("\nTotal calls to directionalSolve: " + callCount);
        
        // Remove the 'C' at the end if the maze was solved
        if (!result.equals("X") && result.endsWith("C")) {
            result = result.substring(0, result.length() - 1);
        }
        
        return result;
    }
    
    /**
     * Helper function for directionalSolve().
     */
    private String directionalSolve(int patronusRow, int patronusCol) {
        callCount++;
        
        System.out.println("Patronus enters row " + patronusRow + ", col " + patronusCol);
        drawMazeWithPatronus(patronusRow, patronusCol);
        canvas.pause(pauseTime);
        
        // Base case: Are we at the cup?
        if (patronusRow == cupRow && patronusCol == cupCol) {
            return "C";
        }
        
        // Drop a breadcrumb
        char originalChar = maze[patronusRow][patronusCol];
        maze[patronusRow][patronusCol] = '.';
        
        // Try NORTH
        if (canMove(patronusRow - 1, patronusCol)) {
            String result = directionalSolve(patronusRow - 1, patronusCol);
            if (!result.equals("X")) {
                return "N" + result;
            }
        }
        
        // Try SOUTH
        if (canMove(patronusRow + 1, patronusCol)) {
            String result = directionalSolve(patronusRow + 1, patronusCol);
            if (!result.equals("X")) {
                return "S" + result;
            }
        }
        
        // Try EAST
        if (canMove(patronusRow, patronusCol + 1)) {
            String result = directionalSolve(patronusRow, patronusCol + 1);
            if (!result.equals("X")) {
                return "E" + result;
            }
        }
        
        // Try WEST
        if (canMove(patronusRow, patronusCol - 1)) {
            String result = directionalSolve(patronusRow, patronusCol - 1);
            if (!result.equals("X")) {
                return "W" + result;
            }
        }
        
        // Dead end - pick up breadcrumb
        maze[patronusRow][patronusCol] = originalChar;
        
        System.out.println("Patronus backtracks from row " + patronusRow + ", col " + patronusCol);
        drawMazeWithPatronus(patronusRow, patronusCol);
        canvas.pause(pauseTime);
        
        return "X";
    }
}
