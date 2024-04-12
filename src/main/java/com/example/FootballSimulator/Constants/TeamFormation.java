package com.example.FootballSimulator.Constants;

public enum TeamFormation {
    FOUR_FOUR_TWO ("4-4-2"),
    FOUR_TWO_FOUR ("4-2-4"),
    FOUR_THREE_THREE ("4-3-3"),
    THREE_THREE_FOUR ("3-3-4");
    String label;

    TeamFormation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
