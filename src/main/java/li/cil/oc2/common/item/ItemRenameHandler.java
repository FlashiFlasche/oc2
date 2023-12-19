/* SPDX-License-Identifier: MIT */

package li.cil.oc2.common.item;

import li.cil.oc2.api.API;
import li.cil.oc2.api.util.Registries;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class ItemRenameHandler {
    private static final Map<String, Supplier<Item>> RENAMES = Util.make(() -> {
        final Map<String, Supplier<Item>> map = new HashMap<>();

        map.put("hard_drive_buildroot", Items.HARD_DRIVE_CUSTOM::get);
        map.put("flash_memory_buildroot", Items.FLASH_MEMORY_CUSTOM::get);

        return map;
    });

    ///////////////////////////////////////////////////////////////////

    @SubscribeEvent
    public static void onItemMissingMappings(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Item> mapping : event.getAllMappings(ForgeRegistries.ITEMS.getRegistryKey())) {
            final ResourceLocation key = mapping.getKey();
            if (key == null || !Objects.equals(key.getNamespace(), API.MOD_ID)) {
                continue;
            }

            final Supplier<Item> replacement = RENAMES.get(key.getPath());
            if (replacement != null) {
                mapping.remap(replacement.get());
            }
        }
    }
}
