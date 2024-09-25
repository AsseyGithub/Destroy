package com.petrolpark.destroy.item;

import java.util.function.Supplier;

import com.petrolpark.itemdecay.ConfiguredDecayingItem;
import com.petrolpark.itemdecay.IDecayingItem;
import com.simibubi.create.foundation.config.ConfigBase.ConfigInt;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OxidizingItem extends ConfiguredDecayingItem {

    public OxidizingItem(Properties properties, Supplier<ItemStack> decayProduct, Supplier<ConfigInt> lifetime) {
        super(properties, decayProduct, lifetime);
    };

    @Override
    public String getDecayTimeTranslationKey(ItemStack stack) {
        return "item.destroy.oxidizing_item.remaining";
    };

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        checkForWater(stack, entity, isSelected);
    };

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        checkForWater(stack, entity, true);
        return super.onEntityItemUpdate(stack, entity);
    };

    public void checkForWater(ItemStack stack, Entity entity, boolean rainSensitive) {
        if (entity.isInWaterOrBubble() || (entity.isInWaterRainOrBubble() && (rainSensitive || (entity instanceof LivingEntity livingEntity && livingEntity.getOffhandItem() == stack)))) {
            IDecayingItem.extendLifetime(stack, (int)-IDecayingItem.getRemainingTime(this, stack, stack.getOrCreateTag())); // Instantly rust
        };
    };
    
};
