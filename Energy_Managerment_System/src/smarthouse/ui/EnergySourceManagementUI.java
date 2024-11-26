package smarthouse.ui;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.*;

import smarthouse.devices.DeviceManager;
import smarthouse.devices.SmartDevice;
import smarthouse.engergy.EnergyManager;
import smarthouse.engergy.EnergySource;
import smarthouse.engergy.EnergySource.EnergyType;
import smarthouse.log.CustomLogger;

@SuppressWarnings("serial")
public class EnergySourceManagementUI extends javax.swing.JPanel {
	
    /**
     * Creates new form EnergySourceManagementUI
     */
    public EnergySourceManagementUI(EnergyManager energyManager, DeviceManager deviceManager) {  
        this.energyManager = energyManager;
        this.deviceManager = deviceManager;
    	initComponents();
    }
    
    public EnergySourceManagementUI(EnergyManager energyManager, DeviceManager deviceManager, Boolean timerStatus) { 
        this.energyManager = energyManager;
        this.deviceManager = deviceManager;
        this.isTimerOn = timerStatus;
    	initComponents();
    }
	
	 /**
     * This method is called from within the constructor to initialize the form.
     */                         
    private void initComponents() {
    	List<String> sourceNames = this.energyManager.getEnergySourceNames();
    	List<String> energySourceIDs = this.energyManager.getEnergySourceIDs();

        this.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        this.setMaximumSize(new java.awt.Dimension(1500, 1000));
        this.setPreferredSize(new java.awt.Dimension(1000, 500));
        
        // Create Panel Display for each energy source
    	for (int i=0; i<sourceNames.size(); i++) {
    		initEnergySourcePanel(sourceNames.get(i), energySourceIDs.get(i));
        
	        // After creating and setting properties for panel's components, add panel to EnergySourceManagement UI
        	this.add(energySourcePanel.get(energySourceIDs.get(i)));
    	}
    	
    	//
    	updateEnergySourceUI();
    	
        if (isTimerOn==true) {
	    	// Create timer to get and refresh data/info on UI periodically
	        ActionListener displayUITask = new ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	            	// logger.fine("Refresh UI data");
	            	updateEnergySourceUI();
	            }
	        };
	        Timer timer = new Timer(300 ,displayUITask);
	        timer.setRepeats(true);
        	timer.start();
        }
    }
    
    
    /**
     *  Update UI periodically for any changes of data/info
     */
    public synchronized void updateEnergySourceUI() {
    	List<String> sourceNames = this.energyManager.getEnergySourceNames();
    	List<String> energySourceIDs = energyManager.getEnergySourceIDs();
    	int numOfSourcePanel = this.getComponentCount();
    	// [TODO] raise error when mismatch size
    	if ((numOfSourcePanel != sourceNames.size()) || (numOfSourcePanel != energySourceIDs.size())) {
    		logger.severe(String.format(
    				"Mismatch between number of EnergySources and Energy Source Management UI Panel: %d - %d/%d",
    				numOfSourcePanel, sourceNames.size(), energySourceIDs.size()));
    	}
    	
    	// Update value for current available energy sources
    	for (int i=0; i< numOfSourcePanel; i++) {
			String energySourceID = energySourceIDs.get(i);
    		EnergySource energySource = energyManager.getEnergySourceByID(energySourceID);
			Boolean hasBattery = isEnergySourceHasBattery(energySource);
			JSONObject batteryStatus = this.getBatteryStatus(energySource);
			
			// Update energy consumed value
	        if (energySource.getSourceType().equals(EnergyType.GRID.name())) {
	        	energyConsumedAmount.get(energySourceID).setName(
	        			String.format("%.3f", energySource.getEnergyConsumed()));
	        } else {
	        	energyConsumedAmount.get(energySourceID).setName(
	        			String.format("%.3f", getBatteryEnergyConsumed(batteryStatus))); 
	        }
	        energyConsumedAmount.get(energySourceID).setText(
	        		String.format("Current energy consumed: %s kWh", energyConsumedAmount.get(energySourceID).getName()));
	        energyConsumedAmount.get(energySourceID).setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
	        
	        // Update battery source capacity and percentage value
	        if (hasBattery) { 
	        	batteryBarSource.get(energySourceID).setValue((int)getBatteryPercentage(batteryStatus));
	    	    batteryCapacity.get(energySourceID).setText("Battery Capacity: " + batteryStatus.getString("capacity") + " kWh");
	        }
	        
	        // Update list of devices using energy source
	        String deviceComsumingStr = this.getDeviceComsumingStr(energySource);
	        if (deviceComsumingStr.isBlank()) {
	        	deviceComsumingSource.get(energySourceID).setText("No device use this source.");
	        } else {
	        	deviceComsumingSource.get(energySourceID).setText("<html> Devices consume energy from this source:" +  
	        		deviceComsumingStr + "</html>"); 
	        }
    		
	        // Update charging status
	    	if (Boolean.parseBoolean(new JSONObject(energySource.getStatus()).getString("isRecharging")) == false) {
	    		chargeSourceBtn.get(energySourceID).setText("Charge");
	    		chargeSourceBtn.get(energySourceID).setBackground(new JButton().getBackground());
	    		batteryBarSource.get(energySourceID).setForeground(new JProgressBar().getForeground());
	    		
	    	} else {
	    		chargeSourceBtn.get(energySourceID).setText("Un-charge");
	    		chargeSourceBtn.get(energySourceID).setBackground(new java.awt.Color(102, 204, 0));
	    		batteryBarSource.get(energySourceID).setForeground(new java.awt.Color(102, 204, 0));
	    	}
    	}
    	
    	// Create Panel Layout
        javax.swing.GroupLayout energySourceMgmtPanelLayout = new javax.swing.GroupLayout(this);
        this.setLayout(energySourceMgmtPanelLayout);
        
        /**  --- SOURCE LAYOUT --- 
        *	[Source 0] 	[Source 1]
        *	[Source 2]	[Source 3]
        *	[Source 4]	...
        */
        // Set layout for COLUMNs (two columns)
        javax.swing.GroupLayout.ParallelGroup horizontalEvenGroup 	= energySourceMgmtPanelLayout.createParallelGroup(
        		javax.swing.GroupLayout.Alignment.LEADING, false);
        javax.swing.GroupLayout.ParallelGroup horizontalOddGroup 	= energySourceMgmtPanelLayout.createParallelGroup(
        		javax.swing.GroupLayout.Alignment.LEADING, false);
        for (int i=numOfSourcePanel-1; i>=0; i--) {
        	if((i%2)==1) {
        		horizontalOddGroup.addComponent(this.getComponent(i), javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE);
        	} else {
        		horizontalEvenGroup.addComponent(this.getComponent(i), javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE);
        	}
        }
        energySourceMgmtPanelLayout.setHorizontalGroup(
            energySourceMgmtPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(energySourceMgmtPanelLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                	.addGroup(horizontalEvenGroup)
                .addGap(20, 20, 20)
                .addGroup(horizontalOddGroup)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        
        // Set layout for ROWs
        javax.swing.GroupLayout.SequentialGroup verticalSeqGroup = energySourceMgmtPanelLayout.createSequentialGroup();
        javax.swing.GroupLayout.ParallelGroup[] verticalGroup = new javax.swing.GroupLayout.ParallelGroup[numOfSourcePanel/2+1]; 
        for (int i=0; i<numOfSourcePanel; i++) { 
        	if ((i%2)==0) verticalGroup[i/2] = energySourceMgmtPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false);
        	verticalGroup[i/2].addComponent(
        			this.getComponent(i), javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        }
        verticalSeqGroup.addContainerGap(20, Short.MAX_VALUE);
        for (int i=0; i<numOfSourcePanel; i++) { 
        	if ((i%2)==0) verticalSeqGroup.addGroup(verticalGroup[i/2]);
        	else if (i!=(numOfSourcePanel-1)) verticalSeqGroup.addGap(20, 20, 20);
        }
        verticalSeqGroup.addContainerGap(20, Short.MAX_VALUE);
        energySourceMgmtPanelLayout.setVerticalGroup(
            energySourceMgmtPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(verticalSeqGroup)
        );
    }
    
    
    /**
     * Get battery status of one energy source
     * @param energySource
     * @return batteryStatus (JSONOBject)
     */
    private JSONObject getBatteryStatus(EnergySource energySource) {
    	JSONObject jsonStatusString = new JSONObject(); 
    	String batteryStatus = (new JSONObject(energySource.getStatus())).getString("batteryStatus");
    	
    	if (!batteryStatus.contains("No Battery")) {    	
	    	String amountStatus = batteryStatus.substring(batteryStatus.indexOf("capacity"));
	    	String batteryCapacity = amountStatus.substring(
	    			 amountStatus.indexOf("=")+1, amountStatus.indexOf(","));
	    	
	    	amountStatus = batteryStatus.substring(batteryStatus.indexOf("energy_level_kWh"));
	    	String energyLevel = amountStatus.substring(
	    			amountStatus.indexOf("=")+1, amountStatus.indexOf(","));
	    	
	    	amountStatus = batteryStatus.substring(batteryStatus.indexOf("percentage"));    	
	    	String levelPercentage = amountStatus.substring(
	    			amountStatus.indexOf("=")+1, amountStatus.indexOf("%"));
	    	
	    	jsonStatusString.put("capacity", 			batteryCapacity);
	    	jsonStatusString.put("energy_level_kWh", 	energyLevel);
	    	jsonStatusString.put("percentage", 			levelPercentage);
    	} else {
    		// [TODO] 
    	}
    	// logger.fine(String.format("[getBatteryStatus()] %s BatteryStatus: %s", 
    	// 		energySource.getSourceName(), batteryStatus.toString()));
    	return jsonStatusString;
    }
    
    /**
     * Extract battery energy consumed amount from battery status 
     * @param energySource
     * @return energyConnsumed (Double)
     */
    private synchronized double getBatteryEnergyConsumed(JSONObject batteryStatus) {
    	if (!batteryStatus.isEmpty()) {    	
	    	String batteryCapacity = batteryStatus.getString("capacity");
	    	// logger.fine(String.format("[getBatteryEnergyConsumed()] battery capacity %s", batteryCapacity));
	    	
	    	String energyLevel = batteryStatus.getString("energy_level_kWh");
	    	// logger.fine(String.format("[getBatteryEnergyConsumed()] energy_level_kWh %s", energyLevel));
	    			
	    	double energyConsumed = Double.parseDouble(batteryCapacity)	- Double.parseDouble(energyLevel);	
	    	return energyConsumed; 
    	} else {
    		throw new IllegalArgumentException("Invalid input! BatteryStatus should must not be null.");
    	}
    }
    
    /**
     * Extract battery percentage from battery status
     * @param energySource
     * @return batteryPercentage (Double)
     */
    private synchronized double getBatteryPercentage(JSONObject batteryStatus) {
    	if (!batteryStatus.isEmpty()) {    	
	    	String energyLevel = batteryStatus.getString("energy_level_kWh");
	    	// logger.fine(String.format("[getBatteryPercentage()] energy_level_kWh %s", energyLevel));
	    	
	    	double batteryPercentage = Double.parseDouble(batteryStatus.getString("percentage"));
	    	// logger.fine(String.format("[getBatteryPercentage()] battery percentage %.3f", batteryPercentage));
	    	
	    	// If energy level fewer than 1% percent, return/display 1% 
	    	return ((Double.parseDouble(energyLevel)>0) && (batteryPercentage==0)) ? 1: batteryPercentage;
    	} else {
    		throw new IllegalArgumentException("Invalid input! BatteryStatus should must not be null.");
    	}
    }
     
    /**
     * Check if energy source has battery or not
     * @param energySource
     * @return (Boolean)
     */
    private Boolean isEnergySourceHasBattery(EnergySource energySource) {
    	JSONObject jsonStatusString = new JSONObject(energySource.getStatus());
    	String batteryStatus = jsonStatusString.getString("batteryStatus");
    	
    	return !(batteryStatus.contains("No Battery"));
    }
    
    /**
     * 
     * @param energySource
     * @return 
     */
    private String getDeviceComsumingStr(EnergySource energySource) {
    	List<String> deviceIDList = this.deviceManager.getAllDevicesIDs();
    	String deviceListStr = "";
    	
    	for (int i=0; i<deviceIDList.size(); i++) {
    		SmartDevice device = this.deviceManager.getDeviceByID(deviceIDList.get(i));
    		
    		if (device.getEnergySourceID() != null) {    		
	    		if (device.getEnergySourceID().equals(energySource.getSourceID())) {
	        		String deviceInfo = String.format("%s \t is %s", 
	    					device.getDeviceName(), 
	    					(device.isOn() ? "ON" : "OFF"));
	    			deviceListStr = deviceListStr + "<br/>" + deviceInfo;
	    		}
    		}
    	}
    	return deviceListStr;
    }
    
    /**
     *  Button event for charging battery/panel
     * @param evt
     * @param energySourceID
     */
    private synchronized void chargeSourceBtnActionPerformed(java.awt.event.ActionEvent evt, String energySourceID) {                                              
        // TODO add your handling code here:
//    	logger.fine(String.format("Charge Button pressed %s", energySourceID));
    	
//        // Code To popup an ERROR_MESSAGE Dialog.
//        JOptionPane.showMessageDialog(this, "Enter a valid Number", 
//                               "ERROR", JOptionPane.ERROR_MESSAGE);
    	
    	EnergySource energySource = this.energyManager.getEnergySourceByID(energySourceID);
    	JSONObject jsonStatusString = new JSONObject(energySource.getStatus());
    	Boolean isRecharging = Boolean.parseBoolean(jsonStatusString.getString("isRecharging"));
    	
    	logger.fine(String.format("[ChargeButtonPressed] %s Charging Status change from %s to other status.", energySource.getSourceName(), isRecharging));
    	this.energyManager.manageRecharging(energySourceID, !isRecharging);
    }                                                

    
    /**
     * Button event for removing energy source
     * @param evt
     * @param sourceID
     */
    private synchronized void removeSourceBtnActionPerformed(java.awt.event.ActionEvent evt, String sourceID) {                                               
        // TODO add your handling code here: 	
    	logger.fine(String.format("Remove Source %d", 
    			energyManager.getEnergySourceIDs().indexOf(sourceID)));
    	
    	// remove energy source panel from UI
    	this.remove(this.energySourcePanel.get(sourceID));
    	// remove components in each energy source panel
    	energySourcePanel.remove(sourceID);
        energySourceLabel.remove(sourceID);
        batteryLabel.remove(sourceID);
        batteryBarSource.remove(sourceID);
        batteryCapacity.remove(sourceID);
        energyConsumedAmount.remove(sourceID);
        deviceComsumingSource.remove(sourceID);
        chargeSourceBtn.remove(sourceID);
        removeSourceBtn.remove(sourceID);
    	// remove energy source from database
    	this.energyManager.removeEnergySource(sourceID);
    }  
    
    
    /**
     * 
     * @param sourceName
     * @param energySourceID
     */
    private void initEnergySourcePanel(String sourceName, String energySourceID) {
    	EnergySource energySource = this.energyManager.getEnergySourceByID(energySourceID);
		Boolean hasBattery = isEnergySourceHasBattery(energySource);	
		JSONObject batteryStatus = this.getBatteryStatus(energySource);
		
		// Init Elements of each Energy Source UI Panel
        energySourcePanel.put(energySourceID, new javax.swing.JPanel());  
		energySourceLabel.put(energySourceID, new javax.swing.JLabel()); 
        batteryLabel.put(energySourceID, new javax.swing.JLabel());
        batteryBarSource.put(energySourceID, new javax.swing.JProgressBar());
        batteryCapacity.put(energySourceID, new javax.swing.JLabel());
        energyConsumedAmount.put(energySourceID, new javax.swing.JLabel());
        deviceComsumingSource.put(energySourceID, new javax.swing.JLabel());
        chargeSourceBtn.put(energySourceID, new javax.swing.JButton());
        removeSourceBtn.put(energySourceID, new javax.swing.JButton());

        //
        energySourcePanel.get(energySourceID).setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        energySourcePanel.get(energySourceID).setName(energySourceID); // NOI18N

        //
        energySourceLabel.get(energySourceID).setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        energySourceLabel.get(energySourceID).setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        energySourceLabel.get(energySourceID).setText(sourceName);
        energySourceLabel.get(energySourceID).setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        energySourceLabel.get(energySourceID).setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        //
        if (hasBattery) { 
	        batteryCapacity.get(energySourceID).setBackground(new java.awt.Color(255, 255, 255));
	        batteryCapacity.get(energySourceID).setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
	        batteryCapacity.get(energySourceID).setText("Battery Capacity: " + batteryStatus.getString("capacity") + " kWh");
        }
        
        //
        energyConsumedAmount.get(energySourceID).setBackground(new java.awt.Color(255, 255, 255));
        energyConsumedAmount.get(energySourceID).setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
//        if (sourceName == "Grid Power") {
//        	energyConsumedAmount.get(energySourceID).setName(
//        			String.format("%.3f", energySource.getEnergyConsumed()));
//        } else {
//        	energyConsumedAmount.get(energySourceID).setName(
//        			String.format("%s", getBatteryEnergyConsumed(batteryStatus))); 
//        }
//        energyConsumedAmount.get(energySourceID).setText(
//        		String.format("Current energy consumed: %s kWh", energyConsumedAmount.get(energySourceID).getName()));
        energyConsumedAmount.get(energySourceID).setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        //
        // if pin battery or solar panel
//        batteryBarSource.get(energySourceID).setToolTipText("");
//        if (hasBattery) { 
//        	batteryBarSource.get(energySourceID).setValue((int)getBatteryPercentage(batteryStatus));
//        }
        batteryBarSource.get(energySourceID).setStringPainted(true);

        batteryLabel.get(energySourceID).setText("Battery Percentage:");
        
        //
//        String deviceComsumingStr = this.getDeviceComsumingStr(energySource);
        deviceComsumingSource.get(energySourceID).setBackground(new java.awt.Color(255, 255, 255));
        deviceComsumingSource.get(energySourceID).setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
//        if (deviceComsumingStr.isBlank()) {
//        	deviceComsumingSource.get(energySourceID).setText("No device use this source.");
//        } else {
//        	deviceComsumingSource.get(energySourceID).setText("<html>  Devices consume energy from this source: " +  
//        		deviceComsumingStr + "</html>"); 
//        }
        deviceComsumingSource.get(energySourceID).setVerticalAlignment(javax.swing.SwingConstants.TOP);
        
        //
        // if pin battery or solar panel 
        final JButton chargeBtn = new JButton();
        chargeBtn.setBackground(new JButton().getBackground());
        chargeBtn.setText("Charge");
        chargeBtn.setName(energySourceID);
        chargeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeSourceBtnActionPerformed(evt, chargeBtn.getName());
            }
        });
        chargeSourceBtn.put(energySourceID, chargeBtn);
        
        //
        final JButton removeBtn = new JButton();
        removeBtn.setBackground(new java.awt.Color(255, 153, 153));
        removeBtn.setText("Delete");
        removeBtn.setName(energySourceID);
        removeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSourceBtnActionPerformed(evt, removeBtn.getName());
            }
        });
        removeSourceBtn.put(energySourceID, removeBtn);

        //
        if (hasBattery==true) {
	        javax.swing.GroupLayout energySourcePanelLayout = new javax.swing.GroupLayout(energySourcePanel.get(energySourceID));
	        energySourcePanel.get(energySourceID).setLayout(energySourcePanelLayout);
	        energySourcePanelLayout.setHorizontalGroup(
	            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(energySourcePanelLayout.createSequentialGroup()
	                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(energySourcePanelLayout.createSequentialGroup()
	                        .addGap(10, 10, 10)
	                        .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(deviceComsumingSource.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
	                            .addComponent(energyConsumedAmount.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
	                            .addComponent(batteryCapacity.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
	                            .addGroup(energySourcePanelLayout.createSequentialGroup()
	                                .addComponent(batteryLabel.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                                .addComponent(batteryBarSource.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                                .addComponent(chargeSourceBtn.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))
	                    .addGroup(energySourcePanelLayout.createSequentialGroup()
	                        .addGap(113, 113, 113)
	                        .addComponent(energySourceLabel.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addGap(35, 35, 35)
	                        .addComponent(removeSourceBtn.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
	                .addContainerGap(14, Short.MAX_VALUE))
	        );
	        energySourcePanelLayout.setVerticalGroup(
	            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(energySourcePanelLayout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(energySourceLabel.get(energySourceID))
	                    .addComponent(removeSourceBtn.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(20, 20, 20)
	                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(batteryLabel.get(energySourceID))
	                    .addComponent(batteryBarSource.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(chargeSourceBtn.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(batteryCapacity.get(energySourceID))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(energyConsumedAmount.get(energySourceID))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(deviceComsumingSource.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addContainerGap())
	        );
        } else if (!(energySource.getSourceType().equals(EnergyType.GRID.name()))) {
        	javax.swing.GroupLayout energySourcePanelLayout = new javax.swing.GroupLayout(energySourcePanel.get(energySourceID));
	        energySourcePanel.get(energySourceID).setLayout(energySourcePanelLayout);
	        energySourcePanelLayout.setHorizontalGroup(
	            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(energySourcePanelLayout.createSequentialGroup()
	                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(energySourcePanelLayout.createSequentialGroup()
	                        .addGap(10, 10, 10)
	                        .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(deviceComsumingSource.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
	                            .addComponent(energyConsumedAmount.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
	                            .addGroup(energySourcePanelLayout.createSequentialGroup()
	                                .addComponent(batteryLabel.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                                .addComponent(batteryBarSource.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
//	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//	                                .addComponent(chargeSourceBtn.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
	                    .addGroup(energySourcePanelLayout.createSequentialGroup()
	                        .addGap(113, 113, 113)
	                        .addComponent(energySourceLabel.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addGap(35, 35, 35)
	                        .addComponent(removeSourceBtn.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
	                .addContainerGap(14, Short.MAX_VALUE))
	        );
	        energySourcePanelLayout.setVerticalGroup(
		            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		            .addGroup(energySourcePanelLayout.createSequentialGroup()
		                .addContainerGap()
		                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                    .addComponent(energySourceLabel.get(energySourceID))
		                    .addComponent(removeSourceBtn.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
		                .addGap(30, 30, 30)
		                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                    .addComponent(batteryLabel.get(energySourceID))
		                    .addComponent(batteryBarSource.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
//		                    .addComponent(chargeSourceBtn.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                .addComponent(energyConsumedAmount.get(energySourceID))
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                .addComponent(deviceComsumingSource.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
		                .addContainerGap())
		        );
        } else {
        	javax.swing.GroupLayout energySourcePanelLayout = new javax.swing.GroupLayout(energySourcePanel.get(energySourceID));
	        energySourcePanel.get(energySourceID).setLayout(energySourcePanelLayout);
	        energySourcePanelLayout.setHorizontalGroup(
	            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(energySourcePanelLayout.createSequentialGroup()
	                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(energySourcePanelLayout.createSequentialGroup()
	                        .addGap(10, 10, 10)
	                        .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(deviceComsumingSource.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
	                            .addComponent(energyConsumedAmount.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
//	                            .addGroup(energySourcePanelLayout.createSequentialGroup()
//	                                .addComponent(batteryLabel.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
//	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//	                                .addComponent(batteryBarSource.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
//	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//	                                .addComponent(chargeSourceBtn.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
	                            ))
	                    .addGroup(energySourcePanelLayout.createSequentialGroup()
	                        .addGap(113, 113, 113)
	                        .addComponent(energySourceLabel.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addGap(35, 35, 35)
	                        .addComponent(removeSourceBtn.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
	                .addContainerGap(14, Short.MAX_VALUE))
	        );
	        energySourcePanelLayout.setVerticalGroup(
		            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		            .addGroup(energySourcePanelLayout.createSequentialGroup()
		                .addContainerGap()
		                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                    .addComponent(energySourceLabel.get(energySourceID))
		                    .addComponent(removeSourceBtn.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
		                .addGap(30, 30, 30)
//		                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//		                    .addComponent(batteryLabel.get(energySourceID))
//		                    .addComponent(batteryBarSource.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
//		                    .addComponent(chargeSourceBtn.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                .addComponent(energyConsumedAmount.get(energySourceID))
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                .addComponent(deviceComsumingSource.get(energySourceID), javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
		                .addContainerGap())
		        );
        }
    }
    
    
	
    public Map<String, javax.swing.JPanel> getEnergySourcePanel() {
		return energySourcePanel;
	}

	public Map<String, javax.swing.JLabel> getEnergySourceLabel() {
		return energySourceLabel;
	}

	public Map<String, javax.swing.JButton> getChargeSourceBtn() {
		return chargeSourceBtn;
	}

	public Map<String, javax.swing.JButton> getRemoveSourceBtn() {
		return removeSourceBtn;
	}
	
	public Map<String, javax.swing.JLabel> getDeviceComsumingSource() {
		return deviceComsumingSource;
	}

	public void setTimerStatus(Boolean on) {
		this.isTimerOn = on;
	}

	
	// Variables declaration 
	private EnergyManager energyManager;
	private DeviceManager deviceManager;
	private Boolean isTimerOn = true;
	private static final Logger logger = CustomLogger.getLogger();

    private Map<String, javax.swing.JPanel> 		energySourcePanel 		= new HashMap<>();
    private Map<String, javax.swing.JLabel> 		energySourceLabel 		= new HashMap<>();
    private Map<String, javax.swing.JLabel> 		batteryLabel 			= new HashMap<>();
    private Map<String, javax.swing.JProgressBar> 	batteryBarSource 		= new HashMap<>();
    private Map<String, javax.swing.JLabel> 		batteryCapacity 		= new HashMap<>();
    private Map<String, javax.swing.JLabel> 		energyConsumedAmount 	= new HashMap<>();
    private Map<String, javax.swing.JLabel> 		deviceComsumingSource 	= new HashMap<>();
    private Map<String, javax.swing.JButton> 		chargeSourceBtn 		= new HashMap<>();
    private Map<String, javax.swing.JButton> 		removeSourceBtn 		= new HashMap<>();
}
