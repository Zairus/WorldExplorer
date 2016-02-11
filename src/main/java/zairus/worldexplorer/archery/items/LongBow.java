package zairus.worldexplorer.archery.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import zairus.worldexplorer.archery.entity.EntitySpecialArrow;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;

public class LongBow
	extends WEItemRanged
{
	public static final String[] bowPullIconNameArray = new String[] {"pulling_0", "pulling_1", "pulling_2"};
	
	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;
	@SideOnly(Side.CLIENT)
	private IIcon[] stringIconArray;
	@SideOnly(Side.CLIENT)
	private IIcon[] arrowIconArray;
	@SideOnly(Side.CLIENT)
	private IIcon stringIcon;
	
	public LongBow()
	{
		super();
		
		setUnlocalizedName("longbow");
		setTextureName(WEConstants.CORE_PREFIX + ":longbow_handle");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		setFull3D();
		setMaxStackSize(1);
		setMaxDamage(512);
		
		this.addAllowedAmmo(Items.arrow, WEArcheryItems.specialarrow);
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
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int useCount)
	{
		int j = this.getMaxItemUseDuration(stack) - useCount;
		
		ArrowLooseEvent event = new ArrowLooseEvent(player, stack, j);
		MinecraftForge.EVENT_BUS.post(event);
		
		this.setBowHasArrow(stack, false);
		
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
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		ArrowNockEvent event = new ArrowNockEvent(player, stack);
		MinecraftForge.EVENT_BUS.post(event);
		
		if (event.isCanceled())
		{
			return event.result;
		}
		
		ItemStack ammo = WEItemRanged.getAmmo(stack, player);
		
		if (player.capabilities.isCreativeMode || ammo != null)
		{
			world.playSoundAtEntity(
					player
					, "worldexplorer:longbow_draw_1"
					, 1.0F
					, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
			
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		}
		
		return stack;
    }
	
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 7200;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(this.getIconString() + "_standby");
		this.stringIcon = iconRegister.registerIcon("worldexplorer:longbow_string_standby");
		this.iconArray = new IIcon[bowPullIconNameArray.length];
		this.stringIconArray = new IIcon[bowPullIconNameArray.length];
		this.arrowIconArray = new IIcon[bowPullIconNameArray.length];
		
		for (int i = 0; i < this.iconArray.length; ++i)
		{
			this.iconArray[i] = iconRegister.registerIcon(this.getIconString() + "_" + bowPullIconNameArray[i]);
			this.stringIconArray[i] = iconRegister.registerIcon("worldexplorer:longbow_string_" + bowPullIconNameArray[i]);
			this.arrowIconArray[i] = iconRegister.registerIcon("worldexplorer:specialarrow");
		}
	}
	
	public IIcon getArrowIcon()
	{
		return this.arrowIconArray[0];
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int renderPass)
	{
		IIcon icon = (renderPass == 0)? itemIcon : stringIcon;
		
		return icon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
	{
		IIcon icon = (renderPass == 0)? itemIcon : stringIcon;
		IIcon[] passIcon = (renderPass == 0)? iconArray : stringIconArray;
		
		if (player.getItemInUse() == null)
			return icon;
		
		int pulling = stack.getMaxItemUseDuration() - useRemaining;
		
		if (pulling >= 20)
		{
			return passIcon[2];
		}
		else if (pulling > 10)
		{
			return passIcon[1];
		}
		else if (pulling > 3)
		{
			return passIcon[0];
		}
		
		return icon;
	}
}
