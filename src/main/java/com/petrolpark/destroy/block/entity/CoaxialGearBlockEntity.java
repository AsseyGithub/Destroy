package com.petrolpark.destroy.block.entity;

import java.util.List;

import com.petrolpark.destroy.advancement.DestroyAdvancementTrigger;
import com.petrolpark.destroy.block.CoaxialGearBlock;
import com.petrolpark.destroy.block.DestroyBlocks;
import com.petrolpark.destroy.block.DirectionalRotatedPillarKineticBlock;
import com.petrolpark.destroy.block.entity.behaviour.DestroyAdvancementBehaviour;
import com.petrolpark.destroy.util.KineticsHelper;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CoaxialGearBlockEntity extends BracketedKineticBlockEntity {

    protected DestroyAdvancementBehaviour advancementBehaviour;

    public CoaxialGearBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        advancementBehaviour = new DestroyAdvancementBehaviour(this, DestroyAdvancementTrigger.COAXIAL_GEAR);
        behaviours.add(advancementBehaviour);
    };

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        Direction direction = KineticsHelper.directionBetween(target.getBlockPos(), getBlockPos());
        if (direction != null) {
            if (stateTo.getBlock() instanceof IRotate iRotate && iRotate.hasShaftTowards(getLevel(), target.getBlockPos(), stateTo, direction)) CoaxialGearBlock.updatePropagationOfLongShaft(stateFrom, level, getBlockPos());
        };
        return super.propagateRotationTo(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
	};

    @Override
    @SuppressWarnings("null")
    public void tick() {
        super.tick();
        if (isVirtual() || !hasLevel()) return;
        if (getBlockState().getValue(CoaxialGearBlock.HAS_SHAFT) && !getLevel().isClientSide()) { // It thinks getLevel() might be null (it's not)
            Axis axis = getBlockState().getValue(RotatedPillarKineticBlock.AXIS);
            boolean longShaftExists = false;
            for (AxisDirection axisDirection : AxisDirection.values()) {
                BlockPos longShaftPos = getBlockPos().relative(Direction.get(axisDirection, axis));
                BlockState longShaftState = getLevel().getBlockState(longShaftPos); // It thinks getLevel() might be null (it's not)
                if (DestroyBlocks.LONG_SHAFT.has(longShaftState) && longShaftState.getValue(RotatedPillarKineticBlock.AXIS) == axis && longShaftState.getValue(DirectionalRotatedPillarKineticBlock.POSITIVE_AXIS_DIRECTION) == (axisDirection != AxisDirection.POSITIVE)) {
                    longShaftExists = true;
                    if (speed != 0f && advancementBehaviour.getPlayer() != null && getLevel().getBlockEntity(longShaftPos, DestroyBlockEntityTypes.LONG_SHAFT.get()).map(
                        be -> be.getSpeed() != 0f && be.getSpeed() != speed
                    ).orElse(false)) {
                        advancementBehaviour.awardDestroyAdvancement(DestroyAdvancementTrigger.COAXIAL_GEAR);
                    };
                    break;
                };
            };
            if (!longShaftExists) {
                getLevel().setBlockAndUpdate(getBlockPos(), AllBlocks.SHAFT.getDefaultState().setValue(RotatedPillarKineticBlock.AXIS, axis));
                Block.dropResources(getBlockState(), level, getBlockPos());
            };
        };
    };
    
};
