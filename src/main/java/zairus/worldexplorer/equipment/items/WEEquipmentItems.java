package zairus.worldexplorer.equipment.items;

import cpw.mods.fml.common.registry.GameRegistry;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.items.WEItem;

public class WEEquipmentItems
{
	public static WEItem whiphandle = null;
	public static WEItem spyglass = null;
	
	public static void init()
	{
		spyglass = new SpyGlass();
		whiphandle = new WEItem().setUnlocalizedName("whiphandle").setTextureName(WEConstants.CORE_PREFIX + ":whip_handle").setCreativeTab(WorldExplorer.tabWorldExplorer);
	}
	
	public static void register()
	{
		GameRegistry.registerItem(spyglass, spyglass.getUnlocalizedName());
		GameRegistry.registerItem(whiphandle, whiphandle.getUnlocalizedName());
	}
}
