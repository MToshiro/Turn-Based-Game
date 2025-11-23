package main.enemies;

import main.GameCharacter;
import main.characters.Player;

// EarthGolem class - tanky enemy with high health and stuns
public class EarthGolem extends Enemy {
    // Set up EarthGolem with high HP, lower attack, low dodge, and crit
    public EarthGolem() {
        super("Earth Golem", 220, 18, 5, 10); // 220 HP, 18 Attack, 5 Dodge, 10 Crit
    }

    // Skill 1: Boulder Toss – big attack, low cooldown
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 25 + rand.nextInt(6); // 25-30 damage
        System.out.println("Earth Golem used BOULDER TOSS!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 2: Seismic Slam – heavy attack, chance to stun (stun = freeze)
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(15);
        int dmg = 20 + rand.nextInt(11); // 20-30 damage
        System.out.println("Earth Golem used SEISMIC SLAM!");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied && rand.nextInt(100) < 35) { // 35% stun chance
            target.applyFreeze(1);
        }
        return applied ? dmg : 0;
    }

    // Skill 3: Stone Armor – buffs self dodge (no damage)
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(10);
        System.out.println("Earth Golem used STONE ARMOR! Gains dodge.");
        this.applyTempDodgeBonus(3); // High dodge for next 3 turns
        return 0;
    }

    // Ultimate: Earthquake – big attack and stuns the player
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) return 0;
        ultCd = 7;
        spendAllEnergy();
        int dmg = 40 + rand.nextInt(11); // 40-50 damage
        System.out.println("Earth Golem unleashed EARTHQUAKE!");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) target.applyFreeze(1); // Guaranteed stun (freeze)
        return applied ? dmg : 0;
    }

    // Enemy AI: decides which move to use, prioritizes healing at low HP, then skills and attacks
    @Override
    public int decideAction(Player player) {
        if (health < maxHealth * 0.4 && healsLeft > 0 && rand.nextInt(100) < 50) {
            heal();
            return 0;
        }
        if (canUseUltimate()) return ultimate(player);
        int pick = rand.nextInt(100);
        if (pick < 40) return performBasicAttack(player);
        else if (pick < 70) {
            int d = skill2(player); return d > 0 ? d : performBasicAttack(player);
        } else {
            int d = skill3(player); return d > 0 ? d : performBasicAttack(player);
        }
    }
}
