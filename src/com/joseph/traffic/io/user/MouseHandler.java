package com.joseph.traffic.io.user;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.projecttriumph.engine.api.io.user.IGameMouseInputHandler;

public class MouseHandler implements IGameMouseInputHandler {
	private Point mouseDown;
	private Point mouseUp;

	@Override
	public void mouseClicked(MouseEvent e) {
		
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
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

	@Override
	public void captureInput() {
		
	}	
}