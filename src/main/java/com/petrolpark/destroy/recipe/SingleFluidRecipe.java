package com.petrolpark.destroy.recipe;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public abstract class SingleFluidRecipe extends ProcessingRecipe<RecipeWrapper> {

    public SingleFluidRecipe(IRecipeTypeInfo typeInfo, ProcessingRecipeParams params) {
        super(typeInfo, params);
        if (processingDuration <= 0) processingDuration = 20;
    };

    public FluidIngredient getRequiredFluid() {
        if (fluidIngredients.isEmpty()) {
            throw new IllegalStateException(this.getRecipeTypeName() + " Recipe: " + id.toString() + " has no fluid ingredient!");
        };
        return fluidIngredients.get(0);
    };

    @Override
    public boolean matches(RecipeWrapper pContainer, Level level) {
        return true;
    };

    @Override
    protected int getMaxInputCount() {
        return 0;
    };

    @Override
    protected int getMaxOutputCount() {
        return 0;
    };

    @Override
    protected final int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 2;
    };

    @Override
    protected boolean canSpecifyDuration() {
		return true;
	};

    /**
     * Used in error message
     * @return Name of Recipe Type (e.g. "Centrifugation")
     */
    public abstract String getRecipeTypeName();
}
