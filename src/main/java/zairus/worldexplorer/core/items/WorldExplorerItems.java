package zairus.worldexplorer.core.items;

import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import cpw.mods.fml.common.registry.GameRegistry;

public class WorldExplorerItems
{
	public static WEItem journal = null;
	public static WEItem needle = null;
	public static WEItem raregem = null;
	public static WEItem explorerbag = null;
	
	public static void init()
	{
		journal = new BookJournal();
		needle = new WEItem().setUnlocalizedName("needle").setTextureName(WEConstants.CORE_PREFIX + ":needle").setCreativeTab(WorldExplorer.tabWorldExplorer);
		raregem = new WERareGem();
		explorerbag = new WEItem().setUnlocalizedName("bag").setTextureName(WEConstants.CORE_PREFIX + ":bag").setCreativeTab(WorldExplorer.tabWorldExplorer);
	}
	
	public static void register()
	{
		GameRegistry.registerItem(journal, journal.getUnlocalizedName());
		GameRegistry.registerItem(needle, needle.getUnlocalizedName());
		GameRegistry.registerItem(raregem, raregem.getUnlocalizedName());
		GameRegistry.registerItem(explorerbag, explorerbag.getUnlocalizedName());
	}
}
