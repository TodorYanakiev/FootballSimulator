package com.example.FootballSimulator;

import com.example.FootballSimulator.Constants.Position;
import com.example.FootballSimulator.FootballPlayer.FootballPlayer;

import java.util.Random;

public class TransferSumCalculator {
    public Integer getTransferSumForPlayer(FootballPlayer player) {
        Double transferSum = 0.0;
        Byte overall = calculatePlayerOverall(player, player.getBaseFootballPlayer().getPosition());
        transferSum = (double)1000000 * overall;
        if (overall < 60) {
            transferSum /= 100;
        } else if (overall < 70) {
            transferSum /= 10;
            transferSum /= 3;
        } else if (overall < 80) {
            transferSum /= 10;
            transferSum *= 3;
        } else if (overall < 90) {
            transferSum /= 1.3;
        } else {
            transferSum *= 1.3;
        }
        Random rand = new Random();
        int plusOrMinus = rand.nextInt();
        double percent = rand.nextDouble(0, 0.16);
        if (plusOrMinus %2 == 0 ) {
            transferSum -= transferSum * percent;
        } else {
            transferSum += transferSum * percent;
        }
        Integer sum = transferSum.intValue();
        return sum;
    }

    public Byte calculatePlayerOverall(FootballPlayer player, Position position) {
        Byte overall = 0;
        if (position.equals(Position.GK)) {
            overall = (byte) (0.9 * (player.getGoalkeeping()) + 0.05 * player.getPositioning() + 0.05 * player.getPassing());
        } else if (position.equals(Position.LB) || position.equals(Position.RB)) {
            overall = (byte) (0.2 * player.getSpeed() + 0.1 * player.getStamina() + 0.15 * player.getDribble() +
                    0.05 * player.getScoring() + 0.2 * player.getDefending() + 0.1 * player.getPositioning()
                    + 0.2 * player.getPassing());
        } else if (position.equals(Position.CB) || position.equals(Position.LCB) || position.equals(Position.RCB)) {
            overall = (byte) (0.05 * player.getSpeed() + 0.1 * player.getStamina() + 0.05 * player.getScoring() +
                    0.4 * player.getDefending() + 0.25 * player.getPositioning() + 0.15 * player.getPassing());
        } else if (position.equals(Position.LM) || position.equals(Position.RM)) {
            overall = (byte) (0.15 * player.getSpeed() + 0.15 * player.getStamina() + 0.15 * player.getDribble() +
                    0.1 * player.getScoring() + 0.05 * player.getDefending() + 0.15 * player.getPositioning()
                    + 0.25 * player.getPassing());
        } else if (position.equals(Position.CM) || position.equals(Position.LCM) || position.equals(Position.RCM)) {
            overall = (byte) (0.05 * player.getSpeed() + 0.1 * player.getStamina() + 0.1 * player.getDribble() +
                    0.1 * player.getScoring() + 0.1 * player.getDefending() + 0.25 * player.getPositioning()
                    + 0.3 * player.getPassing());
        } else if (position.equals(Position.LF) || position.equals(Position.RF)) {
            overall = (byte) (0.25 * player.getSpeed() + 0.15 * player.getStamina() + 0.2 * player.getDribble() +
                    0.1 * player.getScoring() + 0.1 * player.getPositioning() + 0.2 * player.getPassing());
        } else if (position.equals(Position.CF) || position.equals(Position.LCF) || position.equals(Position.RCF)) {
            overall = (byte) (0.1 * player.getSpeed() + 0.1 * player.getStamina() + 0.15 * player.getDribble() +
                    0.35 * player.getScoring() + 0.2 * player.getPositioning() + 0.1 * player.getPassing());
        }
        return overall;
    }
}