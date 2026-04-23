package project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ella
 */
public class Scene {

    private String sceneId;
    private String sceneDesc;
    // Trait specific scenes
    private Map<TraitType, String> varDialogue;
    private List<Choice> choices;
    private Npc npc;
    private boolean isEndScene;

    public Scene(String sceneId, String sceneDesc, boolean isEndScene) {
        this.sceneId = sceneId;
        this.sceneDesc = sceneDesc;
        this.isEndScene = isEndScene;
        this.choices = new ArrayList<>();
        this.varDialogue = new HashMap<>();
        this.npc = null;
    }

    // Different options for different traits
    public void display(TraitType trait) {

        // Use trait variant if one exists
        if (varDialogue.containsKey(trait)) {
            System.out.println(varDialogue.get(trait));
        } else {
            System.out.println(sceneDesc);
        }

        // Print NPC dialogue if this scene has an NPC
        if (npc != null) {
            System.out.println("\n" + npc.getName() + " says:");
            System.out.println("  \"" + npc.getSpeak(trait) + "\"");
        }
    }

    // Return choice
    public Choice getChoice(int number) {
        for (Choice c : choices) {
            if (c.getNumber() == number) {
                return c;
            }
        }
        return null;
    }

    // Return avaliable choices
    public List<Choice> getAvailableChoices(Player player) {
        List<Choice> available = new ArrayList<>();
        for (Choice c : choices) {
            if (c.isAvailable(player)) {
                available.add(c);
            }
        }
        return available;
    }

    // Add choice
    public void addChoice(Choice choice) {
        choices.add(choice);
    }

    // Set NPC
    public void setNpc(Npc npc) {
        this.npc = npc;
    }

    // Adds trait description
    public void addVarDialogue(TraitType trait, String description) {
        varDialogue.put(trait, description);
    }

    // Getters
    public String getSceneId() {
        return sceneId;
    }

    public boolean isEndScene() {
        return isEndScene;
    }

}
