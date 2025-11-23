package main.enemies;

import main.GameCharacter;
import main.characters.Player;

// Base class for all enemy types (bosses, monsters, etc.)
public abstract class Enemy extends GameCharacter {
    // Skill 1, 2, 3, and ultimate cooldown counters (in turns)
    protected int cd1, cd2, cd3, ultCd;
    // Enemy-specific critical hit chance
    protected int enemyCritChance;

    // Initialize an enemy with all its stats and crit chance
    public Enemy(String name, int maxHP, int baseAttack, int dodgeChance, int critChance) {
        super(name, maxHP, baseAttack, dodgeChance);
        this.cd1 = this.cd2 = this.cd3 = this.ultCd = 0;
        this.enemyCritChance = critChance;
    }

    // Override crit chance for each enemy
    @Override
    protected int critChance() { return enemyCritChance; }

    // Enemy AI: choose and execute a move, returns damage dealt to player (or 0 for buffs/heals)
    public abstract int decideAction(Player player);

    // Each turn, decrease skill and ultimate cooldowns if active
    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
        if (ultCd > 0) ultCd--;
    }

    // Check if enemy can use its ultimate ability (needs 100 energy and not on cooldown)
    protected boolean canUseUltimate() {
        return energy >= 100 && ultCd == 0;
    }

    // Helper for basic attack (adds energy, performs attack, applies crit if occurs)
    protected int performBasicAttack(GameCharacter target) {
        gainEnergy(10);
        int dmg = baseAttack + rand.nextInt(6); // Adds variability (0-5 bonus)
        System.out.println(name + " attacked!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }
}
