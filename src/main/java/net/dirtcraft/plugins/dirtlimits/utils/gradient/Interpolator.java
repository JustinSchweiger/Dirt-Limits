package net.dirtcraft.plugins.dirtlimits.utils.gradient;

@FunctionalInterface
public interface Interpolator {

	double[] interpolate(double from, double to, int max);
}
