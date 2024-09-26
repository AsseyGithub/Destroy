package com.petrolpark.destroy.block.display;

import javax.annotation.Nullable;

import com.petrolpark.destroy.block.entity.PollutometerBlockEntity;
import com.petrolpark.destroy.capability.Pollution.PollutionType;
import com.petrolpark.destroy.util.DestroyLang;
import com.petrolpark.destroy.util.PollutionHelper;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.PercentOrProgressBarDisplaySource;

import net.minecraft.network.chat.Component;

public class PollutometerDisplaySource extends PercentOrProgressBarDisplaySource {

    @Override
    @Nullable
    protected Float getProgress(DisplayLinkContext context) {
        if (!(context.getSourceBlockEntity() instanceof PollutometerBlockEntity pollutometer)) return null;
        PollutionType pollutionType = pollutometer.getPollutionType();
        return (float)PollutionHelper.getPollution(context.level(), context.getSourcePos(), pollutionType) / pollutionType.max;
    };

    @Override
    protected boolean progressBarActive(DisplayLinkContext context) {
        return true;
    };

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    };

    @Override
    public Component getName() {
        return DestroyLang.translate("display_source.pollutometer").component();
    };
    
};
