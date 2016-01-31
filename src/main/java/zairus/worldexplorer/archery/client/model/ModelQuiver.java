package zairus.worldexplorer.archery.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelQuiver
	extends ModelBase
{
	ModelRenderer QuiverBody;
	ModelRenderer QuiverBottom;
	ModelRenderer QuiverTop;
	
	public ModelQuiver()
	{
		textureWidth = 64;
		textureHeight = 32;
		
		QuiverBody = new ModelRenderer(this, 0, 5);
		QuiverBody.addBox(0F, -4F, 0F, 4, 6, 2);
		QuiverBody.setRotationPoint(-2F, 6F, 2F);
		QuiverBody.setTextureSize(64, 32);
		setRotation(QuiverBody, 0F, 0F, 0F);
		
		QuiverBottom = new ModelRenderer(this, 0, 13);
		QuiverBottom.addBox(0F, 0F, 0F, 3, 2, 2);
		QuiverBottom.setRotationPoint(-1.5F, 8F, 2F);
		QuiverBottom.setTextureSize(64, 32);
		setRotation(QuiverBottom, 0F, 0F, 0F);
		
		QuiverTop = new ModelRenderer(this, 0, 0);
		QuiverTop.addBox(0F, 0F, 0F, 5, 2, 3);
		QuiverTop.setRotationPoint(-2.5F, 1F, 1.5F);
		QuiverTop.setTextureSize(64, 32);
		setRotation(QuiverTop, 0F, 0F, 0F);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		QuiverBody.render(f5);
		QuiverBottom.render(f5);
		QuiverTop.render(f5);
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
