
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

        // Jeder Value-Wert erhält eine feste Punktzahl
        private final int punktwert;

        // Enum-Konstruktor (wird automatisch aufgerufen)
        Value(int punktwert) {
            this.punktwert = punktwert;
        }

        public int getPunktwert() {
            return punktwert;
        }
    }
