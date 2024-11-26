/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Van Dao
 */
package smarthouse.ui;

import smarthouse.devices.DeviceManager;
import smarthouse.devices.SmartDevice;
import smarthouse.engergy.Battery;
import smarthouse.engergy.EnergyManager;
import smarthouse.engergy.EnergySource;
import smarthouse.log.CustomLogger;


import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.time.Duration;
import java.time.Instant;

public class UIManagingSmartObjects extends javax.swing.JFrame {
	public static final Logger logger = CustomLogger.getLogger();
//	private DeviceManagementUI deviceManagementUI;
	// get device and resource manamgerment value
    private EnergyManager energyManager;
	private DeviceManager deviceManager;
	//define 
	
	private List<String> sourceNames;
	private String[] sourceList;
	private SmartDevice fanDev = new SmartDevice("Living Room Fan", SmartDevice.EnergyType.DC);
	private SmartDevice coolerDev = new SmartDevice("Bedroom Cooler", SmartDevice.EnergyType.DC);
	private SmartDevice heaterDev = new SmartDevice("Living Room Heater", SmartDevice.EnergyType.AC);
	
	// timer
	private Instant oldStartFan;
	private Instant oldStartHeater;
	private Instant oldStartCooler;
	private Instant startFan;
	private Instant stopFan;
	private Instant startHeater;
	private Instant stopHeater;
	private Instant startCooler;
	private Instant stopCooler;
	private long timeTakenForFan = 0;
	private long timeTakenForHeater = 0;
	private long timeTakenForCooler = 0;
//	private Duration timeElapsedForFan;
	
    /**
     * Creates new form UIManagingSmartObjects
     */
    public UIManagingSmartObjects(EnergyManager energyManager) throws IOException {
    	this.energyManager = energyManager;
//    	this.deviceManager = deviceManager;
    	emsInit();
        initComponents();
        scaleImage();
    }
    
    //============for testing============================
    public javax.swing.JButton getOnButton(String name){
    	javax.swing.JButton onButton = null;
    	if(name == "fan") {
    		onButton = onFanButton;
    	} else if(name == "heater") {
    		onButton = onHeaterButton;
    	} else if(name == "cooler") {
    		onButton = onLightButton;
    	}
    	return onButton;
    }
    
    public javax.swing.JButton getOffButton(String name){
    	javax.swing.JButton offButton = null;
    	if(name == "fan") {
    		offButton = offFanButton;
    	} else if(name == "heater") {
    		offButton = offHeaterButton;
    	} else if(name == "cooler") {
    		offButton = offLightButton;
    	}
    	return offButton;
    }
    
    public SmartDevice getDevice(String name) {
    	SmartDevice device = null;
    	if(name == "fan") {
    		device = fanDev;
    	} else if(name == "heater") {
    		device = heaterDev;
    	} else if(name == "cooler") {
    		device = coolerDev;
    	}
    	return device;
    }
    
    public javax.swing.JLabel getlabelConsume(String name){
    	javax.swing.JLabel labelConsume = null;
    	if(name == "fan") {
    		labelConsume = labelFanConsum;
    	} else if(name == "heater") {
    		labelConsume = labelHeaterConsum;
    	} else if(name == "cooler") {
    		labelConsume = labelLightConsum;
    	}
    	return labelConsume;
    }
    
