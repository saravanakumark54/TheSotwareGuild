/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.saravanakumar.guessthenumber.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author saravanakumar
 */
@ControllerAdvice
@RestController
public class GuessTheNumberControllerExceptionHandler {
    private static final String NULL_POINTER_MESSAGE = "Must include a guess.";
    private static final String EMPTY_RESULT_MESSAGE = "Could not find game. "
            + "Please ensure gameId is valid and try again.";
    
    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<Error> handleNullPointerException(
            NullPointerException ex,
            WebRequest request){
        
        Error err = new Error();
        err.setMessage(NULL_POINTER_MESSAGE);
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public final ResponseEntity<Error> handleEmptyResultDataAccessException(
            EmptyResultDataAccessException ex,
            WebRequest request){
        
        Error err = new Error();
        err.setMessage(EMPTY_RESULT_MESSAGE);
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
}
