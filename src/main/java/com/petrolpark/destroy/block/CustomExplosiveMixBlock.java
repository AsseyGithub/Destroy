package com.petrolpark.destroy.block;

import java.util.Collections;
import java.util.List;

import com.petrolpark.destroy.block.entity.CustomExplosiveMixBlockEntity;
import com.petrolpark.destroy.block.entity.DestroyBlockEntityTypes;
import com.petrolpark.destroy.block.entity.SimpleDyeableNameableCustomExplosiveMixBlockEntity;
import com.petrolpark.destroy.entity.CustomExplosiveMixEntity;
import com.petrolpark.destroy.entity.DestroyEntityTypes;
import com.petrolpark.destroy.item.inventory.CustomExplosiveMixInventory;
import com.petrolpark.destroy.world.explosion.CustomExplosiveMixExplosion;
import com.petrolpark.destroy.world.explosion.ExplosiveProperties;
import com.petrolpark.destroy.world.explosion.SmartExplosion;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class CustomExplosiveMixBlock extends PrimeableBombBlock<CustomExplosiveMixEntity> implements IBE<CustomExplosiveMixBlockEntity> {
    
    public CustomExplosiveMixBlock(Properties properties) {
        super(properties, new CustomExplosiveMixEntityFactory());
    };

    @Override
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, Direction face, LivingEntity igniter) {
        withBlockEntityDo(level, pos, be -> {
            CustomExplosiveMixInventory inv = be.getExplosiveInventory();
            ExplosiveProperties properties = inv.getExplosiveProperties();
            if (properties.fulfils(ExplosiveProperties.NO_FUSE)) {
                level.removeBlock(pos, false);
                if (level instanceof ServerLevel serverLevel) SmartExplosion.explode(serverLevel, CustomExplosiveMixExplosion.create(level, inv, igniter, Vec3.atCenterOf(pos)));
            } else if (properties.fulfils(ExplosiveProperties.CAN_EXPLODE)) {
                super.onCaughtFire(state, level, pos, face, igniter);
                level.removeBlock(pos, false);
            };
        });
    };

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock()) && pLevel.hasNeighborSignal(pPos)) onCaughtFire(pOldState, pLevel, pPos, null, null);
    };

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (pLevel.hasNeighborSignal(pPos)) {
           onCaughtFire(pState, pLevel, pPos, null, null);
        };
    };

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    };

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        withBlockEntityDo(pLevel, pPos, be -> {
            if (be.getExplosiveInventory().getExplosiveProperties().fulfils(ExplosiveProperties.EXPLODES_RANDOMLY)) onCaughtFire(pState, pLevel, pPos, null, null);
        });
    };

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity pPlacer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, pPlacer, stack);
        withBlockEntityDo(level, pos, be -> be.onPlace(stack));
    };

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult pHit) {
        ItemStack stack = player.getItemInHand(hand);
        boolean lighter = stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE);
        InteractionResult result = onBlockEntityUse(level, pos, be -> {
            if (lighter && be.getExplosiveInventory().getExplosiveProperties().fulfils(ExplosiveProperties.CAN_EXPLODE)) return super.use(state, level, pos, player, hand, pHit);
            return be.tryDye(stack, pHit, level, pos, player);
        });
        if (result != InteractionResult.PASS) return result;
        if (!lighter && !level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            withBlockEntityDo(level, pos, be -> NetworkHooks.openScreen(serverPlayer, be, be::writeToBuffer));
            return InteractionResult.sidedSuccess(level.isClientSide());
        };
        return InteractionResult.PASS;
    };

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        BlockEntity be = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (!(be instanceof SimpleDyeableNameableCustomExplosiveMixBlockEntity ebe)) return Collections.emptyList();
        return Collections.singletonList(ebe.getFilledItemStack(DestroyBlocks.CUSTOM_EXPLOSIVE_MIX.asStack()));
    };

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return getCloneItemStack(level, pos);
    };

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return getCloneItemStack(level, pos);
    };

    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos) {
        SimpleDyeableNameableCustomExplosiveMixBlockEntity be = getBlockEntity(level, pos);
        if (be == null) return ItemStack.EMPTY;
        return be.getFilledItemStack(DestroyBlocks.CUSTOM_EXPLOSIVE_MIX.asStack());
    };

    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true; // So the text on the side renders correctly
    };

    @Override
    public Class<CustomExplosiveMixBlockEntity> getBlockEntityClass() {
        return CustomExplosiveMixBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends CustomExplosiveMixBlockEntity> getBlockEntityType() {
        return DestroyBlockEntityTypes.CUSTOM_EXPLOSIVE_MIX.get();
    };

    public static class CustomExplosiveMixEntityFactory implements PrimedBombEntityFactory<CustomExplosiveMixEntity> {

        @Override
        public CustomExplosiveMixEntity create(Level level, BlockPos pos, BlockState state, LivingEntity igniter) {
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof CustomExplosiveMixBlockEntity ebe)) return DestroyEntityTypes.PRIMED_CUSTOM_EXPLOSIVE.create(level);
            CustomExplosiveMixEntity entity = new CustomExplosiveMixEntity(level, pos, state, igniter, ebe.getColor(), ebe.getExplosiveInventory());
            entity.setCustomName(ebe.getCustomName());
            return entity;
        };
        
    };
    
};
