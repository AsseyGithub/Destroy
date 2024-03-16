package com.petrolpark.destroy.chemistry.reactionresult;

import com.petrolpark.destroy.advancement.DestroyAdvancementTrigger;
import com.petrolpark.destroy.block.entity.VatControllerBlockEntity;
import com.petrolpark.destroy.block.entity.behaviour.DestroyAdvancementBehaviour;
import com.petrolpark.destroy.chemistry.Reaction;
import com.petrolpark.destroy.chemistry.ReactionResult;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;

import net.minecraft.world.level.Level;

/**
 * Awards a {@link com.petrolpark.destroy.advancement.DestroyAdvancementTrigger Destroy Advancement} when enough of a Reaction takes place.
 * It is recommended that addon creators make their own child class of {@link com.petrolpark.destroy.chemistry.ReactionResult ReactionResult} for Advancements rather than trying to piggyback off this.
 */
public class DestroyAdvancementReactionResult extends ReactionResult {

    private final DestroyAdvancementTrigger advancement;

    public DestroyAdvancementReactionResult(float moles, Reaction reaction, DestroyAdvancementTrigger advancement) {
        super(moles, reaction);
        this.advancement = advancement;
    };

    @Override
    public void onBasinReaction(Level level, BasinBlockEntity basin) {
        DestroyAdvancementBehaviour behaviour = basin.getBehaviour(DestroyAdvancementBehaviour.TYPE);
        if (behaviour != null) behaviour.awardDestroyAdvancement(advancement);
    };

    @Override
    public void onVatReaction(Level level, VatControllerBlockEntity vatController) {
        vatController.getBehaviour(DestroyAdvancementBehaviour.TYPE).awardDestroyAdvancement(advancement);
    };
    
};
