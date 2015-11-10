package zairus.worldexplorer.archery.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zairus.worldexplorer.archery.entity.EntityPebble;
import zairus.worldexplorer.archery.items.WEArcheryItems;

public class EntitySkeletonExplorer
	extends EntitySkeleton
{
	public EntitySkeletonExplorer(World world) {
		super(world);
		
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityCreeper.class, 8.0F));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityCreeper.class, 0, true));
	}
	
	protected void addRandomArmor()
    {
        super.addRandomArmor();
        this.setCurrentItemOrArmor(0, new ItemStack(WEArcheryItems.slingshot));
    }
	
	public void setCombatTask()
    {
    }
	
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
    {
        EntityPebble entitypebble = new EntityPebble(this.worldObj, this, p_82196_1_, 1.6F, (float)(14 - this.worldObj.difficultySetting.getDifficultyId() * 4));
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
        entitypebble.setDamage((double)(p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.difficultySetting.getDifficultyId() * 0.11F));

        if (i > 0)
        {
            entitypebble.setDamage(entitypebble.getDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0)
        {
            entitypebble.setKnockbackStrength(j);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.getSkeletonType() == 1)
        {
            entitypebble.setFire(100);
        }
        
        this.playSound("worldexplorer:slingshot_release_1", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entitypebble);
    }
	
	protected Item getDropItem()
    {
        return WEArcheryItems.pebble;
    }
	
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        int j;
        int k;

        if (this.getSkeletonType() == 1)
        {
            j = this.rand.nextInt(3 + p_70628_2_) - 1;

            for (k = 0; k < j; ++k)
            {
                this.dropItem(Items.coal, 1);
            }
        }
        else
        {
            j = this.rand.nextInt(3 + p_70628_2_);

            for (k = 0; k < j; ++k)
            {
                this.dropItem(WEArcheryItems.pebble, 1);
            }
        }

        j = this.rand.nextInt(3 + p_70628_2_);

        for (k = 0; k < j; ++k)
        {
            this.dropItem(Items.bone, 1);
        }
    }
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

    protected void entityInit()
    {
        super.entityInit();
    }
    
    protected String getLivingSound()
    {
        return "mob.skeleton.say";
    }

    protected String getHurtSound()
    {
        return "mob.skeleton.hurt";
    }

    protected String getDeathSound()
    {
        return "mob.skeleton.death";
    }

    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {
        this.playSound("mob.skeleton.step", 0.15F, 1.0F);
    }
}
