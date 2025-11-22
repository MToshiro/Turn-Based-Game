package main.enemies;

import main.GameCharacter;
import main.characters.Player;

public class ShadowOverlord extends Enemy {
    public ShadowOverlord() { super("Shadow Overlord", 170, 20, 10, 15); } 

    @Override
    public int skill1(GameCharacter target) {  // Dark Slash
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 15 + rand.nextInt(11);  // Increased from 10-11 to 15-26
        System.out.println("👿 Shadow Overlord used DARK SLASH!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) {  // Shadow Bolt
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(15);
        int dmg = 20 + rand.nextInt(11);  // Increased from 15-11 to 20-31
        System.out.println("🌑 Shadow Overlord cast SHADOW BOLT!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) {  // Fear Howl (attack reduction)
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(12);
        System.out.println("😱 Shadow Overlord used FEAR HOWL! (reduces player's attack)");
        target.applyAttackReduction(2, 0.20);
        int dmg = 18;  // Increased from 12 to 18
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int ultimate(GameCharacter target) {  // Abyssal Cataclysm
        if (!canUseUltimate()) return 0;
        ultCd = 6;
        spendAllEnergy();
        int dmg = 45 + rand.nextInt(11);  // Increased from 35-11 to 45-56
        System.out.println("💀 Shadow Overlord unleashed ABYSSAL CATACLYSM!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int decideAction(Player player) {
        // Smarter AI: Prioritize healing at low HP, ultimate when ready, then skills/basic
        if (health <= maxHealth * 0.3 && healsLeft > 0 && rand.nextInt(100) < 70) {
            heal();
            return 0;
        }
        if (canUseUltimate()) return ultimate(player);
        int pick = rand.nextInt(100);
        if (pick < 35) {
            return performBasicAttack(player);
        } else if (pick < 65) {
            int d = skill2(player);
            return d > 0 ? d : performBasicAttack(player);
        } else {
            int d = skill1(player);
            return d > 0 ? d : performBasicAttack(player);
        }
    }
}