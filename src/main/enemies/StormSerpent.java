package main.enemies;

import main.GameCharacter;
import main.characters.Player;

public class StormSerpent extends Enemy {
    public StormSerpent() { super("Storm Serpent", 150, 21, 15, 20); }  // Increased HP to 150, attack to 21

    @Override
    public int skill1(GameCharacter target) {  // Thunder Bite
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 22 + rand.nextInt(8);  // Increased from 18+8 to 22+8
        System.out.println("⚡ Storm Serpent used THUNDER BITE!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) {  // Lightning Chain (2-3 hits)
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(18);
        int hits = 2 + rand.nextInt(2); // 2 or 3 hits
        int total = 0;
        System.out.println("⚡ Storm Serpent used LIGHTNING CHAIN! ("+hits+" hits)");
        for (int i=0;i<hits;i++) {
            int dmg = 15 + rand.nextInt(12);  // Increased from 10+12 to 15+12
            boolean applied = target.receiveDamage(applyCrit(dmg));
            if (applied) total += dmg;
        }
        return total;
    }

    @Override
    public int skill3(GameCharacter target) {  // Disrupt Pulse (clears debuffs)
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(12);
        System.out.println("🔌 Storm Serpent used DISRUPT PULSE! (clears debuffs)");
        target.clearDebuffs(); // Safe debuff clearing
        int dmg = 18;  // Increased from 12 to 18
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int ultimate(GameCharacter target) {  // Electric Surge
        if (!canUseUltimate()) return 0;
        ultCd = 6;
        spendAllEnergy();
        System.out.println("⚡ Storm Serpent used TEMPEST WRATH! (multi-hit storm)");
        int total = 0;
        int hits = 3 + rand.nextInt(3); // 3-5 hits
        for (int i=0;i<hits;i++) {
            int dmg = 12 + rand.nextInt(12);  // Increased from 8+12 to 12+12
            boolean applied = target.receiveDamage(applyCrit(dmg));
            if (applied) total += dmg;
        }
        return total;
    }

    @Override
    public int decideAction(Player player) {
        // Smarter AI: Prioritize healing at low HP, ultimate when ready, then skills/basic
        if (health <= maxHealth * 0.3 && healsLeft > 0 && rand.nextInt(100) < 60) {
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