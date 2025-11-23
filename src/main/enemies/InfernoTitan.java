package main.enemies;

import main.GameCharacter;
import main.characters.Player;

// InfernoTitan class - fire-themed enemy with high HP and strong burning effect
public class InfernoTitan extends Enemy {
    // Set up InfernoTitan with high HP, moderate attack, low dodge and crit
    public InfernoTitan() {
        super("Inferno Titan", 210, 22, 5, 8); // 210 HP, 22 Attack, 5 Dodge, 8 Crit
    }

    // Skill 1: Flame Punch – heavy physical attack, quick cooldown
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 30 + rand.nextInt(6); // 30-35 damage
        System.out.println("Inferno Titan used FLAME PUNCH!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 2: Fire Burst – big attack, applies burn (damage over time)
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(15);
        int dmg = 25 + rand.nextInt(11); // 25-35 damage
        System.out.println("Inferno Titan used FIRE BURST! Applies burn.");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) {
            target.applyBurn(3, 6); // Burns for 3 turns, 6 damage per turn
        }
        return applied ? dmg : 0;
    }

    // Skill 3: Burn Aura – damages and applies burn (AoE/consistent)
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(10);
        System.out.println("Inferno Titan used BURN AURA! Applies burn.");
        target.applyBurn(3, 8); // Burns for 3 turns, 8 damage per turn
        int dmg = 18;
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Ultimate: Hellfire Meteor – massive damage, applies strong burn
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) return 0;
        ultCd = 7;
        spendAllEnergy();
        int dmg = 50 + rand.nextInt(11); // 50-60 damage
        System.out.println("Inferno Titan used HELLFIRE METEOR! Massive burn.");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) {
            target.applyBurn(4, 10); // Burns for 4 turns, 10 damage per turn
        }
        return applied ? dmg : 0;
    }

    // Enemy AI: picks action based on HP, chance to heal, prioritizes ultimate
    @Override
    public int decideAction(Player player) {
        if (health < maxHealth * 0.35 && healsLeft > 0 && rand.nextInt(100) < 60) {
            heal();
            return 0;
        }
        if (canUseUltimate()) return ultimate(player);
        int pick = rand.nextInt(100);
        if (pick < 40) return performBasicAttack(player);
        else if (pick < 70) {
            int d = skill2(player); return d > 0 ? d : performBasicAttack(player);
        } else {
            int d = skill1(player); return d > 0 ? d : performBasicAttack(player);
        }
    }
}
