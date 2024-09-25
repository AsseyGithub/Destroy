package com.petrolpark.destroy.compat.jei.animation;

import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.client.gui.MoleculeRenderer;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;

public class JEIMoleculeRenderer extends MoleculeRenderer implements IDrawable {

    public JEIMoleculeRenderer(LegacySpecies molecule) {
        super(molecule);
    };

    @Override
    public int getWidth() {
        return width;
    };

    @Override
    public int getHeight() {
        return height;
    };

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        render(xOffset, yOffset, graphics);
    };
    
};
