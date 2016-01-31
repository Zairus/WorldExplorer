package zairus.worldexplorer.core.event;

import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import zairus.worldexplorer.archery.items.WEItemRanged;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WEKeyBindings;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.client.IPlayerRenderer;
import zairus.worldexplorer.core.items.WorldExplorerItems;
import zairus.worldexplorer.core.player.CorePlayerManager;

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
	public void playerJoined(PlayerLoggedInEvent event)
	{
		CorePlayerManager.checkInitialize(event.player);
		
		boolean journalGiven = event.player.getEntityData().getBoolean("journalGiven");
		
		if (!journalGiven)
		{
			ItemStack stack = new ItemStack(WorldExplorerItems.journal);
			event.player.inventory.addItemStackToInventory(stack);
			journalGiven = true;
			event.player.getEntityData().setBoolean("journalGiven", journalGiven);
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onFOVUpdate(FOVUpdateEvent event)
	{
		if (!event.entity.isUsingItem())
			return;
		
		if (!(event.entity.getItemInUse().getItem() instanceof WEItemRanged))
			return;
		
		event.newfov = event.fov  / (event.fov + (0.4f  * getItemInUsePercentaje(event.entity, 350.0f)));
	}
	
	@SideOnly(Side.CLIENT)
	private float getItemInUsePercentaje(EntityPlayerSP player, float speedFactor)
	{
		float maxUse = player.getItemInUse().getItem().getMaxItemUseDuration(player.getItemInUse()); //7200.0f;
		float curUse = (float)player.getItemInUseCount();
		float percent = 0.0f;
		
		percent = maxUse - curUse;
		percent *= (speedFactor * ((percent > 100)? 1.0f : percent / 100.0f));
		percent = percent / maxUse;
		percent *= 1 +  percent;
		
		return (percent > 1.0f)? 1.0f : percent;
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
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRender(RenderPlayerEvent.Post event)
	{
		List<IPlayerRenderer> renderers = WorldExplorer.proxy.getPlayerRenderers();
		
		for (int i = 0; i < renderers.size(); ++i)
		{
			renderers.get(i).render(event.entityPlayer);
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onKeyInput(InputEvent.KeyInputEvent event)
	{
		if(WEKeyBindings.explorergui.isPressed())
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			World world = Minecraft.getMinecraft().theWorld;
			
			if (player != null && world != null)
				player.openGui(WorldExplorer.instance, WEConstants.GUIID_EQUIPMENT, world, 0, 0, 0);
		}
	}
	
	/*
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event)
	{
		;
	}
	*/
}
