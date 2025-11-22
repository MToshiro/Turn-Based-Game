package main;
import java.util.Random;

public abstract class GameCharacter {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int baseAttack;
    protected int energy; // 0-100
    protected int healsLeft; // 2 heals (reduced for balance)
    public int dodgeChance; // percent
    protected Random rand = new Random();

    // Debuff / Buff trackers
    protected int burnTurns; // damage over time each turn
    protected int burnDamage;
    protected int attackReductionTurns;
    protected double attackReductionPercent; // e.g., 0.2 for 20%
    protected int freezeTurns; // skip turns
    protected int stunTurns; // similar to freeze
    protected int reducedEnergyGainTurns; // reduces energy gain
    protected int tempDodgeBonusTurns; // temporary dodge increase (for FrostWraith)
    protected int bleedTurns; // damage over time each turn
    protected int bleedDamage;

    public GameCharacter(String name, int maxHealth, int baseAttack, int dodgeChance) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.baseAttack = baseAttack;
        this.energy = 0;
        this.healsLeft = 2; // Reduced from 3 for balance
        this.dodgeChance = dodgeChance;

        // debuffs default
        this.burnTurns = 0;
        this.burnDamage = 0;
        this.attackReductionTurns = 0;
        this.attackReductionPercent = 0.0;
        this.freezeTurns = 0;
        this.stunTurns = 0;
        this.reducedEnergyGainTurns = 0;
        this.tempDodgeBonusTurns = 0;
        this.bleedTurns = 0;
        this.bleedDamage = 0;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getEnergy() { return energy; }
    public int getHealsLeft() { return healsLeft; }
    public boolean isAlive() { return health > 0; }

    // energy management
    protected void gainEnergy(int amt) {
        if (reducedEnergyGainTurns > 0) amt = Math.max(0, amt - 5); // example reduction
        energy += amt;
        if (energy > 100) energy = 100;
    }

    protected void spendAllEnergy() { energy = 0; }

    // Healing (increased to 40 HP for balance)
    public void heal() {
        if (healsLeft <= 0) {
            System.out.println("❌ " + name + " has no heals left!");
            return;
        }
        healsLeft--;
        health += 40;
        if (health > maxHealth) health = maxHealth;
        System.out.println("💖 " + name + " healed 40 HP! (Heals left: " + healsLeft + ")");
    }

    // Base attack: returns damage
    public int basicAttack() {
        int gain = 10;
        gainEnergy(gain);
        int dmg = baseAttack + rand.nextInt(6); // slight variability
        dmg = applyAttackReduction(dmg);
        return applyCrit(dmg);
    }

    protected int applyAttackReduction(int dmg) {
        if (attackReductionTurns > 0) {
            dmg = (int) Math.max(0, dmg * (1.0 - attackReductionPercent));
        }
        return dmg;
    }

    // Crit system (default 20% for players, enemies override)
    protected int critChance() { return 20; }
    protected double critMultiplier() { return 1.5; }

    protected int applyCrit(int dmg) {
        if (rand.nextInt(100) < critChance()) {
            System.out.println("💥 CRITICAL HIT!");
            return (int) (dmg * critMultiplier());
        }
        return dmg;
    }

    // Damage application that checks dodge. Returns true if damage applied (not dodged)
    public boolean receiveDamage(int dmg) {
        if (isDodged()) {
            System.out.println("➡️ " + name + " dodged the attack!");
            return false;
        }
        health -= dmg;
        if (health < 0) health = 0;
        return true;
    }

    protected boolean isDodged() {
        int effectiveDodge = dodgeChance;
        if (tempDodgeBonusTurns > 0) effectiveDodge += 20; // Temporary bonus
        return rand.nextInt(100) < effectiveDodge;
    }

    // Debuff application helpers
    public void applyBurn(int turns, int dmgPerTurn) {
        burnTurns = Math.max(burnTurns, turns);
        burnDamage = Math.max(burnDamage, dmgPerTurn);
        System.out.println(name + " is burning for " + burnDamage + " HP for " + burnTurns + " turn(s)! 🔥");
    }

    public void applyAttackReduction(int turns, double percent) {
        attackReductionTurns = Math.max(attackReductionTurns, turns);
        attackReductionPercent = Math.max(attackReductionPercent, percent);
        System.out.println(name + "'s attack reduced by " + (int)(percent*100) + "% for " + turns + " turn(s)! ⚠️");
    }

    public void applyFreeze(int turns) {
        freezeTurns = Math.max(freezeTurns, turns);
        System.out.println(name + " is frozen for " + turns + " turn(s)! ❄️");
    }

    public void applyReduceEnergyGain(int turns) {
        reducedEnergyGainTurns = Math.max(reducedEnergyGainTurns, turns);
        System.out.println(name + "'s energy gain reduced for " + turns + " turn(s)! ⚡");
    }

    public void applyTempDodgeBonus(int turns) {
        tempDodgeBonusTurns = Math.max(tempDodgeBonusTurns, turns);
        System.out.println(name + "'s dodge increased temporarily for " + turns + " turn(s)! 🛡️");
    }
    public void applyBleed(int turns) {
        bleedTurns = Math.max(bleedTurns, turns);
        bleedDamage = Math.max(bleedDamage, 5); // Example: 5 bleed damage per turn (adjust as needed)
        System.out.println(name + " is bleeding for " + bleedDamage + " HP for " + bleedTurns + " turn(s)! 🩸");
    }

    // Clear debuffs (for StormSerpent)
    public void clearDebuffs() {
        burnTurns = 0;
        burnDamage = 0;
        attackReductionTurns = 0;
        attackReductionPercent = 0.0;
        freezeTurns = 0;
        stunTurns = 0;
        reducedEnergyGainTurns = 0;
        tempDodgeBonusTurns = 0;
        System.out.println(name + "'s debuffs were cleared!");
    }

    // Called each round to process DoT and reduce debuff timers
    public void processTurnStart() {
        if (burnTurns > 0) {
            System.out.println(name + " suffers " + burnDamage + " burn damage.");
            health -= burnDamage;
            if (health < 0) health = 0;
            burnTurns--;
            if (burnTurns == 0) burnDamage = 0;
        }
        if (bleedTurns > 0) {
            System.out.println(name + " suffers " + bleedDamage + " bleed damage.");
            health -= bleedDamage;
            if (health < 0) health = 0;
            bleedTurns--;
            if (bleedTurns == 0) bleedDamage = 0;
        }
        if (attackReductionTurns > 0) {
            attackReductionTurns--;
            if (attackReductionTurns == 0) attackReductionPercent = 0.0;
        }
        if (freezeTurns > 0) {
            freezeTurns--;
        }
        if (reducedEnergyGainTurns > 0) {
            reducedEnergyGainTurns--;
        }
        if (tempDodgeBonusTurns > 0) {
            tempDodgeBonusTurns--;
        }
    }

    public boolean isFrozen() { return freezeTurns > 0; }

    // Character-specific actions (skills / ultimate)
    public abstract int skill1(GameCharacter target);
    public abstract int skill2(GameCharacter target);
    public abstract int skill3(GameCharacter target);
    public abstract int ultimate(GameCharacter target);

    public abstract void reduceCooldowns();
}