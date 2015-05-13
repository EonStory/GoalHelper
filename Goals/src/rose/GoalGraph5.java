package rose;


import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.ArrayList;
import javax.swing.Timer;

public class GoalGraph5 extends JFrame {
	public ArrayList<Goal> goals = new ArrayList<Goal>();	
	public GoalPanel goalPanel = new GoalPanel(0, 200, 800, 800);	
	//public GoalPanel(int xStart, int yStart, int xEnd, int yEnd) {
	double workLoad1; 
	double timeLimit1;
	double workFraction1;
	
	public MouseMoveListener mouseMoveEars = new MouseMoveListener();
	public ButtonListener buttonListener = new ButtonListener();
	
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
	
	public GoalGraph5() {
		//Create example goals
		//goals.add(new Goal(2.0, 100.0, true));
		//goals.add(new Goal(180, 200, false));
		//goals.add(new Goal(250, 350, false));
		//goals.add(new Goal(320.0, 460.0, true));
		//goals.add(new Goal(300, 660.0, false));
		
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
		
		
		JPanel goalInfoPanel = new JPanel();		
		//goalInfoPanel.setBorder(new LineBorder(Color.BLACK, 1));	
		goalInfoPanel.setLayout(new GridLayout(0, 4, 3, 0));
		goalInfoPanel.add(new JLabel("Work Load"));
		goalInfoPanel.add(new JLabel("Time Limit"));
		goalInfoPanel.add(new JLabel("Work Fraction"));
		goalInfoPanel.add(new JLabel("Status"));
		for (int i = 0; i < goals.size(); i++) {
			goalInfoPanel.add(new JLabel(String.format("%.2f", goals.get(i).workLoad)));
			goalInfoPanel.add(new JLabel(String.format("%.2f", goals.get(i).timeLimit)));
			goalInfoPanel.add(new JLabel(String.format("%.2f", goals.get(i).workLoad / goals.get(i).timeLimit)));
			if (goals.get(i).status) {
				goalInfoPanel.add(new JLabel("Success"));
			}
			else {
				goalInfoPanel.add(new JLabel("Failure"));
			}
		}
		goalInfoPanel.setPreferredSize(new Dimension(300, 20 * goals.size()));//goalInfoPanel.getWidth(), goalInfoPanel.getHeight()));
		//goalInfoPanel.setPreferredSize(new Dimension(goalInfoPanel.WIDTH, 50));
		
		JScrollPane goalInfoScroll = new JScrollPane(goalInfoPanel);
		goalInfoScroll.setPreferredSize(new Dimension(100,100));
		//.add(goalInfoPanel);
		
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
		rightPanel.setLayout(new GridLayout(0, 1, 0, 10));
		rightPanel.add(mouseInfoPanel);
		rightPanel.add(goalInputPanel);
		rightPanel.add(loadSavePanel);
		rightPanel.add(axisOptions);
		rightPanel.add(goalInfoScroll);
		add(rightPanel);
		
		jbEnter.addActionListener(buttonListener);
		jbSave.addActionListener(buttonListener);
		jbLoad.addActionListener(buttonListener);		
		
	    goalPanel.addMouseMotionListener(mouseMoveEars);
	    
	   // fc.set
	    
	}
	
