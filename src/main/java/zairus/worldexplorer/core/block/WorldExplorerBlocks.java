package zairus.worldexplorer.core.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import zairus.worldexplorer.core.client.render.RenderStudyDesk;
import zairus.worldexplorer.core.tileentity.TileEntityDesk;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WorldExplorerBlocks
{
	public static Block studydesk;
	
	public static final void init()
	{
		GameRegistry.registerBlock(studydesk = new BlockStudyDesk("studydesk"), "studydesk");
	}
	
	@SideOnly(Side.CLIENT)
	public static final void registerRenderIds()
	{
		TileEntitySpecialRenderer renderStudyDesk = new RenderStudyDesk();
		
		((BlockStudyDesk)studydesk).setRenderId(RenderingRegistry.getNextAvailableRenderId());
		
		ClientRegistry.registerTileEntity(TileEntityDesk.class, "tileentitystudydesk", renderStudyDesk);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDesk.class, renderStudyDesk);
		
		RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)renderStudyDesk);
		RenderingRegistry.registerBlockHandler(((BlockStudyDesk)WorldExplorerBlocks.studydesk).getRenderType(), (ISimpleBlockRenderingHandler)renderStudyDesk);
	}
}
