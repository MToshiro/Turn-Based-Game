package main.characters;

import main.GameCharacter;

// Devourer class - adaptive fighter with lifesteal, growth, and scaling damage
public class Devourer extends Player {
    // Cooldowns for each skill
    private int cd1, cd2, cd3;
    // How much bonus damage from Consume passive (based on enemy's missing HP)
    private double consumeSkill = 0.50;
    // Total extra HP gained from Growth passive
    private int growthSkill = 0;
    // Turns survived so far for growth tracking
    private int turnsAlive = 0;
    // Multiplier for outgoing (next) attack, used by Charging Feast/Ultimate
    private double outgoingBuff = 1.0;
    // If next attack is empowered by a skill/ultimate
    private boolean empoweredAuto = false;
    // Turns remaining for buff from the ultimate
    private int outgoingBuffTurns = 0;

    // Set up Devourer with specific stats and initial cooldowns/buffs
    public Devourer() {
        super("Devourer", 115, 30, 10); // 10 dodge, high attack, low HP
        cd1 = cd2 = cd3 = 0;
    }

    // Slightly elevated crit chance/multiplier for Devourer
    @Override
    protected int critChance() { return 18; }
    @Override
    protected double critMultiplier() { return 2.0; }

    // PASSIVE: Deals more damage to low-HP enemies
    private double applyConsumePassive(GameCharacter enemy) {
        double missingHP = 1.0 - (double) enemy.getHealth() / enemy.getMaxHealth();
        return 1.0 + missingHP * consumeSkill;
    }

    // PASSIVE: Gain max HP every 3 turns (Grow stronger as the battle goes on)
    private void applyGrowth() {
        turnsAlive++;
        if (turnsAlive % 3 == 0) {
            growthSkill += 5;
            health += 5;
            if (health > maxHealth + growthSkill) health = maxHealth + growthSkill;
            System.out.println("Devourer’s body mutates and gains 5 HP!");
        }
    }

    // Each round, apply growth and expire outgoing buffs if any
    @Override
    public void processTurnStart() {
        super.processTurnStart();
        applyGrowth();
        if (outgoingBuffTurns > 0) outgoingBuffTurns--;
        if (outgoingBuffTurns == 0) outgoingBuff = 1.0;
    }

    // Skill 1: Devour – normal hit, boosted by passive and next-attack bonuses
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) {
            System.out.println("Devour cooling down (" + cd1 + " turns left)");
            return 0;
        }
        cd1 = 2;
        gainEnergy(15);
        double dmg = baseAttack * 1.1 * applyConsumePassive(target) * outgoingBuff;
        if (empoweredAuto) {
            dmg *= 1.5;
            empoweredAuto = false;
        }
        outgoingBuff = 1.0; // Reset single-use buff if present
        System.out.println("Devourer used DEVOUR!");
        return target.receiveDamage(applyCrit((int)dmg)) ? (int)dmg : 0;
    }

    // Skill 2: Gathered Soul – lifesteal hit that lowers enemy's attack
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) {
            System.out.println("Gathered Soul cooling down (" + cd2 + " turns left)");
            return 0;
        }
        cd2 = 3;
        gainEnergy(20);
        double dmg = baseAttack * 1.2 * applyConsumePassive(target) * outgoingBuff;
        if (empoweredAuto) {
            dmg *= 1.5;
            empoweredAuto = false;
        }
        outgoingBuff = 1.0;
        int finalDmg = (int)dmg;
        System.out.println("Devourer used GATHERED SOUL!");
        boolean dealt = target.receiveDamage(applyCrit(finalDmg));
        if (dealt) {
            int healAmount = (int) (finalDmg * 0.30); // Heal back 30% of damage dealt
            health += healAmount;
            if (health > maxHealth + growthSkill) health = maxHealth + growthSkill;
            System.out.println("Devourer heals " + healAmount + " HP!");
            target.applyAttackReduction(2, 0.10); // Debuff: reduce enemy attack
        }
        return dealt ? finalDmg : 0;
    }

    // Skill 3: Charging Feast – empowers your next attack by 35%
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) {
            System.out.println("Charging Feast cooling down (" + cd3 + " turns left)");
            return 0;
        }
        cd3 = 3;
        gainEnergy(25);
        outgoingBuff = 1.35;
        System.out.println("Devourer used CHARGING FEAST! Next attack empowered!");
        return 0; // Buff skill, no damage this turn
    }

    // Ultimate: Ravenous Entity – big hit, buffs next attacks for 3 turns
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) {
            System.out.println("Ultimate not ready or on cooldown.");
            return 0;
        }
        setUltimateOnCooldown(3);
        spendAllEnergy();
        empoweredAuto = true;
        outgoingBuff = 1.5;
        outgoingBuffTurns = 3;
        double dmg = baseAttack * 2.0 * applyConsumePassive(target) * outgoingBuff;
        System.out.println("DEVOURER becomes a RAVENOUS ENTITY! Damage massively increased! Total Growth: " + growthSkill + " HP");
        return target.receiveDamage(applyCrit((int)dmg)) ? (int)dmg : 0;
    }

    // Each turn, decrement skill cooldowns if not already zero
    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

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
