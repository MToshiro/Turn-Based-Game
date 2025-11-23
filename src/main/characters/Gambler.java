package main.characters;

import java.util.Random;
import main.GameCharacter;

// Gambler class - luck-based hero with random effects and dice mechanics
public class Gambler extends Player {
    // Cooldowns for all three skills
    private int cd1, cd2, cd3;
    // Stores crit multiplier for next attack determined by High Roller
    private double highRollerValue = 1.0;
    // Random number generator for dice rolls
    private Random rand = new Random();

    // Set up Gambler with stats and cooldowns
    public Gambler() {
        super("Gambler", 130, 20, 15);
        cd1 = cd2 = cd3 = 0;
    }

    // Crit chance is standard, but multiplier changes based on High Roller
    @Override
    protected int critChance() { return 20; }
    @Override
    protected double critMultiplier() { return highRollerValue; }

    // After each turn, reset High Roller multiplier for next round
    @Override
    public void processTurnStart() {
        super.processTurnStart();
        highRollerValue = 1.0;
    }

    // Skill 1: High Roller – roll for random crit bonus or penalty (does not deal direct damage)
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) {
            System.out.println("High Roller cooling down (" + cd1 + " turns left)");
            return 0;
        }
        cd1 = 2;
        gainEnergy(20);
        System.out.println("Gambler used HIGH ROLLER! Rolls two dice.");
        int dice1 = rand.nextInt(6) + 1; // 1-6
        int dice2 = rand.nextInt(6) + 1;
        int sum = dice1 + dice2;
        if (sum == 12) {
            highRollerValue = 5.0;
            System.out.println("Rolled " + sum + "! Critical jackpot!");
        } else if (sum == 10) {
            highRollerValue = 3.0;
            System.out.println("Rolled " + sum + "! High roll!");
        } else if (sum == 6) {
            highRollerValue = 2.0;
            System.out.println("Rolled " + sum + "! Decent roll!");
        } else if (sum == 4) {
            highRollerValue = 0.5;
            System.out.println("Rolled " + sum + "! Low roll...");
        } else if (sum == 3) {
            highRollerValue = 0.25;
            System.out.println("Rolled " + sum + "! Terrible roll!");
        } else if (sum == 2) {
            highRollerValue = -1.5;
            System.out.println("Rolled " + sum + "! Disaster! Next attack hurts self!");
        } else {
            highRollerValue = 1.0;
            System.out.println("Rolled " + sum + "! Normal effect.");
        }
        return 0; // No damage, buffs/debuffs next attack
    }

    // Skill 2: Headhunt – a heavy physical attack (affected by current crit multiplier)
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) {
            System.out.println("Headhunt cooling down (" + cd2 + " turns left)");
            return 0;
        }
        cd2 = 1;
        gainEnergy(25);
        int dmg = baseAttack + 5;
        System.out.println("Gambler used HEADHUNT!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 3: Lucky Pot – roll die to heal or hurt yourself (no effect on enemy)
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) {
            System.out.println("Lucky Pot cooling down (" + cd3 + " turns left)");
            return 0;
        }
        cd3 = 2;
        gainEnergy(20);
        System.out.println("Gambler used LUCKY POT! Rolls a die for random effect.");
        int dice = rand.nextInt(6) + 1;
        int healAmount = 0;
        if (dice == 6)       healAmount = 10;    // Big heal
        else if (dice == 5)  healAmount = 7;
        else if (dice == 4)  healAmount = 5;
        else if (dice == 3)  healAmount = 3;
        else if (dice == 2)  healAmount = -2;    // Small self-damage
        else if (dice == 1)  healAmount = -5;    // Big self-damage
        if (healAmount > 0) {
            health += healAmount;
            if (health > maxHealth) health = maxHealth;
            System.out.println("Lucky Pot! Healed " + healAmount + " HP!");
        } else if (healAmount < 0) {
            System.out.println("Unlucky Pot! Took " + Math.abs(healAmount) + " self-damage!");
            receiveDamage(Math.abs(healAmount));
        }
        return 0; // No enemy damage
    }

    // Ultimate: BoomBah – a massive attack (crit multiplier can be very high or negative)
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) {
            System.out.println("Ultimate not ready or on cooldown.");
            return 0;
        }
        setUltimateOnCooldown(4);
        spendAllEnergy();
        int dmg = baseAttack * 2 + 40;
        System.out.println("Gambler used BOOMBAH!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Each turn, reduce cooldown for all 3 skills if active
    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

    @Override
    public String getSkill1Name() { return "High Roller"; }
    @Override
    public String getSkill2Name() { return "Headhunt"; }
    @Override
    public String getSkill3Name() { return "Lucky Pot"; }
    @Override
    public String getUltimateName() { return "BoomBah"; }
    @Override
    public int getSkill1Cooldown() { return cd1; }
    @Override
    public int getSkill2Cooldown() { return cd2; }
    @Override
    public int getSkill3Cooldown() { return cd3; }
}
