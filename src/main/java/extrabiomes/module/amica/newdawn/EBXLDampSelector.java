package extrabiomes.module.amica.newdawn;

import two.newdawn.API.ChunkInformation;
import two.newdawn.API.NewDawnBiome;
import two.newdawn.API.NewDawnBiomeSelector;
import two.newdawn.API.noise.NoiseStretch;
import two.newdawn.API.noise.SimplexNoise;
import extrabiomes.lib.BiomeSettings;

public class EBXLDampSelector extends NewDawnBiomeSelector {
	protected static final NewDawnBiome biomeExtremeJungle = NewDawnPlugin.getBiomeIfEnabled(BiomeSettings.EXTREMEJUNGLE);
	protected static final NewDawnBiome biomeGreenHills = NewDawnPlugin.getBiomeIfEnabled(BiomeSettings.GREENHILLS);
    protected static final NewDawnBiome biomeGreenSwamp = NewDawnPlugin.getBiomeIfEnabled(BiomeSettings.GREENSWAMP);
    protected static final NewDawnBiome biomeMiniJungle = NewDawnPlugin.getBiomeIfEnabled(BiomeSettings.MINIJUNGLE);
    protected static final NewDawnBiome biomeRainforest = NewDawnPlugin.getBiomeIfEnabled(BiomeSettings.RAINFOREST);
    protected static final NewDawnBiome biomeRedwoodForest = NewDawnPlugin.getBiomeIfEnabled(BiomeSettings.REDWOODFOREST);
    protected static final NewDawnBiome biomeRedwoodLush = NewDawnPlugin.getBiomeIfEnabled(BiomeSettings.REDWOODLUSH);
    protected static final NewDawnBiome biomeSnowRainforest = NewDawnPlugin.getBiomeIfEnabled(BiomeSettings.SNOWYRAINFOREST);
    protected static final NewDawnBiome biomeTemperateRainforest = NewDawnPlugin.getBiomeIfEnabled(BiomeSettings.TEMPORATERAINFOREST);
	
    protected NoiseStretch stretchExtremeJungle;
    protected NoiseStretch stretchGreen;
    protected NoiseStretch stretchMiniJungle;
    protected NoiseStretch stretchRainforest;
    protected NoiseStretch stretchRedwood;
    
	public EBXLDampSelector(SimplexNoise worldNoise, int priority) {
		super(worldNoise, priority);
		
		stretchExtremeJungle = worldNoise.generateNoiseStretcher(384, 384, worldNoise.getRandom().nextDouble(), worldNoise.getRandom().nextDouble());
		stretchGreen = worldNoise.generateNoiseStretcher(512, 512, worldNoise.getRandom().nextDouble(), worldNoise.getRandom().nextDouble());
		stretchMiniJungle = worldNoise.generateNoiseStretcher(128, 128, worldNoise.getRandom().nextDouble(), worldNoise.getRandom().nextDouble());
		stretchRainforest = worldNoise.generateNoiseStretcher(512, 512, worldNoise.getRandom().nextDouble(), worldNoise.getRandom().nextDouble());
		stretchRedwood = worldNoise.generateNoiseStretcher(384, 384, worldNoise.getRandom().nextDouble(), worldNoise.getRandom().nextDouble());	
	}

	@Override
	public NewDawnBiome selectBiome(int blockX, int blockZ,
			ChunkInformation chunkInfo) {
		if( chunkInfo.isBelowGroundLevel(blockX, blockZ) ||
			!chunkInfo.isHumidityWet(blockX, blockZ) ) return null;
		
		final boolean isMountain = chunkInfo.isMountain(blockX, blockZ);
		final boolean isCold = chunkInfo.isTemperatureFreezing(blockX, blockZ);
		final boolean isTemperate = chunkInfo.isTemperatureMedium(blockX, blockZ);
		final boolean isLush = chunkInfo.getAverageHumidity() > 0.75;
		
		if( isMountain ) {
			if( stretchExtremeJungle.getNoise(blockX, blockZ) > 0 )
				return biomeExtremeJungle;
		} else if( stretchGreen.getNoise(blockX, blockZ) > 0 ) {
			if( chunkInfo.isGroundLevelOrShallowWater(blockX, blockZ) )
				return biomeGreenSwamp;
			else
				return biomeGreenHills;
		} else if( stretchRedwood.getNoise(blockX, blockZ) > 0 ) {
			if( isLush )
				return biomeRedwoodLush;
			else
				return biomeRedwoodForest;
		} else if( isLush ) {
			if( stretchMiniJungle.getNoise(blockX, blockZ) > 0 ) {
				return biomeMiniJungle;
			} else if( stretchRainforest.getNoise(blockX, blockZ) > 0 ) {
				if( isCold )
					return biomeSnowRainforest;
				else if( isTemperate )
					return biomeTemperateRainforest;
				else
					return biomeRainforest;
			}
		}
		
		return null;
	}

}
