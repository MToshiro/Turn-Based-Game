package main.characters;

import main.GameCharacter;

// Avenger class - a defensive, tanky hero who can debuff or empower
public class Avenger extends Player {
    // Cooldown trackers for all three skills
    private int cd1, cd2, cd3;
    // If true, next attack is empowered
    private boolean empoweredAuto;
    // Track number of turns left with outgoing damage buff
    private int outgoingBuffTurns;

    // Set up Avenger with stats and default cooldowns/buffs
    public Avenger() {
        super("Avenger", 150, 20, 15);
        cd1 = cd2 = cd3 = 0;
        empoweredAuto = false;
        outgoingBuffTurns = 0;
    }

    // Normal crit chance and crit multiplier
    @Override
    protected int critChance() { return 20; }
    @Override
    protected double critMultiplier() { return 1.5; }

    // Apply a random debuff to the enemy (attack reduction, burn, or reduced energy gain)
    private void applyRandomDebuff(GameCharacter enemy) {
        int roll = rand.nextInt(3);
        switch (roll) {
            case 0:
                enemy.applyAttackReduction(2, 0.15);
                System.out.println("Enemy attack reduced!");
                break;
            case 1:
                enemy.applyBurn(2, 5);
                System.out.println("Enemy is burning!");
                break;
            case 2:
                enemy.applyReduceEnergyGain(2);
                System.out.println("Enemy energy gain reduced!");
                break;
        }
    }

    // Skill 1: Crush - does damage, double damage if empowered
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) {
            System.out.println("Crush cooling down (" + cd1 + " turns left)");
            return 0;
        }
        cd1 = 2;
        gainEnergy(15);
        int dmg = baseAttack + 6; // base + 6, tweak for balancing
        if (empoweredAuto) {
            dmg *= 2;
            empoweredAuto = false;
            System.out.println("Empowered! Deals double damage!");
        }
        if (outgoingBuffTurns > 0) {
            dmg = (int) (dmg * 1.5);
        }
        System.out.println("Avenger used CRUSH!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 2: Siphon - does damage, heals Avenger, empowers next attack
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) {
            System.out.println("Siphon cooling down (" + cd2 + " turns left)");
            return 0;
        }
        cd2 = 3;
        gainEnergy(20);
        empoweredAuto = true;
        int dmg = baseAttack + 14;
        if (outgoingBuffTurns > 0) {
            dmg = (int) (dmg * 1.5);
        }
        System.out.println("Avenger used SIPHON!");
        boolean dealt = target.receiveDamage(applyCrit(dmg));
        if (dealt) {
            int healAmount = dmg / 3; // lifesteal: heal 1/3 of damage
            health += healAmount;
            if (health > maxHealth) health = maxHealth;
            System.out.println("Avenger heals " + healAmount + " HP!");
        }
        return dealt ? dmg : 0;
    }

    // Skill 3: Corruption - deals damage, empowers next attack, applies random debuff
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) {
            System.out.println("Corruption cooling down (" + cd3 + " turns left)");
            return 0;
        }
        cd3 = 3;
        gainEnergy(25);
        empoweredAuto = true;
        int dmg = baseAttack + 8;
        if (outgoingBuffTurns > 0) {
            dmg = (int) (dmg * 1.5);
        }
        System.out.println("Avenger used CORRUPTION!");
        applyRandomDebuff(target);
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Ultimate: Power Unleashed - empowers and buffs outgoing damage for 3 turns
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) {
            System.out.println("Ultimate not ready or on cooldown.");
            return 0;
        }
        setUltimateOnCooldown(4);
        spendAllEnergy();
        empoweredAuto = true;
        outgoingBuffTurns = 3;
        int dmg = (baseAttack * 2) + 20; // adjust as needed for balance
        System.out.println("AVENGER unleashes POWER UNLEASHED! Outgoing damage increased!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // At the start of each turn, decrease buff duration
    @Override
    public void processTurnStart() {
        super.processTurnStart();
        if (outgoingBuffTurns > 0) outgoingBuffTurns--;
    }

    // Reduce each skill's cooldown
    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

    @Override
    public String getSkill1Name() { return "Crush"; }
    @Override
    public String getSkill2Name() { return "Siphon"; }
    @Override
    public String getSkill3Name() { return "Corruption"; }
    @Override
    public String getUltimateName() { return "Power Unleashed"; }
    @Override
    public int getSkill1Cooldown() { return cd1; }
    @Override
    public int getSkill2Cooldown() { return cd2; }
    @Override
    public int getSkill3Cooldown() { return cd3; }
}
