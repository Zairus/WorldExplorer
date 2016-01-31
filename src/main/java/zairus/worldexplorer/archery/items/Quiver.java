package zairus.worldexplorer.archery.items;

import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.items.WEItem;

public class Quiver
	extends WEItem
{
	public Quiver()
	{
		super();
		
		setUnlocalizedName("quiver");
		setTextureName(WEConstants.CORE_PREFIX + ":quiver");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		setFull3D();
		
		this.maxStackSize = 1;
	}
}
