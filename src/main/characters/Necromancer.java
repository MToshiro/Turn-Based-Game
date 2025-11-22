package main.characters;

import main.GameCharacter;

public class Necromancer extends Player {
    private int cd1, cd2, cd3;
    private boolean skeletonSummoned; // Tracks if skeleton is active for next attack

    public Necromancer() {
        super("Necromancer", 140, 18, 12); // Moderate HP, low attack, 12% dodge
        cd1 = cd2 = cd3 = 0;
        skeletonSummoned = false;
    }

    @Override
    protected int critChance() { return 30; } // High crit chance for dark magic theme

    @Override
    protected double critMultiplier() { return 1.5; } // Standard crit multiplier

    @Override
    public int skill1(GameCharacter target) { // Bone Spike (piercing attack, reduced dodge effectiveness)
        if (cd1 > 0) { System.out.println("❌ Bone Spike cooling down (" + cd1 + " turns left)"); return 0; }
        cd1 = 2;
        gainEnergy(15);
        int dmg = baseAttack + 8; // 26 damage
        // Simulate piercing: temporarily reduce target's dodge for this attack
        int originalDodge = target.dodgeChance;
        target.dodgeChance = Math.max(0, target.dodgeChance - 10); // Reduce dodge by 10% for this hit
        boolean hit = target.receiveDamage(applyCrit(dmg));
        target.dodgeChance = originalDodge; // Restore dodge
        System.out.println("🦴 Necromancer used BONE SPIKE! (pierces defenses)");
        return hit ? dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) { // Life Drain (damage + lifesteal)
        if (cd2 > 0) { System.out.println("❌ Life Drain cooling down (" + cd2 + " turns left)"); return 0; }
        cd2 = 3;
        gainEnergy(20);
        int dmg = baseAttack + 12; // 30 damage
        System.out.println("💀 Necromancer used LIFE DRAIN!");
        boolean hit = target.receiveDamage(applyCrit(dmg));
        if (hit) {
            int healAmount = dmg / 2; // 50% lifesteal
            health += healAmount;
            if (health > maxHealth) health = maxHealth;
            System.out.println("💖 Necromancer drains " + healAmount + " HP!");
        }
        return hit ? dmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) { // Summon Skeleton (buff for next attack)
        if (cd3 > 0) { System.out.println("❌ Summon Skeleton cooling down (" + cd3 + " turns left)"); return 0; }
        cd3 = 4;
        gainEnergy(25);
        skeletonSummoned = true; // Empower next attack
        System.out.println("🦴 Necromancer summons a SKELETON! Next attack is empowered!");
        return 0; // Buff skill, no damage
    }

    @Override
    public int ultimate(GameCharacter target) { // Death Wave (strong damage + freeze debuff)
        if (!canUseUltimate()) { System.out.println("❌ Ultimate not ready or on cooldown."); return 0; }
        setUltimateOnCooldown(5);
        spendAllEnergy();
        int dmg = baseAttack * 2 + 30; // 66 damage
        System.out.println("💀 Necromancer unleashes DEATH WAVE!");
        boolean hit = target.receiveDamage(applyCrit(dmg));
        if (hit) target.applyFreeze(1); // Freeze for 1 turn
        return hit ? dmg : 0;
    }

    @Override
    public int basicAttack() { // Override to include skeleton buff
        int dmg = super.basicAttack(); // Call parent for energy/damage calc
        if (skeletonSummoned) {
            dmg += 15; // Extra damage from skeleton
            System.out.println("🦴 Skeleton assists! +15 damage!");
            skeletonSummoned = false; // Reset after use
        }
        return dmg;
    }

    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

    // Implement new methods
    @Override
    public String getSkill1Name() { return "Bone Spike"; }
    @Override
    public String getSkill2Name() { return "Life Drain"; }
    @Override
    public String getSkill3Name() { return "Summon Skeleton"; }
    @Override
    public String getUltimateName() { return "Death Wave"; }
    @Override
    public int getSkill1Cooldown() { return cd1; }
    @Override
    public int getSkill2Cooldown() { return cd2; }
    @Override
    public int getSkill3Cooldown() { return cd3; }
}