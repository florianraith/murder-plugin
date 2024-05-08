package com.florianraith.murder.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

import java.util.Objects;

public final class Attributes {

    private Attributes() {
        throw new UnsupportedOperationException();
    }

    public static double set(@NonNull Attributable attributable, @NonNull Attribute attribute, double value) {
        AttributeInstance instance = attributable.getAttribute(attribute);
        Objects.requireNonNull(instance);
        instance.setBaseValue(value);
        return value;
    }

    public static double get(@NonNull Attributable attributable, @NonNull Attribute attribute) {
        AttributeInstance instance = attributable.getAttribute(attribute);
        Objects.requireNonNull(instance);
        return instance.getBaseValue();
    }


    public static double reset(@NonNull Attributable attributable, @NonNull Attribute attribute) {
        AttributeInstance instance = attributable.getAttribute(attribute);
        Objects.requireNonNull(instance);
        double defaultValue = instance.getDefaultValue();
        instance.setBaseValue(defaultValue);
        return defaultValue;
    }

}
