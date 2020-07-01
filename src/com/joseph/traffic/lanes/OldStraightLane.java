package com.joseph.traffic.lanes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;

public class OldStraightLane implements ILane {
	public static final int WIDTH = 16;
	private int x;
	private int y;
	private int w;
	private int h;
	private int length;
	private boolean vertical;
	private ILane next;
	
	public OldStraightLane(int x, int y, boolean vertical, int length) {
		this.x = x;
		this.y = y;
		this.vertical = vertical;
		this.length = length;
		if (vertical) {
			this.w = 16;
			this.h = length;
			if (length < 0) {
				this.y -= length; // minus minus becomes add
			}
		} else {
			this.h = 16;
			this.w = length;
			if (length < 0) {
				this.x -= length; // minus minus becomes add
			}
		}
	}
	
	public void drawLane(Graphics2D g) {
		Stroke old = g.getStroke();
		
		// dashed lines
		g.setColor(LaneConstants.Colors.BOUNDS);
		g.setStroke(LaneConstants.Strokes.DASHED);
		
		if (vertical) {
			// tall lines
			g.drawLine(x    , y    , x    , y + h);
			g.drawLine(x + w, y    , x + w, y + h);
			
			// end point lines
			g.drawLine(x    , y    , x + w, y    );
			g.drawLine(x    , y + h, x + w, y + h);
		} else {
			// tall lines
			g.drawLine(x    , y    , x + w, y    );
			g.drawLine(x    , y + h, x + w, y + h);
			
			// end point lines
			g.drawLine(x    , y    , x    , y + h);
			g.drawLine(x + w, y    , x + w, y + h);
		}
		
		// the lane
		g.setColor(LaneConstants.Colors.STRAIGHT);
		g.setStroke(LaneConstants.Strokes.LANE);
		
		if (vertical) {
			g.drawLine(x + w / 2, y, x + w / 2, y + h);
		} else {
			g.drawLine(x, y + h / 2, x + w, y + h / 2);
		}
		
		// arrow of direction
		g.setColor(LaneConstants.Colors.ARROW);
		g.setStroke(LaneConstants.Strokes.SMALL);
		if (vertical) {
			if (h > 0) {
				g.drawLine(x + w / 2, y + h / 2, x + w / 2 - 4, y + h / 2 + 4);
				g.drawLine(x + w / 2, y + h / 2, x + w / 2 + 4, y + h / 2 + 4);
			} else {
				g.drawLine(x + w / 2, y + h / 2, x + w / 2 - 4, y + h / 2 - 4);
				g.drawLine(x + w / 2, y + h / 2, x + w / 2 + 4, y + h / 2 - 4);
			}
		} else {
			if (w > 0) {
				g.drawLine(x + w / 2, y + h / 2, x + w / 2 + 4, y + h / 2 - 4);
				g.drawLine(x + w / 2, y + h / 2, x + w / 2 + 4, y + h / 2 + 4);
			} else {
				g.drawLine(x + w / 2, y + h / 2, x + w / 2 - 4, y + h / 2 - 4);
				g.drawLine(x + w / 2, y + h / 2, x + w / 2 - 4, y + h / 2 + 4);
			}
		}
		
		// debug section
		g.setColor(LaneConstants.Colors.CRIT_POINTS);
		g.setStroke(old);
		if (vertical) {
			g.fillRect(x, y, 1, 1);
			g.fillRect(x + w, y, 1, 1);
			g.fillRect(x + w / 2, y, 1, 1);
		} else {
			g.fillRect(x, y, 1, 1);
			g.fillRect(x, y + h, 1, 1);
			g.fillRect(x, y + h / 2, 1, 1);
		}
	}
	
	public boolean isVertical() {
		return this.vertical;
	}
	
	public Point2D getLaneStartLocation() {
		if (vertical) {
			return new Point(x + w / 2, y);
		} else {
			return new Point(x, y + h / 2);
		}
	}
	
	public Point2D getLaneEndLocation() {
		if (vertical) {
			return new Point(x + w / 2, y + h);
		} else {
			return new Point(x + w, y + h / 2);
		}
	}

	@Override
	public ILane getNextLane() {
		return next;
	}

	@Override
	public void setNextLane(ILane lane) {
		this.next = lane;
	}

	@Override
	public Point2D getPositionAtDistance(double distance) {
		int dis = (int) (Math.round(distance));
		if (length < 0) {
			dis = -dis;
		}
		if (vertical) {
			return new Point(x + w / 2, y + h + -dis);
		} else {
			return new Point(x + w - dis, y + h / 2);
		}
	}

	@Override
	public double getLength() {
		return Math.abs(length);
	}

	@Override
	public double getRoationAtDistanceInRad(double distance) {
		if (vertical) {
			if (length < 0) {
				return StrictMath.toRadians(90);
			} else {
				return StrictMath.toRadians(-90);
			}
		} else {
			if (length < 0) {
				return StrictMath.toRadians(0);
			} else {
				return StrictMath.toRadians(180);
			}
		}
	}
}
