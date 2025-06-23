package io.github.velaliilunalii.mixae.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModSoundEvents {
	static Map<SoundEvent, Identifier> SOUND_EVENTS = new LinkedHashMap<>();

	//	final SoundEvent SOUND_EVENT = createSoundEvent("sound.event.name");
	public static final SoundEvent SCYTHE_SLASH_HEAVY = createSoundEvent("item.scythe_slash_heavy");
	public static final SoundEvent SCYTHE_SLASH_LIGHT = createSoundEvent("item.scythe_slash_light");
	public static final SoundEvent DAGGER_SLASH = createSoundEvent("item.dagger_slash");
	public static final SoundEvent ASTRAL_DRIP_HEAVY = createSoundEvent("item.astral_drip_heavy");
	public static final SoundEvent ASTRAL_DRIP_LIGHT = createSoundEvent("item.astral_drip_light");

	public static void initialize() {
		SOUND_EVENTS.keySet().forEach(soundEvent -> {
			Registry.register(Registry.SOUND_EVENT, SOUND_EVENTS.get(soundEvent), soundEvent);
		});
	}

	private static SoundEvent createSoundEvent(String path) {
		SoundEvent soundEvent = new SoundEvent(new Identifier("mixae", path));
		SOUND_EVENTS.put(soundEvent, new Identifier("mixae", path));
		return soundEvent;
	}
}
