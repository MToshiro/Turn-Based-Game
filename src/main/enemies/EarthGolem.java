package main.enemies;

import main.GameCharacter;
import main.characters.Player;

public class EarthGolem extends Enemy {
    public EarthGolem() { super("Earth Golem", 220, 18, 5, 10); }

    @Override
    public int skill1(GameCharacter target) {  // Boulder Toss
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 25 + rand.nextInt(6);
        System.out.println("🪨 Earth Golem used BOULDER TOSS!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) {  // Seismic Slam (stun chance)
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(15);
        int dmg = 20 + rand.nextInt(11);
        System.out.println("🌍 Earth Golem used SEISMIC SLAM! (chance to stun)");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied && rand.nextInt(100) < 35) target.applyFreeze(1);  // Stun as freeze
        return applied ? dmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) {  // Stone Armor (self-buff)
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(10);
        System.out.println("🛡️ Earth Golem used STONE ARMOR! (temporary dodge boost for itself)");
        this.applyTempDodgeBonus(3);
        return 0;
    }

    @Override
    public int ultimate(GameCharacter target) {  // Earthquake
        if (!canUseUltimate()) return 0;
        ultCd = 7;
        spendAllEnergy();
        int dmg = 40 + rand.nextInt(11);
        System.out.println("🌋 Earth Golem unleashed EARTHQUAKE!");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) target.applyFreeze(1);  // Stun
        return applied ? dmg : 0;
    }

    @Override
    public int decideAction(Player player) {
        if (health <= maxHealth * 0.4 && healsLeft > 0 && rand.nextInt(100) < 50) {
            heal();
            return 0;
        }
        if (canUseUltimate()) return ultimate(player);
        int pick = rand.nextInt(100);
        if (pick < 40) return performBasicAttack(player);
        else if (pick < 70) {
            int d = skill2(player);
            return d > 0 ? d : performBasicAttack(player);
        } else {
            int d = skill3(player);
            return d > 0 ? d : performBasicAttack(player);
        }
    }
}