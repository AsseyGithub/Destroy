package com.petrolpark.destroy.client.ponder.instruction;

import com.mojang.blaze3d.vertex.PoseStack;
import com.petrolpark.destroy.item.CircuitMaskItem;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.element.AnimatedOverlayElement;
import com.simibubi.create.foundation.ponder.instruction.FadeInOutInstruction;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import com.simibubi.create.foundation.utility.Pointing;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class ShowCircuitPatternInstruction extends FadeInOutInstruction {

    private final Vec3 sceneSpace;
    private final Pointing direction;

    private final int pattern;
    
    private CircuitMaskElement element;

	public ShowCircuitPatternInstruction(Pointing direction, Vec3 sceneSpace, int pattern, int ticks) {
		super(ticks);

        this.sceneSpace = sceneSpace;
        this.direction = direction;
        this.pattern = pattern;

		this.element = new CircuitMaskElement();
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

    public class CircuitMaskElement extends AnimatedOverlayElement {

        /**
         * Largely copied from {@link com.simibubi.create.foundation.ponder.element.InputWindowElement Create source code}.
         */
        @Override
        protected void render(PonderScene scene, PonderUI screen, GuiGraphics graphics, float partialTicks, float fade) {
            float xFade = direction == Pointing.RIGHT ? -1 : direction == Pointing.LEFT ? 1 : 0;
            float yFade = direction == Pointing.DOWN ? -1 : direction == Pointing.UP ? 1 : 0;
            xFade *= 10 * (1 - fade);
            yFade *= 10 * (1 - fade);

            if (fade < 1 / 16f) return;
            Vec2 sceneToScreen = scene.getTransform().sceneToScreen(sceneSpace, partialTicks);

            PoseStack ms = graphics.pose();
            ms.pushPose();
            ms.translate(sceneToScreen.x + xFade, sceneToScreen.y + yFade, 400);
            PonderUI.renderSpeechBox(graphics, 0, 0, 48, 48, false, direction, true);
            ms.translate(0, 0, 100);
            CircuitMaskItem.renderCircuitMask(pattern, graphics);
            ms.translate(0, 0, 100);
            ms.popPose();
        };
        
    };
};
