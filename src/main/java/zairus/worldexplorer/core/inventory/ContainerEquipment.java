package zairus.worldexplorer.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public class ContainerEquipment
	extends Container
{
	public ContainerEquipment(EntityPlayer player, World world)
	{
		;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return false;
	}
}
