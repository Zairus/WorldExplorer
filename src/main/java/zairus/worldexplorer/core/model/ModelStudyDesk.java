package zairus.worldexplorer.core.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelStudyDesk
	extends ModelBase
{
	ModelRenderer LeftPane;
	ModelRenderer RightPane;
	ModelRenderer RearPane;
	ModelRenderer Surface;
	ModelRenderer Chest;
	ModelRenderer ChestLock;
	ModelRenderer BookBackCover;
	ModelRenderer BookFrontCover;
	ModelRenderer BookPages;
	
	public ModelStudyDesk()
	{
		textureWidth = 64;
		textureHeight = 96;
		
		LeftPane = new ModelRenderer(this, 0, 35);
		LeftPane.addBox(0F, 0F, 0F, 2, 12, 16);
		LeftPane.setRotationPoint(-8F, 12F, -8F);
		LeftPane.setTextureSize(64, 96);
		//LeftPane.mirror = true;
		setRotation(LeftPane, 0F, 0F, 0F);
		
		RightPane = new ModelRenderer(this, 0, 35);
		RightPane.addBox(0F, 0F, 0F, 2, 12, 16);
		RightPane.setRotationPoint(6F, 12F, -8F);
		RightPane.setTextureSize(64, 96);
		//RightPane.mirror = true;
		setRotation(RightPane, 0F, 0F, 0F);
		
		RearPane = new ModelRenderer(this, 36, 49);
		RearPane.addBox(0F, 0F, 0F, 12, 12, 2);
		RearPane.setRotationPoint(-6F, 12F, 6F);
		RearPane.setTextureSize(64, 96);
		//RearPane.mirror = true;
		setRotation(RearPane, 0F, 0F, 0F);
		
		Surface = new ModelRenderer(this, 0, 17);
		Surface.addBox(0F, 0F, 0F, 16, 2, 16);
		Surface.setRotationPoint(-8F, 10F, -8F);
		Surface.setTextureSize(64, 96);
		//Surface.mirror = true;
		setRotation(Surface, 0F, 0F, 0F);
		
		Chest = new ModelRenderer(this, 0, 63);
		Chest.addBox(0F, 0F, 0F, 10, 10, 10);
		Chest.setRotationPoint(-5F, 14F, -5F);
		Chest.setTextureSize(64, 96);
		//Chest.mirror = true;
		setRotation(Chest, 0F, 0F, 0F);
		
		ChestLock = new ModelRenderer(this, 0, 0);
		ChestLock.addBox(0F, 0F, 0F, 1, 2, 1);
		ChestLock.setRotationPoint(-0.5F, 15.5F, -6F);
		ChestLock.setTextureSize(64, 96);
		//ChestLock.mirror = true;
		setRotation(ChestLock, 0F, 0F, 0F);
		
		BookBackCover = new ModelRenderer(this, 22, 10);
		BookBackCover.addBox(0F, 0F, 0F, 4, 0, 7);
		BookBackCover.setRotationPoint(4F, 9.9F, 1F);
		BookBackCover.setTextureSize(64, 96);
		//BookBackCover.mirror = true;
		setRotation(BookBackCover, 0F, 0F, 0F);
		
		BookFrontCover = new ModelRenderer(this, 0, 10);
		BookFrontCover.addBox(0F, 0F, 0F, 4, 0, 7);
		BookFrontCover.setRotationPoint(4F, 8.9F, 1F);
		BookFrontCover.setTextureSize(64, 96);
		//BookFrontCover.mirror = true;
		setRotation(BookFrontCover, 0F, 0F, 0F);
		
		BookPages = new ModelRenderer(this, 0, 3);
		BookPages.addBox(0F, 0F, 0F, 4, 1, 6);
		BookPages.setRotationPoint(4F, 9F, 1.5F);
		BookPages.setTextureSize(64, 96);
		//BookPages.mirror = true;
		setRotation(BookPages, 0F, 0F, 0F);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
		LeftPane.render(f5);
		RightPane.render(f5);
		RearPane.render(f5);
		Surface.render(f5);
		Chest.render(f5);
		ChestLock.render(f5);
		BookBackCover.render(f5);
		BookFrontCover.render(f5);
		BookPages.render(f5);
	}
	
	public void renderModel(float f5)
	{
		LeftPane.render(f5);
		RightPane.render(f5);
		RearPane.render(f5);
		Surface.render(f5);
		Chest.render(f5);
		ChestLock.render(f5);
		BookBackCover.render(f5);
		BookFrontCover.render(f5);
		BookPages.render(f5);
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