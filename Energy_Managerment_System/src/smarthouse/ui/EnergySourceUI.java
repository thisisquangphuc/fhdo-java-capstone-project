//package smarthouse.ui;
//
//import java.awt.EventQueue;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.border.EmptyBorder;
//import javax.swing.JLabel;
//import javax.swing.SwingConstants;
//import com.jgoodies.forms.factories.DefaultComponentFactory;
//
//import smarthouse.engergy.EnergySource;
//
//import java.awt.FlowLayout;
//
//import javax.swing.BorderFactory;
//import javax.swing.BoxLayout;
//import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.BorderLayout;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.CardLayout;
//import java.awt.Color;
//
//import javax.swing.GroupLayout;
//import javax.swing.JButton;
//import javax.swing.GroupLayout.Alignment;
//import javax.swing.JList;
//
//public class EnergySourceUI extends JFrame {
//
//	private static final long serialVersionUID = 1L;
////	private JPanel contentPane;
////	private final JLabel lblNewLabel = new JLabel("New label");
//	
//	private JPanel[] energyPanels 				= new JPanel[100];
//	private JLabel[] supplyAmountLabel 			= new JLabel[100];
//	private JLabel[] remainingPercentageLabel 	= new JLabel[100];
//	private JLabel[] deviceComsumingLabel		= new JLabel[100];
//	private JButton[] chargeBtn					= new JButton[100];
//	
//	private EnergySource[] energySources;
//
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				EnergySource[] energySources = new EnergySource[5];
//				for (int i=0; i<energySources.length; i++) {
//					energySources[i] = new EnergySource("EnergySource"+i);
//				}
//				try {
//					EnergySourceUI frame = new EnergySourceUI(energySources);
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	/**
//	 * Create the frame.
//	 */
//	public EnergySourceUI(EnergySource[] energySources) {
//		this.energySources = energySources;
//		
//		setTitle("Energy Source Management");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
//		
//		for (int i=0; i<energySources.length; i++) {
//			energyPanels[i] = new JPanel();
//	    	energyPanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
//	    	energyPanels[i].setAlignmentX(Component.CENTER_ALIGNMENT);
//	    	energyPanels[i].setPreferredSize(new Dimension(500, 180));
//	    	energyPanels[i].setMaximumSize(new Dimension(500, 180));
//	    	energyPanels[i].setBorder(BorderFactory.createTitledBorder("EnergySource" + i)); // getEnergySourceName
//	    	getContentPane().add(energyPanels[i]);
//	    	  
//	    	//
//	    	supplyAmountLabel[i] = new JLabel();
//	    	supplyAmountLabel[i].setPreferredSize(new Dimension(230, 25));
//	    	supplyAmountLabel[i].setMaximumSize(new Dimension(230, 25));
//	    	supplyAmountLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLUE));
//	    	supplyAmountLabel[i].setText("Current Supply Amount: " + Math.round(100*Math.random()%1000) + "kWh"); // getSupplyAmount
//	    	supplyAmountLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
//	    	energyPanels[i].add(supplyAmountLabel[i]);
//	    	  
//	    	// 	
//	    	remainingPercentageLabel[i] = new JLabel();
//	    	remainingPercentageLabel[i].setPreferredSize(new Dimension(230, 25));
//	    	remainingPercentageLabel[i].setMaximumSize(new Dimension(230, 25));
//	    	remainingPercentageLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLUE));
//	    	// if battery/panel, display remaining amount	
//	    	if (i>2) {
//	    		remainingPercentageLabel[i].setText("Remaining Percentage: " + energySources[i].getA() + "%"); // getRemainedAmount
//	    	} else {
//	    		remainingPercentageLabel[i].setText("Remaining Percentage: -");
//	    	}
//	    	remainingPercentageLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
//	    	energyPanels[i].add(remainingPercentageLabel[i]);
//	    	  
//	    	//
//	    	deviceComsumingLabel[i] = new JLabel();
//	    	deviceComsumingLabel[i].setPreferredSize(new Dimension(230, 100));
//	    	deviceComsumingLabel[i].setMaximumSize(new Dimension(230, 100));
//	    	deviceComsumingLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLUE));
//	    	deviceComsumingLabel[i].setText("<html>Being Consumed by <br> Device A <br> Device B <br> Device C</html>"); // getDevices
//	    	deviceComsumingLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
//	    	energyPanels[i].add(deviceComsumingLabel[i]);
//	    	  
//	    	
//	    	// if battery/panel, display charge option
//	    	if (i>2) {
//	    		JButton btn = new JButton();
//	    		btn.setText("Charge");
//	    		btn.setPreferredSize(new Dimension(230, 25));
//	    		btn.setMaximumSize(new Dimension(230, 25));
//	    		btn.addActionListener(new ActionListener() {
//	    			@Override
//					public void actionPerformed(ActionEvent e) {
//						if (btn.getText() == "Charge") {
//							// if remaining percentage < 100% 
//				    		btn.setText("Charging");  
//				    	 } else {
//							btn.setText("Charge");
//				    	 }	
//					}
//	    		});
//	    		chargeBtn[i] = btn;
//	    		energyPanels[i].add(chargeBtn[i]);
//	    	}
//		}
//		
//		setBounds(100, 100, 600, 1200);
//	}
//
//}
