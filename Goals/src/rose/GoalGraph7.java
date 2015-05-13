package rose;


import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.ArrayList;
import java.util.Date;

public class GoalGraph7 extends JFrame {
	public ArrayList<Goal> goals = new ArrayList<Goal>();	
	public GoalPanel goalPanel = new GoalPanel(0, 200, 800, 800);	
	//public GoalPanel(int xStart, int yStart, int xEnd, int yEnd) {
	double workLoad1; 
	double timeLimit1;
	double workFraction1;
	
	public MouseMoveListener mouseMoveEars = new MouseMoveListener();
	public ButtonListener buttonListener = new ButtonListener();
	public TextListener textListener = new TextListener();
	
	JLabel workLoadLabel = new JLabel("Work Load: ");
	JLabel timeLimitLabel = new JLabel("Time Limit: ");
	JLabel workFractionLabel = new JLabel("Work Fraction: ");
	
	JTextField jtfTimeLimit = new JTextField();
	JTextField jtfWorkLoad = new JTextField();
	JTextField jtfXMin = new JTextField();
	JTextField jtfXMax = new JTextField();
	JTextField jtfYMin = new JTextField();
	JTextField jtfYMax = new JTextField();
	
	
	JButton jbEnter = new JButton("Enter");
	JButton jbLoad = new JButton("Load");
	JButton jbSave = new JButton("Save");
	
	JRadioButton xAxisRate = new JRadioButton("Rate");
	JRadioButton xAxisTotal = new JRadioButton("Total");
	JRadioButton yAxisLinear = new JRadioButton("Linear");
	JRadioButton yAxisLogarithmic = new JRadioButton("Logarithmic");
	
	final JFileChooser fc = new JFileChooser();
	
	//need to create 3 tables: completed goals, goals in progress and goals awaiting status
	//if a goal is sucess or fail before time limit is over, it is considered complete (not in progress)
	String[] completeGoalColumns = {"Work Load","Time Limit", "Start Date","End Date", "Status"};
	String[] inProgressGoalColumns = {"Work Load","Time Limit", "Start Date","End Date", "Time Remaining", "Status"};
	String[] awaitingStatusGoalColumns = {"Work Load","Time Limit", "Start Date","End Date", "Status"};
	
	
	
