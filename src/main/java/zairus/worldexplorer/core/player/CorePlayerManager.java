package zairus.worldexplorer.core.player;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import zairus.worldexplorer.core.inventory.InventoryPlayerEquipment;

public class CorePlayerManager
{
	private static NBTTagCompound worldExplorerData;
	
	public static final String KEY_PLAYERS = "players";
	public static final String KEY_WORLDEXPLORERDATA = "WorldExplorerData";
	public static final String KEY_UNLOCKEDITEMS = "unlockedItems";
	public static final String KEY_JOURNALGIVEN = "journalGiven";
	public static final String KEY_PLAYEREQUIPMENTINVENTORY = "equipmentInventory";
	
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
	
	@SideOnly(Side.CLIENT)
	public static boolean getPlayerHasQuiver(EntityPlayer player)
	{
		boolean hasQuiver = false;
		
		if (player != null)
		{
			InventoryPlayerEquipment equipmentInventory = getPlayerEquipmentInventory(player);
			
			if (equipmentInventory != null)
				hasQuiver = equipmentInventory.hasQuiver();
		}
		
		return hasQuiver;
	}
	
	public static InventoryPlayerEquipment getPlayerEquipmentInventory(EntityPlayer player)
	{
		InventoryPlayerEquipment playerEquipmentInventory = null;
		
		if (worldExplorerData == null)
			checkInitialize(player);
		
		NBTTagCompound WEPlayerData = worldExplorerData.getCompoundTag(KEY_PLAYERS).getCompoundTag(player.getUniqueID().toString());
		NBTTagList tagList = new NBTTagList();
		playerEquipmentInventory = new InventoryPlayerEquipment(player);
		
		if (player.getEntityData().hasKey(KEY_WORLDEXPLORERDATA) && player.getEntityData().getCompoundTag(KEY_WORLDEXPLORERDATA).hasKey(KEY_PLAYEREQUIPMENTINVENTORY))
		{
			tagList = player.getEntityData().getCompoundTag(KEY_WORLDEXPLORERDATA).getTagList(KEY_PLAYEREQUIPMENTINVENTORY, 10);
			playerEquipmentInventory.readFromNBT(tagList);
		}
		else
		{
			tagList = playerEquipmentInventory.writeToNBT(tagList);
			WEPlayerData.setTag(KEY_PLAYEREQUIPMENTINVENTORY, tagList);
			savePlayerData(player);
		}
		
		return playerEquipmentInventory;
	}
	
	public static void savePlayerEquipmentInventory(InventoryPlayerEquipment inv, EntityPlayer player)
	{
		NBTTagCompound WEPlayerData = worldExplorerData.getCompoundTag(KEY_PLAYERS).getCompoundTag(player.getUniqueID().toString());
		
		NBTTagList tagList = new NBTTagList();
		tagList = inv.writeToNBT(tagList);
		
		WEPlayerData.setTag(CorePlayerManager.KEY_PLAYEREQUIPMENTINVENTORY, tagList);
		
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
	
	public static boolean getJournalGiven(EntityPlayer player)
	{
		boolean given = false;
		NBTTagCompound WEPlayerData = worldExplorerData.getCompoundTag(KEY_PLAYERS).getCompoundTag(player.getUniqueID().toString());
		
		if (WEPlayerData.hasKey(KEY_JOURNALGIVEN))
			given = WEPlayerData.getBoolean(KEY_JOURNALGIVEN);
		
		return given;
	}
	
	public static void setJournalGiven(EntityPlayer player, boolean given)
	{
		NBTTagCompound WEPlayerData = worldExplorerData.getCompoundTag(KEY_PLAYERS).getCompoundTag(player.getUniqueID().toString());
		
		WEPlayerData.setBoolean(KEY_JOURNALGIVEN, given);
		
		savePlayerData(player);
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
