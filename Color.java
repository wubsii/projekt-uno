public enum Color {
    RED, BLUE, GREEN, YELLOW, BLACK;

    @Override
    public String toString() {
        switch (this) {
            case RED:    return "Red";
            case BLUE:   return "Blue";
            case GREEN:  return "Green";
            case YELLOW: return "Yellow";
            case BLACK:  return "Black";
            default:     return name(); // Fallback
        }
    }
}