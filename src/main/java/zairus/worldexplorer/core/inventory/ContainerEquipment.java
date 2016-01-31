package zairus.worldexplorer.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ContainerEquipment
	extends ContainerBase
{
	public ContainerEquipment(EntityPlayer player, World world)
	{
		int iIndex = 0;
		
		//hotbar :from 0 to 8
		int gridX = 7;
		int gridY = 149;
		int gridCols = 9;
		int gridRows = 1;
		iIndex = placeSlotGrid(player.inventory, iIndex, gridX, gridY, gridCols, gridRows);
		
		//player's :from 9 to 35
		gridX = 7;
		gridY = 91;
		gridCols = 9;
		gridRows = 3;
		iIndex = placeSlotGrid(player.inventory, iIndex, gridX, gridY, gridCols, gridRows);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return false;
	}
}
