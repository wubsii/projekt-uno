public enum Value {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    SKIP(20),
    REVERSE(20),
    PLUS_TWO(20),
    COLOR_CHANGE(50),
    PLUS_FOUR(50);

    @Override
    public String toString() {
        switch (this) {
            case ONE:      return "1";
            case TWO:      return "2";
            case THREE:    return "3";
            case FOUR:     return "4";
            case FIVE:     return "5";
            case SIX:      return "6";
            case SEVEN:    return "7";
            case EIGHT:    return "8";
            case NINE:     return "9";
            case SKIP:     return "Skip";
            case PLUS_TWO: return "+2";
            // Add other cases as needed
            default:       return name(); // Fallback
        }
    }

    // Jeder Value-Wert erhält eine feste Punktzahl
    private final int pointValue;

    // Enum-Konstruktor (wird automatisch aufgerufen)
    Value(int pointValue) {
        this.pointValue = pointValue;
    }

    public int getPointValue() {
        return pointValue;
    }
}