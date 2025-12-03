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

public class EnchantmentCopierScreenHandler extends ForgingScreenHandler {

    private ForgingSlotsManager manager;
    private Property levelCost = Property.create();
    private ScreenHandlerContext context;

    public static final int CURSE_MULTIPLIER = 11;
    public static final int TREASURE_MULTIPLIER = 8;
    public static final int NORMAL_MULTIPLIER = 6;

    public EnchantmentCopierScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlerInit.ENCHANT_COPIER_SCREEN_HANDLER, syncId, inventory, context);

        this.addProperty(levelCost);
        this.context = context;
    }

    public EnchantmentCopierScreenHandler(int i, PlayerInventory playerInventory, BlockPosPayload posPayload) {
        super(ScreenHandlerInit.ENCHANT_COPIER_SCREEN_HANDLER, i, playerInventory, ScreenHandlerContext.EMPTY);

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
            this.input.setStack(1, new ItemStack(Items.BOOK,
                    this.input.getStack(1).getCount() - 1));
        }

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
        ItemStack book = this.input.getStack(1);

        if(input.isEmpty() || book.isEmpty()) {
            this.setLevelCost(0);
            this.output.setStack(0, ItemStack.EMPTY);
        } else if(input.hasEnchantments() && !book.isEmpty()) {
            AtomicInteger cost = new AtomicInteger();

            ItemEnchantmentsComponent enchantments = EnchantmentHelper.getEnchantments(input);
            ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);

            cost.addAndGet(enchantments.getSize() + 12);

            enchantments.getEnchantmentEntries().forEach((
                    enchantment) -> {
                enchantedBook.addEnchantment(enchantment.getKey(), enchantment.getIntValue());

                cost.getAndIncrement();

                if(enchantment.getKey().isIn(EnchantmentTags.CURSE)) {
                    cost.addAndGet(CURSE_MULTIPLIER * enchantment.getIntValue());
                } else if(enchantment.getKey().isIn(EnchantmentTags.TREASURE)) {
                    cost.addAndGet(TREASURE_MULTIPLIER * enchantment.getIntValue());
                } else {
                    cost.addAndGet(NORMAL_MULTIPLIER * enchantment.getIntValue());
                }
            });

            int finalCost = (int) (Math.ceil(Math.pow(cost.intValue(), 3.0 / 4.0))) * 2;
            this.levelCost.set(finalCost);

            this.output.setStack(0, enchantedBook);
            this.sendContentUpdates();
        }
    }

    @Override
    protected ForgingSlotsManager getForgingSlotsManager() {
        return ForgingSlotsManager.create()
                .input(0, 49, 19, ItemStack::hasEnchantments)
                .input(1, 49, 40, itemStack -> itemStack.isOf(Items.BOOK))
                .output(2, 148, 34).build();
    }

    public Property getLevelCost() {
        return levelCost;
    }

    public void setLevelCost(int cost) {
        this.levelCost.set(cost);
    }
}
