package main.enemies;

import main.GameCharacter;
import main.characters.Player;

// StormSerpent class - lightning-based enemy with debuff removal and chain attacks
public class StormSerpent extends Enemy {
    // Set up StormSerpent with balanced stats, moderate crit and dodge
    public StormSerpent() {
        super("Storm Serpent", 150, 21, 15, 20); // 150 HP, 21 Attack, 15 Dodge, 20 Crit
    }

    // Skill 1: Thunder Bite – strong hit, quick cooldown
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 22 + rand.nextInt(8); // 22-29 damage
        System.out.println("Storm Serpent used THUNDER BITE!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 2: Lightning Chain – multi-hit (2 or 3 random), moderate cooldown
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(18);
        int hits = 2 + rand.nextInt(2); // 2 or 3 hits
        int total = 0;
        System.out.println("Storm Serpent used LIGHTNING CHAIN! hits: " + hits);
        for (int i = 0; i < hits; i++) {
            int dmg = 15 + rand.nextInt(12); // 15-26 damage per hit
            boolean applied = target.receiveDamage(applyCrit(dmg));
            if (applied) total += dmg;
        }
        return total;
    }

    // Skill 3: Disrupt Pulse – deals damage and clears all debuffs from self
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(12);
        System.out.println("Storm Serpent used DISRUPT PULSE! Clears debuffs.");
        clearDebuffs();
        int dmg = 18; // Fixed damage on debuff removal
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Ultimate: Tempest Wrath – multi-hit, high total damage (3-5 random hits)
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) return 0;
        ultCd = 6;
        spendAllEnergy();
        System.out.println("Storm Serpent used TEMPEST WRATH! Multi-hit attack.");
        int hits = 3 + rand.nextInt(3); // 3, 4, or 5 hits
        int total = 0;
        for (int i = 0; i < hits; i++) {
            int dmg = 12 + rand.nextInt(12); // 12-23 per hit
            boolean applied = target.receiveDamage(applyCrit(dmg));
            if (applied) total += dmg;
        }
        return total;
    }

    // Enemy AI: picks action based on HP/energy, uses heal/ult when appropriate
    @Override
    public int decideAction(Player player) {
        if (health < maxHealth * 0.3 && healsLeft > 0 && rand.nextInt(100) < 60) {
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
