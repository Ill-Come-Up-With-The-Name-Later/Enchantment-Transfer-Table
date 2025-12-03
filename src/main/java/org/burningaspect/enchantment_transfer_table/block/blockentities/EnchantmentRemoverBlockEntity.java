package org.burningaspect.enchantment_transfer_table.block.blockentities;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.burningaspect.enchantment_transfer_table.block.ModBlockEntities;
import org.burningaspect.enchantment_transfer_table.misc.ImplementedInventory;
import org.burningaspect.enchantment_transfer_table.network.BlockPosPayload;
import org.burningaspect.enchantment_transfer_table.screen.handler.EnchantmentRemoverScreenHandler;
import org.jetbrains.annotations.Nullable;

public class EnchantmentRemoverBlockEntity extends BlockEntity implements
        ExtendedScreenHandlerFactory<BlockPosPayload>, ImplementedInventory, SidedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private final Text uiTitle = Text.literal("Enchantment Removal");

    public EnchantmentRemoverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(ModBlockEntities.REMOVER_ENTITY, pos, state);
    }

    public EnchantmentRemoverBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.REMOVER_ENTITY, blockPos, blockState);
    }

    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new BlockPosPayload(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return uiTitle;
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new EnchantmentRemoverScreenHandler(syncId, playerInventory,
                ScreenHandlerContext.create(player.getWorld(), player.getBlockPos()));
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbt = super.toInitialChunkDataNbt(registryLookup);
        writeNbt(nbt, registryLookup);

        return nbt;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState,
                            EnchantmentRemoverBlockEntity enchantmentRemoverBlockEntity) {
    }
}
