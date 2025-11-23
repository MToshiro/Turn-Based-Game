package main.characters;

import main.GameCharacter;

// Necromancer class - dark mage with lifesteal, ally summoning, and freezing attacks
public class Necromancer extends Player {
    // Cooldowns for each skill
    private int cd1, cd2, cd3;
    // Tracks if a summoned skeleton will empower the next attack
    private boolean skeletonSummoned;

    // Set up Necromancer with stats and default skill/summon states
    public Necromancer() {
        super("Necromancer", 140, 18, 12); // Moderate HP, low attack, moderate dodge
        cd1 = cd2 = cd3 = 0;
        skeletonSummoned = false;
    }

    // Higher crit chance (dark magic takes more risks)
    @Override
    protected int critChance() { return 30; }
    @Override
    protected double critMultiplier() { return 1.5; }

    // Skill 1: Bone Spike – reduces enemy dodge for this strike (piercing)
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) {
            System.out.println("Bone Spike cooling down (" + cd1 + " turns left)");
            return 0;
        }
        cd1 = 2;
        gainEnergy(15);
        int dmg = baseAttack + 8;
        // Temporarily make enemy easier to hit for piercing effect
        int originalDodge = target.dodgeChance;
        target.dodgeChance = Math.max(0, target.dodgeChance - 10);
        boolean hit = target.receiveDamage(applyCrit(dmg));
        target.dodgeChance = originalDodge; // Restore original dodge
        System.out.println("Necromancer used BONE SPIKE!");
        return hit ? dmg : 0;
    }

    // Skill 2: Life Drain – attack and heal self for half the damage dealt
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) {
            System.out.println("Life Drain cooling down (" + cd2 + " turns left)");
            return 0;
        }
        cd2 = 3;
        gainEnergy(20);
        int dmg = baseAttack + 12;
        System.out.println("Necromancer used LIFE DRAIN!");
        boolean hit = target.receiveDamage(applyCrit(dmg));
        if (hit) {
            int healAmount = dmg / 2;
            health += healAmount;
            if (health > maxHealth) health = maxHealth;
            System.out.println("Necromancer drains " + healAmount + " HP!");
        }
        return hit ? dmg : 0;
    }

    // Skill 3: Summon Skeleton – empowers next attack (extra damage only once)
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) {
            System.out.println("Summon Skeleton cooling down (" + cd3 + " turns left)");
            return 0;
        }
        cd3 = 4;
        gainEnergy(25);
        skeletonSummoned = true;
        System.out.println("Necromancer summons a SKELETON! Next attack is empowered!");
        return 0; // Does not do damage, buffs next attack
    }

    // Ultimate: Death Wave – big attack and has a chance to freeze the enemy
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) {
            System.out.println("Ultimate not ready or on cooldown.");
            return 0;
        }
        setUltimateOnCooldown(5);
        spendAllEnergy();
        int dmg = baseAttack * 2 + 30;
        System.out.println("Necromancer unleashes DEATH WAVE!");
        boolean hit = target.receiveDamage(applyCrit(dmg));
        if (hit) {
            target.applyFreeze(1); // Freeze enemy for one turn
        }
        return hit ? dmg : 0;
    }

    // Override basic attack to add skeleton bonus (if summoned)
    @Override
    public int basicAttack() {
        int dmg = super.basicAttack();
        if (skeletonSummoned) {
            dmg += 15;
            System.out.println("Skeleton assists! 15 damage!");
            skeletonSummoned = false;
        }
        return dmg;
    }

    // Each turn, reduce cooldowns for all three skills (if active)
    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

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
