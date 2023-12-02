package moss.shed;

import java.io.*;
import java.util.*;

public class Scheduling {

  private static int processnum = 5;
  private static int meanDev = 1000;
  private static int standardDev = 100;
  private static int runtime = 1000;
  private static Vector processVector = new Vector();
  private static Results result = new Results("null","null",0);
  private static String resultsFile = "Summary-Results";

  private static void Init(String file) {
    File f = new File(file);
    String line;
    int cputime = 0;
    int ioblocking = 0;
    int blockingTime = 0;
    int arrivedTime = 0;
    double X = 0.0;

    try {   
      DataInputStream in = new DataInputStream(new FileInputStream(f));
      while ((line = in.readLine()) != null) {
        if (line.startsWith("numprocess")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          processnum = Common.s2i(st.nextToken());
        }
        if (line.startsWith("meandev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          meanDev = Common.s2i(st.nextToken());
        }
        if (line.startsWith("standdev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          standardDev = Common.s2i(st.nextToken());
        }
        if (line.startsWith("blockingTime")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          blockingTime = Common.s2i(st.nextToken());
        }
        if (line.startsWith("arrivedTime")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          arrivedTime = Common.s2i(st.nextToken());
        }
        if (line.startsWith("process")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          ioblocking = Common.s2i(st.nextToken());
          X = Common.R1();
          while (X == -1.0) {
            X = Common.R1();
          }
          X = X * standardDev;
          cputime = (int) X + meanDev;
          processVector.addElement(new sProcess(cputime, ioblocking, blockingTime, arrivedTime, 0, 0, 0, 0));
        }
        if (line.startsWith("runtime")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          runtime = Common.s2i(st.nextToken());
        }
      }
      in.close();
    } catch (IOException e) {}
  }

  public static void main(String[] args) {
    int i = 0;
    args = new String[1];
    args[0] = "src\\moss\\shed\\scheduling.conf";

    if (args.length != 1) {
      System.out.println("Usage: 'java Scheduling <INIT FILE>'");
      System.exit(-1);
    }
    File f = new File(args[0]);
    if (!(f.exists())) {
      System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
      System.exit(-1);
    }  
    if (!(f.canRead())) {
      System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
      System.exit(-1);
    }
    System.out.println("Working...");
    Init(args[0]);
    if (processVector.size() < processnum) {
      while (processVector.size() < processnum) {
          double X = Common.R1();
          while (X == -1.0) {
            X = Common.R1();
          }
          X = X * standardDev;
        int cputime = (int) X + meanDev;
        processVector.addElement(new sProcess(cputime,i*100, i*100, i*10,0,0,0, 0));
        i++;
      }
    }
    result = SchedulingAlgorithm.Run(runtime, processVector, result);    
    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      out.println("Scheduling Type: " + result.schedulingType);
      out.println("Scheduling Name: " + result.schedulingName);
      out.println("Simulation Run Time: " + result.compuTime);
      out.println("Mean: " + meanDev);
      out.println("Standard Deviation: " + standardDev);
      out.println("Process #\tCPU Time\tIO Blocking\tCPU Completed\tCPU Blocked");
      for (i = 0; i < processVector.size(); i++) {
        sProcess process = (sProcess) processVector.elementAt(i);
        out.print(i);
        if (i < 100) { out.print("\t\t"); } else { out.print("\t"); }
        out.print(process.cputime);
        if (process.cputime < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.print(process.ioblocking);
        if (process.ioblocking < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.print(process.cpudone);
        if (process.cpudone < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.println(process.numblocked + " times");
      }
      out.close();
    } catch (IOException e) {}
  System.out.println("Completed.");
  }
}

