package zairus.worldexplorer.core.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.inventory.ContainerEquipment;
import zairus.worldexplorer.core.inventory.ContainerStudyDesk;
import zairus.worldexplorer.core.tileentity.TileEntityDesk;

public class GuiHandler
	implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(id)
		{
		case WEConstants.GUIID_STUDYDESK:
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityDesk)
			{
				return new ContainerStudyDesk(player.inventory, (TileEntityDesk)tileEntity, world);
			}
			break;
		case WEConstants.GUIID_EQUIPMENT:
			return new ContainerEquipment(player, world);
		}
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(id)
		{
		case WEConstants.GUIID_STUDYDESK:
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityDesk)
			{
				return new GuiStudyDesk(player.inventory, (TileEntityDesk)tileEntity, world);
			}
			break;
		case WEConstants.GUIID_EQUIPMENT:
			return new GuiScreenEquipment(player, world);
		}
		
		return null;
	}
	
}
