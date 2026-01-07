package com.pedropathing.ivy.bindings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public final class Bindings {
    private static final List<Binding> bindings = new ArrayList<>();

    private Bindings() {
    }

    public static void update() {
        bindings.forEach(Binding::update);
    }

    public static Binding bind(BooleanSupplier supplier) {
        Binding binding = new Binding(supplier);
        bindings.add(binding);
        return binding;
    }
}
