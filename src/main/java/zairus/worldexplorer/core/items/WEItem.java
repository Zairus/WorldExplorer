package zairus.worldexplorer.core.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class WEItem
	extends Item
{
	@Override
	public WEItem setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(name);
		return this;
	}
	
	@Override
	public WEItem setTextureName(String name)
	{
		this.iconString = name;
		return this;
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return super.getUnlocalizedName();
	}
	
	@Override
	public WEItem setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	
	@Override
	public WEItem setMaxStackSize(int size)
    {
        super.setMaxStackSize(size);
        return this;
    }
}
