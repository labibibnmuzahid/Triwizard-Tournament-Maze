/*
 * RunTournament.java
 * CS 142 Project 4: Triwizard Tournament Maze
 * 
 * Driver class to run the maze solver.
 * 
 * I have neither given nor received unauthorized aid on this program.
 */

import java.util.Scanner;

public class RunTournament {
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Welcome to the Triwizard Tournament Maze Solver!");
        System.out.println();
        
        // Get maze file from user
        System.out.print("Enter maze filename (e.g., maze0.txt): ");
        String filename = scan.nextLine();
        
        // Get pause time from user
        System.out.print("Enter pause time in milliseconds (e.g., 100): ");
        int pauseTime = scan.nextInt();
        scan.nextLine(); // consume newline
        
        // Get solver type from user
        System.out.print("Which solver? (boolean or directional): ");
        String solverType = scan.nextLine().toLowerCase();
        
        System.out.println();
        System.out.println("Initial maze:");
        
        // Create maze and solve
        Maze maze = new Maze(filename, pauseTime);
        maze.printMaze();
        System.out.println();
        
        if (solverType.equals("boolean")) {
            boolean solved = maze.canSolve();
            System.out.println();
            System.out.println("Final maze:");
            maze.printMaze();
            System.out.println();
            if (solved) {
                System.out.println("Maze can be solved!");
            } else {
                System.out.println("Maze cannot be solved.");
            }
        } else if (solverType.equals("directional")) {
            String path = maze.directionalSolve();
            System.out.println();
            System.out.println("Final maze:");
            maze.printMaze();
            System.out.println();
            if (!path.equals("X")) {
                System.out.println("Solution path: " + path);
                System.out.println("Path length: " + path.length());
            } else {
                System.out.println("Maze cannot be solved.");
            }
        } else {
            System.out.println("Invalid solver type. Please enter 'boolean' or 'directional'.");
        }
        
        scan.close();
    }
    
    // Test methods (optional - you can use these for debugging)
    
    public static void testFileReading() {
        Maze m = new Maze("maze0.txt", 100);
        m.printMaze();
    }
    
    public static void testDrawing() {
        Maze m = new Maze("maze1.txt", 100);
        m.drawMaze();
    }
    
    public static void testDrawingPatronus() {
        Maze m = new Maze("maze1.txt", 100);
        m.drawMazeWithPatronus(1, 2);
    }
    
    public static void testBooleanSolver() {
        Maze m = new Maze("maze0.txt", 100);
        boolean result = m.canSolve();
        System.out.println("Can solve: " + result);
    }
}
