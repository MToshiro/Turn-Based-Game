package main.characters;

import main.GameCharacter;

public class Assassin extends Player {
    private int cd1, cd2, cd3;

    public Assassin() {
        super("Assassin", 130, 18, 15);
        cd1 = cd2 = cd3 = 0;
    }

    @Override
    protected int critChance() { return 25; } // 25% crit chance

    @Override
    protected double critMultiplier() { return 2.5; } // 250% crit damage

    @Override
    public int skill1(GameCharacter target) { // Shadow Step (dodge buff, no damage)
        if (cd1 > 0) { System.out.println("❌ Shadow Step cooling down (" + cd1 + " turns left)"); return 0; }
        cd1 = 2;
        gainEnergy(10);
        System.out.println("👤 Assassin used SHADOW STEP! (temporary dodge boost)");
        this.applyTempDodgeBonus(1); // 1-turn dodge boost
        return 0; // Buff skill, no damage
    }

    @Override
    public int skill2(GameCharacter target) { // Backstab (heavy attack)
        if (cd2 > 0) { System.out.println("❌ Backstab cooling down (" + cd2 + " turns left)"); return 0; }
        cd2 = 1;
        gainEnergy(25);
        int dmg = baseAttack + 10; // 18 + 10 = 28 damage 
        System.out.println("🗡️ Assassin used BACKSTAB!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) { // Smoke Screen (dodge buff)
        if (cd3 > 0) { System.out.println("❌ Smoke Screen cooling down (" + cd3 + " turns left)"); return 0; }
        cd3 = 2;
        gainEnergy(20);
        System.out.println("💨 Assassin used SMOKE SCREEN! (temporary dodge boost)");
        this.applyTempDodgeBonus(1); // 1-turn dodge boost
        return 0; // Buff skill, no damage
    }

    @Override
    public int ultimate(GameCharacter target) { // Silenced Strike (massive attack)
        if (!canUseUltimate()) { System.out.println("❌ Ultimate not ready or on cooldown."); return 0; }
        setUltimateOnCooldown(4);
        spendAllEnergy();
        int dmg = baseAttack * 3 + 45; // 18*3 + 45 = 99 damage 
        System.out.println("⚔️ Assassin unleashes SILENCED STRIKE!");
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
    public String getSkill1Name() { return "Shadow Step"; }
    @Override
    public String getSkill2Name() { return "Backstab"; }
    @Override
    public String getSkill3Name() { return "Smoke Screen"; }
    @Override
    public String getUltimateName() { return "Silenced Strike"; }
    @Override
    public int getSkill1Cooldown() { return cd1; }
    @Override
    public int getSkill2Cooldown() { return cd2; }
    @Override
    public int getSkill3Cooldown() { return cd3; }
}