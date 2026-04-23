/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Assignment1;

/**
 *
 * @author Angie
 */
public class Player {
    private String name;
    private TraitType trait;
    private int totalPoints;
    private Inventory inventory;
    
    public Player(){
        this.name = name;
        this.trait = trait;
        this.totalPoints = 0;
        this.inventory = new Inventory();
    }
    
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
    
    public TraitType getTrait(){
        return trait;
    }

    public void setTrait(TraitType trait){
        this.trait = trait;
    }
    
    public int getTotalPoints(){
        return totalPoints;
    }
    
    public void changeTotalPoints(int points){
        totalPoints += points;
    }
    
    public Inventory getInventory(){
        return inventory;
    }

}
