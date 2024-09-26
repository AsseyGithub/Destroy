package com.petrolpark.destroy.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.petrolpark.destroy.block.model.DestroyPartials;
import com.petrolpark.destroy.block.renderer.CustomExplosiveMixRenderer;
import com.petrolpark.destroy.entity.CustomExplosiveMixEntity;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;

public class CustomExplosiveMixEntityRenderer extends PrimedBombRenderer<CustomExplosiveMixEntity> {

    public CustomExplosiveMixEntityRenderer(Context context) {
        super(context);
    };

    @Override
    public void renderBlock(CustomExplosiveMixEntity entity, PoseStack ms, MultiBufferSource buffer, int light, int fuse) {
        BlockState state = entity.getBlockStateToRender();
        VertexConsumer vc = buffer.getBuffer(Sheets.cutoutBlockSheet());
        SuperByteBuffer base = CachedBufferer.partial(DestroyPartials.CUSTOM_EXPLOSIVE_MIX_BASE, state)
            .forEntityRender()
            .light(light)
            .color(entity.color);

        SuperByteBuffer label = CachedBufferer.partial(DestroyPartials.CUSTOM_EXPLOSIVE_MIX_OVERLAY, state)
            .forEntityRender()
            .light(light);

        if (fuse / 5 % 2 == 0) {
            int overlay = OverlayTexture.pack(OverlayTexture.u(1f), OverlayTexture.WHITE_OVERLAY_V);
            base.overlay(overlay);
            label.overlay(overlay);
        };

        if (entity.hasCustomName()) CustomExplosiveMixRenderer.renderTruncated(ms, buffer, d -> light, entity.getCustomName().getString());

        base.renderInto(ms, vc);
        label.renderInto(ms, vc);
    };
    
};
