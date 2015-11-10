package zairus.worldexplorer.archery.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import zairus.worldexplorer.core.WorldExplorer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LongBow
	extends WEItemRanged
{
	public static final String[] bowPullIconNameArray = new String[] {"pulling_0", "pulling_1", "pulling_2"};
	
	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;
	private IIcon[] stringIconArray;
	private IIcon[] arrowIconArray;
	private IIcon stringIcon;
	
	public boolean hasArrow = false;
	
	private int bowUseTick = 0;
	
	public LongBow()
	{
		super();
		
		setUnlocalizedName("longbow");
		setTextureName("worldexplorer:longbow_handle");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		setFull3D();
		setMaxStackSize(1);
		setMaxDamage(512);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int useCount)
	{
		int j = this.getMaxItemUseDuration(stack) - useCount;
		
		ArrowLooseEvent event = new ArrowLooseEvent(player, stack, j);
		MinecraftForge.EVENT_BUS.post(event);
		
		this.bowUseTick = 0;
		this.hasArrow = false;
		
		if (event.isCanceled())
		{
			return;
		}
		
		j = event.charge;
		
		boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;
		
		if (flag || player.inventory.hasItem(Items.arrow))
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
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		ArrowNockEvent event = new ArrowNockEvent(player, stack);
		MinecraftForge.EVENT_BUS.post(event);
		
		if (event.isCanceled())
		{
			return event.result;
		}
		
		if (player.capabilities.isCreativeMode || player.inventory.hasItem(Items.arrow))
		{
			world.playSoundAtEntity(
					player
					, "worldexplorer:longbow_draw_1"
					, 1.0F
					, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
			
			this.hasArrow = true;
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		}
		
		return stack;
    }
	
	public int getMaxItemUseDuration(ItemStack duration)
	{
		return 72000;
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
	
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
	{
		if (player.getItemInUse() == null)
			return itemIcon;
		
		int pulling = stack.getMaxItemUseDuration() - useRemaining;
		
		if (pulling >= 20)
		{
			return this.iconArray[2];
		}
		else if (pulling > 10)
		{
			return this.iconArray[1];
		}
		else if (pulling > 3)
		{
			return this.iconArray[0];
		}
		
		return itemIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromUseTick()
	{
		if (this.bowUseTick >= 20)
		{
			return this.iconArray[2];
		}
		if (this.bowUseTick > 10)
		{
			return this.iconArray[1];
		}
		if (this.bowUseTick > 3)
		{
			return this.iconArray[0];
		}
		
		return itemIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getStringIconFromUseTick()
	{
		if (this.bowUseTick >= 20)
		{
			return this.stringIconArray[2];
		}
		if (this.bowUseTick > 10)
		{
			return this.stringIconArray[1];
		}
		if (this.bowUseTick > 3)
		{
			return this.stringIconArray[0];
		}
		return stringIcon;
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
	{
		++this.bowUseTick;
	}
	
	public int getUseTick()
	{
		return this.bowUseTick;
	}
}
