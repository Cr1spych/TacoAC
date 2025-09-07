package ac.anticheat.taco.animations;

import ac.anticheat.taco.TacoAC;
import ac.anticheat.taco.utils.ConfigUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Animation {
    private static JavaPlugin plugin = TacoAC.getInstance();

    private static final String particle = ConfigUtil.getString("animation.particle", "FLAME").toUpperCase();
    private static final String sound = ConfigUtil.getString("animation.sound", "ENTITY_DRAGON_FIREBALL_EXPLODE").toUpperCase();

    public static void start(Player player) {
        Location location = player.getLocation();

        final int particleCount = ConfigUtil.getInt("animation.particle_count", 80);
        final int durationTicks = ConfigUtil.getInt("animation.duration_ticks", 3);
        final boolean enabled = ConfigUtil.getBoolean("animation.enabled", true);

        if (!enabled) return;

        player.getWorld().playSound(location, Sound.valueOf(sound), 1, 1);

        spawn(location, particleCount, durationTicks);
    }

    private static void spawn(Location location, int particleCount, int durationTicks) {
        Random random = new Random();

        final double speed = ConfigUtil.getDouble("animation.speed", 0.2);

        new BukkitRunnable() {
            int ticksLived = 0;

            @Override
            public void run() {
                if (ticksLived++ > durationTicks) {
                    cancel();
                    return;
                }

                for (int i = 0; i < particleCount; i++) {
                    double x = (random.nextDouble() - 0.5) * 2;
                    double y = (random.nextDouble() - 0.5) * 2;
                    double z = (random.nextDouble() - 0.5) * 2;

                    location.getWorld().spawnParticle(
                            Particle.valueOf(particle),
                            location.getX(), location.getY(), location.getZ(),
                            1,
                            x, y, z,
                            speed
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
