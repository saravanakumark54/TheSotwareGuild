/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.saravanakumar.guessthenumber.controller;

import java.time.LocalDateTime;

/**
 *
 * @author saravanakumar
 */
public class Error {
    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    
    public LocalDateTime getTimestamp(){
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }
    
    public String getMessage(){
        return message;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
}
