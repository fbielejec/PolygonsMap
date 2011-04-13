package app;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class DrawPolygonMap extends PApplet {

	private static ReadLocations mapdata;
	// min/max longitude
	private static float minX, maxX;
	// min/max latitude
	private static float minY, maxY;
	// Border of where the map should be drawn on screen
	private static float mapX1, mapX2;
	private static float mapY1, mapY2;

	public static void main(String args[]) {
		PApplet.main(new String[] { "app.DrawPolygonMap" });
	}

	public void setup() {

		size(800, 500);

		mapX1 = 30;
		mapX2 = width - mapX1;
		mapY1 = 20;
		mapY2 = height - mapY1;

		// load the map data
		mapdata = new ReadLocations(getClass().getResource("world_map.txt")
				.getPath());

		// calculate min/max longitude
		minX = mapdata.getLongMin();
		maxX = mapdata.getLongMax();
		// calculate min/max latitude
		minY = getMercatorLatitude(mapdata.getLatMin());
		maxY = getMercatorLatitude(mapdata.getLatMax());

	}// END:setup

	public void draw() {

		pushMatrix();

		float sval = (float) 1.0;

		if (mousePressed) {
			sval = (float) 1.5;
		}
		scale(sval);

		smooth();
		drawPlotArea();
		drawHorGrid();
		drawVertGrid();
		drawMapPolygons();
		drawOutline();

		popMatrix();

	}// END:draw

	void drawPlotArea() {

		// White background
		background(255, 255, 255);
		// Show the plot area as a blue box.
		fill(100, 149, 237);
		noStroke();
		rectMode(CORNERS);
		rect(mapX1, mapY1, mapX2, mapY2);

	}// END: drawPlotArea

	void drawMapPolygons() {

		// Dark grey polygon boundaries
		stroke(105, 105, 105, 255);
		strokeWeight(1);
		strokeJoin(ROUND);
		// Sand brown polygon filling
		fill(244, 164, 96, 255);

		int rowCount = mapdata.nrow;
		String region;
		String nextRegion;

		for (int row = 0; row < rowCount - 1; row++) {

			region = mapdata.locations[row];
			nextRegion = mapdata.locations[row + 1];

			beginShape();

			if (nextRegion.toLowerCase().equals(region.toLowerCase())) {

				float X = map(mapdata.getFloat(row, 0), minX, maxX, mapX1,
						mapX2);
				float Y = map(getMercatorLatitude(mapdata.getFloat(row, 1)),
						minY, maxY, mapY2, mapY1);

				float XEND = map(mapdata.getFloat(row + 1, 0), minX, maxX,
						mapX1, mapX2);
				float YEND = map(getMercatorLatitude(mapdata.getFloat(row + 1,
						1)), minY, maxY, mapY2, mapY1);

				vertex(X, Y);
				vertex(XEND, YEND);
//				 vertex(X, YEND);
				// vertex(Y, XEND);
				// vertex(X, XEND);
				// vertex(Y, YEND);
			}

			endShape();

		}// END: row loop
	}// END: drawMapPolygons

	void drawVertGrid() {

		int Interval = 100;
		stroke(255, 255, 255);

		for (float v = mapX1; v <= mapX2; v += Interval) {

			strokeWeight(1);
			line(v, mapY1, v, mapY2);

		}// END: longitude loop
	}// END: drawVertGrid

	void drawHorGrid() {

		int Interval = 50;
		stroke(255, 255, 255);

		for (float v = mapY1; v <= mapY2; v += Interval) {

			strokeWeight(1);
			line(mapX1, v, mapX2, v);

		}// END: latitude loop
	}// End: drawHorGrid

	void drawOutline() {
		noFill();
		stroke(0, 0, 0);
		rectMode(CORNERS);
		rect(mapX1, mapY1, mapX2, mapY2);
	}// END: drawOutline

	private static float getMercatorLatitude(double lat) {

		double R_MAJOR = 6378137.0;
		double R_MINOR = 6356752.3142;

		if (lat > 89.5) {
			lat = 89.5;
		}
		if (lat < -89.5) {
			lat = -89.5;
		}
		double temp = R_MINOR / R_MAJOR;
		double es = 1.0 - (temp * temp);
		double eccent = Math.sqrt(es);
		double phi = Math.toRadians(lat);
		double sinphi = Math.sin(phi);
		double con = eccent * sinphi;
		double com = 0.5 * eccent;
		con = Math.pow(((1.0 - con) / (1.0 + con)), com);
		double ts = Math.tan(0.5 * ((Math.PI * 0.5) - phi)) / con;
		double y = 0 - R_MAJOR * Math.log(ts);

		return (float) y;
	}

}// END: PlotOnMap class