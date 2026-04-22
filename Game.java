package project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author ella
 */
public class Game {
    // Calling other classes - check names & requirements 

    private Player player;
    private Scene currentScene;
    private Map<String, Scene> scenes;

    // Game constructor
    public Game() {
        this.scenes = new HashMap<>();
    }

    // Start game for new player
    public void start() {
        initScenes();
        player = new Player();
        selectTrait();
        loadScene("s1");
        run();
    }

    // Start game for loaded player (continue game)
    public void start(Player loadPlayer, String sceneId) {
        initScenes();
        this.player = loadPlayer;
        loadScene(sceneId);
        run();
    }

    // Gameplay loop
    private void run() {
        while (true) {
            currentScene.display(player.getTrait());

            // Stop if ending scene
            if (checkEnding()) {
                break;
            }

            List<Choice> avaliable = currentScene.getAvailableChoices(player);

            int chosen = Choice.getInput(avaliable);
            processChoice(chosen);
        }
    }

    // Load a scene
    public void loadScene(String sceneId) {

        Scene next = scenes.get(sceneId);
        if (next != null) {
            currentScene = next;

        } else {
            System.out.println("[ERROR] scene " + sceneId);
        }

    }

    public void processChoice(int num) {
        Choice choice = currentScene.getChoice(num);

        if (choice == null) {
            System.out.println("[ERROR] choice");
            return;
        }

        // Text after choice is made
        if (choice.getTransitionText() != null) {
            System.out.println("\n" + choice.getTransitionText());
        }

        // Update points
        player.addPoints(choice.getPointValue());

        // Add item (if in choice)
        for (Item item : choice.getGivenItems()) {
            player.getInventory().addItem(item);
            System.out.println("you got: " + item.getName());
        }

        // Next scene
        loadScene(choice.getNextSceneId());

    }

    public boolean checkEnding() {
        if (!currentScene.isEndScene()) {
            return false;
        }

        int points = player.getTotalPoints();

        if (points >= 20) {
            loadScene("good");
        } else if (points >= 0) {
            loadScene("neutral");
        } else {
            loadScene("bad");
        }

        // Ending extends scene
        // Display changes text
        Ending ending = (Ending) currentScene;
        ending.display(player.getTrait());
        return true;
    }

    // Trait selection
    private void selectTrait() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("who are you, traveller?");
        System.out.println("  1. brave");
        System.out.println("  2. cunning");
        System.out.println("  3. timid");

