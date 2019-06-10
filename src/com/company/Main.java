package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void printOcean(String[][] ocean) {
        System.out.println("   0123456789   ");
        for(int i = 0; i < ocean.length; i++) {
            String row = i + " |";
            for(int j = 0; j < ocean.length; j++) {
                row += ocean[i][j];
            }
            row += "| " + i;
            System.out.println(row);
        }
        System.out.println("   0123456789   ");
    }

    public static boolean shipsContains(int x, int y, int[][] ships) {
        for(int i = 0; i < ships.length; i++) {
            if(ships[i][0] == x && ships[i][1] == y) {
                return true;
            }
        }
        return false;
    }

    public static int[] getPlayerShips(Scanner input, int[][] playerShips, int index) {
        int indexOffset = index + 1;
        int x, y;
        while(true) {
            System.out.print("Enter X coordinate for ship # " + indexOffset);
            x = input.nextInt();
            System.out.print("Enter Y coordinate for ship # " + indexOffset);
            y = input.nextInt();
            if(shipsContains(x, y, playerShips)) {
                System.out.println("There's already a ship there, try again");
            } else if(x < 0 || x > 9 || y < 0 || y > 9) {
                System.out.println("Invalid coordinates, try again");
            } else {
                break;
            }
        }
        return new int[] {x, y};
    }

    public static int randint(int min, int max) {
        int range = max - min + 1;
        return (int) (Math.random() * range) + min;
    }

    public static int[] getComputerShips(int[][] playerShips, int[][] computerShips, int index) {
        int x, y;
        while(true) {
            x = randint(0, 9);
            y = randint(0, 9);
            if(!shipsContains(x, y, playerShips) && !shipsContains(x, y, computerShips)) {
                break;
            }
        }
        System.out.println(++index + ". ship DEPLOYED");
        return new int[] {x, y};
    }

    public static int[] getPlayerGuess(Scanner input) {
        int x, y;
        while(true) {
            System.out.println("Enter X coordinate: ");
            x = input.nextInt();
            System.out.println("Enter Y coordinate: ");
            y = input.nextInt();
            if(x < 0 || x > 9 || y < 0 || y > 9) {
                System.out.println("Invalid coordinates, try again");
            } else {
                break;
            }
        }
        return new int[] {x, y};
    }

    public static Integer[] getComputerGuess(ArrayList<Integer[]> guesses) {
        Integer x, y;
        Integer[] guess;
        while(true) {
            x = randint(0,9);
            y = randint(0,9);
            guess = new Integer[] { x, y};
            if(!guesses.contains(guess)) {
                break;
            }
        }
        return guess;
    }

    public static void main(String[] args) {
	    var input = new Scanner(System.in);
	    var ocean = new String[10][10];
	    for(int i = 0; i < ocean.length; i++) {
	        for(int j = 0; j < ocean[i].length; j++) {
	            ocean[i][j] = " ";
            }
        }
        int playerShipsRemaining = 5;
        int computerShipsRemaining = 5;
	    var playerShips = new int[playerShipsRemaining][2];
	    var computerShips = new int[computerShipsRemaining][2];

        System.out.println("Deploy Ships");
        for(int i = 0; i < playerShips.length; i++) {
            var shipPosition = getPlayerShips(input, playerShips, i);
            playerShips[i][0] = shipPosition[0];
            playerShips[i][1] = shipPosition[1];
        }
        System.out.println("Player has deployed ships!");
        System.out.println("Computer is deploying ships!");
        for(int i = 0; i < computerShips.length; i++) {
            var shipPosition = getComputerShips(playerShips, computerShips, i);
            computerShips[i][0] = shipPosition[0];
            computerShips[i][1] = shipPosition[1];
        }
        System.out.println("Computer has deployed ships!");
        for(int i = 0; i < playerShips.length; i++) {
            int x = playerShips[i][0];
            int y = playerShips[i][1];
            ocean[x][y] = "@";
        }
        printOcean(ocean);
        boolean playerTurn = true;
        var computerGuesses = new ArrayList<Integer[]>();
        while(playerShipsRemaining > 0 && computerShipsRemaining > 0) {
            String marker;
            if(playerTurn) {
                System.out.println("Your turn!");
                int[] guess = getPlayerGuess(input);
                if(shipsContains(guess[0], guess[1], playerShips)) {
                    System.out.println("Oh no! You sunk your own ship!");
                    playerShipsRemaining -= 1;
                    marker = "x";
                } else if(shipsContains(guess[0], guess[1], computerShips)) {
                    System.out.println("Boom!  You sunk the ship!");
                    computerShipsRemaining -= 1;
                    marker = "!";
                } else {
                    System.out.println("You missed");
                    marker = "-";
                }
                ocean[guess[0]][guess[1]] = marker;
                playerTurn = false;
            } else {
                System.out.println("COMPUTER'S TURN");
                Integer[] guess = getComputerGuess(computerGuesses);
                computerGuesses.add(guess);
                if(shipsContains(guess[0], guess[1], playerShips)) {
                    System.out.println("The computer sunk one of your ships!");
                    playerShipsRemaining -= 1;
                    marker = "x";
                } else if(shipsContains(guess[0], guess[1], computerShips)) {
                    System.out.println("The computer sunk one of its own ships");
                    computerShipsRemaining -= 1;
                    marker = "!";
                } else {
                    System.out.println("Computer missed");
                    marker = " ";
                }
                ocean[guess[0]][guess[1]] = marker;
                playerTurn = true;
            }
            printOcean(ocean);
            String shipsMessage = "Computer ships: " + computerShipsRemaining;
            shipsMessage += "| Player ships: " + playerShipsRemaining;
            System.out.println(shipsMessage);
        }
        if(playerShipsRemaining == 0) {
            System.out.println("The computer wins");
        } else {
            System.out.println("Hooray!  You win the battle!");
        }
        input.nextInt();
        input.close();
    }
}
