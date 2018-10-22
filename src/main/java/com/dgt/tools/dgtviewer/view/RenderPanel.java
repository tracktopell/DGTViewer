package com.dgt.tools.dgtviewer.view;

import com.dgt.tools.dgtviewer.model.MachineModel;
import com.dgt.tools.dgtviewer.model.MachineModelAccesor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;

/**
 *
 * @author alfredo
 */
public class RenderPanel extends javax.swing.JPanel {
	private double scale;
	private int    originScreenX;
	private int    originScreenY;
	private int    gridDiff1;
	private int    gridDiff2;
	private int    objectMaxBound;
	private int    maxFrame;
	private double radarAngle;
	private double radarAngleArch;
	private Color  gridBack1Color;
	private Color  gridBack2Color;
	private Color  gridBack3Color;
	private boolean radaring =false;
	private BufferedImage imgComputer;

	private MachineModelAccesor machineModelAccesor;
	//private List<MachineModel> machineModelList;
		
	/**
	 * Creates new form RenderPanel
	 */
	public RenderPanel() {
		initComponents();
		scale = 1.0;
		originScreenX  = 0;
		originScreenY  = 0;
		gridDiff1      = 10;
		gridDiff2      = 50;
		objectMaxBound = 80;
		maxFrame       = 600;
	
		gridBack1Color = new Color(10,60,15);
		gridBack2Color = new Color(5,80,5);
		gridBack3Color = new Color(30,180,30);
		radarAngle     = Math.random()*Math.PI;
		radarAngleArch = Math.PI/2;
		try{
			imgComputer = ImageIO.read(getClass().getResourceAsStream("/images/50px-Blue_computer_icon.png"));
		}catch(IOException ioe){
		
		}
		addComponentListener(
			new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {					
					originScreenX  = getWidth()/2;
					originScreenY  = getHeight()/2;		
					gridDiff1      = (int)Math.round(scale * 10);
					gridDiff2      = (int)Math.round(scale * 50);
					objectMaxBound = (int)Math.round(scale * 80);
				}
			}
		);
		new Thread(){
			@Override
			public void run() {
				radaring = true;
				while(radaring){
					try{
						steepRadar();
						Thread.sleep(25);
						if(isVisible()){
							repaint();
						}
					}catch(InterruptedException ie){
					}
				}
			}
		}.start();
	}
	
	public void stopRadaring(){
		radaring = false;
	};

	public void setMachineModelAccesor(MachineModelAccesor machineModelAccesor) {
		this.machineModelAccesor = machineModelAccesor;
	}

	public synchronized void steepRadar() {
		radarAngle -= Math.PI/180;
	}
	

	private void doDrawing(Graphics2D g2d) {
		g2d.setBackground(Color.BLACK);
        g2d.clearRect(0, 0, getWidth(), getHeight());
		
		originScreenX  = getWidth()/2;
		originScreenY  = getHeight()/2;		
		
		int maxArea=((getWidth()+getHeight())*3)/4;
		
		double ang=0;
		double rad=maxArea;				
		
		double iterAngle = radarAngle;
		int angSteeps = 10;
		double stepAngle  = radarAngleArch / angSteeps;
		Color iterColor = gridBack3Color;
		for(int i=0;i<angSteeps;i++){
			
			g2d.setColor(iterColor);
			iterAngle = radarAngle + i * stepAngle;
			g2d.fillArc (originScreenX - maxArea / 4, originScreenY - maxArea / 4, maxArea / 2,maxArea / 2,
					     (int) (Math.round(iterAngle*180/Math.PI)), (int) (Math.round(stepAngle*180/Math.PI)));
			iterColor = iterColor.darker();			
		}
//		g2d.setColor(Color.YELLOW);
//		g2d.drawArc     (originScreenX - maxArea / 4, originScreenY - maxArea / 4, maxArea / 2,maxArea / 2,
//				(int) (Math.round(radarAngle*180/Math.PI)), (int) (Math.round(radarAngleArch*180/Math.PI)));
//		
		int radX=0;
		int radY=0;
		
		radX = (int)(Math.round(Math.cos(radarAngle)*(maxArea/2)));
		radY = (int)(Math.round(Math.sin(radarAngle)*(maxArea/2)));
		
		g2d.setColor(Color.GREEN);
		g2d.drawLine(originScreenX, originScreenY, originScreenX + radX,originScreenY - radY);		
		
		
		
		g2d.setColor(gridBack1Color);		
		for(ang=0;ang<= 2*Math.PI; ang+= Math.PI/10){			
			radX=0;
			radY=0;
			radX= (int)(Math.cos(ang)*rad);
			radY= (int)(Math.sin(ang)*rad);
			g2d.drawLine(originScreenX, originScreenY, originScreenX + radX,originScreenY - radY);
		}
		
		g2d.setColor(gridBack2Color);
		for(int i=gridDiff2;i<=maxArea;i+=gridDiff2){
			g2d.drawOval(originScreenX -i/2, originScreenY - i/2, i,i);
		}
		
		/*
		g2d.setColor(gridBack3Color);
		g2d.drawLine(originScreenX,0,originScreenX,getHeight());
		g2d.drawLine(0,originScreenY,getWidth(),originScreenY);
		*/
		if(machineModelAccesor != null) {
			int relativePosX = 0;
			int relativePosY = 0;
		
			Iterator<MachineModel> machineModelIterator = machineModelAccesor.getMachineModelIterator();
			
			while(machineModelIterator.hasNext()){
				
				MachineModel mm= machineModelIterator.next();
				
				//System.out.println("[*]-->G @"+mm.getPosX()+","+mm.getPosY());				
				
				relativePosX = originScreenX + (int)Math.round(scale * (mm.getPosX()* maxFrame)/(200.0));
				relativePosY = originScreenY - (int)Math.round(scale * (mm.getPosY()* maxFrame)/(200.0));
				
				
				if(mm.getStatus()==MachineModel.STATUS_NOT_MONITORING){
					g2d.setColor(Color.GRAY);
				} else if(mm.getStatus()==MachineModel.STATUS_MONITORING_OFFLINE){
					g2d.setColor(Color.YELLOW);
				} else if(mm.getStatus()==MachineModel.STATUS_MONITORING_ONLINE){
					g2d.setColor(Color.GREEN);
				} else if(mm.getStatus()==MachineModel.STATUS_MONITORING_ERROR){
					g2d.setColor(Color.RED);
				} else if(mm.getStatus()==MachineModel.STATUS_END_MONITORING){
					g2d.setColor(Color.LIGHT_GRAY);
				}
				
				g2d.fillOval(
						relativePosX - objectMaxBound/2,relativePosY -objectMaxBound/2,
						objectMaxBound,objectMaxBound);				
				
				if(imgComputer != null){
					g2d.drawImage(imgComputer, 
							relativePosX - imgComputer.getWidth()/2,relativePosY  - imgComputer.getHeight()/2, null);
				} else{
					g2d.setColor(Color.WHITE);
					g2d.fillRect(relativePosX - objectMaxBound/4,relativePosY - objectMaxBound/4, objectMaxBound /2,objectMaxBound/2);
				}
				
				
				
			}
		}
		
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing((Graphics2D)g);
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
