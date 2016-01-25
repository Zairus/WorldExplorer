package zairus.worldexplorer.archery.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import zairus.worldexplorer.archery.entity.EntityPebble;
import zairus.worldexplorer.core.WorldExplorer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Slingshot
	extends WEItemRanged
{
	public static final String[] bowPullIconNameArray = new String[] {"pulling_0", "pulling_1", "pulling_2"};
	
	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;
	
	public Slingshot ()
	{
		super();
		
		setUnlocalizedName("slingshot");
		setTextureName("worldexplorer:slingshot");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		setFull3D();
		setMaxStackSize(1);
		setMaxDamage(192);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int useCount)
	{
		int j = this.getMaxItemUseDuration(stack) - useCount;
		
		ArrowLooseEvent event = new ArrowLooseEvent(player, stack, j);
		MinecraftForge.EVENT_BUS.post(event);
		
		if (event.isCanceled())
		{
			return;
		}
		
		j = event.charge;
		
		boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;
		
		if (flag || player.inventory.hasItem(WEArcheryItems.pebble))
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
			
			EntityPebble entityPebble = new EntityPebble(world, player, f * 1.0F);
			
			if (f == 1.0F)
			{
				entityPebble.setIsCritical(true);
			}
			
			int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
			
			if (k > 0)
			{
				entityPebble.setDamage(entityPebble.getDamage() + (double)k * 0.5D + 0.5D);
			}
			
			int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
			
			if (l > 0)
			{
				entityPebble.setKnockbackStrength(l);
			}
			
			stack.damageItem(1, player);
			
			if (!world.isRemote)
				world.playSoundAtEntity(player, "worldexplorer:slingshot_release_1", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
			
			if (flag)
			{
				entityPebble.canBePickedUp = 2;
			}
			else
			{
				player.inventory.consumeInventoryItem(WEArcheryItems.pebble);
			}
			
			if (!world.isRemote)
			{
				world.spawnEntityInWorld(entityPebble);
			}
		}
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		ArrowNockEvent event = new ArrowNockEvent(player, stack);
		MinecraftForge.EVENT_BUS.post(event);
		
		if (event.isCanceled())
		{
			return event.result;
		}
		
		if (player.capabilities.isCreativeMode || player.inventory.hasItem(WEArcheryItems.pebble))
		{
			world.playSoundAtEntity(
					player
					, "worldexplorer:slingshot_pull_1"
					, 1.0F
					, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
			
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		}
		
		return stack;
    }
	
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 1000;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(this.getIconString() + "_standby");
		this.iconArray = new IIcon[bowPullIconNameArray.length];
		
		for (int i = 0; i < this.iconArray.length; ++i)
		{
			this.iconArray[i] = iconRegister.registerIcon(this.getIconString() + "_" + bowPullIconNameArray[i]);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getItemIconForUseDuration(int duration)
	{
		return this.iconArray[duration];
	}
	
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
	{
		if (player.getItemInUse() == null)
			return itemIcon;
		
		int pulling = stack.getMaxItemUseDuration() - useRemaining;
		
		if (pulling >= 6)
		{
			return this.iconArray[2];
		}
		else if (pulling > 3)
		{
			return this.iconArray[1];
		}
		else if (pulling > 0)
		{
			return this.iconArray[0];
		}
		//return getIcon(stack, renderPass);
		return itemIcon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
	{
		//Archery.proxy.onBowFOV(stack, player, count);
	}
}
