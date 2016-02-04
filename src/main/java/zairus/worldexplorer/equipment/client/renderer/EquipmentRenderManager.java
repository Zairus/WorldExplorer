package zairus.worldexplorer.equipment.client.renderer;

import net.minecraftforge.client.MinecraftForgeClient;
import zairus.worldexplorer.core.IWEAddonRenderManager;
import zairus.worldexplorer.equipment.client.render.item.ItemSpyGlassRenderer;
import zairus.worldexplorer.equipment.items.WEEquipmentItems;

public class EquipmentRenderManager
	implements IWEAddonRenderManager
{
	@Override
	public void registerRenderers()
	{
		MinecraftForgeClient.registerItemRenderer(WEEquipmentItems.spyglass, new ItemSpyGlassRenderer());
		//RenderingRegistry.registerEntityRenderingHandler(EntityWhipTip.class, new RenderEntityWhip());
		//MinecraftForgeClient.registerItemRenderer(WEEquipmentItems.whip, new ItemWhipRenderer());
	}
}
