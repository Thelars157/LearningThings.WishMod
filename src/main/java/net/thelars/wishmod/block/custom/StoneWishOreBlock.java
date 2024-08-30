package net.thelars.wishmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.thelars.wishmod.blockentity.StoneWishOreBlockEntity;

import javax.annotation.Nullable;

public class StoneWishOreBlock extends BaseEntityBlock {

    public StoneWishOreBlock(Properties properties) {
        super(properties.noOcclusion().dynamicShape());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StoneWishOreBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof StoneWishOreBlockEntity blockEntity) {
            return calculateShape(blockEntity);
        }
        return Shapes.block();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShape(state, world, pos, context);
    }

    private VoxelShape calculateShape(StoneWishOreBlockEntity blockEntity) {
        VoxelShape shape = Shapes.empty();
        for (Direction dir : Direction.values()) {
            int inset = blockEntity.getInset(dir);
            int extension = blockEntity.getExtension(dir);
            shape = Shapes.or(shape, getShapeForDirection(dir, inset, extension));
        }
        return shape;
    }

    private VoxelShape getShapeForDirection(Direction dir, int inset, int extension) {
        double insetAmount = inset / 16.0;
        double extensionAmount = extension / 16.0;
        return switch (dir) {
            case UP    -> Shapes.box(insetAmount, insetAmount, insetAmount, 1 - insetAmount, 1 - insetAmount + extensionAmount, 1 - insetAmount);
            case DOWN  -> Shapes.box(insetAmount, insetAmount - extensionAmount, insetAmount, 1 - insetAmount, 1 - insetAmount, 1 - insetAmount);
            case NORTH -> Shapes.box(insetAmount, insetAmount, insetAmount - extensionAmount, 1 - insetAmount, 1 - insetAmount, 1 - insetAmount);
            case SOUTH -> Shapes.box(insetAmount, insetAmount, insetAmount, 1 - insetAmount, 1 - insetAmount, 1 - insetAmount + extensionAmount);
            case WEST  -> Shapes.box(insetAmount - extensionAmount, insetAmount, insetAmount, 1 - insetAmount, 1 - insetAmount, 1 - insetAmount);
            case EAST  -> Shapes.box(insetAmount, insetAmount, insetAmount, 1 - insetAmount + extensionAmount, 1 - insetAmount, 1 - insetAmount);
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof StoneWishOreBlockEntity blockEntity)) return InteractionResult.PASS;

        ItemStack heldItem = player.getItemInHand(hand);
        Direction clickedFace = hit.getDirection();

        if (heldItem.isEmpty()) {
            blockEntity.resetToDefault();
        } else if (heldItem.getItem() == Items.WOODEN_SWORD) {
            blockEntity.decreaseInsetOnAdjacentFaces(clickedFace);
        } else if (heldItem.getItem() == Items.GOLDEN_SWORD) {
            blockEntity.increaseExtension(clickedFace);
        }

        world.sendBlockUpdated(pos, state, state, 3);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        if (world.isClientSide) return;

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof StoneWishOreBlockEntity blockEntity)) return;

        ItemStack heldItem = player.getMainHandItem();
        Direction clickedFace = getFacingFromHitVector(world, pos, player);

        if (heldItem.getItem() == Items.WOODEN_SWORD) {
            blockEntity.increaseInsetOnAdjacentFaces(clickedFace);
        } else if (heldItem.getItem() == Items.GOLDEN_SWORD) {
            blockEntity.decreaseExtension(clickedFace);
        }

        world.sendBlockUpdated(pos, state, state, 3);
    }

    private Direction getFacingFromHitVector(Level world, BlockPos pos, Player player) {
        BlockHitResult hitResult = (BlockHitResult) player.pick(20.0D, 0.0F, false);
        return hitResult.getDirection();
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof StoneWishOreBlockEntity) {
                // Handle any cleanup if necessary
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    // This method is key for piston movement
    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int id, int param) {
        super.triggerEvent(state, world, pos, id, param);
        BlockEntity blockentity = world.getBlockEntity(pos);
        return blockentity != null && blockentity.triggerEvent(id, param);
    }
}
