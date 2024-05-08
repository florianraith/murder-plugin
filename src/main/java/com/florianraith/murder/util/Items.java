package com.florianraith.murder.util;

import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.function.Consumer;

public final class Items {

    private Items() {
        throw new UnsupportedOperationException();
    }

    public static ItemStack create(@NonNull Material material, @NonNull Consumer<ItemMeta> metaConsumer) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        metaConsumer.accept(meta);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack create(@NonNull Material material) {
        return create(material, meta -> {
        });
    }

    public static ItemStack create(@NonNull Material material, @NonNull String name) {
        return create(material, meta -> meta.setDisplayName(name));
    }

    public static void assign(@NonNull ItemStack item, @NonNull Class<?> clazz) {
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(NamespacedKey.minecraft("class"), PersistentDataType.STRING, clazz.getName());
        item.setItemMeta(meta);
    }

    public static boolean isAssignedTo(ItemStack item, @NonNull Class<?> clazz) {
        if (item == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return false;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String key = container.get(NamespacedKey.minecraft("class"), PersistentDataType.STRING);
        return clazz.getName().equals(key);
    }

}
