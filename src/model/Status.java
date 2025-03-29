package model;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;

    @Override
    public String toString() {
        return switch (this.ordinal()) {
            case 0 -> "NEW";
            case 1 -> "IN_PROGRESS";
            case 2 -> "DONE";
            default -> null;
        };
    }
}
