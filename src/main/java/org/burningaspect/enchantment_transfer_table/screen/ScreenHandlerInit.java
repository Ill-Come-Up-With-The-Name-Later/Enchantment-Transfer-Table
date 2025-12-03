package org.burningaspect.enchantment_transfer_table.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.burningaspect.enchantment_transfer_table.EnchantmentTransferTable;
import org.burningaspect.enchantment_transfer_table.network.BlockPosPayload;
import org.burningaspect.enchantment_transfer_table.screen.handler.EnchantmentCopierScreenHandler;
import org.burningaspect.enchantment_transfer_table.screen.handler.EnchantmentRemoverScreenHandler;
import org.burningaspect.enchantment_transfer_table.screen.handler.ItemPurifierScreenHandler;
import org.burningaspect.enchantment_transfer_table.screen.handler.TransferTableScreenHandler;

public class ScreenHandlerInit {

    public static ScreenHandlerType<TransferTableScreenHandler> TRANSFER_TABLE_SCREEN_HANDLER =
            register("enchantment_transferring",
                    TransferTableScreenHandler::new, BlockPosPayload.PACKET_CODEC);

    public static ScreenHandlerType<EnchantmentCopierScreenHandler> ENCHANT_COPIER_SCREEN_HANDLER =
            register("enchantment_copying",
                    EnchantmentCopierScreenHandler::new, BlockPosPayload.PACKET_CODEC);

    public static ScreenHandlerType<ItemPurifierScreenHandler> ITEM_PURIFICATION_SCREEN_HANDLER =
            register("item_purification",
                    ItemPurifierScreenHandler::new, BlockPosPayload.PACKET_CODEC);

    public static ScreenHandlerType<EnchantmentRemoverScreenHandler> ENCHANT_REMOVER_SCREEN_HANDLER =
            register("enchantment_remover", EnchantmentRemoverScreenHandler::new, BlockPosPayload.PACKET_CODEC);

    public static <T extends ScreenHandler, P extends CustomPayload> ExtendedScreenHandlerType<T, P>
    register(String name, ExtendedScreenHandlerType.ExtendedFactory<T, P> factory,
                                             PacketCodec<? super RegistryByteBuf, P> codec) {
        return Registry.register(Registries.SCREEN_HANDLER, EnchantmentTransferTable.id(name), new
                ExtendedScreenHandlerType<>(factory, codec));
    }

    public static void load() {
        EnchantmentTransferTable.LOGGER.debug("Registering ScreenHandler for Enchantment Transfer Table");
    }
}
