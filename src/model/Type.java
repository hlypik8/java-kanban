package model;

public enum Type {
    TASK,
    EPIC,
    SUBTASK;

    @Override
    public String toString() {
        return switch (this.ordinal()) {
            case 0 -> "TASK";
            case 1 -> "EPIC";
            case 2 -> "SUBTASK";
            default -> null;
        };
    }
}
