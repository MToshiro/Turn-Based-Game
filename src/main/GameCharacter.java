package main;

import java.util.Random;

// Base class for all characters (players and enemies) in the game
public abstract class GameCharacter {
    // Character's name
    protected String name;
    // Current and maximum health
    protected int health, maxHealth;
    // Base attack damage stat
    protected int baseAttack;
    // Current energy (0-100)
    protected int energy;
    // Number of heals remaining (each gives health boost)
    protected int healsLeft;
    // Dodge chance (percentage, higher means more likely to avoid attacks)
    public int dodgeChance;
    // Random number generator for variability in actions
    protected Random rand = new Random();

    // Status trackers for different debuffs/effects
    protected int burnTurns, burnDamage;                    // Burning: take damage each turn
    protected int attackReductionTurns;                     // Reduced attack power duration
    protected double attackReductionPercent;                // How much attack is reduced
    protected int freezeTurns, stunTurns;                   // Number of turns frozen or stunned
    protected int reducedEnergyGainTurns;                   // Reduced energy gain duration
    protected int tempDodgeBonusTurns;                      // Temporary dodge boost duration
    protected int bleedTurns, bleedDamage;                  // Bleeding effect: take damage each turn

    // Constructor sets up all essential stats
    public GameCharacter(String name, int maxHealth, int baseAttack, int dodgeChance) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.baseAttack = baseAttack;
        this.energy = 0;
        this.healsLeft = 2; // Balanced to allow 2 heals per character
        this.dodgeChance = dodgeChance;
        // Default all debuffs to inactive
        this.burnTurns = 0; this.burnDamage = 0;
        this.attackReductionTurns = 0; this.attackReductionPercent = 0.0;
        this.freezeTurns = 0; this.stunTurns = 0;
        this.reducedEnergyGainTurns = 0;
        this.tempDodgeBonusTurns = 0;
        this.bleedTurns = 0; this.bleedDamage = 0;
    }

    // Returns character name
    public String getName() { return name; }
    // Returns current health
    public int getHealth() { return health; }
    // Returns max possible health
    public int getMaxHealth() { return maxHealth; }
    // Returns amount of energy
    public int getEnergy() { return energy; }
    // Returns number of heals left
    public int getHealsLeft() { return healsLeft; }
    // Returns true if alive (health above 0)
    public boolean isAlive() { return health > 0; }

    // Increase energy; debuffs can reduce effective gain
    protected void gainEnergy(int amt) {
        if (reducedEnergyGainTurns > 0) amt = Math.max(0, amt - 5);
        if (energy + amt > 100) energy = 100; else energy += amt;
    }

    // Reset energy to zero (used on ultimate)
    protected void spendAllEnergy() { energy = 0; }

    // Heal for a set amount of HP, if heals are available
    public void heal() {
        if (healsLeft == 0) {
            System.out.println(name + " has no heals left!");
            return;
        }
        healsLeft--;
        health += 40;
        if (health > maxHealth) health = maxHealth;
        System.out.println(name + " healed 40 HP! Heals left: " + healsLeft);
    }

    // Basic attack: does some damage and grants energy
    public int basicAttack() {
        gainEnergy(10);
        int dmg = baseAttack + rand.nextInt(6); // Adds randomness (0-5)
        dmg = applyAttackReduction(dmg);
        return applyCrit(dmg);
    }

    // If character is under attack reduction, damage is lowered
    protected int applyAttackReduction(int dmg) {
        if (attackReductionTurns > 0) dmg = (int) Math.max(0, dmg * (1.0 - attackReductionPercent));
        return dmg;
    }

    // Critical hit chance and multiplier (can be overridden by subclasses)
    protected int critChance() { return 20; }
    protected double critMultiplier() { return 1.5; }

    // Randomly determines if the hit is critical
    protected int applyCrit(int dmg) {
        if (rand.nextInt(100) < critChance()) {
            System.out.println("CRITICAL HIT!");
            return (int) (dmg * critMultiplier());
        }
        return dmg;
    }

    // Handles being attacked (returns true if hit, false if dodged)
    public boolean receiveDamage(int dmg) {
        if (isDodged()) {
            System.out.println(name + " dodged the attack!");
            return false;
        }
        health -= dmg;
        if (health < 0) health = 0;
        return true;
    }

    // Determines dodge success based on character’s dodge chance and temporary buffs
    protected boolean isDodged() {
        int effectiveDodge = dodgeChance;
        if (tempDodgeBonusTurns > 0) effectiveDodge += 20;
        return rand.nextInt(100) < effectiveDodge;
    }

    // Debuff applications: activate burning, attack reduction, freezing, etc.
    public void applyBurn(int turns, int dmgPerTurn) {
        burnTurns = Math.max(burnTurns, turns);
        burnDamage = Math.max(burnDamage, dmgPerTurn);
        System.out.println(name + " is burning for " + burnDamage + " HP for " + burnTurns + " turns!");
    }

    public void applyAttackReduction(int turns, double percent) {
        attackReductionTurns = Math.max(attackReductionTurns, turns);
        attackReductionPercent = Math.max(attackReductionPercent, percent);
        System.out.println(name + "'s attack reduced by " + (int) (percent*100) + "% for " + turns + " turns!");
    }

    public void applyFreeze(int turns) {
        freezeTurns = Math.max(freezeTurns, turns);
        System.out.println(name + " is frozen for " + turns + " turns!");
    }

    public void applyReduceEnergyGain(int turns) {
        reducedEnergyGainTurns = Math.max(reducedEnergyGainTurns, turns);
        System.out.println(name + "'s energy gain reduced for " + turns + " turns!");
    }

    public void applyTempDodgeBonus(int turns) {
        tempDodgeBonusTurns = Math.max(tempDodgeBonusTurns, turns);
        System.out.println(name + "'s dodge increased temporarily for " + turns + " turns!");
    }

    public void applyBleed(int turns, int dmg) {
        bleedTurns = Math.max(bleedTurns, turns);
        bleedDamage = Math.max(bleedDamage, dmg);
        System.out.println(name + " is bleeding for " + bleedDamage + " HP for " + bleedTurns + " turns!");
    }

    // Removes all active debuffs (mainly for StormSerpent skill)
    public void clearDebuffs() {
        burnTurns = 0; burnDamage = 0;
        attackReductionTurns = 0; attackReductionPercent = 0.0;
        freezeTurns = 0; stunTurns = 0;
        reducedEnergyGainTurns = 0; tempDodgeBonusTurns = 0;
        System.out.println(name + "'s debuffs were cleared!");
    }

    // Runs at the start of each turn: processes debuffs, burns, and timer reductions
    public void processTurnStart() {
        // Take burn damage
        if (burnTurns > 0) {
            System.out.println(name + " suffers " + burnDamage + " burn damage.");
            health -= burnDamage;
            if (health < 0) health = 0;
            burnTurns--; if (burnTurns == 0) burnDamage = 0;
        }
        // Take bleed damage
        if (bleedTurns > 0) {
            System.out.println(name + " suffers " + bleedDamage + " bleed damage.");
            health -= bleedDamage;
            if (health < 0) health = 0;
            bleedTurns--; if (bleedTurns == 0) bleedDamage = 0;
        }
        // Reduce any debuff timers
        if (attackReductionTurns > 0) {
            attackReductionTurns--;
            if (attackReductionTurns == 0) attackReductionPercent = 0.0;
        }
        if (freezeTurns > 0) freezeTurns--;
        if (reducedEnergyGainTurns > 0) reducedEnergyGainTurns--;
        if (tempDodgeBonusTurns > 0) tempDodgeBonusTurns--;
    }
    
    // Returns true if character is frozen currently
    public boolean isFrozen() { return freezeTurns > 0; }

    // Abstract methods for character’s special skills/abilities
    public abstract int skill1(GameCharacter target);
    public abstract int skill2(GameCharacter target);
    public abstract int skill3(GameCharacter target);
    public abstract int ultimate(GameCharacter target);
    public abstract void reduceCooldowns();
}
