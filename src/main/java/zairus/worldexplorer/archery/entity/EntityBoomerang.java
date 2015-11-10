package zairus.worldexplorer.archery.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import zairus.worldexplorer.archery.items.WEArcheryItems;
import zairus.worldexplorer.core.WorldExplorer;

/**
 * yyyymmdd: Suggedted by TheMissingnoLP
 * yyyymmdd: Junt_sauce suggested it should break grass, not possible yet
 * */

public class EntityBoomerang
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
	private ItemStack thrownBoomerang;
	private float strength;
	private double boomerangReach = 16.0D;
    private boolean destReached = false;
    private boolean canCollectItems = true;
    private List<ItemStack> inventory = new ArrayList<ItemStack>();
    private List<EntityXPOrb> orbsCollected = new ArrayList<EntityXPOrb>();
    private int capacity = 1;
	
    public Entity shootingEntity;
    
	public EntityBoomerang(World world)
	{
		super(world);
		
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
	}
	
	public EntityBoomerang(World world, double posX, double posY, double posZ)
	{
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
		this.setPosition(posX, posY, posZ);
		this.yOffset = 0.0F;
	}
	
	public EntityBoomerang(World world, EntityLivingBase entityShooter, EntityLivingBase entityTarget, float p_i1755_4_, float p_i1755_5_)
	{
		super(world);
		
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = entityShooter;
        
        this.posY = entityShooter.posY + (double)entityShooter.getEyeHeight() - 0.10000000149011612D;
        double d0 = entityTarget.posX - entityShooter.posX;
        double d1 = entityTarget.boundingBox.minY + (double)(entityTarget.height / 3.0F) - this.posY;
        double d2 = entityTarget.posZ - entityShooter.posZ;
        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        
        if (d3 >= 1.0E-7D)
        {
            float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
            double d4 = d0 / d3;
            double d5 = d2 / d3;
            this.setLocationAndAngles(entityShooter.posX + d4, this.posY, entityShooter.posZ + d5, f2, f3);
            this.yOffset = 0.0F;
            float f4 = (float)d3 * 0.2F;
            this.setThrowableHeading(d0, d1 + (double)f4, d2, p_i1755_4_, p_i1755_5_);
        }
	}
	
	public EntityBoomerang(World world, EntityLivingBase player, float p_i1756_3_)
	{
		super(world);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = player;
        
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ, player.rotationYaw, player.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.60000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.strength = p_i1756_3_ * 1.5F;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.strength, 1.0F);
	}
	
	private void returnToSender()
	{
		double d0 = this.shootingEntity.posX - this.posX;
		double d1 = this.shootingEntity.boundingBox.minY + (double)(this.shootingEntity.height / 3.0F) - this.posY;
		d1 -= 0.6D;
		double d2 = this.shootingEntity.posZ - this.posZ;
		double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		
		if (d3 >= 1.0E-7D)
		{
			float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
			float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, f2, f3);
			float f4 = (float)d3 * 0.2F;
			this.setThrowableHeading(d0, d1 + (double)f4, d2, this.strength, 1.0F);
		}
	}
	
	public void setReach(double reach)
	{
		this.boomerangReach = reach;
	}
	
	public void setCanCollectItems(boolean canCollect)
	{
		this.canCollectItems = canCollect;
	}
	
	public void setInventoryCapacity(int cap)
	{
		this.capacity = cap;
	}
	
	public List<ItemStack> getInventory()
	{
		return this.inventory;
	}
	
	public void setThrownBoomerang(ItemStack stack)
	{
		this.thrownBoomerang = stack;
	}
	
	public ItemStack getThrownBoomerang()
	{
		return this.thrownBoomerang;
	}
	
	@Override
	protected void entityInit() {
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(10, new ItemStack(WEArcheryItems.boomerang));
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
	public void setThrowableHeading(double dirX, double dirY, double dirZ, float p_70186_7_, float p_70186_8_)
	{
		float f2 = MathHelper.sqrt_double(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX /= (double)f2;
        dirY /= (double)f2;
        dirZ /= (double)f2;
        dirX += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)p_70186_8_;
        dirY += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)p_70186_8_;
        dirZ += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)p_70186_8_;
        dirX *= (double)p_70186_7_;
        dirY *= (double)p_70186_7_;
        dirZ *= (double)p_70186_7_;
        this.motionX = dirX;
        this.motionY = dirY;
        this.motionZ = dirZ;
        float f3 = MathHelper.sqrt_double(dirX * dirX + dirZ * dirZ);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(dirX, dirZ) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(dirY, (double)f3) * 180.0D / Math.PI);
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
			
			boolean c_flag = (block == Blocks.grass || block == Blocks.tallgrass);
			
			if (c_flag)
			{
				try
	        	{
	        		for (int p_i = 0; p_i < (c_flag ? 35 : 15); p_i++)
	        			this.worldObj.spawnParticle(
	        					"blockcrack_" + Block.getIdFromBlock(block) + "_0"
	        					, this.positionX
	        					, this.positionY
	        					, this.positionZ
	        					, 0.0D
	        					, 0.0D
	        					, 0.0D);
	        	}
	        	catch (Exception e)
	        	{
	        		WorldExplorer.log(e.getMessage());
	        	}
				if (block.stepSound.getBreakSound().length() > 0)
	    			this.playSound(block.stepSound.getBreakSound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
				block.breakBlock(worldObj, this.positionX, this.positionY, this.positionZ, block, 1);
				//this.worldObj.setBlockToAir(this.positionX, this.positionY, this.positionZ);
			}
			else
			{
				this.destReached = true;
			}
		}
		
		++this.ticksInAir;
		
		if (this.ticksInAir > 100)
		{
        	returnToPlayer();
        	return;
		}
		
		if ( ((double)this.ticksInAir / 2.0D) - Math.floor(((double)this.ticksInAir / 2.0D) ) == 0.0D )
			this.playSound("worldexplorer:boomerang_swoosh", 2.5F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
		
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
            
            if (entity1 instanceof EntityXPOrb)
            {
            	this.orbsCollected.add((EntityXPOrb)entity1);
            }
            
            if (entity1 instanceof EntityItem && this.canCollectItems && !this.isDead)
            {
            	if (this.inventory.size() < this.capacity)
                {
            		if (((EntityItem)entity1).getEntityItem() != this.thrownBoomerang)
            		{
	                	this.inventory.add(((EntityItem)entity1).getEntityItem().copy());
	                	
	                	if(!this.worldObj.isRemote)
	                		entity1.setDead();
            		}
                }
            }
            
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
                    damagesource = (new EntityDamageSourceIndirect("boomerang", this, this)).setProjectile();
                }
                else
                {
                    damagesource = (new EntityDamageSourceIndirect("boomerang", this, this.shootingEntity)).setProjectile();
                }
                
                if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman))
                {
                    movingobjectposition.entityHit.setFire(5);
                }
                
                if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float)k))
                {
                	if (this.shootingEntity instanceof EntityLivingBase)
                	{
                		boolean dFlag = true;
                		if (this.shootingEntity instanceof EntityPlayer)
                		{
                			if (((EntityPlayer)this.shootingEntity).capabilities.isCreativeMode)
                				dFlag = false;
                		}
                		
                		if (dFlag)
                		{
	                		if (this.thrownBoomerang.attemptDamageItem(1, ((EntityLivingBase)this.shootingEntity).getRNG()))
	                		{
	                			if(this.thrownBoomerang.getItemDamage() >= this.thrownBoomerang.getMaxDamage())
	                			{
	                				this.setDead();
	                				this.playSound("random.break", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	                				return;
	                			}
	                		}
                		}
                	}
                	
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
                        this.destReached = true;
                    }
                }
                else
                {
                	this.destReached = true;
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
        
        if (this.boomerangReach > 32.0F)
        	for (i = 0; i < 2; ++i)
	    		this.worldObj.spawnParticle(
	    				"crit"
	    				, this.posX + this.motionX * (double)i / 4.0D
	    				, this.posY + this.motionY * (double)i / 4.0D
	    				, this.posZ + this.motionZ * (double)i / 4.0D
	    				, -this.motionX
	    				, -this.motionY + 0.2D
	    				, -this.motionZ);
        
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
	        
	        if (distance >= this.boomerangReach && !this.destReached)
	        {
	        	this.destReached = true;
	        }
	        
	        if (this.destReached)
	        {
	        	returnToSender();
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
        
        for (int o_i = 0; o_i < this.orbsCollected.size(); o_i++)
        {
        	this.orbsCollected.get(o_i).setPosition(this.posX, this.posY, this.posZ);
        }
        
        this.setPosition(this.posX, this.posY, this.posZ);
        this.func_145775_I();
	}
	
	public double getDamage()
    {
        return this.damage;
    }
	
	public void setDamage(double damage)
    {
        this.damage = damage;
    }
	
	public void readEntityFromNBT(NBTTagCompound tagCompound) {
		this.positionX = tagCompound.getShort("xTile");
		this.positionY = tagCompound.getShort("yTile");
		this.positionZ = tagCompound.getShort("zTile");
		this.collisionBlock = Block.getBlockById(tagCompound.getByte("inTile") & 255);
		
		if (tagCompound.hasKey("damage", 99))
		{
			this.damage = tagCompound.getDouble("damage");
		}
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		tagCompound.setShort("xTile", (short)this.positionX);
		tagCompound.setShort("yTile", (short)this.positionY);
		tagCompound.setShort("zTile", (short)this.positionZ);
		tagCompound.setByte("inTile", (byte)Block.getIdFromBlock(this.collisionBlock));
		tagCompound.setDouble("damage", this.damage);
	}
	
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (!(this.shootingEntity instanceof EntityPlayer))
			return;
		
		if (!this.worldObj.isRemote && this.thrownBoomerang != null && this.ticksInAir > 2 && player == (EntityPlayer)this.shootingEntity)
		{
			returnToPlayer(true);
		}
	}
	
	private void returnToPlayer()
	{
		returnToPlayer(false);
	}
	
	private void returnToPlayer(boolean catched)
	{
		if (this.isDead)
			return;
		
		if (this.thrownBoomerang == null)
		{
			if (!this.isDead)
				this.setDead();
			
			return;
		}
		
		if(this.thrownBoomerang.getItemDamage() >= this.thrownBoomerang.getMaxDamage())
		{
			if (!this.isDead)
				this.setDead();
			
			return;
		}
		
		if (this.shootingEntity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)this.shootingEntity;
			this.canCollectItems = false;
			
			if (this.isBurning())
				player.setFire(3);
			
			if (!player.inventory.addItemStackToInventory(this.thrownBoomerang))
			{
				if(!this.worldObj.isRemote)
					this.worldObj.spawnEntityInWorld(
							new EntityItem(
									this.worldObj
									, (catched)? this.posX : this.shootingEntity.posX
									, (catched)? this.posY : this.shootingEntity.posY
									, (catched)? this.posZ : this.shootingEntity.posZ
									, this.thrownBoomerang));
			}
			else
			{
				EntityItemPickupEvent event = new EntityItemPickupEvent(
						player
						,new EntityItem(
								this.worldObj
								, (catched)? this.posX : this.shootingEntity.posX
								, (catched)? this.posY : this.shootingEntity.posY
								, (catched)? this.posZ : this.shootingEntity.posZ
								, this.thrownBoomerang));
				MinecraftForge.EVENT_BUS.post(event);
			}
			
			for (int i = 0; i < this.inventory.size(); i++)
			{
				if (!player.inventory.addItemStackToInventory(this.inventory.get(i)))
				{
					if(!this.worldObj.isRemote)
						this.worldObj.spawnEntityInWorld(
								new EntityItem(
										this.worldObj
										, (catched)? this.posX : this.shootingEntity.posX
										, (catched)? this.posY : this.shootingEntity.posY
										, (catched)? this.posZ : this.shootingEntity.posZ
										, this.inventory.get(i)));
				}
				else
				{
					EntityItemPickupEvent event = new EntityItemPickupEvent(
							player
							,new EntityItem(
									this.worldObj
									, (catched)? this.posX : this.shootingEntity.posX
									, (catched)? this.posY : this.shootingEntity.posY
									, (catched)? this.posZ : this.shootingEntity.posZ
									, this.inventory.get(i)));
					MinecraftForge.EVENT_BUS.post(event);
				}
			}
			
			this.setDead();
			
			this.playSound("worldexplorer:boomerang_catch", 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.onItemPickup(this, 1);
		}
	}
}
