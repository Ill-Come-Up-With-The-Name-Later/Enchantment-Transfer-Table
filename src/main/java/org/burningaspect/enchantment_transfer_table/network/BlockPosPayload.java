package org.burningaspect.enchantment_transfer_table.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import org.burningaspect.enchantment_transfer_table.EnchantmentTransferTable;

public record BlockPosPayload(BlockPos pos) implements CustomPayload {

    public static final Id<BlockPosPayload> ID = new Id<>(EnchantmentTransferTable.id("block_pos"));
    public static final PacketCodec<RegistryByteBuf, BlockPosPayload> PACKET_CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, BlockPosPayload::pos, BlockPosPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
