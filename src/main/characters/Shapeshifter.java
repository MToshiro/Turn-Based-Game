package main.characters;

import main.GameCharacter;

public class Shapeshifter extends Player {
    private int cd1, cd2, cd3;

    public enum Form { HUMAN, DRAGON, GOLEM }
    public Form currentForm = Form.HUMAN;

    public Shapeshifter() {
        super("Shapeshifter", 140, 20, 10);  // Balanced stats: moderate HP/attack, low dodge
        cd1 = cd2 = cd3 = 0;
    }

    @Override
    protected int critChance() { return 15; }  // As per original

    @Override
    protected double critMultiplier() { return 1.8; }  // As per original

    // Transform method (cycles through forms)
    public void transform() {
        switch (currentForm) {
            case HUMAN:
                currentForm = Form.DRAGON;
                System.out.println("🔥 The Shapeshifter transformed into a DRAGON!");
                break;
            case DRAGON:
                currentForm = Form.GOLEM;
                System.out.println("🪨 The Shapeshifter transformed into a GOLEM!");
                break;
            case GOLEM:
                currentForm = Form.HUMAN;
                System.out.println("👤 The Shapeshifter returned to HUMAN form!");
                break;
        }
    }

    // Getter for currentForm (added for MainApp to check form)
    public Form getCurrentForm() {
        return currentForm;
    }

    @Override
    public int skill1(GameCharacter target) {  // Transform between forms (available in all forms)
        if (cd1 > 0) { System.out.println("❌ Transform cooling down (" + cd1 + " turns left)"); return 0; }
        cd1 = 2;
        gainEnergy(15);
        transform();
        return 0;  // No damage, just transformation
    }

    @Override
    public int skill2(GameCharacter target) {  // Wing Slash (only available in DRAGON form)
        if (currentForm != Form.DRAGON) {
            System.out.println("❌ Wing Slash only available in DRAGON form!");
            return 0;
        }
        if (cd2 > 0) { System.out.println("❌ Wing Slash cooling down (" + cd2 + " turns left)"); return 0; }
        cd2 = 4;
        gainEnergy(20);
        System.out.println("🐉 Dragon uses WING SLASH!");
        int dmg = baseAttack + 10; 
        boolean hit = target.receiveDamage(applyCrit(dmg));
        if (hit && rand.nextInt(100) < 40) target.applyBurn(2, 6);  // 40% chance to burn
        return hit ? dmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) {  // Quaking Stomp (only available in GOLEM form)
        if (currentForm != Form.GOLEM) {
            System.out.println("❌ Quaking Stomp only available in GOLEM form!");
            return 0;
        }
        if (cd3 > 0) { System.out.println("❌ Quaking Stomp cooling down (" + cd3 + " turns left)"); return 0; }
        cd3 = 3;
        gainEnergy(20);
        System.out.println("🗿 Golem uses QUAKING STOMP!");
        int dmg = baseAttack + 10; 
        boolean hit = target.receiveDamage(applyCrit(dmg));
        if (hit && rand.nextInt(100) < 30) target.applyFreeze(1);  // 30% chance to stun (freeze)
        return hit ? dmg : 0;
    }

    @Override
    public int ultimate(GameCharacter target) {  // Primal Fusion (available in all forms)
        if (!canUseUltimate()) { System.out.println("❌ Ultimate not ready or on cooldown."); return 0; }
        setUltimateOnCooldown(5);
        spendAllEnergy();
        System.out.println("🌟 SHAPESHIFTER unleashes PRIMAL FUSION! (fuses all forms for ultimate power)");
        int hits = 3;  // Multi-hit
        int totalDmg = 0;
        for (int i = 0; i < hits; i++) {
            int dmg = baseAttack + 10 + rand.nextInt(11);  // 30-40 per hit
            boolean hit = target.receiveDamage(applyCrit(dmg));
            if (hit) totalDmg += dmg;
        }
        target.applyBurn(3, 8);  // Burn debuff
        target.applyAttackReduction(3, 0.25);  // Attack reduction debuff
        this.applyTempDodgeBonus(3);  // Self-buff dodge
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