    public EnergyManager getEnergyManager() {
    	return energyManager;
    }
    //=====================================================
    
    
    public void scaleImage() throws IOException{
        //String currentPath = new java.io.File(".").getCanonicalPath();
        ImageIcon iconFan = new ImageIcon(getClass().getResource("/smarthouse/ui/icons/fanon.png"));
        ImageIcon iconLightBulb = new ImageIcon(getClass().getResource("/smarthouse/ui/icons/cooler.png"));
        ImageIcon iconHeater = new ImageIcon(getClass().getResource("/smarthouse/ui/icons/heater.png"));
        ImageIcon iconEnergy = new ImageIcon(getClass().getResource("/smarthouse/ui/icons/energy.png"));
        ImageIcon iconBackground = new ImageIcon(getClass().getResource("/smarthouse/ui/icons/smarthome2.jpg"));
        // scaling image fan
        Image imgFan = iconFan.getImage();
        Image imageScaleFan = imgFan.getScaledInstance(fan.getWidth(), fan.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon iconScaleFan = new ImageIcon(imageScaleFan);
        fan.setIcon(iconScaleFan);
        
        // scaling image light bulb
        Image imgLightBulb = iconLightBulb.getImage();
        Image imageScaleLightBulb = imgLightBulb.getScaledInstance(lightBulb.getWidth(), lightBulb.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon iconScaleLightBulb = new ImageIcon(imageScaleLightBulb);
        lightBulb.setIcon(iconScaleLightBulb);
        
        // scaling image heater
        Image imgHeater = iconHeater.getImage();
        Image imageScaleHeater = imgHeater.getScaledInstance(heater.getWidth(), heater.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon iconScaleHeater = new ImageIcon(imageScaleHeater);
        heater.setIcon(iconScaleHeater);
        
        // scaling image background
//        Image imgBackground = iconBackground.getImage();
//        Image imageScaleBackground = imgBackground.getScaledInstance(background.getWidth(), background.getHeight(), Image.SCALE_SMOOTH);
//        ImageIcon iconScaleBackground = new ImageIcon(imageScaleBackground);
//        background.setIcon(iconScaleBackground);
        
    }
    private void emsInit() {   	
    	//device manager
    	deviceManager = new DeviceManager();
    	deviceManager.addDevice(fanDev);
    	deviceManager.addDevice(heaterDev);
    	deviceManager.addDevice(coolerDev);
    	
    	
    	sourceNames = energyManager.getEnergySourceNames();
    	 
        sourceList = sourceNames.toArray(new String[sourceNames.size()]); 	
    	
    }
    
    public static void setWarningMsg(String text){
        Toolkit.getDefaultToolkit().beep();
        JOptionPane optionPane = new JOptionPane(text,JOptionPane.WARNING_MESSAGE);
        JDialog dialog = optionPane.createDialog("Warning!");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() throws IOException {

        
        energyButton = new javax.swing.JButton();
        devicesButton = new javax.swing.JButton();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        fan = new javax.swing.JLabel();
        fanSource = new javax.swing.JComboBox<>();
        onFanButton = new javax.swing.JButton();
        offFanButton = new javax.swing.JButton();
        heater = new javax.swing.JLabel();
        heaterSource = new javax.swing.JComboBox<>();
        onHeaterButton = new javax.swing.JButton();
        offHeaterButton = new javax.swing.JButton();
        lightBulb = new javax.swing.JLabel();
        lightSource = new javax.swing.JComboBox<>();
        onLightButton = new javax.swing.JButton();
        offLightButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labelFanRate = new javax.swing.JLabel();
        labelHeaterRate = new javax.swing.JLabel();
        labelFanConsum = new javax.swing.JLabel();
        labelLightRate = new javax.swing.JLabel();
        labelHeaterConsum = new javax.swing.JLabel();
        labelLightConsum = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Smart Hause");
        setSize(new java.awt.Dimension(100, 100));



        jTabbedPane.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        fan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        
        fanSource.setModel(new javax.swing.DefaultComboBoxModel<>(sourceList));
        

        onFanButton.setText("ON");
        onFanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onFanButtonActionPerformed(evt);
            }
        });

        offFanButton.setText("OFF");
        offFanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offFanButtonActionPerformed(evt);
            }
        });

        heater.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        heaterSource.setModel(new javax.swing.DefaultComboBoxModel<>(sourceList));

        onHeaterButton.setText("ON");
        onHeaterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onHeaterButtonActionPerformed(evt);
            }
        });

        offHeaterButton.setText("OFF");
        offHeaterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offHeaterButtonActionPerformed(evt);
            }
        });

        lightBulb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lightSource.setModel(new javax.swing.DefaultComboBoxModel<>(sourceList));

        onLightButton.setText("ON");
        onLightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onLightButtonActionPerformed(evt);
            }
        });

        offLightButton.setText("OFF");
        offLightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offLightButtonActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Time Taken");
        jLabel4.setOpaque(true);

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Consumption");
        jLabel5.setOpaque(true);

        labelFanRate.setBackground(new java.awt.Color(255, 255, 255));
        labelFanRate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelFanRate.setText("Fan:");
        labelFanRate.setOpaque(true);

        labelHeaterRate.setBackground(new java.awt.Color(255, 255, 255));
        labelHeaterRate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelHeaterRate.setText("Heater:");
        labelHeaterRate.setOpaque(true);

        labelFanConsum.setBackground(new java.awt.Color(255, 255, 255));
        labelFanConsum.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelFanConsum.setText("Fan:");
        labelFanConsum.setOpaque(true);

        labelLightRate.setBackground(new java.awt.Color(255, 255, 255));
        labelLightRate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelLightRate.setText("Cooler:");
        labelLightRate.setOpaque(true);

        labelHeaterConsum.setBackground(new java.awt.Color(255, 255, 255));
        labelHeaterConsum.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelHeaterConsum.setText("Heater:");
        labelHeaterConsum.setOpaque(true);

        labelLightConsum.setBackground(new java.awt.Color(255, 255, 255));
        labelLightConsum.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelLightConsum.setText("Cooler:");
        labelLightConsum.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(onLightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(offLightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(26, 26, 26)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lightBulb, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lightSource, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(59, 59, 59)
                            .addComponent(fan, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(59, 59, 59)
                            .addComponent(fanSource, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(33, 33, 33)
                            .addComponent(onFanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(offFanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 171, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(onHeaterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(offHeaterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(heater, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(heaterSource, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(182, 182, 182))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelFanRate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelHeaterRate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(73, 73, 73))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(labelLightRate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelLightConsum, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelFanConsum, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelHeaterConsum, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(85, 85, 85))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(heater, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(heaterSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(onHeaterButton)
                            .addComponent(offHeaterButton)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(fan, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(fanSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(onFanButton)
                            .addComponent(offFanButton))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lightBulb, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelFanRate)
                            .addComponent(labelFanConsum))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lightSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(onLightButton)
                            .addComponent(offLightButton)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelHeaterRate)
                            .addComponent(labelHeaterConsum))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelLightRate)
                            .addComponent(labelLightConsum))))
                .addGap(64, 64, 64))
        );
        
        jTabbedPane.addTab("Devices Management", jPanel2);
        
        this.energySourceMgmtPanel = new EnergySourceManagementUI(this.energyManager, this.deviceManager);

        jTabbedPane.addTab("Energy Source Management", this.energySourceMgmtPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onFanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onFanButtonActionPerformed
    	// TODO add your handling code here:
        int a = fanSource.getSelectedIndex();
        logger.info(String.format("Option value: %d", a));
        String sourceName = sourceList[a]; 
        String sourceID = energyManager.getEnergySourceIDByName(sourceName);
        
        logger.info(String.format("Fan ID: " + fanDev.getDeviceId()));       
        try {
        	deviceManager.getDeviceByID(fanDev.getDeviceId()).setEnergySourceID(sourceID);
        	if(energyManager.getEnergySourceByID(sourceID).getAvailableEnergy() > 0) {
        		deviceManager.turnOnDevice(fanDev, false);
            	onFanButton.setBackground(Color.GREEN);
            	offFanButton.setBackground(null);
                startFan = Instant.now();
        	}
        	else {
        		offFanButton.setBackground(Color.GREEN);
            	onFanButton.setBackground(null);
        		setWarningMsg("Energy source does not have sufficient energy");
        	}
        } catch(IllegalArgumentException e) {
        	setWarningMsg(e.getMessage());
        }
        
    }//GEN-LAST:event_onFanButtonActionPerformed

    private void offFanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offFanButtonActionPerformed
    	// TODO add your handling code here:
    	long checkDiffStartPoint = 0;
    	if(fanDev.isOn()) {
    		deviceManager.turnOffDevice(fanDev);
    		offFanButton.setBackground(Color.GREEN);
        	onFanButton.setBackground(null);
        	stopFan = Instant.now();
        	Duration timeElapsed = Duration.between(startFan, stopFan);
        	timeTakenForFan += (timeElapsed.toMillis())/1000;
        	oldStartFan = startFan;
        	labelFanRate.setText("Fan: " + timeTakenForFan + " s");
        	labelFanConsum.setText("Fan: " + round(fanDev.getConsumedEnergy(),2) + " kWh");
    	}
    	else {
    		if(startFan != null) {
	    		offFanButton.setBackground(Color.GREEN);
	        	onFanButton.setBackground(null);
	        	Duration startElapsed = Duration.between(startFan, oldStartFan);
    	    	checkDiffStartPoint = (startElapsed.toMillis())/1000;
	        	if(checkDiffStartPoint != 0) {
		        	stopFan = Instant.now();
		        	Duration timeElapsed = Duration.between(startFan, stopFan);
		        	timeTakenForFan += (timeElapsed.toMillis())/1000;
		        	oldStartFan = startFan;
		        	labelFanRate.setText("Fan: " + timeTakenForFan + " s");
	        	}
	        	else {
	        		labelFanRate.setText("Fan: " + timeTakenForFan + " s");
	        	}
	        	labelFanConsum.setText("Fan: " + round(fanDev.getConsumedEnergy(),2) + " kWh");
	    		setWarningMsg("Fan is already off");
    		}
    		else {
    			offFanButton.setBackground(Color.GREEN);
	        	onFanButton.setBackground(null);
	        	labelFanRate.setText("Fan: " + timeTakenForFan + " s");
	        	labelFanConsum.setText("Fan: " + round(fanDev.getConsumedEnergy(),2) + " kWh");
	    		setWarningMsg("Fan is already off");
    		}
    	}
    	
    }//GEN-LAST:event_offFanButtonActionPerformed

    private void onHeaterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onHeaterButtonActionPerformed
        // TODO add your handling code here:
    	int a = heaterSource.getSelectedIndex();
        logger.info(String.format("Option value: %d", a));
        String sourceName = sourceList[a]; 
        String sourceID = energyManager.getEnergySourceIDByName(sourceName);
        try {
            deviceManager.getDeviceByID(heaterDev.getDeviceId()).setEnergySourceID(sourceID);
            deviceManager.turnOnDevice(heaterDev, false);
            onHeaterButton.setBackground(Color.GREEN);
        	offHeaterButton.setBackground(null);
            startHeater = Instant.now();
            
        } catch(IllegalArgumentException e) {	
        	setWarningMsg(e.getMessage());
        }     
    }//GEN-LAST:event_onHeaterButtonActionPerformed

    private void offHeaterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offHeaterButtonActionPerformed
        // TODO add your handling code here:
    	long checkDiffStartPoint = 0;
    	if(heaterDev.isOn()) {
    		deviceManager.turnOffDevice(heaterDev);
	    	offHeaterButton.setBackground(Color.GREEN);
        	onHeaterButton.setBackground(null);
	    	stopHeater = Instant.now();
	    	Duration timeElapsed = Duration.between(startHeater, stopHeater);
	    	timeTakenForHeater += (timeElapsed.toMillis())/1000;
        	oldStartHeater = startHeater;
        	labelHeaterRate.setText("Heater: " + timeTakenForHeater + " s");
	    	labelHeaterConsum.setText("Heater: " + round(heaterDev.getConsumedEnergy(),2) + " kWh");
    	}
    	else {
    		if(startHeater != null) {
	    		offHeaterButton.setBackground(Color.GREEN);
	        	onHeaterButton.setBackground(null);
	        	Duration startElapsed = Duration.between(startHeater, oldStartHeater);
    	    	checkDiffStartPoint = (startElapsed.toMillis())/1000;
	        	if(checkDiffStartPoint != 0) {
		        	stopHeater = Instant.now();
		        	Duration timeElapsed = Duration.between(startHeater, stopHeater);
		        	timeTakenForHeater += (timeElapsed.toMillis())/1000;
		        	oldStartHeater = startHeater;
		        	labelHeaterRate.setText("Heater: " + timeTakenForHeater + " s");
	        	}
	        	else {
	        		labelHeaterRate.setText("Heater: " + timeTakenForHeater + " s");
	        	}
	        	labelHeaterConsum.setText("Heater: " + round(heaterDev.getConsumedEnergy(),2) + " kWh");
	    		setWarningMsg("Heater is already off");
    		}
    		else {
    			offHeaterButton.setBackground(Color.GREEN);
	        	onHeaterButton.setBackground(null);
	        	labelHeaterRate.setText("Heater: " + timeTakenForHeater + " s");
	        	labelHeaterConsum.setText("Heater: " + round(heaterDev.getConsumedEnergy(),2) + " kWh");
	    		setWarningMsg("Heater is already off");
    		}
    	}
    }//GEN-LAST:event_offHeaterButtonActionPerformed

    private void onLightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onLightButtonActionPerformed
        // TODO add your handling code here:
        int a = lightSource.getSelectedIndex();
        logger.info(String.format("Option value: %d", a));
        String sourceName = sourceList[a]; 
        String sourceID = energyManager.getEnergySourceIDByName(sourceName);
        try {
            deviceManager.getDeviceByID(coolerDev.getDeviceId()).setEnergySourceID(sourceID);
            if(energyManager.getEnergySourceByID(sourceID).getAvailableEnergy() > 0) {
            	deviceManager.turnOnDevice(coolerDev, false);
            	onLightButton.setBackground(Color.GREEN);
            	offLightButton.setBackground(null);
                startCooler = Instant.now();
        	}
        	else {
        		offLightButton.setBackground(Color.GREEN);
        		onLightButton.setBackground(null);
        		setWarningMsg("Energy source does not have sufficient energy");
        	}
        	
        } catch(IllegalArgumentException e) {	
        	setWarningMsg(e.getMessage());
        }                
        
    }//GEN-LAST:event_onLightButtonActionPerformed

    private void offLightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offLightButtonActionPerformed
        // TODO add your handling code here:
    	long checkDiffStartPoint = 0;
    	if(coolerDev.isOn()) {
    		deviceManager.turnOffDevice(coolerDev);
	    	offLightButton.setBackground(Color.GREEN);
        	onLightButton.setBackground(null);
	    	stopCooler = Instant.now();
	    	Duration timeElapsed = Duration.between(startCooler, stopCooler);
	    	timeTakenForCooler += (timeElapsed.toMillis())/1000;
        	oldStartCooler = startCooler;
        	labelLightRate.setText("Cooler: " + timeTakenForCooler + " s");
	    	labelLightConsum.setText("Cooler: " + round(coolerDev.getConsumedEnergy(),2) + " kWh");
    	}
    	else {
    		if(startCooler != null) {
	    		offLightButton.setBackground(Color.GREEN);
	        	onLightButton.setBackground(null);
	        	Duration startElapsed = Duration.between(startCooler, oldStartCooler);
    	    	checkDiffStartPoint = (startElapsed.toMillis())/1000;
	        	if(checkDiffStartPoint != 0) {
		        	stopCooler = Instant.now();
		        	Duration timeElapsed = Duration.between(startCooler, stopCooler);
		        	timeTakenForCooler += (timeElapsed.toMillis())/1000;
		        	oldStartCooler = startCooler;
		        	labelLightRate.setText("Cooler: " + timeTakenForCooler + " s");
	        	}
	        	else {
	        		labelLightRate.setText("Cooler: " + timeTakenForCooler + " s");
	        	}
	        	labelLightConsum.setText("Cooler: " + round(coolerDev.getConsumedEnergy(),2) + " kWh");
	    		setWarningMsg("Cooler is already off");
    		}
    		else {
    			offLightButton.setBackground(Color.GREEN);
	        	onLightButton.setBackground(null);
	        	labelLightRate.setText("Cooler: " + timeTakenForCooler + " s");
	        	labelLightConsum.setText("Cooler: " + round(coolerDev.getConsumedEnergy(),2) + " kWh");
	    		setWarningMsg("Cooler is already off");
    		}
    	}
    }//GEN-LAST:event_offLightButtonActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton devicesButton;
    private javax.swing.JButton energyButton;
    private javax.swing.JLabel fan;
    private javax.swing.JComboBox<String> fanSource;
    private javax.swing.JLabel heater;
    private javax.swing.JComboBox<String> heaterSource;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
//    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private EnergySourceManagementUI energySourceMgmtPanel;
//    private javax.swing.JPanel jPanel3;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JLabel labelFanConsum;
    private javax.swing.JLabel labelFanRate;
    private javax.swing.JLabel labelHeaterConsum;
    private javax.swing.JLabel labelHeaterRate;
    private javax.swing.JLabel labelLightConsum;
    private javax.swing.JLabel labelLightRate;
    private javax.swing.JLabel lightBulb;
    private javax.swing.JComboBox<String> lightSource;
    private javax.swing.JButton offFanButton;
    private javax.swing.JButton offHeaterButton;
    private javax.swing.JButton offLightButton;
    private javax.swing.JButton onFanButton;
    private javax.swing.JButton onHeaterButton;
    private javax.swing.JButton onLightButton;
    // End of variables declaration//GEN-END:variables
    
    
}
