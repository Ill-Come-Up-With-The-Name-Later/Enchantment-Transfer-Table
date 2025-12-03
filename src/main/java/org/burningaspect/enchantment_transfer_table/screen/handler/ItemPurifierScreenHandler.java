package org.burningaspect.enchantment_transfer_table.screen.handler;

import net.minecraft.block.BlockState;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.ForgingSlotsManager;
import org.burningaspect.enchantment_transfer_table.network.BlockPosPayload;
import org.burningaspect.enchantment_transfer_table.screen.ScreenHandlerInit;

import java.util.concurrent.atomic.AtomicInteger;

public class ItemPurifierScreenHandler extends ForgingScreenHandler {

    private ForgingSlotsManager manager;
    private Property levelCost = Property.create();
    private ScreenHandlerContext context;

    public ItemPurifierScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlerInit.ITEM_PURIFICATION_SCREEN_HANDLER, syncId, inventory, context);

        this.addProperty(levelCost);
        this.context = context;
    }

    public ItemPurifierScreenHandler(int i, PlayerInventory playerInventory, BlockPosPayload posPayload) {
        super(ScreenHandlerInit.ITEM_PURIFICATION_SCREEN_HANDLER, i, playerInventory, ScreenHandlerContext.EMPTY);

        this.addProperty(levelCost);
        this.context = ScreenHandlerContext.EMPTY;
    }

    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return (player.isInCreativeMode() || player.experienceLevel >=
                this.levelCost.get()) && this.levelCost.get() > 0;
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        if(!player.getAbilities().creativeMode) {
            player.addExperienceLevels(-this.levelCost.get());
        }

        if(!this.input.getStack(1).isEmpty()) {
            this.input.setStack(1, new ItemStack(Items.DRAGON_BREATH,
                    this.input.getStack(1).getCount() - 1));
        }

        this.input.setStack(0, ItemStack.EMPTY);
        this.output.setStack(0, ItemStack.EMPTY);
        this.sendContentUpdates();
        this.levelCost.set(0);
    }

    @Override
    protected boolean canUse(BlockState state) {
        return true;
    }

    @Override
    public void updateResult() {
        ItemStack input = this.input.getStack(0);
        ItemStack breath = this.input.getStack(1);

        if(input.isEmpty() || breath.isEmpty()) {
            this.setLevelCost(0);
            this.output.setStack(0, ItemStack.EMPTY);
        } else if(input.hasEnchantments() && !breath.isEmpty()) {
            AtomicInteger cost = new AtomicInteger();

            ItemEnchantmentsComponent enchantments = EnchantmentHelper.getEnchantments(input);
            ItemStack purified = new ItemStack(input.getItem());

            cost.addAndGet(25);

            enchantments.getEnchantmentEntries().forEach((
                    enchantment) -> {
                if(!enchantment.getKey().isIn(EnchantmentTags.CURSE)) {
                    purified.addEnchantment(enchantment.getKey(), enchantment.getIntValue());
                }
            });

            this.levelCost.set(cost.get());

            if(!purified.hasEnchantments() && purified.isOf(Items.ENCHANTED_BOOK)) {
                this.output.setStack(0, new ItemStack(Items.BOOK));
            } else {
                this.output.setStack(0, purified);
            }

            this.sendContentUpdates();
        }
    }

    @Override
    protected ForgingSlotsManager getForgingSlotsManager() {
        return ForgingSlotsManager.create()
                .input(0, 49, 19, ItemStack::hasEnchantments)
                .input(1, 49, 40, itemStack -> itemStack.isOf(Items.DRAGON_BREATH))
                .output(2, 148, 34).build();
    }

    public Property getLevelCost() {
        return levelCost;
    }

    public void setLevelCost(int cost) {
        this.levelCost.set(cost);
    }
}
