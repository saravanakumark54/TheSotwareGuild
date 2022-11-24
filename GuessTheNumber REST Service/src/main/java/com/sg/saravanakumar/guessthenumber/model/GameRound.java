/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.saravanakumar.guessthenumber.model;

import java.time.LocalTime;
import java.util.Objects;

/**
 *
 * @author saravanakumar
 */
public class GameRound {
    
    private int roundId;
    private int gameId;
    private String guess;
    private LocalTime guessTime;
    private String guessResult;

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public LocalTime getGuessTime() {
        return guessTime;
    }

    public void setGuessTime(LocalTime guessTime) {
        this.guessTime = guessTime;
    }

    public String getGuessResult() {
        return guessResult;
    }

    public void setGuessResult(String guessResult) {
        this.guessResult = guessResult;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.roundId;
        hash = 53 * hash + this.gameId;
        hash = 53 * hash + Objects.hashCode(this.guess);
        hash = 53 * hash + Objects.hashCode(this.guessTime);
        hash = 53 * hash + Objects.hashCode(this.guessResult);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameRound other = (GameRound) obj;
        if (this.roundId != other.roundId) {
            return false;
        }
        if (this.gameId != other.gameId) {
            return false;
        }
        if (!Objects.equals(this.guess, other.guess)) {
            return false;
        }
        if (!Objects.equals(this.guessResult, other.guessResult)) {
            return false;
        }
        return Objects.equals(this.guessTime, other.guessTime);
    }

    @Override
    public String toString() {
        return "GameRound{" + "roundId=" + roundId + ", gameId=" + gameId + ", guess=" + guess + ", guessTime=" + guessTime + ", guessResult=" + guessResult + '}';
    }
    
    
}
