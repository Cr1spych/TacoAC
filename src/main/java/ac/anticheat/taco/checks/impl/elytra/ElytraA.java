package ac.anticheat.taco.checks.impl.elytra;

import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.checks.type.PacketCheck;
import ac.anticheat.taco.utils.PacketUtil;
import ac.anticheat.taco.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ElytraA extends Check implements PacketCheck {
    public ElytraA(PlayerData playerData) {
        super("ElytraA", playerData);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!isEnabled()) return;

        if (PacketUtil.isStartFlyingWithElytra(event)) {
            ItemStack chest = playerData.getInventory().getChestplate();
            boolean chestIsNull = chest == null;
            boolean chestIsElytra = !chestIsNull && chest.getType() == Material.ELYTRA;

            if ((chestIsNull || !chestIsElytra) || playerData.getBukkitPlayer().isGliding() || playerData.getBukkitPlayer().isInsideVehicle()) {
                punish("Gliding=" + playerData.getBukkitPlayer().isGliding() + ", In vehicle=" + playerData.getBukkitPlayer().isInsideVehicle() + ", Elytra=" + chestIsElytra);
            }
        }
    }
}
