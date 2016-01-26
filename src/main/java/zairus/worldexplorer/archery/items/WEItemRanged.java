package zairus.worldexplorer.archery.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zairus.worldexplorer.core.items.WEItem;

public class WEItemRanged
	extends WEItem
{
	protected List<Item> allowedAmmo = new ArrayList<Item>();
	
	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		return stack;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.bow;
	}
	
	@Override
	public int getItemEnchantability()
	{
		return 1;
	}
	
	protected void addAllowedAmmo(Item... ammo)
	{
		for (int i = 0; i < ammo.length; ++i)
		{
			this.allowedAmmo.add(ammo[i]);
		}
	}
	
	public List<Item> getAllowedAmmo()
	{
		return this.allowedAmmo;
	}
	
	public static ItemStack getAmmo(ItemStack stack, EntityPlayer player)
	{
		ItemStack ammo = null;
		
		invLoop:
		for (int i = 0; i < 36; ++i)
		{
			if(
					player.inventory.getStackInSlot(i) != null
					&& ((WEItemRanged)stack.getItem()).getAllowedAmmo().contains(player.inventory.getStackInSlot(i).getItem()))
			{
				ammo = player.inventory.getStackInSlot(i);
				break invLoop;
			}
		}
		
		return ammo;
	}
}
