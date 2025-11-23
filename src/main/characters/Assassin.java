package main.characters;

import main.GameCharacter;

// Assassin class - a playable hero focused on dodge and burst damage
public class Assassin extends Player {
    // Tracks cooldowns for the three skills
    private int cd1, cd2, cd3;

    // Set up Assassin with specific stats and reset all skill cooldowns
    public Assassin() {
        super("Assassin", 130, 18, 15);
        cd1 = cd2 = cd3 = 0;
    }

    // Assassin has higher crit chance than most classes
    @Override
    protected int critChance() { return 25; }
    // Assassin crits do more damage
    @Override
    protected double critMultiplier() { return 2.5; }

    // Skill 1: Shadow Step (adds temporary dodge, no damage)
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) {
            System.out.println("Shadow Step cooling down (" + cd1 + " turns left)");
            return 0;
        }
        cd1 = 2;
        gainEnergy(10);
        System.out.println("Assassin used SHADOW STEP! Dodge greatly increased for 1 turn.");
        this.applyTempDodgeBonus(1);
        return 0; // No damage
    }

    // Skill 2: Backstab (big hit, short cooldown)
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) {
            System.out.println("Backstab cooling down (" + cd2 + " turns left)");
            return 0;
        }
        cd2 = 1;
        gainEnergy(25);
        int dmg = baseAttack + rand.nextInt(11) + 10; // 10–20 bonus
        System.out.println("Assassin used BACKSTAB!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 3: Smoke Screen (adds dodge, no damage)
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) {
            System.out.println("Smoke Screen cooling down (" + cd3 + " turns left)");
            return 0;
        }
        cd3 = 2;
        gainEnergy(20);
        System.out.println("Assassin used SMOKE SCREEN! Dodge greatly increased for 1 turn.");
        this.applyTempDodgeBonus(1);
        return 0; // No damage
    }

    // Ultimate: Silenced Strike (massive hit, requires full energy & cooldown)
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) {
            System.out.println("Ultimate not ready or on cooldown.");
            return 0;
        }
        setUltimateOnCooldown(4);
        spendAllEnergy();
        int dmg = (baseAttack * 3) + rand.nextInt(55) + 45; // Huge damage
        System.out.println("Assassin unleashes SILENCED STRIKE!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Each turn, reduce the cooldown of each skill (if above 0)
    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

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
