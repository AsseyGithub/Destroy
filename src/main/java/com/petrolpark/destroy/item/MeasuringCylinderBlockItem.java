package com.petrolpark.destroy.item;

import java.util.Collections;
import java.util.function.Consumer;

import org.joml.Vector3f;

import com.petrolpark.destroy.block.IPickUpPutDownBlock;
import com.petrolpark.destroy.block.MeasuringCylinderBlock;
import com.petrolpark.destroy.block.entity.MeasuringCylinderBlockEntity;
import com.petrolpark.destroy.block.renderer.SimpleMixtureTankRenderer.ISimpleMixtureTankRenderInformation;
import com.petrolpark.destroy.client.gui.screen.TransferFluidScreen;
import com.petrolpark.destroy.item.renderer.SimpleMixtureTankItemRenderer;
import com.petrolpark.destroy.util.DestroyLang;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour.ValueSettings;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.DistExecutor;

public class MeasuringCylinderBlockItem extends PlaceableMixtureTankItem<MeasuringCylinderBlock> implements ISimpleMixtureTankRenderInformation<ItemStack> {

    public MeasuringCylinderBlockItem(MeasuringCylinderBlock block, Properties properties) {
        super(block, properties);
    };

    public static InteractionResult tryOpenTransferScreen(Level level, BlockPos pos, BlockState state, Direction face, Player player, InteractionHand hand, ItemStack stack, boolean blockToItem) {
        if (stack.getItem() instanceof IMixtureStorageItem mixtureItem) {
            IFluidHandler otherTank = mixtureItem.getTank(level, pos, state, face, player, hand, stack, blockToItem);
            if (otherTank == null) return InteractionResult.PASS;
            LazyOptional<IFluidHandlerItem> cap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            if (!cap.isPresent()) return InteractionResult.PASS;
            ItemMixtureTank itemTank = (ItemMixtureTank)cap.resolve().get();
            int maxTransfer = blockToItem ? otherTank.drain(itemTank.getRemainingSpace(), FluidAction.SIMULATE).getAmount() : otherTank.fill(itemTank.getFluid(), FluidAction.SIMULATE);
            if (maxTransfer == 0) return InteractionResult.FAIL;

            Component itemName = mixtureItem.getNameRegardlessOfFluid(stack);
            Component blockName = state.getBlock().getName();

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> 
                openTransferScreen(pos, face, hand, 
                    new ValueSettingsBoard(
                        DestroyLang.translate("tooltip.measuring_cylinder", blockToItem ? blockName : itemName, blockToItem ? itemName : blockName).component(),
                        maxTransfer,
                        50,
                        Collections.singletonList(Lang.translateDirect("generic.unit.millibuckets")),
                        new ValueSettingsFormatter(ValueSettings::format)
                    ),
                    new ValueSettings(0, maxTransfer),
                    blockToItem
                )
            );
            return InteractionResult.SUCCESS;
        };
        return InteractionResult.PASS;
    };

    @OnlyIn(Dist.CLIENT)
    protected static void openTransferScreen(BlockPos pos, Direction sideAccessed, InteractionHand hand, ValueSettingsBoard board, ValueSettings valueSettings, boolean blockToItem) {
        ScreenOpener.open(new TransferFluidScreen(pos, sideAccessed, hand, board, valueSettings, blockToItem));
    };

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = tryOpenTransferScreen(context.getLevel(), context.getClickedPos(), context.getLevel().getBlockState(context.getClickedPos()), context.getClickedFace(), context.getPlayer(), context.getHand(), context.getItemInHand(), false);
        if (result == InteractionResult.PASS) return place(new BlockPlaceContext(context));
        return result;
    };

    @Override
    public InteractionResult attack(Level level, BlockPos pos, BlockState state, Direction face, Player player, InteractionHand hand, ItemStack stack) {
        return tryOpenTransferScreen(level, pos, state, face, player, hand, stack, true);
    };

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        return IPickUpPutDownBlock.removeItemFromInventory(context, super.place(context));
    };

    @Override
    public Couple<Vector3f> getFluidBoxDimensions() {
        return MeasuringCylinderBlockEntity.FLUID_BOX_DIMENSIONS;
    };

    @Override
    public float getFluidLevel(ItemStack container, float partialTicks) {
        return getContents(container).map(fs -> (float)fs.getAmount()).orElse(0f) / getCapacity(container);
    };

    @Override
    public FluidStack getRenderedFluid(ItemStack container) {
        return getContents(container).orElse(FluidStack.EMPTY);
    };

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new SimpleMixtureTankItemRenderer(this)));
    };
    
};
