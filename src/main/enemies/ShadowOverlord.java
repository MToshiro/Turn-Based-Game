package main.enemies;

import main.GameCharacter;
import main.characters.Player;

// ShadowOverlord class - boss with debuffs and heavy multi-hit attacks
public class ShadowOverlord extends Enemy {
    // Set up ShadowOverlord with solid HP, attack, dodge, crit
    public ShadowOverlord() {
        super("Shadow Overlord", 170, 20, 10, 15); // 170 HP, 20 Attack, 10 Dodge, 15 Crit
    }

    // Skill 1: Dark Slash – moderate attack, quick cooldown
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 15 + rand.nextInt(11); // 15-25 damage
        System.out.println("Shadow Overlord used DARK SLASH!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 2: Shadow Bolt – strong attack, mid cooldown
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(15);
        int dmg = 20 + rand.nextInt(11); // 20-30 damage
        System.out.println("Shadow Overlord cast SHADOW BOLT!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 3: Fear Howl – debuffs player's attack and deals damage
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(12);
        System.out.println("Shadow Overlord used FEAR HOWL! Reduces player's attack!");
        target.applyAttackReduction(2, 0.20); // Reduce player's attack for 2 turns
        int dmg = 18;
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Ultimate: Abyssal Cataclysm – very strong attack
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) return 0;
        ultCd = 6;
        spendAllEnergy();
        int dmg = 45 + rand.nextInt(11); // 45-55 damage
        System.out.println("Shadow Overlord unleashed ABYSSAL CATACLYSM!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Enemy AI: prioritizes heal at low HP, ult when charged, otherwise attacks/buffs
    @Override
    public int decideAction(Player player) {
        if (health < maxHealth * 0.3 && healsLeft > 0 && rand.nextInt(100) < 70) {
            heal();
            return 0;
        }
        if (canUseUltimate()) return ultimate(player);
        int pick = rand.nextInt(100);
        if (pick < 35) return performBasicAttack(player);
        else if (pick < 65) {
            int d = skill2(player); return d > 0 ? d : performBasicAttack(player);
        } else {
            int d = skill1(player); return d > 0 ? d : performBasicAttack(player);
        }
    }
}
