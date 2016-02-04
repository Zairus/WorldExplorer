package zairus.worldexplorer.core.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;

public class GUIEquipmentPacket
	extends AbstractPacket
{
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		;
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		;
	}
	
	@Override
	public void handleClientSide(EntityPlayer player)
	{
		player.openGui(WorldExplorer.instance, WEConstants.GUIID_EQUIPMENT, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
	}
	
	@Override
	public void handleServerSide(EntityPlayer player)
	{
		player.openGui(WorldExplorer.instance, WEConstants.GUIID_EQUIPMENT, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
	}
}
