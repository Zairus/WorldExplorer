package zairus.worldexplorer.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import zairus.worldexplorer.core.inventory.ContainerStudyDesk;
import zairus.worldexplorer.core.tileentity.TileEntityDesk;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler
	implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityDesk)
		{
			return new ContainerStudyDesk(player.inventory, (TileEntityDesk)tileEntity, world);
		}
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityDesk)
		{
			return new GuiStudyDesk(player.inventory, (TileEntityDesk)tileEntity, world);
		}
		
		return null;
	}
	
}
