package org.burningaspect.enchantment_transfer_table.item.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class TransferCharge extends Item {

    public TransferCharge(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Halves the cost of enchantment").formatted(Formatting.YELLOW));
        tooltip.add(Text.literal("transfers.").formatted(Formatting.YELLOW));
    }
}
