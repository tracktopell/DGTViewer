package com.dgt.tools.dgtviewer.view;

import com.dgt.tools.dgtviewer.model.MachineModel;
import com.dgt.tools.dgtviewer.model.MachineModelAccesor;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;

/**
 *
 * @author alfredo
 */
public class RenderPanel extends javax.swing.JPanel implements MouseListener, MouseMotionListener {

	private double scale;
	private int originScreenX;
	private int originScreenY;
	private int gridDiff1;
	private int gridDiff2;
	private int objectMaxBound;
	private int maxFrame;
	private double radarAngle;
	private double radarAngleArch;
	private Color gridBack1Color;
	private Color gridBack2Color;
	private Color gridBack3Color;
	private boolean radaring = false;
	private Image imgComputer;
	private Image imgBackgound;
	
	private static final int STATUS_DRAGING_STATIC    = 0;
	private static final int STATUS_DRAGING_CLICKED   = 1;
	private static final int STATUS_DRAGING_DRAGGING  = 2;
	
	private MachineModel mmMV = null; 
	
	private int   objectStatusDragging;
	private int   objectSDX;
	private int   objectSDY;
	private int   objectMVX;
	private int   objectMVY;

	private MachineModelAccesor machineModelAccesor;
	//private List<MachineModel> machineModelList;

