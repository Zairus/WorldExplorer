package zairus.worldexplorer.core.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import zairus.worldexplorer.core.items.WorldExplorerItems;

public class JournalPacket 
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
		;
	}
	
	@Override
	public void handleServerSide(EntityPlayer player)
	{
		String name = player.getDisplayName() + "'s Journal";
		ItemStack journal = player.getHeldItem();
		
		if (journal == null)
			return;
		
		if (journal.getItem() != WorldExplorerItems.journal)
			return;
		
		if (!journal.hasTagCompound())
			journal.setTagCompound(new NBTTagCompound());
		
		journal.getTagCompound().setString("JournalTitle", name);
	}
}
