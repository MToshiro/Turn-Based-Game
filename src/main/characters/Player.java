package main.characters;

import main.GameCharacter;

// Abstract Player class for all playable heroes
public abstract class Player extends GameCharacter {
    // Tracks ultimate ability's cooldown (in turns)
    protected int ultimateCooldown;

    // Set up a player with usual stats and no ult cooldown
    public Player(String name, int maxHealth, int baseAttack, int dodgeChance) {
        super(name, maxHealth, baseAttack, dodgeChance);
        this.ultimateCooldown = 0;
    }

    // Specialized critical hit chance for players (default 20%)
    @Override
    protected int critChance() { return 20; }

    // Check if player can use ultimate: needs 100 energy & not on cooldown
    public boolean canUseUltimate() {
        return energy >= 100 && ultimateCooldown == 0;
    }

    // Set ultimate on cooldown for 'turns' number of turns
    public void setUltimateOnCooldown(int turns) {
        ultimateCooldown = turns;
    }

    // Each turn, reduce ultimate cooldown
    @Override
    public void processTurnStart() {
        super.processTurnStart();
        if (ultimateCooldown > 0) ultimateCooldown--;
    }

    // Methods every player must provide for skill/ultimate names and cooldowns
    public abstract String getSkill1Name();
    public abstract String getSkill2Name();
    public abstract String getSkill3Name();
    public abstract String getUltimateName();
    public abstract int getSkill1Cooldown();
    public abstract int getSkill2Cooldown();
    public abstract int getSkill3Cooldown();
    public int getUltimateCooldown() { return ultimateCooldown; }
}
