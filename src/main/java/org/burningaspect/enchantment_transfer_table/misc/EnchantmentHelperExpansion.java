package org.burningaspect.enchantment_transfer_table.misc;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public class EnchantmentHelperExpansion {

    public static ItemStack removeEnchantment(ItemStack item, Object2IntMap.Entry<RegistryEntry<Enchantment>> enchant) {
        ItemEnchantmentsComponent enchantments = EnchantmentHelper.getEnchantments(item);
        ItemStack clone = new ItemStack(item.getItem());

        enchantments.getEnchantmentEntries().forEach((
                enchantment) -> {
            if(!enchantment.getKey().equals(enchant.getKey())) {
                clone.addEnchantment(enchantment.getKey(), enchantment.getIntValue());
            }
        });

        return clone;
    }
}
