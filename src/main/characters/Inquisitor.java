package main.characters;

import main.GameCharacter;

public class Inquisitor extends Player {
    private int cd1, cd2, cd3;

    public Inquisitor() {
        super("Inquisitor", 145, 19, 12);  // Balanced: Moderate HP/tankiness, solid attack, decent dodge
        cd1 = cd2 = cd3 = 0;
    }

    @Override
    protected int critChance() { return 20; }  // Standard crit chance

    @Override
    protected double critMultiplier() { return 1.5; }  // Standard crit multiplier

    @Override
    public int skill1(GameCharacter target) {  // Holy Judgment (damage + attack reduction debuff)
        if (cd1 > 0) { System.out.println("❌ Holy Judgment cooling down (" + cd1 + " turns left)"); return 0; }
        cd1 = 3;
        gainEnergy(20);
        int dmg = baseAttack + 12;  // 31 damage
        System.out.println("⚖️ Inquisitor casts HOLY JUDGMENT!");
        boolean hit = target.receiveDamage(applyCrit(dmg));
        if (hit && rand.nextInt(100) < 50) target.applyAttackReduction(2, 0.20);  // 50% chance to reduce attack by 20% for 2 turns
        return hit ? dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) {  // Purify (self-heal + temporary dodge buff)
        if (cd2 > 0) { System.out.println("❌ Purify cooling down (" + cd2 + " turns left)"); return 0; }
        cd2 = 4;
        gainEnergy(25);
        System.out.println("✨ Inquisitor uses PURIFY! (heals and buffs dodge)");
        // Self-heal (partial, balanced)
        int healAmount = 30;
        health += healAmount;
        if (health > maxHealth) health = maxHealth;
        System.out.println("💖 Inquisitor heals " + healAmount + " HP!");
        // Buff: Temporary dodge increase
        this.applyTempDodgeBonus(2);  // +20% dodge for 2 turns
        return 0;  // Utility skill, no damage
    }

    @Override
    public int skill3(GameCharacter target) {  // Interrogation (damage + freeze chance)
        if (cd3 > 0) { System.out.println("❌ Interrogation cooling down (" + cd3 + " turns left)"); return 0; }
        cd3 = 3;
        gainEnergy(20);
        int dmg = baseAttack + 10;  // 29 damage
        System.out.println("🔍 Inquisitor performs INTERROGATION! (chance to freeze)");
        boolean hit = target.receiveDamage(applyCrit(dmg));
        if (hit && rand.nextInt(100) < 40) target.applyFreeze(1);  // 40% chance to freeze for 1 turn
        return hit ? dmg : 0;
    }

    @Override
    public int ultimate(GameCharacter target) {  // Divine Inquisition (multi-hit damage + debuffs)
        if (!canUseUltimate()) { System.out.println("❌ Ultimate not ready or on cooldown."); return 0; }
        setUltimateOnCooldown(5);
        spendAllEnergy();
        int hits = 3;  // Multi-hit
        int totalDmg = 0;
        System.out.println("🌟 Inquisitor unleashes DIVINE INQUISITION! (judgment strikes with debuffs)");
        for (int i = 0; i < hits; i++) {
            int dmg = baseAttack + 8 + rand.nextInt(11);  // 27-37 per hit
            boolean hit = target.receiveDamage(applyCrit(dmg));
            if (hit) totalDmg += dmg;
        }
        // Debuffs: Attack reduction and burn
        target.applyAttackReduction(3, 0.25);  // Reduce attack by 25% for 3 turns
        target.applyBurn(3, 7);  // Burn for 7 damage per turn for 3 turns
        return totalDmg;
    }

    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

    // Implement new methods
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
