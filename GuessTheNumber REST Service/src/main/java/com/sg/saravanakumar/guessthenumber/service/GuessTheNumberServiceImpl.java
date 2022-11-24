/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.saravanakumar.guessthenumber.service;

import com.sg.saravanakumar.guessthenumber.data.GameDao;
import com.sg.saravanakumar.guessthenumber.data.GameRoundDao;
import com.sg.saravanakumar.guessthenumber.model.Game;
import com.sg.saravanakumar.guessthenumber.model.GameRound;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import org.springframework.stereotype.Component;

/**
 *
 * @author saravnakumar
 */
@Component
public class GuessTheNumberServiceImpl implements GuessTheNumberService{
    
    private final GameDao gameDao;
    private final GameRoundDao gameRoundDao;
    
    @Autowired
    public GuessTheNumberServiceImpl(GameDao gameDao, GameRoundDao gameRoundDao){
        this.gameDao = gameDao;
        this.gameRoundDao = gameRoundDao;
    }

    @Override
    public int createAndAddGame() {
        Game game = new Game();
        game.setStatus("In Progress");
        game.setAnswer(generateAnswer());
        return gameDao.addGame(game);
    }

    @Override
    public GameRound submitGuess(String guess, Game game) {
        
        // Evaluate guess
        String guessResult = generateGuessResult(guess, game.getAnswer());
        
        // Create GameRound object
        GameRound round = new GameRound();
        round.setGameId(game.getGameId());
        round.setGuess(guess);
        round.setGuessResult(guessResult);
        round.setGuessTime(LocalTime.now().withNano(0));
        
        // Add GuessRound to DB and update Game if correct guess
        if (guessResult.equals("e:4:p:0")){
            return gameRoundDao.addWinningRound(round);
        }
        else{
            return gameRoundDao.addLosingRound(round);
        }
    }

    @Override
    public List<Game> getAllGames() {
        return gameDao.getAllGames();
    }

    @Override
    public Game getGame(int gameId) {
        return gameDao.getGame(gameId);
    }

    @Override
    public List<GameRound> getRounds(Game game) {
        return gameRoundDao.getRounds(game);
    }

    private String generateAnswer() {
        List<Integer> availableNums = new ArrayList<>();
        for (int i=0; i<10; i++){
            availableNums.add(i);
        }
        String answer = "";
        Random rand = new Random();
        while (answer.length() < 4){
            answer += availableNums.remove(rand.nextInt(availableNums.size()));
        }
        return answer;
    }

    private String generateGuessResult(String guess, String answer) {
        
        int exactMatches = 0;
        int partialMatches = 0;
        
        for (int i=0; i<answer.length(); i++){
            if (guess.charAt(i) == answer.charAt(i)){
                exactMatches += 1;
            }
            else if (answer.contains(String.valueOf(guess.charAt(i)))){
                partialMatches += 1;
            }
        }
        
        return "e:" + exactMatches + ":p:" + partialMatches;
    }
}
