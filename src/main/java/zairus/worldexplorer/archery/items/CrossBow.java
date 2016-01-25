package zairus.worldexplorer.archery.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;

public class CrossBow
	extends WEItemRanged
{
	private int useTick = 0;
	
	public CrossBow()
	{
		super();
		
		setUnlocalizedName("crossbow");
		setTextureName(WEConstants.CORE_PREFIX + ":crossbow");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		
		this.maxStackSize = 1;
		
		this.bFull3D = true;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		ArrowNockEvent event = new ArrowNockEvent(player, stack);
		MinecraftForge.EVENT_BUS.post(event);
		
		if (event.isCanceled())
		{
			return event.result;
		}
		
		if (player.capabilities.isCreativeMode || player.inventory.hasItem(WEArcheryItems.specialarrow) || player.inventory.hasItem(Items.arrow))
		{
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		}
		
		return stack;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int useCount)
	{
		int j = this.getMaxItemUseDuration(stack) - useCount;
		
		ArrowLooseEvent event = new ArrowLooseEvent(player, stack, j);
		MinecraftForge.EVENT_BUS.post(event);
		
		useTick = 0;
		
		if (event.isCanceled())
		{
			return;
		}
		
		j = event.charge;
		
		boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;
		
		if (flag || player.inventory.hasItem(Items.arrow) || player.inventory.hasItem(WEArcheryItems.specialarrow))
		{
			float f = (float)j / 25.0F;
			f = (f * f + f * 2.0F) / 3.0F;
			
			if ((double)f < 0.1D)
			{
				return;
			}
			
			if (f > 1.0F)
			{
				f = 1.0F;
			}
			
			EntityArrow entityArrow = new EntityArrow(world, player, f * 2.5f);
			
			if (f == 1.0F)
			{
				entityArrow.setIsCritical(true);
			}
			
			int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
			
			if (k > 0)
			{
				entityArrow.setDamage(entityArrow.getDamage() + (double)k * 0.5D + 0.5D);
			}
			
			int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
			
			if (l > 0)
			{
				entityArrow.setKnockbackStrength(l);
			}
			
			if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
			{
				entityArrow.setFire(100);
			}
			
			stack.damageItem(1, player);
			
			if (!world.isRemote)
				world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
			
			if (flag)
			{
				entityArrow.canBePickedUp = 2;
			}
			else
			{
				player.inventory.consumeInventoryItem(Items.arrow);
			}
			
			if (!world.isRemote)
			{
				world.spawnEntityInWorld(entityArrow);
			}
		}
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack duration)
	{
		return 7200;
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
	
	public float getStagePercent(ItemStack stack)
	{
		float percent = 0.0F;
		float maxTime = 70;
		
		if (useTick < maxTime)
		{
			percent = (float)useTick / maxTime;
		}
		else
		{
			percent = 1.0F;
		}
		
		return percent;
	}
}
