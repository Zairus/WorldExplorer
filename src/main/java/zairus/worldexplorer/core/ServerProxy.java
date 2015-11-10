package zairus.worldexplorer.core;

import zairus.worldexplorer.core.player.CorePlayerManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy
	extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		// TODO Auto-generated method stub
		super.preInit(e);
	}
	
	@Override
	public void init(FMLInitializationEvent e) {
		// TODO Auto-generated method stub
		super.init(e);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent e) {
		// TODO Auto-generated method stub
		super.postInit(e);
	}
	
	@Override
	public void displayBookJournalGUI(EntityPlayer player, ItemStack stack)
	{
		super.displayBookJournalGUI(player, stack);
		
		CorePlayerManager.checkInitialize(player);
	}
}
