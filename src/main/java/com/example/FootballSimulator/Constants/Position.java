package com.example.FootballSimulator.Constants;


public enum Position {
    GK("GK"),
    LB("LB"),
    LCB("LCB"),
    CB("CB"),
    RCB("RCB"),
    RB("RB"),
    LM("LM"),
    LCM("LCM"),
    CM("CM"),
    RCM("RCM"),
    RM("RM"),
    RF("RF"),
    LCF("LCF"),
    CF("CF"),
    RCF("RCF"),
    LF("LF");

    private String label;

    Position(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
