package main.characters;

import main.GameCharacter;

// Shapeshifter class - can transform between three forms (Human, Dragon, Golem)
// Each form enables different skills and effects
public class Shapeshifter extends Player {
    // Cooldowns for all three skills
    private int cd1, cd2, cd3;

    // Enum for tracking current form
    public enum Form { HUMAN, DRAGON, GOLEM }
    public Form currentForm = Form.HUMAN;

    // Set up Shapeshifter stats and cooldowns
    public Shapeshifter() {
        super("Shapeshifter", 140, 20, 10); // Balanced: moderate HP, attack, low dodge
        cd1 = cd2 = cd3 = 0;
    }

    // Crit chance and multiplier (average values)
    @Override
    protected int critChance() { return 15; }
    @Override
    protected double critMultiplier() { return 1.8; }

    // Change to the next form (HUMAN -> DRAGON -> GOLEM -> HUMAN)
    public void transform() {
        switch (currentForm) {
            case HUMAN:
                currentForm = Form.DRAGON;
                System.out.println("The Shapeshifter transformed into a DRAGON!");
                break;
            case DRAGON:
                currentForm = Form.GOLEM;
                System.out.println("The Shapeshifter transformed into a GOLEM!");
                break;
            case GOLEM:
                currentForm = Form.HUMAN;
                System.out.println("The Shapeshifter returned to HUMAN form!");
                break;
        }
    }

    // Return the current form, allows UI or logic checks
    public Form getCurrentForm() { return currentForm; }

    // Skill 1: Transform (available in any form, switches form, no damage)
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) {
            System.out.println("Transform cooling down (" + cd1 + " turns left)");
            return 0;
        }
        cd1 = 2;
        gainEnergy(15);
        transform();
        return 0; // Just transformation, no damage
    }

    // Skill 2: Wing Slash - DRAGON only, deals damage & may burn
    @Override
    public int skill2(GameCharacter target) {
        if (currentForm != Form.DRAGON) {
            System.out.println("Wing Slash only available in DRAGON form!");
            return 0;
        }
        if (cd2 > 0) {
            System.out.println("Wing Slash cooling down (" + cd2 + " turns left)");
            return 0;
        }
        cd2 = 4;
        gainEnergy(20);
        System.out.println("Dragon uses WING SLASH!");
        int dmg = baseAttack + 10;
        boolean hit = target.receiveDamage(applyCrit(dmg));
        if (hit && rand.nextInt(100) < 40) { // 40% burn chance
            target.applyBurn(2, 6);
        }
        return hit ? dmg : 0;
    }

    // Skill 3: Quaking Stomp - GOLEM only, deals damage & may freeze/stun
    @Override
    public int skill3(GameCharacter target) {
        if (currentForm != Form.GOLEM) {
            System.out.println("Quaking Stomp only available in GOLEM form!");
            return 0;
        }
        if (cd3 > 0) {
            System.out.println("Quaking Stomp cooling down (" + cd3 + " turns left)");
            return 0;
        }
        cd3 = 3;
        gainEnergy(20);
        System.out.println("Golem uses QUAKING STOMP!");
        int dmg = baseAttack + 10;
        boolean hit = target.receiveDamage(applyCrit(dmg));
        if (hit && rand.nextInt(100) < 30) { // 30% freeze (stun) chance
            target.applyFreeze(1);
        }
        return hit ? dmg : 0;
    }

    // Ultimate: Primal Fusion (all forms) - multi-hit, debuffs enemy, buffs self
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) {
            System.out.println("Ultimate not ready or on cooldown.");
            return 0;
        }
        setUltimateOnCooldown(5);
        spendAllEnergy();
        System.out.println("SHAPESHIFTER unleashes PRIMAL FUSION!");
        int hits = 3;
        int totalDmg = 0;
        for (int i = 0; i < hits; i++) {
            int dmg = baseAttack + 10 + rand.nextInt(11); // 10–20 bonus per hit
            boolean hit = target.receiveDamage(applyCrit(dmg));
            if (hit) {
                totalDmg += dmg;
                target.applyBurn(3, 8);         // Burn debuff
                target.applyAttackReduction(3, 0.25); // Attack down debuff
                this.applyTempDodgeBonus(3);    // Boost self-dodge
            }
        }
        return totalDmg;
    }

    // Each turn, reduce skill cooldowns
    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
    }

    @Override
    public String getSkill1Name() { return "Transform"; }
    @Override
    public String getSkill2Name() { return "Wing Slash"; }
    @Override
    public String getSkill3Name() { return "Quaking Stomp"; }
    @Override
    public String getUltimateName() { return "Primal Fusion"; }
    @Override
    public int getSkill1Cooldown() { return cd1; }
    @Override
    public int getSkill2Cooldown() { return cd2; }
    @Override
    public int getSkill3Cooldown() { return cd3; }
}
