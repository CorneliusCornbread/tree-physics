package com.farcr.treephysics.mixin.walk_through_leaves;

import com.farcr.treephysics.api.util.TreeUtil;
import com.farcr.treephysics.index.TreePhysicsConfig;
import com.farcr.treephysics.mixinterface.LivingEntityExtension;
import dev.ryanhcode.sable.api.math.LevelReusedVectors;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.entity_collision.SubLevelEntityCollision;
import dev.ryanhcode.sable.util.LevelAccelerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SubLevelEntityCollision.class)
public class SubLevelEntityCollisionMixin {

    @Inject(method = "getSubLevelEntityCollisionShape", at = @At("HEAD"), cancellable = true)
    private static void treephysics$getSubLevelEntityCollisionShape(Entity entity, Vector3dc boundsCenter, Pose3dc subLevelPose, BlockState state, LevelAccelerator level, BlockPos pos, LevelReusedVectors sink, CallbackInfoReturnable<VoxelShape> cir) {
        boolean walkThroughLeaves = entity instanceof LivingEntity && TreePhysicsConfig.LEAF_WALKING_BEHAVIOR.get().allowSubLevel() && TreeUtil.isLeaf(state);
        boolean wasHit = entity instanceof LivingEntityExtension extension && extension.treephysics$wasHitByTree();
        if(walkThroughLeaves || wasHit) {
            cir.setReturnValue(Shapes.empty());
        }
    }

}
