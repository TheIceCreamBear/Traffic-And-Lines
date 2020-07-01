package com.joseph.traffic.lanes;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

public class OldCurvedLane implements ILane {
	private QuadCurve2D.Double curve;
	private Point2D start;
	private Point2D end;
	private Point2D control;
	private ILane next;
	private double length;
	
	public OldCurvedLane(Point2D start, Point2D end, boolean isEndVertical) {
		this.start = start;
		this.end = end;
		
		if (isEndVertical) {
			this.control = new Point2D.Double(end.getX(), start.getY());
		} else {
			this.control = new Point2D.Double(start.getX(), end.getY());
		}
		
		this.curve = new QuadCurve2D.Double();
		this.curve.setCurve(this.start, control, this.end);
		
		
		// LENGTH CALCULATION
		// https://web.archive.org/web/20180418075534/http://www.malczak.linuxpl.com/blog/quadratic-bezier-curve-length/
		double ax = start.getX() - 2 * control.getX() + end.getX();
		double ay = start.getY() - 2 * control.getY() + end.getY();
		double bx = 2 * control.getX() - 2 * start.getX();
		double by = 2 * control.getY() - 2 * start.getY();
		
		double a = 4 * (ax * ax + ay * ay);
		double b = 4 * (ax * bx + ay * by);
		double c = bx * bx + by * by;
		
		double sabc = 2 * StrictMath.sqrt(a + b + c);
		double a_2 = StrictMath.sqrt(a);
		double a_32 = 2 * a * a_2;
		double c_2 = 2 * StrictMath.sqrt(c);
		double ba = b / a_2;
		
		this.length = (a_32 * sabc + a_2 * b * (sabc - c_2) + (4 * c * a - b * b) * StrictMath.log10((2 * a_2 + ba + sabc) / (ba + c_2))) / (4 * a_32);
		
	}
	
	public void drawLane(Graphics2D g) {
		Stroke old = g.getStroke();
		
		g.setStroke(LaneConstants.Strokes.LANE);
		g.setColor(LaneConstants.Colors.CURVED);
		
		g.draw(curve);
		
		g.setStroke(old);
		g.setColor(LaneConstants.Colors.CRIT_POINTS);
		
		g.fillRect((int) start.getX(), (int) start.getY(), 1, 1);
		g.fillRect((int) control.getX(), (int) control.getY(), 1, 1);
		g.fillRect((int) end.getX(), (int) end.getY(), 1, 1);
	}

	@Override
	public Point2D getLaneStartLocation() {
		return start;
	}

	@Override
	public Point2D getLaneEndLocation() {
		return end;
	}
	
	public Point2D getControlPoint() {
		return control;
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
		return getPositionAt(distance / length);
	}
	
	private Point2D getPositionAt(double t) {
		// https://en.wikipedia.org/wiki/B%C3%A9zier_curve#Quadratic_B%C3%A9zier_curves
		double x = Math.pow(1 - t, 2) * start.getX() + 2 * t * (1 - t) * control.getX() + Math.pow(t, 2) * end.getX();
		double y = Math.pow(1 - t, 2) * start.getY() + 2 * t * (1 - t) * control.getY() + Math.pow(t, 2) * end.getY();
		return new Point2D.Double(x, y);
	}

	@Override
	public double getLength() {
		return length;
	}

	@Override
	public double getRoationAtDistanceInRad(double distance) {
		return getRotationAt(distance / length);
	}
	
	private double getRotationAt(double t) {
		double dx = (2 * (1 - t) * (control.getX() - start.getX())) + (2 * t * (end.getX() - control.getX()));
		double dy = (2 * (1 - t) * (control.getY() - start.getY())) + (2 * t * (end.getY() - control.getY()));
		if (start.getX() > end.getX()) {
			return StrictMath.atan(dy / dx) + StrictMath.toRadians(180);
		} else {
			return StrictMath.atan(dy / dx);
		}
	}
}
