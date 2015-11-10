package zairus.worldexplorer.core.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import zairus.worldexplorer.core.items.WorldExplorerItems;
import zairus.worldexplorer.core.player.CorePlayerManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WEEventHandler
{
	@SubscribeEvent
	public void itemCrafted(ItemCraftedEvent event)
	{
		CorePlayerManager.unlockItem(event.player, event.crafting.getItem());
		
		if (event.crafting.getItem() == WorldExplorerItems.journal)
		{
			;
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void playerJoined(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			CorePlayerManager.checkInitialize((EntityPlayer)event.entity);
			
			boolean journalGiven = ((EntityPlayer)event.entity).getEntityData().getBoolean("journalGiven");
			
			if (!journalGiven)
			{
				((EntityPlayer)event.entity).inventory.addItemStackToInventory(new ItemStack(WorldExplorerItems.journal));
				journalGiven = true;
				((EntityPlayer)event.entity).getEntityData().setBoolean("journalGiven", journalGiven);
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		//WorldExplorer.log("[ItemPickup] Picked up: " + event.item.getEntityItem().getItem().getUnlocalizedName());
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		//
	}
	/*
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event)
	{
		;
	}
	*/
}
