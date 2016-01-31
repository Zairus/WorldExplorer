package zairus.worldexplorer.core;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import zairus.worldexplorer.core.block.WorldExplorerBlocks;
import zairus.worldexplorer.core.client.IPlayerRenderer;
import zairus.worldexplorer.core.world.gen.feature.WorldGenBurial;

public class CommonProxy
{
	protected List<IPlayerRenderer> playerRenderers = new ArrayList<IPlayerRenderer>();
	
	public void preInit(FMLPreInitializationEvent e)
	{
		WorldExplorerBlocks.init();
		
		for (int i = 0; i < WorldExplorer.getRegisteredMods().size(); i++)
		{
			WorldExplorer.getRegisteredMods().get(i).getEntityManager().registerEntities();
		}
	}
	
	public void init(FMLInitializationEvent e)
	{
		for (int i = 0; i < WorldExplorer.getRegisteredMods().size(); i++)
		{
			WorldExplorer.getRegisteredMods().get(i).getMonsterManager().registerMobs();
		}
		
		GameRegistry.registerWorldGenerator(new WorldGenBurial(), 5);
	}
	
	public void postInit(FMLPostInitializationEvent e)
	{
		//
	}
	
	public void displayBookJournalGUI(EntityPlayer player, ItemStack stack)
	{
		;
	}
	
	public void registerPlayerRenderer(IPlayerRenderer... renderers)
	{
		;
	}
	
	public List<IPlayerRenderer> getPlayerRenderers ()
	{
		return playerRenderers;
	}
}
