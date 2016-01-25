package zairus.worldexplorer.archery.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.items.WEItem;

public class SpecialArrow
	extends WEItem
{
	public static final String[] arrow_types = new String[] {"sharpened_stick", "stone_arrow"};
	
	private IIcon[] arrowIcons;
	
	public SpecialArrow()
	{
		super();
		
		setUnlocalizedName("specialarrow");
		setTextureName(WEConstants.CORE_PREFIX + ":specialarrow");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		setHasSubtypes(true);
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
		int j = MathHelper.clamp_int(damage, 0, arrow_types.length - 1);;
		
		return arrowIcons[j];
    }
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		int i = MathHelper.clamp_int(stack.getItemDamage(), 0, arrow_types.length - 1);
		return super.getUnlocalizedName() + "." + arrow_types[i];
	}
	
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, @SuppressWarnings("rawtypes") List list)
    {
		for (int i = 0; i < arrow_types.length; ++i)
		{
			list.add(new ItemStack(item, 1, i));
		}
    }
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
		arrowIcons = new IIcon[arrow_types.length];
		
		for (int i = 0; i < arrow_types.length; ++i)
		{
			this.arrowIcons[i] = iconRegister.registerIcon(this.getIconString() + ((i > 0)? ("_" + arrow_types[i]):""));
		}
    }
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		DamageSource damagesource = DamageSource.causePlayerDamage(player);
		
		damagesource.damageType = "specialarrow";
		
		entity.attackEntityFrom(damagesource, 2.0F);
		entity.playSound("random.bowhit", 1.1F, 1.2F / (player.worldObj.rand.nextFloat() * 0.2F + 0.9F));
		
		if (!player.capabilities.isCreativeMode)
		{
			player.inventory.decrStackSize(player.inventory.currentItem, 1);
		}
		
		return true;
	}
}
