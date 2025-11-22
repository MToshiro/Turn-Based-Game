package main.characters;

import main.GameCharacter;

public class Devourer extends Player {
    private int cd1, cd2, cd3; // Cooldowns for skills
    private double consumeSkill = 0.50; // +50% bonus damage at low enemy HP
    private int growthSkill = 0;        // Increases HP every 3 turns (cumulative tracker)
    private int turnsAlive = 0;         // Track turns for growth
    private double outgoingBuff = 1.0;  // Damage multiplier for next attack
    private boolean empoweredAuto = false; // Tracks if next attack is empowered
    private int outgoingBuffTurns = 0; // Turns left for outgoing damage buff

    public Devourer() {
        super("Devourer", 115, 30, 10); // Adjusted dodge to 10% for balance (original didn't specify)
        cd1 = cd2 = cd3 = 0;
    }

    @Override
    protected int critChance() { return 18; } // 18% crit

    @Override
    protected double critMultiplier() { return 2.0; } // 200% crit damage

    // ====================================================
    // PASSIVE — CONSUME: Bonus damage based on enemy missing HP
    // ====================================================
    private double applyConsumePassive(GameCharacter enemy) {
        double missingHP = 1.0 - ((double) enemy.getHealth() / enemy.getMaxHealth());
        return 1.0 + (missingHP * consumeSkill);
    }

    // ====================================================
    // PASSIVE — GROWTH: Gain HP every 3 turns
    // ====================================================
    private void applyGrowth() {
        turnsAlive++;
        if (turnsAlive % 3 == 0) {
            growthSkill += 5;
            health += 5; // Directly increase health (no heal charges used)
            if (health > maxHealth) health = maxHealth;
            System.out.println("🌱 Devourer's body mutates and gains +5 HP!");
        }
    }

    @Override
    public void processTurnStart() {
        super.processTurnStart();
        applyGrowth(); // Apply growth at start of turn
        if (outgoingBuffTurns > 0) {
            outgoingBuffTurns--;
            if (outgoingBuffTurns == 0) outgoingBuff = 1.0; // Reset buff
        }
    }

    @Override
    public int skill1(GameCharacter target) { // Devour (Simple hit, affected by Consume passive)
        if (cd1 > 0) { System.out.println("❌ Devour cooling down (" + cd1 + " turns left)"); return 0; }
        cd1 = 2; // Cooldown
        gainEnergy(15);
        double dmg = baseAttack * 1.1;
        dmg *= applyConsumePassive(target);
        dmg *= outgoingBuff; // Apply any outgoing buff
        if (empoweredAuto) {
            dmg *= 1.5; // Empowered bonus
            empoweredAuto = false;
        }
        outgoingBuff = 1.0; // Reset after use
        System.out.println("🍖 Devourer used DEVOUR!");
        return target.receiveDamage(applyCrit((int) dmg)) ? (int) dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) { // Gathered Soul (Lifesteal + enemy outgoing debuff)
        if (cd2 > 0) { System.out.println("❌ Gathered Soul cooling down (" + cd2 + " turns left)"); return 0; }
        cd2 = 3; // Cooldown
        gainEnergy(20);
        double dmg = baseAttack * 1.2;
        dmg *= applyConsumePassive(target);
        dmg *= outgoingBuff;
        if (empoweredAuto) {
            dmg *= 1.5;
            empoweredAuto = false;
        }
        outgoingBuff = 1.0;
        int finalDmg = (int) dmg;
        System.out.println("💚 Devourer used GATHERED SOUL!");
        boolean dealt = target.receiveDamage(applyCrit(finalDmg));
        if (dealt) {
            int healAmount = (int) (finalDmg * 0.30); // 30% lifesteal
            health += healAmount;
            if (health > maxHealth) health = maxHealth;
            System.out.println("💖 Devourer heals " + healAmount + " HP!");
            target.applyAttackReduction(2, 0.10); // Reduce enemy attack by 10% for 2 turns (adapted from modifyDamage)
        }
        return dealt ? finalDmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) { // Charging Feast (Buff only, no damage)
        if (cd3 > 0) { System.out.println("❌ Charging Feast cooling down (" + cd3 + " turns left)"); return 0; }
        cd3 = 3; // Cooldown
        gainEnergy(25);
        outgoingBuff *= 1.35; // Increases next attack
        System.out.println("🌀 Devourer used CHARGING FEAST! Next attack empowered!");
        return 0; // No damage
    }

    @Override
    public int ultimate(GameCharacter target) { // Ravenous Entity (Burst + 3-turn Outgoing Buff) - Adapted from superSkill
        if (!canUseUltimate()) { System.out.println("❌ Ultimate not ready or on cooldown."); return 0; }
        setUltimateOnCooldown(3); // Cooldown
        spendAllEnergy();
        empoweredAuto = true;
        outgoingBuff *= 1.5; // +50% outgoing damage for 3 turns
        outgoingBuffTurns = 3;
        double dmg = baseAttack * 2.0;
        dmg *= applyConsumePassive(target);
        dmg *= outgoingBuff;
        System.out.println("🔥 DEVOURER becomes a RAVENOUS ENTITY! Damage massively increased! (Total Growth: " + growthSkill + " HP)");
        return target.receiveDamage(applyCrit((int) dmg)) ? (int) dmg : 0;
    }

    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

    // Implement new methods
    @Override
    public String getSkill1Name() { return "Devour"; }
    @Override
    public String getSkill2Name() { return "Gathered Soul"; }
    @Override
    public String getSkill3Name() { return "Charging Feast"; }
    @Override
    public String getUltimateName() { return "Ravenous Entity"; }
    @Override
    public int getSkill1Cooldown() { return cd1; }
    @Override
    public int getSkill2Cooldown() { return cd2; }
    @Override
    public int getSkill3Cooldown() { return cd3; }
}