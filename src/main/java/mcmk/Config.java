package mcmk;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = BetterCreeperMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

	public static final String CATEGORY_GENERAL = "General configuration";
	public static final String CATEGORY_SETTINGS = "Better Creeper Mod";
	
	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	
	public static ForgeConfigSpec COMMON_CONFIG;
	public static ForgeConfigSpec.BooleanValue DISABLE_BLOCK_DAMAGE;
	public static ForgeConfigSpec.BooleanValue DISABLE_ITEM_DAMAGE;
	public static ForgeConfigSpec.BooleanValue ENABLE_FLOWER_SPAWN;
	
	
	static {
		
		COMMON_BUILDER.comment("General configuration").push(CATEGORY_GENERAL);
		COMMON_BUILDER.pop();
		
		setupFirstBlockConfig();
		
		COMMON_CONFIG = COMMON_BUILDER.build();
	}
	
	private static void setupFirstBlockConfig() {
		
		COMMON_BUILDER.comment("Mod settings").push(CATEGORY_SETTINGS);
	
		DISABLE_BLOCK_DAMAGE = COMMON_BUILDER.comment("Disable creepers block damage")
				.define("DisableBlockDamage", true);
		
		DISABLE_ITEM_DAMAGE = COMMON_BUILDER.comment("Disable creepers item damage")
				.define("DisableItemDamage", true);
		
		ENABLE_FLOWER_SPAWN = COMMON_BUILDER.comment("Enable creepers to spwan flowers")
				.define("FlowerSpawn", false);
			
		COMMON_BUILDER.pop();
	}
	
	public static void loadConfig(ForgeConfigSpec spec, Path path){
	
		final CommentedFileConfig configData = CommentedFileConfig.builder(path)
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();
		
		configData.load();
		spec.setConfig(configData);
	}
	
	
}
