package main.characters;

import main.GameCharacter;

// Inquisitor class - balanced hero with debuffs, self-heals, and crowd control
public class Inquisitor extends Player {
    // Cooldown trackers for the three skills
    private int cd1, cd2, cd3;

    // Set up Inquisitor stats and initial cooldowns
    public Inquisitor() {
        super("Inquisitor", 145, 19, 12);
        cd1 = cd2 = cd3 = 0;
    }

    // Standard crit chance and multiplier
    @Override
    protected int critChance() { return 20; }
    @Override
    protected double critMultiplier() { return 1.5; }

    // Skill 1: Holy Judgment – hit and 50% chance to reduce enemy's attack
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) {
            System.out.println("Holy Judgment cooling down (" + cd1 + " turns left)");
            return 0;
        }
        cd1 = 3;
        gainEnergy(20);
        int dmg = baseAttack + 12;
        System.out.println("Inquisitor casts HOLY JUDGMENT!");
        boolean hit = target.receiveDamage(applyCrit(dmg));
        if (hit && rand.nextInt(100) < 50) {
            target.applyAttackReduction(2, 0.20);
        }
        return hit ? dmg : 0;
    }

    // Skill 2: Purify – heal self and gain temporary dodge
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) {
            System.out.println("Purify cooling down (" + cd2 + " turns left)");
            return 0;
        }
        cd2 = 4;
        gainEnergy(25);
        System.out.println("Inquisitor uses PURIFY! Heals self and buffs dodge.");
        int healAmount = 30;
        health += healAmount;
        if (health > maxHealth) health = maxHealth;
        this.applyTempDodgeBonus(2);
        return 0; // Utility skill, no direct damage
    }

    // Skill 3: Interrogation – hit and 40% chance to freeze enemy for 1 turn
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) {
            System.out.println("Interrogation cooling down (" + cd3 + " turns left)");
            return 0;
        }
        cd3 = 3;
        gainEnergy(20);
        int dmg = baseAttack + 10;
        System.out.println("Inquisitor performs INTERROGATION!");
        boolean hit = target.receiveDamage(applyCrit(dmg));
        if (hit && rand.nextInt(100) < 40) {
            target.applyFreeze(1);
        }
        return hit ? dmg : 0;
    }

    // Ultimate: Divine Inquisition – multi-hit, debuffs (attack down and burn)
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) {
            System.out.println("Ultimate not ready or on cooldown.");
            return 0;
        }
        setUltimateOnCooldown(5);
        spendAllEnergy();
        int hits = 3;
        int totalDmg = 0;
        System.out.println("Inquisitor unleashes DIVINE INQUISITION!");
        for (int i = 0; i < hits; i++) {
            int dmg = baseAttack + 8 + rand.nextInt(11); // 8–18 bonus per hit
            boolean hit = target.receiveDamage(applyCrit(dmg));
            if (hit) {
                totalDmg += dmg;
                target.applyAttackReduction(3, 0.25);
                target.applyBurn(3, 7);
            }
        }
        return totalDmg;
    }

    // Each turn, decrease cooldowns for all three skills (if active)
    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

    @Override
    public String getSkill1Name() { return "Holy Judgment"; }
    @Override
    public String getSkill2Name() { return "Purify"; }
    @Override
    public String getSkill3Name() { return "Interrogation"; }
    @Override
    public String getUltimateName() { return "Divine Inquisition"; }
    @Override
    public int getSkill1Cooldown() { return cd1; }
    @Override
    public int getSkill2Cooldown() { return cd2; }
    @Override
    public int getSkill3Cooldown() { return cd3; }
}
