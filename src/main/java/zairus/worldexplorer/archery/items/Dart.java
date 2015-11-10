package zairus.worldexplorer.archery.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.items.WEItem;

/**
 * History
 * 20150115: BlowPipe Suggested by Tshallacka
 * */

public class Dart
	extends WEItem
{
	public static final String[] dart_types = new String[] {"dart"};
	
	private IIcon[] dartIcons;
	
	public Dart()
	{
		super();
		
		setUnlocalizedName("dart");
		setTextureName("worldexplorer:dart");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		setHasSubtypes(true);
		
		this.maxStackSize = 64;
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
		int j = MathHelper.clamp_int(damage, 0, dart_types.length - 1);;
		
		return dartIcons[j];
    }
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		int i = MathHelper.clamp_int(stack.getItemDamage(), 0, dart_types.length - 1);
		return super.getUnlocalizedName() + "." + dart_types[i];
	}
	
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, @SuppressWarnings("rawtypes") List list)
    {
		for (int i = 0; i < dart_types.length; ++i)
		{
			list.add(new ItemStack(item, 1, i));
		}
    }
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
		dartIcons = new IIcon[dart_types.length];
		
		for (int i = 0; i < dart_types.length; ++i)
		{
			this.dartIcons[i] = iconRegister.registerIcon(this.getIconString());
		}
    }
}
