package org.burningaspect.enchantment_transfer_table.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import org.burningaspect.enchantment_transfer_table.block.ModBlocks;
import org.burningaspect.enchantment_transfer_table.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLangProvider extends FabricLanguageProvider {

    public ModEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.ENCHANTMENT_TRANSFER_TABLE_ITEM, "Enchantment Transfer Table");
        translationBuilder.add(ModBlocks.TABLE, "Enchantment Transfer Table");
        translationBuilder.add(ModItems.NETHER_STAR_FRAGMENT, "Nether Star Fragment");
        translationBuilder.add(ModItems.TRANSFER_CHARGE, "Transfer Charge");
    }
}
