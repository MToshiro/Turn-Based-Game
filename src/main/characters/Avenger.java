package main.characters;

import main.GameCharacter;

public class Avenger extends Player {
    private int cd1, cd2, cd3;
    private boolean empoweredAuto; // Tracks if next attack is empowered
    private int outgoingBuffTurns; // Turns left for outgoing damage buff

    public Avenger() {
        super("Avenger", 150, 20, 15); // Tanky HP, moderate attack, 15% dodge
        cd1 = cd2 = cd3 = 0;
        empoweredAuto = false;
        outgoingBuffTurns = 0;
    }

    @Override
    protected int critChance() { return 20; } // Standard crit chance

    @Override
    protected double critMultiplier() { return 1.5; } // Standard crit multiplier

    // Helper to apply random debuff (adapted to existing mechanics)
    private void applyRandomDebuff(GameCharacter enemy) {
        int roll = rand.nextInt(3); // 3 possible debuffs (simplified)
        switch (roll) {
            case 0:
                enemy.applyAttackReduction(2, 0.15); // 15% attack reduction for 2 turns
                System.out.println("🔻 Enemy attack reduced!");
                break;
            case 1:
                enemy.applyBurn(2, 5); // Burn for 2 turns, 5 damage
                System.out.println("🔥 Enemy is burning!");
                break;
            case 2:
                enemy.applyReduceEnergyGain(2); // Reduce energy gain for 2 turns
                System.out.println("⚡ Enemy energy gain reduced!");
                break;
        }
    }

    @Override
    public int skill1(GameCharacter target) { // Crush (empowered attack)
        if (cd1 > 0) { System.out.println("❌ Crush cooling down (" + cd1 + " turns left)"); return 0; }
        cd1 = 2;
        gainEnergy(15);
        int dmg = baseAttack + 6; // 26 damage 
        if (empoweredAuto) {
            dmg *= 2; // Double damage if empowered
            empoweredAuto = false;
            System.out.println("⚡ Empowered! Deals double damage!");
        }
        if (outgoingBuffTurns > 0) dmg = (int) (dmg * 1.5); // Buffed damage
        System.out.println("💥 Avenger used CRUSH!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) { // Siphon (lifesteal)
        if (cd2 > 0) { System.out.println("❌ Siphon cooling down (" + cd2 + " turns left)"); return 0; }
        cd2 = 3;
        gainEnergy(20);
        empoweredAuto = true; // Empower next attack
        int dmg = baseAttack + 14; // 34 damage (balanced)
        if (outgoingBuffTurns > 0) dmg = (int) (dmg * 1.5);
        System.out.println("💚 Avenger used SIPHON!");
        boolean dealt = target.receiveDamage(applyCrit(dmg));
        if (dealt) {
            int healAmount = dmg / 3; // 30% lifesteal (integer)
            health += healAmount;
            if (health > maxHealth) health = maxHealth;
            System.out.println("💖 Avenger heals " + healAmount + " HP!");
        }
        return dealt ? dmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) { // Corruption (debuff + damage)
        if (cd3 > 0) { System.out.println("❌ Corruption cooling down (" + cd3 + " turns left)"); return 0; }
        cd3 = 3;
        gainEnergy(25);
        empoweredAuto = true; // Empower next attack
        int dmg = baseAttack + 8; // 28 damage
        if (outgoingBuffTurns > 0) dmg = (int) (dmg * 1.5);
        System.out.println("🌀 Avenger used CORRUPTION!");
        applyRandomDebuff(target); // Apply debuff
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int ultimate(GameCharacter target) { // Power Unleashed (buff + damage)
        if (!canUseUltimate()) { System.out.println("❌ Ultimate not ready or on cooldown."); return 0; }
        setUltimateOnCooldown(4);
        spendAllEnergy();
        empoweredAuto = true; // Empower next attack
        outgoingBuffTurns = 3; // Buff outgoing damage for 3 turns
        int dmg = baseAttack * 2 + 20; // 60 damage (balanced)
        System.out.println("🔥 AVENGER unleashes POWER UNLEASHED! Outgoing damage increased!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public void processTurnStart() {
        super.processTurnStart();
        if (outgoingBuffTurns > 0) outgoingBuffTurns--; // Reduce buff turns
    }

    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

    // Implement new methods
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