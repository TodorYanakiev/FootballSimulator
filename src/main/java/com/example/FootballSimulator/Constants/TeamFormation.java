package com.example.FootballSimulator.Constants;

public enum TeamFormation {
    FOUR_FOUR_TWO ("442"),
    FOUR_TWO_FOUR ("424"),
    FOUR_THREE_THREE ("433"),
    THREE_THREE_FOUR ("334"),
    THREE_FOUR_THREE ("343"),
    THREE_FIVE_TWO ("352"),
    FIVE_FOUR_ONE ("541"),
    FIVE_THREE_TWO ("532");

    private String label;

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
