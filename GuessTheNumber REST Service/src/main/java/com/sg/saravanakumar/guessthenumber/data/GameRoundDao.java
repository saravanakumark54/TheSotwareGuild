/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sg.saravanakumar.guessthenumber.data;

import com.sg.saravanakumar.guessthenumber.model.Game;
import com.sg.saravanakumar.guessthenumber.model.GameRound;
import java.util.List;

/**
 *
 * @author saravanakumar
 */
public interface GameRoundDao {
    
    GameRound addLosingRound(GameRound round);
    GameRound addWinningRound(GameRound round);
    List<GameRound> getRounds(Game game);
}
