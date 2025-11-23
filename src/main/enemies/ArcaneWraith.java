package main.enemies;

import main.GameCharacter;
import main.characters.Player;

// ArcaneWraith class - enemy that uses arcane spells, high crits and dodges
public class ArcaneWraith extends Enemy {
    // Set up ArcaneWraith with high crit chance and dodge
    public ArcaneWraith() {
        super("Arcane Wraith", 130, 22, 18, 35); // 130 HP, 22 Attack, 18 Dodge, 35 Crit
    }

    // Skill 1: Arcane Bolt - moderate damage, low cooldown
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 20 + rand.nextInt(8); // 20-27 damage
        System.out.println("Arcane Wraith used ARCANE BOLT!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 2: Mana Burst - multi-hit, random number of hits (2-3 times)
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(18);
        int hits = 2 + rand.nextInt(2); // 2 or 3 hits
        int total = 0;
        System.out.println("Arcane Wraith used MANA BURST! hits: " + hits);
        for (int i = 0; i < hits; i++) {
            int dmg = 12 + rand.nextInt(10);
            boolean applied = target.receiveDamage(applyCrit(dmg));
            if (applied) total += dmg;
        }
        return total;
    }

    // Skill 3: Mystic Veil - raises own dodge significantly (no damage)
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(12);
        System.out.println("Arcane Wraith used MYSTIC VEIL! Gains dodge.");
        this.applyTempDodgeBonus(3); // High dodge for next 3 turns
        return 0;
    }

    // Ultimate: Arcane Nova - big damage, debuffs enemy's attack
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) return 0;
        ultCd = 6;
        spendAllEnergy();
        int dmg = 45 + rand.nextInt(11); // 45-55 damage
        System.out.println("Arcane Wraith unleashed ARCANE NOVA!");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) target.applyAttackReduction(2, 0.15); // Reduce attack
        return applied ? dmg : 0;
    }

    // Enemy AI: chooses skill/basic/ult (returns damage dealt, 0 for buff/heal)
    @Override
    public int decideAction(Player player) {
        if (health < maxHealth * 0.25 && healsLeft > 0 && rand.nextInt(100) < 75) {
            heal();
            return 0;
        }
        if (canUseUltimate()) return ultimate(player);

        int pick = rand.nextInt(100);
        if (pick < 35) return performBasicAttack(player);
        else if (pick < 65) {
            int d = skill2(player); return d > 0 ? d : performBasicAttack(player);
        } else {
            int d = skill3(player); return d > 0 ? d : performBasicAttack(player);
        }
    }
}
