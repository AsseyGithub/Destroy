package com.petrolpark.destroy.block.entity;

import com.petrolpark.destroy.item.DestroyItems;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;

import net.minecraft.world.item.ItemStack;

public interface IHaveLabGoggleInformation extends IHaveGoggleInformation {

    @Override
    default ItemStack getIcon(boolean isPlayerSneaking) {
        return DestroyItems.LABORATORY_GOGGLES.asStack();
    };
    
};
