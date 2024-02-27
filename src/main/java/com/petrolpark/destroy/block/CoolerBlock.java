package com.petrolpark.destroy.block;


import com.petrolpark.destroy.block.entity.CoolerBlockEntity;
import com.petrolpark.destroy.block.entity.CoolerBlockEntity.ColdnessLevel;
import com.petrolpark.destroy.block.entity.DestroyBlockEntityTypes;
import com.petrolpark.destroy.block.shape.DestroyShapes;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CoolerBlock extends Block implements IBE<CoolerBlockEntity> {

    public static final EnumProperty<ColdnessLevel> COLD_LEVEL = EnumProperty.create("breeze", ColdnessLevel.class);

    public CoolerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
            .setValue(COLD_LEVEL, ColdnessLevel.IDLE)
            .setValue(BlazeBurnerBlock.HEAT_LEVEL, HeatLevel.NONE));
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COLD_LEVEL, BlazeBurnerBlock.HEAT_LEVEL);
    };

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
        withBlockEntityDo(level, pos, be -> be.updateHeatLevel(state.getValue(COLD_LEVEL)));
        
        if (level.isClientSide()) return;
        BlockEntity blockEntity = level.getBlockEntity(pos.above()); // Check for a Basin
        if (!(blockEntity instanceof BasinBlockEntity)) return;
        BasinBlockEntity basin = (BasinBlockEntity) blockEntity;
        basin.notifyChangeOfContents(); // Let the Basin know there's now a Cooler
    };

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult) {
        ItemStack stack = player.getItemInHand(hand);
        if (AllItems.CREATIVE_BLAZE_CAKE.isIn(stack)) {
            withBlockEntityDo(world, pos, cooler -> {
                if (getColdnessLevelOf(state) == ColdnessLevel.FROSTING) {
                    cooler.coolingTicks = 0;
                    cooler.setColdnessOfBlock(ColdnessLevel.IDLE);
                } else  {
                    cooler.coolingTicks = Integer.MAX_VALUE;
                    cooler.setColdnessOfBlock(ColdnessLevel.FROSTING);
                };
            });
            if (!player.isCreative()) stack.shrink(1);
            player.setItemInHand(hand, stack);
            return InteractionResult.sidedSuccess(world.isClientSide());
        };
        return InteractionResult.PASS;
    };

    public static InteractionResultHolder<ItemStack> tryInsert(BlockState state, Level world, BlockPos pos, ItemStack stack, boolean doNotConsume, boolean forceOverflow, boolean simulate) { //this is an override
        return InteractionResultHolder.fail(ItemStack.EMPTY);
    };

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState();
    };

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        return DestroyShapes.COOLER;
    };

    @Override
	public VoxelShape getCollisionShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (context == CollisionContext.empty()) return AllShapes.HEATER_BLOCK_SPECIAL_COLLISION_SHAPE;
		return getShape(blockState, level, pos, context);
	};

    public static ColdnessLevel getColdnessLevelOf(BlockState blockState) {
        return blockState.getValue(COLD_LEVEL);
    };

    @Override
    public Class<CoolerBlockEntity> getBlockEntityClass() {
        return CoolerBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends CoolerBlockEntity> getBlockEntityType() {
        return DestroyBlockEntityTypes.COOLER.get();
    };

    @Override
	public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
		return false;
	};
    
};
