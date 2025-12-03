package org.burningaspect.enchantment_transfer_table.block.blockentities;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
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
import org.burningaspect.enchantment_transfer_table.EnchantmentTransferTable;
import org.burningaspect.enchantment_transfer_table.block.ModBlockEntities;
import org.burningaspect.enchantment_transfer_table.misc.ImplementedInventory;
import org.burningaspect.enchantment_transfer_table.network.BlockPosPayload;
import org.burningaspect.enchantment_transfer_table.screen.handler.TransferTableScreenHandler;
import org.jetbrains.annotations.Nullable;

public class EnchantmentTransferTableBlockEntity extends BlockEntity implements
        ExtendedScreenHandlerFactory<BlockPosPayload>, ImplementedInventory, SidedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private final Text uiTitle = Text.literal("Enchantment Transferring");

    public EnchantmentTransferTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(ModBlockEntities.TABLE_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, EnchantmentTransferTableBlockEntity blockEntity)  {

    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    public EnchantmentTransferTableBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TABLE_ENTITY, blockPos, blockState);
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
        EnchantmentTransferTable.LOGGER.debug("Creating menu ScreenHandler for EnchantmentTransferTableBlockEntity");

        if(player == null) {
            EnchantmentTransferTable.LOGGER.error("Player is null");
        }

        if(playerInventory == null) {
            EnchantmentTransferTable.LOGGER.error("Player inventory is null");
        }

        return new TransferTableScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if(world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    private void update() {
        markDirty();
        if(world != null)
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
    }
}
