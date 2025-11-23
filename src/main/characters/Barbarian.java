package main.characters;

import main.GameCharacter;

// Barbarian class - high damage melee fighter with a rage mechanic
public class Barbarian extends Player {
    // Cooldowns for all three skills
    private int cd1, cd2, cd3;
    // Whether the Barbarian is currently in Rage mode
    private boolean inRage;

    // Set up Barbarian stats and initial cooldowns/rage state
    public Barbarian() {
        super("Barbarian", 150, 22, 10);
        cd1 = cd2 = cd3 = 0;
        inRage = false;
    }

    // Lower crit chance and multiplier for Barbarian (defaults for this hero)
    @Override
    protected int critChance() { return 10; }
    @Override
    protected double critMultiplier() { return 1.0; }

    // Check for rage activation: triggers if health drops below 20% and not already enraged
    private void checkRage() {
        if (!inRage && health <= (int) (maxHealth * 0.2)) {
            inRage = true;
            System.out.println("BARBARIAN ENTERS RAGE FORM! Damage doubled, defense reduced!");
        }
    }

    // If in rage, deal double damage for skills/attacks
    private int applyRageDamage(int dmg) {
        return inRage ? dmg * 2 : dmg;
    }

    // While in rage, Barbarian also takes double damage
    @Override
    public boolean receiveDamage(int dmg) {
        if (inRage) dmg *= 2;
        boolean hit = super.receiveDamage(dmg);
        checkRage(); // See if rage should now activate
        return hit;
    }

    // Skill 1: Lethal Swing (quick attack, low cooldown)
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) {
            System.out.println("Lethal Swing cooling down (" + cd1 + " turns left)");
            return 0;
        }
        cd1 = 2;
        checkRage();
        gainEnergy(15);
        int dmg = baseAttack + 6;
        dmg = applyRageDamage(dmg);
        System.out.println("Barbarian used LETHAL SWING!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 2: Bone Crusher (heavy attack)
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) {
            System.out.println("Bone Crusher cooling down (" + cd2 + " turns left)");
            return 0;
        }
        cd2 = 3;
        checkRage();
        gainEnergy(20);
        int dmg = baseAttack + 13;
        dmg = applyRageDamage(dmg);
        System.out.println("Barbarian used BONE CRUSHER!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 3: Furious Flail (strongest attack outside of ultimate)
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) {
            System.out.println("Furious Flail cooling down (" + cd3 + " turns left)");
            return 0;
        }
        cd3 = 4;
        checkRage();
        gainEnergy(25);
        int dmg = baseAttack + 19;
        dmg = applyRageDamage(dmg);
        System.out.println("Barbarian used FURIOUS FLAIL!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Ultimate: Primal Annihilation (massive damage; rage amplifies it even more)
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) {
            System.out.println("Ultimate not ready or on cooldown.");
            return 0;
        }
        setUltimateOnCooldown(5);
        checkRage();
        spendAllEnergy();
        int dmg = baseAttack * 4;
        dmg = applyRageDamage(dmg);
        System.out.println("BARBARIAN unleashes PRIMAL ANNIHILATION!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Each turn, reduce each skill’s cooldown timer if active
    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

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
