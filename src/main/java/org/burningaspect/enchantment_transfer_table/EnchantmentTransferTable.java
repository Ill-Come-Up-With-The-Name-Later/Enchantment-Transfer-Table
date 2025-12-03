package org.burningaspect.enchantment_transfer_table;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.burningaspect.enchantment_transfer_table.block.ModBlockEntities;
import org.burningaspect.enchantment_transfer_table.block.ModBlocks;
import org.burningaspect.enchantment_transfer_table.command.CommandRegister;
import org.burningaspect.enchantment_transfer_table.item.ModItems;
import org.burningaspect.enchantment_transfer_table.screen.ScreenHandlerInit;
import org.burningaspect.enchantment_transfer_table.whitelist.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantmentTransferTable implements ModInitializer {

    public static final String MOD_ID = "ench_transfer_table";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModBlocks.registerModBlocks();
        ModBlockEntities.initialize();
        ModItems.registerModItems();
        ScreenHandlerInit.load();
        CommandRegister.init();

        ServerTickEvents.START_SERVER_TICK.register(Whitelist::removeBlacklistedUsers);
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
