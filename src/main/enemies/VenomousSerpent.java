package main.enemies;

import main.GameCharacter;
import main.characters.Player;

public class VenomousSerpent extends Enemy {
    public VenomousSerpent() { super("Venomous Serpent", 140, 20, 20, 25); }

    @Override
    public int skill1(GameCharacter target) {  // Venom Fang
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 18 + rand.nextInt(8);
        System.out.println("🐍 Venomous Serpent used VENOM FANG!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) {  // Poison Spit (burn debuff)
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(15);
        int dmg = 15 + rand.nextInt(11);
        System.out.println("🧪 Venomous Serpent used POISON SPIT! (applies burn)");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) target.applyBurn(3, 5);
        return applied ? dmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) {  // Toxic Cloud (energy reduction)
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(12);
        System.out.println("☁️ Venomous Serpent used TOXIC CLOUD! (reduces player's energy gain)");
        target.applyReduceEnergyGain(2);  // Debuff energy gain
        int dmg = 12;
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int ultimate(GameCharacter target) {  // Venom Storm
        if (!canUseUltimate()) return 0;
        ultCd = 6;
        spendAllEnergy();
        int dmg = 35 + rand.nextInt(11);
        System.out.println("🐍 Venomous Serpent unleashed VENOM STORM!");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) target.applyBurn(4, 8);
        return applied ? dmg : 0;
    }

    @Override
    public int decideAction(Player player) {
        if (health <= maxHealth * 0.3 && healsLeft > 0 && rand.nextInt(100) < 65) {
            heal();
            return 0;
        }
        if (canUseUltimate()) return ultimate(player);
        int pick = rand.nextInt(100);
        if (pick < 35) return performBasicAttack(player);
        else if (pick < 65) {
            int d = skill2(player);
            return d > 0 ? d : performBasicAttack(player);
        } else {
            int d = skill1(player);
            return d > 0 ? d : performBasicAttack(player);
        }
    }
}