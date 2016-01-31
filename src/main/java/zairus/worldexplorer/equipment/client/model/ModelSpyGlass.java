package zairus.worldexplorer.equipment.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSpyGlass extends ModelBase
{
	ModelRenderer eyelid;
	ModelRenderer handle;
	ModelRenderer magnifier;
	
	public ModelSpyGlass()
	{
		textureWidth = 64;
		textureHeight = 32;
		
		eyelid = new ModelRenderer(this, 0, 14);
		eyelid.addBox(0F, 0F, 0F, 1, 3, 1);
		eyelid.setRotationPoint(0F, 21F, 0F);
		eyelid.setTextureSize(64, 32);
		setRotation(eyelid, 0F, 0F, 0F);
		
		handle = new ModelRenderer(this, 0, 8);
		handle.addBox(0F, 0F, 0F, 2, 4, 2);
		handle.setRotationPoint(-0.5F, 17F, -0.5F);
		handle.setTextureSize(64, 32);
		setRotation(handle, 0F, 0F, 0F);
		
		magnifier = new ModelRenderer(this, 0, 0);
		magnifier.addBox(0F, 0F, 0F, 3, 5, 3);
		magnifier.setRotationPoint(-1F, 12F, -1F);
		magnifier.setTextureSize(64, 32);
		magnifier.mirror = true;
		setRotation(magnifier, 0F, 0F, 0F);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		eyelid.render(f5);
		handle.render(f5);
		magnifier.render(f5);
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
