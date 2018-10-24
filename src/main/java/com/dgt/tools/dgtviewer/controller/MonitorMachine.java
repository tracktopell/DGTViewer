package com.dgt.tools.dgtviewer.controller;

import com.dgt.tools.dgtviewer.model.MachineModel;
import com.dgt.tools.dgtviewer.model.MonitorListUpdater;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JList;

/**
 *
 * com.nonex.multipingmonitor.main.MonitorMachine
 *
 * @author alfredo
 */
public class MonitorMachine {

	private MachineModel machineModel;
	private static MonitorListUpdater monitorListUpdater;

	boolean monitoring = false;
	private Thread currentThread = null;

	public MonitorMachine(String host, String label) {
		this.machineModel = new MachineModel(host, label);
		this.machineModel.setPosX(0);
		this.machineModel.setPosY(0);
		this.machineModel.setPosZ(0);
	}

	public MachineModel getMachineModel() {
		return machineModel;
	}
	
	public static void setMonitorListUpdater(MonitorListUpdater mlu) {
		monitorListUpdater = mlu;
	}

	class ProcessStreamConsummer extends Thread {

		InputStream inputStream = null;
		String std;

		public ProcessStreamConsummer(InputStream is, String std) {
			this.inputStream = is;
			this.std = std;
		}

		@Override
		public void run() {
			String isLine = null;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				while ((isLine = br.readLine()) != null) {
					//System.out.println(host+"/"+label+" - ["+std+"]"+isLine);
				}
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	public synchronized void stopMonitoring() {
		monitoring = false;
		if (monitorListUpdater != null) {
			monitorListUpdater.updateMachineList();
		}
	}

	public synchronized void stopMonitoring_NOW() {
		monitoring = false;
		if (currentThread != null && currentThread.isAlive()) {
			currentThread.interrupt();
			currentThread = null;
			if (monitorListUpdater != null) {
				monitorListUpdater.updateMachineList();;
			}
		}
	}

	public void startMonitoring() {
		if (monitoring) {
			throw new IllegalStateException("First call : stopMonitoring() || stopMonitoring_NOW()");
		}

		if (currentThread != null && currentThread.isAlive()) {
			try {
				System.err.println("\txxxxx >>> waiting for previus (" + currentThread + ") is still surring !");
				while (currentThread.isAlive()) {
					Thread.sleep(10);
					System.err.println("\t\txxxxx >>> waiting for terminate");
				}
			} catch (InterruptedException iew) {

			}
		}

		String threadName = this.machineModel.getHost() + "@" + System.currentTimeMillis();
		currentThread = new Thread(threadName) {
			@Override
			public void run() {
				Runtime rt = Runtime.getRuntime();
				try {

					final String osName = System.getProperty("os.name");

					System.err.println("===>>osName:" + osName);
					monitoring = true;
					Process proc1 = null;
					long t0, t1, dt;
					final long MAX_WAIT = 2000;
					machineModel.setStatus(MachineModel.STATUS_NOT_MONITORING);
					while (monitoring) {
						t0 = System.currentTimeMillis();
						if (osName.toLowerCase().contains("windows")) {
							System.out.println(" ====> WINDOWS --- EXCEC MONITOR");
							proc1 = rt.exec("ping /w 1000 /n 1 " + machineModel.getHost());
						} else {
							System.out.println(" ====> U N I X --- EXCEC MONITOR");
							proc1 = rt.exec("ping -W 1 -c 1 " + machineModel.getHost());
						}

						ProcessStreamConsummer stdErr = null;
						ProcessStreamConsummer stdOut = null;
						stdOut = new ProcessStreamConsummer(proc1.getInputStream(), "stdout");
						stdErr = new ProcessStreamConsummer(proc1.getErrorStream(), "stderr");

						stdOut.start();
						stdErr.start();

						int ev = proc1.waitFor();

						proc1.destroy();
						proc1 = null;
						t1 = System.currentTimeMillis();
						dt = t1 - t0;

						while (monitoring && dt < MAX_WAIT) {
							t1 = System.currentTimeMillis();
							dt = t1 - t0;
							Thread.sleep(100);
						}

						System.out.println(" >>>> [" + machineModel.getHost() + "/" + machineModel.getLabel() + "] " + ev);

						if (ev == 0) {
							machineModel.setStatus(MachineModel.STATUS_MONITORING_ONLINE);
						} else {
							machineModel.setStatus(MachineModel.STATUS_MONITORING_OFFLINE);
						}
						if (monitorListUpdater != null) {
							monitorListUpdater.updateMachineList();;
						}
					}
					System.out.println(" ----> END MONITOR");
					machineModel.setStatus(MachineModel.STATUS_END_MONITORING);
				} catch (IOException ioe) {
					//ioe.printStackTrace(System.err);
					machineModel.setStatus(MachineModel.STATUS_MONITORING_ERROR);
					System.out.println(" ----> I/O ERROR IN MONITOR");
				} catch (InterruptedException ie) {
					//ie.printStackTrace(System.err);
					machineModel.setStatus(MachineModel.STATUS_END_MONITORING);
					System.out.println(" ----> INTERRUPTED MONITOR");
				} finally {
					if (monitorListUpdater != null) {
						monitorListUpdater.updateMachineList();;
					}
				}
			}
		};

		currentThread.start();
	}

	@Override
	public String toString() {
		return this.machineModel.getHost() + "(" + this.machineModel.getLabel() + ") [" + this.machineModel.getStatusRendered() + "]";
	}

	public static void main(String[] args) {
		MonitorMachine mm = null;
		try {
			System.out.println(" =================================================== >>>");

			mm = new MonitorMachine(args[0], args[1]);

			System.out.println(" ----> mm1 start 1");
			mm.startMonitoring();

			Thread.sleep(10000);

			//mm.stopMonitoring();
			mm.stopMonitoring_NOW();

			System.out.println(" ----> mm1 start 2");
			mm.startMonitoring();

			Thread.sleep(10000);

			mm.stopMonitoring();
			System.out.println(" <<< ===================================================");

		} catch (InterruptedException ie) {
			ie.printStackTrace(System.err);
		}
	}

}
