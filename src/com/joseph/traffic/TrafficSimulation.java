package com.joseph.traffic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.joseph.traffic.io.user.KeyHandler;
import com.joseph.traffic.io.user.MouseHandler;
import com.joseph.traffic.lanes.OldCurvedLane;
import com.joseph.traffic.lanes.OldStraightLane;
import com.joseph.traffic.lanes.StraightLane;
import com.joseph.traffic.objects.Car;
import com.joseph.traffic.roads.OldCurvedRoad;
import com.joseph.traffic.roads.OldStraightRoad;
import com.projecttriumph.engine.GameEngine;
import com.projecttriumph.engine.api.game.Game;
import com.projecttriumph.engine.api.game.IGameController;
import com.projecttriumph.engine.io.user.KeyInputHandler;
import com.projecttriumph.engine.rendering.ScreenManager;

@Game(gameID = "traffic-simulator", gameName = "Traffic Simulator", keyInput = KeyHandler.class, mouseInput = MouseHandler.class, version = "0.0.1", camera = TSCTEMP.class)
public class TrafficSimulation implements IGameController {
	
	private BasicStroke dashed;
	private BasicStroke line;
	private BasicStroke smol;
	
	private BufferedImage bi;
	
	private long ticks = 0;
	
	boolean u = true;
	
	int amount = -1;
	
	int x = 100;
	int y = 100;
	int w = 16;
	int h = 16;
	int cpx = x + w / 2;
	int cpy = y + h / 2;
	int start = 90;
	int theta = 90;
	
	OldStraightLane horizontal1 = new OldStraightLane(200 + 32 * 2, 200, false, 500);
	OldStraightLane vertical1 = new OldStraightLane(200, 200 + 16 * 2, true, -500);
	OldStraightLane horizontal2 = new OldStraightLane(200 + 32 * 2, 200 + 500 + 32 * 2, false, -500);
	OldStraightLane vertical2 = new OldStraightLane(200 + 500 + 32 * 2, 200 + 16 * 2, true, 500);
	
	OldCurvedLane curve1 = new OldCurvedLane(horizontal1.getLaneStartLocation(), vertical1.getLaneEndLocation(), vertical1.isVertical());
	OldCurvedLane curve2 = new OldCurvedLane(vertical1.getLaneStartLocation(), horizontal2.getLaneEndLocation(), horizontal2.isVertical());
	OldCurvedLane curve3 = new OldCurvedLane(horizontal2.getLaneStartLocation(), vertical2.getLaneEndLocation(), vertical2.isVertical());
	OldCurvedLane curve4 = new OldCurvedLane(vertical2.getLaneStartLocation(), horizontal1.getLaneEndLocation(), horizontal1.isVertical());
	
	OldStraightRoad verticalRoad1 = new OldStraightRoad(2, 3, 20*16, 20*16, true, 10*16);
	OldStraightRoad horizontalRoad1 = new OldStraightRoad(2, 3, 26*16, 14*16, false, -10*16);
	OldStraightRoad verticalRoad2 = new OldStraightRoad(3, 2, 36*16, 20*16, true, 10*16);
	OldStraightRoad horizontalRoad2 = new OldStraightRoad(3, 2, 26*16, 30*16, false, -10*16);
	
	OldCurvedRoad croad1 = new OldCurvedRoad(verticalRoad1, horizontalRoad1, false, false);
	OldCurvedRoad croad2 = new OldCurvedRoad(horizontalRoad1, verticalRoad2, true, false);
	OldCurvedRoad croad3 = new OldCurvedRoad(horizontalRoad2, verticalRoad2, false, false);
	OldCurvedRoad croad4 = new OldCurvedRoad(verticalRoad1, horizontalRoad2, true, true);
	
	Car car0 = new Car(vertical1);
	Car car1 = new Car(verticalRoad1.getForward()[0]);
	Car car2 = new Car(verticalRoad1.getForward()[1]);
	Car car3 = new Car(verticalRoad1.getBackward()[0]);
	Car car4 = new Car(verticalRoad1.getBackward()[1]);
	Car car5 = new Car(verticalRoad1.getBackward()[2]);
	
	public static ArrayList<StraightLane> lanes = new ArrayList<StraightLane>();
	
