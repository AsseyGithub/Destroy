package com.petrolpark.destroy.mixin.compat.jei;

import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.common.gui.TooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.petrolpark.destroy.chemistry.Molecule;
import com.petrolpark.destroy.item.MoleculeDisplayItem;

@Mixin(TooltipRenderer.class)
public abstract class TooltipRendererMixin {
    
    /**
     * Injection into JEI's {@link mezz.jei.common.gui.TooltipRenderer#drawHoveringText TooltipRenderer}.
     * This allows {@link com.petrolpark.destroy.item.MoleculeDisplayItem Molecule tooltips} to be rendered - usually tooltips are only rendered for Item Stack ingredients.
     */
    @Inject(
        method = "Lmezz/jei/common/gui/TooltipRenderer;drawHoveringText(Lnet/minecraft/client/gui/GuiGraphics;Ljava/util/List;IILjava/lang/Object;Lmezz/jei/api/ingredients/IIngredientRenderer;)V",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private static <T> void inDrawHoveringText(GuiGraphics graphics, List<Component> textLines, int x, int y, T ingredient, IIngredientRenderer<T> ingredientRenderer, CallbackInfo ci) {
        if (ingredient instanceof Molecule molecule) {
            Minecraft minecraft = Minecraft.getInstance();
            Font font = ingredientRenderer.getFontRenderer(minecraft, ingredient);
            invokeDrawHoveringText(graphics, textLines, x, y, MoleculeDisplayItem.with(molecule), font);
            ci.cancel();
        };
    };

    @Invoker(
        value = "drawHoveringText",
        remap = false
    )
    public static void invokeDrawHoveringText(GuiGraphics graphics, List<Component> textLines, int x, int y, ItemStack itemStack, Font font) {};
};
