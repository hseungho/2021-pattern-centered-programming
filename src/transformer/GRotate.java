package transformer;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import shapeTools.GShapeTool;

public class GRotate extends GTransformer {

	public GRotate(GShapeTool selectedShape) {
		super(selectedShape);
	}
	
	private double computedRotate(Point2D centerPoint, double cx, double cy) {
		double startAngle = Math.toDegrees(Math.atan2(centerPoint.getX()-px, centerPoint.getY()-py));
		double endAngle = Math.toDegrees(Math.atan2(centerPoint.getX()-cx, centerPoint.getY()-cy));
		
		double rotateAngle = startAngle-endAngle;
		if(rotateAngle < 0)
			rotateAngle += 360;
		
		return rotateAngle;
	}

	@Override
	public void transform(Graphics2D graphics2d, double x, double y) {
		Point2D centerPoint = this.selectedShape.getCenterPoint();
		double rotateAngle = this.computedRotate(centerPoint, x, y);
		this.selectedShape.rotate(graphics2d, rotateAngle, centerPoint.getX(), centerPoint.getY());
	}
}
