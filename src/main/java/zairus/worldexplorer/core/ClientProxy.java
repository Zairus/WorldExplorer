package zairus.worldexplorer.core;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import zairus.worldexplorer.core.block.WorldExplorerBlocks;
import zairus.worldexplorer.core.client.IPlayerRenderer;
import zairus.worldexplorer.core.gui.GuiScreenJournal;

public class ClientProxy
	extends CommonProxy
{
	public static Minecraft mc = Minecraft.getMinecraft();
	
	@Override
	public void preInit(FMLPreInitializationEvent e)
	{
		super.preInit(e);
		
		WorldExplorerBlocks.registerRenderIds();
	}
	
	@Override
	public void init(FMLInitializationEvent e)
	{
		super.init(e);
		
		for (int i = 0; i < WorldExplorer.getRegisteredMods().size(); i++)
		{
			WorldExplorer.getRegisteredMods().get(i).getRenderManager().registerRenderers();
		}
		
		WEKeyBindings.init();
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent e)
	{
		super.postInit(e);
	}
	
	@Override
	public void displayBookJournalGUI(EntityPlayer player, ItemStack stack)
	{
		super.displayBookJournalGUI(player, stack);
		
		mc.displayGuiScreen(new GuiScreenJournal(player, stack));
	}
	
	public void registerPlayerRenderer(IPlayerRenderer... renderers)
	{
		for (int i = 0; i < renderers.length; ++i)
		{
			playerRenderers.add(renderers[i]);
		}
	}
}
