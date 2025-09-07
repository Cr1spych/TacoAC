package ac.anticheat.taco.listeners;

import ac.anticheat.taco.data.PlayerData;
import ac.anticheat.taco.managers.PlayerDataManager;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;

public class InventoryListener implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            WrapperPlayClientClickWindow wrapper = new WrapperPlayClientClickWindow(event);
            int windowId = wrapper.getWindowId();
            PlayerData playerData = PlayerDataManager.get(event.getPlayer());

            playerData.getInventory().setOpened(true);

            playerData.getInventory().setWindowId(windowId);
        }

        if (event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            PlayerData playerData = PlayerDataManager.get(event.getPlayer());
            playerData.getInventory().setOpened(false);
        }
    }
}
