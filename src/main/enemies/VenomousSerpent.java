package main.enemies;

import main.GameCharacter;
import main.characters.Player;

// VenomousSerpent class - poison-themed enemy with burn and energy-reduction moves
public class VenomousSerpent extends Enemy {
    // Set up VenomousSerpent with decent HP, attack, dodge, and high crit
    public VenomousSerpent() {
        super("Venomous Serpent", 140, 20, 20, 25); // 140 HP, 20 Attack, 20 Dodge, 25 Crit
    }

    // Skill 1: Venom Fang – moderate attack, quick cooldown
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 18 + rand.nextInt(8); // 18-25 damage
        System.out.println("Venomous Serpent used VENOM FANG!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 2: Poison Spit – applies a burning poison (damage over time)
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(15);
        int dmg = 15 + rand.nextInt(11); // 15-25 damage
        System.out.println("Venomous Serpent used POISON SPIT! Applies poison burn.");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) {
            target.applyBurn(3, 5); // Poison burn for 3 turns
        }
        return applied ? dmg : 0;
    }

    // Skill 3: Toxic Cloud – hurts and reduces player's energy gain
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(12);
        System.out.println("Venomous Serpent used TOXIC CLOUD! Slows player's energy gain.");
        target.applyReduceEnergyGain(2); // Reduces enemy's energy gain for 2 turns
        int dmg = 12;
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Ultimate: Venom Storm – big attack and strong poison burn
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) return 0;
        ultCd = 6;
        spendAllEnergy();
        int dmg = 35 + rand.nextInt(11); // 35-45 damage
        System.out.println("Venomous Serpent unleashed VENOM STORM! Major poison burn.");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) {
            target.applyBurn(4, 8); // 4 turns, 8 damage per turn
        }
        return applied ? dmg : 0;
    }

    // Enemy AI: uses heal if low HP, prioritizes ult and skills, otherwise basic attack
    @Override
    public int decideAction(Player player) {
        if (health < maxHealth * 0.3 && healsLeft > 0 && rand.nextInt(100) < 65) {
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
