package org.burningaspect.enchantment_transfer_table.screen.handler;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.ForgingSlotsManager;
import org.burningaspect.enchantment_transfer_table.network.BlockPosPayload;
import org.burningaspect.enchantment_transfer_table.screen.ScreenHandlerInit;

public class EnchantmentRemoverScreenHandler extends ForgingScreenHandler {

    private ForgingSlotsManager manager;
    private Property levelCost = Property.create();
    private ScreenHandlerContext context;
    private Object2IntMap.Entry<RegistryEntry<Enchantment>> enchant = null;

    public EnchantmentRemoverScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlerInit.ENCHANT_REMOVER_SCREEN_HANDLER, syncId, inventory, context);

        this.addProperty(levelCost);
        this.context = context;
    }

    public EnchantmentRemoverScreenHandler(int i, PlayerInventory playerInventory, BlockPosPayload posPayload) {
        super(ScreenHandlerInit.ENCHANT_REMOVER_SCREEN_HANDLER, i, playerInventory, ScreenHandlerContext.EMPTY);

        this.addProperty(levelCost);
        this.context = ScreenHandlerContext.EMPTY;
    }

    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return (player.isInCreativeMode() || player.experienceLevel >=
                this.levelCost.get()) && this.levelCost.get() >= 0;
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        /*
        if(!this.input.getStack(1).isEmpty()) {
            this.input.setStack(1, new ItemStack(Items.AMETHYST_SHARD,
                    this.input.getStack(1).getCount() - 1));
        }

        this.setCursorStack(this.output.getStack(0));
        this.input.setStack(0, ItemStack.EMPTY);
        this.output.setStack(0, ItemStack.EMPTY);
        this.sendContentUpdates();
        this.levelCost.set(0);
         */
    }

    @Override
    protected boolean canUse(BlockState state) {
        return true;
    }

    @Override
    public void updateResult() {
        /*
        ItemStack item = this.input.getStack(0);
        ItemStack shard = this.input.getStack(1);
        ItemStack output = this.output.getStack(0);

        if(input.isEmpty() || shard.isEmpty()) {
            this.setLevelCost(0);
            this.output.setStack(0, ItemStack.EMPTY);
            this.sendContentUpdates();
        } else if(item.hasEnchantments() && !shard.isEmpty()) {
            if(!(enchant == null)) {
                if(player.experienceLevel >= this.getLevelCost().get() || player.isInCreativeMode()) {
                    if(!player.getAbilities().creativeMode) {
                        player.experienceLevel -= this.getLevelCost().get();
                    }

                    if(!output.isEmpty()) {
                        item = output;
                    }

                    this.output.setStack(0, EnchantmentHelperExpansion.removeEnchantment(item, enchant));

                    this.enchant = null;
                    this.setLevelCost(0);

                    this.sendContentUpdates();
                }
            } else {
                this.output.setStack(0, item);
                this.sendContentUpdates();
            }
        } else {
            this.setLevelCost(0);
            this.output.setStack(0, ItemStack.EMPTY);
            this.sendContentUpdates();
        }
         */
    }

    @Override
    protected ForgingSlotsManager getForgingSlotsManager() {
        return ForgingSlotsManager.create()
                .input(0, 15, 47, itemStack -> itemStack.hasEnchantments() &&
                        !itemStack.isOf(Items.ENCHANTED_BOOK))
                .input(1, 35, 47, itemStack -> itemStack.isOf(Items.AMETHYST_SHARD))
                .output(2, 24, 27)
                .build();
    }

    public Property getLevelCost() {
        return levelCost;
    }

    public void setLevelCost(int cost) {
        this.levelCost.set(cost);
    }

    public Object2IntMap.Entry<RegistryEntry<Enchantment>> getEnchant() {
        return enchant;
    }

    public void setEnchant(Object2IntMap.Entry<RegistryEntry<Enchantment>> enchant) {
        this.enchant = enchant;
    }

    /*
    public void updateItem(ItemStack item, Object2IntMap.Entry<RegistryEntry<Enchantment>> enchantment) {
        this.setLevelCost(enchantment.getIntValue() * 5);

        ItemStack shard = this.input.getStack(1);
        PlayerEntity player = this.player;

        if(player.experienceLevel >= this.getLevelCost().get()
                || this.player.getAbilities().creativeMode) {
            if(!shard.isEmpty()) {
                this.input.setStack(0, EnchantmentHelperExpansion.removeEnchantment(item, enchantment));
                this.input.getStack(1).decrement(1);

                this.output.setStack(0, this.input.getStack(0));

                if(!player.getAbilities().creativeMode) {
                    player.experienceLevel -= this.getLevelCost().get();
                }

                this.updateResult();
            }
        }
    }
     */
}
