package main.characters;

import java.util.Random;
import main.GameCharacter;

public class Gambler extends Player {
    private int cd1, cd2, cd3;
    private double highRollerValue = 1.0; // Crit multiplier from High Roller
    private Random rand = new Random(); // For dice rolls

    public Gambler() {
        super("Gambler", 130, 20, 15);
        cd1 = cd2 = cd3 = 0;
    }

    @Override
    protected int critChance() { return 20; } // 20% crit chance

    @Override
    protected double critMultiplier() { return highRollerValue; } // Dynamic crit multiplier

    @Override
    public void processTurnStart() {
        super.processTurnStart();
        // Reset highRollerValue after the buffed attack
        highRollerValue = 1.0;
    }

    @Override
    public int skill1(GameCharacter target) { // High Roller (Crit buff/debuff on next attack)
        if (cd1 > 0) { System.out.println("❌ High Roller cooling down (" + cd1 + " turns left)"); return 0; }
        cd1 = 2;
        gainEnergy(20);
        System.out.println("👤 Gambler used High Roller! (Rolls dice)");
        int dice1 = rand.nextInt(6) + 1; // 1-6
        int dice2 = rand.nextInt(6) + 1;
        int sum = dice1 + dice2;
        if (sum == 12) {
            highRollerValue = 5.0; // 500% crit damage
            System.out.println("🎲 Rolled " + sum + "! Critical jackpot!");
        } else if (sum >= 10) {
            highRollerValue = 3.0; // 300% crit damage
            System.out.println("🎲 Rolled " + sum + "! High roll!");
        } else if (sum >= 6) {
            highRollerValue = 2.0; // 200% crit damage
            System.out.println("🎲 Rolled " + sum + "! Decent roll!");
        } else if (sum >= 4) {
            highRollerValue = 0.5; // 50% crit damage
            System.out.println("🎲 Rolled " + sum + "! Low roll...");
        } else if (sum >= 3) {
            highRollerValue = 0.25; // 25% crit damage
            System.out.println("🎲 Rolled " + sum + "! Terrible roll!");
        } else if (sum == 2) {
            highRollerValue = -1.5; // -150% crit damage (damage to self)
            System.out.println("🎲 Rolled " + sum + "! Disaster! Next attack hurts self!");
        }
        return 0; // Buff skill, no damage
    }

    @Override
    public int skill2(GameCharacter target) { // Headhunt (heavy attack)
        if (cd2 > 0) { System.out.println("❌ Headhunt cooling down (" + cd2 + " turns left)"); return 0; }
        cd2 = 1;
        gainEnergy(25);
        int dmg = baseAttack + 5; // 25 damage
        System.out.println("🗡️ Gambler used Headhunt!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) { // Lucky Pot (Heal / Self Damage)
        if (cd3 > 0) { System.out.println("❌ Lucky Pot cooling down (" + cd3 + " turns left)"); return 0; }
        cd3 = 2;
        gainEnergy(20);
        System.out.println("💨 Gambler used Lucky Pot! (rolls a die)");
        int dice = rand.nextInt(6) + 1; // 1-6
        int healAmount = 0;
        if (dice == 6) {
            healAmount = 10;
        } else if (dice == 5) {
            healAmount = 7;
        } else if (dice == 4) {
            healAmount = 5;
        } else if (dice == 3) {
            healAmount = 3;
        } else if (dice == 2) {
            healAmount = -2; // Self-damage
        } else if (dice == 1) {
            healAmount = -5; // Self-damage
        }
        if (healAmount > 0) {
            health += healAmount;
            if (health > maxHealth) health = maxHealth;
            System.out.println("💖 Lucky Pot! Healed " + healAmount + " HP!");
        } else {
            System.out.println("💥 Unlucky Pot! Took " + Math.abs(healAmount) + " self-damage!");
            receiveDamage(Math.abs(healAmount)); // Apply self-damage
        }
        return 0; // No damage to enemy
    }

    @Override
    public int ultimate(GameCharacter target) { // BoomBah (massive attack)
        if (!canUseUltimate()) { System.out.println("❌ Ultimate not ready or on cooldown."); return 0; }
        setUltimateOnCooldown(4);
        spendAllEnergy();
        int dmg = baseAttack * 2 + 40; // 80 damage
        System.out.println("⚔️ Gambler used BoomBah!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

    // Implement new methods
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
