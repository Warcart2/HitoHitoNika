package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.server.management.OpList;
import java.util.Optional;

public class GearSet extends ArrayList<Ability> {
	public boolean containsAbility(AbilityCore in) {
		return this.stream().anyMatch(itr -> {return itr.getCore() == in;});
	}

	public Optional<Ability> getAbility(AbilityCore in) {
		return this.stream().filter(itr -> {return itr.getCore() == in;}).findFirst();
	}
}
