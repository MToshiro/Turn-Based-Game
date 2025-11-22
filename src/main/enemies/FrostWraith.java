package main.enemies;

import main.GameCharacter;
import main.characters.Player;

public class FrostWraith extends Enemy {
    public FrostWraith() { super("Frost Wraith", 160, 24, 25, 30); }  // Increased HP to 160, attack to 24

    @Override
    public int skill1(GameCharacter target) {  // Ice Slash
        if (cd1 > 0) return 0;
        cd1 = 2;
        gainEnergy(12);
        int dmg = 22 + rand.nextInt(8);  // Increased from 18+8 to 22+8
        System.out.println("❄️ Frost Wraith used ICE SLASH!");
        return target.receiveDamage(applyCrit(dmg)) ? dmg : 0;
    }

    @Override
    public int skill2(GameCharacter target) {  // Frozen Arrow (chance to freeze)
        if (cd2 > 0) return 0;
        cd2 = 3;
        gainEnergy(18);
        int dmg = 25 + rand.nextInt(10);  // Increased from 20+10 to 25+10
        System.out.println("🧊 Frost Wraith used FROZEN ARROW! (chance to freeze)");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied && rand.nextInt(100) < 40) target.applyFreeze(1);
        return applied ? dmg : 0;
    }

    @Override
    public int skill3(GameCharacter target) {  // Snow Cloak (temporary dodge increase for self)
        if (cd3 > 0) return 0;
        cd3 = 4;
        gainEnergy(12);
        System.out.println("🌨️ Frost Wraith used SNOW CLOAK! (temporary dodge boost for itself)");
        this.applyTempDodgeBonus(3); // Now buffs itself instead of the target
        return 0; // No damage, just effect
    }

    @Override
    public int ultimate(GameCharacter target) {  // Absolute Zero
        if (!canUseUltimate()) return 0;
        ultCd = 6;
        spendAllEnergy();
        int dmg = 50 + rand.nextInt(11);  // Increased from 45+11 to 50+11
        System.out.println("❄️ Frost Wraith used ABSOLUTE ZERO! (freezes you)");
        boolean applied = target.receiveDamage(applyCrit(dmg));
        if (applied) target.applyFreeze(1);
        return applied ? dmg : 0;
    }

    @Override
    public int decideAction(Player player) {
        // Smarter AI: Prioritize healing at low HP, ultimate when ready, then skills/basic
        if (health <= maxHealth * 0.25 && healsLeft > 0 && rand.nextInt(100) < 75) {
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
            int d = skill3(player); // Prioritize buff skill
            return d > 0 ? d : performBasicAttack(player);
        }
    }
}