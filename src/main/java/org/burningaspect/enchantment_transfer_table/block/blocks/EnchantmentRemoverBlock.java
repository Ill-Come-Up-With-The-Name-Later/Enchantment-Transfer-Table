package org.burningaspect.enchantment_transfer_table.block.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
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
import org.burningaspect.enchantment_transfer_table.block.blockentities.EnchantmentRemoverBlockEntity;
import org.jetbrains.annotations.Nullable;

public class EnchantmentRemoverBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final MapCodec<EnchantmentRemoverBlock> CODEC =
            EnchantmentRemoverBlock.createCodec(EnchantmentRemoverBlock::new);

    public EnchantmentRemoverBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(!state.isOf(newState.getBlock())) {
            BlockEntity entity = world.getBlockEntity(pos);

            if(entity instanceof EnchantmentRemoverBlockEntity) {
                ItemScatterer.spawn(world, pos, ((EnchantmentRemoverBlockEntity) entity));
                world.updateComparators(pos, this);

                EnchantmentTransferTable.LOGGER.debug("Enchantment Remover found at block position");
            }

            EnchantmentTransferTable.LOGGER.debug("Block state replaced for Enchantment Remover");
            //super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(!world.isClient) {
            if(world.getBlockEntity(pos) == null) {
                EnchantmentTransferTable.LOGGER.debug("No Enchantment Remover at pos");
                return ActionResult.FAIL;
            }

            if(world.getBlockEntity(pos) instanceof EnchantmentRemoverBlockEntity entity) {
                EnchantmentTransferTable.LOGGER.debug("Player has clicked an Enchantment Remover");

                player.openHandledScreen(entity);
            }
        }

        return ActionResult.success(world.isClient);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.REMOVER_ENTITY, EnchantmentRemoverBlockEntity::tick);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.REMOVER_ENTITY.instantiate(pos, state);
    }
}
