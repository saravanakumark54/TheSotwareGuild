/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.saravanakumar.guessthenumber.controller;

import com.sg.saravanakumar.guessthenumber.model.Game;
import com.sg.saravanakumar.guessthenumber.model.GameRound;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sg.saravanakumar.guessthenumber.service.GuessTheNumberService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 *
 * @author saravanakumar
 */
@RestController
@RequestMapping("/api")
public class GuessTheNumberController {
    
    private final GuessTheNumberService service;
    
    @Autowired
    public GuessTheNumberController(GuessTheNumberService service){
        this.service = service;
    }
    
    @PostMapping("/begin")
    public ResponseEntity<Integer> beginGame(){
        return new ResponseEntity(service.createAndAddGame(), HttpStatus.CREATED); 
    }
    
    @PostMapping("/guess")
    public ResponseEntity<GameRound> makeGuess(@RequestBody GameRound guess){
        
        Game game = service.getGame(guess.getGameId());
        GameRound round = service.submitGuess(guess.getGuess(), game);
        return ResponseEntity.ok(round);
    }

    @GetMapping("/game")
    public List<Game> getAllGames(){
        List<Game> games = service.getAllGames();
        games.forEach(game -> hideInProgressAnswers(game));
        return games;
    }
    
    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable int gameId){
        Game game = service.getGame(gameId);
        hideInProgressAnswers(game);
        return ResponseEntity.ok(game);
    }
    
    @GetMapping("/rounds/{gameId}")
    public ResponseEntity<List<GameRound>> getAllRoundsByGameId(
            @PathVariable int gameId){
        
        Game game = service.getGame(gameId);
        List<GameRound> rounds = service.getRounds(game);
        return ResponseEntity.ok(rounds);
    }
    
    private void hideInProgressAnswers(Game game){
        if (game.getStatus().equals("In Progress")){
            game.setAnswer("Hidden");
        }
    }
}
