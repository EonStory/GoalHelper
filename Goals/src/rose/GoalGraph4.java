package rose;
//Graphically shows the probability of you succeeding in a goal.
//Green = 100% success, red = 0% success. yellow = 50% success
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.ArrayList;
import javax.swing.Timer;

public class GoalGraph4 extends JFrame {
	public ArrayList<Goal> goals = new ArrayList<Goal>();	
	public GoalPanel goalPanel = new GoalPanel(800, 800, 100, 100, true);
	
	double xS = 0.001; //x scale
	double yS = 1.0; //y scale
	
	double workLoad1; 
	double timeLimit1;
	double workFraction1;
	
	public MouseMoveListener mouseMoveEars = new MouseMoveListener();
	public ButtonListener buttonListener = new ButtonListener();
	
	boolean isXRate = true;
	boolean isYLinear = true;
	
	JLabel workLoadLabel = new JLabel("Work Load: ");
	JLabel timeLimitLabel = new JLabel("Time Limit: ");
	JLabel workFractionLabel = new JLabel("Work Fraction: ");
	
	JTextField jtfTimeLimit = new JTextField();
	JTextField jtfWorkLoad = new JTextField();
	JRadioButton jrbSuccess = new JRadioButton("Sucess");
	JRadioButton jrbFailure = new JRadioButton("Failure");
	
	JButton jbEnter = new JButton("Enter");
	JButton jbLoad = new JButton("Load");
	JButton jbSave = new JButton("Save");
	
	JRadioButton xAxisRate = new JRadioButton("Rate");
	JRadioButton xAxisTotal = new JRadioButton("Total");
	JRadioButton yAxisLinear = new JRadioButton("Linear");
	JRadioButton yAxisLogarithmic = new JRadioButton("Logarithmic");
	
	final JFileChooser fc = new JFileChooser();
	
