package com.bookstore.dto;

public class Views {

   public interface NameEmail {}      
   
   public interface NameEmailAgeGenre extends NameEmail {}
   
   public interface All extends NameEmailAgeGenre {}
}
