package ac.anticheat.taco.utils;

import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

// это тоже хавайте
public class BoundingBoxUtil {
    public static List<Vector> getSamplePoints(BoundingBox box) {
        List<Vector> points = new ArrayList<>();

        int steps = 5;
        double dx = (box.getMaxX() - box.getMinX()) / steps;
        double dy = (box.getMaxY() - box.getMinY()) / steps;
        double dz = (box.getMaxZ() - box.getMinZ()) / steps;

        for (int i = 0; i <= steps; i++) {
            for (int j = 0; j <= steps; j++) {
                for (int k = 0; k <= steps; k++) {
                    double x = box.getMinX() + dx * i;
                    double y = box.getMinY() + dy * j;
                    double z = box.getMinZ() + dz * k;
                    points.add(new Vector(x, y, z));
                }
            }
        }

        return points;
    }
}
