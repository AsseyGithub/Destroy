package com.petrolpark.destroy.fluid.pipeEffectHandler;

import com.simibubi.create.content.fluids.OpenEndedPipe;
import com.simibubi.create.content.fluids.OpenEndedPipe.LavaEffectHandler;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BurningOpenEndedPipeEffect extends LavaEffectHandler {

    protected final Fluid fluid;

    public BurningOpenEndedPipeEffect(Fluid fluid) {
        this.fluid = fluid;
    };
    
    @Override
    public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
        return fluid.getFluid().isSame(this.fluid);
    };
};
