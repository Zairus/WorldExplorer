package zairus.worldexplorer.archery.entity;

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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import zairus.worldexplorer.core.WorldExplorer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityDart
	extends Entity
	implements IProjectile
{
	private int positionX = -1;
	private int positionY = -1;
	private int positionZ = -1;
	private Block field_145790_g;
	private int inData;
	public Entity shootingEntity;
	private int ticksInAir;
	private double damage = 1.0D;
	
	public EntityDart(World world)
	{
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
	}
	
	public EntityDart(World world, EntityLivingBase entity, float f)
	{
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = entity;
		
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ, entity.rotationYaw, entity.rotationPitch);
		this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, f * 1.5F, 1.0F);
	}
	
	protected void entityInit()
	{
		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}
	
	public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_)
	{
		float f2 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
		p_70186_1_ /= (double) f2;
		p_70186_3_ /= (double) f2;
		p_70186_5_ /= (double) f2;
		p_70186_1_ += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) p_70186_8_;
		p_70186_3_ += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) p_70186_8_;
		p_70186_5_ += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) p_70186_8_;
		p_70186_1_ *= (double) p_70186_7_;
		p_70186_3_ *= (double) p_70186_7_;
		p_70186_5_ *= (double) p_70186_7_;
		this.motionX = p_70186_1_;
		this.motionY = p_70186_3_;
		this.motionZ = p_70186_5_;
		float f3 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
		this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(p_70186_1_, p_70186_5_) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(p_70186_3_, (double) f3) * 180.0D / Math.PI);
	}
	
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_)
	{
		this.setPosition(p_70056_1_, p_70056_3_, p_70056_5_);
		this.setRotation(p_70056_7_, p_70056_8_);
	}
	
	@SideOnly(Side.CLIENT)
	public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_)
	{
		this.motionX = p_70016_1_;
		this.motionY = p_70016_3_;
		this.motionZ = p_70016_5_;
		
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt_double(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(p_70016_1_, p_70016_5_) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(p_70016_3_, (double) f) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
		}
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f) * 180.0D / Math.PI);
		}
		
		Block block = this.worldObj.getBlock(this.positionX, this.positionY, this.positionZ);
		
		if (block.getMaterial() != Material.air)
		{
			if (block.stepSound.getBreakSound().length() > 0)
				this.playSound(block.stepSound.getBreakSound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			
			try
			{
				for (int p_i = 0; p_i < 15; p_i++)
					this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_0", this.positionX, this.positionY, this.positionZ, 0.0D, 0.0D, 0.0D);
			} catch (Exception e) {
				WorldExplorer.log(e.getMessage());
			}
			
			setDead();
		}
		
		++this.ticksInAir;
		Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY,this.posZ);
		Vec3 vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec31, vec3, false, true, false);
		vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		
		if (movingobjectposition != null)
		{
			vec3 = Vec3.createVectorHelper(
					movingobjectposition.hitVec.xCoord,
					movingobjectposition.hitVec.yCoord,
					movingobjectposition.hitVec.zCoord);
		}
		
		Entity entity = null;
		@SuppressWarnings("rawtypes")
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
		double d0 = 0.0D;
		int i;
		float f1;
		
		for (i = 0; i < list.size(); ++i)
		{
			Entity entity1 = (Entity) list.get(i);
			
			if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
			{
				f1 = 0.3F;
				AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double) f1, (double) f1, (double) f1);
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
		
		if (movingobjectposition != null
				&& movingobjectposition.entityHit != null
				&& movingobjectposition.entityHit instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;
			
			if (entityplayer.capabilities.disableDamage
					|| this.shootingEntity instanceof EntityPlayer
					&& !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer))
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
				int k = MathHelper.ceiling_double_int((double) f2 * this.damage);
				
				if (this.getIsCritical())
				{
					k += this.rand.nextInt(k / 2 + 2);
				}
				
				DamageSource damagesource = null;
				
				if (this.shootingEntity == null)
				{
					damagesource = (new EntityDamageSourceIndirect("dart", this, this)).setProjectile();
				} else {
					damagesource = (new EntityDamageSourceIndirect("dart", this, this.shootingEntity)).setProjectile();
				}
				
				if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman))
				{
					movingobjectposition.entityHit.setFire(5);
				}
				
				if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float) k))
				{
					if (movingobjectposition.entityHit instanceof EntityLivingBase)
					{
						EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;
						
						if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase)
						{
							EnchantmentHelper.func_151384_a(entitylivingbase, this.shootingEntity);
							EnchantmentHelper.func_151385_b((EntityLivingBase) this.shootingEntity, entitylivingbase);
						}
						
						if (this.shootingEntity != null
								&& movingobjectposition.entityHit != this.shootingEntity
								&& movingobjectposition.entityHit instanceof EntityPlayer
								&& this.shootingEntity instanceof EntityPlayerMP)
						{
							((EntityPlayerMP) this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
						}
					}
					
					if (!(movingobjectposition.entityHit instanceof EntityEnderman))
					{
						this.setDead();
					}
				} else {
					this.motionX *= -0.10000000149011612D;
					this.motionY *= -0.10000000149011612D;
					this.motionZ *= -0.10000000149011612D;
					this.rotationYaw += 180.0F;
					this.prevRotationYaw += 180.0F;
					this.ticksInAir = 0;
				}
			} else {
				this.positionX = movingobjectposition.blockX;
				this.positionY = movingobjectposition.blockY;
				this.positionZ = movingobjectposition.blockZ;
				this.field_145790_g = this.worldObj.getBlock(this.positionX, this.positionY, this.positionZ);
				this.inData = this.worldObj.getBlockMetadata(this.positionX, this.positionY, this.positionZ);
				this.motionX = (double) ((float) (movingobjectposition.hitVec.xCoord - this.posX));
				this.motionY = (double) ((float) (movingobjectposition.hitVec.yCoord - this.posY));
				this.motionZ = (double) ((float) (movingobjectposition.hitVec.zCoord - this.posZ));
				
				f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
				this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
				this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
				this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
				
				this.setIsCritical(false);
				
				if (this.field_145790_g.getMaterial() != Material.air)
				{
					this.field_145790_g.onEntityCollidedWithBlock(this.worldObj, this.positionX, this.positionY, this.positionZ, this);
				}
			}
		}
		
		if (this.getIsCritical())
		{
			for (i = 0; i < 1; ++i)
			{
				this.worldObj.spawnParticle("crit", this.posX
						+ this.motionX * (double) i / 4.0D, this.posY
						+ this.motionY * (double) i / 4.0D, this.posZ
						+ this.motionZ * (double) i / 4.0D, -this.motionX,
						-this.motionY + 0.2D, -this.motionZ);
			}
		}
		
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
		
		for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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
		f1 = 0.05F;
		
		if (this.isInWater())
		{
			for (int l = 0; l < 4; ++l)
			{
				f4 = 0.25F;
				this.worldObj.spawnParticle("bubble", this.posX
						- this.motionX * (double) f4, this.posY
						- this.motionY * (double) f4, this.posZ
						- this.motionZ * (double) f4, this.motionX,
						this.motionY, this.motionZ);
			}
			
			f3 = 0.8F;
		}
		
		if (this.isWet())
		{
			this.extinguish();
		}
		
		this.motionX *= (double) f3;
		this.motionY *= (double) f3;
		this.motionZ *= (double) f3;
		this.motionY -= (double) f1;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.func_145775_I();
	}
	
	public void writeEntityToNBT(NBTTagCompound p_70014_1_)
	{
		p_70014_1_.setShort("xTile", (short) this.positionX);
		p_70014_1_.setShort("yTile", (short) this.positionY);
		p_70014_1_.setShort("zTile", (short) this.positionZ);
		p_70014_1_.setByte("inTile", (byte) Block.getIdFromBlock(this.field_145790_g));
		p_70014_1_.setByte("inData", (byte) this.inData);
		p_70014_1_.setDouble("damage", this.damage);
	}
	
	public void readEntityFromNBT(NBTTagCompound p_70037_1_)
	{
		this.positionX = p_70037_1_.getShort("xTile");
		this.positionY = p_70037_1_.getShort("yTile");
		this.positionZ = p_70037_1_.getShort("zTile");
		this.field_145790_g = Block.getBlockById(p_70037_1_.getByte("inTile") & 255);
		this.inData = p_70037_1_.getByte("inData") & 255;
		
		if (p_70037_1_.hasKey("damage", 99))
		{
			this.damage = p_70037_1_.getDouble("damage");
		}
	}
	
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public float getShadowSize()
	{
		return 0.0F;
	}
	
	public void setDamage(double damage)
	{
		this.damage = damage;
	}
	
	public double getDamage()
	{
		return this.damage;
	}
	
	public boolean canAttackWithItem()
	{
		return false;
	}
	
	public void setIsCritical(boolean p_70243_1_)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);
		
		if (p_70243_1_)
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -2)));
		}
	}
	
	public boolean getIsCritical()
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);
		return (b0 & 1) != 0;
	}
}