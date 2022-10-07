package com.prayervolumecontrol;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Prayer volume control"
)
public class PrayerVolumeControlPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private PrayerVolumeControlConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Prayer volume started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Prayer volume stopped!");
	}

	@Subscribe
	public void onSoundEffectPlayed(SoundEffectPlayed soundEffectPlayed)
	{
		// Check for prayer sounds
		int soundId = soundEffectPlayed.getSoundId();
		int soundVolume = 100;
		switch (soundId) {
			case SoundEffectID.PRAYER_ACTIVATE_MYSTIC_WILL_AUGURY:
				soundVolume = Math.round(config.auguryMysticWill());
				break;
			case SoundEffectID.PRAYER_ACTIVATE_BURST_OF_STRENGTH:
				soundVolume = config.burstOfStrength();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_CHIVALRY:
				soundVolume = config.chivalry();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_CLARITY_OF_THOUGHT:
				soundVolume = config.clarityOfThought();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_EAGLE_EYE:
				soundVolume = config.eagleEye();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_HAWK_EYE:
				soundVolume = config.hawkEye();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_IMPROVED_REFLEXES:
				soundVolume = config.improvedReflexes();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_INCREDIBLE_REFLEXES:
				soundVolume = config.incredibleReflexes();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_MYSTIC_LORE:
				soundVolume = config.mysticLore();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_MYSTIC_MIGHT:
				soundVolume = config.mysticMight();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_PIETY:
				soundVolume = config.piety();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_RAPID_RESTORE_PRESERVE:
				soundVolume = config.preserveRapidRestore();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_PROTECT_FROM_MAGIC:
				soundVolume = config.protectFromMagic();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_PROTECT_FROM_MELEE:
				soundVolume = config.protectFromMelee();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_PROTECT_FROM_MISSILES:
				soundVolume = config.protectFromMissiles();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_PROTECT_ITEM:
				soundVolume = config.protectItem();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_RAPID_HEAL:
				soundVolume = config.rapidHeal();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_REDEMPTION:
				soundVolume = config.redemption();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_RETRIBUTION:
				soundVolume = config.retribution();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_SHARP_EYE_RIGOUR:
				soundVolume = config.rigourSharpEye();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_ROCK_SKIN:
				soundVolume = config.rockSkin();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_SMITE:
				soundVolume = config.smite();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_STEEL_SKIN:
				soundVolume = config.steelSkin();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_SUPERHUMAN_STRENGTH:
				soundVolume = config.superhumanStrength();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_THICK_SKIN:
				soundVolume = config.thickSkin();
				break;
			case SoundEffectID.PRAYER_ACTIVATE_ULTIMATE_STRENGTH:
				soundVolume = config.ultimateStrength();
				break;
			case SoundEffectID.PRAYER_DEACTIVE_VWOOP:
				soundVolume = config.prayerDeactivate();
				break;
			case SoundEffectID.UI_BOOP:
				soundVolume = config.uiBoop();
				break;
		}

		// Check if we should ignore this prayer (exactly 100 volume)
		if (soundVolume == 100) {
			return;
		}

		// Cancel/consume the current sound effect
		soundEffectPlayed.consume();

		// Get the sound effect volume
		int currentVolume = client.getPreferences().getSoundEffectVolume();

		// Change the volume to the config's volume and play the sound effect
		client.getPreferences().setSoundEffectVolume(soundVolume);
		client.playSoundEffect(soundId, soundVolume);

		// Revert the sound effect volume to the original
		client.getPreferences().setSoundEffectVolume(currentVolume);

		log.info("Overriding prayer sound " + soundEffectPlayed + " at volume " + soundVolume + ".");
	}

	@Provides
	PrayerVolumeControlConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PrayerVolumeControlConfig.class);
	}
}
