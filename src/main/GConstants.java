package main;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import shapeTools.GLine;
import shapeTools.GOval;
import shapeTools.GPolygon;
import shapeTools.GRectangle;
import shapeTools.GShapeTool;

public class GConstants {
	public static class CFrame{
		public final static Point point = new Point(200, 100);
		public final static Dimension dimension = new Dimension(400,600);
	}
	
	public enum EDrawingStyle {
		e2PointDrawing,
		eNPointDrawing;
	}

	public final static int wAnchor = 10;
	public final static int hAnchor = 10;
	
	public enum EAction {
		eDraw, eMove, eResize, eRotate, eShear
	}
	
	public enum EShapeTool{
		eRectangle(new GRectangle(), "Rectangle", new ImageIcon("icon/rectIcon.jpg")),
		eOval(new GOval(), "Oval", new ImageIcon("icon/ovalIcon.jpg")),
		ePolygon(new GPolygon(), "Polygon", new ImageIcon("icon/polyIcon.jpg")),
		eLine(new GLine(), "Line", new ImageIcon("icon/lineIcon.jpg"));

		private GShapeTool shapeTool;
		private String text;
		private ImageIcon icon;
		
		private EShapeTool(GShapeTool shapeTool, String text, ImageIcon icon) {
			this.shapeTool=shapeTool;
			this.text=text;
			this.icon=icon;
		}
		public GShapeTool getShapeTool() {
			return this.shapeTool;
		}
		public String getText() {
			return this.text;
		}
		public ImageIcon getIcon() {
			return this.icon;
		}
	}
	
	public enum EMenu {
		eFile("파일"),
		eEdit("편집"),
		eHelp("도움말");
		
		private String text;
		
		private EMenu(String text) {
			this.text=text;
		}
		public String getText() {
			return this.text;
		}
	}
	
	public enum EFileMenuItem {
		eNew("새로만들기", KeyStroke.getKeyStroke('N', Event.CTRL_MASK)),
		eOpen("열기", KeyStroke.getKeyStroke('O', Event.CTRL_MASK)),
		eSave("저장", KeyStroke.getKeyStroke('S', Event.CTRL_MASK)),
		eSaveAs("다름이름으로 저장", KeyStroke.getKeyStroke('D', Event.CTRL_MASK)),
		ePrint("프린트", KeyStroke.getKeyStroke('P', Event.CTRL_MASK)),
		eExit("나가기", null);
		
		private String text;
		private KeyStroke key;
		
		private EFileMenuItem(String text, KeyStroke key) {
			this.text=text;
			this.key=key;
		}
		public String getText() {
			return this.text;
		}
		public KeyStroke getKeyStroke() {
			return this.key;
		}
	}
	public enum EEditMenuItem {
		eUndo("실행 취소", KeyStroke.getKeyStroke('Z', Event.CTRL_MASK)),
		eRedo("다시 실행", KeyStroke.getKeyStroke('Y', Event.CTRL_MASK)),
		eCut("오려두기", KeyStroke.getKeyStroke('X', Event.CTRL_MASK)),
		eCopy("복사", KeyStroke.getKeyStroke('C', Event.CTRL_MASK)),
		ePaste("붙여넣기", KeyStroke.getKeyStroke('V', Event.CTRL_MASK)),
		eGroup("그룹화", KeyStroke.getKeyStroke('G', Event.CTRL_MASK)),
		eUngroup("그룹화 해제", KeyStroke.getKeyStroke('U', Event.CTRL_MASK)),
		eDelete("삭제", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		
		private String text;
		private KeyStroke key;
		
		private EEditMenuItem(String text, KeyStroke key) {
			this.text=text;
			this.key=key;
			
		}
		public String getText() {
			return this.text;
		}
		public KeyStroke getKeyStroke() {
			return this.key;
		}
	}
	
	
	public enum EPopupMenuItem{
		eDelete("삭제"),
		eCut("오려두기"),
		eCopy("복사"),
		ePaste("붙여넣기");
		
		private String text;
		
		private EPopupMenuItem(String text) {
			this.text= text;
		}
		public String getText() {
			return this.text;
		}
	}
}
