package zairus.worldexplorer.core.items;

import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import cpw.mods.fml.common.registry.GameRegistry;

public class WorldExplorerItems
{
	public static WEItem journal = null;
	public static WEItem needle = null;
	public static WEItem raregem = null;
	
	public static void init()
	{
		journal = new BookJournal();
		needle = new WEItem().setUnlocalizedName("needle").setTextureName(WEConstants.CORE_PREFIX + ":needle").setCreativeTab(WorldExplorer.tabWorldExplorer);
		raregem = new WERareGem();
	}
	
	public static void register()
	{
		GameRegistry.registerItem(journal, journal.getUnlocalizedName());
		GameRegistry.registerItem(needle, needle.getUnlocalizedName());
		GameRegistry.registerItem(raregem, raregem.getUnlocalizedName());
	}
}