        while (true) {
            System.out.print("\n> ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 ->
                        player.setTrait(TraitType.BRAVE);
                    case 2 ->
                        player.setTrait(TraitType.CUNNING);
                    case 3 ->
                        player.setTrait(TraitType.TIMID);
                    default -> {
                        System.out.println("please enter 1, 2, or 3.");
                        continue;
                    }
                }
                System.out.println("you chose: " + player.getTrait());
                break;
            } catch (NumberFormatException e) {
                System.out.println("please enter 1, 2, or 3.");
            }
        }
    }

    // Add scenes
    public void addScene(Scene scene) {
        scenes.put(scene.getSceneId(), scene);
    }

    private void initScenes() {
        // Items
        List<Item> noItems = new ArrayList<>();
        List<Item> survivalGuide = new ArrayList<>();
        survivalGuide.add(new Item("Survival Guide"));
        List<Item> loot = new ArrayList<>();
        loot.add(new Item("Coin Pouch"));
        loot.add(new Item("Bread"));
        loot.add(new Item("Crystal Dagger"));

        // Opening Scene s1 //
        Scene s1 = new Scene("s1", """
                                   You wake slowly, the dawn light peers down through the trees, 
                                   dissipating into layers of mist. A swift breeze flutters leaves, 
                                   carrying subtle sounds of song, it seems you are not alone here. 
                                   Surrounded by forest, you notice a path stretching ahead of you.\n""", false);
        // Choices
        Choice s1c1 = new Choice(1, "Follow the path", "s2", 10, null, noItems);
        s1c1.setTransitionText("As you walk the music fades and the mist lifts,\n"
                + "the forest feels quiet and serene.");

        Choice s1c2brave = new Choice(2, "Abandon the path", "s2", -10, null, noItems);
        s1c2brave.addVarDialogue(TraitType.BRAVE,
                "As you walk the music builds and\n"
                + "the mist transforms suddenly into thick fog, \n"
                + "you can barely see ahead of you.\n\n"
                + "Confidently you don't break pace.\n"
                + "Even when you see the same tree 3 times...");
        s1c2brave.addVarDialogue(TraitType.CUNNING,
                "As you walk the music builds and\n"
                + "the mist transforms suddenly into thick fog, \n"
                + "you can barely see ahead of you.\n\n"
                + "You scan the little ground you can see\n"
                + "to grab a sharp rock, marking trees as \n"
                + "you walk, in an attempt to not get lost.");
        s1c2brave.setTransitionText("As you walk the music builds and\n"
                + "the mist transforms suddenly into thick fog, \n"
                + "you can barely see ahead of you.");

        // Timid gets a different choice with survival guide item
        Choice s1c2timid = new Choice(2, "Abandon the path", "s2", -10, null, survivalGuide);
        s1c2timid.addVarDialogue(TraitType.TIMID,
                "As you walk the music builds and\n"
                + "the mist transforms suddenly into thick fog, \n"
                + "you can barely see ahead of you.\n\n"
                + "Wearly you slow your pace, the forest feels unsettling.\n"
                + "Faitly you hear from all around, \"here...lost one...\""
                + "A book forms in the mist, dropping flat at"
                + "your feet. \"Survival Guide\"");

        s1.addChoice(s1c1);
        // Note: swap s1c2brave for s1c2timid in game logic if player is TIMID
        s1.addChoice(s1c2brave);
        scenes.put(s1.getSceneId(), s1);

        // Scene 2 s2 //
        Scene s2 = new Scene("s2", """
                                   Through the trees you make out a small cabin. Moss and
                                   vines have overtaken the walls, which are half rubble...
                                   But strangely enough, the chimney is smoking.\n""", false);
        // Choices
        Choice s2c1 = new Choice(1, "Ignore the cabin", "s3", 5, null, noItems);
        s2c1.setTransitionText("You walk past the cabin.");

        Choice s2c2 = new Choice(2, "Go inside", "s3", 10, null, loot);
        s2c2.addVarDialogue(TraitType.BRAVE,
                "You swing open the cabin door, standing alert.\n"
                + "You scan the room, spotting a chest off to the side. \n"
                + "Immediately you swing open the chest lid revealing\n"
                + "supplies, bread, a coin pouch, and a crystal dagger \n"
                + "that catches your eye.");
        s2c2.addVarDialogue(TraitType.TIMID,
                "Carefully you open the cabin door, it smells of \n"
                + "mold, and dust stirs through the air. Quietly, you make\n"
                + "your way to a chest in a dark corner, peering in you find\n"
                + "supplies. Bread, a coin pouch, and a crystal dagger.\n"
                + "\n"
                + "You decide to read the survival guide before you continue.\n"
                + "It says: \"Never stray from the path, for it is the right way\"\n"
                + "In bold in the center, all of the other pages are blank...");
        s2c2.addVarDialogue(TraitType.CUNNING,
                "You grab a nearby branch to push the cabin door\n"
                + "ajar, you scan the dark gloomy room, the fireplace seems\n"
                + "recently put out. After deciding its safe, you proceed. \n"
                + "You find a chest in the darkness, it contains bread, a coin pouch,\n"
                + "and a crystal dagger. \n"
                + "'The bread is fresh, somebody lives here' you think, with\n"
                + "haste you leave the cabin. ");
        s2c2.setTransitionText("You enter the dark gloomy cabin and find a\n"
                + "chest, containing bread, a coin pouch, and a crystal dagger.");

        s2.addChoice(s2c1);
        s2.addChoice(s2c2);
        scenes.put(s2.getSceneId(), s2);
    }

    // Getters and setters
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player p) {
        this.player = p;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public Map<String, Scene> getScenes() {
        return scenes;
    }

}
