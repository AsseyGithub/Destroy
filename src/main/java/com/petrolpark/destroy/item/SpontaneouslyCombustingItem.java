package com.petrolpark.destroy.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;

public class SpontaneouslyCombustingItem extends Item {

    public SpontaneouslyCombustingItem(Properties properties) {
        super(properties);
    };

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(stack, level, entity, pSlotId, pIsSelected);
        if (level.getRandom().nextInt(1200) == 0) {
            stack.shrink(1);
            entity.setSecondsOnFire(3);
        };
    };

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (entity.level().getRandom().nextInt(1200) == 0) {
            stack.shrink(1);
            BlockPos pos = BlockPos.containing(entity.position());
            entity.level().setBlockAndUpdate(pos, BaseFireBlock.getState(entity.level(), pos));
        };
        return super.onEntityItemUpdate(stack, entity);
    };
    
};
