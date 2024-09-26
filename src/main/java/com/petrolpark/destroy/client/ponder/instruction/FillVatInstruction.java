package com.petrolpark.destroy.client.ponder.instruction;

import com.petrolpark.destroy.block.entity.DestroyBlockEntityTypes;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.instruction.PonderInstruction;

import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class FillVatInstruction extends PonderInstruction {

    public final BlockPos vatControllerPos;
    public final FluidStack fillStack;

    public FillVatInstruction(BlockPos vatControllerPos, FluidStack fillStack) {
        this.vatControllerPos = vatControllerPos;
        this.fillStack = fillStack;
    };

    @Override
    public boolean isComplete() {
        return true;
    };

    @Override
    public void tick(PonderScene scene) {
        scene.getWorld().getBlockEntity(vatControllerPos, DestroyBlockEntityTypes.VAT_CONTROLLER.get()).ifPresent(vat ->
            vat.addFluid(fillStack, FluidAction.SIMULATE)
        );
    };
    
};
