/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sg.saravanakumar.guessthenumber.service;

import com.sg.saravanakumar.guessthenumber.model.Game;
import com.sg.saravanakumar.guessthenumber.model.GameRound;
import java.util.List;

/**
 *
 * @author saravanakumar
 */
public interface GuessTheNumberService {
    
    int createAndAddGame();
    GameRound submitGuess(String guess, Game game);
    List<Game> getAllGames();
    Game getGame(int gameId);
    List<GameRound> getRounds(Game game);
}
