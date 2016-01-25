package zairus.worldexplorer.archery.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import zairus.worldexplorer.archery.entity.EntityDart;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;

/**
 * History
 * 20150115: Suggested by Tshallacka
 * */

public class BlowPipe
	extends WEItemRanged
{
	private int useTick = 0;
	
	public BlowPipe()
	{
		super();
		
		setUnlocalizedName("blowpipe");
		setTextureName(WEConstants.CORE_PREFIX + ":blowpipe");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		setFull3D();
		setMaxDamage(350);
		
		this.maxStackSize = 1;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (player.capabilities.isCreativeMode || player.inventory.hasItem(WEArcheryItems.dart))
		{
			world.playSoundAtEntity(
					player
					, "worldexplorer:blowpipe_breathe1"
					, 1.0F
					, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
			
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		}
		
		return stack;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int useCount)
	{
		int j = this.getMaxItemUseDuration(stack) - useCount;
		
		useTick = 0;
		
		ArrowLooseEvent event = new ArrowLooseEvent(player, stack, j);
		MinecraftForge.EVENT_BUS.post(event);
		
		if (event.isCanceled())
		{
			return;
		}
		
		j = event.charge;
		
		boolean flag = player.capabilities.isCreativeMode;
		
		if (flag || player.inventory.hasItem(WEArcheryItems.dart))
		{
			float f = (float)j / 7.0F;
			f = (f * f + f * 2.0F) / 3.0F;
			
			if ((double)f < 0.1D)
			{
				return;
			}
			
			if (f > 1.0F)
			{
				f = 1.0F;
			}
			
			EntityDart dart = new EntityDart(world, player, f * 1.7F);
			
			stack.damageItem(1, player);
			
			if (!world.isRemote)
				world.playSoundAtEntity(player, "worldexplorer:blowpipe_blow" + (itemRand.nextInt(3) + 1), 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
			
			if (!flag)
			{
				player.inventory.consumeInventoryItem(WEArcheryItems.dart);
			}
			
			if (!world.isRemote)
			{
				world.spawnEntityInWorld(dart);
			}
		}
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack duration)
	{
		return 3000;
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
	{
		++useTick;
	}
	
	public int getUseTick()
	{
		return useTick;
	}
}
