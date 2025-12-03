package org.burningaspect.enchantment_transfer_table.block;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.burningaspect.enchantment_transfer_table.EnchantmentTransferTable;
import org.burningaspect.enchantment_transfer_table.block.blockentities.EnchantmentRemoverBlockEntity;
import org.burningaspect.enchantment_transfer_table.block.blockentities.EnchantmentTransferTableBlockEntity;

public class ModBlockEntities {

    public static BlockEntityType<EnchantmentTransferTableBlockEntity> TABLE_ENTITY =
            register("table_entity", BlockEntityType.Builder.create(EnchantmentTransferTableBlockEntity::new,
                    ModBlocks.TABLE).build());

    public static BlockEntityType<EnchantmentRemoverBlockEntity> REMOVER_ENTITY =
            register("remover_entity", BlockEntityType.Builder.create(
                    EnchantmentRemoverBlockEntity::new, ModBlocks.ENCHANT_REMOVER).build());

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(EnchantmentTransferTable.MOD_ID,
                path), blockEntityType);
    }

    public static void initialize() {
        EnchantmentTransferTable.LOGGER.debug("Registering block entities");
    }
}
