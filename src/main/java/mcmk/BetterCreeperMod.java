package mcmk;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(BetterCreeperMod.MOD_ID)
public class BetterCreeperMod {
	
	public static final String MOD_ID = "bettercreeper";
	//public static final String NAME = "Better Creeper Mod";
	//public static final String VERSION = "1.1.0";
    //private static final Logger LOGGER = LogManager.getLogger();

    public BetterCreeperMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("bettercreeper-common.toml"));
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onExplosionDetonate(ExplosionEvent.Detonate event)
    {
    	//LOGGER.info("onExplosionDetonate");
    	Explosion explosion = event.getExplosion();
    	
    	//check if creeper explodes
        if (explosion.getExploder() instanceof Creeper)
        {
        	
        	if (Config.DISABLE_BLOCK_DAMAGE.get()) {
        		//clear block damage
        		explosion.clearToBlow();
        	}
        	
        	if (Config.DISABLE_ITEM_DAMAGE.get()) {
        		//prevent item damage
        		this.removeItemEntities(event.getAffectedEntities());
        	}
      	
        	Level world = event.getWorld();
        	
        	if (world.isClientSide() == false) {
        		//LOGGER.info("Server World");
	        	
	        	BlockPos blockPos = explosion.getExploder().blockPosition();
	        	BlockState blockStateDown = world.getBlockState(blockPos.below());
	        	BlockState blockState = world.getBlockState(blockPos);
	            Block flower = getRandomFlowerBlock();
	            
	            //check if flower can placed
	        	if( blockState.getBlock() == Blocks.AIR && (
	        		blockStateDown.getBlock() == Blocks.GRASS_BLOCK ||
	        		blockStateDown.getBlock() == Blocks.DIRT ||
	        		blockStateDown.getBlock() == Blocks.COARSE_DIRT )) {
	        		//place flower
	                BlockPos blockPosition = explosion.getExploder().blockPosition();                     
	                world.setBlockAndUpdate(blockPosition, flower.defaultBlockState());
	        	}else {
	        		//instead drop it
	        		explosion.getExploder().spawnAtLocation(flower);
	        	}
        	
        	//}else {
        		//LOGGER.info("Client World");
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
    
    /*
    private static boolean setBlock(Level world, BlockPos pos, BlockState blockState) {
        if (world.isOutsideBuildHeight(pos)) {
            return false;
        } else {
            LevelChunk chunk = world.getChunkAt(pos);
            world.markAndNotifyBlock(pos, chunk, blockState, blockState, 2, 3);
            return true;
        }
    }*/
}
