package zairus.worldexplorer.archery.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.items.WEItem;
import cpw.mods.fml.common.registry.GameRegistry;

public class WEArcheryItems
{
	public static WEItem slingshot = null;
	public static WEItem boomerang = null;
	public static WEItem longbow = null;
	public static WEItem blowpipe = null;
	public static WEItem crossbow = null;
	
	public static WEItem dart = null;
	public static WEItem specialarrow = null;
	
	public static WEItem pebble = null;
	public static WEItem longbow_handle = null;
	public static WEItem longbow_string = null;
	
	public static void init()
	{
		slingshot = new Slingshot();
		boomerang = new Boomerang();
		longbow = new LongBow();
		blowpipe = new BlowPipe();
		crossbow = new CrossBow(); //*********
		
		dart = new Dart();
		specialarrow = new SpecialArrow();
		
		pebble = new WEItem().setUnlocalizedName("pebble").setTextureName(WEConstants.CORE_PREFIX + ":pebble").setCreativeTab(WorldExplorer.tabWorldExplorer).setMaxStackSize(16);
		longbow_handle = new WEItem().setUnlocalizedName("longbow_handle").setTextureName(WEConstants.CORE_PREFIX + ":longbow_handle_standby").setCreativeTab(WorldExplorer.tabWorldExplorer);
		longbow_string = new WEItem().setUnlocalizedName("longbow_string").setTextureName(WEConstants.CORE_PREFIX + ":longbow_string_pulling_0").setCreativeTab(WorldExplorer.tabWorldExplorer);
	}
	
	public static void register()
	{
		GameRegistry.registerItem(slingshot, slingshot.getUnlocalizedName());
		GameRegistry.registerItem(boomerang, boomerang.getUnlocalizedName());
		GameRegistry.registerItem(longbow, longbow.getUnlocalizedName());
		GameRegistry.registerItem(blowpipe, blowpipe.getUnlocalizedName());
		GameRegistry.registerItem(crossbow, crossbow.getUnlocalizedName());
		
		GameRegistry.registerItem(dart, dart.getUnlocalizedName());
		GameRegistry.registerItem(specialarrow, specialarrow.getUnlocalizedName());
		
		GameRegistry.registerItem(pebble, pebble.getUnlocalizedName());
		GameRegistry.registerItem(longbow_handle, longbow_handle.getUnlocalizedName());
		GameRegistry.registerItem(longbow_string, longbow_string.getUnlocalizedName());
		
		ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(new ItemStack(slingshot), 1, 2, 5));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(slingshot), 1, 2, 5));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(new ItemStack(slingshot), 1, 2, 5));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(boomerang), 1, 1, 2));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(new ItemStack(boomerang), 1, 1, 2));
		
		MinecraftForge.addGrassSeed(new ItemStack(pebble), 4);
		MinecraftForge.addGrassSeed(new ItemStack(Items.stick), 4);
	}
}
