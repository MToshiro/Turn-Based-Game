package main.enemies;

import main.GameCharacter;
import main.characters.Player;

public class ArcaneWraith extends Enemy {
    public ArcaneWraith() { super("Arcane Wraith", 130, 22, 18, 35); }

    @Override
    public int skill1(GameCharacter target) {  // Arcane Bolt
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 20 + rand.nextInt(8);
        System.out.println("✨ Arcane Wraith used ARCANE BOLT!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) {  // Mana Burst (multi-hit)
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(18);
        int hits = 2 + rand.nextInt(2);  // 2-3 hits
        int total = 0;
        System.out.println("💥 Arcane Wraith used MANA BURST! (" + hits + " hits)");
        for (int i = 0; i < hits; i++) {
            int dmg = 12 + rand.nextInt(10);
            boolean applied = target.receiveDamage(applyCrit(dmg));
            if (applied) total += dmg;
        }
        return total;
    }

    @Override
    public int skill3(GameCharacter target) {  // Mystic Veil (self-dodge buff)
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(12);
        System.out.println("🌀 Arcane Wraith used MYSTIC VEIL! (temporary dodge boost for itself)");
        this.applyTempDodgeBonus(3);
        return 0;
    }

    @Override
    public int ultimate(GameCharacter target) {  // Arcane Nova
        if (!canUseUltimate()) return 0;
        ultCd = 6;
        spendAllEnergy();
        int dmg = 45 + rand.nextInt(11);
        System.out.println("🌟 Arcane Wraith unleashed ARCANE NOVA!");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) target.applyAttackReduction(2, 0.15);  // Debuff attack
        return applied ? dmg : 0;
    }

    @Override
    public int decideAction(Player player) {
        if (health <= maxHealth * 0.25 && healsLeft > 0 && rand.nextInt(100) < 75) {
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
            int d = skill3(player);
            return d > 0 ? d : performBasicAttack(player);
        }
    }
}