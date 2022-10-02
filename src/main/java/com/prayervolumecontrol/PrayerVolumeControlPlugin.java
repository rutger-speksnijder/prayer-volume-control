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

	private final int QUICK_PRAYER_ACTIVATE = 2266;

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
		if (soundId == SoundEffectID.PRAYER_ACTIVATE_MYSTIC_WILL_AUGURY) {
			soundVolume = Math.round(config.auguryMysticWill());
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_BURST_OF_STRENGTH) {
			soundVolume = config.burstOfStrength();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_CHIVALRY) {
			soundVolume = config.chivalry();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_CLARITY_OF_THOUGHT) {
			soundVolume = config.clarityOfThought();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_EAGLE_EYE) {
			soundVolume = config.eagleEye();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_HAWK_EYE) {
			soundVolume = config.hawkEye();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_IMPROVED_REFLEXES) {
			soundVolume = config.improvedReflexes();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_INCREDIBLE_REFLEXES) {
			soundVolume = config.incredibleReflexes();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_MYSTIC_LORE) {
			soundVolume = config.mysticLore();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_MYSTIC_MIGHT) {
			soundVolume = config.mysticMight();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_PIETY) {
			soundVolume = config.piety();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_RAPID_RESTORE_PRESERVE) {
			soundVolume = config.preserveRapidRestore();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_PROTECT_FROM_MAGIC) {
			soundVolume = config.protectFromMagic();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_PROTECT_FROM_MELEE) {
			soundVolume = config.protectFromMelee();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_PROTECT_FROM_MISSILES) {
			soundVolume = config.protectFromMissiles();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_PROTECT_ITEM) {
			soundVolume = config.protectItem();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_RAPID_HEAL) {
			soundVolume = config.rapidHeal();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_REDEMPTION) {
			soundVolume = config.redemption();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_RETRIBUTION) {
			soundVolume = config.retribution();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_SHARP_EYE_RIGOUR) {
			soundVolume = config.rigourSharpEye();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_ROCK_SKIN) {
			soundVolume = config.rockSkin();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_SMITE) {
			soundVolume = config.smite();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_STEEL_SKIN) {
			soundVolume = config.steelSkin();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_SUPERHUMAN_STRENGTH) {
			soundVolume = config.superhumanStrength();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_THICK_SKIN) {
			soundVolume = config.thickSkin();
		} else if (soundId == SoundEffectID.PRAYER_ACTIVATE_ULTIMATE_STRENGTH) {
			soundVolume = config.ultimateStrength();
		} else if (soundId == SoundEffectID.PRAYER_DEACTIVE_VWOOP) {
			soundVolume = config.prayerDisable();
		} else if (soundId == QUICK_PRAYER_ACTIVATE) {
			soundVolume = config.quickPrayer();
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
