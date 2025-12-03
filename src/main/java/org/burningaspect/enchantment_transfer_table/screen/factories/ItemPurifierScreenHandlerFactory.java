package org.burningaspect.enchantment_transfer_table.screen.factories;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import org.burningaspect.enchantment_transfer_table.misc.ImplementedInventory;
import org.burningaspect.enchantment_transfer_table.network.BlockPosPayload;
import org.burningaspect.enchantment_transfer_table.screen.handler.ItemPurifierScreenHandler;
import org.jetbrains.annotations.Nullable;

public class ItemPurifierScreenHandlerFactory implements ExtendedScreenHandlerFactory<BlockPosPayload>,
        ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    public final Text TITLE = Text.literal("Item Purification");

    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new BlockPosPayload(player.getBlockPos());
    }

    @Override
    public Text getDisplayName() {
        return TITLE;
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ItemPurifierScreenHandler(syncId, playerInventory,
                ScreenHandlerContext.create(player.getWorld(), player.getBlockPos()));
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }
}
