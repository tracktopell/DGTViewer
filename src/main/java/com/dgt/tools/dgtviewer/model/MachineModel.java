package com.dgt.tools.dgtviewer.model;

import java.io.Serializable;

/**
 *
 * @author Wayssen
 */
public class MachineModel implements Serializable {

	protected String host;
	protected String label;
	protected String statusRendered;
	protected int status;
	protected int posX;
	protected int posY;
	protected int posZ;

	public static final int STATUS_NOT_MONITORING = 0;
	public static final int STATUS_MONITORING_ONLINE = 1;
	public static final int STATUS_MONITORING_OFFLINE = 2;
	public static final int STATUS_MONITORING_ERROR = 3;
	public static final int STATUS_END_MONITORING = 4;

	public MachineModel(String host, String label) {
		this.host = host;
		this.label = label;
		this.status = 0;
		this.posX = 0;
		this.posY = 0;
		this.posZ = 0;
	}
	
	public void generateRandomPos(){
		posX = (int)(Math.random()*100);
		posY = (int)(Math.random()*100);
	}
	
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the statusRendered
	 */
	public String getStatusRendered() {
		if (status == STATUS_NOT_MONITORING) {
			statusRendered = "_";
		} else if (status == STATUS_MONITORING_ONLINE) {
			statusRendered = "*";
		} else if (status == STATUS_MONITORING_OFFLINE) {
			statusRendered = ".";
		} else if (status == STATUS_MONITORING_ERROR) {
			statusRendered = "X";
		} else if (status == STATUS_END_MONITORING) {
			statusRendered = "=";
		} else {
			statusRendered = "#";
		}
		return statusRendered;
	}

	/**
	 * @param statusRendered the statusRendered to set
	 */
	public void setStatusRendered(String statusRendered) {
		this.statusRendered = statusRendered;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the posX
	 */
	public int getPosX() {
		return posX;
	}

	/**
	 * @param posX the posX to set
	 */
	public void setPosX(int posX) {
		this.posX = posX;
	}

	/**
	 * @return the posY
	 */
	public int getPosY() {
		return posY;
	}

	/**
	 * @param posY the posY to set
	 */
	public void setPosY(int posY) {
		this.posY = posY;
	}

	/**
	 * @return the posZ
	 */
	public int getPosZ() {
		return posZ;
	}

	/**
	 * @param posZ the posZ to set
	 */
	public void setPosZ(int posZ) {
		this.posZ = posZ;
	}

}
