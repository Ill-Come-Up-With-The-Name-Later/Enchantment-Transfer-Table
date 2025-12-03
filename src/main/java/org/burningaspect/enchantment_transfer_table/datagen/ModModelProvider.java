package org.burningaspect.enchantment_transfer_table.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import org.burningaspect.enchantment_transfer_table.block.ModBlocks;
import org.burningaspect.enchantment_transfer_table.item.ModItems;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerAnvil(ModBlocks.TABLE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.ENCHANTMENT_TRANSFER_TABLE_ITEM, Models.CUBE);
        itemModelGenerator.register(ModItems.NETHER_STAR_FRAGMENT, Models.GENERATED);
        itemModelGenerator.register(ModItems.TRANSFER_CHARGE, Models.GENERATED);
    }
}
