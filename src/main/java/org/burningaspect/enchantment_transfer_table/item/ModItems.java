package org.burningaspect.enchantment_transfer_table.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.burningaspect.enchantment_transfer_table.EnchantmentTransferTable;
import org.burningaspect.enchantment_transfer_table.block.ModBlocks;
import org.burningaspect.enchantment_transfer_table.item.items.*;

public class ModItems {

    public static final Item ENCHANTMENT_TRANSFER_TABLE_ITEM = registerItem("enchantment_transfer_table",
            new BlockItem(ModBlocks.TABLE, new Item.Settings().rarity(Rarity.UNCOMMON)));

    public static final Item TRANSFER_CHARGE = registerItem("transfer_charge",
            new TransferCharge(new Item.Settings().rarity(Rarity.UNCOMMON)));

    public static final Item NETHER_STAR_FRAGMENT = registerItem("nether_star_fragment",
            new NetherStarFragment(new Item.Settings().rarity(Rarity.UNCOMMON)));

    public static final Item ENCHANTMENT_COPIER = registerItem("enchantment_copier",
            new EnchantmentCopier(new Item.Settings().rarity(Rarity.UNCOMMON)));

    public static final Item ITEM_PURIFIER = registerItem("item_purifier",
            new ItemPurifier(new Item.Settings().rarity(Rarity.UNCOMMON)));

    public static final Item ENCHANTMENT_REMOVER_ITEM = registerItem("enchantment_remover",
            new BlockItem(ModBlocks.ENCHANT_REMOVER, new Item.Settings().rarity(Rarity.UNCOMMON)));

    public static final Item GUIDED_LAUNCHER = registerItem("guided_launcher",
            new GuidedWeapon(new BowItem.Settings().maxDamage(100).maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(EnchantmentTransferTable.MOD_ID, name), item);
    }

    public static void registerModItems() {
        EnchantmentTransferTable.LOGGER.debug("Registering mod items");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(GUIDED_LAUNCHER);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(ENCHANTMENT_TRANSFER_TABLE_ITEM);
            entries.add(ENCHANTMENT_REMOVER_ITEM);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(TRANSFER_CHARGE);
            entries.add(NETHER_STAR_FRAGMENT);
            entries.add(ENCHANTMENT_COPIER);
            entries.add(ITEM_PURIFIER);
        });
    }
}
