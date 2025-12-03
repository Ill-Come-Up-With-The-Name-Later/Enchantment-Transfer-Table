package org.burningaspect.enchantment_transfer_table.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.burningaspect.enchantment_transfer_table.client.screen.EnchantmentCopierScreen;
import org.burningaspect.enchantment_transfer_table.client.screen.EnchantmentRemoverScreen;
import org.burningaspect.enchantment_transfer_table.client.screen.EnchantmentTransferScreen;
import org.burningaspect.enchantment_transfer_table.client.screen.ItemPurificationScreen;
import org.burningaspect.enchantment_transfer_table.screen.ScreenHandlerInit;

public class EnchantmentTransferTableClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlerInit.TRANSFER_TABLE_SCREEN_HANDLER,
                EnchantmentTransferScreen::new);

        HandledScreens.register(ScreenHandlerInit.ENCHANT_COPIER_SCREEN_HANDLER,
                EnchantmentCopierScreen::new);

        HandledScreens.register(ScreenHandlerInit.ITEM_PURIFICATION_SCREEN_HANDLER,
                ItemPurificationScreen::new);

        HandledScreens.register(ScreenHandlerInit.ENCHANT_REMOVER_SCREEN_HANDLER,
                EnchantmentRemoverScreen::new);
    }
}
