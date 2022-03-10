/* Winslow Conneen
 * COSC 3355 Assignment 2
 * Dr. Subramanian
 * 3.23.2021
 * Purpose: Create a shell in java that interacts with windows through CMD.EXE line interface
 */

import java.io.*;
import java.util.*;

public class OSShellSimulation {

	public static void main(String [] args) throws IOException, InterruptedException
	{
		String userIn = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//Create an infinite loop
		while(true)
		{
			userIn = "";
			System.out.print(">>>");
			userIn = br.readLine();
			
			//'ls' method calls shell with 'dir' call to retrieve directory from cmd line
			//note: my system for whatever reason has "null" before every line transferred over from the 
			//shell. I have corrected for this by setting i to 4 instead of to 0. If another system produces
			//strings without this, it may cause an error, but I doubt that they will.
			if(userIn.equalsIgnoreCase("ls"))
			{
				int i = 4;
				String [] ex = myShell("dir");
				
				while(ex[i] != null)
				{
					ex[i] = ex[i].substring(4);
					System.out.println(ex[i]);
					i++;
				}
			}
			//'pwd' method calls shell with 'dir' call to retrieve absolute file path to current position
			//note: same issue with "null" as mentioned above
			else if(userIn.equalsIgnoreCase("pwd"))
			{
				String [] dir = myShell("dir");
				String absPath = dir[3].substring(18);
				
				System.out.println("Current absolute path is: \n" + absPath);
				
			}
			//this method uses a call to the "cls" command in the CMD line interface to clear the console
			//this is the only explicit call to the OS that does not use "myShell"
			else if(userIn.equalsIgnoreCase("clear"))
			{
		        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			}
			//this if statement finds the calendar date, time, and time zone with 3 seperate calls to the cmd
			//line interface ('date', 'time', and 'systemInfo'), then extracts the valuable information 
			//(month, hour, time zone, etc) and configures it to the desired format
			else if(userIn.equalsIgnoreCase("date"))
			{
				//3 cmd line interface calls
				String [] temp = myShell("date");
				String calDate = temp[0].substring(25);
				
				temp = myShell("time");
				String time = temp[0].substring(25);
				
				temp = myShell("systemInfo");
				String timeZone = null;
				int i = 0;
				while (temp[i] != null)
				{
					if(temp[i].startsWith("nullTime Zone:"))
					{
						timeZone = temp[i].substring(32,35);
					}
					i++;
				}
				
				//Apportioning information into explicit parts for the sake of modularity and customization
				
				//Converting numerical month to its corresponding word-month
				String month = calDate.substring(4,6);
				
				switch(month)
				{
				case "01":
					month = "Jan";
					break;
				case "02":
					month = "Feb";
					break;
				case "03":
					month = "Mar";
					break;
				case "04":
					month = "Apr";
					break;
				case "05":
					month = "May";
					break;
				case "06":
					month = "Jun";
					break;
				case "07":
					month = "Jul";
					break;
				case "08":
					month = "Aug";
					break;
				case "09":
					month = "Sep";
					break;
				case "10":
					month = "Oct";
					break;
				case "11":
					month = "Nov";
					break;
				case "12":
					month = "Dec";
					break;
				default:
					break;
				}
				
				String weekday = calDate.substring(0,3);
				String day = calDate.substring(7,9);
				String year = calDate.substring(10);
				String hour = time.substring(0,2);
				String minute = time.substring(3,5);
				String second = time.substring(6,8);
				
				//placing in desired format and printing
				String fullDate = weekday + " " + month + " " + day + " " + hour + ":" + minute + ":" + second + " " + timeZone + " " + year;
				
				System.out.println("The date is: \n" + fullDate);
			}
			//exit method ends the program
			else if(userIn.equalsIgnoreCase("exit"))
			{
				System.exit(0);
			}
			//This chunk outputs the 'help' blurb in the correct format
			else if(userIn.equalsIgnoreCase("help"))
			{
				System.out.println("MyShell, version 1.0, runs on Windows 10, developed by Winslow Conneen\n"
						+ "Release date March 23, 2021\n"
						+ "These shell commands are defined internally. Type 'help' to see the list.\n"
						+ "Command and its parameter, if any, should be seperated by one space only.\n"
						+ "Type 'help name' to find out more about the command 'name'.\n\n"
						+ "Command\t\t\tFunction\n"
						+ "=======\t\t\t========\n"
						+ "ls\t\t\tlists contents of current directory\n"
						+ "pwd\t\t\tdisplays the current directory\n"
						+ "clear\t\t\tclears the console\n"
						+ "date\t\t\tdisplays the current day, date, time, and time zone\n"
						+ "help cmd\t\tget help for command cmd\n"
						+ "help\t\t\tdisplays this list\n"
						+ "exit\t\t\tquits the console\n"
						+ "whoami\t\t\tdisplays the name of the programmer");
			}
			
			//This portion tells the user the purpose of each function is by checking if their query begins with "help"
			//and then checking the following method to determine what they need to know
			else if(userIn.startsWith("help"))
			{
				if(userIn.substring(5).equalsIgnoreCase("ls"))
				{
					System.out.println("Type 'ls' to display the current directory's contense in the format:\n"
							+ "[date of latest access(mm/dd/yyyy)] [time of latest access (hh:mm AM/PM)] <DIR> [file size, if blank then empty] [file name]\n"
							+ "Ex: 03/22/2021  07:22 PM  <DIR> 396 .classpath\n\n"
							+ "The total byte size of the directory, the number of files, the number of immediate sub-directories, \n"
							+ "and the remaining space available for use will also be displayed.");
				}
				else if(userIn.substring(5).equalsIgnoreCase("pwd"))
				{
					System.out.println("Enter 'pwd' to show the current absolute path to the current directory.\n"
							+ "The path will be formatted as a series of recursive folders seperated by a '\\'\n"
							+ "Ex: 'C:\\Users\\Winslow\\eclipse-workspace\\COSC 3355 Assignment 2' ");
				}
				else if(userIn.substring(5).equalsIgnoreCase("clear"))
				{
					System.out.println("Enter 'clear' to erase all the information on the console up to that point.");
				}
				else if(userIn.substring(5).equalsIgnoreCase("date"))
				{
					System.out.println("Displays day, date, time, and timezone in the following format:\n"
							+ "Ex. Sun Mar 1 11:08:58 CST 2021\n\n"
							+ "Type 'date' to display the system's set day and time settings.");
				}
				else if(userIn.substring(5).equalsIgnoreCase("help"))
				{
					System.out.println("Type 'help' to display a full list of commands as well a system information");
				}
				else if(userIn.substring(5).equalsIgnoreCase("exit"))
				{
					System.out.println("Type 'exit' to exit the console and close the program.");
				}
				else if(userIn.substring(5).equalsIgnoreCase("whoami"))
				{
					System.out.println("Type 'whoami' to display the name of the programmer of this shell.");
				}
				else
				{
					System.out.println("invalid entry.");
				}
			}
			//This method prints the name of the programmer
			else if(userIn.equalsIgnoreCase("whoami"))
			{
				System.out.println("The programmer of this program is Winslow Conneen.");
			}
			//default case if there is an invalid entry
			else
			{
				System.out.println("Unknown Command!");
			}
			
			System.out.print("\n");
			
		}
	}
	
	//This method communicates directly with the Windows OS by converting CMD.EXE content to java strings in an array.
	private static String [] myShell(String in) throws IOException
	{
		//creates new process with an explicit process builder call, this initially opens the cmd line interface
		//it also enters the desired command into the prompt 
		Process p = new ProcessBuilder("CMD","/C", in).start();
		
		//defines output string for process p and empties the buffered writer with "flush" method
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
		writer.write(" ");
		writer.flush();
		
		String [] output = new String[100];
		int i = 0;
		
		//converts the stream to a String array, 1 string per line, max 100 lines
		Scanner scan = new Scanner(p.getInputStream());
		while(scan.hasNext())
		{
			output[i] = output[i] + scan.nextLine();
			i++;
		}
		
		return output;
	}
}
