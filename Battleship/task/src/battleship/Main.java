package battleship;

import java.util.Scanner;

public class Main {
    static final Scanner scanner = new Scanner(System.in);
    static String currentPlayer = "Player 1";

    public static void main(String[] args) {


        Field field1 = new Field();
        Field field2 = new Field();

        System.out.println(currentPlayer + ", place your ships to the game field");
        field1.print();
        for (Ship ship : Ship.values()) {
            field1.setShip(ship, scanner);
            field1.print();
        }
        changePlayer();

        System.out.println(currentPlayer + ", place your ships to the game field");
        field2.print();
        for (Ship ship : Ship.values()) {
            field2.setShip(ship, scanner);
            field2.print();
        }
        changePlayer();

        System.out.println("The game starts!");

        while (!field1.isGameOver() && !field2.isGameOver()) {
            System.out.println(currentPlayer + ", it's your turn:");
            if (currentPlayer.equals("Player 1")) {
                field1.unfogging();
                field2.fogging();
                field2.print();
                System.out.println("---------------------");
                field1.print();
                field2.takeShot(scanner);
            } else {
                field2.unfogging();
                field1.fogging();
                field1.print();
                System.out.println("---------------------");
                field2.print();
                field1.takeShot(scanner);
            }
            if (!field1.isGameOver() && !field2.isGameOver()) {
                changePlayer();
            }
        }

        scanner.close();
    }

    private static void changePlayer() {
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        currentPlayer = currentPlayer.equals("Player 1") ? "Player 2" : "Player 1";
    }
}







