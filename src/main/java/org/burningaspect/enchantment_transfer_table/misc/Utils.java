package org.burningaspect.enchantment_transfer_table.misc;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Utils {

    public static void drawParticleTrail(World world, Vec3d start, Vec3d end, ParticleEffect particle, int steps) {
        Vec3d direction = end.subtract(start).normalize();
        double distance = start.distanceTo(end);

        for(int i = 0; i < steps; i++) {
            double progress = (i / (double) steps) * distance;
            Vec3d point = start.add(direction.multiply(progress));

            world.addParticle(particle, point.x, point.y, point.z, 0, 0, 0);
        }
    }
}
