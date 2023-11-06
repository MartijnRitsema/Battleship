package battleship;

public class Cell {
    private CellType type = CellType.EMPTY;
    private boolean isFog = false;

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public boolean isFog() {
        return isFog;
    }

    public void setFog(boolean fog) {
        isFog = fog;
    }

    @Override
    public String toString() {
        if (isFog) {
            return "~";
        } else {
            return type.getSymbol();
        }
    }
}