package org.burningaspect.enchantment_transfer_table.item.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NetherStarFragment extends Item {

    public NetherStarFragment(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