	public GoalGraph7() {
		//Create example goals
		// workLoad,  timeLimit, status, startDate
		Goal g1 = new Goal(400, 500, Goal.SUCCESS, new Date(System.currentTimeMillis()));
		Goal g2 = new Goal(200, 700, Goal.SUCCESS, new Date(System.currentTimeMillis()));
		Goal g3 = new Goal(1000, 1800, Goal.SUCCESS, new Date(System.currentTimeMillis()));
		Goal g4 = new Goal(490, 530, Goal.PENDING, new Date(System.currentTimeMillis()));
		Goal g5 = new Goal(400, 600, Goal.PENDING, new Date(System.currentTimeMillis()));
		
		g4.endDate = new Date(g4.startDate.getTime() - 500000);
		goals.add(g1);
		goals.add(g2);
		goals.add(g3);
		goals.add(g4);
		goals.add(g5);
		
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
		goalInputPanel.setLayout(new GridLayout(3, 0, 0, 0));
		goalInputPanel.add(new JLabel("Work Load"));
		goalInputPanel.add(jtfWorkLoad);
		goalInputPanel.add(new JLabel("Time Limit"));		
		goalInputPanel.add(jtfTimeLimit);
		goalInputPanel.add(jbEnter);
		goalInputPanel.setBorder(new LineBorder(Color.BLACK, 1));
		
		//jtfWorkLoad.getDocument().addDocumentListener(textListener);
		//Radio buttons need to be in a group so only 1 at a time can be selected
		
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
		
		jtfXMin.setText(Double.toString(goalPanel.xStart));
		jtfXMax.setText(Double.toString(goalPanel.xEnd));
		jtfYMin.setText(Double.toString(goalPanel.yStart));
		jtfYMax.setText(Double.toString(goalPanel.yEnd));
		
		jtfXMin.getDocument().addDocumentListener(textListener);
		jtfXMax.getDocument().addDocumentListener(textListener);
		jtfYMin.getDocument().addDocumentListener(textListener);
		jtfYMax.getDocument().addDocumentListener(textListener);
		
		axisOptions.add(new JLabel("x Min"));
		axisOptions.add(jtfXMin);
		axisOptions.add(new JLabel("x Max"));
		axisOptions.add(jtfXMax);
		axisOptions.add(new JLabel("y Min"));
		axisOptions.add(jtfYMin);
		axisOptions.add(new JLabel("y Max"));
		axisOptions.add(jtfYMax);
				
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
		
		jbEnter.addActionListener(buttonListener);
		jbSave.addActionListener(buttonListener);
		jbLoad.addActionListener(buttonListener);		
		
	    goalPanel.addMouseMotionListener(mouseMoveEars);
	    
	   // fc.set
	    
	    //create three tables
	    //table 1 (in progress goals are pending)
	    //table 2 goals wich have a status of either fail or sucess
	    //table 3 goals requiring input which arent in progress
	    
	    //create complete goals table
	    int completedGoals = 0;
	    int goalsInProgress = 0;
	    int goalsAwaitingInput = 0;
	    
	    for (int i = 0; i < goals.size(); i++) {
	    	if (goals.get(i).status == Goal.SUCCESS || goals.get(i).status == Goal.FAILURE) {
	    		completedGoals++;
	    	}
	    	else if (goals.get(i).isInProgress() == true) {
	    		goalsInProgress++;
	    	}
	    	else {
	    		goalsAwaitingInput++;
	    	}
	    }
	    
	    String[] goalsCompletedColumn =     {"Status",           "Work Load", "Time Limit", "Work Fraction", "Starting Date", "Ending Date"};
	    String[] goalsInProgressColumn =    {"Succes", "Failed", "Work Load", "Time Limit", "Work Fraction", "Starting Date", "Ending Date"};
	    String[] goalsAwaitingInputColumn = {"Succes", "Failed", "Work Load", "Time Limit", "Work Fraction", "Starting Date", "Ending Date"};
	    
	    Object[][] goalsCompletedData = new Object[completedGoals][6];
	    Object[][] goalsInProgressData = new Object[goalsInProgress][8];
	    Object[][] goalsAwaitingInputData = new Object[goalsAwaitingInput][8];
	    
	    //this code is nasty but its just the formatting for 3 unique tables. no way to make it better really
	    for (int i = 0; i < goals.size(); i++) {	    	
	    	if (goals.get(i).status == Goal.SUCCESS || goals.get(i).status == Goal.FAILURE) {
	    		System.out.println("suc or fail");
	    		goalsCompletedData[--completedGoals][0] = goals.get(i).status;
	    		goalsCompletedData[completedGoals][1] = goals.get(i).workLoad;
	    		goalsCompletedData[completedGoals][2] = goals.get(i).timeLimit;
	    		goalsCompletedData[completedGoals][3] = String.format("%.2f", goals.get(i).workLoad / goals.get(i).timeLimit);
	    		goalsCompletedData[completedGoals][4] = goals.get(i).startDate;
	    		goalsCompletedData[completedGoals][5] = goals.get(i).endDate;	    		
	    	}
	    	else if (goals.get(i).isInProgress()) {
	    		System.out.println("in prog");
	    		goalsInProgressData[--goalsInProgress][0] = new Boolean(false);
	    		goalsInProgressData[goalsInProgress][1] = "TBD2";
	    		goalsInProgressData[goalsInProgress][2] = goals.get(i).workLoad;
	    		goalsInProgressData[goalsInProgress][3] = goals.get(i).timeLimit;
	    		goalsInProgressData[goalsInProgress][4] = String.format("%.2f", goals.get(i).workLoad / goals.get(i).timeLimit);
	    		goalsInProgressData[goalsInProgress][5] = goals.get(i).startDate;
	    		goalsInProgressData[goalsInProgress][6] = goals.get(i).endDate;	   
	    	}
	    	else {
	    		System.out.println("pending not in prog");
	    		goalsAwaitingInputData[--goalsAwaitingInput][0] = "TBDawp";
	    		goalsAwaitingInputData[goalsAwaitingInput][1] = "TBD2awp";
	    		goalsAwaitingInputData[goalsAwaitingInput][2] = goals.get(i).workLoad;
	    		goalsAwaitingInputData[goalsAwaitingInput][3] = goals.get(i).timeLimit;
	    		goalsAwaitingInputData[goalsAwaitingInput][4] = String.format("%.2f", goals.get(i).workLoad / goals.get(i).timeLimit);
	    		goalsAwaitingInputData[goalsAwaitingInput][5] = goals.get(i).startDate;
	    		goalsAwaitingInputData[goalsAwaitingInput][6] = goals.get(i).endDate;
	    	}
	    }
	    //TODO THIS
	    JTable JTGoalsCompleted = new JTable(goalsCompletedData, goalsCompletedColumn);
	    JTable JTGoalsAwaitingInput = new JTable(goalsAwaitingInputData, goalsAwaitingInputColumn);
	    JTable JTGoalsInProgress = new JTable(goalsInProgressData, goalsInProgressColumn);
	    

	    //add them all to a scroll pane so header is visible
	    JScrollPane sPGC  = new JScrollPane(JTGoalsCompleted);
	    JScrollPane sPGAI = new JScrollPane(JTGoalsAwaitingInput);
	    JScrollPane sPGIP = new JScrollPane(JTGoalsInProgress);
	    
	    sPGC.setPreferredSize(new Dimension(700, 100));
	    sPGAI.setPreferredSize(new Dimension(700, 100));
	    sPGIP.setPreferredSize(new Dimension(700, 100));	    
	    
	    
	    
	    JPanel rightPanel = new JPanel();
		VerticalFlowLayout hope = new VerticalFlowLayout();
		rightPanel.setLayout(hope);
		rightPanel.add(mouseInfoPanel);
		rightPanel.add(goalInputPanel);
		rightPanel.add(loadSavePanel);
		rightPanel.add(axisOptions);
		rightPanel.add(new JLabel("Completed Goals"));
		rightPanel.add(sPGC);
		rightPanel.add(new JLabel("Goals Awaiting Input"));
		rightPanel.add(sPGAI);
		rightPanel.add(new JLabel("Goals In Progress"));
		rightPanel.add(sPGIP);
		add(rightPanel);
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
	//no e.getSource because this is designe for only 1 thing (the axis)
	class TextListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {	
		}

		public void insertUpdate(DocumentEvent e) {
			if (jtfXMin.getText().isEmpty() == false && jtfXMax.getText().isEmpty() == false && 
					jtfYMin.getText().isEmpty() == false && jtfYMax.getText().isEmpty() == false)   {
				goalPanel.setX(Double.parseDouble(jtfXMin.getText()), Double.parseDouble(jtfXMax.getText()));
				goalPanel.setY(Double.parseDouble(jtfYMin.getText()), Double.parseDouble(jtfYMax.getText()));
			}
		}

		public void removeUpdate(DocumentEvent e) {
			if (jtfXMin.getText().isEmpty() == false && jtfXMax.getText().isEmpty() == false && 
					jtfYMin.getText().isEmpty() == false && jtfYMax.getText().isEmpty() == false)   {
				goalPanel.setX(Double.parseDouble(jtfXMin.getText()), Double.parseDouble(jtfXMax.getText()));
				goalPanel.setY(Double.parseDouble(jtfYMin.getText()), Double.parseDouble(jtfYMax.getText()));
			}
		}
		
	}
	
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			if (e.getSource() == jbEnter) {
				goals.add(new Goal(Double.parseDouble(jtfWorkLoad.getText()), Double.parseDouble(jtfTimeLimit.getText()), 
						Goal.PENDING, new Date(System.currentTimeMillis())));
				goalPanel.repaint();
			}
			else if (e.getSource() == jbSave) {
				int returnVal = fc.showSaveDialog(GoalGraph7.this);
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
				int returnVal = fc.showOpenDialog(GoalGraph7.this); // ???
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
			else if (e.getSource() == jtfWorkLoad) {
				System.out.println("DETECTED. TEST COMPLETE");
			}			

		}
	}

	static class Goal implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public static final int SUCCESS = 0;
		public static final int FAILURE = 1;
		public static final int PENDING = 2;
		
		double workLoad;
		double timeLimit;
		int status;
		Date startDate;
		Date endDate;
		
		public Goal(double workLoad, double timeLimit, int status, Date startDate) {
			this.workLoad = workLoad;
			this.timeLimit = timeLimit;
			this.status = status;
			this.startDate = startDate;
			endDate = new Date(startDate.getTime() + (long) (60 * 1000 * timeLimit));
		}
		
		public boolean isInProgress() {
			//should this be >= or >
			if (System.currentTimeMillis() > endDate.getTime()) {
				return false;
			}
			else {
				return true;
			}
		}
	}
	
	public static void main(String[] args) {
		GoalGraph7 frame = new GoalGraph7();
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
		double xStart, yStart, xEnd, yEnd;
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
		
		public void setX(double xMin, double xMax) {
			if (xMin < xMax && xMin >= 0) {
				this.xStart = xMin;
				this.xEnd = xMax;
				xScale = ((double) (xEnd - xStart)) / width;
			}
			this.repaint();
		}
		
		public void setY(double yMin, double yMax) {
			if (yMin < yMax && yMin > 0) {
				this.yStart = yMin;
				this.yEnd = yMax;
				yScale = ((double) (yEnd - yStart)) / height;
				yLogSteps = (Math.log(yEnd) - Math.log(yStart)) / axisSplits;
			}
			this.repaint();
		}
		
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
					pTL = Math.pow(Math.E, Math.log(yStart) + (yLogSteps * axisSplits / height) * (yEnd - y * yScale)); //yScale appropriate here? NOPE new guess maybe it is
				}
				 //cartesian coordinates (y starts at bottom)	
				
				if (xRate == true) {
					pFW = (double) (x) / width; //asuming scale goes from 0 to 1 always
				}
				else {
					pFW = (double) (x * xScale) / pTL; //TODO: check this xScale is valid and also check if it need to be in bit earlier
				}		
					
				pWL = pFW * pTL; //TODO pWL = (double) x; <- this had promising results
				
				if (goals.get(i).status == Goal.SUCCESS && pFW < gFW && pTL > gTL && pWL < gWL) {
					green += 255.0 / goals.size();			
				}				
				else if (goals.get(i).status == Goal.SUCCESS && pFW < gFW && pTL <= gTL && (gTL - gWL) < (pTL - pWL)) {
					green += 255.0 / goals.size();				
				}				
				else if (goals.get(i).status == Goal.FAILURE && pFW > gFW && pTL > gTL && (gTL - gWL) > (pTL - pWL)) {
					red += 255.0 / goals.size();
				}				
				else if (goals.get(i).status == Goal.FAILURE && pFW > gFW && pWL > gWL && pTL <= gTL) {
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

