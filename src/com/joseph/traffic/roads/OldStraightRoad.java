package com.joseph.traffic.roads;

import java.awt.Graphics2D;

import com.joseph.traffic.lanes.OldStraightLane;

public class OldStraightRoad implements IRoad {
	private OldStraightLane[] forward;
	private OldStraightLane[] backward;
	
	public OldStraightRoad(int forward, int backward, int x, int y, boolean vertical, int length) {
		this.forward = new OldStraightLane[forward];
		this.backward = new OldStraightLane[backward];
		int xOff = 0;
		int yOff = 0;
//		if (length > 0) {
			for (int i = 0; i < backward; i++) {
				this.backward[i] = new OldStraightLane(x + xOff, y + yOff, vertical, -length);
				if (vertical) {
					xOff += OldStraightLane.WIDTH;
				} else {
					yOff += OldStraightLane.WIDTH;
				}
			}
			
			if (vertical) {
				xOff += OldStraightLane.WIDTH;
			} else {
				yOff += OldStraightLane.WIDTH;
			}
			
			for (int i = 0; i < forward; i++) {
				this.forward[i] = new OldStraightLane(x + xOff, y + yOff, vertical, length);
				if (vertical) {
					xOff += OldStraightLane.WIDTH;
				} else {
					yOff += OldStraightLane.WIDTH;
				}
			}
//		} else {
//			for (int i = 0; i < forward; i++) {
//				this.forward[i] = new StraightLane(x + xOff, y + yOff, vertical, -length);
//				if (vertical) {
//					xOff += StraightLane.WIDTH;
//				} else {
//					yOff += StraightLane.WIDTH;
//				}
//			}
//			
//			if (vertical) {
//				xOff += StraightLane.WIDTH;
//			} else {
//				yOff += StraightLane.WIDTH;
//			}
//			
//			for (int i = 0; i < backward; i++) {
//				this.backward[i] = new StraightLane(x + xOff, y + yOff, vertical, length);
//				if (vertical) {
//					xOff += StraightLane.WIDTH;
//				} else {
//					yOff += StraightLane.WIDTH;
//				}
//			}
//		}
	}
	
	public void drawRoad(Graphics2D g) {
		for (int i = 0; i < backward.length; i++) {
			this.backward[i].drawLane(g);
		}
		
		for (int i = 0; i < forward.length; i++) {
			this.forward[i].drawLane(g);
		}
	}
	
	public OldStraightLane[] getForward() {
		return this.forward;
	}
	
	public OldStraightLane[] getBackward() {
		return this.backward;
	}
}
