package me.emii.emysvillagers.rendering;

import me.emii.emysvillagers.HumanoidManager.BodyType;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.world.entity.LivingEntity;

public class EmysHumanoidModel<T extends LivingEntity> extends HumanoidModel<T> {

    public EmysHumanoidModel(ModelPart root) {
        super(root);
    }
    
    public static LayerDefinition createBodyLayer(BodyType type) {
        MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, type == BodyType.FEM);
        return LayerDefinition.create(mesh, 64, 64);
    }
}
