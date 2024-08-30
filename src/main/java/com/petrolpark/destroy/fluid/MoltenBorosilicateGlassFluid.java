package com.petrolpark.destroy.fluid;

import com.petrolpark.destroy.block.DestroyBlocks;
import com.petrolpark.destroy.item.DestroyItems;
import com.petrolpark.fluid.ICustomBlockStateFluid;
import com.simibubi.create.content.fluids.VirtualFluid;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

public class MoltenBorosilicateGlassFluid extends VirtualFluid implements ICustomBlockStateFluid {

    public MoltenBorosilicateGlassFluid(Properties properties) {
        super(properties);
    };

    @Override
    public Item getBucket() {
        return DestroyItems.MOLTEN_BOROSILICATE_GLASS_BUCKET.get();
    };

    @Override
    public BlockState getBlockState() {
        return DestroyBlocks.MOLTEN_BOROSILICATE_GLASS.getDefaultState();
    };
    
};
