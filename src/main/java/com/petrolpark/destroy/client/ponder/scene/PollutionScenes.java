package com.petrolpark.destroy.client.ponder.scene;

import com.petrolpark.destroy.block.entity.VatControllerBlockEntity;
import com.petrolpark.destroy.block.entity.VatSideBlockEntity;
import com.petrolpark.destroy.block.entity.VatSideBlockEntity.DisplayType;
import com.petrolpark.destroy.capability.Pollution.PollutionType;
import com.petrolpark.destroy.chemistry.Mixture;
import com.petrolpark.destroy.chemistry.index.DestroyMolecules;
import com.petrolpark.destroy.client.particle.DestroyParticleTypes;
import com.petrolpark.destroy.client.particle.data.GasParticleData;
import com.petrolpark.destroy.client.ponder.PonderPlayer;
import com.petrolpark.destroy.client.ponder.instruction.AdvanceTimeOfDayInstruction;
import com.petrolpark.destroy.client.ponder.instruction.CreateFishingHookInstruction;
import com.petrolpark.destroy.client.ponder.instruction.SmogInstruction;
import com.petrolpark.destroy.client.ponder.particle.DestroyEmitters;
import com.petrolpark.destroy.fluid.MixtureFluid;
import com.petrolpark.destroy.item.DestroyItems;
import com.petrolpark.destroy.util.PollutionHelper;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.element.EntityElement;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.ponder.instruction.EmitParticlesInstruction.Emitter;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Pointing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PollutionScenes {

    private static final Emitter emitter = Emitter.simple(new GasParticleData(DestroyParticleTypes.EVAPORATION.get(), MixtureFluid.of(100, Mixture.pure(DestroyMolecules.NITROGEN_DIOXIDE))), new Vec3(0d, 0.05d, 0d));
    
    public static final void pipesAndTanks(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pollution.tanks", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        
        BlockPos tank = util.grid.at(3, 1, 2);
        Vec3 pipeTop = util.vector.topOf(1, 2, 2);

        scene.idle(10);
        scene.world.showSection(util.select.position(5, 0, 2), Direction.WEST);
        scene.idle(10);
        scene.world.showSection(util.select.fromTo(2, 1, 3, 5, 1, 3), Direction.NORTH);
        scene.idle(10);
        scene.world.showSection(util.select.fromTo(1, 1, 2, 3, 2, 2), Direction.DOWN);
        scene.idle(10);

        scene.world.propagatePipeChange(util.grid.at(2, 1, 2));
        scene.overlay.showText(100)
            .text("This text is defined in a language file.")
            .pointAt(pipeTop)
            .attachKeyFrame();
        for (int i = 0; i < 6; i ++) {
            scene.effects.emitParticles(pipeTop, emitter, 1f, 1);
            scene.idle(20);
        };

        scene.overlay.showText(100)
            .text("This text is defined in a language file.")
            .pointAt(util.vector.blockSurface(tank, Direction.NORTH))
            .attachKeyFrame();
        scene.overlay.showOutline(PonderPalette.RED, "crude_oil_tank", util.select.position(tank), 100);
        scene.idle(60);
        scene.world.destroyBlock(tank);
        scene.world.propagatePipeChange(util.grid.at(2, 1, 2));
        scene.effects.emitParticles(util.vector.centerOf(tank), emitter, 4f, 1);
        scene.idle(60);

        scene.overlay.showText(100)
            .text("This text is defined in a language file.")
            .independent();
        scene.idle(100);

        scene.markAsFinished();
    };

    public static final void basinsAndVats(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pollution.basins_and_vats", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 9);
        scene.scaleSceneView(0.5f);
        scene.showBasePlate();

        BlockPos mixer = util.grid.at(1, 3, 2);
        BlockPos basin = util.grid.at(1, 1, 2);
        BlockPos vatTop = util.grid.at(5, 4, 4);
        BlockPos lever = util.grid.at(5, 4, 2);

        scene.idle(10);
        scene.world.showSection(util.select.fromTo(1, 0, 9, 2, 2, 9), Direction.NORTH);
        scene.idle(10);
        for (int i = 8; i >= 3; i--) {
            scene.world.showSection(util.select.position(1, 2, i), Direction.DOWN);
            scene.idle(3);
        };
        scene.world.showSection(util.select.position(1, 3, 3), Direction.DOWN);
        scene.idle(10);
        scene.world.showSection(util.select.position(mixer), Direction.DOWN);
        scene.idle(10);
        scene.world.showSection(util.select.position(basin), Direction.SOUTH);
        scene.idle(10);

        scene.overlay.showText(100)
            .text("This text is defined in a language file.")
            .pointAt(util.vector.blockSurface(basin, Direction.SOUTH))
            .attachKeyFrame();
        scene.idle(10);
        scene.world.modifyBlockEntity(mixer, MechanicalMixerBlockEntity.class, be -> be.startProcessingBasin());
        scene.idle(50);
        scene.effects.emitParticles(util.vector.centerOf(basin.above()), emitter, 4f, 1);
        scene.idle(60);

        scene.world.showSection(util.select.fromTo(4, 1, 3, 7, 4, 6), Direction.DOWN);
        scene.idle(10);

        scene.overlay.showText(160)
            .text("This text is defined in a language file.")
            .pointAt(util.vector.topOf(5, 4, 4))
            .attachKeyFrame();
        scene.idle(20);
        scene.overlay.showControls(
            new InputWindowElement(util.vector.topOf(vatTop), Pointing.DOWN)
                .rightClick()
                .withWrench(), 
        80);
        scene.world.modifyBlockEntity(vatTop, VatSideBlockEntity.class, be -> be.setDisplayType(DisplayType.OPEN_VENT));
        scene.idle(100);
        scene.effects.emitParticles(util.vector.centerOf(vatTop.above()), emitter, 4f, 1);
        scene.world.modifyBlockEntity(util.grid.at(5, 2, 3), VatControllerBlockEntity.class, VatControllerBlockEntity::flush);
        scene.idle(60);

        scene.overlay.showText(100)
            .text("This text is defined in a language file.")
            .pointAt(util.vector.topOf(vatTop));
        scene.idle(20);
        scene.world.showSection(util.select.position(lever), Direction.SOUTH);
        scene.idle(40);
        scene.world.toggleRedstonePower(util.select.position(lever));
        scene.effects.indicateRedstone(lever);
        scene.world.modifyBlockEntity(vatTop, VatSideBlockEntity.class, be -> be.setDisplayType(DisplayType.CLOSED_VENT));
        scene.idle(60);

        scene.markAsFinished();
    };

    public static final void smog(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pollution.smog", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.everywhere(), Direction.UP);

        scene.idle(10);
        scene.overlay.showText(200)
            .text("This text is defined in a language file.")
            .independent();
        for (int i = 0; i < 200; i++) {
            scene.idle(1);
            scene.addInstruction(new SmogInstruction(i * PollutionType.SMOG.max / 200));
        };
        scene.markAsFinished();
    };

    private static final RandomSource rand = new XoroshiroRandomSource(0);

    private static final void growthSuccessParticles(SceneBuilder scene, SceneBuildingUtil util, BlockPos pos) {
        scene.effects.emitParticles(util.vector.blockSurface(pos, Direction.DOWN).add(0, 0.25f, 0), Emitter.withinBlockSpace(ParticleTypes.HAPPY_VILLAGER, Vec3.ZERO), 10f, 1);
    };

    private static final void growthFailureParticles(SceneBuilder scene, SceneBuildingUtil util, BlockPos pos) {
        scene.effects.emitParticles(util.vector.blockSurface(pos, Direction.DOWN).add(0, 0.25f, 0), Emitter.withinBlockSpace(PollutionHelper.cropGrowthFailureParticles(), Vec3.ZERO), 10f, 1);
    };
    public static final void cropGrowthFailure(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pollution.crop_growth_failure", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.fromTo(0, 0, 0, 4, 1, 4).substract(util.select.position(3, 1, 4)), Direction.UP);

        BlockPos clock = util.grid.at(3, 2, 4);
        BlockPos farmland = util.grid.at(0, 0, 1);

        scene.idle(5);
        scene.world.showSection(util.select.position(3, 1, 4), Direction.DOWN);
        scene.idle(5);
        scene.world.showSection(util.select.position(clock), Direction.DOWN);
        scene.idle(5);
        scene.world.showSection(util.select.position(1, 0, 5), Direction.NORTH);
        scene.idle(5);
        scene.world.showSection(util.select.position(2, 1, 5), Direction.NORTH);
        scene.idle(5);
        scene.world.showSection(util.select.position(3, 2, 5), Direction.NORTH);
        scene.addInstruction(new AdvanceTimeOfDayInstruction(235, 1000));
        scene.idle(15);

        scene.overlay.showText(200)
            .text("This text is defined in a language file.")
            .independent();
        for (int i = 0; i < 10; i++) {
            scene.idle(20);
            BlockPos pos = util.grid.at(2 * rand.nextInt(3), 1, rand.nextInt(5));
            if (pos.equals(clock.below())) continue;
            if (rand.nextInt(3) == 0) {
                growthSuccessParticles(scene, util, pos);
                scene.world.cycleBlockProperty(pos, CropBlock.AGE);
            } else {
                growthFailureParticles(scene, util, pos);
            };
        };
        scene.idle(40);

        scene.overlay.showText(100)
            .text("This text is defined in a language file.")
            .pointAt(util.vector.blockSurface(farmland, Direction.WEST))
            .attachKeyFrame();
        scene.idle(60);
        scene.overlay.showControls(
            new InputWindowElement(util.vector.blockSurface(farmland, Direction.UP), Pointing.RIGHT)
            .rightClick()
            .withItem(new ItemStack(Items.BONE_MEAL)),
        40);
        scene.idle(10);
        growthFailureParticles(scene, util, farmland.above());
        scene.idle(50);

        scene.overlay.showText(100)
            .text("This text is defined in a language file.")
            .pointAt(util.vector.blockSurface(farmland, Direction.WEST));
        scene.idle(60);
        scene.overlay.showControls(
            new InputWindowElement(util.vector.blockSurface(farmland, Direction.UP), Pointing.RIGHT)
            .rightClick()
            .withItem(DestroyItems.HYPERACCUMULATING_FERTILIZER.asStack()),
        40);
        scene.idle(10);
        growthSuccessParticles(scene, util, farmland.above());
        scene.world.setBlock(farmland.above(), Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, CropBlock.MAX_AGE), false);
        scene.idle(50);

        scene.markAsFinished();

    };

    public static final void fishingFailure(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pollution.fishing_failure", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.addInstruction(new SmogInstruction(3 * PollutionType.SMOG.max / 4));
        scene.world.showSection(util.select.everywhere(), Direction.UP);

        BlockPos playerPos = util.grid.at(4, 2, 2);
        ItemStack rod = new ItemStack(Items.FISHING_ROD);

        scene.idle(10);
        scene.overlay.showText(140)
            .text("This text is defined in a language file.")
            .independent();
        scene.idle(20);

        ElementLink<EntityElement> playerElement = scene.world.createEntity(w -> {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer localPlayer = minecraft.player;
            if (localPlayer == null) return null;
            PonderPlayer player = new PonderPlayer(w, localPlayer.getScoreboardName());
            Vec3 v = util.vector.topOf(playerPos.below());
            player.setPos(v.x, v.y, v.z);
            player.xo = v.x;
            player.yo = v.y;
            player.zo = v.z;
            player.yBodyRot = player.yBodyRotO = player.yHeadRot = player.yHeadRotO = player.yRotO = 90;
            player.setYRot(90);
            player.xRotO = 35;
            player.setXRot(35);
            player.setInvisible(true);
            return player;
        });

        scene.world.modifyEntity(playerElement, entity -> {
            if (!(entity instanceof PonderPlayer player)) return;
            player.setItemInHand(InteractionHand.MAIN_HAND, rod);
            player.setInvisible(false);
        });

        scene.idle(20);

        ElementLink<EntityElement> hookElement = CreateFishingHookInstruction.add(scene, playerElement);
        scene.idle(20);
        scene.world.modifyEntity(hookElement, entity -> {
            if (!(entity instanceof FishingHook hook)) return;
            Vec3 v = new Vec3(0.259812f, 1.922581f, 2.5f);
            hook.setPos(v);
            hook.xo = v.x;
            hook.yo = v.y;
            hook.zo = v.z;
            hook.setDeltaMovement(Vec3.ZERO);
        });
        scene.idle(30);

        scene.overlay.showControls(
            new InputWindowElement(util.vector.topOf(0, 1, 2), Pointing.DOWN)
            .withItem(new ItemStack(Items.DIRT)),
        50);
        scene.idle(70);

        scene.markAsFinished();
    };

    public static final void breedingFailure(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pollution.breeding_failure", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 3);
        scene.showBasePlate();

        scene.world.createEntity(w -> {
            Sheep sheep = EntityType.SHEEP.create(w);
            Vec3 v = util.vector.of(2.35d, 1d, 1.5d);
            sheep.setPos(v.x, v.y, v.z);
            sheep.xo = v.x;
            sheep.yo = v.y;
            sheep.zo = v.z;
            sheep.yBodyRot = sheep.yBodyRotO = sheep.yHeadRot = sheep.yHeadRotO = sheep.yRotO = 90;
            sheep.setYRot(90);
            return sheep;
        });

        scene.world.createEntity(w -> {
            Sheep sheep = EntityType.SHEEP.create(w);
            Vec3 v = util.vector.of(0.65d, 1d, 1.5d);
            sheep.setPos(v.x, v.y, v.z);
            sheep.xo = v.x;
            sheep.yo = v.y;
            sheep.zo = v.z;
            sheep.yBodyRot = sheep.yBodyRotO = sheep.yHeadRot = sheep.yHeadRotO = sheep.yRotO = 270;
            sheep.setYRot(270);
            return sheep;
        });

        scene.idle(10);
        scene.overlay.showText(160)
            .text("This text is defined in a language file.")
            .independent();
        scene.idle(20);

        ItemStack wheat = new ItemStack(Items.WHEAT);

        scene.overlay.showControls(
            new InputWindowElement(util.vector.of(0.5d, 2.5d, 1.5d), Pointing.DOWN)
            .rightClick()
            .withItem(wheat)
        , 40);
        scene.idle(50);
        scene.overlay.showControls(
            new InputWindowElement(util.vector.of(2.5d, 2.5d, 1.5d), Pointing.DOWN)
            .rightClick()
            .withItem(wheat)
        , 40);
        scene.idle(70);

        scene.effects.emitParticles(util.vector.of(1.5d, 2.2d, 1.5d), Emitter.withinBlockSpace(ParticleTypes.ANGRY_VILLAGER, Vec3.ZERO), 10f, 1);
        scene.idle(40);

        scene.markAsFinished();
    };

    public static final void villagerPriceIncrease(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pollution.villager_price_increase", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.everywhere(), Direction.UP);

        scene.idle(10);
        scene.world.createEntity(w -> {
            Villager villager = EntityType.VILLAGER.create(w);
            villager.assignProfessionWhenSpawned();
            Vec3 v = util.vector.of(2.5d, 2d, 3.5d);
            villager.setPos(v.x, v.y, v.z);
            villager.xo = v.x;
            villager.yo = v.y;
            villager.zo = v.z;
            villager.yBodyRot = villager.yBodyRotO = villager.yHeadRot = villager.yHeadRotO = villager.yRotO = 180;
            villager.setYRot(180);
            return villager;
        });

        scene.idle(10);
        scene.overlay.showText(100)
            .text("This text is defined in a language file.")
            .independent();
        for (int i = 0; i < 100; i++) {
            scene.idle(1);
            scene.addInstruction(new SmogInstruction(i * PollutionType.SMOG.max / 100));
            if (i == 50) scene.effects.emitParticles(util.vector.of(2.5d, 3.8d, 3.5d), Emitter.withinBlockSpace(ParticleTypes.ANGRY_VILLAGER, Vec3.ZERO), 10f, 1);
        };
        scene.idle(20);
        scene.markAsFinished();
    };

    public static final void cancer(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pollution.cancer", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.everywhere(), Direction.UP);

        scene.idle(10);
        scene.world.createEntity(w -> {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer localPlayer = minecraft.player;
            if (localPlayer == null) return null;
            PonderPlayer player = new PonderPlayer(w, localPlayer.getScoreboardName());
            Vec3 v = util.vector.topOf(2, 0, 2);
            player.setPos(v.x, v.y, v.z);
            player.xo = v.x;
            player.yo = v.y;
            player.zo = v.z;
            player.yBodyRot = player.yBodyRotO = player.yHeadRot = player.yHeadRotO = player.yRotO = 180;
            player.setYRot(180);
            return player;
        });

        scene.overlay.showText(80)
            .text("This text is defined in a language file.")
            .independent();
        scene.idle(100);
        scene.overlay.showText(80)
            .text("This text is defined in a language file.")
            .independent();
        scene.idle(100);
        scene.overlay.showText(100)
            .text("This text is defined in a language file.")
            .independent();
        scene.idle(50);
        scene.overlay.showControls(
            new InputWindowElement(util.vector.topOf(2, 2, 2), Pointing.DOWN)
            .withItem(DestroyItems.SUNSCREEN_BOTTLE.asStack()),
            50
        );
        scene.idle(70);
        scene.markAsFinished();
    };

    public static final void acidRain(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pollution.acid_rain", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.scaleSceneView(0.75f);
        scene.world.showSection(util.select.everywhere(), Direction.UP);

        scene.overlay.showText(200)
            .text("This text is defined in a language file.")
            .independent();
        for (int i = 0; i < 10; i++) {
            Color color = new Color(Color.mixColors(0xFF3E5EB8, 0xFF00FF00, (float)i / 10f));
            scene.effects.emitParticles(Vec3.ZERO, DestroyEmitters.rain(new AABB(0d, 11d, 0d, 5d, 12d, 5d), color.getRedAsFloat(), color.getGreenAsFloat(), color.getBlueAsFloat()), 100f, 20);
            scene.idle(20);
        };
        scene.effects.emitParticles(Vec3.ZERO, DestroyEmitters.rain(new AABB(0d, 11d, 0d, 5d, 12d, 5d), 0d, 1d, 0d), 100f, 240);
        scene.idle(20);
        scene.overlay.showText(200)
            .text("This text is defined in a language file.")
            .attachKeyFrame()
            .independent();

        scene.idle(20);
        scene.world.destroyBlock(util.grid.at(0, 2, 1));
        scene.world.destroyBlock(util.grid.at(0, 3, 1));
        scene.idle(20);
        scene.world.destroyBlock(util.grid.at(1, 7, 2));
        scene.idle(20);
        scene.world.setBlock(util.grid.at(1, 1, 0), Blocks.DIRT.defaultBlockState(), true);
        scene.idle(20);
        scene.world.setBlock(util.grid.at(0, 1, 4), Blocks.DIRT.defaultBlockState(), true);
        scene.idle(20);
        scene.world.destroyBlock(util.grid.at(2, 2, 0));
        scene.idle(20);
        scene.world.destroyBlock(util.grid.at(3, 7, 2));
        scene.idle(20);
        scene.world.setBlock(util.grid.at(0, 1, 1), Blocks.DIRT.defaultBlockState(), true);
        scene.idle(20);
        scene.world.destroyBlock(util.grid.at(1, 7, 3));
        scene.idle(20);
        scene.world.destroyBlock(util.grid.at(0, 2, 0));
        scene.idle(40);

        scene.markAsFinished();
    };

    public static final void reduction(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pollution.reduction", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 5);
        scene.addInstruction(new SmogInstruction(PollutionType.SMOG.max));
        scene.world.showSection(util.select.fromTo(0, 0, 0, 4, 2, 4), Direction.UP);

        BlockPos sapling = util.grid.at(2, 2, 2);

        scene.overlay.showText(100)
            .text("This text is defined in a language file.")
            .independent();
        for (int i = 200; i >= 100; i--) {
            scene.addInstruction(new SmogInstruction(i * PollutionType.SMOG.max / 200));
            Color color = new Color(Color.mixColors(0xFF3E5EB8,0xFF00FF00,  (float)i / 200f));
            scene.effects.emitParticles(Vec3.ZERO, DestroyEmitters.rain(new AABB(0d, 11d, 0d, 5d, 12d, 5d), color.getRedAsFloat(), color.getGreenAsFloat(), color.getBlueAsFloat()), 100f, 1);
            scene.idle(1);
        };
        scene.effects.emitParticles(Vec3.ZERO, DestroyEmitters.rain(new AABB(0d, 11d, 0d, 5d, 12d, 5d), 31 / 256f, 174 / 256f, 92 / 256f), 100f, 50);
        scene.idle(20);
        scene.overlay.showText(140)
            .text("This text is defined in a language file.")
            .independent()
            .attachKeyFrame();
        scene.idle(20);
        scene.overlay.showControls(
            new InputWindowElement(util.vector.blockSurface(sapling, Direction.WEST), Pointing.LEFT)
            .rightClick()
            .withItem(new ItemStack(Items.BONE_MEAL)),
        40
        );
        scene.idle(10);
        ElementLink<WorldSectionElement> tree = scene.world.showIndependentSection(util.select.fromTo(0, 3, 0, 4, 10, 4), Direction.DOWN, 0);
        scene.world.moveSection(tree, util.vector.of(0d, -1d, 0d), 0);
        growthSuccessParticles(scene, util, sapling);

        for (int i = 100; i >= 0; i--) {
            scene.addInstruction(new SmogInstruction(i * PollutionType.SMOG.max / 200));
            Color color = new Color(Color.mixColors(0xFF3E5EB8,0xFF00FF00,  (float)i / 200f));
            scene.effects.emitParticles(Vec3.ZERO, DestroyEmitters.rain(new AABB(0d, 11d, 0d, 5d, 12d, 5d), color.getRedAsFloat(), color.getGreenAsFloat(), color.getBlueAsFloat()), 100f, 1);
            scene.idle(1);
        };
        scene.markAsFinished();
    };

    public static final void lightning(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pollution.lightning", "This text is defined in a language file.");
        scene.configureBasePlate(0, 0, 3);
        scene.showBasePlate();

        scene.effects.emitParticles(Vec3.ZERO, DestroyEmitters.rain(new AABB(0d, 10d, 0d, 3d, 11d, 3d), 0f, 0f, 1f), 20, 120);

        scene.idle(20);
        scene.overlay.showText(100)
            .text("This text is defined in a language file.")
            .independent();
        scene.idle(40);

        scene.world.createEntity(w -> {
            ThrownTrident trident = EntityType.TRIDENT.create(w);
            Vec3 p = new Vec3(10.5d, 2.5d, 1.5d);
            trident.setPos(p);
            trident.xo = p.x;
            trident.yo = p.y;
            trident.zo = p.z;
            trident.setDeltaMovement(-1.1d, 0d, 0d);
            return trident;
        });

        scene.idle(10);

        scene.world.createEntity(w -> {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(w);
            bolt.setPos(new Vec3(1.5d, 1d, 1.5d));
            return bolt;
        });

        scene.idle(70);

    };

    public static final void catalyticConverter(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pollution.catalytic_converter", "This text is defined in a language file.");

    };


};
