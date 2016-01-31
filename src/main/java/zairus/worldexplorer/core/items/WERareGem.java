package zairus.worldexplorer.core.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;

public class WERareGem
	extends WEItem
{
	public static final String[] gem_types = {
			"raregem_1"};
	
	private IIcon[] gemIcons;
	
	public WERareGem()
	{
		super();
		
		setUnlocalizedName("raregem");
		setTextureName(WEConstants.CORE_PREFIX + ":raregem_1");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		setHasSubtypes(true);
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
		int j = MathHelper.clamp_int(damage, 0, gem_types.length - 1);
		
		return gemIcons[j];
    }
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		int i = MathHelper.clamp_int(stack.getItemDamage(), 0, gem_types.length - 1);
		return super.getUnlocalizedName() + "." + gem_types[i];
	}
	
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, @SuppressWarnings("rawtypes") List list)
    {
		for (int i = 0; i < gem_types.length; ++i)
		{
			list.add(new ItemStack(item, 1, i));
		}
    }
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
		gemIcons = new IIcon[gem_types.length];
		
		for (int i = 0; i < gem_types.length; ++i)
		{
			this.gemIcons[i] = iconRegister.registerIcon(WEConstants.CORE_PREFIX + ":" + gem_types[i]);
		}
    }
}
