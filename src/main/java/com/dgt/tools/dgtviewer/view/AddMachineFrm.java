/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dgt.tools.dgtviewer.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author Wayssen
 */
public class AddMachineFrm extends javax.swing.JDialog {

	/**
	 * Creates new form AddMachineFrm
	 */
	public AddMachineFrm(JFrame parent, String tit) {
		super(parent, tit, true);
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtHost = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtLabel = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setTitle("Machine");

        jPanel1.setLayout(new java.awt.GridLayout(3, 1));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Host / IP  :");
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel2.add(jLabel1);

        txtHost.setColumns(10);
        jPanel2.add(txtHost);

        jPanel1.add(jPanel2);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Label :");
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel3.add(jLabel2);

        txtLabel.setColumns(10);
        jPanel3.add(txtLabel);

        jPanel1.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        btnSave.setText("Save");
        jPanel4.add(btnSave);

        btnCancel.setText("Cancel");
        jPanel4.add(btnCancel);

        jPanel1.add(jPanel4);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField txtHost;
    private javax.swing.JTextField txtLabel;
    // End of variables declaration//GEN-END:variables

	public JTextField getTxtHost() {
		return txtHost;
	}

	public JTextField getTxtLabel() {
		return txtLabel;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

}
