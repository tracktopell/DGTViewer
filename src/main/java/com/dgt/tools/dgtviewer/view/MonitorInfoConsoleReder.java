package com.dgt.tools.dgtviewer.view;

import com.dgt.tools.dgtviewer.model.MonitorInfoConsole;
import javax.swing.JTextArea;

/**
 *
 * @author Wayssen
 */
public class MonitorInfoConsoleReder implements MonitorInfoConsole {

	private JTextArea txtArea;
	private static MonitorInfoConsoleReder instance;

	private MonitorInfoConsoleReder(JTextArea txtArea) {
		this.txtArea = txtArea;
	}

	public static MonitorInfoConsoleReder getInstance(JTextArea txtArea) {
		if (instance == null) {
			instance = new MonitorInfoConsoleReder(txtArea);
		}
		return instance;
	}

	@Override
	public void infoMessage(String mssg) {
		this.txtArea.append(mssg);
		this.txtArea.append("\n");
	}
}
