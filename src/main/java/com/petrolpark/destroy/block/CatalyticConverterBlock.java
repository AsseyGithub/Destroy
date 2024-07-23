package com.petrolpark.destroy.block;

import com.petrolpark.destroy.block.entity.CatalyticConverterBlockEntity;
import com.petrolpark.destroy.block.entity.DestroyBlockEntityTypes;
import com.petrolpark.destroy.block.shape.DestroyShapes;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CatalyticConverterBlock extends DirectionalBlock implements IBE<CatalyticConverterBlockEntity> {

    protected CatalyticConverterBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP));
    };

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getClickedFace());
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
    };

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return DestroyShapes.CATALYTIC_CONVERER.get(pState.getValue(FACING));
    };

    @Override
    public Class<CatalyticConverterBlockEntity> getBlockEntityClass() {
        return CatalyticConverterBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends CatalyticConverterBlockEntity> getBlockEntityType() {
        return DestroyBlockEntityTypes.CATALYTIC_CONVERTER.get();
    };
    
};
