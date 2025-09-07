package ac.anticheat.taco.managers;

import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.checks.impl.aim.AimA;
import ac.anticheat.taco.checks.impl.aim.AimB;
import ac.anticheat.taco.checks.impl.aim.AimC;
import ac.anticheat.taco.checks.impl.combat.KillAuraA;
import ac.anticheat.taco.checks.impl.elytra.ElytraA;
import ac.anticheat.taco.checks.impl.inventory.InventoryA;
import ac.anticheat.taco.checks.impl.inventory.InventoryB;
import ac.anticheat.taco.checks.impl.inventory.InventoryC;
import ac.anticheat.taco.checks.impl.multiactions.MultiActionsA;
import ac.anticheat.taco.checks.impl.sprint.SprintA;
import ac.anticheat.taco.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.*;

public class CheckManager {
    private static final Map<UUID, List<Check>> checks = new HashMap<>();

    public static void registerChecks(Player player) {
        PlayerData playerData = new PlayerData(player);

        List<Check> playerChecks = new ArrayList<>();
        playerChecks.add(new AimA(playerData));
        playerChecks.add(new AimB(playerData));
        playerChecks.add(new AimC(playerData));
        playerChecks.add(new ElytraA(playerData));
        playerChecks.add(new MultiActionsA(playerData));
        playerChecks.add(new InventoryA(playerData));
        playerChecks.add(new InventoryB(playerData));
        playerChecks.add(new InventoryC(playerData));
        playerChecks.add(new KillAuraA(playerData));
        playerChecks.add(new SprintA(playerData));

        playerChecks.add(playerData.getActionData());
        playerChecks.add(playerData.getRotationData());
        playerChecks.add(playerData.getPacketData());

        for (Check check : playerChecks) {
            check.resetViolations();
        }

        checks.put(player.getUniqueId(), playerChecks);
    }

    public static void unregisterChecks(Player player) {
        List<Check> playerChecks = checks.remove(player.getUniqueId());
        if (playerChecks != null) {
            for (Check check : playerChecks) {
                check.resetViolations();
                check.cancelDecayTask();
            }
        }
    }

    public static List<Check> getChecks(Player player) {
        return checks.getOrDefault(player.getUniqueId(), Collections.emptyList());
    }
}