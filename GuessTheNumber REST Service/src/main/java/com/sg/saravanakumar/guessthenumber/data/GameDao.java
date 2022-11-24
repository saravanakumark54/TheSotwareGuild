/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sg.saravanakumar.guessthenumber.data;

import com.sg.saravanakumar.guessthenumber.model.Game;
import java.util.List;

/**
 *
 * @author saravanakumar
 */
public interface GameDao {
    
    int addGame(Game game);
    List<Game> getAllGames();
    Game getGame(int gameId);
}
