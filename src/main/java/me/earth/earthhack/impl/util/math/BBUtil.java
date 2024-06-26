package me.earth.earthhack.impl.util.math;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;

public class BBUtil
{
    public static final Box EMPTY_BOX = new Box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

    public static boolean intersects(Box bb, Vec3i vec3i)
    {
        return bb.minX < vec3i.getX() + 1
                && bb.maxX > vec3i.getX()
                && bb.minY < vec3i.getY() + 1
                && bb.maxY > vec3i.getY()
                && bb.minZ < vec3i.getZ() + 1
                && bb.maxZ > vec3i.getZ();
    }

}
