package com.dgt.tools.dgtviewer.model;

import com.dgt.tools.dgtviewer.controller.MonitorMachine;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Wayssen
 */
public class MonitorMachineGroupModel implements ListModel<MonitorMachine>, MachineModelAccesor{

	private List<MonitorMachine> monitorMachineList;

	private List<ListDataListener> listDataListenerList;

	private JList jlist;

	public void add(String host, String label) {
		if (this.monitorMachineList == null) {
			this.monitorMachineList = new ArrayList<>();
		}
		MonitorMachine mmF=null;
		for(MonitorMachine mmx: monitorMachineList){
			if(mmx.getMachineModel().getHost().equalsIgnoreCase(host)){
				mmF=mmx;				
			} else{
				
			}
		}
		
		if(mmF == null){
			mmF=new MonitorMachine(host, label);
			//mmF.getMachineModel().generateRandomPos();
			monitorMachineList.add(mmF);
		}
	}

	public void stopMonitorAll() {
		for(MonitorMachine mm: monitorMachineList){
			mm.stopMonitoring();
		}
	}

	public MonitorMachine[] getMonitorAsArray() {
		if (this.monitorMachineList == null) {
			this.monitorMachineList = new ArrayList<>();
		}		
		MonitorMachine[] array = new MonitorMachine[this.monitorMachineList.size()];		
		this.monitorMachineList.toArray(array);		
		return array;
	}

	@Override
	public int getSize() {
		if (this.monitorMachineList == null) {
			this.monitorMachineList = new ArrayList<>();
		}
		return this.monitorMachineList.size();
	}

	@Override
	public MonitorMachine getElementAt(int index) {
		return monitorMachineList.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		if (this.listDataListenerList == null) {
			this.listDataListenerList = new ArrayList<>();
		}
		this.listDataListenerList.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		if (this.listDataListenerList == null) {
			this.listDataListenerList = new ArrayList<>();
		}
		this.listDataListenerList.remove(l);
	}

	@Override
	public Iterator<MachineModel> getMachineModelIterator() {
		if (this.listDataListenerList == null) {
			this.listDataListenerList = new ArrayList<>();
		}
		List<MachineModel> mmList=new ArrayList<MachineModel>();
		for(MonitorMachine mmx: monitorMachineList){
			mmList.add(mmx.getMachineModel());
		}
		
		return mmList.iterator();
	}
}
