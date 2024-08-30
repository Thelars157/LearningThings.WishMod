/*
package net.thelars.wishmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FlowControlBlock extends Block {

    public static final BlockStateProperties.FACING FACING = BlockStateProperties.FACING;
    public static final IntegerProperty SIZE = IntegerProperty.create("size", 0, 84);
    public static final BooleanProperty NORTH_FLOW = BooleanProperty.create("north_flow");
    public static final BooleanProperty SOUTH_FLOW = BooleanProperty.create("south_flow");
    public static final BooleanProperty EAST_FLOW = BooleanProperty.create("east_flow");
    public static final BooleanProperty WEST_FLOW = BooleanProperty.create("west_flow");
    public static final BooleanProperty UP_FLOW = BooleanProperty.create("up_flow");
    public static final BooleanProperty DOWN_FLOW = BooleanProperty.create("down_flow");

    public FlowControlBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SIZE, 0)
                .setValue(NORTH_FLOW, true)
                .setValue(SOUTH_FLOW, true)
                .setValue(EAST_FLOW, true)
                .setValue(WEST_FLOW, true)
                .setValue(UP_FLOW, true)
                .setValue(DOWN_FLOW, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SIZE, NORTH_FLOW, SOUTH_FLOW, EAST_FLOW, WEST_FLOW, UP_FLOW, DOWN_FLOW);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        switch (facing) {
            case NORTH:
                return Shapes.or(Shapes.block(), Shapes.box(0, 0, 12/16.0, 1, 1, 1)); // Gap on north side
            case SOUTH:
                return Shapes.or(Shapes.block(), Shapes.box(0, 0, 0, 1, 1, 4/16.0)); // Gap on south side
            case EAST:
                return Shapes.or(Shapes.block(), Shapes.box(12/16.0, 0, 0, 1, 1, 1)); // Gap on east side
            case WEST:
                return Shapes.or(Shapes.block(), Shapes.box(0, 0, 0, 4/16.0, 1, 1)); // Gap on west side
            case UP:
                return Shapes.or(Shapes.block(), Shapes.box(0, 12/16.0, 0, 1, 1, 1)); // Gap on top
            case DOWN:
                return Shapes.or(Shapes.block(), Shapes.box(0, 0, 0, 1, 4/16.0, 1)); // Gap on bottom
            default:
                return Shapes.block(); // Solid wall, no gaps
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShape(state, world, pos, context);
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.getItem() == Items.SLIME_BALL || heldItem.getItem() == Items.ARROW) {
            BlockHitResult hitResult = (BlockHitResult) player.pick(20.0D, 0.0F, false);
            Direction direction = hitResult.getDirection();

            // Update the flow property for the clicked direction
            world.setBlock(pos, state.setValue(getFlowProperty(direction), !state.getValue(getFlowProperty(direction))), 3);
        }
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        // Check if any fluid is present at adjacent blocks
        checkAndUpdateFlow(world, pos, state, Direction.NORTH);
        checkAndUpdateFlow(world, pos, state, Direction.SOUTH);
        checkAndUpdateFlow(world, pos, state, Direction.EAST);
        checkAndUpdateFlow(world, pos, state, Direction.WEST);
        checkAndUpdateFlow(world, pos, state, Direction.UP);
        checkAndUpdateFlow(world, pos, state, Direction.DOWN);
    }

    private void checkAndUpdateFlow(Level world, BlockPos pos, BlockState state, Direction direction) {
        BlockPos neighborPos = pos.relative(direction);
        FluidState fluidState = world.getFluidState(neighborPos);
        Fluid fluid = fluidState.getType();
        // Check if the fluid is not empty and the corresponding flow property is true
        if (!fluid.isEmpty() && state.getValue(getFlowProperty(direction))) {
            // If fluid is present and flow is allowed, update the fluid state at the neighbor block
            world.setBlock(neighborPos, fluidState.createLegacyBlock(), 3);
        }
    }

    // Helper method to get the appropriate flow property for a direction
    private BooleanProperty getFlowProperty(Direction direction) {
        switch (direction) {
            case NORTH:
                return NORTH_FLOW;
            case SOUTH:
                return SOUTH_FLOW;
            case EAST:
                return EAST_FLOW;
            case WEST:
                return WEST_FLOW;
            case UP:
                return UP_FLOW;
            case DOWN:
                return DOWN_FLOW;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    private int calculateOverallSize(BlockState state) {
        // Calculate overall size based on face properties
        // Example: sum the absolute values of face extensions
        int size = Math.abs(state.getValue(NORTH)) + Math.abs(state.getValue(SOUTH)) +
                Math.abs(state.getValue(EAST)) + Math.abs(state.getValue(WEST)) +
                Math.abs(state.getValue(UP)) + Math.abs(state.getValue(DOWN));
        return size;
    }
}    */