	/**
	 * Creates new form RenderPanel
	 */
	public RenderPanel() {
		initComponents();
		scale = 1.0;
		originScreenX = 0;
		originScreenY = 0;
		gridDiff1 = 10;
		gridDiff2 = 50;
		objectMaxBound = 20;
		maxFrame = 600;
		
		objectSDX  = -1;
		objectSDY  = -1;
		objectStatusDragging = STATUS_DRAGING_STATIC;

		gridBack1Color = new Color(100, 180, 100);
		gridBack2Color = new Color(180, 210, 180);
		gridBack3Color = new Color(80, 240, 80);
		radarAngle = Math.random() * Math.PI;
		radarAngleArch = Math.PI / 2;
		try {
			imgComputer = ImageIO.read(getClass().getResourceAsStream("/images/20px-Blue_computer_icon.png"));
		} catch (IOException ioe) {

		}
		addComponentListener(
				new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				originScreenX = getWidth() / 2;
				originScreenY = getHeight() / 2;
				gridDiff1 = (int) Math.round(scale * 10);
				gridDiff2 = (int) Math.round(scale * 50);
				objectMaxBound = (int) Math.round(scale * 20);
			}
		}
		);
		new Thread() {
			@Override
			public void run() {
				radaring = true;
				while (radaring) {
					try {
						steepRadar();
						Thread.sleep(25);
						if (isVisible()) {
							repaint();
						}
					} catch (InterruptedException ie) {
					}
				}
			}
		}.start();
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void setImgBackgound(BufferedImage imgBackgound) {
		this.imgBackgound = imgBackgound;
	}

	public void stopRadaring() {
		radaring = false;
	}

	;

	public void setMachineModelAccesor(MachineModelAccesor machineModelAccesor) {
		this.machineModelAccesor = machineModelAccesor;
	}

	public synchronized void steepRadar() {
		radarAngle -= Math.PI / 180;
	}

	private void doDrawing(Graphics2D g2d) {
		originScreenX = getWidth() / 2;
		originScreenY = getHeight() / 2;
		Composite ac = null;
		Composite acBefore = g2d.getComposite();
		try {
			g2d.setBackground(Color.BLACK);
			g2d.clearRect(0, 0, getWidth(), getHeight());

			if (imgBackgound != null) {
				/*
				if(imgBackgound.getWidth(null) != getWidth()){					
					double bgRatio = ((double)imgBackgound.getWidth(null) / (double)getWidth());
					int    bgW     = (int)Math.round(imgBackgound.getWidth (null) / bgRatio);
					int    bgH     = (int)Math.round(imgBackgound.getHeight(null) / bgRatio);
					System.out.println("\t--->> REZISE: bgWR="+bgRatio+", W="+getWidth()+", H="+getHeight()+",  bgW="+bgW+", bgH="+bgH);
					imgBackgound = imgBackgound.getScaledInstance(bgW, bgH, Image.SCALE_AREA_AVERAGING);
					System.out.println("\t\t--->> REZISE: bgWR="+imgBackgound.getWidth (null)+", W="+imgBackgound.getWidth (null));
				}
				*/
				g2d.drawImage(imgBackgound,
						(getWidth()  - imgBackgound.getWidth (null) ) / 2,
						(getHeight() - imgBackgound.getHeight(null) ) / 2,
						null);
			}

			int maxArea = ((getWidth() + getHeight()) * 3) / 4;

			double ang = 0;
			double rad = maxArea;

			double iterAngle = radarAngle;
			int angSteeps = 10;
			double stepAngle = radarAngleArch / angSteeps;
			Color iterColor = gridBack3Color;
			
			for (int i = 0; i < angSteeps; i++) {
				ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f - (5.0f*i/100.0f));
				g2d.setComposite(ac);
				
				g2d.setColor(iterColor);
				iterAngle = radarAngle + i * stepAngle;
				g2d.fillArc(originScreenX - maxArea / 4, originScreenY - maxArea / 4, maxArea / 2, maxArea / 2,
						(int) (Math.round(iterAngle * 180 / Math.PI)), (int) (Math.round(stepAngle * 180 / Math.PI)));
				iterColor = iterColor.brighter();
			}
			g2d.setComposite(acBefore);
			
			int radX = 0;
			int radY = 0;

			radX = (int) (Math.round(Math.cos(radarAngle) * (maxArea / 2)));
			radY = (int) (Math.round(Math.sin(radarAngle) * (maxArea / 2)));

			g2d.setColor(Color.GREEN);
			g2d.drawLine(originScreenX, originScreenY, originScreenX + radX, originScreenY - radY);

			g2d.setColor(gridBack1Color);
			for (ang = 0; ang <= 2 * Math.PI; ang += Math.PI / 10) {
				radX = 0;
				radY = 0;
				radX = (int) (Math.cos(ang) * rad);
				radY = (int) (Math.sin(ang) * rad);
				g2d.drawLine(originScreenX, originScreenY, originScreenX + radX, originScreenY - radY);
			}
			
			
			g2d.setColor(gridBack2Color);
			for (int i = gridDiff2; i <= maxArea; i += gridDiff2) {
				g2d.drawOval(originScreenX - i / 2, originScreenY - i / 2, i, i);
			}

			/*
			g2d.setColor(gridBack3Color);
			g2d.drawLine(originScreenX,0,originScreenX,getHeight());
			g2d.drawLine(0,originScreenY,getWidth(),originScreenY);
			 */
			
			if (machineModelAccesor != null) {
				int relativePosX = 0;
				int relativePosY = 0;

				Iterator<MachineModel> machineModelIterator = machineModelAccesor.getMachineModelIterator();

				while (machineModelIterator.hasNext()) {

					MachineModel mm = machineModelIterator.next();

					relativePosX = originScreenX + mm.getPosX();
					relativePosY = originScreenY - mm.getPosY();
					
					if (mm.getStatus() == MachineModel.STATUS_NOT_MONITORING) {
						g2d.setColor(Color.GRAY);
					} else if (mm.getStatus() == MachineModel.STATUS_MONITORING_OFFLINE) {
						g2d.setColor(Color.YELLOW);
					} else if (mm.getStatus() == MachineModel.STATUS_MONITORING_ONLINE) {
						g2d.setColor(Color.GREEN);
					} else if (mm.getStatus() == MachineModel.STATUS_MONITORING_ERROR) {
						g2d.setColor(Color.RED);
					} else if (mm.getStatus() == MachineModel.STATUS_END_MONITORING) {
						g2d.setColor(Color.LIGHT_GRAY);
					}
					
					Rectangle2D  stringBounds = g2d.getFontMetrics().getStringBounds(mm.getLabel(), g2d);
					int labelW = (int)stringBounds.getWidth();
					int labelH = (int)stringBounds.getHeight();
					
					g2d.drawString(mm.getLabel(), 
								relativePosX - (labelW/2), 
								relativePosY + objectMaxBound );

					g2d.fillRect(
								relativePosX - objectMaxBound / 2, 
								relativePosY - objectMaxBound / 2, 
								objectMaxBound, 
								objectMaxBound);										
					
					if(objectStatusDragging == STATUS_DRAGING_CLICKED){
						g2d.setColor(Color.BLUE);
						
						g2d.drawRect(
									(originScreenX + objectMVX ) - objectMaxBound / 2 - 4, 
									(originScreenY - objectMVY ) - objectMaxBound / 2 - 4, 
									objectMaxBound + 8, 
									objectMaxBound + 8);
					} else if(objectStatusDragging == STATUS_DRAGING_DRAGGING){
						g2d.setColor(Color.BLUE);
						g2d.drawRect(
									(originScreenX + objectMVX ) - objectMaxBound / 2 - 4, 
									(originScreenY - objectMVY ) - objectMaxBound / 2 - 4, 
									objectMaxBound + 8, 
									objectMaxBound + 8);
					} else if(objectStatusDragging == STATUS_DRAGING_STATIC){
						
					}
					
					if (imgComputer != null) {
						g2d.setColor(Color.RED);
						
						g2d.drawLine(
								relativePosX - objectMaxBound / 2, 
								relativePosY , 
								relativePosX + objectMaxBound / 2,
								relativePosY );
						g2d.drawLine(
								relativePosX , 
								relativePosY - objectMaxBound / 2, 
								relativePosX ,
								relativePosY + objectMaxBound / 2);
						
						g2d.drawImage(imgComputer,
								relativePosX - imgComputer.getWidth(null) / 2, 
								relativePosY - imgComputer.getHeight(null) / 2, null);
												
						g2d.drawRect(
								relativePosX - objectMaxBound / 2, 
								relativePosY - objectMaxBound / 2, 
								objectMaxBound, 
								objectMaxBound);

					} else {
						g2d.setColor(Color.RED);
						
						g2d.drawLine(
								relativePosX - objectMaxBound / 2, 
								relativePosY , 
								relativePosX + objectMaxBound / 2,
								relativePosY );
						g2d.drawLine(
								relativePosX , 
								relativePosY - objectMaxBound / 2, 
								relativePosX ,
								relativePosY + objectMaxBound / 2);
						
						g2d.setColor(Color.WHITE);
						g2d.fillRect(
								relativePosX - objectMaxBound / 2, 
								relativePosY - objectMaxBound / 2, objectMaxBound , objectMaxBound);
						
						
						g2d.drawRect(
								relativePosX - objectMaxBound / 2, 
								relativePosY - objectMaxBound / 2, 
								objectMaxBound, 
								objectMaxBound);
						
					}

				}

			}
		} catch (Exception e) {

		} finally {
			g2d.setComposite(acBefore);
		}

	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		doDrawing((Graphics2D) g);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

	private boolean isInArea( int mx,int my,int ox,int oy){
		if(	mx > (ox - objectMaxBound / 2) &&  mx < (ox + objectMaxBound / 2) && 
			my > (oy - objectMaxBound / 2) &&  my < (oy + objectMaxBound / 2) ){
			return true;
		} else{
			return false;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Iterator<MachineModel> machineModelIterator = machineModelAccesor.getMachineModelIterator();

		//System.out.println("==>mousePressed("+e.getX()+","+e.getY()+")");
		int relativePosX = 0;
		int relativePosY = 0;

		while (machineModelIterator.hasNext()) {
			
			MachineModel mm = machineModelIterator.next();
			
			relativePosX = originScreenX + mm.getPosX();
			relativePosY = originScreenY - mm.getPosY();					

			if(isInArea(e.getX(),e.getY(), relativePosX, relativePosY ) ){
				//System.out.println("\t[X]==>mousePressed("+e.getX()+","+e.getY()+") <= ["+relativePosX+","+relativePosY+"]");				
				objectStatusDragging = STATUS_DRAGING_CLICKED;
				
				objectSDX = mm.getPosX() + (e.getX() - relativePosX);
				objectSDY = mm.getPosY() + (e.getY() - relativePosY);
	
				objectMVX = objectSDX;
				objectMVY = objectSDY;

				mmMV = mm;
				break;
			}
			
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(objectStatusDragging >= STATUS_DRAGING_DRAGGING && mmMV != null){

			//System.out.println("==>AFTER STATUS_DRAGING_DRAGGING mouseReleased ("+e.getX()+","+e.getY()+")");
			int relativePosX = 0;
			int relativePosY = 0;

			MachineModel mm = mmMV;

			relativePosX = originScreenX + mm.getPosX();
			relativePosY = originScreenY - mm.getPosY();					

			//System.out.println("\t[*]==>mousePressed("+e.getX()+","+e.getY()+") <= ["+relativePosX+","+relativePosY+"]");

			mm.setPosX(objectMVX);
			mm.setPosY(objectMVY);

		}
		objectSDX = -1;
		objectSDY = -1;
		objectMVX = -1;
		objectMVY = -1;
		mmMV      = null;
		
		objectStatusDragging = STATUS_DRAGING_STATIC;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(objectStatusDragging > STATUS_DRAGING_STATIC){
			Iterator<MachineModel> machineModelIterator = machineModelAccesor.getMachineModelIterator();
			
			int relativePosX = 0;
			int relativePosY = 0;

			relativePosX = originScreenX + objectSDX;
			relativePosY = originScreenY - objectSDY;					

			objectStatusDragging = STATUS_DRAGING_DRAGGING;

			objectMVX = objectSDX + (e.getX() - relativePosX);
			objectMVY = objectSDY - (e.getY() - relativePosY);

			//System.out.println("\t[X]==>mouseDragged("+e.getX()+","+e.getY()+") <= ["+relativePosX+","+relativePosY+"] - - - > ("+objectSDX+","+objectSDY+")");
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
