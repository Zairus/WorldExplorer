package zairus.worldexplorer.core.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import zairus.worldexplorer.core.items.WorldExplorerItems;
import zairus.worldexplorer.core.player.CorePlayerManager;

public class JournalPacket 
	extends AbstractPacket
{
	private JournalPacketAction action = JournalPacketAction.ACTION_JOURNAL_TITLE;
	
	public enum JournalPacketAction {
		ACTION_JOURNAL_TITLE
		,ACTION_PLAYER_INIT
	}
	
	public JournalPacket()
	{
		;
	}
	
	public JournalPacket(JournalPacketAction packetAction)
	{
		action = packetAction;
	}
	
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
		switch (action)
		{
		case ACTION_PLAYER_INIT:
			playerInit(player);
			break;
		default:
			updateJournalTitle(player);
			break;
		}
	}
	
	private void playerInit(EntityPlayer player)
	{
		CorePlayerManager.checkInitialize(player);
	}
	
	private void updateJournalTitle(EntityPlayer player)
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
