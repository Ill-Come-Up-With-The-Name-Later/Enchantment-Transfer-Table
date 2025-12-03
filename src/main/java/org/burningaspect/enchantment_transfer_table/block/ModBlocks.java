package org.burningaspect.enchantment_transfer_table.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.burningaspect.enchantment_transfer_table.EnchantmentTransferTable;
import org.burningaspect.enchantment_transfer_table.block.blocks.EnchantmentRemoverBlock;
import org.burningaspect.enchantment_transfer_table.block.blocks.EnchantmentTransferTableBlock;

public class ModBlocks {

    public static final Block TABLE =
            registerBlockWithoutBlockItem("enchantment_transfer_table_block",
                    new EnchantmentTransferTableBlock(AbstractBlock.Settings.create()
                            .strength(10f, 600f).requiresTool()));

    public static final Block ENCHANT_REMOVER =
            registerBlockWithoutBlockItem("enchantment_remover_block",
                    new EnchantmentRemoverBlock(AbstractBlock.Settings.create()
                            .strength(8f, 400f).requiresTool()));

    private static Block registerBlockWithoutBlockItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(EnchantmentTransferTable.MOD_ID,
                name), block);
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(EnchantmentTransferTable.MOD_ID,
                name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(EnchantmentTransferTable.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        EnchantmentTransferTable.LOGGER.debug("Registering mod blocks");
    }
}
