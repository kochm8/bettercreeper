package mcmk;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
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
	//public static final String VERSION = "1.0.0";

    private static final Logger LOGGER = LogManager.getLogger();

    public BetterCreeperMod() {
    	
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("examplemod-common.toml"));
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onExplosionDetonate(ExplosionEvent.Detonate event)
    {
    	LOGGER.info("onExplosionDetonate");
    	Explosion explosion = event.getExplosion();

        if (explosion.getExplosivePlacedBy() instanceof CreeperEntity)
        {
        	
        	if (Config.DISABLE_BLOCK_DAMAGE.get()) {
        		explosion.clearAffectedBlockPositions();
        	}
        	
        	if (Config.DISABLE_ITEM_DAMAGE.get()) {
        		this.removeItemEntities(event.getAffectedEntities());
        	}
      	
        	World world = event.getWorld();
        	
        	if (world.isRemote == false) {
        		//LOGGER.info("Server World");
	        	
	        	BlockPos blockPos = explosion.getExploder().getPosition();
	        	BlockState blockStateDown = world.getBlockState(blockPos.down());
	        	BlockState blockState = world.getBlockState(blockPos);
	            Block flower = getRandomFlowerBlock();
	                        
	            //check if block can placed
	        	if( blockState.getBlock() == Blocks.AIR && blockStateDown.getBlock() != Blocks.AIR ) {
	        		//place flower
	                BlockPos blockPosition = explosion.getExploder().getPosition();               
	                world.setBlockState(blockPosition, flower.getDefaultState());
	        	}else {
	        		//instead drop it
	        		explosion.getExploder().entityDropItem(flower);
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

}