	class MouseMoveListener extends MouseMotionAdapter {
    	public void mouseMoved(MouseEvent e) {  
    		
    		if (goalPanel.yLinear == true) {
    			timeLimit1 = goalPanel.yEnd -  e.getY() * goalPanel.yScale; 
    		}
    		
    		else {
    			//pTL =      Math.pow(Math.E, Math.log(          yStart) + (          yLogSteps *           axisSplits /           height) * (          yEnd - y)); 
    			timeLimit1 = Math.pow(Math.E, Math.log(goalPanel.yStart) + (goalPanel.yLogSteps * goalPanel.axisSplits / goalPanel.height) * (goalPanel.yEnd - e.getY()));
    		}
    		if (goalPanel.xRate == true) {    			
        		workFraction1 = (double) (e.getX() - goalPanel.yAxisWidth) / goalPanel.width;
        		workLoad1 = workFraction1 * timeLimit1;    
    		}
    		else {       		
        		workLoad1 = (double) (e.getX() - goalPanel.yAxisWidth); 
        		workFraction1 = workLoad1 / timeLimit1;
    		}    		
    		workLoadLabel.setText("Work Load: " + String.format("%.2f", workLoad1));
    		timeLimitLabel.setText("Time Limit: " + String.format("%.1f", timeLimit1));
    		workFractionLabel.setText("Work Fraction: " + String.format("%.3f", workFraction1));
    	}
	}
	
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			if (e.getSource() == jbEnter) {
				goals.add(new Goal(Double.parseDouble(jtfWorkLoad.getText()), Double.parseDouble(jtfTimeLimit.getText()), jrbSuccess.isSelected()));
				goalPanel.repaint();
			}
			else if (e.getSource() == jbSave) {
				int returnVal = fc.showSaveDialog(GoalGraph5.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("Saved as: ");
					System.out.println(fc.getName(fc.getSelectedFile()));
					try {
						//save all goals
						//ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fc.getName(fc.getSelectedFile())));
						ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("test.dat"));
						output.writeObject(goals);
						System.out.println("written goals");
						output.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
						System.out.println(":(");
					}					
					//BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(targetFile));
				}				
			}
			else if (e.getSource() == jbLoad) {
				int returnVal = fc.showOpenDialog(GoalGraph5.this); // ???
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("APPROVED");
					try {
						ObjectInputStream input = new ObjectInputStream(new FileInputStream(fc.getName(fc.getSelectedFile())));
						goals = (ArrayList<Goal>) input.readObject();
						goalPanel.repaint();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			else if (e.getSource() == xAxisRate || e.getSource() == xAxisTotal) {
				if (xAxisRate.isSelected()) {
					goalPanel.xRate = true;
					goalPanel.repaint();
				}
				else {
					goalPanel.xRate = false;
					goalPanel.repaint();
				}
			}
			else if (e.getSource() == yAxisLinear || e.getSource() == yAxisLogarithmic) {
				if (yAxisLinear.isSelected()) {
					goalPanel.yLinear = true;
					goalPanel.repaint();
				}
				else {
					goalPanel.yLinear = false;
					goalPanel.repaint();
				}
			}
			
		}
	}

	static class Goal implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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
		GoalGraph5 frame = new GoalGraph5();
		frame.setTitle("Goal Achievements");
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	//width and height are both scrollable. In the case of time, x wil not be scrollable
	
	public class GoalPanel extends JPanel {
		int width = 800; //width of actual graph area
		int height = 800; //width of actual graph area
		int xAxisHeight = 100;
		int yAxisWidth = 100;
		int axisSplits = 8; // number of points of measure on both the axes
		boolean xRate = true;
		boolean yLinear = true;
		int xStart, yStart, xEnd, yEnd;
		double yScale, xScale; //number of increase per pixel		
		double yLogSteps;// = 
		
		public GoalPanel(int xStart, int yStart, int xEnd, int yEnd) {
			this.xStart = xStart;
			this.yStart = yStart;
			this.xEnd = xEnd;
			this.yEnd = yEnd;
			xScale = ((double) (xEnd - xStart)) / width;
			yScale = ((double) (yEnd - yStart)) / height;	
			yLogSteps = (Math.log(yEnd) - Math.log(yStart)) / axisSplits;
		}
		
		/* TODO: these stubs need to be finished to simplify the code in the following places:
		 * the mouse, drawing, axis marks and goal strings. also needs inverse of this method (getxy of workload etc)
		public double getWorkLoad(int x, int y) {
			
		}
		public double getTimeLimit(int x, int y) {
			
		}
		public double getWorkFraction(int x, int y) {
			
		}
		*/
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
						
			
			//draw axis
			for (int i = 0; i <= axisSplits; i++) {
				double xPixels = width / axisSplits; //gap size in pixels between the marks on axis
				double yPixels = height / axisSplits;			
				//note: last line for 1 is not drawn because its outside of the panel but it doesnt matter
				g.drawLine((int) (xAxisHeight + i * xPixels), height, (int) (xAxisHeight + i * xPixels), height + (int) (xAxisHeight * 0.1));				
				g.drawLine(yAxisWidth, (int) (height - i * yPixels), (int) (yAxisWidth * 0.9), (int) (height - i * yPixels));				
								
				//draw x axis numbers
				if (xRate == true) {
					g.drawString(Double.toString(i / 8.0), (int) (xAxisHeight + i * xPixels) - 4, height + (int) (xAxisHeight * 0.3));									
				}
				else {
					g.drawString(Double.toString(xStart + (xEnd - xStart)/axisSplits * i), (int) (xAxisHeight + i * xPixels) - 4, height + (int) (xAxisHeight * 0.3));					
				}
				//draw y axis numbers
				if (yLinear == true) {
					g.drawString(Double.toString(yStart + (yEnd - yStart)/axisSplits * i), yAxisWidth - 66, height - (int) (i * yPixels) + 6);
				}
				else {
					g.drawString(String.format("%.2f", Math.pow(Math.E, Math.log(yStart) + yLogSteps * i)), yAxisWidth - 66, height - (int) (i * yPixels) + 6);
				}
				
			}			
			
			//draw axis labels
			if (xRate == true) {
				g.drawString("Work Load / Time Limit", (width + xAxisHeight) / 2, height + (int) (0.5 * yAxisWidth));
			}
			else {
				g.drawString("Work Load", (width + xAxisHeight) / 2, height + (int) (0.5 * yAxisWidth)); 
			}
			
			if (yLinear == true) {
				g.drawString("Time Limit", 0, (int) (height * 0.55));	
			}
			else {
				g.drawString("Time Limit Log", 0, (int) (height * 0.55));
			}			
			
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
				gTL = goals.get(i).timeLimit;
				gWL = goals.get(i).workLoad;
				gFW = gWL / gTL;		
				
				
				//assuminig y is linear:
				if (yLinear) {
					pTL = yEnd - y * yScale;
				}
				else {
					//(yLogSteps * axisSplits / height) is yLogSteps per pixel instead of per axis mark
					pTL = Math.pow(Math.E, Math.log(yStart) + (yLogSteps * axisSplits / height) * (yEnd - y)); //yScale appropriate here? NOPE
				}
				 //cartesian coordinates (y starts at bottom)	
				
				if (xRate == true) {
					pFW = (double) (x) / width; //asuming scale goes from 0 to 1 always
				}
				else {
					pFW = (double) (x) / pTL;
				}		
						
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

