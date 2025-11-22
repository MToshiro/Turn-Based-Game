package main.characters;

import main.GameCharacter;

public class Barbarian extends Player {
    private int cd1, cd2, cd3;
    private boolean inRage; // Rage state

    public Barbarian() {
        super("Barbarian", 150, 22, 10);
        cd1 = cd2 = cd3 = 0;
        inRage = false;
    }

    @Override
    protected int critChance() { return 10; } // Low crit chance

    @Override
    protected double critMultiplier() { return 1.0; } // Basic crit damage

    // Passive: Check and activate rage at low HP
    private void checkRage() {
        if (!inRage && health <= maxHealth * 0.2) { // 20% HP
            inRage = true;
            System.out.println("🔥 BARBARIAN ENTERS RAGE FORM! Damage doubled, defense reduced!");
        }
    }

    // Modify outgoing damage based on rage
    private int applyRageDamage(int dmg) {
        return inRage ? dmg * 2 : dmg;
    }

    // Override receiveDamage to apply rage penalty
    @Override
    public boolean receiveDamage(int dmg) {
        if (inRage) {
            dmg *= 2; // Takes double damage while enraged
        }
        boolean hit = super.receiveDamage(dmg);
        checkRage(); // Rage may activate after damage
        return hit;
    }

    @Override
    public int skill1(GameCharacter target) { // Lethal Swing (light attack)
        if (cd1 > 0) { System.out.println("❌ Lethal Swing cooling down (" + cd1 + " turns left)"); return 0; }
        cd1 = 2;
        checkRage();
        gainEnergy(15);
        int dmg = baseAttack + 6; // 28 damage
        dmg = applyRageDamage(dmg);
        System.out.println("⚔️ Barbarian used LETHAL SWING!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) { // Bone Crusher (heavy attack)
        if (cd2 > 0) { System.out.println("❌ Bone Crusher cooling down (" + cd2 + " turns left)"); return 0; }
        cd2 = 3;
        checkRage();
        gainEnergy(20);
        int dmg = baseAttack + 13; // 35 damage
        dmg = applyRageDamage(dmg);
        System.out.println("💥 Barbarian used BONE CRUSHER!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) { // Furious Flail (projectile attack)
        if (cd3 > 0) { System.out.println("❌ Furious Flail cooling down (" + cd3 + " turns left)"); return 0; }
        cd3 = 4;
        checkRage();
        gainEnergy(25);
        int dmg = baseAttack + 19; // 41 damage
        dmg = applyRageDamage(dmg);
        System.out.println("🌪️ Barbarian used FURIOUS FLAIL!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int ultimate(GameCharacter target) { // Primal Annihilation
        if (!canUseUltimate()) { System.out.println("❌ Ultimate not ready or on cooldown."); return 0; }
        setUltimateOnCooldown(5);
        checkRage();
        spendAllEnergy();
        int dmg = baseAttack * 4; // 88 damage
        dmg = applyRageDamage(dmg);
        System.out.println("💥 BARBARIAN unleashes PRIMAL ANNIHILATION!");
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
    public String getSkill1Name() { return "Lethal Swing"; }
    @Override
    public String getSkill2Name() { return "Bone Crusher"; }
    @Override
    public String getSkill3Name() { return "Furious Flail"; }
    @Override
    public String getUltimateName() { return "Primal Annihilation"; }
    @Override
    public int getSkill1Cooldown() { return cd1; }
    @Override
    public int getSkill2Cooldown() { return cd2; }
    @Override
    public int getSkill3Cooldown() { return cd3; }
}