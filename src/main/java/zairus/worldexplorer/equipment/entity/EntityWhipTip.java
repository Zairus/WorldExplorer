package zairus.worldexplorer.equipment.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import zairus.worldexplorer.equipment.items.WEEquipmentItems;
import zairus.worldexplorer.equipment.items.Whip;

public class EntityWhipTip
	extends Entity
	implements IProjectile
{
	private int positionX = -1;
	private int positionY = -1;
	private int positionZ = -1;
	private Block collisionBlock;
	private int ticksInAir;
	private double damage = 2.5D;
	private int knockbackStrength;
	private ItemStack whipItem;
	private float tipWidth = 0.5F;
	private float tipHeight = 0.5F;
	private float strength;
	private float whipReach = 4.0F;
	private boolean destReached = false;
	private boolean isCritical = false;
	private double distanceFromShooter = 0.0D;
	private List<Entity> owners = new ArrayList<Entity>();
	
	public Entity shootingEntity;
	
	public EntityWhipTip(World world)
	{
		super(world);
		
		this.renderDistanceWeight = 10.0D;
        this.setSize(tipWidth, tipHeight);
	}
	
	public EntityWhipTip(World world, double posX, double posY, double posZ)
	{
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.setSize(tipWidth, tipHeight);
		this.setPosition(posX, posY, posZ);
		this.yOffset = 0.0F;
	}
	
	public EntityWhipTip(World world, EntityLivingBase entityShooter, EntityLivingBase entityTarget, float p_i1755_4_, float p_i1755_5_)
	{
		super(world);
		
		this.renderDistanceWeight = 10.0D;
        this.shootingEntity = entityShooter;
        
        this.setSize(tipWidth, tipHeight);
	}
	
	public EntityWhipTip(World world, Entity player, float strength)
	{
		super(world);
		
		this.renderDistanceWeight = 10.0D;
        this.shootingEntity = player;
		
        this.setSize(tipWidth, tipHeight);
        this.setLocationAndAngles(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ, player.rotationYaw, player.rotationPitch);
		
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.60000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.strength = strength * 1.5F;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.strength, 1.0F);
	}
	
	public void setShootingEntity(Entity shooting)
	{
		this.shootingEntity = shooting;
	}
	
	public double getDistanceFromShooter()
	{
		return this.distanceFromShooter;
	}
	
	public void setReach(float reach)
	{
		this.whipReach = reach;
	}
	
	public float getReach()
	{
		return this.whipReach;
	}
	
	public void setWhipItem(ItemStack item)
	{
		this.whipItem = item;
	}
	
	public ItemStack getWhipItem()
	{
		return this.whipItem;
	}
	
	public List<Entity> getOwners()
	{
		return this.owners;
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f) * 180.0D / Math.PI);
        }
		
		Block block = this.worldObj.getBlock(this.positionX, this.positionY, this.positionZ);
		
		this.collisionBlock = block;
		
		if (block.getMaterial() != Material.air)
		{
			block.setBlockBoundsBasedOnState(this.worldObj, this.positionX, this.positionY, this.positionZ);
			
			this.destReached = true;
		}
		
		++this.ticksInAir;
		
		if (this.ticksInAir > 50)
		{
        	this.setDead();
        	return;
		}
		
		Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec31, vec3, false, true, false);
        vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        
        if (movingobjectposition != null)
        {
            vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }
        
        Entity entity = null;
        
        @SuppressWarnings("rawtypes")
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;
        int i;
        float f1;
        
        for (i = 0; i < list.size(); ++i)
        {
            Entity entity1 = (Entity)list.get(i);
            
            if (!entity1.isDead && entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
            {
                f1 = 0.3F;
                AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double)f1, (double)f1, (double)f1);
                MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);
                
                if (movingobjectposition1 != null)
                {
                    double d1 = vec31.distanceTo(movingobjectposition1.hitVec);
                    
                    if (d1 < d0 || d0 == 0.0D)
                    {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }
        
        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }
        
        if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;
            
            if (entityplayer == (EntityPlayer)this.shootingEntity || entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))
            {
                movingobjectposition = null;
            }
        }
        
        float f2;
        float f4;
        
        if (movingobjectposition != null)
        {
            if (movingobjectposition.entityHit != null)
            {
                f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                int k = MathHelper.ceiling_double_int((double)f2 * this.damage);
                
                DamageSource damagesource = null;
                
                if (this.shootingEntity == null)
                {
                    damagesource = (new EntityDamageSourceIndirect("whip", this, this));
                }
                else
                {
                    damagesource = (new EntityDamageSourceIndirect("whip", this, this.shootingEntity));
                }
                
                if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman))
                {
                    movingobjectposition.entityHit.setFire(5);
                }
                
                if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float)k))
                {
                    if (movingobjectposition.entityHit instanceof EntityLivingBase)
                    {
                        EntityLivingBase entitylivingbase = (EntityLivingBase)movingobjectposition.entityHit;
                        
                        if (this.knockbackStrength > 0)
                        {
                            f4 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                            if (f4 > 0.0F)
                            {
                                movingobjectposition.entityHit.addVelocity(this.motionX * (double)this.knockbackStrength * 0.6000000238418579D / (double)f4, 0.1D, this.motionZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)f4);
                            }
                        }
                        
                        if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase)
                        {
                            EnchantmentHelper.func_151384_a(entitylivingbase, this.shootingEntity);
                            EnchantmentHelper.func_151385_b((EntityLivingBase)this.shootingEntity, entitylivingbase);
                        }
                        
                        if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
                        {
                            ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                        }
                    }
                    
                    if (!(movingobjectposition.entityHit instanceof EntityEnderman))
                    {
                    	this.playSound("worldexplorer:pebble_hit_1", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    }
                }
            }
            else
            {
            	this.collisionBlock = this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
                
                if (this.collisionBlock.getMaterial() != Material.air)
                {
                	this.playSound("worldexplorer:pebble_hit_1", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.collisionBlock.onEntityCollidedWithBlock(this.worldObj, this.positionX, this.positionY, this.positionZ, this);
                    this.destReached = true;
                }
            }
        }
        
        if (this.isCritical)
        {
        	this.playSound("worldexplorer:whip_snap", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        	
        	for (int p_i = 0; p_i < 15; ++p_i)
        		this.worldObj.spawnParticle(
        				"crit"
        				, this.posX + this.motionX
        				, this.posY + this.motionY
        				, this.posZ + this.motionZ
        				, -this.motionX
        				, -this.motionY + 0.2D
        				, -this.motionZ);
        }
        
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        
        f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
        
        for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }
        
        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }
        
        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }
        
        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }
        
        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        
        float f3 = 0.99F;
        f1 = 0.01F;
        
        if (this.isInWater())
        {
            for (int l = 0; l < 4; ++l)
            {
                f4 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f4, this.posY - this.motionY * (double)f4, this.posZ - this.motionZ * (double)f4, this.motionX, this.motionY, this.motionZ);
            }
            this.destReached = true;
        }
        
        if (this.isWet())
        {
            this.extinguish();
        }
        
        if (this.shootingEntity != null)
        {
	        double distance = Vec3.createVectorHelper(this.shootingEntity.posX, this.shootingEntity.posY, this.shootingEntity.posZ).distanceTo(Vec3.createVectorHelper(this.posX, this.posY, this.posZ));
	        this.distanceFromShooter = distance;
	        
	        if (this.destReached)
	        {
	        	if (this.whipItem != null)
	        		((Whip)this.whipItem.getItem()).unleashed = false;
	        	
	        	this.setDead();
	        	if (this.shootingEntity instanceof EntityPlayer)
	        		((EntityPlayer)this.shootingEntity).stopUsingItem();
	        }
	        
	        if (distance >= this.whipReach && !this.destReached)
	        {
	        	this.isCritical = true;
	        	this.destReached = true;
	        }
	        else
	        {
	        	this.isCritical = false;
	        }
        }
        else
        {
        	f3 = 0.99F;
        	
        	this.motionX *= (double)f3;
            this.motionY *= (double)f3;
            this.motionZ *= (double)f3;
            this.motionY -= (double)f1;
        }
        
        this.setPosition(this.posX, this.posY, this.posZ);
        this.func_145775_I();
	}
	
	@Override
	protected void entityInit() {
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(10, new ItemStack(WEEquipmentItems.whip));
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompound) {
		this.positionX = tagCompound.getShort("xTile");
		this.positionY = tagCompound.getShort("yTile");
		this.positionZ = tagCompound.getShort("zTile");
		this.collisionBlock = Block.getBlockById(tagCompound.getByte("inTile") & 255);
		
		if (tagCompound.hasKey("damage", 99))
		{
			this.damage = tagCompound.getDouble("damage");
		}
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
		tagCompound.setShort("xTile", (short)this.positionX);
		tagCompound.setShort("yTile", (short)this.positionY);
		tagCompound.setShort("zTile", (short)this.positionZ);
		tagCompound.setByte("inTile", (byte)Block.getIdFromBlock(this.collisionBlock));
		tagCompound.setDouble("damage", this.damage);
	}
	
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (this.ticksInAir <= 2 && this.shootingEntity == null)
		{
			if (owners.size() == 0)
				owners.add(player);
		}
		
		if (!(this.shootingEntity instanceof EntityPlayer))
			return;
		
		if (!this.worldObj.isRemote && this.ticksInAir > 2 && player == (EntityPlayer)this.shootingEntity)
		{
			((Whip)this.whipItem.getItem()).unleashed = false;
			this.setDead();
		}
	}
	
	public void setKnockbackStrength(int knockback)
	{
		this.knockbackStrength = knockback;
	}
	
	public void setThrowableHeading(double dirX, double dirY, double dirZ)
	{
		setThrowableHeading(dirX, dirY, dirZ, this.strength, 1.0F);
	}
	
	@Override
	public void setThrowableHeading(double dirX, double dirY, double dirZ, float strength, float p_70186_8_)
	{
		float f2 = MathHelper.sqrt_double(dirX * dirX + dirY * dirY + dirZ * dirZ);
		dirX /= (double)f2;
		dirY /= (double)f2;
		dirZ /= (double)f2;
		dirX += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)p_70186_8_;
		dirY += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)p_70186_8_;
		dirZ += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)p_70186_8_;
		dirX *= (double)strength;
		dirY *= (double)strength;
		dirZ *= (double)strength;
		this.motionX = dirX;
		this.motionY = dirY;
		this.motionZ = dirZ;
		float f3 = MathHelper.sqrt_double(dirX * dirX + dirZ * dirZ);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(dirX, dirZ) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(dirY, (double)f3) * 180.0D / Math.PI);
	}
}
