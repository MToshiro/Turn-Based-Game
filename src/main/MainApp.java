package main;
import java.util.Random;
import java.util.Scanner;

import main.characters.Player;
import main.characters.Assassin;
import main.characters.Avenger;
import main.characters.Barbarian;
import main.characters.Devourer;
import main.characters.Gambler;
import main.characters.Inquisitor;
import main.characters.Necromancer;
import main.characters.Shapeshifter;

import main.enemies.Enemy;
import main.enemies.ArcaneWraith;
import main.enemies.EarthGolem;
import main.enemies.FrostWraith;
import main.enemies.InfernoTitan;
import main.enemies.SavageBeast;
import main.enemies.ShadowOverlord;
import main.enemies.StormSerpent;
import main.enemies.VenomousSerpent;

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        // Outer loop for replaying the game
        boolean playAgain = true;
        while (playAgain) {
            Player player = null;
            Enemy enemy = null;

            String[] playerActionLog = new String[500];
            String[] enemyActionLog = new String[500];
            int playerLogIndex = 0, enemyLogIndex = 0;

            System.out.println("=== ⚔️ BATTLE OF REALMS - ULTIMATE ===");

            // Player selection with retry on invalid input
            while (player == null) {
                try {
                    System.out.println("\nChoose your hero:");
                    System.out.println("[1] Assassin");
                    System.out.println("[2] Avenger");
                    System.out.println("[3] Barbarian");
                    System.out.println("[4] Devourer");
                    System.out.println("[5] Gambler");
                    System.out.println("[6] Inquisitor");
                    System.out.println("[7] Necromancer");
                    System.out.println("[8] Shapeshifter");
                    System.out.print("Enter choice: ");
                    int choice = Integer.parseInt(sc.nextLine());

                    switch (choice) {
                        case 1 -> player = new Assassin();
                        case 2 -> player = new Avenger();
                        case 3 -> player = new Barbarian();
                        case 4 -> player = new Devourer();
                        case 5 -> player = new Gambler();
                        case 6 -> player = new Inquisitor();
                        case 7 -> player = new Necromancer();
                        case 8 -> player = new Shapeshifter();
                        default -> System.out.println("Invalid choice! Try again.");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Invalid input. Please enter a number (1-8).");
                }
            }

            // Enemy pool
            Enemy[] enemies = { new ArcaneWraith(), new EarthGolem(), new FrostWraith(), new InfernoTitan(), new SavageBeast(), new ShadowOverlord(), new StormSerpent(), new VenomousSerpent() };
            enemy = enemies[rand.nextInt(enemies.length)];
            System.out.println("\n⚠️ An enemy appeared: " + enemy.getName() + "!");

            // Battle loop
            int round = 1;
            while (player.isAlive() && enemy.isAlive()) {
                System.out.println("\n--------------------------------------------");
                System.out.println("ROUND " + round);
                player.processTurnStart();
                enemy.processTurnStart();

                System.out.println("PLAYER: " + player.getName() + " | HP: " + player.getHealth() + "/" + player.getMaxHealth()
                        + " | Energy: " + player.getEnergy() + "/100 | Heals: " + player.getHealsLeft());
                System.out.println("ENEMY:  " + enemy.getName() + " | HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth()
                        + " | Energy: " + enemy.getEnergy() + "/100 | Heals: " + enemy.getHealsLeft());
                System.out.println("--------------------------------------------");

                // Check if player frozen/stunned
                if (player.isFrozen()) {
                    System.out.println("❄️ You are frozen and skip this turn!");
                    playerActionLog[playerLogIndex++] = "skipped turn (frozen)"; // Log descriptive string
                } else {
                    // Player action
                    System.out.println("\nChoose your action:");
                    System.out.println("[1] Basic Attack");
                    System.out.println("[2] " + player.getSkill1Name() + (player.getSkill1Cooldown() == 0 ? " [Ready]" : " [Cooldown: " + player.getSkill1Cooldown() + " turn(s)]"));

                    if (player instanceof Shapeshifter) {
                        Shapeshifter shapeshifter = (Shapeshifter) player;
                        Shapeshifter.Form form = shapeshifter.getCurrentForm();
                        int optionNumber = 3;

                        if (form == Shapeshifter.Form.DRAGON) {
                            System.out.println("[" + optionNumber + "] " + player.getSkill2Name() + (player.getSkill2Cooldown() == 0 ? " [Ready]" : " [Cooldown: " + player.getSkill2Cooldown() + " turn(s)]"));
                            optionNumber++;
                        } else if (form == Shapeshifter.Form.GOLEM) {
                            System.out.println("[" + optionNumber + "] " + player.getSkill3Name() + (player.getSkill3Cooldown() == 0 ? " [Ready]" : " [Cooldown: " + player.getSkill3Cooldown() + " turn(s)]"));
                            optionNumber++;
                        }  // In HUMAN form, no Skill2 or Skill3 shown

                        String ultStatus;
                        if (player.canUseUltimate()) {
                            ultStatus = " [Ready]";
                        } else if (player.getEnergy() < 100) {
                            ultStatus = " [Not Ready: Need 100 Energy]";
                        } else if (player.getUltimateCooldown() > 0) {
                            ultStatus = " [Not Ready: Cooldown " + player.getUltimateCooldown() + " turn(s)]";
                        } else {
                            ultStatus = " [Not Ready]";
                        }

                        System.out.println("[" + optionNumber + "] " + player.getUltimateName() + ultStatus);
                        optionNumber++;
                        System.out.println("[" + optionNumber + "] Heal");
                    } else {
                    
                    System.out.println("[3] " + player.getSkill2Name() + (player.getSkill2Cooldown() == 0 ? " [Ready]" : " [Cooldown: " + player.getSkill2Cooldown() + " turn(s)]"));
                    System.out.println("[4] " + player.getSkill3Name() + (player.getSkill3Cooldown() == 0 ? " [Ready]" : " [Cooldown: " + player.getSkill3Cooldown() + " turn(s)]"));

                    String ultStatus;
                    if (player.canUseUltimate()) {
                        ultStatus = " [Ready]";
                    } else if (player.getEnergy() < 100) {
                        ultStatus = " [Not Ready: Need 100 Energy]";
                    } else if (player.getUltimateCooldown() > 0) {
                        ultStatus = " [Not Ready: Cooldown " + player.getUltimateCooldown() + " turn(s)]";
                    } else {
                        ultStatus = " [Not Ready]";
                    }
                    System.out.println("[5] " + player.getUltimateName() + ultStatus);
                    System.out.println("[6] Heal");
                }
                    System.out.print("Enter your action: ");
                
                    int action = 0;
                    try { action = Integer.parseInt(sc.nextLine()); } catch (Exception e) { action = 0; }

                    System.out.println("\n--- Your Turn ---");

                    String logEntry = "";
                    if (player instanceof Shapeshifter) {
                        Shapeshifter shapeshifter = (Shapeshifter) player;
                        Shapeshifter.Form form = shapeshifter.getCurrentForm();
                        switch (action) {
                            case 1 -> {
                                int dmg = player.basicAttack();
                                if (enemy.receiveDamage(dmg)) {
                                    System.out.println("💥 You dealt " + dmg + " damage to " + enemy.getName() + "!");
                                    logEntry = dmg + " damage to enemy";
                                } else {
                                    logEntry = "attack dodged by enemy";
                                }
                            }
                            case 2 -> {
                                player.skill1(enemy);
                                logEntry = "used Skill 1 (transform)";
                            }
                            case 3 -> {
                                if (form == Shapeshifter.Form.DRAGON) {
                                    int dmg = player.skill2(enemy);
                                    if (dmg > 0) {
                                        logEntry = dmg + " damage to enemy (Skill 2)";
                                    } else {
                                        logEntry = "used Skill 2 (buff/effect)";
                                    }
                                } else if (form == Shapeshifter.Form.GOLEM) {
                                    int dmg = player.skill3(enemy);
                                    if (dmg > 0) {
                                        logEntry = dmg + " damage to enemy (Skill 3)";
                                    } else {
                                        logEntry = "used Skill 3 (buff/effect)";
                                    }
                                } else {
                                    int dmg = player.ultimate(enemy);
                                    if (dmg > 0) {
                                        logEntry = dmg + " damage to enemy (Ultimate)";
                                    } else {
                                        logEntry = "used Ultimate (buff/effect)";
                                    }
                                }
                            }
                            case 4 -> {
                                if (form == Shapeshifter.Form.HUMAN) {
                                    player.heal();
                                    logEntry = "healed " + 40 + " HP";
                                } else {
                                    int dmg = player.ultimate(enemy);
                                    if (dmg > 0) {
                                        logEntry = dmg + " damage to enemy (Ultimate)";
                                    } else {
                                        logEntry = "used Ultimate (buff/effect)";
                                    }
                                }
                            }
                            case 5 -> {
                                if (form != Shapeshifter.Form.HUMAN) {
                                    player.heal();
                                    logEntry = "healed " + 40 + " HP";
                                } else {
                                    System.out.println("Invalid choice. Turn wasted!");
                                    logEntry = "invalid action (turn wasted)";
                                }
                            }
                            default -> {
                                System.out.println("Invalid choice. Turn wasted!");
                                logEntry = "invalid action (turn wasted)";
                            }
                        }
                    } else {
                        switch (action) {
                            case 1 -> {
                                int dmg = player.basicAttack();
                                if (enemy.receiveDamage(dmg)) {
                                    System.out.println("💥 You dealt " + dmg + " damage to " + enemy.getName() + "!");
                                    logEntry = dmg + " damage to enemy";
                                } else {
                                    logEntry = "attack dodged by enemy";
                                }
                            }
                            case 2 -> {
                                int dmg = player.skill1(enemy);
                                if (dmg > 0) {
                                    logEntry = dmg + " damage to enemy (Skill 1)";
                                } else {
                                    logEntry = "used Skill 1 (buff/effect)";
                                }
                            }
                            case 3 -> { 
                                int dmg = player.skill2(enemy);
                                if (dmg > 0) {
                                    logEntry = dmg + " damage to enemy (Skill 2)";
                                } else {
                                    logEntry = "used Skill 2 (buff/effect)";
                                }
                            }
                            case 4 -> { 
                                int dmg = player.skill3(enemy);
                                if (dmg > 0) {
                                    logEntry = dmg + " damage to enemy (Skill 3)";
                                } else {
                                    logEntry = "used Skill 3 (buff/effect)";
                                }
                            }
                            case 5 -> {
                                int dmg = player.ultimate(enemy);
                                if (dmg > 0) {
                                    logEntry = dmg + " damage to enemy (Ultimate)";
                                } else {
                                    logEntry = "used Ultimate (buff/effect)";
                                }
                            }
                            case 6 -> {
                                player.heal();
                                logEntry = "healed " + 40 + " HP";
                            }
                            default -> {
                                System.out.println("Invalid choice. Turn wasted!");
                                logEntry = "invalid action (turn wasted)";
                            }
                        }
                    }
                    playerActionLog[playerLogIndex++] = logEntry;
                }

                // Wait 0.8-1.8 seconds after player turn before enemy turn
                try { Thread.sleep(8000 + rand.nextInt(1000)); } catch (Exception ignored) {}

                if (!enemy.isAlive()) break;

                // Enemy turn
                System.out.println("\n--- Enemy's Turn ---");
                if (enemy.isFrozen()) {
                    System.out.println(enemy.getName() + " is frozen and skips its turn!");
                    enemyActionLog[enemyLogIndex++] = "skipped turn (frozen)";
                } else {
                    int dmgFromEnemy = enemy.decideAction(player);
                    if (dmgFromEnemy > 0) {
                        enemyActionLog[enemyLogIndex++] = dmgFromEnemy + " damage to player";
                    } else {
                        enemyActionLog[enemyLogIndex++] = "used ability (buff/heal/effect)"; // For non-damaging actions like heals
                    }
                }

                if (!player.isAlive()) break;

                player.reduceCooldowns();
                enemy.reduceCooldowns();

                System.out.println("\n--- TURN SUMMARY ---");
                System.out.println("Player HP: " + player.getHealth() + "/" + player.getMaxHealth() + " | Enemy HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth());

                round++;
                try { Thread.sleep(300); } catch (Exception ignored) {}
            }

                System.out.println("\n===== BATTLE RESULT =====");
                if (player.isAlive()) {
                    System.out.println("🎉 YOU WIN! Defeated " + enemy.getName() + "!");
                } else {
                    System.out.println("💀 YOU LOST... " + enemy.getName() + " was too strong.");
                }   

                System.out.println("\n--- PLAYER ACTION HISTORY ---");
                for (int i = 0; i < playerLogIndex; i++) {
                    System.out.println("Round " + (i + 1) + ": " + playerActionLog[i]);
                }

                System.out.println("\n--- ENEMY ACTION HISTORY ---");
                for (int i = 0; i < enemyLogIndex; i++) {
                    System.out.println("Round " + (i + 1) + ": " + enemyActionLog[i]);
                }

                while (true) {
                    System.out.println("\n--------------------------------------------");
                    System.out.print("\nDo you want to play again? (yes/no): ");
                    String response = sc.nextLine().trim().toLowerCase();
                    if (response.equals("yes")) {
                        playAgain = true;
                        break;
                    } else if (response.equals("no")) {
                        playAgain = false;
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                    }
                }
                System.out.println("\n--------------------------------------------\n");
        }

        System.out.println("Thanks for playing! Goodbye.");
        sc.close();
    }
}