package com.petrolpark.destroy.block.renderer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.petrolpark.destroy.block.RedstoneProgrammerBlock;
import com.petrolpark.destroy.block.entity.RedstoneProgrammerBlockEntity;
import com.petrolpark.destroy.block.model.DestroyPartials;
import com.petrolpark.destroy.util.RedstoneProgram;
import com.petrolpark.destroy.util.RedstoneProgram.Channel;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class RedstoneProgrammerRenderer extends SafeBlockEntityRenderer<RedstoneProgrammerBlockEntity> {

    public RedstoneProgrammerRenderer(BlockEntityRendererProvider.Context context) {};

    @Override
    protected void renderSafe(RedstoneProgrammerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        Direction direction = be.getBlockState().getValue(RedstoneProgrammerBlock.FACING);
        RedstoneProgram program = be.programmer.program;
        VertexConsumer vc = bufferSource.getBuffer(RenderType.cutout());
        SuperByteBuffer cylinder = CachedBufferer.partial(DestroyPartials.REDSTONE_PROGRAMMER_CYLINDER, be.getBlockState())
            .centre()
            .rotateY(AngleHelper.horizontalAngle(direction))
            .unCentre();
        SuperByteBuffer needle = CachedBufferer.partial(DestroyPartials.REDSTONE_PROGRAMMER_NEEDLE, be.getBlockState())
            .centre()
            .rotateY(AngleHelper.horizontalAngle(direction))
            .unCentre();

        float rotation = program.paused ? 0f : AnimationTickHolder.getRenderTime();

        cylinder
            .translate(0, 6 / 16d, 10 / 16d)
            .rotateX(rotation)
            .translateBack(0, 6 / 16d, 10 / 16d);

        needle
            .translate(0d, 8.5 / 16d, 5.5 / 16d)
            .rotateX(-2 + 8 * -Mth.sin(4 * AngleHelper.rad(rotation)))
            .translateBack(0d, 8.5 / 16d, 5.5 / 16d);

        cylinder.renderInto(ms, vc);
        needle.renderInto(ms, vc);

        ImmutableList<Channel> channels = program.getChannels();
        for (int i = 0; i < 6; i++) {
            if (i >= channels.size()) continue;
            boolean powered = !program.paused && program.getChannels().get(i).getTransmittedStrength() != 0;
            CachedBufferer.partial(powered ? DestroyPartials.REDSTONE_PROGRAMMER_TRANSMITTER_POWERED : DestroyPartials.REDSTONE_PROGRAMMER_TRANSMITTER, be.getBlockState())
                .centre()
                .rotateY(AngleHelper.horizontalAngle(direction))
                .translate(i % 2 == 0 ? 0f : 15 / 16f, 1 / 16f, (3 + 4.5F * (i / 2)) / 16f)
                .unCentre()
                .renderInto(ms, vc);
        };
    };
    
};
