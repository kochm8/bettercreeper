package mcmk;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import java.nio.file.Path;

public class Config {

		public static final String CATEGORY_GENERAL = "General configuration";
	public static final String CATEGORY_SETTINGS = "Better Creeper Mod";
	
	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	
	public static ForgeConfigSpec COMMON_CONFIG;
	public static ForgeConfigSpec.BooleanValue DISABLE_BLOCK_DAMAGE;
	public static ForgeConfigSpec.BooleanValue DISABLE_ITEM_DAMAGE;
	public static ForgeConfigSpec.BooleanValue DISABLE_PLAYER_DAMAGE;
	
	
	static {
		
		COMMON_BUILDER.comment("General configuration").push(CATEGORY_GENERAL);
		COMMON_BUILDER.pop();
		
		setupFirstBlockConfig();
		
		COMMON_CONFIG = COMMON_BUILDER.build();
	}
	
	private static void setupFirstBlockConfig() {
		
		COMMON_BUILDER.comment("Creeper damage settings").push(CATEGORY_SETTINGS);
	
		DISABLE_BLOCK_DAMAGE = COMMON_BUILDER.comment("Disable creepers block damage")
				.define("DisableBlockDamage", true);
		
		DISABLE_ITEM_DAMAGE = COMMON_BUILDER.comment("Disable creepers item damage")
				.define("DisableItemDamage", true);
		
		//DISABLE_PLAYER_DAMAGE = COMMON_BUILDER.comment("Disable creepers player damage")
		//		.define("DisablePlayerDamage", false);
			
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
