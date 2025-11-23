package main.enemies;

import main.GameCharacter;
import main.characters.Player;

// FrostWraith class - fast, evasive enemy with freeze abilities
public class FrostWraith extends Enemy {
    // Set up FrostWraith with high HP, high attack, high dodge, good crit
    public FrostWraith() {
        super("Frost Wraith", 160, 24, 25, 30); // HP: 160, Attack: 24, Dodge: 25, Crit: 30
    }

    // Skill 1: Ice Slash – moderate attack, low cooldown
    @Override
    public int skill1(GameCharacter target) {
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 22 + rand.nextInt(8); // 22-29 damage
        System.out.println("Frost Wraith used ICE SLASH!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    // Skill 2: Frozen Arrow – strong attack, chance to freeze
    @Override
    public int skill2(GameCharacter target) {
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(18);
        int dmg = 25 + rand.nextInt(10); // 25-34 damage
        System.out.println("Frost Wraith used FROZEN ARROW!");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied && rand.nextInt(100) < 40) { // 40% chance to freeze
            target.applyFreeze(1);
        }
        return applied ? dmg : 0;
    }

    // Skill 3: Snow Cloak – buffs self dodge (no damage)
    @Override
    public int skill3(GameCharacter target) {
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(12);
        System.out.println("Frost Wraith used SNOW CLOAK! Gains dodge.");
        this.applyTempDodgeBonus(3); // High dodge for next 3 turns
        return 0;
    }

    // Ultimate: Absolute Zero – powerful attack, guaranteed freeze
    @Override
    public int ultimate(GameCharacter target) {
        if (!canUseUltimate()) return 0;
        ultCd = 6;
        spendAllEnergy();
        int dmg = 50 + rand.nextInt(11); // 50-60 damage
        System.out.println("Frost Wraith used ABSOLUTE ZERO!");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) target.applyFreeze(1); // Guaranteed freeze
        return applied ? dmg : 0;
    }

    // Enemy AI: picks action based on HP, energy, and skill cooldowns
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
