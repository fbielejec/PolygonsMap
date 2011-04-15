package templates;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.ParseException;

import peasy.PeasyCam;
import processing.core.PApplet;
import utils.ReadLocations;
import utils.Setter;

@SuppressWarnings("serial")
public class DrawPolygonMap extends PApplet {

	private PeasyCam cam;
	private ReadLocations mapdata;
	private Dimension dimension;

	// Borders of the map coordinates
	// min/max longitude
	private float minX, maxX;
	// min/max latitude
	private float minY, maxY;

	// Borders of where the map should be drawn on screen
	private float mapX1, mapX2;
	private float mapY1, mapY2;

	private enum MapProjection {
		MERCATOR, EQUIRECTANGULAR
	}

	private MapProjection mapProjection;

	public void setEquirrectangularProjection() {
		mapProjection = MapProjection.EQUIRECTANGULAR;
	}

	public void setMercatorProjection() {
		mapProjection = MapProjection.MERCATOR;
	}

	public void setup() {

		try {

			dimension = Toolkit.getDefaultToolkit().getScreenSize();
			width = dimension.width;
			height = dimension.height;

			size(width, height, P3D);
			setCam(new PeasyCam(this, width / 2, height / 2, 0, 450));
			cam.setMinimumDistance(100);
			// cam.setMaximumDistance(650);

			mapX1 = 30;
			mapX2 = width - mapX1;
			mapY1 = 20;
			mapY2 = height - mapY1;

			// load the map data
			mapdata = new ReadLocations(LoadMapData(new Setter()
					.getJarBoolean()));

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}// END:setup

	public void draw() {

		smooth();
		drawPlotArea();
		drawHorGrid();
		drawVertGrid();
		drawMapPolygons();
		drawOutline();

		// System.out.println(cam.getDistance());

	}// END:draw

	private void drawPlotArea() {

		// White background
		background(255, 255, 255);
		// Show the plot area as a blue box.
		fill(100, 149, 237);
		noStroke();
		rectMode(CORNERS);
		rect(mapX1, mapY1, mapX2, mapY2);

	}// END: drawPlotArea

	private void drawMapPolygons() {

		switch (mapProjection) {
		case EQUIRECTANGULAR:
			// calculate min/max longitude
			minX = (mapdata.getLongMin());
			maxX = (mapdata.getLongMax());
			// calculate min/max latitude
			minY = (mapdata.getLatMin());
			maxY = (mapdata.getLatMax());
			break;
		case MERCATOR:
			// calculate min/max longitude
			minX = mapdata.getLongMin();
			maxX = mapdata.getLongMax();
			// calculate min/max latitude
			minY = getMercatorLatitude(mapdata.getLatMin());
			maxY = getMercatorLatitude(mapdata.getLatMax());
			break;
		}

		// Dark grey polygon boundaries
		stroke(105, 105, 105, 255);
		strokeWeight(1);
		// Sand brown polygon filling
		fill(244, 164, 96, 255);

		int rowCount = mapdata.nrow;
		String region;
		String nextRegion;

		for (int row = 0; row < rowCount - 1; row++) {

			region = mapdata.locations[row];
			nextRegion = mapdata.locations[row + 1];
			float X = 0, Y = 0, XEND = 0, YEND = 0;

			beginShape(POLYGON);

			if (nextRegion.toLowerCase().equals(region.toLowerCase())) {

				switch (mapProjection) {
				case EQUIRECTANGULAR:

					// longitude
					X = map((mapdata.getFloat(row, 0)), minX, maxX, mapX1,
							mapX2);

					XEND = map((mapdata.getFloat(row + 1, 0)), minX, maxX,
							mapX1, mapX2);

					// latitude
					Y = map((mapdata.getFloat(row, 1)), minY, maxY, mapY2,
							mapY1);
					YEND = map((mapdata.getFloat(row + 1, 1)), minY, maxY,
							mapY2, mapY1);
					break;

				case MERCATOR:

					// longitude
					X = map(mapdata.getFloat(row, 0), minX, maxX, mapX1, mapX2);

					XEND = map(mapdata.getFloat(row + 1, 0), minX, maxX, mapX1,
							mapX2);

					// latitude
					Y = map(getMercatorLatitude(mapdata.getFloat(row, 1)),
							minY, maxY, mapY2, mapY1);
					YEND = map(
							getMercatorLatitude(mapdata.getFloat(row + 1, 1)),
							minY, maxY, mapY2, mapY1);
					break;

				}

				vertex(X, Y);
				vertex(XEND, YEND);

			}
			endShape(CLOSE);

		}// END: row loop
	}// END: drawMapPolygons

	private void drawVertGrid() {

		int Interval = 100;
		stroke(255, 255, 255);

		for (float v = mapX1; v <= mapX2; v += Interval) {

			strokeWeight(1);
			line(v, mapY1, v, mapY2);

		}// END: longitude loop
	}// END: drawVertGrid

	private void drawHorGrid() {

		int Interval = 50;
		stroke(255, 255, 255);

		for (float v = mapY1; v <= mapY2; v += Interval) {

			strokeWeight(1);
			line(mapX1, v, mapX2, v);

		}// END: latitude loop
	}// End: drawHorGrid

	private void drawOutline() {
		noFill();
		stroke(0, 0, 0);
		rectMode(CORNERS);
		rect(mapX1, mapY1, mapX2, mapY2);
	}// END: drawOutline

	private float getMercatorLatitude(double lat) {

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

	private void setCam(PeasyCam cam) {
		this.cam = cam;
	}

	public PeasyCam getCam() {
		return cam;
	}

	private String LoadMapData(boolean fromJar) {

		String imgPath;

		if (fromJar) {
			imgPath = "jar:"
					+ this.getClass().getResource("world_map.txt").getPath();
		} else {
			imgPath = this.getClass().getResource("world_map.txt").getPath();
		}

		return imgPath;
	}

}// END: PlotOnMap class