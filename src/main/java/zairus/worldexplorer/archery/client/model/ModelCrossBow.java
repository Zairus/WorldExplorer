package zairus.worldexplorer.archery.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCrossBow 
	extends ModelBase
{
	ModelRenderer Handle;
	ModelRenderer MiddleConnection;
	ModelRenderer BowRight;
	ModelRenderer BowLeft;
	ModelRenderer Hook;
	ModelRenderer Trigger;
	ModelRenderer StringLeft;
	ModelRenderer StringRight;

	public ModelCrossBow()
	{
		textureWidth = 64;
		textureHeight = 32;

		Handle = new ModelRenderer(this, 0, 0);
		Handle.addBox(0F, 0F, 0F, 2, 16, 2);
		Handle.setRotationPoint(-1F, 8F, 0F);
		Handle.setTextureSize(64, 32);
		setRotation(Handle, 0F, 0F, 0F);
		
		MiddleConnection = new ModelRenderer(this, 14, 0);
		MiddleConnection.addBox(0F, 0F, 0F, 4, 1, 1);
		MiddleConnection.setRotationPoint(-2F, 7F, -1F);
		MiddleConnection.setTextureSize(64, 32);
		setRotation(MiddleConnection, 0F, 0F, 0F);
		
		BowRight = new ModelRenderer(this, 14, 2);
		BowRight.addBox(0F, 0F, 0F, 8, 1, 1);
		BowRight.setRotationPoint(2F, 7F, -1F);
		BowRight.setTextureSize(64, 32);
		setRotation(BowRight, 0F, 0F, 0.5235988F);
		
		BowLeft = new ModelRenderer(this, 14, 2);
		BowLeft.addBox(-8F, 0F, 0F, 8, 1, 1);
		BowLeft.setRotationPoint(-2F, 7F, -1F);
		BowLeft.setTextureSize(64, 32);
		setRotation(BowLeft, 0F, 0F, -0.5235988F);
		
		Hook = new ModelRenderer(this, 0, 18);
		Hook.addBox(0F, 0F, 0F, 1, 1, 1);
		Hook.setRotationPoint(-0.5F, 17F, -1F);
		Hook.setTextureSize(64, 32);
		setRotation(Hook, 0F, 0F, 0F);
		
		Trigger = new ModelRenderer(this, 8, 0);
		Trigger.addBox(0F, 0F, 0F, 1, 10, 2);
		Trigger.setRotationPoint(-0.5F, 13F, 2F);
		Trigger.setTextureSize(64, 32);
		setRotation(Trigger, 0F, 0F, 0F);
		
		StringLeft = new ModelRenderer(this, 14, 4);
		StringLeft.addBox(0F, 0F, 0F, 8, 1, 1);
		StringLeft.setRotationPoint(-8F, 11F, -1F);
		StringLeft.setTextureSize(64, 32);
		setRotation(StringLeft, 0F, 0F, 0F);
		
		StringRight = new ModelRenderer(this, 14, 4);
		StringRight.addBox(-8F, 0F, 0F, 8, 1, 1);
		StringRight.setRotationPoint(8F, 11F, -1F);
		StringRight.setTextureSize(64, 32);
		setRotation(StringRight, 0F, 0F, 0F);
	}
	
	public void setStage(float stagePercent)
	{
		float bowAngle = 0.5235988F + (0.3F * stagePercent);
		float stringAngle = (0.6F * stagePercent);
		
		setRotation(BowRight, 0F, 0F, bowAngle);
		setRotation(BowLeft, 0F, 0F, bowAngle * -1);
		
		setRotation(StringRight, 0F, 0F, stringAngle * -1);
		setRotation(StringLeft, 0F, 0F, stringAngle);
		
		StringRight.offsetX = -0.1F * stagePercent;
		StringLeft.offsetX = 0.1F * stagePercent;
		
		StringRight.offsetY = 0.1F * stagePercent;
		StringLeft.offsetY = 0.1F * stagePercent;
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
		Handle.render(f5);
		MiddleConnection.render(f5);
		BowRight.render(f5);
		BowLeft.render(f5);
		Hook.render(f5);
		Trigger.render(f5);
		StringLeft.render(f5);
		StringRight.render(f5);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}