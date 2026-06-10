public enum Color {
    RED, BLUE, GREEN, YELLOW, BLACK;

    // toString-Methode zum Ausgeben der Karten
    @Override
    public String toString() {
        return switch (this) {
            case RED -> "Red";
            case BLUE -> "Blue";
            case GREEN -> "Green";
            case YELLOW -> "Yellow";
            case BLACK -> "Black";
        };
    }
}