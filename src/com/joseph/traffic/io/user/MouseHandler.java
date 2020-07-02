package com.joseph.traffic.io.user;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import com.joseph.traffic.TrafficSimulation;
import com.joseph.traffic.lanes.StraightLane;
import com.projecttriumph.engine.api.io.user.IGameMouseInputHandler;

public class MouseHandler implements IGameMouseInputHandler {
	private Point mouseDown;
	private Point mouseUp;
	private boolean firstPoint = false;
	private StraightLane lane;

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!firstPoint) {
			firstPoint = true;
			Point p = e.getPoint();
			lane = new StraightLane(new Point2D.Double(p.x, p.y), new Point2D.Double(p.x, p.y));
			TrafficSimulation.lanes.add(lane);
		} else {
			Point p = e.getPoint();
			lane.updateSecondPoint(new Point2D.Double(p.x, p.y));
			firstPoint = false;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown = e.getLocationOnScreen();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseUp = e.getLocationOnScreen();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (firstPoint) {
			Point p = e.getPoint();
			lane.updateSecondPoint(new Point2D.Double(p.x, p.y));
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

	@Override
	public void captureInput() {
		
	}	
}