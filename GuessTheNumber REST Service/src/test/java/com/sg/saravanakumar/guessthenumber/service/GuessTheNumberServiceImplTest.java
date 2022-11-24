/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.sg.saravanakumar.guessthenumber.service;

import com.sg.saravanakumar.guessthenumber.data.GameDao;
import com.sg.saravanakumar.guessthenumber.model.Game;
import com.sg.saravanakumar.guessthenumber.model.GameRound;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
public class GuessTheNumberServiceImplTest {
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    GuessTheNumberService service;
    
    @Autowired
    GameDao gameDao;
    
    private Game game;
    
    public GuessTheNumberServiceImplTest() {
    }

    @Before
    public void setUp() {
        final String DELETE_ALL_ROUNDS = "DELETE FROM gameround "
                + "WHERE gameId > 0;";
        jdbc.update(DELETE_ALL_ROUNDS);
        final String DELETE_ALL_GAMES = "DELETE FROM game "
                + "WHERE gameId > 0;";
        jdbc.update(DELETE_ALL_GAMES);
        game = new Game();
        game.setAnswer("1234");
        game.setStatus("In Progress");
        game.setGameId(gameDao.addGame(game));
    }
    
    @Test
    public void testCreateAddGetGame() {
        int gameId = service.createAndAddGame();
        Game newGame = service.getGame(gameId);
        
        assertNotNull(newGame);
        assertNotNull(newGame.getAnswer());
        assertEquals("In Progress", newGame.getStatus());
        
        String answer = newGame.getAnswer();
        assertEquals(4, newGame.getAnswer().length());
        assertEquals(gameId, newGame.getGameId());
    }

    @Test
    public void testSubmitGuessIncorrect() {
        String guess = "1389";
        GameRound round = new GameRound();
        round.setGameId(game.getGameId());
        round.setGuess(guess);
        round.setGuessResult("e:1:p:1");
        
        GameRound returnedRound = service.submitGuess(guess, game);
        assertNotNull(returnedRound);
        round.setRoundId(returnedRound.getRoundId());
        round.setGuessTime(returnedRound.getGuessTime());
        
        assertEquals(round, returnedRound);
    }

    @Test
    public void testSubmitGuessCorrect() {
        String guess = "1234";
        GameRound round = new GameRound();
        round.setGameId(game.getGameId());
        round.setGuess(guess);
        round.setGuessResult("e:4:p:0");
        
        GameRound returnedRound = service.submitGuess(guess, game);
        assertNotNull(returnedRound);
        round.setRoundId(returnedRound.getRoundId());
        round.setGuessTime(returnedRound.getGuessTime());
        
        assertEquals(round, returnedRound);
    }

    @Test
    public void testGetAllGames() {
        int game2Id = service.createAndAddGame();
        Game game2 = service.getGame(game2Id);
        
        List<Game> games = service.getAllGames();
        
        assertEquals(2, games.size());
        assertTrue(games.contains(game));
        assertTrue(games.contains(game2));
    }

    @Test
    public void testGetRounds() {
        GameRound round1 = service.submitGuess("5678", game);
        GameRound round2 = service.submitGuess("1234", game);
        
        List<GameRound> rounds = service.getRounds(game);
        
        assertEquals(2, rounds.size());
        assertFalse(rounds.contains(round1));
        assertFalse(rounds.contains(round2));
    }
    
}
