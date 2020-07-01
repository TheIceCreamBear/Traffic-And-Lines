package com.joseph.traffic.lanes;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public interface ILane {
	public void drawLane(Graphics2D g);
	
	public Point2D getLaneStartLocation();
	
	public Point2D getLaneEndLocation();
	
	public ILane getNextLane();
	
	public void setNextLane(ILane lane);
	
	public Point2D getPositionAtDistance(double distance);
	
	public double getLength();
	
	public double getRoationAtDistanceInRad(double distance);
}
