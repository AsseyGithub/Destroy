package com.petrolpark.destroy.block.renderer;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.petrolpark.destroy.block.TestTubeRackBlock;
import com.petrolpark.destroy.block.entity.TestTubeRackBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TestTubeRackRenderer extends SmartBlockEntityRenderer<TestTubeRackBlockEntity> {

    public TestTubeRackRenderer(Context context) {
        super(context);
    };

    @Override
    protected void renderSafe(TestTubeRackBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(blockEntity, partialTicks, ms, buffer, light, overlay);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ms.pushPose();
        if (!blockEntity.getBlockState().getValue(TestTubeRackBlock.X)) TransformStack.cast(ms).rotateY(-90d);
        for (int i = 0; i < blockEntity.inv.getSlots(); i++) {
            ItemStack stack = blockEntity.inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            ms.pushPose();
            ms.translate(2 / 16d + i * 4 / 16d, 6 / 16d, blockEntity.getBlockState().getValue(TestTubeRackBlock.X) ? 8 / 16d : - 8/16d);
            ms.scale(0.5f, 0.5f, 0.5f);
            TransformStack.cast(ms).rotateY(45d);
            itemRenderer.renderStatic(blockEntity.inv.getStackInSlot(i), ItemDisplayContext.FIXED, light, overlay, ms, buffer, blockEntity.getLevel(), 0);
            ms.popPose();
        };
        ms.popPose();
    };
    
};
