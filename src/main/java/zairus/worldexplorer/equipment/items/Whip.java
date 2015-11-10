package zairus.worldexplorer.equipment.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.items.WEItem;
import zairus.worldexplorer.equipment.entity.EntityWhipTip;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Whip
	extends WEItem
{
	public static final String[] whipIconNameArray = new String[] {"tail"};
	public boolean unleashed = false;
	
	private IIcon[] tailIcons;
	private EntityWhipTip whipTip;
	private EntityPlayer shooterPlayer;
	
	private float throwYaw = 0;
	private float throwPitch = 0;
	private float maxReach = 4.0F;
	
	public Whip()
	{
		setMaxStackSize(1);
		setUnlocalizedName("whip");
		setTextureName("worldexplorer:whip");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		setFull3D();
		setMaxDamage(512);
	}
	
	public EntityPlayer getShooter()
	{
		return this.shooterPlayer;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(this.getIconString());
		tailIcons = new IIcon[whipIconNameArray.length];
		
		for (int i = 0; i < tailIcons.length; ++i)
		{
			tailIcons[i] = iconRegister.registerIcon(this.getIconString() + "_" + whipIconNameArray[i]);
		}
	}
	
	public IIcon getIconFromUseTick()
	{
		return this.tailIcons[0];
	}
	
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		return false;
	}
	
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int useCount)
	{
		if (whipTip != null && !whipTip.isDead)
		{
			whipTip.setDead();
		}
		this.unleashed = false;
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		stack.damageItem(1, player);
		this.unleashed = true;
		this.shooterPlayer = player;
		
		player.swingItem();
		player.setItemInUse(stack, getMaxItemUseDuration(stack));
		
		EntityWhipTip whipTip1 = new EntityWhipTip(world, player, 1.0F);
		
		this.throwYaw = player.rotationYaw;
		this.throwPitch = player.rotationPitch;
		
		whipTip1.setReach(this.maxReach);
		
		whipTip1.setWhipItem(player.inventory.getStackInSlot(player.inventory.currentItem));
		whipTip1.setShootingEntity(player);
		
		if (!world.isRemote)
			world.playSoundAtEntity(player, "worldexplorer:whip_swing", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
		
		if (!world.isRemote)
		{
			world.spawnEntityInWorld(whipTip1);
		}
		
		this.whipTip = whipTip1;
		
		return stack;
	}
	
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.bow;
	}
	
	public int getMaxItemUseDuration(ItemStack duration)
	{
		return 72000;
	}
	
	public double getWhipTipDistance()
	{
		double distance = 0.0D;
		
		if (this.whipTip != null)
		{
			distance = this.whipTip.getDistanceFromShooter();
		}
		
		return distance;
	}
	
	public float getPercentaje()
	{
		float p = 0;
		
		p = (float)getWhipTipDistance() / this.maxReach;
		
		if (p > 1.0F)
			p = 1.0F;
		
		return p;
	}
	
	public float getThrowYaw()
	{
		return this.throwYaw;
	}
	
	public float getThrowPitch()
	{
		return this.throwPitch;
	}
	
	public float getCurYaw()
	{
		float curYaw = 0;
		
		if (this.shooterPlayer != null)
		{
			curYaw = this.shooterPlayer.rotationYaw;
		}
		
		return curYaw;
	}
	
	public float getCurPitch()
	{
		float curPitch = 0;
		
		if (this.shooterPlayer!= null)
		{
			curPitch = this.shooterPlayer.rotationPitch;
		}
		
		return curPitch;
	}
}
