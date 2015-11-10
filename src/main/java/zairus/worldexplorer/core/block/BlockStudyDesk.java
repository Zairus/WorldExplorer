package zairus.worldexplorer.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.tileentity.TileEntityDesk;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStudyDesk
	extends BlockContainer
{
	private IIcon blockIconTop;
	private IIcon blockIconFront;
	private IIcon blockIconBack;
	
	private int renderType = 0;
	
	protected BlockStudyDesk(String unlocalizedName)
	{
		super(Material.wood);
		
		this.setBlockName(unlocalizedName);
		this.setCreativeTab(WorldExplorer.tabWorldExplorer);
		this.setBlockTextureName("worldexplorer:studydesk");
		this.setBlockName("studydesk");
		this.setStepSound(soundTypeWood);
		this.setHardness(1.5F);
		this.setResistance(5.0F);
		this.setHarvestLevel("axe", 0);
		
		this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
		
		/*
		BlockWorkbench;
		BlockChest;
		BlockEnderChest;
		
		ContainerWorkbench;
		CraftingManager;
		
		InventoryChest;
		TileEntityChest
		RenderBlocks;
		RenderStudyDesk;
		http://www.minecraftforge.net/forum/index.php?topic=4743.0
		*/
	}
	
	public void setRenderId(int id)
	{
		this.renderType = id;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return side == 1 ? this.blockIconTop : (side == 0 ? Blocks.planks.getBlockTextureFromSide(side) : (side == 4 ? this.blockIconBack : (side == 2 ? this.blockIconFront : this.blockIcon)));
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.blockIcon = iconRegister.registerIcon(this.getTextureName() + "_side");
		this.blockIconTop = iconRegister.registerIcon(this.getTextureName() + "_top");
		this.blockIconFront = iconRegister.registerIcon(this.getTextureName() + "_front");
		this.blockIconBack = iconRegister.registerIcon(this.getTextureName() + "_back");
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			player.openGui(WorldExplorer.instance, 0, world, x, y, z);
			return true;
		}
	}
	
	public boolean renderAsNormalBlock()
	{
		return false;
    }
	
	public int getRenderType()
	{
		return this.renderType;
    }
	
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	public boolean canProvidePower()
	{
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntityDesk();
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);
		func_149930_e(world, x, y, z);
	}
	
	private void func_149930_e(World p_149930_1_, int p_149930_2_, int p_149930_3_, int p_149930_4_)
	{
		if (!p_149930_1_.isRemote)
		{
			Block block = p_149930_1_.getBlock(p_149930_2_, p_149930_3_, p_149930_4_ - 1);
			Block block1 = p_149930_1_.getBlock(p_149930_2_, p_149930_3_, p_149930_4_ + 1);
			Block block2 = p_149930_1_.getBlock(p_149930_2_ - 1, p_149930_3_, p_149930_4_);
			Block block3 = p_149930_1_.getBlock(p_149930_2_ + 1, p_149930_3_, p_149930_4_);
			byte b0 = 3;
			
			if (block.func_149730_j() && !block1.func_149730_j())
			{
				b0 = 3;
			}
			
			if (block1.func_149730_j() && !block.func_149730_j())
			{
				b0 = 2;
			}
			
			if (block2.func_149730_j() && !block3.func_149730_j())
			{
				b0 = 5;
			}
			
			if (block3.func_149730_j() && !block2.func_149730_j())
			{
				b0 = 4;
			}
			
			p_149930_1_.setBlockMetadataWithNotify(p_149930_2_, p_149930_3_, p_149930_4_, b0, 3);
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		byte b0 = 0;
        int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        
        if (l == 0)
        {
            b0 = 2;
        }
        
        if (l == 1)
        {
            b0 = 5;
        }
        
        if (l == 2)
        {
            b0 = 3;
        }
        
        if (l == 3)
        {
            b0 = 4;
        }
        
        world.setBlockMetadataWithNotify(x, y, z, b0, 3);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int id)
	{
		TileEntityDesk tileEntityDesk = (TileEntityDesk)world.getTileEntity(x, y, z);
		
		for (int i = 0; i < tileEntityDesk.getSizeInventory(); ++i)
		{
			if (tileEntityDesk.getStackInSlot(i) != null)
				world.spawnEntityInWorld(new EntityItem(world, x, y, z, tileEntityDesk.getStackInSlot(i)));
		}
		
		super.breakBlock(world, x, y, z, block, id);
	}
}
