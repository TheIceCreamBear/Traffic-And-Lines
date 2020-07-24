package com.joseph.traffic;

import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

import com.projecttriumph.engine.rendering.Camera;

public class LinearLeveledScaleCamera extends Camera {
	private static final double SCALE_FACTOR = 1.1;
	private double[] scaleLevels = new double[50];
	private int currentScaleLevel;
	
	public LinearLeveledScaleCamera() {
		super();
		for (int i = 0; i < this.scaleLevels.length; i++) {
			this.scaleLevels[i] = StrictMath.pow(SCALE_FACTOR, i - 9);
		}
		this.currentScaleLevel = 9;
		
		for (int i = 0; i < scaleLevels.length; i++) {
			System.out.println("this.scaleLevels[" + i + "]=" + this.scaleLevels[i]);
		}
	}

	@Override
	public synchronized void onMouseWheelEvent(MouseWheelEvent event) {
		int rotation = event.getWheelRotation() * -1;
		this.currentScaleLevel += rotation;
		
		// clamp the value to be in bounds
		this.currentScaleLevel = Math.max(0, Math.min(this.currentScaleLevel, this.scaleLevels.length - 1));
	}

	@Override
	public synchronized AffineTransform getCurrentTransform() {
		AffineTransform at = new AffineTransform();
		// shifts the center of the scaled screen to the center of the screen to create zooming from center
		at.translate(-screenCenterX * (this.scaleLevels[currentScaleLevel] - 1), -screenCenterY * (this.scaleLevels[currentScaleLevel] - 1));
		// zooms the screen 
		at.scale(this.scaleLevels[this.currentScaleLevel], this.scaleLevels[this.currentScaleLevel]);
		// Translates to the off set position
		at.translate(offsetX, offsetY);
		return at;
	}
}
