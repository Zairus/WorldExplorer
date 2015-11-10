package zairus.worldexplorer.archery.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import zairus.worldexplorer.archery.entity.EntityBoomerang;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.items.WEItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Boomerang
	extends WEItem
{
	@SideOnly(Side.CLIENT)
	protected IIcon iconGunpowder;
	@SideOnly(Side.CLIENT)
	protected IIcon iconSlime;
	@SideOnly(Side.CLIENT)
	protected IIcon iconGlowstone;
	@SideOnly(Side.CLIENT)
	protected IIcon iconRedstone;
	@SideOnly(Side.CLIENT)
	protected IIcon iconEnder;
	
	public Boomerang ()
	{
		super();
		
		setMaxStackSize(1);
		setUnlocalizedName("boomerang");
		setTextureName("worldexplorer:boomerang");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		setFull3D();
		setMaxDamage(256);
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		stack.damageItem(1, player);
		
		EntityBoomerang entityBoomerang = new EntityBoomerang(world, player, 1.0F);
		
		float redstoneModifier = 0.0F;
		float capacityModifier = 0.0F;
		float powerModifier = 0.0F;
		float punchModifier = 0.0F;
		
		if (stack.getTagCompound() != null)
		{
			if (stack.getTagCompound().hasKey("Slime Modifier"))
				capacityModifier = stack.getTagCompound().getFloat("Slime Modifier");
			
			if (stack.getTagCompound().hasKey("Redstone Modifier"))
				redstoneModifier = stack.getTagCompound().getFloat("Redstone Modifier");
			
			if (stack.getTagCompound().hasKey("Glowstone Modifier"))
				powerModifier = stack.getTagCompound().getFloat("Glowstone Modifier");
			
			if (stack.getTagCompound().hasKey("Gunpowder Modifier"))
				punchModifier = stack.getTagCompound().getFloat("Gunpowder Modifier");
		}
		
		entityBoomerang.setReach(16.0D + redstoneModifier);
		entityBoomerang.setInventoryCapacity(1 + (int)capacityModifier);
		entityBoomerang.setDamage(entityBoomerang.getDamage() + powerModifier);
		entityBoomerang.setKnockbackStrength(Math.round(punchModifier));
		
		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
		{
			entityBoomerang.setFire(100);
		}
		
		if (!world.isRemote)
			world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
		
		entityBoomerang.setThrownBoomerang(player.inventory.getStackInSlot(player.inventory.currentItem).copy());
		
		if (player.capabilities.isCreativeMode)
			player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
		else
			stack.stackSize--;
		
		if (!world.isRemote)
		{
			world.spawnEntityInWorld(entityBoomerang);
		}
		
		return stack;
	}
	
	public int getItemEnchantability()
	{
		return 1;
	}
	
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.block;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_)
	{
		NBTTagCompound tag = stack.getTagCompound();
		
		if (tag != null)
		{
			if (tag.hasKey("Gunpowder Modifier"))
				list.add("Punch modifier +" + tag.getFloat("Gunpowder Modifier"));
			
			if (tag.hasKey("Slime Modifier"))
				list.add("Capacity modifier +" + tag.getFloat("Slime Modifier"));
			
			if (tag.hasKey("Glowstone Modifier"))
				list.add("Power modifier +" + tag.getFloat("Glowstone Modifier"));
			
			if (tag.hasKey("Redstone Modifier"))
				list.add("Reach modifier +" + tag.getFloat("Redstone Modifier"));
			
			if (tag.hasKey("Ender Modifier"))
				list.add("Special modifier +" + tag.getFloat("Ender Modifier"));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconregister)
	{
		this.itemIcon = iconregister.registerIcon(this.getIconString());
		
		this.iconGunpowder = iconregister.registerIcon(this.getIconString() + "_gunpowder");
		this.iconSlime = iconregister.registerIcon(this.getIconString() + "_slime");
		this.iconGlowstone = iconregister.registerIcon(this.getIconString() + "_glowstone");
		this.iconRedstone = iconregister.registerIcon(this.getIconString() + "_redstone");
		this.iconEnder= iconregister.registerIcon(this.getIconString() + "_ender");
	}
	
	public IIcon getModifierIconLayer(String layer)
	{
		IIcon iconLayer = null;
		
		switch(layer)
		{
		case "redstone":
			iconLayer = this.iconRedstone;
			break;
		case "glowstone":
			iconLayer = this.iconGlowstone;
			break;
		case "gunpowder":
			iconLayer = this.iconGunpowder;
			break;
		case "slime":
			iconLayer = this.iconSlime;
			break;
		case "ender":
			iconLayer = this.iconEnder;
			break;
		}
		
		return iconLayer;
	}
}
