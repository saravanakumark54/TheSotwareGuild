/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.saravanakumar.guessthenumber.data;

import com.sg.saravanakumar.guessthenumber.model.Game;
import com.sg.saravanakumar.guessthenumber.model.GameRound;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author saravanakumar
 */
@Repository
@Profile("database")
public class GameRoundDaoDB implements GameRoundDao{
    private final JdbcTemplate jdbc;
    
    @Autowired
    public GameRoundDaoDB(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public GameRound addLosingRound(GameRound round) {
        return addRound(round);
    }
    
    @Override
    @Transactional
    public GameRound addWinningRound(GameRound round){
        final String UPDATE_GAME_TO_FINISHED = "UPDATE game " +
                "SET status = ? " +
                "WHERE gameId = ?;";
        final String FINISHED_STATUS = "Finished";
        jdbc.update(UPDATE_GAME_TO_FINISHED,
                FINISHED_STATUS,
                round.getGameId());
        return addRound(round);
    }

    @Override
    public List<GameRound> getRounds(Game game) {
        final String GET_GAME_ROUNDS = "SELECT roundId, gameId, guess, guessTime, guessResult "
                + "FROM gameround "
                + "WHERE gameId = ? "
                + "ORDER BY guessTime;";
        return jdbc.query(GET_GAME_ROUNDS, new RoundMapper(), game.getGameId());
    }
    
    private static final class RoundMapper implements RowMapper<GameRound>{
        
        @Override
        public GameRound mapRow(ResultSet rs, int index) throws SQLException{
            GameRound round = new GameRound();
            round.setGameId(rs.getInt("gameId"));
            round.setRoundId(rs.getInt("roundId"));
            round.setGuess(rs.getString("guess"));
            round.setGuessResult(rs.getString("guessResult"));
            round.setGuessTime(rs.getTime("guessTime").toLocalTime());
            return round;
        }
    }

    private GameRound addRound(GameRound round){
        final String GET_NEXT_GAME_ROUND_ID = "SELECT IFNULL(MAX(roundId)+1, 1) " +
                "FROM gameround " +
                "WHERE gameId = ?;";
        int roundId = jdbc.queryForObject(GET_NEXT_GAME_ROUND_ID, Integer.class, round.getGameId());
        round.setRoundId(roundId);
        
        final String INSERT_ROUND = "INSERT INTO gameround(roundId, gameId, guess, guessTime, guessResult) "
                + "VALUES (?, ?, ?, ?, ?);";
        
        jdbc.update(INSERT_ROUND,
                round.getRoundId(),
                round.getGameId(),
                round.getGuess(),
                round.getGuessTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                round.getGuessResult());
        
        return round;
    }
}
