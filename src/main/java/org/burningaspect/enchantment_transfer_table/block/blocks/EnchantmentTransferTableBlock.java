package org.burningaspect.enchantment_transfer_table.block.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.burningaspect.enchantment_transfer_table.EnchantmentTransferTable;
import org.burningaspect.enchantment_transfer_table.block.ModBlockEntities;
import org.burningaspect.enchantment_transfer_table.block.blockentities.EnchantmentTransferTableBlockEntity;
import org.jetbrains.annotations.Nullable;

public class EnchantmentTransferTableBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final MapCodec<EnchantmentTransferTableBlock> CODEC =
            EnchantmentTransferTableBlock.createCodec(EnchantmentTransferTableBlock::new);

    public EnchantmentTransferTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        EnchantmentTransferTable.LOGGER.debug("Created new Enchantment Transfer Table block entity");

        return ModBlockEntities.TABLE_ENTITY.instantiate(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(!state.isOf(newState.getBlock())) {
            BlockEntity entity = world.getBlockEntity(pos);

            if(entity instanceof EnchantmentTransferTableBlockEntity) {
                ItemScatterer.spawn(world, pos, ((EnchantmentTransferTableBlockEntity) entity));
                world.updateComparators(pos, this);

                EnchantmentTransferTable.LOGGER.debug("Enchantment Transfer Table found at block position");
            }

            EnchantmentTransferTable.LOGGER.debug("Block state replaced for Enchantment Transfer Table");
            //super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(!world.isClient) {
            if(world.getBlockEntity(pos) == null) {
                EnchantmentTransferTable.LOGGER.debug("No Enchantment Transfer Table at pos");
                return ActionResult.FAIL;
            }

            if(world.getBlockEntity(pos) instanceof EnchantmentTransferTableBlockEntity entity) {
                EnchantmentTransferTable.LOGGER.debug("Player has clicked an Enchantment Transfer table");

                player.openHandledScreen(entity);
            }
        }

        return ActionResult.success(world.isClient);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.TABLE_ENTITY, EnchantmentTransferTableBlockEntity::tick);
    }

    /*
    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos,
                                             PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if(be instanceof EnchantmentTransferTableBlockEntity table) {
                ItemStack heldItem = player.getStackInHand(hand);

                if(heldItem.isEmpty()) {
                    clearTable(world, pos, table);
                    return ItemActionResult.SUCCESS;
                }

                if(!(heldItem.hasEnchantments() || heldItem.isOf(Items.BOOK))) {
                    player.sendMessage(Text.literal("Cannot add item"), true);
                    return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
                }

                if(heldItem.hasEnchantments() && !table.getStack(0).isEmpty()) {
                    player.sendMessage(Text.literal("Item already added"), true);
                    return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
                }

                if(heldItem.isOf(Items.BOOK) && heldItem.getCount() > 1) {
                    player.sendMessage(Text.literal("Only one book can be added"), true);
                    return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
                }

                if(heldItem.isOf(Items.BOOK) && !table.getStack(1).isEmpty()) {
                    player.sendMessage(Text.literal("Book already added"), true);
                    return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
                }

                if(table.getStack(0).isEmpty() && heldItem.hasEnchantments()) {
                    table.setStack(0, heldItem.copy());
                    player.setStackInHand(hand, ItemStack.EMPTY);
                    player.sendMessage(Text.literal("Enchanted item added"), true);
                } else if(table.getStack(1).isEmpty() && heldItem.isOf(Items.BOOK)) {
                    table.setStack(1, heldItem.copy());
                    player.setStackInHand(hand, ItemStack.EMPTY);
                    player.sendMessage(Text.literal("Book added"), true);
                } else {
                    player.sendMessage(Text.literal("Already contains items"), true);
                }

                if(heldItem.isEmpty()) {
                    if(table.getStack(0).isEmpty() || table.getStack(1).isEmpty()) {
                        clearTable(world, pos, table);
                    }
                } else {
                    AtomicInteger cost = new AtomicInteger();

                    ItemEnchantmentsComponent enchantments = EnchantmentHelper.getEnchantments(table.getStack(0));
                    ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);

                    cost.addAndGet(enchantments.getSize() + 10);

                    enchantments.getEnchantmentEntries().forEach((
                            enchantment) -> {
                        enchantedBook.addEnchantment(enchantment.getKey(), enchantment.getIntValue());

                        if(!enchantment.getKey().isIn(EnchantmentTags.CURSE)) {
                            cost.addAndGet(3 * enchantment.getIntValue());
                        }
                    });

                    int finalCost = (int) Math.ceil(Math.pow(cost.intValue(), 2.0 / 3.0));

                    if(player.experienceLevel >= finalCost || player.isCreative()) {
                        player.experienceLevel -= finalCost;
                        ItemEntity book = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), enchantedBook);
                        world.spawnEntity(book);

                        ItemStack item = table.getStack(0);

                        ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.apply(
                                item, components -> components.remove(enchantment ->
                                        !enchantment.isIn(EnchantmentTags.CURSE)));

                        clearTable(world, pos, table);
                    } else {
                        clearTable(world, pos, table);
                        player.sendMessage(Text.literal("You need " + finalCost + " levels to transfer enchantments."));
                    }
                }
            }
        }

        return ItemActionResult.SUCCESS;
    }

    private void clearTable(World world, BlockPos pos, EnchantmentTransferTableBlockEntity table) {
        for(int i = 0; i < table.size(); i++) {
            ItemEntity itemEnt = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), table.getStack(i));
            table.removeStack(i);
        }
    }
     */
}
