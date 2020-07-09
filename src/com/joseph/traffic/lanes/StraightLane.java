package com.joseph.traffic.lanes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import com.projecttriumph.engine.api.math.MathHelper;

public class StraightLane implements ILane {
	public static final int WIDTH = 16;
	private Point2D.Double start;
	private Point2D.Double end;
	/** the angle in radians */
	private double direction;
	private double length;
	
	// TODO remake next system to allow multiple
	private ILane next;
	
	/**
	 * Constructs a new straight lane from the given points. The given start and end 
	 * points are the actual points of the lane and not on the boundary of the lane.
	 * @param start - the start point
	 * @param end - the end points
	 */
	public StraightLane(Point2D.Double start, Point2D.Double end) {
		this.start = start;
		this.end = end;
		this.direction = MathHelper.getAngleRad(start, end);
		this.length = MathHelper.getDistance(start, end);
	}
	
	/**
	 * Constructs a new Straight line with the given point as the starting point, and
	 * the ending point being {@code length} away from said starting point, at an angle
	 * of {@code direction} radians
	 * @param start - the starting point
	 * @param direction - the direction angle in radians
	 * @param length - the length
	 */
	public StraightLane(Point2D.Double start, double direction, double length) {
		this.start = start;
		this.end = MathHelper.offsetPoint(start, direction, length);
		this.direction = direction;
		this.length = length;
	}
	
	@Override
	public void drawLane(Graphics2D g) {
		Stroke old = g.getStroke();
		
		// calculate points
		Point2D startLeft = MathHelper.offsetPoint(start, direction + StrictMath.toRadians(-90), WIDTH / 2);
		Point2D startRight = MathHelper.offsetPoint(start, direction + StrictMath.toRadians(90), WIDTH / 2);
		Point2D endLeft = MathHelper.offsetPoint(end, direction + StrictMath.toRadians(-90), WIDTH / 2);
		Point2D endRight = MathHelper.offsetPoint(end, direction + StrictMath.toRadians(90), WIDTH / 2);
		Point2D arrowHead = MathHelper.offsetPoint(start, direction, length / 2);;
		Point2D arrowLeft = MathHelper.offsetPoint((Point2D.Double) arrowHead, direction + StrictMath.toRadians(-135), WIDTH / 4);
		Point2D arrowRight = MathHelper.offsetPoint((Point2D.Double) arrowHead, direction + StrictMath.toRadians(135), WIDTH / 4);
		
		
		// dashed lines
		g.setColor(Color.YELLOW);
		g.setStroke(LaneConstants.Strokes.DASHED);
		
		// draw lines
		g.drawLine((int) startLeft.getX(), (int) startLeft.getY(), (int) startRight.getX(), (int) startRight.getY());
		g.drawLine((int) startLeft.getX(), (int) startLeft.getY(), (int) endLeft.getX(), (int) endLeft.getY());
		g.drawLine((int) startRight.getX(), (int) startRight.getY(), (int) endRight.getX(), (int) endRight.getY());
		g.drawLine((int) endLeft.getX(), (int) endLeft.getY(), (int) endRight.getX(), (int) endRight.getY());
		
		// the lane
		g.setColor(Color.YELLOW);
		g.setStroke(LaneConstants.Strokes.LANE);
		
		// draw lane
		g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
		
		// arrow of direction
		g.setColor(LaneConstants.Colors.ARROW);
		g.setStroke(LaneConstants.Strokes.SMALL);
		
		// draw arrow
		g.drawLine((int) arrowHead.getX(), (int) arrowHead.getY(), (int) arrowLeft.getX(), (int) arrowLeft.getY());
		g.drawLine((int) arrowHead.getX(), (int) arrowHead.getY(), (int) arrowRight.getX(), (int) arrowRight.getY());
		
		// debug
		g.setColor(LaneConstants.Colors.CRIT_POINTS);
		g.setStroke(old);
		
		// draw points
		g.fillRect((int) start.getX(), (int) start.getY(), 1, 1);
		g.fillRect((int) startLeft.getX(), (int) startLeft.getY(), 1, 1);
		g.fillRect((int) startRight.getX(), (int) startRight.getY(), 1, 1);
		g.fillRect((int) end.getX(), (int) end.getY(), 1, 1);
		g.fillRect((int) endLeft.getX(), (int) endLeft.getY(), 1, 1);
		g.fillRect((int) endRight.getX(), (int) endRight.getY(), 1, 1);
		g.fillRect((int) arrowHead.getX(), (int) arrowHead.getY(), 1, 1);
		g.fillRect((int) arrowLeft.getX(), (int) arrowLeft.getY(), 1, 1);
		g.fillRect((int) arrowRight.getX(), (int) arrowRight.getY(), 1, 1);
	}
	
	@Override
	public Point2D getLaneStartLocation() {
		return start;
	}
	
	@Override
	public Point2D getLaneEndLocation() {
		return end;
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
		return MathHelper.offsetPoint(start, direction, distance);
	}
	
	@Override
	public double getLength() {
		return this.length;
	}
	
	@Override
	public double getRoationAtDistanceInRad(double distance) {
		return this.direction;
	}
	
	public void updateSecondPoint(Point2D.Double newEnd) {
		this.end = newEnd;
		this.direction = MathHelper.getAngleRad(start, end);
		this.length = MathHelper.getDistance(start, end);
	}
	
}
