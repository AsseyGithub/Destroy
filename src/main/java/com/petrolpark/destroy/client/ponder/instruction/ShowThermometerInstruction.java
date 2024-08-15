package com.petrolpark.destroy.client.ponder.instruction;

import java.util.function.Consumer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.petrolpark.destroy.client.gui.DestroyGuiTextures;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.element.BoxElement;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.PonderScene.SceneTransform;
import com.simibubi.create.foundation.ponder.element.AnimatedOverlayElement;
import com.simibubi.create.foundation.ponder.instruction.FadeInOutInstruction;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.foundation.utility.animation.LerpedFloat.Chaser;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class ShowThermometerInstruction extends FadeInOutInstruction {

    protected final ThermometerElement element;
    protected final float initialTemperature;

    protected ShowThermometerInstruction(Vec3 sceneSpace, int ticks, float initialTemperature) {
        super(ticks);
        this.element = new ThermometerElement(sceneSpace);
        this.initialTemperature = Mth.clamp(initialTemperature, 0f, 1f);
        element.temperature.setValue(initialTemperature);
    };

    public static ThermometerElement add(SceneBuilder scene, Vec3 pointingAt, int ticks, float initialTemperature) {
        ShowThermometerInstruction instruction = new ShowThermometerInstruction(pointingAt, ticks, initialTemperature);
        scene.addInstruction(instruction);
        return instruction.element;
    };

    @Override
    protected void show(PonderScene scene) {
        scene.addElement(element);
        element.setVisible(true);
    };

    @Override
    protected void hide(PonderScene scene) {
        element.setVisible(false);
    };

    @Override
    protected void applyFade(PonderScene scene, float fade) {
        element.setFade(fade);
    };

    @Override
    protected void firstTick(PonderScene scene) {
        super.firstTick(scene);
        element.temperature.setValue(initialTemperature);
    };

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        element.tick(scene);
    };

    public static Consumer<PonderScene> chaseTemperature(ThermometerElement thermometer, float temperature, float speed) {
        return s -> thermometer.chaseTemperature(temperature, speed);
    };

    public static class ThermometerElement extends AnimatedOverlayElement {

        protected LerpedFloat temperature = LerpedFloat.linear();
        protected Vec3 pointingAt;

        protected ThermometerElement(Vec3 pointingAt) {
            this.pointingAt = pointingAt;
        };

        public void chaseTemperature(float temperature, float speed) {
            this.temperature.chase(Mth.clamp(temperature, 0f, 1f), speed, Chaser.LINEAR);
        };

        @Override
        protected void render(PonderScene scene, PonderUI screen, GuiGraphics graphics, float partialTicks, float fade) {
            if (fade < 1 / 16f) return;
            SceneTransform transform = scene.getTransform();
            Vec2 pointingAtScreen = transform.sceneToScreen(pointingAt, partialTicks);
            boolean settled = transform.xRotation.settled() && transform.yRotation.settled();
            float pY = settled ? (int) pointingAtScreen.y : pointingAtScreen.y;

            float boxWidth = 16f;
            float boxHeight = 32f;
            float boxBottom = -48f + 16f * fade;

            PoseStack ms = graphics.pose();


            RenderSystem.enableDepthTest();
            ms.pushPose();

            // Box
            ms.translate(pointingAtScreen.x, pY, 400f);
            new BoxElement().withBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT))
			    .gradientBorder(Theme.p(Theme.Key.TEXT_WINDOW_BORDER))
			    .at(-boxWidth / 2f, boxBottom - boxHeight, 100f)
			    .withBounds((int)boxWidth, (int)boxHeight)
			    .render(graphics);

            // Line
            int brighterColor = 0xFFF6F6E5;
            brighterColor = (0x00ffffff & brighterColor) | 0xff000000;
            ms.pushPose();
            ms.translate(0f, boxBottom, 0f);
            double lineTarget = -boxBottom * fade;
            ms.scale(1f, (float)lineTarget, 1f);
            graphics.fillGradient(0, 0, 1, 1, -100, brighterColor, brighterColor);
            graphics.fillGradient(1, 0, 2, 1, -100, 0xFF494949, 0xFF393939);
            ms.popPose();

            ms.pushPose();
            ms.translate(-boxWidth / 2, boxBottom - boxHeight, 100f);
            graphics.fill((int)(0.25f * boxWidth), (int)((0.0625f + 0.625f * (1f - temperature.getValue(partialTicks))) * boxHeight), (int)(0.75f * boxWidth), (int)(0.9375f * boxHeight), Color.mixColors(0xFF7BF7F5, 0xFFFF0000, temperature.getValue(partialTicks)));
            //ms.scale(2f, 2f, 0f);
            DestroyGuiTextures.THERMOMETER.render(graphics, 0, 0);
            ms.popPose();

            ms.popPose();
            RenderSystem.disableDepthTest();
        };

        @Override
        public void tick(PonderScene scene) {
            super.tick(scene);
            temperature.tickChaser();
        };
    };
    
};
