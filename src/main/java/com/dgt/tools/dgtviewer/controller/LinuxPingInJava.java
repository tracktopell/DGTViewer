package com.dgt.tools.dgtviewer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * com.nonex.multipingmonitor.main.LinuxPingInJava
 *
 * @author alfredo
 */
public class LinuxPingInJava {

	private class crunchifyResultFromCommand extends Thread {

		InputStream inputStream = null;

		// This abstract class is the superclass of all classes representing an input stream of bytes.
		crunchifyResultFromCommand(InputStream is, String type) {
			this.inputStream = is;
		}

		public void run() {
			String crunchifyString = null;
			try {

				// Reads text from a character-input stream, buffering characters so as to provide for the efficient reading of characters, arrays, and lines.
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				while ((crunchifyString = br.readLine()) != null) {
					System.out.println(crunchifyString);
				}
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	public crunchifyResultFromCommand getStreamResult(InputStream inputStream, String type) {
		return new crunchifyResultFromCommand(inputStream, type);
	}

	public static void main(String[] args) {

		// Returns the runtime object associated with the current Java application.
		Runtime crunchifyRuntime = Runtime.getRuntime();
		LinuxPingInJava rte = new LinuxPingInJava();
		crunchifyResultFromCommand crunchifyError, crunchifyResult;

		try {
			// Process proc = rt.exec("curl -v https://www.google.com");
			Process proc1 = crunchifyRuntime.exec("ping " + args[0]);
			crunchifyError = rte.getStreamResult(proc1.getErrorStream(), "ERROR");
			crunchifyResult = rte.getStreamResult(proc1.getInputStream(), "OUTPUT");
			crunchifyError.start();
			crunchifyResult.start();

			// Signals that an I/O exception of some sort has occurred.
		} catch (IOException exception) {
			exception.printStackTrace();
		}

	}
}
