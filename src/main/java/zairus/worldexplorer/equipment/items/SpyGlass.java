package zairus.worldexplorer.equipment.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.gui.IGuiOverlay;
import zairus.worldexplorer.core.items.WEItem;
import zairus.worldexplorer.equipment.gui.GuiScreenSpyGlass;

public class SpyGlass
	extends WEItem
{
	public SpyGlass()
	{
		super();
		
		setUnlocalizedName("spyglass");
		setTextureName(WEConstants.CORE_PREFIX + ":spyglass");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		setFull3D();
		setMaxDamage(3000);
		
		this.maxStackSize = 1;
	}
	
	@Override
	public boolean updatesFOV()
	{
		return true;
	}
	
	@Override
	public float getFOVValue()
	{
		return 6.5f;
	}
	
	@Override
	public float getFOVSpeedFactor()
	{
		return 1500.0f;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IGuiOverlay getUseOverlay()
	{
		return new GuiScreenSpyGlass();
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
			world.playSoundAtEntity(player, WEConstants.CORE_PREFIX + ":spyglass_look", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1.5F);
		
		player.setItemInUse(stack, getMaxItemUseDuration(stack));
		
		return stack;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int useCount)
	{
		if (!world.isRemote)
			world.playSoundAtEntity(player, WEConstants.CORE_PREFIX + ":spyglass_close", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1.5F);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack duration)
	{
		return 10000;
	}
	
	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		return stack;
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
}
