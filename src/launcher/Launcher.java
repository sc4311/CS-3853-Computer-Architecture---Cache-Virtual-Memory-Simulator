package launcher;

import core.CPU;
import core.utils.CommandLineParser;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Launcher
{
    //Change these before turning in
    private static int milestone = 2;
    private static String fileNameMileStone1 = "Output/Milestone 1/Team_07_Sim_n_M31.txt";
    private static String fileNameMileStone2A = "Output/Milestone 2A/Team_07_Sim_n_M11.txt";
    private static String fileNameMileStone2B = "Output/Milestone 2B/Team_07_Sim_n_M11.txt";

    public static void main(String[] args)
    {
        /*
         * Assumptions
         * 32-bit address bus
         * Write-through policy = Write to cache and then to Main Memory
         * ALL data access is 4 bytes (32Bits)
         *
         */

        CPU cpu = CommandLineParser.parseCommands(args);

        switch(milestone)
        {
            case 1 -> {writer(fileNameMileStone1, cpu);}
            case 2 -> {writer(fileNameMileStone2A, cpu);}
            case 3 -> {writer(fileNameMileStone2B, cpu);}
        }
    }

    public static void writer(String filename, CPU cpu)
    {
        ArrayList<String> output = new ArrayList<>();
        output.add(cpu.information());

        switch(milestone)
        {
            case 1 -> {output.add(cpu.milestone1());}
            case 2 -> {output.add(cpu.milestone2A());}
            //case 3 -> {output.add(cpu.milestone2B());}
        }

        File projectReport = new File(filename);
        projectReport.getParentFile().mkdirs();

        try(BufferedOutputStream boss = new BufferedOutputStream(new FileOutputStream(projectReport)))
        {
            for(int i = 0; i < output.size(); i++)
            {
                String s = output.get(i);
                boss.write(s.getBytes());
                System.out.println(s);
            }

            if(Desktop.isDesktopSupported())
            {
                Desktop d = Desktop.getDesktop();
                d.open(projectReport);
            }
            else
            {
                System.out.printf("Please open \"%s\" in \"%s\" to view results\n", projectReport.getName(), projectReport.getParentFile());
            }
        }
        catch(IOException IOe)
        {
            System.err.printf("ERROR::Could not create \"%s\" in directory \"%s\": Please make sure you have appropriate privileges\n", projectReport.getName(), projectReport.getParentFile());
            IOe.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
