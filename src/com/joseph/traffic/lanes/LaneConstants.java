package com.joseph.traffic.lanes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public class LaneConstants {
	public static class Colors {
		public static final Color STRAIGHT = Color.CYAN;
		public static final Color CURVED = Color.MAGENTA;
		public static final Color BOUNDS = Color.CYAN;
		public static final Color CRIT_POINTS = Color.GREEN;
		public static final Color ARROW = Color.RED;
	}
	
	public static class Strokes {
		public static final Stroke LANE = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
		public static final Stroke DASHED = new BasicStroke(0.25f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] {5.0f, 5.0f}, 0.0f);
		public static final Stroke SMALL = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
	}
}
