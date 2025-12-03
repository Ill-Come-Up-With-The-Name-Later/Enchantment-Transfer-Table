package org.burningaspect.enchantment_transfer_table.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public class RemoveEnchantmentLevelLimit {

    @Inject(method = "updateResult", at = @At("TAIL"))
    private void removeLevelLimit(CallbackInfo ci) {
        AnvilScreenHandler handler = (AnvilScreenHandler) (Object) this;

        ItemStack itemStack = handler.getSlot(0).getStack();
        ItemStack itemStack2 = handler.getSlot(1).getStack();
        ItemStack itemStack3 = itemStack.copy();

        if(!itemStack.isOf(Items.ENCHANTED_BOOK) || !itemStack2.isOf(Items.ENCHANTED_BOOK)) {
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.
                    getEnchantments(itemStack));
            ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack2);

            for(Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                if(itemStack3.canBeEnchantedWith(entry.getKey(), EnchantingContext.ACCEPTABLE)) {
                    RegistryEntry<Enchantment> registryEntry = entry.getKey();

                    int firstLevel = builder.getLevel(registryEntry);
                    int secondLevel = entry.getIntValue();

                    secondLevel = firstLevel == secondLevel ? secondLevel + 1 : Math.max(secondLevel, firstLevel);

                    builder.set(registryEntry, secondLevel);
                    EnchantmentHelper.set(itemStack3, builder.build());
                    handler.getSlot(2).setStack(itemStack3);
                }
            }
        }
    }
}
