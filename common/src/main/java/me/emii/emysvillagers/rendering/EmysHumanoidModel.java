package me.emii.emysvillagers.rendering;

import me.emii.emysvillagers.HumanoidManager.BodyType;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class EmysHumanoidModel<T extends LivingEntity> extends HumanoidModel<T> {

    public EmysHumanoidModel(ModelPart root) {
        super(root);
    }
    
    public static LayerDefinition createBodyLayer(BodyType type) {
        MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, type == BodyType.FEM);

        if (type == BodyType.FEM) {
            PartDefinition body = mesh.getRoot().getChild("body");
            body.addOrReplaceChild("chest",
                                    CubeListBuilder.create().texOffs(19, 21).addBox(-4.0f, -3.0f, -1.0f, 8.0f, 3.5f, 1.3f, new CubeDeformation(-0.05f)),
                                    PartPose.offsetAndRotation(0.0f, 5.0f, -2.0f, -0.3491f, 0.0f, 0.0f));
        }

        return LayerDefinition.create(mesh, 64, 64);
    }
}
