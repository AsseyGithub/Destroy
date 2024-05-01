package com.petrolpark.destroy.block;

import com.petrolpark.destroy.item.DestroyItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MoltenBorosilicateGlassBlock extends SemiMoltenBlock {

    public MoltenBorosilicateGlassBlock(Properties properties) {
        super(properties);
    };

    @Override
    public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        return DestroyItems.MOLTEN_BOROSILICATE_GLASS_BUCKET.asStack();
    };

    @Override
    public Item asItem() {
        return DestroyItems.MOLTEN_BOROSILICATE_GLASS_BUCKET.get();
    };

    @Override
    public BlockState getSolidifiedBlockState() {
        return DestroyBlocks.BOROSILICATE_GLASS.getDefaultState();
    };

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction side) {
        return DestroyBlocks.MOLTEN_BOROSILICATE_GLASS.has(adjacentState) || (DestroyBlocks.BOROSILICATE_GLASS_FIBER.has(adjacentState) && adjacentState.getValue(FastCoolingMoltenPillarBlock.MOLTEN)) || super.skipRendering(state, adjacentState, side);
    };

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    };
  
    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1f;
    };
  
    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
    };
    
};
