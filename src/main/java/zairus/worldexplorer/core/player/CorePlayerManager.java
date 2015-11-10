package zairus.worldexplorer.core.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

public class CorePlayerManager
{
	private static NBTTagCompound worldExplorerData;
	
	private static final String KEY_PLAYERS = "players";
	private static final String KEY_WORLDEXPLORERDATA = "WorldExplorerData";
	private static final String KEY_UNLOCKEDITEMS = "unlockedItems";
	private static final String KEY_JOURNALGIVEN = "journalGiven";
	
	public static void checkInitialize(EntityPlayer player)
	{
		if (worldExplorerData == null)
			worldExplorerData = new NBTTagCompound();
		
		if (!worldExplorerData.hasKey(KEY_PLAYERS))
			worldExplorerData.setTag(KEY_PLAYERS, new NBTTagCompound());
		
		if (!worldExplorerData.getCompoundTag(KEY_PLAYERS).hasKey(player.getUniqueID().toString()))
		{
			if (player.getEntityData().hasKey(KEY_WORLDEXPLORERDATA))
				worldExplorerData.getCompoundTag(KEY_PLAYERS).setTag(player.getUniqueID().toString(), player.getEntityData().getCompoundTag(KEY_WORLDEXPLORERDATA));
			else
				worldExplorerData.getCompoundTag(KEY_PLAYERS).setTag(player.getUniqueID().toString(), new NBTTagCompound());
		}
		
		savePlayerData(player);
	}
	
	private static void setDefaults(EntityPlayer player)
	{
		NBTTagCompound WEPlayerData = worldExplorerData.getCompoundTag(KEY_PLAYERS).getCompoundTag(player.getUniqueID().toString());
		
		if (!WEPlayerData.hasKey(KEY_UNLOCKEDITEMS))
			WEPlayerData.setTag(KEY_UNLOCKEDITEMS, new NBTTagCompound());
		
		if (!WEPlayerData.hasKey(KEY_JOURNALGIVEN))
			WEPlayerData.setBoolean(KEY_JOURNALGIVEN, false);
	}
	
	public static void unlockItem(EntityPlayer player, Item item)
	{
		checkInitialize(player);
		
		NBTTagCompound WEPlayerData = worldExplorerData.getCompoundTag(KEY_PLAYERS).getCompoundTag(player.getUniqueID().toString());
		
		if (WEPlayerData.hasKey(KEY_UNLOCKEDITEMS))
		{
			if(!WEPlayerData.getCompoundTag(KEY_UNLOCKEDITEMS).hasKey(item.getUnlocalizedName()))
				WEPlayerData.getCompoundTag(KEY_UNLOCKEDITEMS).setBoolean(item.getUnlocalizedName(), true);
		}
		
		savePlayerData(player);
	}
	
	public static boolean isItemUnlocked(EntityPlayer player, Item item)
	{
		checkInitialize(player);
		
		NBTTagCompound WEPlayerData = worldExplorerData.getCompoundTag(KEY_PLAYERS).getCompoundTag(player.getUniqueID().toString());
		
		return WEPlayerData.getCompoundTag(KEY_UNLOCKEDITEMS).hasKey(item.getUnlocalizedName());
	}
	
	public static void savePlayerData(EntityPlayer player)
	{
		NBTTagCompound WEPlayerData = worldExplorerData.getCompoundTag(KEY_PLAYERS).getCompoundTag(player.getUniqueID().toString());
		
		if (player != null && !player.getEntityData().hasKey(KEY_WORLDEXPLORERDATA))
		{
			setDefaults(player);
			player.getEntityData().setTag(KEY_WORLDEXPLORERDATA, WEPlayerData);
		}
		else
		{
			player.getEntityData().setTag(KEY_WORLDEXPLORERDATA, WEPlayerData);
		}
	}
}
