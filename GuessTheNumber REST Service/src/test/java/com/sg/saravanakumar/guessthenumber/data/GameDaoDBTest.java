/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.sg.saravanakumar.guessthenumber.data;

import com.sg.saravanakumar.guessthenumber.model.Game;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author abinayarajkumar
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GameDaoDBTest {
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    GameDao gameDao;
  
    @Before
    public void setUp() {
        final String DELETE_ALL_ROUNDS = "DELETE FROM gameround "
                + "WHERE gameId > 0;";
        jdbc.update(DELETE_ALL_ROUNDS);
        final String DELETE_ALL_GAMES = "DELETE FROM game "
                + "WHERE gameId > 0;";
        jdbc.update(DELETE_ALL_GAMES);
    }

    @Test
    public void testAddGetGame() {
        Game game = new Game();
        game.setAnswer("1234");
        game.setStatus("In Progress");
        int gameId = gameDao.addGame(game);
        
        Game fromDao = gameDao.getGame(gameId);
        
        assertEquals(gameId, fromDao.getGameId());
        game.setGameId(gameId);
        assertEquals(game, fromDao);
    }

    @Test
    public void testGetAllGames() {
        Game game1 = new Game();
        game1.setAnswer("1234");
        game1.setStatus("In Progress");
        gameDao.addGame(game1);
        
        Game game2 = new Game();
        game2.setAnswer("5678");
        game2.setStatus("Finished");
        gameDao.addGame(game2);
        
        List<Game> games = gameDao.getAllGames();
        
        assertEquals(2, games.size());
        assertTrue(games.contains(game1));
        assertTrue(games.contains(game2));
        
    }
}
