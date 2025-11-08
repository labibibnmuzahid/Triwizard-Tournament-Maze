/*
 * RunTournamentChallenge.java
 * CS 142 Project 4: Challenge Driver
 * 
 * Driver class to run the challenge maze solver that finds the shortest path
 * when multiple solutions exist.
 * 
 * I have neither given nor received unauthorized aid on this program.
 */

import java.util.Scanner;

public class RunTournamentChallenge {
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        
        System.out.println("===========================================");
        System.out.println("Triwizard Tournament Maze Solver CHALLENGE");
        System.out.println("Finding the SHORTEST path in mazes with multiple solutions");
        System.out.println("===========================================");
        System.out.println();
        
        System.out.print("Enter maze filename (e.g., maze-challenge.txt): ");
        String filename = scan.nextLine();
        
        System.out.print("Enter pause time in milliseconds (0 for no animation, 50-100 recommended): ");
        int pauseTime = scan.nextInt();
        scan.nextLine();
        
        System.out.println();
        System.out.println("Initial maze:");
        
        MazeChallenge maze = new MazeChallenge(filename, pauseTime);
        maze.printMaze();
        System.out.println();
        
        System.out.println("Choose solver:");
        System.out.println("1. Standard directional solver (finds first path)");
        System.out.println("2. Challenge solver (finds ALL paths, determines shortest)");
        System.out.print("Enter choice (1 or 2): ");
        int choice = scan.nextInt();
        scan.nextLine();
        
        System.out.println();
        System.out.println("========================================");
        
        if (choice == 1) {
            // Standard solver
            String path = maze.directionalSolve();
            System.out.println();
            System.out.println("Final maze:");
            maze.printMaze();
            System.out.println();
            if (!path.equals("X")) {
                System.out.println("Solution path (first found): " + path);
                System.out.println("Path length: " + path.length());
            } else {
                System.out.println("Maze cannot be solved.");
            }
        } else if (choice == 2) {
            // Challenge solver - finds all paths and shortest
            String shortestPath = maze.findShortestPath();
            
            if (!shortestPath.equals("X")) {
                System.out.println("\n========================================");
                System.out.print("\nVisualize shortest path? (yes/no): ");
                String visualize = scan.nextLine();
                
                if (visualize.equalsIgnoreCase("yes") || visualize.equalsIgnoreCase("y")) {
                    maze.visualizeShortestPath(shortestPath);
                }
            }
        } else {
            System.out.println("Invalid choice!");
        }
        
        System.out.println("\n========================================");
        System.out.println("Challenge complete!");
        
        scan.close();
    }
}
