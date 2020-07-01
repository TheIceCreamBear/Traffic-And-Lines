package com.joseph.traffic.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import com.joseph.traffic.lanes.ILane;

public class Car {
	private static final int SPEED = 4; // pixels per tick
	private final double rad90 = StrictMath.toRadians(90);
	private final Polygon shape = new Polygon(new int[] {-6, 5, 5, -6, -6, 5, 5, -6, -6}, new int[] {-3, -3, 1, 1, 12, 12, -8, -8, 1}, 9);
	private final Point[] shapePoints;
	private Point[] drawPoints;
	private int x;
	private int y;
	private double rotation;
	private ILane currentRoad;
	private double maxRoadTravelLength;
	private double currentRoadTravled;
	private boolean f;
	
	public Car(ILane road) {
		this.x = (int) road.getLaneEndLocation().getX();
		this.y = (int) road.getLaneEndLocation().getY();
		this.maxRoadTravelLength = road.getLength();
		this.currentRoad = road;
		this.currentRoadTravled = 0;
		this.shapePoints = new Point[shape.npoints];
		this.drawPoints = new Point[shapePoints.length];
		for (int i = 0; i < shapePoints.length; i++) {
			shapePoints[i] = new Point(shape.xpoints[i], shape.ypoints[i]);
			drawPoints[i] = new Point(shape.xpoints[i], shape.ypoints[i]);
		}
		f = true;
	}
	
	public void update(boolean p) {
		if (this.currentRoad == null) {
			return;
		}
		
		Point2D pos = this.currentRoad.getPositionAtDistance(currentRoadTravled);
		this.x = (int) pos.getX();
		this.y = (int) pos.getY();
		this.rotation = this.currentRoad.getRoationAtDistanceInRad(currentRoadTravled);
		
		this.rotatePoints();
		
		if (f && p) {
			this.currentRoadTravled += SPEED;
		}
		f = !f;
		
		if (this.currentRoadTravled > this.maxRoadTravelLength) {
			this.currentRoadTravled -= this.maxRoadTravelLength;
			this.currentRoad = this.currentRoad.getNextLane();
			if (this.currentRoad == null) {
				return;
			}
			this.maxRoadTravelLength = this.currentRoad.getLength();
		}
	}
	
	public void drawCar(Graphics2D g) {
		g.setColor(Color.RED);
		g.drawPolygon(makePoly(drawPoints));
//		if (this.currentRoad != null) {
//			this.currentRoad.drawLane(g);
//			if (this.currentRoad.getNextLane() != null) {
//				this.currentRoad.getNextLane().drawLane(g);
//			}
//		}
	}
	
	private void rotatePoints() {
		AffineTransform.getRotateInstance(rotation + rad90, 00, 00).transform(shapePoints, 0, drawPoints, 0, 9);
	}
	
	private Polygon makePoly(Point[] points) {
		Polygon poly = new Polygon();
		
		for (int i = 0; i < points.length; i++) {
			Point p = points[i];
			poly.addPoint(p.x, p.y);
		}
		
		poly.translate((int) this.x, (int) this.y);
		
		return poly;
	}
	
	public void reverse() {
		currentRoadTravled -= SPEED;
		if (currentRoadTravled < 0) {
			currentRoadTravled = 0;
		}
		printInfo();
	}
	
	public void forward() {
		this.currentRoadTravled += SPEED;
	
		if (this.currentRoadTravled > this.maxRoadTravelLength) {
			this.currentRoadTravled -= this.maxRoadTravelLength;
			this.currentRoad = this.currentRoad.getNextLane();
			this.maxRoadTravelLength = this.currentRoad.getLength();
		}
		printInfo();
	}
	
	public void printInfo() {
		System.err.println("====================================================");
		System.err.println("Current / Max = " + currentRoadTravled + " / " + maxRoadTravelLength);
		System.err.println("(x,y) = (" + x + "," + y + ")");
		System.err.println("rotation = " + rotation);
	}
}
