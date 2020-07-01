package com.joseph.traffic.roads;

import java.awt.Graphics2D;

import com.joseph.traffic.lanes.OldCurvedLane;

public class OldCurvedRoad implements IRoad {
	private OldCurvedLane[] forward;
	private OldCurvedLane[] backward;
	
	public OldCurvedRoad(OldStraightRoad r1, OldStraightRoad r2, boolean reversed, boolean order) {
		if (!reversed) {
			if (r1.getForward().length != r2.getForward().length || r1.getBackward().length != r2.getBackward().length) {
				return;
			}
			this.forward = new OldCurvedLane[r1.getForward().length];
			this.backward = new OldCurvedLane[r1.getBackward().length];
			
			for (int i = 0; i < forward.length; i++) {
				this.forward[i] = new OldCurvedLane(r1.getForward()[i].getLaneStartLocation(), r2.getForward()[i].getLaneEndLocation(), r2.getForward()[i].isVertical());
			}
			
			for (int i = 0; i < backward.length; i++) {
				this.backward[i] = new OldCurvedLane(r2.getBackward()[i].getLaneStartLocation(), r1.getBackward()[i].getLaneEndLocation(), r1.getBackward()[i].isVertical());
			}
		} else {
			if (r1.getForward().length != r2.getBackward().length || r1.getBackward().length != r2.getForward().length) {
				return;
			}
			this.forward = new OldCurvedLane[r1.getForward().length];
			this.backward = new OldCurvedLane[r1.getBackward().length];
			
			if (!order) {
				for (int i = 0; i < forward.length; i++) {
					this.forward[i] = new OldCurvedLane(r1.getForward()[i].getLaneStartLocation(), r2.getBackward()[forward.length - 1 - i].getLaneEndLocation(), r2.getBackward()[forward.length - 1 - i].isVertical());
				}
				
				for (int i = 0; i < backward.length; i++) {
					this.backward[i] = new OldCurvedLane(r2.getForward()[i].getLaneStartLocation(), r1.getBackward()[backward.length - 1 - i].getLaneEndLocation(), r1.getBackward()[backward.length - 1 - i].isVertical());
				}
			} else {
				for (int i = 0; i < forward.length; i++) {
					this.forward[i] = new OldCurvedLane(r2.getBackward()[forward.length - 1 - i].getLaneStartLocation(), r1.getForward()[i].getLaneEndLocation(), r1.getForward()[i].isVertical());
				}
				
				for (int i = 0; i < backward.length; i++) {
					this.backward[i] = new OldCurvedLane(r1.getBackward()[backward.length - 1 - i].getLaneStartLocation(), r2.getForward()[i].getLaneEndLocation(), r2.getForward()[i].isVertical());
				}
			}
		}
	}
	
	@Override
	public void drawRoad(Graphics2D g) {
		for (int i = 0; i < forward.length; i++) {
			forward[i].drawLane(g);
		}
		
		for (int i = 0; i < backward.length; i++) {
			backward[i].drawLane(g);
		}
	}
	
	public OldCurvedLane[] getForward() {
		return this.forward;
	}
	
	public OldCurvedLane[] getBackward() {
		return this.backward;
	}
}
