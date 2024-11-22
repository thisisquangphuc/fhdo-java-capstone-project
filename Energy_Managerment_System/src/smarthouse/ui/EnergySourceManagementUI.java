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
import smarthouse.engergy.EnergyManager;
import smarthouse.engergy.EnergySource;
import smarthouse.engergy.EnergySource.EnergyType;
import smarthouse.log.CustomLogger;

@SuppressWarnings("serial")
public class EnergySourceManagementUI extends JPanel {
	
    /**
     * Creates new form EnergySourceManagementUI
     */
    public EnergySourceManagementUI(EnergyManager energyManager, DeviceManager deviceManager) { //throws IOException {
        this.energyManager = energyManager;
        this.deviceManager = deviceManager;
    	initComponents();
    }
	
	 /**
     * This method is called from within the constructor to initialize the form.
     */                         
    private void initComponents() {
    	List<String> sourceNames = this.energyManager.getEnergySourceNames();
    	List<String> energySourceIDs = energyManager.getEnergySourceIDs();

        this.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        this.setMaximumSize(new java.awt.Dimension(1500, 1000));
        
        // Create Panel Display for each energy source
    	for (int i=0; i<sourceNames.size(); i++) {
			EnergySource energySource = energyManager.getEnergySourceByID(energySourceIDs.get(i));
			Boolean hasBattery = isEnergySourceHasBattery(energySource);
			
			energySourceLabel.put(energySourceIDs.get(i), new javax.swing.JLabel());
    		
	        energySourcePanel[i] = new javax.swing.JPanel();
	        energyConsumedAmount[i] = new javax.swing.JLabel();
	        remainingBarSource[i] = new javax.swing.JProgressBar();
	        remainingLabel[i] = new javax.swing.JLabel();
	        deviceComsumingSource[i] = new javax.swing.JLabel();
	        removeSourceBtn[i] = new javax.swing.JButton();
	        chargeSourceBtn[i] = new javax.swing.JButton();
	
	        energySourcePanel[i].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
	        energySourcePanel[i].setName(energySourceIDs.get(i)); // NOI18N
	
	        energySourceLabel.get(energySourceIDs.get(i)).setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
	        energySourceLabel.get(energySourceIDs.get(i)).setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	        energySourceLabel.get(energySourceIDs.get(i)).setText(sourceNames.get(i));
	        energySourceLabel.get(energySourceIDs.get(i)).setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
	        energySourceLabel.get(energySourceIDs.get(i)).setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
	
	        energyConsumedAmount[i].setBackground(new java.awt.Color(255, 255, 255));
	        energyConsumedAmount[i].setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
	        if (sourceNames.get(i) == "Grid Power") {
	        	energyConsumedAmount[i].setText(
	        			String.format("Current energy consumed amount: \t %.3f kWh", energySource.getEnergyConsumed()));
	        } else {
	        	energyConsumedAmount[i].setText(
	        			String.format("Current energy consumed amount: \t %s kWh", getBatteryEnergyConsumed(energySource))); 
	        }
	        energyConsumedAmount[i].setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
	
	        // if pin battery or solar panel
	        remainingBarSource[i].setToolTipText("");
	        if (hasBattery) { 
	        	remainingBarSource[i].setValue((int)getBatteryPercentage(energySource));
	        }
	        remainingBarSource[i].setStringPainted(true);
	
	        remainingLabel[i].setText("Remaining amount:");
	        
	
	        deviceComsumingSource[i].setBackground(new java.awt.Color(255, 255, 255));
	        deviceComsumingSource[i].setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
	        deviceComsumingSource[i].setText("<html> Being consumed by:" +  
	        		this.getDeviceComsumingStr(energySource) + "</html>"); 
	        deviceComsumingSource[i].setVerticalAlignment(javax.swing.SwingConstants.TOP);
	
	        final JButton removeBtn = new JButton();
	        removeBtn.setBackground(new java.awt.Color(255, 153, 153));
	        removeBtn.setText("Remove");
	        removeBtn.setName(energySource.getSourceID());
	        removeBtn.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                removeSourceBtnActionPerformed(evt, removeBtn.getName());
	            }
	        });
	        removeSourceBtn[i] = removeBtn;
	
	        // if pin battery or solar panel 
	        final JButton chargeBtn = new JButton();
	        chargeBtn.setText("Charge");
	        chargeBtn.setName(energySource.getSourceID());
	        chargeBtn.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                chargeSourceBtnActionPerformed(evt, chargeBtn.getName());
	            }
	        });
	        chargeSourceBtn[i] = chargeBtn;
	        
	        if (hasBattery==true) {
		        javax.swing.GroupLayout energySourcePanelLayout = new javax.swing.GroupLayout(energySourcePanel[i]);
		        energySourcePanel[i].setLayout(energySourcePanelLayout);
		        energySourcePanelLayout.setHorizontalGroup(
		            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		            .addGroup(energySourcePanelLayout.createSequentialGroup()
		                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                    .addGroup(energySourcePanelLayout.createSequentialGroup()
		                        .addGap(10, 10, 10)
		                        .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                            .addComponent(deviceComsumingSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
		                            .addComponent(energyConsumedAmount[i], javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
		                            .addGroup(energySourcePanelLayout.createSequentialGroup()
		                                .addComponent(remainingLabel[i], javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		                                .addComponent(remainingBarSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		                                .addComponent(chargeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
		                    .addGroup(energySourcePanelLayout.createSequentialGroup()
		                        .addGap(113, 113, 113)
		                        .addComponent(energySourceLabel.get(energySourceIDs.get(i)), javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
		                        .addGap(35, 35, 35)
		                        .addComponent(removeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
		                .addContainerGap(14, Short.MAX_VALUE))
		        );
		        energySourcePanelLayout.setVerticalGroup(
		            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		            .addGroup(energySourcePanelLayout.createSequentialGroup()
		                .addContainerGap()
		                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                    .addComponent(energySourceLabel.get(energySourceIDs.get(i)))
		                    .addComponent(removeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
		                .addGap(20, 20, 20)
		                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                    .addComponent(remainingLabel[i])
		                    .addComponent(remainingBarSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
		                    .addComponent(chargeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                .addComponent(energyConsumedAmount[i])
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                .addComponent(deviceComsumingSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
		                .addContainerGap())
		        );
	        } else if (!(energySource.getSourceType().equals(EnergyType.GRID.name()))) {
	        	javax.swing.GroupLayout energySourcePanelLayout = new javax.swing.GroupLayout(energySourcePanel[i]);
		        energySourcePanel[i].setLayout(energySourcePanelLayout);
		        energySourcePanelLayout.setHorizontalGroup(
		            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		            .addGroup(energySourcePanelLayout.createSequentialGroup()
		                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                    .addGroup(energySourcePanelLayout.createSequentialGroup()
		                        .addGap(10, 10, 10)
		                        .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                            .addComponent(deviceComsumingSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
		                            .addComponent(energyConsumedAmount[i], javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
		                            .addGroup(energySourcePanelLayout.createSequentialGroup()
		                                .addComponent(remainingLabel[i], javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		                                .addComponent(remainingBarSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
//		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//		                                .addComponent(chargeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
		                    .addGroup(energySourcePanelLayout.createSequentialGroup()
		                        .addGap(113, 113, 113)
		                        .addComponent(energySourceLabel.get(energySourceIDs.get(i)), javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
		                        .addGap(35, 35, 35)
		                        .addComponent(removeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
		                .addContainerGap(14, Short.MAX_VALUE))
		        );
		        energySourcePanelLayout.setVerticalGroup(
			            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			            .addGroup(energySourcePanelLayout.createSequentialGroup()
			                .addContainerGap()
			                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
			                    .addComponent(energySourceLabel.get(energySourceIDs.get(i)))
			                    .addComponent(removeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
			                .addGap(30, 30, 30)
			                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
			                    .addComponent(remainingLabel[i])
			                    .addComponent(remainingBarSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
//			                    .addComponent(chargeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
			                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			                .addComponent(energyConsumedAmount[i])
			                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			                .addComponent(deviceComsumingSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
			                .addContainerGap())
			        );
	        } else {
	        	javax.swing.GroupLayout energySourcePanelLayout = new javax.swing.GroupLayout(energySourcePanel[i]);
		        energySourcePanel[i].setLayout(energySourcePanelLayout);
		        energySourcePanelLayout.setHorizontalGroup(
		            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		            .addGroup(energySourcePanelLayout.createSequentialGroup()
		                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                    .addGroup(energySourcePanelLayout.createSequentialGroup()
		                        .addGap(10, 10, 10)
		                        .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                            .addComponent(deviceComsumingSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
		                            .addComponent(energyConsumedAmount[i], javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
//		                            .addGroup(energySourcePanelLayout.createSequentialGroup()
//		                                .addComponent(remainingLabel[i], javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
//		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//		                                .addComponent(remainingBarSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
//		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//		                                .addComponent(chargeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
		                            ))
		                    .addGroup(energySourcePanelLayout.createSequentialGroup()
		                        .addGap(113, 113, 113)
		                        .addComponent(energySourceLabel.get(energySourceIDs.get(i)), javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
		                        .addGap(35, 35, 35)
		                        .addComponent(removeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
		                .addContainerGap(14, Short.MAX_VALUE))
		        );
		        energySourcePanelLayout.setVerticalGroup(
			            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			            .addGroup(energySourcePanelLayout.createSequentialGroup()
			                .addContainerGap()
			                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
			                    .addComponent(energySourceLabel.get(energySourceIDs.get(i)))
			                    .addComponent(removeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
			                .addGap(30, 30, 30)
//			                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//			                    .addComponent(remainingLabel[i])
//			                    .addComponent(remainingBarSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
//			                    .addComponent(chargeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
			                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			                .addComponent(energyConsumedAmount[i])
			                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			                .addComponent(deviceComsumingSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
			                .addContainerGap())
			        );
	        }
        
        	this.add(energySourcePanel[i]);
    	}
    	
    	updateEnergySourceUI();
    	
    	// Create timer to get and refresh data/info on UI periodically
        ActionListener displayUITask = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	logger.fine("Refresh UI data");
            	updateEnergySourceUI();
            }
        };
        Timer timer = new Timer(300 ,displayUITask);
        timer.setRepeats(true);
        timer.start();
    }
    
    
    // Update UI when data change
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
    		EnergySource energySource = energyManager.getEnergySourceByID(energySourceIDs.get(i));
			Boolean hasBattery = isEnergySourceHasBattery(energySource);
			
//    		energySourceLabel[i].setText(energyManager.getEnergySourceNames().get(i)); 
	        if (sourceNames.get(i) == "Grid Power") {
	        	energyConsumedAmount[i].setText(
	        			String.format("Current energy consumed amount: \t %.3f kWh", energySource.getEnergyConsumed()));
	        } else {
	        	energyConsumedAmount[i].setText(
	        			String.format("Current energy consumed amount: \t %s kWh", getBatteryEnergyConsumed(energySource))); 
	        }
	        
	        if (hasBattery) { 
	        	remainingBarSource[i].setValue((int)getBatteryPercentage(energySource));
	        }
	          
	        deviceComsumingSource[i].setText("<html> Being consumed by:" +  
	        		this.getDeviceComsumingStr(energySource) + "</html>"); 
    		
    		// if amount >=100 
    		// chargeSourceBtn[i].setText("Charge");
    	}
    	
    	//
        javax.swing.GroupLayout energySourceMgmtPanelLayout = new javax.swing.GroupLayout(this);
        this.setLayout(energySourceMgmtPanelLayout);
        
        // 
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
        
        //
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
    
	
    // Add new energy source to the system
//	private void addEnergySource() { // TODO
//		
//	}
    
    
    private synchronized double getBatteryEnergyConsumed(EnergySource energySource) {
    	JSONObject jsonStatusString = new JSONObject(energySource.getStatus());
    	String batteryStatus = jsonStatusString.getString("batteryStatus");
    	
    	if (!batteryStatus.contains("No Battery")) {    	
	    	String amountStatus = batteryStatus.substring(batteryStatus.indexOf("capacity"));
	    	String batteryCapacity = amountStatus.substring(
	    			 amountStatus.indexOf("=")+1, amountStatus.indexOf(","));
	    	logger.fine(String.format("[getBatteryEnergyConsumed()] %s battery capacity %s",
	    			energySource.getSourceName(),
	    			batteryCapacity));
	    	
	    	amountStatus = batteryStatus.substring(batteryStatus.indexOf("energy_level_kWh"));
	    	String energyLevel = amountStatus.substring(
	    			amountStatus.indexOf("=")+1, amountStatus.indexOf(","));
	    	logger.fine(String.format("[getBatteryEnergyConsumed()] %s battery energy_level_kWh %s",
	    			energySource.getSourceName(),
	    			energyLevel));
	    			
	    	double energyConsumed = Double.parseDouble(batteryCapacity)	- Double.parseDouble(energyLevel);	
	    	return energyConsumed; 
    	} else {
    		// [TODO] Handle error here
    		return -1;
    	}
    }
    
    
    private synchronized double getBatteryPercentage(EnergySource energySource) {
    	JSONObject jsonStatusString = new JSONObject(energySource.getStatus());
    	String batteryStatus = jsonStatusString.getString("batteryStatus");
    	
    	if (!batteryStatus.contains("No Battery")) {    	
	    	String amountStatus = batteryStatus.substring(batteryStatus.indexOf("energy_level_kWh"));
	    	String energyLevel = amountStatus.substring(
	    			amountStatus.indexOf("=")+1, amountStatus.indexOf(","));
//	    	logger.fine("[getBatteryPercentage()] battery energy_level_kWh " + energyLevel);
	    	
	    	batteryStatus = batteryStatus.substring(batteryStatus.indexOf("percentage"));    	
	    	logger.fine(String.format("[getBatteryPercentage()] %s battery percentage %s",
	    			energySource.getSourceName(),
	    			batteryStatus.substring(
	    					batteryStatus.indexOf("=")+1, batteryStatus.indexOf("%"))));
	    	
	    	double batteryPercentage = Double.parseDouble(batteryStatus.substring(
	    			batteryStatus.indexOf("=")+1, batteryStatus.indexOf("%")));;
	    	
	    	// If energy level fewer than 1% percent, return/display 1% 
	    	return ((Double.parseDouble(energyLevel)>0) && (batteryPercentage==0)) ? 1: batteryPercentage;
    	} else {
    		// [TODO] Handle error here 
    		return -1;
    	}
    }
     
    //
    private Boolean isEnergySourceHasBattery(EnergySource energySource) {
    	JSONObject jsonStatusString = new JSONObject(energySource.getStatus());
    	String batteryStatus = jsonStatusString.getString("batteryStatus");
    	
    	return !(batteryStatus.contains("No Battery"));
    }
    
    //
    private String getDeviceComsumingStr(EnergySource energySource) {
    	List<String> deviceNameList = this.deviceManager.getAllDevicesNames();
    	List<String> deviceIDList = this.deviceManager.getAllDevicesIDs();
    	String deviceListStr = "";
    	
    	for (int i=0; i<deviceNameList.size(); i++) {
//        	logger.fine(String.format("[getDeviceComsumingStr] %s - %s / %s", 
//        			deviceManager.getDeviceByID(deviceIDList.get(i)).getDeviceName(), 
//        			deviceManager.getDeviceByID(deviceIDList.get(i)).getEnergySourceID(), 
//        			energySource.getSourceID()));
        	
    		if (deviceManager.getDeviceByID(deviceIDList.get(i)).getEnergySourceID().equals(
    						energySource.getSourceID())) {
    			deviceListStr = deviceListStr + "<br/>" + deviceNameList.get(i);
    		}
    	}
    	return deviceListStr;
    }
    
    // Button event for charging battery/panel
    private synchronized void chargeSourceBtnActionPerformed(java.awt.event.ActionEvent evt, String sourceID) { // [TODO]                                              
        // TODO add your handling code here:
    	logger.fine("Charge Button pressed ");
//		if (btn.getText() == "Charge") {
//		// if remaining percentage < 100% 
//		btn.setText("Charging");  
//	 } else {
//		btn.setText("Charge");
//	 }
    }                                                

    
    // Button event for removing energy source
    private synchronized void removeSourceBtnActionPerformed(java.awt.event.ActionEvent evt, String sourceID) { // [TODO]                                               
        // TODO add your handling code here: 	
    	logger.info(String.format("Remove %s from energy source system - %s.", 
    			energyManager.getEnergySourceByID(sourceID).getSourceName(), this.getComponent(0).getName()));
    	logger.fine(String.format("Remove panel index %d", 
    			energyManager.getEnergySourceIDs().indexOf(sourceID)));
    	
    	this.remove(energyManager.getEnergySourceIDs().indexOf(sourceID));
    	energyManager.removeEnergySource(sourceID);
    	
    	logger.fine(String.format("%d - %d", 
    			this.getComponentCount(), energyManager.getEnergySourceNames().size()));
    	logger.fine(String.format("%s", this.getComponent(0).getName()));
    	
    	updateEnergySourceUI();
    	
    }  
    
	
	private EnergyManager energyManager;
	private DeviceManager deviceManager;
	private static final Logger logger = CustomLogger.getLogger();
    // Variables declaration 
    private javax.swing.JPanel[] energySourcePanel = new javax.swing.JPanel[50];
    private Map<String, javax.swing.JLabel> energySourceLabel = new HashMap<>();
    private javax.swing.JLabel[] remainingLabel = new javax.swing.JLabel[50];
    private javax.swing.JProgressBar[] remainingBarSource = new javax.swing.JProgressBar[50];
    private javax.swing.JLabel[] energyConsumedAmount = new javax.swing.JLabel[50];
    private javax.swing.JLabel[] deviceComsumingSource = new javax.swing.JLabel[50];
    private javax.swing.JButton[] chargeSourceBtn = new javax.swing.JButton[50];
    private javax.swing.JButton[] removeSourceBtn = new javax.swing.JButton[50];
}
