package com.farcr.treephysics.mixin.leaf_collision;

import com.farcr.treephysics.api.util.TreeUtil;
import com.farcr.treephysics.index.TreePhysicsConfig;
import com.farcr.treephysics.index.TreePhysicsTags;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.ryanhcode.sable.physics.impl.rapier.RapierPhysicsPipeline;
import dev.ryanhcode.sable.physics.impl.rapier.collider.RapierVoxelColliderBakery;
import dev.ryanhcode.sable.physics.impl.rapier.collider.RapierVoxelColliderData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RapierPhysicsPipeline.class)
public class RapierPhysicsPipelineMixin {

    @WrapOperation(method = "handleBlockChange", at =
        @At(value = "INVOKE", target = "Ldev/ryanhcode/sable/physics/impl/rapier/collider/RapierVoxelColliderBakery;getPhysicsDataForBlock(Lnet/minecraft/world/level/block/state/BlockState;)Ldev/ryanhcode/sable/physics/impl/rapier/collider/RapierVoxelColliderData;", ordinal = 0)
    )
    private RapierVoxelColliderData treephysics$handleBlockChange1(RapierVoxelColliderBakery instance, BlockState state, Operation<RapierVoxelColliderData> original, @Local(name = "pos") BlockPos pos) {
        if(treephysics$shouldSkipBlockChangeCollider(state)) {
            return null;
        }
        return original.call(instance, state);
    }

    @WrapOperation(method = "handleBlockChange", at =
        @At(value = "INVOKE", target = "Ldev/ryanhcode/sable/physics/impl/rapier/collider/RapierVoxelColliderBakery;getPhysicsDataForBlock(Lnet/minecraft/world/level/block/state/BlockState;)Ldev/ryanhcode/sable/physics/impl/rapier/collider/RapierVoxelColliderData;", ordinal = 1)
    )
    private RapierVoxelColliderData treephysics$handleBlockChange2(RapierVoxelColliderBakery instance, BlockState state, Operation<RapierVoxelColliderData> original, @Local(name = "globalBlockPos") BlockPos globalBlockPos) {
        if(treephysics$shouldSkipBlockChangeCollider(state)) {
            return null;
        }
        return original.call(instance, state);
    }

    @WrapOperation(method = "handleChunkSectionAddition", at =
        @At(value = "INVOKE", target = "Ldev/ryanhcode/sable/physics/impl/rapier/collider/RapierVoxelColliderBakery;getPhysicsDataForBlock(Lnet/minecraft/world/level/block/state/BlockState;)Ldev/ryanhcode/sable/physics/impl/rapier/collider/RapierVoxelColliderData;")
    )
    private RapierVoxelColliderData treephysics$handleChunkSectionAddition(RapierVoxelColliderBakery instance, BlockState state, Operation<RapierVoxelColliderData> original, @Local(name = "globalPos") BlockPos globalPos) {
        if(treephysics$shouldSkipLeafCollider(state)) {
            return null;
        }
        return original.call(instance, state);
    }

    @Unique
    private boolean treephysics$shouldSkipBlockChangeCollider(BlockState state) {
        return treephysics$shouldSkipLeafCollider(state) || state.is(TreePhysicsTags.FALLS_FROM_TREES);
    }

    @Unique
    private boolean treephysics$shouldSkipLeafCollider(BlockState state) {
        return TreeUtil.isLeaf(state) && !TreePhysicsConfig.STATIC_LEAF_COLLISION.get();
    }
}
