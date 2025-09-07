package ac.anticheat.taco.managers;

import ac.anticheat.taco.data.PlayerData;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    private static final Map<UUID, PlayerData> players = new ConcurrentHashMap<>();

    public static PlayerData get(Player player) {
        return players.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerData(player));
    }

    public static void remove(Player player) {
        players.remove(player.getUniqueId());
    }
}
