package main.characters;

import main.GameCharacter;

public abstract class Player extends GameCharacter {
    protected int ultimateCooldown;

    public Player(String name, int maxHealth, int baseAttack, int dodgeChance) {
        super(name, maxHealth, baseAttack, dodgeChance);
        this.ultimateCooldown = 0;
    }

    @Override
    protected int critChance() { return 20; }

    public boolean canUseUltimate() {
        return energy >= 100 && ultimateCooldown == 0;
    }

    public void setUltimateOnCooldown(int turns) { ultimateCooldown = turns; }

    @Override
    public void processTurnStart() {
        super.processTurnStart();
        if (ultimateCooldown > 0) ultimateCooldown--;
    }

    // New abstract methods for skill names and cooldowns
    public abstract String getSkill1Name();
    public abstract String getSkill2Name();
    public abstract String getSkill3Name();
    public abstract String getUltimateName();
    public abstract int getSkill1Cooldown();
    public abstract int getSkill2Cooldown();
    public abstract int getSkill3Cooldown();
    public int getUltimateCooldown() {
        return ultimateCooldown;
    }
}