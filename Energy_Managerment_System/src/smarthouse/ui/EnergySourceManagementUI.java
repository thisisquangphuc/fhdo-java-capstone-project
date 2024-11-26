package smarthouse.ui;

import javax.swing.*;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import smarthouse.engergy.EnergyManager;
import smarthouse.engergy.EnergySource;
import smarthouse.log.CustomLogger;

@SuppressWarnings("serial")
public class EnergySourceManagementUI extends JPanel implements Runnable {
	
	private EnergyManager energyManager;
	private static final Logger logger = CustomLogger.getLogger();
	
    /**
     * Creates new form EnergySourceManagementUI
     */
    public EnergySourceManagementUI(EnergyManager energyManager) { //throws IOException {
        this.energyManager = energyManager;
    	initComponents();
    }
	
	 /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
    	List<String> sourceNames = this.energyManager.getEnergySourceNames();
    	List<String> energySourceIDs = energyManager.getEnergySourceIDs();

        this.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        this.setMaximumSize(new java.awt.Dimension(1500, 1000));
        
        // Create Panel Display for each energy source
    	for (int i=0; i<sourceNames.size(); i++) {
			EnergySource energySource = energyManager.getEnergySourceByID(energySourceIDs.get(i));
    		
	        energySourcePanel[i] = new javax.swing.JPanel();
	        energySourceLabel[i] = new javax.swing.JLabel();
	        energyConsumedAmount[i] = new javax.swing.JLabel();
	        remainingBarSource[i] = new javax.swing.JProgressBar();
	        remainingLabel[i] = new javax.swing.JLabel();
	        deviceComsumingSource[i] = new javax.swing.JLabel();
	        removeSourceBtn[i] = new javax.swing.JButton();
	        chargeSourceBtn[i] = new javax.swing.JButton();
	
	        energySourcePanel[i].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
	        energySourcePanel[i].setName(energySourceIDs.get(i)); // NOI18N
	
	        energySourceLabel[i].setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
	        energySourceLabel[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	        energySourceLabel[i].setText(sourceNames.get(i)); // [FIXME] energySource.getName()
	        energySourceLabel[i].setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
	        energySourceLabel[i].setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
	
	        energyConsumedAmount[i].setBackground(new java.awt.Color(255, 255, 255));
	        energyConsumedAmount[i].setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
	        if (sourceNames.get(i) == "Grid Power") {
	        	energyConsumedAmount[i].setText(
	        			String.format("Current energy consumed amount: \t %.3f kwH", energySource.getEnergyConsumed())); // [FIXME] getComsumedAmount
	        }
	        energyConsumedAmount[i].setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
	
	        // if pin battery or solar panel
	        remainingBarSource[i].setToolTipText("");
	        remainingBarSource[i].setValue((int)Math.round(100*Math.random()%100)); // [FIXME] getRemainingAmount
	        remainingBarSource[i].setStringPainted(true);
	
	        remainingLabel[i].setText("Remaining amount:");
	
	        deviceComsumingSource[i].setBackground(new java.awt.Color(255, 255, 255));
	        deviceComsumingSource[i].setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
	        deviceComsumingSource[i].setText("Being consumed by:"); // [FIXME] getDeviceComsuming
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
	
	        if (sourceNames.get(i) != "Grid Power") {
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
		                        .addComponent(energySourceLabel[i], javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
		                        .addGap(35, 35, 35)
		                        .addComponent(removeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
		                .addContainerGap(14, Short.MAX_VALUE))
		        );
		        energySourcePanelLayout.setVerticalGroup(
		            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		            .addGroup(energySourcePanelLayout.createSequentialGroup()
		                .addContainerGap()
		                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                    .addComponent(energySourceLabel[i])
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
//		                                .addComponent(remainingBarSource[i], javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
//		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//		                                .addComponent(chargeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
		                            ))
		                    .addGroup(energySourcePanelLayout.createSequentialGroup()
		                        .addGap(113, 113, 113)
		                        .addComponent(energySourceLabel[i], javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
		                        .addGap(35, 35, 35)
		                        .addComponent(removeSourceBtn[i], javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
		                .addContainerGap(14, Short.MAX_VALUE))
		        );
		        energySourcePanelLayout.setVerticalGroup(
			            energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			            .addGroup(energySourcePanelLayout.createSequentialGroup()
			                .addContainerGap()
			                .addGroup(energySourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
			                    .addComponent(energySourceLabel[i])
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
    }// </editor-fold>   
    
    
    // Update UI when data change
    public synchronized void updateEnergySourceUI() {
    	int numOfSourcePanel = this.getComponentCount();
    	
    	// Update value for current available energy sources
    	for (int i=0; i< numOfSourcePanel; i++) {
//    		energySourceLabel[i].setText(energyManager.getEnergySourceNames().get(i)); 
//    		remainingBarSource[i].setValue((int)Math.round(100*Math.random()%100)); // [FIXME] getRemainingAmount
//    		energyConsumedAmount[i].setText(
//    				String.format("Current energy consumed amount: \t %s kwH", Math.round(100*Math.random()%100))); // [FIXME] getComsumedAmount
    		deviceComsumingSource[i].setText("Being consumed by:"); // [FIXME] getDeviceComsuming
    		
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
//	public void addEnergySource() { // TODO
//		
//	}
    
    
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
    private synchronized void removeSourceBtnActionPerformed(java.awt.event.ActionEvent evt, String sourceID) {                                                 
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
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (this.isVisible()) logger.fine("Hi");
		while(true) {
			updateEnergySourceUI();
		}
	}
	
    // Variables declaration 
    private javax.swing.JPanel[] energySourcePanel = new javax.swing.JPanel[50];
    private javax.swing.JLabel[] energySourceLabel = new javax.swing.JLabel[50];
    private javax.swing.JLabel[] remainingLabel = new javax.swing.JLabel[50];
    private javax.swing.JProgressBar[] remainingBarSource = new javax.swing.JProgressBar[50];
    private javax.swing.JLabel[] energyConsumedAmount = new javax.swing.JLabel[50];
    private javax.swing.JLabel[] deviceComsumingSource = new javax.swing.JLabel[50];
    private javax.swing.JButton[] chargeSourceBtn = new javax.swing.JButton[50];
    private javax.swing.JButton[] removeSourceBtn = new javax.swing.JButton[50];
}
