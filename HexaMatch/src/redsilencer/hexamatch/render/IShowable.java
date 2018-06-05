package redsilencer.hexamatch.render;

import processing.core.PApplet;

public interface IShowable<T> {
	void show(PApplet sketch, T instance);
}
