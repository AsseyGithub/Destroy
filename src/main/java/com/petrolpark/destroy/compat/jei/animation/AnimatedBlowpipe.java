package com.petrolpark.destroy.compat.jei.animation;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.petrolpark.destroy.block.BlowpipeBlock;
import com.petrolpark.destroy.block.DestroyBlocks;
import com.petrolpark.destroy.block.renderer.BlowpipeRenderer;
import com.petrolpark.destroy.recipe.GlassblowingRecipe;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Brightness;
import net.minecraftforge.fluids.FluidStack;

public class AnimatedBlowpipe extends AnimatedKinetics {

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        //NOOP
    };

    public void draw(GlassblowingRecipe recipe, FluidStack fluid, GuiGraphics graphics) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(0, 0, 200);
		ms.mulPose(Axis.XP.rotationDegrees(-37.5f));
		ms.mulPose(Axis.YP.rotationDegrees(247.5f));
        ms.scale(23f, 23f, 23f);
        ms.pushPose();

        blockElement(DestroyBlocks.BLOWPIPE.getDefaultState().setValue(BlowpipeBlock.FACING, Direction.NORTH))
            .render(graphics);
        TransformStack.cast(ms)
            .rotateY(180d)
            .translate(-0.5d, -0.5d, 0d);
        ms.pushPose();
        BlowpipeRenderer.render(recipe, fluid, Math.min((AnimationTickHolder.getRenderTime() % 120f) / 100f, 1f), ms, graphics.bufferSource(), Brightness.FULL_BRIGHT.pack(), OverlayTexture.NO_OVERLAY);
        ms.popPose();

        ms.popPose();
        ms.popPose();
    };
    
};
