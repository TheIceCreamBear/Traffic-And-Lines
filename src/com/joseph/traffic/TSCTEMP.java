package com.joseph.traffic;

import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

import com.projecttriumph.engine.rendering.Camera;
import com.projecttriumph.engine.rendering.ScreenManager;

public class TSCTEMP extends Camera {
	private static final double ZOOM_FACTOR = 0.1d;
	private AffineTransform transform = new AffineTransform();
	
	@Override
	public synchronized AffineTransform getCurrentTransform() {
		// shifts the center of the scaled screen to the center of the screen to create zooming from center
//		Point2D p = ScreenManager.getInstance().getMousePoint();
//		try {
//			p = ScreenManager.getInstance().getCamera().getCurrentTransform().inverseTransform(p, null);
//		} catch (NoninvertibleTransformException ex) {
//			ex.printStackTrace();
//		}
//		at.translate(-lastZoom.getX() * (scale - 1), -lastZoom.getY() * (scale - 1));
//		// zooms the screen 
//		at.scale(scale, scale);
		// Translates to the off set position
		synchronized (transform) {			
			AffineTransform at = new AffineTransform(transform);
			at.translate(offsetX, offsetY);
			return at;
		}
	}

	@Override
	public synchronized void onMouseWheelEvent(MouseWheelEvent event) {
//		super.onMouseWheelEvent(event);
		
		Point mouse = ScreenManager.getInstance().getMousePoint();
		int rotation = event.getWheelRotation();
		double scaleFactor = ZOOM_FACTOR * rotation * -1 + 1;
		
		synchronized (transform) {			
			System.out.println("+=================================================================+");
			transform.translate(-mouse.x * (scaleFactor - 1), -mouse.y * (scaleFactor - 1));
			System.out.println(transform);
			transform.scale(scaleFactor, scaleFactor);
			System.out.println(transform);
			// restores true center (0,0)
//		transform.translate(mouse.x / scaleFactor, mouse.y / scaleFactor);
			// over translates (when scale factor is 1.1, by 1.1 times)
//		transform.translate(mouse.x, mouse.y);
//		transform.translate(mouse.x, mouse.y);
			System.out.println(transform);
		}
	}
	
}