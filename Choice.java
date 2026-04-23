/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Assignment1;

import java.util.List;

/**
 *
 * @author angie
 */
public class Choice {
    private int pointValue;
    private String choiceDesc;
    private Item requiredItem;
    private TraitType requiredTrait;
    private String nextSceneID;
    private List<Item> giveItem;
    private int number; 
    private String transitionText;
    
    // getters and setters
    public String getNextSceneId(){
        return nextSceneID;
    }
    
    public String getChoiceDesc(){
        return choiceDesc;
    }
    
    public void setChoiceDesc(){
        this.choiceDesc = choiceDesc;
    }
    
    public int getNumber(){
        return number;
    }
    
    public void setNumber(){
        this.number = number;
    }
    
    public int getPointValue(){
        return pointValue;
    }
    
    public void setPointValue(){
        this.pointValue = pointValue;
    }
    
    public String getTransitionText(){
        return transitionText;
    }
    
    public void setTransitionText(){
        this.transitionText = transitionText;
    }
    
    // choices available depending on player trait and item
    public boolean isAvailable(Player player){
        if (requiredTrait != null){
            if (player.getTrait() != requiredTrait){
                return false;
            }
        }
        
        if (requiredItem != null){
            if (!player.getInventory().hasItem(requiredItem.getName())){
                return false;
            }
        }
        return true;
    }
    
    // plus/minus points from player total points depending on the point value of choice
    public void applyPointEffect(Player player){
        player.changeTotalPoints(pointValue);
    }
    
}
