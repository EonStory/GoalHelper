package rose;


import java.awt.*;
import java.io.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.Date;

public class minimalbreak extends JFrame {
	public ArrayList<Goal> goals = new ArrayList<Goal>();	
	//public GoalPanel(int xStart, int yStart, int xEnd, int yEnd) {
	
	JTable jTGoals;	
	//need to create 3 tables: completed goals, goals in progress and goals awaiting status
	//if a goal is sucess or fail before time limit is over, it is considered complete (not in progress)
	
	public minimalbreak() {
		//Create example goals
		Goal g1 = new Goal(400, 500, Goal.SUCCESS, new Date(System.currentTimeMillis()));		
		goals.add(g1);		
		  	    
	    GoalTable rawr = new GoalTable();
	}
	
	//no e.getSource because this is designe for only 1 thing (the axis)
	class GoalTable extends DefaultTableModel {
		String[] columnNames = {"Name", "Status", "Work Load", "Time Limit", "Work Fraction", "Starting Date",
				"Ending Date", "In Progress", "Awaiting Input"};
		Object[][] goalData;
		
		public GoalTable() {
			System.out.println("why am i not called!! constructed");
			goalData = new Object[goals.size()][columnNames.length];			
			System.out.println(columnNames.length);
			System.out.println("Goals size " + goals.size());
			for (int i = 0; i < goals.size(); i++) {
				goalData[i][0] = goals.get(i).name;
				if (goals.get(i).status == Goal.SUCCESS) {
					goalData[i][1] = "Success"; //TODO this is bad. it doesnt use the combobox
				}
				else if (goals.get(i).status == Goal.FAILURE) {
					goalData[i][1] = "Failure";
				}
				else if (goals.get(i).status == Goal.PENDING) {
					goalData[i][1] = "Pending";
				}				
				goalData[i][2] = goals.get(i).workLoad;
				goalData[i][3] = goals.get(i).timeLimit;
				goalData[i][4] = String.format("%.2f", goals.get(i).workLoad / goals.get(i).timeLimit);
				goalData[i][5] = goals.get(i).startDate;
				goalData[i][6] = goals.get(i).endDate;
				if (goals.get(i).status == Goal.PENDING && goals.get(i).isInProgress()) {
					goalData[i][7] = "Yes";
				}
				else {
					goalData[i][7] = "No";
				}
				if (goals.get(i).status == Goal.PENDING && goals.get(i).isInProgress() == false) {
					goalData[i][8] = "Yes";
				}
				else {
					goalData[i][8] = "No";
				}
			}
		}
		
		public int getColumnCount() {
			return 9;//columnNames.length;
		}

		public int getRowCount() {
			return goalData[0].length;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			System.out.println("row index: " + rowIndex + "  coumn index: " + columnIndex);
			return goalData[rowIndex][columnIndex];
		}
		
        public String getColumnName(int col) {
            return columnNames[col];
        }
		
        //makes the bools be tick boxes
		public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
		
        public boolean isCellEditable(int row, int col) {
        	 if (col < 2) {
                 return true;
             } else {
                 return false;
             }
        }
        
        public void setValueAt(Object value, int row, int col) {
            goalData[row][col] = value;
            fireTableCellUpdated(row, col);
            if (col == 1) {
            	if (value.equals("Success")) {
            		goals.get(row).status = Goal.SUCCESS;
            	}
            	else if (value.equals("Failure")) {
            		goals.get(row).status = Goal.FAILURE;
            	}
            	else if (value.equals("Pending")) {
            		goals.get(row).status = Goal.PENDING;
            	}
            	else {
            		System.out.println("error");
            	}
            }
            System.out.println(value);
            System.out.println(row);
            System.out.println(col);
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
		
		String name;
		double workLoad;
		double timeLimit;
		int status;
		Date startDate;
		Date endDate;
		
		public Goal(double workLoad, double timeLimit, int status, Date startDate) {
			this(workLoad, timeLimit, status, startDate, "Default");
		}
		
		public Goal(double workLoad, double timeLimit, int status, Date startDate, String name) {
			this.workLoad = workLoad;
			this.timeLimit = timeLimit;
			this.status = status;
			this.startDate = startDate;
			endDate = new Date(startDate.getTime() + (long) (60 * 1000 * timeLimit));
			this.name = name;
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
		minimalbreak frame = new minimalbreak();
		frame.setTitle("Goal Achievements");
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}