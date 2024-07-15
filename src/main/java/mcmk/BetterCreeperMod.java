package mcmk;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(BetterCreeperMod.MODID)
public class BetterCreeperMod {
	
	public static final String MODID = "bettercreeper";

    public BetterCreeperMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("bettercreeper-common.toml"));
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onExplosionDetonate(ExplosionEvent.Detonate event)
    {
    	Explosion explosion = event.getExplosion();
    
    	//check if creeper explodes
    	
        if (explosion.getDirectSourceEntity() instanceof Creeper)
        {
        	
        	if (Config.DISABLE_BLOCK_DAMAGE.get()) {
        		//clear block damage
        		explosion.clearToBlow();
        	}
        	
        	if (Config.DISABLE_ITEM_DAMAGE.get()) {
        		//prevent item damage
        		this.removeItemEntities(event.getAffectedEntities());
        	}
        	
        	if (Config.ENABLE_FLOWER_SPAWN.get()) {
        		//Spawn flower
            	Level world = event.getLevel();
            	
            	if (world.isClientSide() == false) {        	
    	        	BlockPos blockPos = explosion.getDirectSourceEntity().blockPosition();
    	        	BlockState blockStateDown = world.getBlockState(blockPos.below());
    	        	BlockState blockState = world.getBlockState(blockPos);
    	            Block flower = this.getRandomFlowerBlock();
    	                        
    	            //check if flower can placed
    	        	if( blockState.getBlock() == Blocks.AIR && (
    	        		blockStateDown.getBlock() == Blocks.GRASS_BLOCK ||
    	        		blockStateDown.getBlock() == Blocks.DIRT ||
    	        		blockStateDown.getBlock() == Blocks.COARSE_DIRT ||
    	        		blockStateDown.getBlock() == Blocks.MUD ||
    	        		blockStateDown.getBlock() == Blocks.MUDDY_MANGROVE_ROOTS)) {
    	        		//place flower
    	                BlockPos blockPosition = explosion.getDirectSourceEntity().blockPosition();                     
    	                world.setBlockAndUpdate(blockPosition, flower.defaultBlockState());
    	        	}else {
    	        		//instead drop it
    	        		explosion.getDirectSourceEntity().spawnAtLocation(flower);
    	        	}
            	}
        	}
        } 
        
    }
    
    private Block getRandomFlowerBlock() 
    {
		Random random = new Random();
		
		Block[] flowers = {Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, 
				Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP, 
				Blocks.PINK_TULIP, Blocks.WHITE_TULIP, Blocks.OXEYE_DAISY, 
				Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY, Blocks.WITHER_ROSE, 
				/*Blocks.SUNFLOWER,*/ Blocks.LILAC, Blocks.ROSE_BUSH, Blocks.PEONY};

		return flowers[random.nextInt(flowers.length)];
    }
    
    private void removeItemEntities(List<Entity> list)
    {
        Iterator<Entity> iterator = list.iterator();
        while (iterator.hasNext())
        {
            Entity entity = iterator.next();
            if (entity instanceof ItemEntity)
            {
            	iterator.remove();
            }
        }
    }
}
