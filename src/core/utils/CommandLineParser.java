package core.utils;

import core.CPU;
import core.Cache;
import core.PhysicalMemory;
import core.TranslationLookasideBuffer;
import data.Policy;

import java.io.File;
import java.util.ArrayList;

public class CommandLineParser
{

    /**
     * Parses the command line arguments and returns a CPU object.
     * @param arguments the command line arguments
     * @return the CPU object
     */
    public static CPU parseCommands(String[] arguments)
    {
        if(arguments.length % 2 != 0) System.err.println("WARNING::All flags do not have an argument and vice versa");

        CPU cpu = new CPU();

        //Default parameters;
        int cacheSize = 8;
        int blockSize = 8;
        int associativity = 1;
        int unusedPhysicalMemory = 0;
        int instructionPerTimeSlice = -1;
        Policy pol = Policy.RoundRobin;
        long physicalMemorySize = Calculator.getBytes("1GB");

        for(int i = 0; i < arguments.length; i += 2)
        {
            String flag = arguments[i];
            String argument = arguments[i + 1];

            switch(flag)
            {
                case "-f":
                    cpu.addTraceFile(new File(argument));
                    break;
                case "-r":
                    if(!(argument.equals("RR") || argument.equals("RND")))
                    {
                        System.err.println("WARNING::Set policy with \"RR\" or \"RND\", not \"" + argument + "\"");
                        pol = Policy.stringToPolicy("RR");
                        break;
                    }
                    pol = Policy.stringToPolicy(argument);
                    break;
                case "-s":
                    cacheSize = (int) CommandLineParser.powChecker(Integer.parseInt(argument), 8, 8192, "Cache Size");
                    break;
                case "-b":
                    blockSize = (int) CommandLineParser.powChecker(Integer.parseInt(argument), 8, 64, "Block Size");
                    break;
                case "-a":
                    associativity = (int) CommandLineParser.powChecker(Integer.parseInt(argument), 1, 16, "Associativity");
                    break;
                case "-p":
                    physicalMemorySize = CommandLineParser.powChecker(Calculator.getBytes(Integer.parseInt(argument) + "MB"),
                            Calculator.getBytes("1MB"), Calculator.getBytes("4GB"), "Physical Memory");
                    break;
                case "-u":
                    unusedPhysicalMemory = (int) paramChecker(Integer.parseInt(argument), 0, 100, "Unused Physical Memory");
                    break;
                case "-n":
                    instructionPerTimeSlice = Integer.parseInt(argument);
                    break;
            }
        }

        cpu.setCache(new Cache(cpu, cacheSize, blockSize, associativity, instructionPerTimeSlice));
        cpu.setReplacementPolicy(pol);
        cpu.setPhysicalMemory(new PhysicalMemory(physicalMemorySize, unusedPhysicalMemory));
        cpu.getPhysicalMemory().setPageTableRAM(cpu.getTraceFiles().size());
        cpu.setTlb(new TranslationLookasideBuffer(cpu.getCache(), cpu.getPhysicalMemory()));

        return cpu;
    }


    /**
     * Checks if the given parameter is within the specified range.
     * @param check the parameter to check
     * @param min the minimum value
     * @param max the maximum value
     * @param varName the name of the variable
     * @return the checked parameter
     */
    private static long paramChecker(long check, long min, long max, String varName)
    {
        boolean condition = (check < min || check > max);
        if(condition) System.err.printf("WARNING::%s is invalid. It must be between %s to %s, not %s\n", varName, min, max, check);
        return condition ? min : check;
    }


    /**
     * Checks if the given parameter is a power of 2 and within the specified range.
     * @param check the parameter to check
     * @param min the minimum value
     * @param max the maximum value
     * @param varName the name of the variable
     * @return the checked parameter
     */
    private static long powChecker(long check, long min, long max, String varName)
    {
        long result = paramChecker(check, min, max, varName);
        boolean condition = !(result != 0 && ((result & (result - 1)) == 0));
        if(condition) System.err.printf("WARNING::%s must be a power of 2!\n", varName);
        return condition ? min : result;
    }


    /**
     * Returns a string of powers of 2 within the specified range.
     * @param min the minimum value
     * @param max the maximum value
     * @return the string of powers of 2
     */
    private static String powersOf2(long min, long max)
    {
        StringBuilder sb = new StringBuilder();

        int num = (int) min;
        for(int i = 1; i < 16; i *= 2) num |= (num >> i);
        num -= (num >>> 1);

        while(num <= max)
        {
            if(num >= min) sb.append(num).append(", ");
            num <<= 1;
        }

        return sb.toString();
    }
}
