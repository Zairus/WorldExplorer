package zairus.worldexplorer.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import zairus.worldexplorer.core.block.WorldExplorerBlocks;
import zairus.worldexplorer.core.event.WEEventHandler;
import zairus.worldexplorer.core.gui.GuiHandler;
import zairus.worldexplorer.core.items.WorldExplorerItems;
import zairus.worldexplorer.core.util.network.PacketPipeline;

@Mod(modid=WEConstants.CORE_MODID, name=WEConstants.CORE_NAME, version=WEConstants.CORE_VERSION)
public class WorldExplorer
{
	private static List<IWEAddonMod> registeredMods = new ArrayList<IWEAddonMod>();
	public static Logger logger;
	public static Configuration configuration;
	public static PacketPipeline packetPipeline = new PacketPipeline();
	
	@SidedProxy(clientSide="zairus.worldexplorer.core.ClientProxy", serverSide="zairus.worldexplorer.core.ServerProxy")
	public static CommonProxy proxy;
	
	@Mod.Instance(WEConstants.CORE_MODID)
	public static WorldExplorer instance;
	
	public static CreativeTabs tabWorldExplorer = new CreativeTabs("tabWorldExplorer") {
		@Override
		public Item getTabIconItem() {
			return WorldExplorerItems.journal;
		}
	};
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		WorldExplorer.proxy.preInit(event);
		WorldExplorer.packetPipeline.initalise();
		
		WEConfig.init(event.getSuggestedConfigurationFile());
		
		WorldExplorerItems.init();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		WorldExplorerItems.register();
		
		WorldExplorer.proxy.init(event);
		
		addRecipes();
		
		WEEventHandler eventHandler = new WEEventHandler();
		
		FMLCommonHandler.instance().bus().register(eventHandler);
		MinecraftForge.EVENT_BUS.register(eventHandler);
		MinecraftForge.TERRAIN_GEN_BUS.register(eventHandler);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(WorldExplorer.instance, new GuiHandler());
	}
	
	@EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
		WorldExplorer.proxy.postInit(event);
		WorldExplorer.packetPipeline.postInitialise();
    }
	
	private void addRecipes()
	{
		GameRegistry.addShapedRecipe(
				new ItemStack(WorldExplorerBlocks.studydesk)
				, new Object[] {
					 " j "
					,"ptp"
					,"pcp"
					,'j'
					,WorldExplorerItems.journal
					,'p'
					,Blocks.planks
					,'t'
					,Blocks.crafting_table
					,'c'
					,Blocks.chest
				});
		
		GameRegistry.addShapelessRecipe(
				new ItemStack(WorldExplorerItems.journal)
				, new Object[] {
					Items.compass
					,Items.clock
					,Items.writable_book
				});
		
		GameRegistry.addShapelessRecipe(
				new ItemStack(WorldExplorerItems.needle, 6)
				, new Object[] {
					Blocks.cactus
				});
	}
	
	public static void registerWEAddonMod(IWEAddonMod mod)
	{
		registeredMods.add(mod);
	}
	
	public static List<IWEAddonMod> getRegisteredMods()
	{
		return registeredMods;
	}
	
	public static void log(String obj)
	{
		if (logger == null) {
			logger = Logger.getLogger("WorldExplorer");
		}
		if (obj == null) {
			obj = "null";
		}
		logger.info("[" + FMLCommonHandler.instance().getEffectiveSide() + "] " + obj);
	}
}
