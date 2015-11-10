package zairus.worldexplorer.equipment.items;

import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.items.WEItem;

public class Rope
	extends WEItem
{
	public Rope()
	{
		super();
		
		setUnlocalizedName("rope");
		setTextureName("worldexplorer:rope");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		
		this.maxStackSize = 1;
	}
}
