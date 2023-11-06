package battleship;

public enum CellType {
    EMPTY("~"), SHIP("O"), HIT("X"), MISSED("M");

    private String symbol;

    CellType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}