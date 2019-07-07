/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.common.blocks.generic;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockPost extends BlockIEBase
{
	public BlockPost(String name, Properties blockProps)
	{
		super(name, blockProps, ItemBlockIEBase.class, IEProperties.MULTIBLOCKSLAVE);
		setNotNormalBlock();
		setMobility(PushReaction.BLOCK);
		lightOpacity = 0;
	}

	@Override
	public void getDrops(BlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune)
	{
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, BlockState newState, boolean moving)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileEntityPost&&!((TileEntityPost)tileEntity).isDummy())
			spawnAsEntity(world, pos, new ItemStack(this));
		super.onReplaced(state, world, pos, newState, moving);
	}

	@Override
	public boolean canIEBlockBePlaced(@Nonnull BlockState newState, BlockItemUseContext context)
	{
		BlockPos startingPos = context.getPos();
		World world = context.getWorld();
		for(int hh = 1; hh <= 3; hh++)
		{
			BlockPos pos = startingPos.up(hh);
			BlockItemUseContext dummyContext = new BlockItemUseContext(
					context.getWorld(), context.getPlayer(), context.getItem(), pos, context.getFace(),
					context.getHitX(), context.getHitY(), context.getHitZ()
			);
			if(World.isOutsideBuildHeight(pos)||
					!world.getBlockState(pos).getBlock().isReplaceable(newState, dummyContext))
				return false;
		}
		return true;
	}

	@Override
	public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileEntityPost();
	}
}