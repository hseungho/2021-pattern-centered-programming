package shapeTools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

import main.GConstants;
import main.GConstants.EAction;
import main.GConstants.EDrawingStyle;

public abstract class GShapeTool implements Serializable, Cloneable {
	// attributes
	private static final long serialVersionUID = 1L;
	
	public enum EAnchors{
		x0y0, x0y1,	x0y2, x1y0,	x1y2, x2y0,	x2y1, x2y2,	RR
	};
	
	protected Shape shape;
	private Ellipse2D[] anchors;
	private boolean isSelected;
	private EAnchors selectedAnchor;
	private EAction eAction;
	private AffineTransform affineTransform;
	private AffineTransform rotateTransform;

	private int myLineWidth;
	private Color myLineColor;
	private Color myFillColor;
	
	// working variables
	private EDrawingStyle eDrawingStyle;
	
	// constructors
	public GShapeTool(EDrawingStyle eDrawingStyle) {
		this.anchors = new Ellipse2D.Double[EAnchors.values().length];
		for(EAnchors eAnchor : EAnchors.values()) {
			this.anchors[eAnchor.ordinal()] = new Ellipse2D.Double();
		}
		this.selectedAnchor = null;
		
		this.isSelected=false;
		this.eDrawingStyle=eDrawingStyle;
		
		this.affineTransform = new AffineTransform();
		this.affineTransform.setToIdentity();
		this.rotateTransform = new AffineTransform();
		this.rotateTransform.setToIdentity();
	}
	public Object clone() {
		GShapeTool cloned = null;
		try {
			cloned = (GShapeTool) super.clone();
			for(EAnchors eAnchor : EAnchors.values()) {
				cloned.anchors[eAnchor.ordinal()] =  (Ellipse2D) this.anchors[eAnchor.ordinal()].clone();
			}
			cloned.affineTransform = (AffineTransform) this.affineTransform.clone();
			cloned.rotateTransform = (AffineTransform) this.rotateTransform.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return cloned;
	}
	
	// getter and setter
	public EDrawingStyle getDrawingStyle() { return this.eDrawingStyle;	}
	
	public EAction getAction() { return this.eAction; }
	
	public EAnchors getSelectedAnchor() { return this.selectedAnchor; }
	
	public Shape transformShape() {
		return this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.shape));
	}
	
	public int getWidth() { return this.transformShape().getBounds().width; }
	
	public int getHeight() { return this.transformShape().getBounds().height; }
	
	public Point2D getResizeOrigin(EAnchors selectedAnchor) {
		Point2D originPoint = new Point2D.Double();
		switch(selectedAnchor) {
			// N
			case x1y0: originPoint.setLocation(
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x1y2.ordinal()])).getBounds().getCenterX(),
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x1y2.ordinal()])).getBounds().getCenterY());
				break;
			// S
			case x1y2: originPoint.setLocation(
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x1y0.ordinal()])).getBounds().getCenterX(),
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x1y0.ordinal()])).getBounds().getCenterY());
				break;
			// W
			case x0y1: originPoint.setLocation(
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x2y1.ordinal()])).getBounds().getCenterX(),
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x2y1.ordinal()])).getBounds().getCenterY());
				break;
			// E
			case x2y1: originPoint.setLocation(
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x0y1.ordinal()])).getBounds().getCenterX(),
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x0y1.ordinal()])).getBounds().getCenterY());
				break;
			// NW
			case x0y0: originPoint.setLocation(
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x2y2.ordinal()])).getBounds().getCenterX(),
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x2y2.ordinal()])).getBounds().getCenterY());
				break;
			// SW
			case x0y2: originPoint.setLocation(
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x2y0.ordinal()])).getBounds().getCenterX(),
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x2y0.ordinal()])).getBounds().getCenterY());
				break;
			// NE
			case x2y0: originPoint.setLocation(
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x0y2.ordinal()])).getBounds().getCenterX(),
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x0y2.ordinal()])).getBounds().getCenterY());
				break;
			// SE
			case x2y2: originPoint.setLocation(
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x0y0.ordinal()])).getBounds().getCenterX(),
					this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.x0y0.ordinal()])).getBounds().getCenterY());
				break;
			default: break;
		}
		return originPoint;
	}
	
	public Point2D getCenterPoint() {
		Point2D centerPoint = new Point2D.Double();
		centerPoint.setLocation(this.shape.getBounds().getCenterX(),
				this.shape.getBounds().getCenterY());
		return centerPoint;
	}

	// methods
	public EAction containes(int x, int y) {
		this.eAction = null;
		if(this.isSelected) {
			for(int i=0; i<this.anchors.length-1; i++) {
				if(this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[i])).contains(x, y)) {
					this.selectedAnchor = EAnchors.values()[i];
					this.eAction = EAction.eResize;
				}
			}
			if(this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[EAnchors.RR.ordinal()])).contains(x, y)) {
				this.eAction = EAction.eRotate;
			}
		}
		if(this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.shape)).contains(x, y)) {
			this.eAction = EAction.eMove;
		}
		return this.eAction;
	}
	
	public void setSelected(boolean isSelected) { //true면 anchor를 그리고, false면 anchor를 지워라
		this.isSelected = isSelected;
	}
	
	public void move(Graphics2D graphics2d, double dx, double dy) {
		this.draw(graphics2d);
		this.affineTransform.translate(dx, dy);
		this.draw(graphics2d);
		graphics2d.transform(affineTransform);
	}
	
	public void resize(Graphics2D graphics2d, Point2D originPoint, Point2D resizeRatio) {
		double ox = originPoint.getX();
		double oy = originPoint.getY();
		double sx = resizeRatio.getX();
		double sy = resizeRatio.getY();
		
		this.draw(graphics2d);
		this.affineTransform.translate(ox, oy);
		this.affineTransform.scale(sx, sy);
		this.affineTransform.translate(-ox, -oy);
		this.draw(graphics2d);
		graphics2d.transform(affineTransform);
	}
	
	public void rotate(Graphics2D graphics2d, double rotateAngle, double ox, double oy) {
		this.draw(graphics2d);
		this.rotateTransform.translate(ox, oy);
		this.rotateTransform.rotate(Math.toRadians(rotateAngle));
		this.rotateTransform.translate(-ox, -oy);
		this.draw(graphics2d);
		graphics2d.transform(rotateTransform);
	}
	
	private void drawAnchors(Graphics2D graphics2d) {
		final int wAnchor = GConstants.wAnchor;
		final int hAnchor = GConstants.hAnchor;
		
		Rectangle rectangle = this.shape.getBounds();
		double x0 = rectangle.x - wAnchor/2;
		double x1 = rectangle.x - wAnchor/2 + rectangle.width/2;
		double x2 = rectangle.x - wAnchor/2 + rectangle.width;
		double y0 = rectangle.y - hAnchor/2;
		double y1 = rectangle.y - hAnchor/2 + rectangle.height/2;
		double y2 = rectangle.y - hAnchor/2 + rectangle.height;
		
 		this.anchors[EAnchors.x0y0.ordinal()].setFrame(x0, y0, wAnchor, hAnchor);
		this.anchors[EAnchors.x0y1.ordinal()].setFrame(x0, y1, wAnchor, hAnchor);
		this.anchors[EAnchors.x0y2.ordinal()].setFrame(x0, y2, wAnchor, hAnchor);
		this.anchors[EAnchors.x1y0.ordinal()].setFrame(x1, y0, wAnchor, hAnchor);
		this.anchors[EAnchors.x1y2.ordinal()].setFrame(x1, y2, wAnchor, hAnchor);
		this.anchors[EAnchors.x2y0.ordinal()].setFrame(x2, y0, wAnchor, hAnchor);
		this.anchors[EAnchors.x2y1.ordinal()].setFrame(x2, y1, wAnchor, hAnchor);
		this.anchors[EAnchors.x2y2.ordinal()].setFrame(x2, y2, wAnchor, hAnchor);
		this.anchors[EAnchors.RR.ordinal()].setFrame(x1, y0-40, wAnchor, hAnchor);
		
		for(EAnchors eAnchor : EAnchors.values()) {
			graphics2d.setStroke(new BasicStroke(1));
			graphics2d.setColor(Color.WHITE);
			graphics2d.fill(this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[eAnchor.ordinal()])));
			graphics2d.setColor(Color.black);
			graphics2d.draw(this.affineTransform.createTransformedShape(this.rotateTransform.createTransformedShape(this.anchors[eAnchor.ordinal()])));
		}
	}
	
	public void setLineWidth(int i) { this.myLineWidth = i;	}
	public int getLineWidth() { return this.myLineWidth; }
	public void setLineColor(Color color) { this.myLineColor = color; }
	public Color getLineColor() { return this.myLineColor; }
	public void setFillColor(Color color) { this.myFillColor = color; }
	public Color getFillColor() { return this.myFillColor; }
	
	public void draw(Graphics2D graphics2d) {
		graphics2d.setStroke(new BasicStroke(this.myLineWidth));
		graphics2d.setColor(this.getFillColor());
		graphics2d.fill(this.transformShape());
		graphics2d.setColor(this.getLineColor());
		graphics2d.draw(this.transformShape());
		if(this.isSelected) {
			this.drawAnchors(graphics2d);
		}
	}
	
	public void animate(Graphics2D graphics2d, int x, int y) {
		this.draw(graphics2d);
		this.movePoint(x, y);
		this.draw(graphics2d);
	}
	
	// interface
	public abstract void setInitPoint(int x, int y);
	
	public void setIntermediatePoint(int x, int y) {}
	
	public abstract void setFinalPoint(int x, int y);

	public abstract void movePoint(int x, int y);

	public abstract void pasteShape() ;
}
