package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;

public class PetalsBlock extends Block
{
    public static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public PetalsBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(getDefaultState().with(AXIS, Direction.Axis.X));
    }

    @Override
    protected void appendProperties(StateContainer.Builder<Block, BlockState> builder)
    {
        super.appendProperties(builder);
        builder.add(AXIS);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(BlockItemUseContext context)
    {
        return getDefaultState().with(AXIS, context.getPlayerFacing().getAxis());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return !state.canPlaceAt(worldIn, currentPos)? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        BlockState floor = worldIn.getBlockState(pos.down());
        if (!ModUtils.equalsAny(floor.getBlock(), Blocks.ICE, Blocks.PACKED_ICE, Blocks.BARRIER, Blocks.HONEY_BLOCK, Blocks.SOUL_SAND))
                return Block.isFaceFullSquare(floor.getCollisionShape(worldIn, pos.down()), Direction.UP);
        return false;
    }

    @Override
    public boolean hasSidedTransparency(BlockState state)
    {
        return true;
    }
}
