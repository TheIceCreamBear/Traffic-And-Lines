package com.joseph.traffic.lanes;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

import com.projecttriumph.engine.api.math.MathHelper;

public class CurvedLane implements ILane {
	private QuadCurve2D.Double curve;
	private Point2D start;
	private Point2D end;
	private Point2D control;
	private double length;

	// TODO remake next system to allow multiple
	private ILane next;
	
	/**
	 * Constructs a new curved lane by finding the intersection between the two given straight lanes
	 * and uses that intersection as the control point for the quadratic bezier curve. The start of
	 * this curved lane will connect to the end point of {@code start} and the start point of 
	 * {@code end}. 
	 * 
	 * <p> Not suited for curves that trace out an angle >= 180 degrees.
	 * <p> Will create a garbage curve if the lanes passed in are parallel
	 * 
	 * @param start - the starting lane
	 * @param end - the ending lane
	 */
	public CurvedLane(StraightLane start, StraightLane end) {
		Point2D start1 = start.getLaneStartLocation();
		Point2D end1 = start.getLaneEndLocation();
		Point2D start2 = end.getLaneStartLocation();
		Point2D end2 = end.getLaneEndLocation();
		
		double x = Double.POSITIVE_INFINITY;
		double y = Double.POSITIVE_INFINITY;
		
		// calculate slope of first line
		double m1Top = end1.getY() - start1.getY();
		double m1Bottom = end1.getX() - start1.getX();
		double m1 = Double.POSITIVE_INFINITY;
		
		// if slope would be vertical or horizontal, handle it and prevent a divide by zero
		if (MathHelper.equal(m1Top, 0, 0.0001)) {
			// horizontal
			y = start1.getY();
		} else if (MathHelper.equal(m1Bottom, 0, 0.0001)) {
			// vertical
			x = start1.getX();
		} else {
			// success
			m1 = m1Top / m1Bottom;
		}
		
		// calculate slope of second line
		double m2Top = end2.getY() - start2.getY();
		double m2Bottom = end2.getX() - start2.getX();
		double m2 = Double.POSITIVE_INFINITY;
		
		// if slope would be vertical or horizontal, handle it and prevent a divide by zero
		if (MathHelper.equal(m2Top, 0, 0.0001)) {
			if (y == Double.POSITIVE_INFINITY) {
				// horizontal
				y = start2.getY();
			} else {
				// horizontal average
				y = (start2.getY() + y) / 2;
			}
		} else if (MathHelper.equal(m2Bottom, 0, 0.0001)) {
			if (x == Double.POSITIVE_INFINITY) {
				// vertical
				x = start2.getX();
			} else {
				// vertical average
				x = (start2.getX() + x) / 2;
			}
		} else {
			// success
			m2 = m2Top / m2Bottom;
		}
		
//		System.out.println("================================================================");
//		System.out.println("Curved Line post Initial Calculation");
//		System.out.println("Points: " + end1 + start2);
//		System.out.println("Slopes: " + m1 + "   " + m2);
//		System.out.println("x:" + x + " y:" + y);
		
		if (m1 != Double.POSITIVE_INFINITY && m2 != Double.POSITIVE_INFINITY) {
			// calculate x and y based off of two line equation
			x = (start2.getY() - start1.getY() + m1 * start1.getX() - m2 * start2.getX()) / (m1 - m2);
			y = m1 * (x - start1.getX()) + start1.getY();
		} else {
			if (m1 == Double.POSITIVE_INFINITY && m2 != Double.POSITIVE_INFINITY) {
				// first line invalid, second valid
				if (x != Double.POSITIVE_INFINITY && y == Double.POSITIVE_INFINITY) {
					// vertical cross with angled
					y = m2 * (x - start2.getX()) + start2.getY();
				} else if (x == Double.POSITIVE_INFINITY && y != Double.POSITIVE_INFINITY) {
					// horizontal cross with angled
					x = (y - start2.getY()) / m2 + start2.getX();
				} else if (x == Double.POSITIVE_INFINITY && y == Double.POSITIVE_INFINITY) {
					// error state
					System.err.println("Encountered an error in creating a curved lane.");
					System.err.println("Both points are not set with one line valid");
					// TODO add more information to error print
				} else {
					// error state
					System.err.println("Encountered an error in creating a curved lane.");
					System.err.println("Both points are set with one line valid");
					// TODO add more information to error print
				}
			} else if (m1 != Double.POSITIVE_INFINITY && m2 == Double.POSITIVE_INFINITY) {
				// first line valid, second invalid
				if (x != Double.POSITIVE_INFINITY && y == Double.POSITIVE_INFINITY) {
					// vertical cross with angled
					y = m1 * (x - start1.getX()) + start1.getY();
				} else if (x == Double.POSITIVE_INFINITY && y != Double.POSITIVE_INFINITY) {
					// horizontal cross with angled
					x = (y - start1.getY()) / m1 + start1.getX();
				} else if (x == Double.POSITIVE_INFINITY && y == Double.POSITIVE_INFINITY) {
					// error state
					System.err.println("Encountered an error in creating a curved lane.");
					System.err.println("Both points are not set with one line valid");
					// TODO add more information to error print
				} else {
					// error state
					System.err.println("Encountered an error in creating a curved lane.");
					System.err.println("Both points are set with one line valid");
					// TODO add more information to error print
				}
			} else {
				// neither valid
				if (y == Double.POSITIVE_INFINITY && x != Double.POSITIVE_INFINITY) {
					// error state
					System.err.println("Encountered an error in creating a curved lane.");
					System.err.println("Both point is not set with neither line valid");
					// TODO add more information to error print
				} else if (x == Double.POSITIVE_INFINITY && y != Double.POSITIVE_INFINITY) {
					// error state
					System.err.println("Encountered an error in creating a curved lane.");
					System.err.println("One point is not set with neither line valid");
					// TODO add more information to error print
				} else if (x == Double.POSITIVE_INFINITY && y == Double.POSITIVE_INFINITY) {
					// error state
					System.err.println("Encountered an error in creating a curved lane.");
					System.err.println("Both points are not set with neither line valid");
					// TODO add more information to error print
				} else {
					// valid state, both points are initialized and neither lane has a valid slope
					// no-op
				}
			}
		}
//		System.out.println("-------------------------------------------------");
//		System.out.println("Curved Line post Calculation");
//		System.out.println("x:" + x + " y:" + y);
//		System.out.println("================================================================");
		
		// init actual lane
		this.start = start.getLaneEndLocation();
		this.control = new Point2D.Double(x, y);
		this.end = end.getLaneStartLocation();
		
		this.curve = new QuadCurve2D.Double();
		this.curve.setCurve(this.start, this.control, this.end);

		this.calculateLength();
	}
	
