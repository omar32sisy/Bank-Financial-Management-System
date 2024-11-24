/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankaccountapp;

/**
 *
 * @author mohhe
 */public class Silver extends Level {
  
   private double onlineFee = 50.0;
  
   @Override
   public double getOnlineFee() {
       return onlineFee;
   }
  
   @Override
   public String toString() {
       return "Silver";
   }
}
