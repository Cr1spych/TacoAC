package ac.anticheat.taco.checks.impl.combat;

import ac.anticheat.taco.TacoAC;
import ac.anticheat.taco.checks.Check;
import ac.anticheat.taco.checks.type.PacketCheck;
import ac.anticheat.taco.utils.BoundingBoxUtil;
import ac.anticheat.taco.utils.BukkitSchedulerUtil;
import ac.anticheat.taco.utils.ConfigUtil;
import ac.anticheat.taco.utils.PacketUtil;
import ac.anticheat.taco.data.PlayerData;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.List;

public class KillAuraA extends Check implements PacketCheck {
    private final Plugin plugin;
    private double buffer;
    private double hitboxExpand;
    private double wallhitThreshold;

    public KillAuraA(PlayerData playerData) {
        super("KillAuraA", playerData);

        this.plugin = TacoAC.getInstance();
        this.hitboxExpand = ConfigUtil.getDouble("checks.KillAuraA.hitbox-expand", 0.55);
        this.wallhitThreshold = ConfigUtil.getDouble("checks.KillAuraA.wallhit-threshold", 0.99);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!isEnabled()) return;

        if (PacketUtil.isAttack(event)) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
            int entityId = wrapper.getEntityId();

            BukkitSchedulerUtil.runSync(() -> {
                Entity target = null;
                for (Entity e : ((Player) event.getPlayer()).getWorld().getEntities()) {
                    if (e.getEntityId() == entityId) {
                        target = e;
                        break;
                    }
                }

                if (target == null) return;

                if (target instanceof Player && isWallHit(playerData.getBukkitPlayer(), target)) {
                    if (++buffer > 4) {
                        buffer = 0;
                        punish();
                    }
                } else {
                    buffer = 0;
                }
            });
        }
    }

    // хавайте
    private boolean isWallHit(Player attacker, Entity target) {
        Location eye = attacker.getEyeLocation();
        BoundingBox box = target.getBoundingBox().clone().expand(hitboxExpand);

        List<Vector> points = BoundingBoxUtil.getSamplePoints(box);

        int blocked = 0;
        int total = points.size();

        for (Vector point : points) {
            double distance = eye.toVector().distance(point);
            RayTraceResult result = attacker.getWorld().rayTraceBlocks(
                    eye,
                    point.clone().subtract(eye.toVector()).normalize(),
                    distance,
                    FluidCollisionMode.NEVER,
                    true
            );

            if (result != null) {
                blocked++;
            }
        }

        double blockedRatio = (double) blocked / total;
        return blockedRatio >= wallhitThreshold;
    }
}