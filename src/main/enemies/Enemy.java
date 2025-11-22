package main.enemies;

import main.GameCharacter;
import main.characters.Player;

public abstract class Enemy extends GameCharacter {
    protected int cd1, cd2, cd3, ultCd;
    protected int enemyCritChance;

    public Enemy(String name, int maxHP, int baseAttack, int dodgeChance, int critChance) {
        super(name, maxHP, baseAttack, dodgeChance);
        this.cd1 = this.cd2 = this.cd3 = this.ultCd = 0;
        this.enemyCritChance = critChance;
    }

    @Override
    protected int critChance() { return enemyCritChance; }

    // Enemy AI decides action: returns damage dealt to player (0 if healed or effect)
    public abstract int decideAction(Player player);

    @Override
    public void reduceCooldowns() {
        if (cd1 > 0) cd1--;
        if (cd2 > 0) cd2--;
        if (cd3 > 0) cd3--;
        if (ultCd > 0) ultCd--;
    }

    protected boolean canUseUltimate() { return energy >= 100 && ultCd == 0; }

    // Helper for basic attack (with energy gain)
    protected int performBasicAttack(GameCharacter target) {
        gainEnergy(10);
        int dmg = baseAttack + rand.nextInt(6);
        System.out.println("👹 " + name + " attacked!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }
}