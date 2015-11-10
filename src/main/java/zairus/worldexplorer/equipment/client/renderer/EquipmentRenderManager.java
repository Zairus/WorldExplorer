package zairus.worldexplorer.equipment.client.renderer;

import net.minecraftforge.client.MinecraftForgeClient;
import zairus.worldexplorer.core.IWEAddonRenderManager;
import zairus.worldexplorer.equipment.client.render.entity.RenderEntityWhip;
import zairus.worldexplorer.equipment.client.render.item.ItemWhipRenderer;
import zairus.worldexplorer.equipment.entity.EntityWhipTip;
import zairus.worldexplorer.equipment.items.WEEquipmentItems;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class EquipmentRenderManager
	implements IWEAddonRenderManager
{
	@Override
	public void registerRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityWhipTip.class, new RenderEntityWhip());
		
		MinecraftForgeClient.registerItemRenderer(WEEquipmentItems.whip, new ItemWhipRenderer());
	}
}
