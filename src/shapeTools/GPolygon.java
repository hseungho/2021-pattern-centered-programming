package shapeTools;

import java.awt.Polygon;

import main.GConstants.EDrawingStyle;

public class GPolygon extends GShapeTool {
	private static final long serialVersionUID = 1L;

	
	public GPolygon() {
		super(EDrawingStyle.eNPointDrawing);
		this.shape = new Polygon();
	}

	public Object clone() {
		GShapeTool cloned = (GShapeTool) super.clone();
		Polygon clonedPolygon = new Polygon();
		Polygon lastPolygon = (Polygon) this.shape;
		for(int i=0; i<lastPolygon.npoints; i++) {
			clonedPolygon.addPoint(lastPolygon.xpoints[i], lastPolygon.ypoints[i]);
		}
		cloned.shape = clonedPolygon;
		return cloned;
	}
	
	@Override
	public void setInitPoint(int x, int y) {
		Polygon polygon = (Polygon) this.shape;
		polygon.addPoint(x, y);
		polygon.addPoint(x, y);
	}
	public void setIntermediatePoint(int x, int y){
		Polygon polygon = (Polygon) this.shape;
		polygon.addPoint(x, y);
	}
	@Override
	public void setFinalPoint(int x, int y) {}
	
	@Override
	public void movePoint(int x, int y) {
		Polygon polygon = (Polygon) this.shape;
		polygon.xpoints[polygon.npoints-1] = x;
		polygon.ypoints[polygon.npoints-1] = y;
	}
	@Override
	public void pasteShape() {
		Polygon copyPolygon = (Polygon) this.shape;
		copyPolygon.translate(20, 20);
	}
}
