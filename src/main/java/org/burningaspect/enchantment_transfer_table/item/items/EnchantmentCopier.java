package org.burningaspect.enchantment_transfer_table.item.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.burningaspect.enchantment_transfer_table.screen.factories.EnchantmentCopierScreenHandlerFactory;

import java.util.List;

public class EnchantmentCopier extends Item {
    public EnchantmentCopier(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.openHandledScreen(new EnchantmentCopierScreenHandlerFactory());
        user.getMainHandStack().decrement(1);

        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Clones enchantments from").formatted(Formatting.YELLOW));
        tooltip.add(Text.literal("items to a book.").formatted(Formatting.YELLOW));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
