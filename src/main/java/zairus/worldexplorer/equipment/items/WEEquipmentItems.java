package zairus.worldexplorer.equipment.items;

import cpw.mods.fml.common.registry.GameRegistry;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.items.WEItem;

public class WEEquipmentItems
{
	public static WEItem hookshot = null;
	public static WEItem rope = null;
	public static WEItem whip = null;
	public static WEItem whiptip = null;
	public static WEItem whiphandle = null;
	public static WEItem leatherstrap = null;
	
	public static void init()
	{
		hookshot = new Hookshot(); //************
		rope = new Rope(); //************
		whip = new Whip();
		
		whiptip = new WEItem().setUnlocalizedName("whiptip").setTextureName(WEConstants.CORE_PREFIX + ":whip_tip").setCreativeTab(WorldExplorer.tabWorldExplorer);
		whiphandle = new WEItem().setUnlocalizedName("whiphandle").setTextureName(WEConstants.CORE_PREFIX + ":whip_handle").setCreativeTab(WorldExplorer.tabWorldExplorer);
		leatherstrap = new WEItem().setUnlocalizedName("leatherstrap").setTextureName(WEConstants.CORE_PREFIX + ":whip_tail").setCreativeTab(WorldExplorer.tabWorldExplorer);
	}
	
	public static void register()
	{
		GameRegistry.registerItem(hookshot, hookshot.getUnlocalizedName());
		GameRegistry.registerItem(rope, rope.getUnlocalizedName());
		GameRegistry.registerItem(whip, whip.getUnlocalizedName());
		GameRegistry.registerItem(whiptip, whiptip.getUnlocalizedName());
		GameRegistry.registerItem(whiphandle, whiphandle.getUnlocalizedName());
		GameRegistry.registerItem(leatherstrap, leatherstrap.getUnlocalizedName());
	}
}
