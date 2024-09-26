package com.petrolpark.destroy.network.packet;

import java.util.function.Supplier;

import com.petrolpark.destroy.block.entity.KeypunchBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent.Context;

public class ChangeKeypunchPositionC2SPacket extends C2SPacket {

    public final BlockPos pos;
    public final int pistonPosition;

    public ChangeKeypunchPositionC2SPacket(BlockPos pos, int pistonPosition) {
        this.pos = pos;
        this.pistonPosition = pistonPosition;
    };

    public ChangeKeypunchPositionC2SPacket(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        pistonPosition = buffer.readVarInt();
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeVarInt(pistonPosition);
    };

    @Override
    public boolean handle(Supplier<Context> supplier) {
        Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender  = context.getSender();
            if (sender == null) return;
            Level level = sender.level();
            if (level == null) return;
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof KeypunchBlockEntity keypunch) {
                keypunch.setPistonPosition(pistonPosition);
            };
        });
        return true;
    };
    
};
