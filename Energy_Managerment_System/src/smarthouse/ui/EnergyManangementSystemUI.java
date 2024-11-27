/**
 *
 */
package smarthouse.ui;

import java.awt.Image;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import smarthouse.engergy.EnergyManager;
import smarthouse.devices.DeviceManager;

@SuppressWarnings("serial")
public class EnergyManangementSystemUI extends javax.swing.JFrame {

    /**
     * Creates new form UIManagingSmartObjects
     */
    public EnergyManangementSystemUI(EnergyManager energyManager, DeviceManager deviceManager) throws IOException {
        this.energyManager = energyManager;
        this.deviceManager = deviceManager;
    	initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        energyButton = new javax.swing.JButton();
        devicesButton = new javax.swing.JButton();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(100, 100));

        jPanel1.setBackground(new java.awt.Color(51, 255, 255));

        energyButton.setText("Energy Source Management");
        energyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                energyButtonActionPerformed(evt);
            }
        });

        devicesButton.setText("Devices Management");
        devicesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devicesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(energyButton, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(devicesButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(devicesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(energyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(207, 207, 207))
        );

        jTabbedPane.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTabbedPane.addTab("Devices Management", jPanel2);

		this.energySourceMgmtPanel = new EnergySourceManagementUI(this.energyManager, this.deviceManager);
//        Thread energyUIThre2ad = new Thread (this.energySourceMgmtPanel);
//        energyUIThread.start();

        jTabbedPane.addTab("Energy Source Management", this.energySourceMgmtPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }

    private void energyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_energyButtonActionPerformed
        // TODO add your handling code here:
    	jTabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_energyButtonActionPerformed

    private void devicesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devicesButtonActionPerformed
        // TODO add your handling code here:
    	jTabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_devicesButtonActionPerformed


    private EnergySourceManagementUI energySourceMgmtPanel; 
    private EnergyManager energyManager;
    private DeviceManager deviceManager;
    // Variables declaration
    private javax.swing.JButton devicesButton;
    private javax.swing.JButton energyButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane;
}
