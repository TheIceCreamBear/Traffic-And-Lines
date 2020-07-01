package com.joseph.traffic;

import java.awt.geom.AffineTransform;

import com.projecttriumph.engine.rendering.Camera;

public class TSCTEMP extends Camera {
	@Override
	public AffineTransform getCurrentTransform() {
		AffineTransform at = new AffineTransform();
		// shifts the center of the scaled screen to the center of the screen to create zooming from center
		at.translate(-300 * (scale - 1), -200 * (scale - 1));
		// zooms the screen 
		at.scale(scale, scale);
		// Translates to the off set position
		at.translate(offsetX, offsetY);
		return at;
	}
}