	public GoalGraph4() {
		//Create example goals
		goals.add(new Goal(2.0, 100.0, true));
		goals.add(new Goal(180, 200, false));
		goals.add(new Goal(250, 350, false));
		goals.add(new Goal(320.0, 460.0, true));
		goals.add(new Goal(300, 660.0, false));
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));			
		add(goalPanel);
		
		JPanel mouseInfoPanel = new JPanel();
		mouseInfoPanel.setLayout(new GridLayout(3, 1, 0, 0));
		mouseInfoPanel.setSize(200, 200);
		mouseInfoPanel.add(workLoadLabel);
		mouseInfoPanel.add(timeLimitLabel);
		mouseInfoPanel.add(workFractionLabel);
		mouseInfoPanel.setBorder(new LineBorder(Color.BLACK, 1));
		
		JPanel goalInputPanel = new JPanel();
		goalInputPanel.setLayout(new GridLayout(4, 2, 0, 0));
		goalInputPanel.add(new JLabel("Work Load"));
		goalInputPanel.add(jtfWorkLoad);
		goalInputPanel.add(new JLabel("Time Limit"));		
		goalInputPanel.add(jtfTimeLimit);			
		goalInputPanel.add(jrbSuccess);
		goalInputPanel.add(jrbFailure);				
		goalInputPanel.add(jbEnter);
		goalInputPanel.setBorder(new LineBorder(Color.BLACK, 1));
		
		//Radio buttons need to be in a group so only 1 at a time can be selected
		ButtonGroup statusBG = new ButtonGroup();
		statusBG.add(jrbSuccess);
		statusBG.add(jrbFailure);
		
		//Create load and save panel
		JPanel loadSavePanel = new JPanel();
		loadSavePanel.add(jbLoad);
		loadSavePanel.add(jbSave);
		loadSavePanel.setBorder(new LineBorder(Color.BLACK, 1));
		
		JPanel axisOptions = new JPanel();
		axisOptions.setLayout(new GridLayout(0, 2, 0, 0));
		axisOptions.add(new JLabel("x Axis Options"));
		axisOptions.add(new JLabel("")); //used to take up space in layout
		axisOptions.add(xAxisRate);
		axisOptions.add(xAxisTotal);
		axisOptions.add(new JLabel("y Axis Options"));
		axisOptions.add(new JLabel("")); //used for spacing
		axisOptions.add(yAxisLinear);
		axisOptions.add(yAxisLogarithmic);
		
		
		ButtonGroup xAxisOptionsBG = new ButtonGroup();		
		xAxisOptionsBG.add(xAxisRate);
		xAxisOptionsBG.add(xAxisTotal);
		xAxisRate.setSelected(true); //selected by default
		xAxisRate.addActionListener(buttonListener);
		xAxisTotal.addActionListener(buttonListener);
		
		ButtonGroup yAxisOptionsBG = new ButtonGroup();		
		yAxisOptionsBG.add(yAxisLinear);
		yAxisOptionsBG.add(yAxisLogarithmic);
		yAxisLinear.setSelected(true);
		yAxisLinear.addActionListener(buttonListener);
		yAxisLogarithmic.addActionListener(buttonListener);
		
		axisOptions.setBorder(new LineBorder(Color.BLACK, 1));		
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(4, 1, 0, 10));
		rightPanel.add(mouseInfoPanel);
		rightPanel.add(goalInputPanel);
		rightPanel.add(loadSavePanel);
		rightPanel.add(axisOptions);
		add(rightPanel);
		
		jbEnter.addActionListener(buttonListener);
		jbSave.addActionListener(buttonListener);
		jbLoad.addActionListener(buttonListener);		
		
	    goalPanel.addMouseMotionListener(mouseMoveEars);
	    
	   // fc.set
	    
	}
	
	class MouseMoveListener extends MouseMotionAdapter {
    	public void mouseMoved(MouseEvent e) {   
    		if (isXRate == true) {
    			timeLimit1 = goalPanel.height - e.getY();
        		workFraction1 = (double) (e.getX() - goalPanel.yAxisWidth) / goalPanel.width;
        		workLoad1 = workFraction1 * timeLimit1;    
    		}
    		else {
    			timeLimit1 = goalPanel.height - e.getY();        		
        		workLoad1 = (double) (e.getX() - goalPanel.yAxisWidth); 
        		workFraction1 = workLoad1 / timeLimit1;
    		}    		
    		workLoadLabel.setText("Work Load: " + String.format("%.2f", workLoad1));
    		timeLimitLabel.setText("Time Limit: " + String.format("%.1f", timeLimit1));
    		workFractionLabel.setText("Work Fraction: " + String.format("%.3f", workFraction1));
    	}
	}
	
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jbEnter) {
				goals.add(new Goal(Double.parseDouble(jtfWorkLoad.getText()), Double.parseDouble(jtfTimeLimit.getText()), jrbSuccess.isSelected()));
				goalPanel.repaint();
			}
			else if (e.getSource() == jbSave) {
				int returnVal = fc.showSaveDialog(GoalGraph4.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("Saved as: ");
					System.out.println(fc.getName(fc.getSelectedFile()));
					File targetFile = fc.getSelectedFile();
					//BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(targetFile));
				}				
			}
			else if (e.getSource() == jbLoad) {
				int returnVal = fc.showOpenDialog(GoalGraph4.this); // ???
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("APPROVED");
				}
			}
			else if (e.getSource() == xAxisRate || e.getSource() == xAxisTotal) {
				if (xAxisRate.isSelected()) {
					isXRate = true;
					goalPanel.repaint();
				}
				else {
					isXRate = false;
					goalPanel.repaint();
				}
			}
			else if (e.getSource() == yAxisLinear || e.getSource() == yAxisLogarithmic) {
				if (yAxisLinear.isSelected()) {
					isYLinear = true;
					goalPanel.repaint();
				}
				else {
					isYLinear = false;
					goalPanel.repaint();
				}
			}
			
		}
	}

	class Goal implements Serializable {
		double workLoad;
		double timeLimit;
		boolean status;
		
		public Goal(double workLoad, double timeLimit, boolean status) {
			this.workLoad = workLoad;
			this.timeLimit = timeLimit;
			this.status = status;
		}
	}
	
	public static void main(String[] args) {
		GoalGraph4 frame = new GoalGraph4();
		frame.setTitle("Goal Achievements");
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	//width and height are both scrollable. In the case of time, x wil not be scrollable
	public class GoalPanel extends JPanel {
		int width; //width of actual graph area
		int height; //width of actual graph area
		int xAxisHeight;
		int yAxisWidth;
		int axisSplits = 8; // number of points of measure on both the axes
		boolean isTime;
		
		public GoalPanel(int x, int y, int xAxisSize, int yAxisSize, boolean isTime) {
			width = x;
			height = y;
			this.xAxisHeight = xAxisSize;
			this.yAxisWidth = yAxisSize;
			this.isTime = isTime;
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			//draw axis
			for (int i = 0; i <= axisSplits; i++) {
				double xPixels = width / axisSplits;
				double yPixels = height / axisSplits;				
				//note: last line for 1 is not drawn because its outside of the panel but it doesnt matter
				//rename them Axis Width and Axis Height
				g.drawLine((int) (xAxisHeight + i * xPixels), height, (int) (xAxisHeight + i * xPixels), height + (int) (xAxisHeight * 0.1));				
				g.drawLine(yAxisWidth, (int) (height - i * yPixels), (int) (yAxisWidth * 0.9), (int) (height - i * yPixels));				
				
				
				//draw x axis numbers
				if (isXRate == true) {
					g.drawString(Double.toString(i / 8.0), (int) (xAxisHeight + i * xPixels) - 4, height + (int) (xAxisHeight * 0.3));
					g.drawString("Work Load / Time Limit", (width + xAxisHeight) / 2, height + (int) (0.5 * yAxisWidth));
				}
				else {
					g.drawString(Double.toString(xPixels * i), (int) (xAxisHeight + i * xPixels) - 4, height + (int) (xAxisHeight * 0.3));
					g.drawString("Work Load", (width + xAxisHeight) / 2, height + (int) (0.5 * yAxisWidth));
				}
				//draw y axis numbers
				g.drawString(Double.toString(yPixels * i * yS), yAxisWidth - 66, height - (int) (i * yPixels) + 6);
				
			}	
			
			
			g.drawString("Time Limit", 0, (int) (height * 0.55));
			
			
			//set origin to top left corner of actual graph bit
			g.translate(yAxisWidth, 0);
			
			//paint all individual pixels
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					g.setColor(computeColor(j, i));
					g.drawRect(j, i, 1, 1);
				}
			}
			
			//paint all goal strings
			/*
			g.setColor(new Color(33, 33, 255));
			g.setFont(new Font("Lucida Console", Font.BOLD, 15));
			for (int i = 0; i < goals.size(); i++) {
				String s = "WL: " + Double.toString(goals.get(i).workLoad);
				s += " TL: " + Double.toString(goals.get(i).timeLimit);
				s += goals.get(i).status ? " success" : " failed";
				g.drawString(s, (int) (goals.get(i).workLoad / goals.get(i).timeLimit * 800), 800 - (int) goals.get(i).timeLimit);
			}
			*/
		}
		
		//This method is the HEART OF THE CODE!
		//it computes the color of a given pixel
		public Color computeColor(int x, int y) {
			double red = 0.0;
			double green = 0.0;
			double gFW; //fraction Worked of Goals
			double pFW; //fraction worked of the x y coords in question
			double pWL; // workload of the point in question
			double pTL; //timeLimit of the point in question
			double gTL;
			double gWL;
			
			for (int i = 0; i < goals.size(); i++) {
				gFW = goals.get(i).workLoad / goals.get(i).timeLimit;
				gTL = goals.get(i).timeLimit;
				gWL = goals.get(i).workLoad;
				
				//assuminig y is linear:
				if (isXRate == true) {
					pFW = (double) (x) / width;
				}
				else {
					pFW = (double) (x) / (double) (height - y);
				}				
				
				pTL = height - y; //cartesian coordinates (y starts at bottom)				
				pWL = pFW * pTL; //TODO pWL = (double) x; <- this had promising results
				
				if (goals.get(i).status == true && pFW < gFW && pTL > gTL && pWL < gWL) {
					green += 255.0 / goals.size();			
				}				
				else if (goals.get(i).status == true && pFW < gFW && pTL <= gTL && (gTL - gWL) < (pTL - pWL)) {
					green += 255.0 / goals.size();				
				}				
				else if (goals.get(i).status == false && pFW > gFW && pTL > gTL && (gTL - gWL) > (pTL - pWL)) {
					red += 255.0 / goals.size();
				}				
				else if (goals.get(i).status == false && pFW > gFW && pWL > gWL && pTL <= gTL) {
					red += 255.0 / goals.size();
				}				
			}
			
			//normalises colour
			if (red != 0 || green != 0) {
				double normal = 255.0 / Math.max(red, green);			
				return new Color((int) (red * normal), (int) (green * normal), 0);
			}
			else {
				return new Color(0, 0, 0);
			}
		}
		
		public Dimension getPreferredSize() {
			return new Dimension(width + xAxisHeight, height + yAxisWidth);
		}
		
		public Dimension getMinimumSize() {
			return new Dimension(width + xAxisHeight, height + yAxisWidth);
		}
	}
}
