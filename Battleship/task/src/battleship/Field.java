package battleship;

import java.util.Scanner;

public class Field {
    private Cell[][] field;
    private boolean gameOver;

    public Field() {
        field = new Cell[10][10];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new Cell();
            }
        }
    }

    public void setShip(Ship ship, Scanner scanner) {
        int[] coordinates = null;
        boolean isCoordinatesValid = false;
        System.out.printf("Enter the coordinates of the %s (%d cells):\n",
                ship.getPrintName(), ship.getLength());
        while (!isCoordinatesValid) {
            coordinates = toDigitCoordinates(scanner.nextLine(), 2);
            try {
                isCoordinatesValid = validateCoordinates(ship, coordinates);
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage() + " Try again:");
            }
        }
        for (int i = Math.min(coordinates[0], coordinates[2]); i <= Math.max(coordinates[0], coordinates[2]); i++) {
            for (int j = Math.min(coordinates[1], coordinates[3]); j <= Math.max(coordinates[1], coordinates[3]); j++) {
                field[i][j].setType(CellType.SHIP);
            }
        }
    }

    public void takeShot(Scanner scanner) {
        int[] coordinates;
        boolean isCoordinatesValid;
        String message;
        //System.out.println("Take a shot!");
        do {
            coordinates = toDigitCoordinates(scanner.nextLine(), 1);
            isCoordinatesValid = true;
            try {
                if (field[coordinates[0]][coordinates[1]].getType() == CellType.SHIP) {
                    field[coordinates[0]][coordinates[1]].setType(CellType.HIT);
                    if (gameOver = checkGameOver()) {
                        message = "You sank the last ship. You won. Congratulations!";
                    } else if (checkSankShip(coordinates[0], coordinates[1])) {
                        message = "You sank a ship! Specify a new target:";
                    } else {
                        message = "You hit a ship! Try again:";
                    }
                } else if (field[coordinates[0]][coordinates[1]].getType() == CellType.HIT) {
                    message = "You hit a ship! Try again:";
                } else {
                    field[coordinates[0]][coordinates[1]].setType(CellType.MISSED);
                    message = "You missed! Try again:";
                }
                field[coordinates[0]][coordinates[1]].setFog(false);
                //print();
                System.out.println(message);
            } catch (ArrayIndexOutOfBoundsException e) {
                isCoordinatesValid = false;
                System.out.println("Error! You entered the wrong coordinates! Try again: ");
            }
        } while (!isCoordinatesValid);
    }

    public void fogging() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j].setFog(true);
            }
        }
    }

    public void unfogging() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j].setFog(false);
            }
        }
    }


    public boolean isGameOver() {
        return gameOver;
    }

    public void print() {
        char letter = 'A';
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < field.length; i++) {
            System.out.print(letter++); // A, B, C and so on.
            for (int j = 0; j < field[i].length; j++) {
                System.out.print(" " + field[i][j]);
            }
            System.out.println();
        }
    }

    private int[] toDigitCoordinates(String coordinates, int countOfCoordinates) {
        int[] result = new int[countOfCoordinates * 2];
        if (countOfCoordinates == 1) {
            result[0] = coordinates.charAt(0) - 65;
            result[1] = Integer.parseInt(coordinates.substring(1)) - 1;
        }
        if (countOfCoordinates == 2) {
            result[0] = coordinates.split(" ")[0].charAt(0) - 65;
            result[1] = Integer.parseInt(coordinates.split(" ")[0].substring(1)) - 1;
            result[2] = coordinates.split(" ")[1].charAt(0) - 65;
            result[3] = Integer.parseInt(coordinates.split(" ")[1].substring(1)) - 1;
        }
        return result;
    }

    private boolean validateCoordinates(Ship ship, int[] coordinates) throws IllegalAccessException {
        boolean isValid = true;

        if ((coordinates[0] == coordinates[2]) == (coordinates[1] == coordinates[3])) {
            throw new IllegalAccessException("Error! Wrong ship location!");
        }
        if (Math.abs((coordinates[0] - coordinates[2]) - (coordinates[1] - coordinates[3])) + 1 != ship.getLength()) {
            throw new IllegalAccessException("Error! Wrong length of the " + ship.getPrintName() + "!");
        }
        for (int i = Math.min(coordinates[0], coordinates[2]); i <= Math.max(coordinates[0], coordinates[2]); i++) {
            for (int j = Math.min(coordinates[1], coordinates[3]); j <= Math.max(coordinates[1], coordinates[3]); j++) {
                if (checkNeighbors(i, j)) {
                    throw new IllegalAccessException("Error! You placed it too close to another one.");
                }
            }
        }

        return isValid;
    }

    private boolean checkSankShip(int coordinate1, int coordinate2) {
        for (int i = coordinate1 - 1; i > 0 && field[i][coordinate2].getType() != CellType.EMPTY &&
                field[i][coordinate2].getType() != CellType.MISSED; i--) {
            if (field[i][coordinate2].getType() == CellType.SHIP) {
                return false;
            }
        }
        for (int i = coordinate1 + 1; i < 9 && field[i][coordinate2].getType() != CellType.EMPTY &&
                field[i][coordinate2].getType() != CellType.MISSED; i++) {
            if (field[i][coordinate2].getType() == CellType.SHIP) {
                return false;
            }
        }
        for (int i = coordinate2 - 1; i > 0 && field[coordinate1][i].getType() != CellType.EMPTY &&
                field[coordinate1][i].getType() != CellType.MISSED; i--) {
            if (field[coordinate1][i].getType() == CellType.SHIP) {
                return false;
            }
        }
        for (int i = coordinate2 + 1; i < 9 && field[coordinate1][i].getType() != CellType.EMPTY &&
                field[coordinate1][i].getType() != CellType.MISSED; i++) {
            if (field[coordinate1][i].getType() == CellType.SHIP) {
                return false;
            }
        }

        return true;
    }

    private boolean checkGameOver() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j].getType() == CellType.SHIP) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkNeighbors(int row, int col) {
        if (field[row][col].getType() == CellType.SHIP) {
            return true;
        }
        if (col > 0) {
            if (field[row][col - 1].getType() == CellType.SHIP) {
                return true;
            }
            if (row > 0) {
                if (field[row - 1][col].getType() == CellType.SHIP) {
                    return true;
                }
                if (field[row - 1][col - 1].getType() == CellType.SHIP) {
                    return true;
                }
            }
            if (row < 9) {
                if (field[row + 1][col].getType() == CellType.SHIP) {
                    return true;
                }
                if (field[row + 1][col - 1].getType() == CellType.SHIP) {
                    return true;
                }
            }
        }
        if (col < 9) {
            if (field[row][col + 1].getType() == CellType.SHIP) {
                return true;
            }
            if (row > 0) {
                if (field[row - 1][col].getType() == CellType.SHIP) {
                    return true;
                }
                if (field[row - 1][col + 1].getType() == CellType.SHIP) {
                    return true;
                }
            }
            if (row < 9) {
                if (field[row + 1][col].getType() == CellType.SHIP) {
                    return true;
                }
                if (field[row + 1][col + 1].getType() == CellType.SHIP) {
                    return true;
                }
            }
        }

        return false;
    }
}