	@Override
	public void initialize() {
		GameEngine.ENGINE_LOGGER.warn("This is a warn");
		GameEngine.ENGINE_LOGGER.error("This is an error");
		
		this.line = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
		this.smol = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
		this.dashed = new BasicStroke(0.25f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] {10.0f, 10.0f}, 0.0f);
		try {
			bi = ImageIO.read(new File("C:/Users/Joseph/Desktop/16Grid.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		horizontal1.setNextLane(curve1);
		curve1.setNextLane(vertical1);
		vertical1.setNextLane(curve2);
		curve2.setNextLane(horizontal2);
		horizontal2.setNextLane(curve3);
		curve3.setNextLane(vertical2);
		vertical2.setNextLane(curve4);
		curve4.setNextLane(horizontal1);
		
		for (int i = 0; i < 2; i++) {
			verticalRoad1.getForward()[i].setNextLane(croad1.getForward()[i]);
			croad1.getForward()[i].setNextLane(horizontalRoad1.getForward()[i]);
			horizontalRoad1.getForward()[i].setNextLane(croad2.getForward()[i]);
			croad2.getForward()[i].setNextLane(verticalRoad2.getBackward()[1 - i]);
			verticalRoad2.getBackward()[1 - i].setNextLane(croad3.getBackward()[1 - i]);
			croad3.getBackward()[1 - i].setNextLane(horizontalRoad2.getBackward()[1 - i]);
			horizontalRoad2.getBackward()[1 - i].setNextLane(croad4.getForward()[i]);
			croad4.getForward()[i].setNextLane(verticalRoad1.getForward()[i]);
		}
		
		for (int i = 0; i < 3; i++) {
			verticalRoad1.getBackward()[i].setNextLane(croad4.getBackward()[2 - i]);
			croad4.getBackward()[2 - i].setNextLane(horizontalRoad2.getForward()[2 - i]);
			horizontalRoad2.getForward()[2 - i].setNextLane(croad3.getForward()[2 - i]);
			croad3.getForward()[2 - i].setNextLane(verticalRoad2.getForward()[2 - i]);
			verticalRoad2.getForward()[2 - i].setNextLane(croad2.getBackward()[2 - i]);
			croad2.getBackward()[2 - i].setNextLane(horizontalRoad1.getBackward()[i]);
			horizontalRoad1.getBackward()[i].setNextLane(croad1.getBackward()[i]);
			croad1.getBackward()[i].setNextLane(verticalRoad1.getBackward()[i]);
		}
	}

	@Override
	public void updateGame() {
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_R)) {
			ScreenManager.getInstance().getCamera().resetOffset();
			cpx = x + w / 2;
			cpy = y + h / 2;
		}
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_U)) {
			cpy--;
		}
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_J)) {
			cpy++;
		}
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_H)) {
			cpx--;
		}
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_K)) {
			cpx++;
		}
		if (KeyInputHandler.wasKeyPressedThisFrame(KeyEvent.VK_NUMPAD8)) {
			amount++;
		}
		if (KeyInputHandler.wasKeyPressedThisFrame(KeyEvent.VK_NUMPAD2)) {
			amount--;
		}
		if (KeyInputHandler.wasKeyPressedThisFrame(KeyEvent.VK_P)) {
			u = !u;
		}
		if (KeyInputHandler.wasKeyPressedThisFrame(KeyEvent.VK_1)) {
			car0.forward();
		}
		if (KeyInputHandler.wasKeyPressedThisFrame(KeyEvent.VK_2)) {
			car0.reverse();
		}
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_3)) {
			car3.update(true);
//			car2.update(true);
		}
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_4)) {
			car4.update(true);
		}
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_5)) {
			car5.update(true);
		}
		
		if (ticks > 141) {
			car0.update(u);
			car1.update(u);
			car2.update(u);
			car3.update(u);
			car4.update(u);
			car5.update(u);
		}
		ticks++;
	}

	@Override
	public void renderGame(Graphics2D g) {
        g.setColor(Color.CYAN);
		
		Stroke state = g.getStroke();
		
//		g.drawImage(bi, null, 1, 1);
		
		g.setStroke(dashed);
		g.drawLine(x    , y + h, x    , y + 300);
		g.drawLine(x + w, y + h, x + w, y + 300);
		
		g.drawLine(x + w + w, y    , x + 300, y    );
		g.drawLine(x + w + w, y + h, x + 300, y + h);
		
		
		g.setStroke(line);
		g.drawLine(x + w / 2, y + h    , x + w / 2  , y + 300  );
		
		g.drawLine(x + w + w, y + h / 2, x + w + 300, y + h / 2);
		
		g.setColor(Color.MAGENTA);
		QuadCurve2D q = new QuadCurve2D.Double(x + w / 2, y + h, cpx, cpy, x + w + w, y + h / 2);
		g.draw(q);
//		g.drawArc(x + w / 2, y + h / 2, w, h, start, theta);
		
		g.setStroke(smol);
		g.setColor(Color.ORANGE);
//		g.drawRect(x + w / 2, y + h / 2, w, h);
		
		g.setStroke(state);
		g.setColor(Color.GREEN);

        // the box
		g.fillRect(x    , y    , 1, 1);
		g.fillRect(x + w + w, y    , 1, 1);
		g.fillRect(x    , y + h, 1, 1);
		g.fillRect(x + w + w, y + h, 1, 1);

        // where the line is drawn
		g.fillRect(x + w / 2, y + h    , 1, 1);
		g.fillRect(x + w + w, y + h / 2, 1, 1);
		g.fillRect(cpx, cpy, 1, 1);
		
		
		horizontal1.drawLane(g);
		horizontal2.drawLane(g);
		vertical1.drawLane(g);
		vertical2.drawLane(g);
		curve1.drawLane(g);
		curve2.drawLane(g);
		curve3.drawLane(g);
		curve4.drawLane(g);
		
		switch (amount) {
			case 8:
				croad4.drawRoad(g);
			case 7:
				horizontalRoad2.drawRoad(g);
			case 6:
				croad3.drawRoad(g);
			case 5:
				verticalRoad2.drawRoad(g);
			case 4:
				croad2.drawRoad(g);
			case 3:
				horizontalRoad1.drawRoad(g);
			case 2:
				croad1.drawRoad(g);
			case 1:
				verticalRoad1.drawRoad(g);
			case 0:
				break;
			default:
				verticalRoad1.drawRoad(g);
				verticalRoad2.drawRoad(g);
				horizontalRoad1.drawRoad(g);
				horizontalRoad2.drawRoad(g);
				
				croad1.drawRoad(g);
				croad2.drawRoad(g);
				croad3.drawRoad(g);
				croad4.drawRoad(g);
		}
		
		for (StraightLane lane : lanes) {
			lane.drawLane(g);
		}
		
		
		car0.drawCar(g);
		car1.drawCar(g);
		car2.drawCar(g);
		car3.drawCar(g);
		car4.drawCar(g);
		car5.drawCar(g);
	}
}