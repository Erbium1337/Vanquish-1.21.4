package wtf.vanquish.client.features.modules.movement.nitrofirework;

import wtf.vanquish.api.system.backend.Choice;
import wtf.vanquish.api.system.backend.Pair;

public abstract class NitroFireworkMode extends Choice {
    public abstract Pair<Float, Float> velocityValues();
}
