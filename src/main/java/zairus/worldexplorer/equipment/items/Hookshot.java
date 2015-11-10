package zairus.worldexplorer.equipment.items;

import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.items.WEItem;

public class Hookshot
	extends WEItem
{
	public Hookshot()
	{
		super();
		
		setUnlocalizedName("hookshot");
		setTextureName("worldexplorer:hookshot");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		
		this.maxStackSize = 1;
		
		this.bFull3D = true;
	}
}
