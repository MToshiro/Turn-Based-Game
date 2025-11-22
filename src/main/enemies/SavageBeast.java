package main.enemies;

import main.GameCharacter;
import main.characters.Player;

public class SavageBeast extends Enemy {
    public SavageBeast() { super("Savage Beast", 180, 23, 10, 15); }

    @Override
    public int skill1(GameCharacter target) {  // Savage Claw
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 25 + rand.nextInt(6);
        System.out.println("🦁 Savage Beast used SAVAGE CLAW!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) {  // Frenzy Bite (self-buff)
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(15);
        int dmg = 20 + rand.nextInt(11);
        System.out.println("😈 Savage Beast used FRENZY BITE! (boosts own attack temporarily)");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) {
            // Temporary self-buff (simulate attack boost by increasing energy for next turn)
            gainEnergy(10);  // Extra energy for aggression
        }
        return applied ? dmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) {  // Roar Intimidate (debuff attack)
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(12);
        System.out.println("🗣️ Savage Beast used ROAR INTIMIDATE! (reduces player's attack)");
        target.applyAttackReduction(2, 0.20);
        int dmg = 15;
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int ultimate(GameCharacter target) {  // Beast Rampage
        if (!canUseUltimate()) return 0;
        ultCd = 7;
        spendAllEnergy();
        int dmg = 50 + rand.nextInt(11);
        System.out.println("🐺 Savage Beast unleashed BEAST RAMPAGE!");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) target.applyBurn(2, 6);  // Minor burn
        return applied ? dmg : 0;
    }

    @Override
    public int decideAction(Player player) {
        if (health <= maxHealth * 0.35 && healsLeft > 0 && rand.nextInt(100) < 60) {
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
            int d = skill1(player);
            return d > 0 ? d : performBasicAttack(player);
        }
    }
}
