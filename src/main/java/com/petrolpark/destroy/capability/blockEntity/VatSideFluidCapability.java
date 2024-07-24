package com.petrolpark.destroy.capability.blockEntity;

import com.petrolpark.destroy.block.entity.VatSideBlockEntity;
import com.petrolpark.destroy.block.entity.VatControllerBlockEntity.VatTankWrapper;
import com.petrolpark.destroy.fluid.DestroyFluids;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class VatSideFluidCapability extends VatTankWrapper {

    private final VatSideBlockEntity vatSide;

    public IFluidHandler getLiquidOutput() {
        return itemHandler[0];
    };

    public IFluidHandler getGasOutput() {
        return itemHandler[1];
    };

    public IFluidHandler getInput() {
        return itemHandler[2];
    };

    public VatSideFluidCapability(VatSideBlockEntity vatSide, IFluidHandler liquidOutput, IFluidHandler gasOutput, IFluidHandler input) {
        super(vatSide::getController, input::fill, liquidOutput, gasOutput, input);
        this.vatSide = vatSide;
    };

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return drain(resource.getAmount(), action);
    };

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return vatSide.isPipeSubmerged(false, null) ? drainLiquidTank(maxDrain, action) : drainGasTankWithMolarDensity(maxDrain, DestroyFluids.AIR_MOLAR_DENSITY, action);
    };
    
};
