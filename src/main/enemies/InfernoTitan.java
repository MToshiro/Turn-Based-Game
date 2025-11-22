package main.enemies;

import main.GameCharacter;
import main.characters.Player;

public class InfernoTitan extends Enemy {
    public InfernoTitan() { super("Inferno Titan", 210, 22, 5, 8); }  // Increased HP to 210, attack to 22

    @Override
    public int skill1(GameCharacter target) {  // Flame Punch
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 30 + rand.nextInt(6);  // Increased from 25+6 to 30+6
        System.out.println("🔥 Inferno Titan used FLAME PUNCH!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) {  // Fire Burst
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(15);
        int dmg = 25 + rand.nextInt(11);  // Increased from 20+11 to 25+11
        System.out.println("💥 Inferno Titan used FIRE BURST! (applies burn)");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) target.applyBurn(3, 6);
        return applied ? dmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) {  // Burn Aura (DoT)
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(10);
        System.out.println("🔥 Inferno Titan used BURN AURA! (applies burn)");
        target.applyBurn(3, 8);
        int dmg = 18;  // Increased from 12 to 18
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int ultimate(GameCharacter target) {  // Hellfire Meteor
        if (!canUseUltimate()) return 0;
        ultCd = 7;
        spendAllEnergy();
        int dmg = 50 + rand.nextInt(11);  // Increased from 40+11 to 50+11
        System.out.println("🔥 Inferno Titan used HELLFIRE METEOR!");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) target.applyBurn(4, 10);
        return applied ? dmg : 0;
    }

    @Override
    public int decideAction(Player player) {
        // Smarter AI: Prioritize healing at low HP, ultimate when ready, then skills/basic
        if (health <= maxHealth * 0.35 && healsLeft > 0 && rand.nextInt(100) < 60) {
            heal();
            return 0;
        }
        if (canUseUltimate()) return ultimate(player);
        int pick = rand.nextInt(100);
        if (pick < 40) {
            return performBasicAttack(player);
        } else if (pick < 70) {
            int d = skill2(player);
            return d > 0 ? d : performBasicAttack(player);
        } else {
            int d = skill1(player);
            return d > 0 ? d : performBasicAttack(player);
        }
    }
}
