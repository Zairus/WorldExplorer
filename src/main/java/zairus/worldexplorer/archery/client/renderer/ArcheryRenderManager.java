package zairus.worldexplorer.archery.client.renderer;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraftforge.client.MinecraftForgeClient;
import zairus.worldexplorer.archery.client.renderer.entity.RenderEntityBoomerang;
import zairus.worldexplorer.archery.client.renderer.entity.RenderEntityDart;
import zairus.worldexplorer.archery.client.renderer.entity.RenderEntityPebble;
import zairus.worldexplorer.archery.client.renderer.entity.RenderEntitySpecialArrow;
import zairus.worldexplorer.archery.client.renderer.item.ItemBlowPipeRenderer;
import zairus.worldexplorer.archery.client.renderer.item.ItemBoomerangRenderer;
import zairus.worldexplorer.archery.client.renderer.item.ItemBowRenderer;
import zairus.worldexplorer.archery.client.renderer.item.ItemCrossBowRenderer;
import zairus.worldexplorer.archery.entity.EntityBoomerang;
import zairus.worldexplorer.archery.entity.EntityDart;
import zairus.worldexplorer.archery.entity.EntityPebble;
import zairus.worldexplorer.archery.entity.EntitySpecialArrow;
import zairus.worldexplorer.archery.entity.monster.EntitySkeletonExplorer;
import zairus.worldexplorer.archery.items.WEArcheryItems;
import zairus.worldexplorer.core.IWEAddonRenderManager;
import zairus.worldexplorer.core.WorldExplorer;

public class ArcheryRenderManager
	implements IWEAddonRenderManager
{
	@Override
	public void registerRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityPebble.class, new RenderEntityPebble());
		RenderingRegistry.registerEntityRenderingHandler(EntityBoomerang.class, new RenderEntityBoomerang());
		RenderingRegistry.registerEntityRenderingHandler(EntityDart.class, new RenderEntityDart());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpecialArrow.class, new RenderEntitySpecialArrow());
		
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonExplorer.class, new RenderSkeleton());
		
		MinecraftForgeClient.registerItemRenderer(WEArcheryItems.longbow, new ItemBowRenderer());
		MinecraftForgeClient.registerItemRenderer(WEArcheryItems.boomerang, new ItemBoomerangRenderer());
		MinecraftForgeClient.registerItemRenderer(WEArcheryItems.blowpipe, new ItemBlowPipeRenderer());
		MinecraftForgeClient.registerItemRenderer(WEArcheryItems.crossbow, new ItemCrossBowRenderer());
		
		WorldExplorer.proxy.registerPlayerRenderer(new PlayerQuiverRenderer());
	}
}
