package zairus.worldexplorer.archery.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import zairus.worldexplorer.archery.entity.EntitySpecialArrow;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;

public class CrossBow
	extends WEItemRanged
{
	public CrossBow()
	{
		super();
		
		setUnlocalizedName("crossbow");
		setTextureName(WEConstants.CORE_PREFIX + ":crossbow");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		
		this.maxStackSize = 1;
		
		this.bFull3D = true;
		
		this.addAllowedAmmo(Items.arrow, WEArcheryItems.specialarrow);
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
	{
		if (count == 2275)
		{
			player.playSound(WEConstants.CORE_PREFIX + ":crossbow_charge", 1.0F, 1.0F);
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		boolean hasArrow = this.getBowHasAwwor(stack);
		
		player.playSound(WEConstants.CORE_PREFIX + ":crossbow_handle", 1.0F, 1.0F);
		
		if (!hasArrow)
		{
			player.playSound(WEConstants.CORE_PREFIX + ":crossbow_pull", 1.0F, 1.0F);
			arrowCharge(stack, player);
		}
		else
		{
			player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		}
		
		return stack;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int useCount)
	{
		boolean hasArrow = this.getBowHasAwwor(stack);
		
		if (hasArrow)
		{
			shootAmmo(stack, world, player);
		}
		
		if (!hasArrow && useCount <= 2275)
		{
			this.setBowHasArrow(stack, true);
		}
	}
	
	private void arrowCharge(ItemStack stack, EntityPlayer player)
	{
		ArrowNockEvent event = new ArrowNockEvent(player, stack);
		MinecraftForge.EVENT_BUS.post(event);
		
		if (event.isCanceled())
		{
			return;
		}
		
		ItemStack ammo = WEItemRanged.getAmmo(stack, player);
		
		if (player.capabilities.isCreativeMode || ammo != null)
		{
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		}
	}
	
	private void initStackTag(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
	}
	
	private void setBowHasArrow(ItemStack stack, boolean hasArrow)
	{
		this.initStackTag(stack);
		
		stack.getTagCompound().setBoolean("hasArrow", hasArrow);
	}
	
	public boolean getBowHasAwwor(ItemStack stack)
	{
		boolean hasArrow = false;
		
		if (stack.hasTagCompound())
		{
			if (stack.getTagCompound().hasKey("hasArrow"))
			{
				hasArrow = stack.getTagCompound().getBoolean("hasArrow");
			}
		}
		
		return hasArrow;
	}
	
	private void shootAmmo(ItemStack stack, World world, EntityPlayer player)
	{
		int j = this.getMaxItemUseDuration(stack);
		
		ArrowLooseEvent event = new ArrowLooseEvent(player, stack, j);
		MinecraftForge.EVENT_BUS.post(event);
		
		if (event.isCanceled())
		{
			return;
		}
		
		j = event.charge;
		
		boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;
		boolean hasArrow = false;
		
		for (int i = 0; i < getAllowedAmmo().size(); ++i)
		{
			if(player.inventory.hasItem(getAllowedAmmo().get(i)))
				hasArrow = true;
		}
		
		if (flag || hasArrow)
		{
			ItemStack ammo = WEItemRanged.getAmmo(stack, player);
			
			if (player.capabilities.isCreativeMode && ammo == null)
				ammo = new ItemStack(WEArcheryItems.specialarrow, 1, 1);
			
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
			
			Entity projectile = getEntityToShoot(stack, ammo, f, world, player, !flag);
			
			stack.damageItem(1, player);
			
			if (!world.isRemote)
				world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
			
			if (!flag)
			{
				if(player.inventory.hasItem(ammo.getItem()))
					player.inventory.consumeInventoryItem(ammo.getItem());
			}
			
			if (!world.isRemote)
			{
				world.spawnEntityInWorld(projectile);
			}
		}
		
		this.setBowHasArrow(stack, false);
	}
	
	private Entity getEntityToShoot(ItemStack stack, ItemStack ammo, float pull, World world, EntityPlayer player, boolean canBePickedUp)
	{
		EntityArrow arrow = null;
		EntitySpecialArrow specialArrow = null;
		
		int power = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
		int punch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
		int flame = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
		
		if (ammo.getItem() == WEArcheryItems.specialarrow)
		{
			specialArrow = new EntitySpecialArrow(world, player, pull * 2.5f, ammo);
			
			if (pull == 1.0f)
				specialArrow.setIsCritical(true);
			
			if (power > 0)
				specialArrow.setDamage(specialArrow.getDamage() + (double)power * 0.5D + 0.5D);
			
			if (punch > 0)
				specialArrow.setKnockbackStrength(punch);
			
			if (flame > 0)
				specialArrow.setFire(100);
			
			if (!canBePickedUp)
				specialArrow.canBePickedUp = 2;
		}
		else
		{
			arrow = new EntityArrow(world, player, pull * 2.5f);
			
			if (pull == 1.0f)
				arrow.setIsCritical(true);
			
			if (power > 0)
				arrow.setDamage(arrow.getDamage() + (double)power * 0.5D + 0.5D);
			
			if (punch > 0)
				arrow.setKnockbackStrength(punch);
			
			if (flame > 0)
				arrow.setFire(100);
			
			if (!canBePickedUp)
				arrow.canBePickedUp = 2;
		}
		
		if (specialArrow != null)
			return (Entity)specialArrow;
		else
			return (Entity)arrow;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		int duration = 2300;
		boolean hasArrow = this.getBowHasAwwor(stack);
		
		if (hasArrow)
			duration = 1000;
		
		return duration;
	}
}
