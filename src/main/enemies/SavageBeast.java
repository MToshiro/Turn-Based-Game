package main.enemies;

import main.GameCharacter;
import main.characters.Player;

// SavageBeast class - aggressive enemy that self-buffs and can debuff the player
public class SavageBeast extends Enemy {
    // Set up SavageBeast with high HP, strong attack, moderate dodge and crit
    public SavageBeast() {
        super("Savage Beast", 180, 23, 10, 15); // HP: 180, Attack: 23, Dodge: 10, Crit: 15
    }

    // Skill 1: Savage Claw – moderate attack, low cooldown
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 25 + rand.nextInt(6); // 25-30 damage
        System.out.println("Savage Beast used SAVAGE CLAW!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 2: Frenzy Bite – attack and self-buff (gains extra energy for next turn)
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(15);
        int dmg = 20 + rand.nextInt(11); // 20-30 damage
        System.out.println("Savage Beast used FRENZY BITE! Boosts its aggression.");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) {
            gainEnergy(10); // Extra energy as aggressive bonus
        }
        return applied ? dmg : 0;
    }

    // Skill 3: Roar Intimidate – weak attack and reduces player's attack (debuff)
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(12);
        System.out.println("Savage Beast used ROAR INTIMIDATE! Reduces player attack.");
        target.applyAttackReduction(2, 0.20); // Reduces player attack by 20% for 2 turns
        int dmg = 15;
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Ultimate: Beast Rampage – huge attack, applies minor burn
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) return 0;
        ultCd = 7;
        spendAllEnergy();
        int dmg = 50 + rand.nextInt(11); // 50-60 damage
        System.out.println("Savage Beast unleashed BEAST RAMPAGE!");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) {
            target.applyBurn(2, 6); // Minor burn, 2 turns, 6 damage per turn
        }
        return applied ? dmg : 0;
    }

    // Enemy AI: uses heal if low HP, prioritizes ultimate, otherwise mixes attacks and buffs
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