	/**
	 * Constructs a new curved lane by creating straight lanes from the given points and their directions.
	 * 
	 * <br>More specifically, it will create a new straight lane with the end point being the point {@code start},
	 * the direction being {@code inDirection}, and length 10. It will also create a new straight lane with
	 * the start point being {@code end}, the direction being {@code outDirection}, and length 10.
	 * 
	 * <br>It will then call {@link CurvedLane#CurvedLane(StraightLane, StraightLane)} on those two 
	 * new straight lanes.
	 * 
	 * <p>Most useful for creating chained curves, where one curved lane connects to another curved lane.
	 * 
	 * @param start - the start point of the curve
	 * @param inDirection - the direction of the incoming lane
	 * @param end - the end point of the curve
	 * @param outDirection - the direction of the outgoing lane
	 * 
	 * @see CurvedLane#CurvedLane(StraightLane, StraightLane)
	 */
	public CurvedLane(Point2D.Double start, double inDirection, Point2D.Double end, double outDirection) {
		this(new StraightLane(MathHelper.offsetPoint(start, inDirection + StrictMath.toRadians(180), 10), start), 
				new StraightLane(MathHelper.offsetPoint(end, outDirection, 10), end));
	}
	
	/**
	 * Most simplified constructor. Takes the start and ends points, as well as the given control point,
	 * and constructs a quadratic bezier curve based off of those points.
	 * <p>
	 * Note: this does not guarantee that the ends of the curve will line up with any other existing lane
	 * or flat object at the point. Most useful for predefining curves. Best suited for curves with angles
	 * >= 180 degrees.
	 * @param start
	 * @param control
	 * @param end
	 */
	public CurvedLane(Point2D.Double start, Point2D.Double control, Point2D.Double end) {
		this.start = start;
		this.control = control;
		this.end = end;
		
		this.curve = new QuadCurve2D.Double();
		this.curve.setCurve(this.start, this.control, this.end);

		this.calculateLength();
	}
	
	@Override
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
		return this.next;
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
		// adapted from:  https://en.wikipedia.org/wiki/B%C3%A9zier_curve#Quadratic_B%C3%A9zier_curves
		double x = Math.pow(1 - t, 2) * start.getX() + 2 * t * (1 - t) * control.getX() + Math.pow(t, 2) * end.getX();
		double y = Math.pow(1 - t, 2) * start.getY() + 2 * t * (1 - t) * control.getY() + Math.pow(t, 2) * end.getY();
		return new Point2D.Double(x, y);
	}
	
	@Override
	public double getLength() {
		return this.length;
	}
	
	private void calculateLength() {
		// LENGTH CALCULATION, adapted from:
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
	
	@Override
	public double getRoationAtDistanceInRad(double distance) {
		return getRotationAt(distance / length);
	}
	
	private double getRotationAt(double t) {
		double dx = (2 * (1 - t) * (control.getX() - start.getX())) + (2 * t * (end.getX() - control.getX()));
		double dy = (2 * (1 - t) * (control.getY() - start.getY())) + (2 * t * (end.getY() - control.getY()));
		if (start.getX() > end.getX()) {
			// TODO test this
			return StrictMath.atan(dy / dx) + StrictMath.toRadians(180);
		} else {
			return StrictMath.atan(dy / dx);
		}
	}
	
}
