package ac.anticheat.taco.player;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class PlayerInventory {
    private final Player player;
    private final User user;

    private int windowId;
    private boolean isOpened;

    public PlayerInventory(Player player) {
        this.player = player;
        this.user = PacketEvents.getAPI().getPlayerManager().getUser(player);
    }

    public void closeInventory() {
        if (user == null) return;

        user.writePacket(new WrapperPlayServerCloseWindow(windowId));
        PacketEvents.getAPI().getProtocolManager().receivePacket(
                user.getChannel(),
                new WrapperPlayClientCloseWindow(windowId)
        );
        user.flushPackets();
    }

    public ItemStack getHelmet() {
        return player.getInventory().getHelmet();
    }

    public ItemStack getChestplate() {
        return player.getInventory().getChestplate();
    }

    public ItemStack getLeggings() {
        return player.getInventory().getLeggings();
    }

    public ItemStack getBoots() {
        return player.getInventory().getBoots();
    }

    public ItemStack getItemInOffHand() {
        return player.getInventory().getItemInOffHand();
    }

    public ItemStack getItemInMainHand() {
        return player.getInventory().getItemInMainHand();
    }
}
