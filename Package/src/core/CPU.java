package core;

import core.utils.AddressSplit;
import core.utils.Calculator;
import core.utils.Statistics;
import data.Block;
import data.Policy;
import data.Set;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CPU
{
    private ArrayList<File> traces = new ArrayList<>();
    private Cache cache;
    private PhysicalMemory physicalMemory;
    private TranslationLookasideBuffer tlb;
    private Policy replacement;
    private Statistics statistics;

    {
        statistics = new Statistics();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Emulation specifics
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String milestone1()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("\n***** Trace file addresses and bytes *****\n");

        for (File trace : traces)
        {
            sb.append(String.format("Trace file: %s\n", trace.getName()));

            try (BufferedReader br = new BufferedReader(new FileReader(trace)))
            {
                String line;
                int addressCounter = 20;

                while (Objects.nonNull(line = br.readLine()) && addressCounter > 0)
                {
                    if (line.isBlank()) continue;

                    String[] elements = Arrays.stream(line.split(" ")).filter(str -> !str.isBlank()).toArray(String[]::new);

                    int bytes = 4;

                    if (Calculator.hashString(elements[0]) == Calculator.hashString("EIP"))
                    {
                        bytes = Integer.parseInt(
                                elements[1].substring(
                                        1, elements[1].indexOf(")")
                                ));
                        sb.append(elements[2]).append(": (").append(String.format("%02d", bytes)).append(")\n");
                        addressCounter--;
                    }
                    else
                    {
                        if (Long.parseLong(elements[1], 16) != 0)
                        {
                            sb.append(elements[1]).append(": (").append(String.format("%02d", bytes)).append(")\n");
                            addressCounter--;
                        }
                        if (Long.parseLong(elements[4], 16) != 0)
                        {
                            sb.append(elements[4]).append(": (").append(String.format("%02d", bytes)).append(")\n");
                            addressCounter--;
                        }
                    }
                }

                sb.append("\n");
            }
            catch (IOException IOe)
            {
                System.err.println("Could not read the trace file \"" + trace.getName() + "\"");
                System.exit(0);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(0);
            }
        }

        return sb.toString();
    }

    public String milestone2A()
    {
        for(File trace : traces) readTraceFile(trace);

        return statistics.toString();
    }

    public void readTraceFile(File trace)
    {
        try(BufferedReader br = new BufferedReader(new FileReader(trace)))
        {
            String line = "sample";

            while(Objects.nonNull(line = br.readLine()))
            {
                if(line.isBlank())
                {
                    getStatistics().incInstructions();
                    continue;
                }

                String[] elements = Arrays.stream(line.split(" ")).filter(str -> !str.isBlank()).toArray(String[]::new);

                int bytes = 4;
                boolean hit = false;

                if(Calculator.hashString(elements[0]) == Calculator.hashString("EIP"))
                {
                    int address = (int) Long.parseLong(elements[2], 16);

                    bytes = Integer.parseInt(
                            elements[1].substring(
                                    1, elements[1].indexOf(")")
                            ));

                    hit = cache.read(address);

                    getStatistics().incCycles(this, hit, bytes);
                    getStatistics().incCycle2();
                }
                else
                {
                    int dstAddr = Calculator.hexToInteger(elements[1]);
                    int srcAddr = Calculator.hexToInteger(elements[4]);

                    if(dstAddr != 0)
                    {
                        if(!elements[3].equals("--------")) cache.write(dstAddr, Calculator.hexToInteger(elements[3]));
                        else hit = cache.read(dstAddr);

                        getStatistics().incCycles(this, hit, bytes);
                    }

                    if(srcAddr != 0)
                    {
                        hit = cache.read(srcAddr);
                        getStatistics().incCycles(this, hit, bytes);
                    }
                }
            }
        }
        catch (IOException IOe)
        {
            System.err.println("Could not read the trace file \"" + trace.getName() + "\"");
            System.exit(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void physicalMemoryWrite(AddressSplit address, int data)
    {
        //In the future, convert physical memory address from virtual address using TLB
        physicalMemory.write(address, data);
    }

    public int physicalMemoryRead(AddressSplit address)
    {
        return physicalMemory.read(address);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Setters and getters
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addTraceFile(File trace)
    {
        traces.add(trace);
    }

    public ArrayList<File> getTraceFiles()
    {
        return traces;
    }

    public Cache getCache()
    {
        return cache;
    }

    public void setCache(Cache cache)
    {
        this.cache = cache;
    }

    public PhysicalMemory getPhysicalMemory()
    {
        return physicalMemory;
    }

    public void setPhysicalMemory(PhysicalMemory physicalMemory)
    {
        this.physicalMemory = physicalMemory;
    }

    public TranslationLookasideBuffer getTlb()
    {
        return tlb;
    }

    public void setTlb(TranslationLookasideBuffer tlb)
    {
        this.tlb = tlb;
    }

    public Policy getReplacementPolicy()
    {
        return replacement;
    }

    public void setReplacementPolicy(Policy replacement)
    {
        this.replacement = replacement;
    }

    public Statistics getStatistics()
    {
        return statistics;
    }

    public String information()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("Cache Simulator CS 3853 Spring 2024 - Group #07\n\n");

        if(!traces.isEmpty())
        {
            for(File trace : traces) sb.append(String.format("Trace file: %s\n", trace.getName()));
        }

        sb.append(String.format("""
                %s***** Cache Input Parameters *****
                Cache Size:                     %s KB
                Block Size:                     %s bytes
                Associativity:                  %s
                Replacement Policy              %s
                Physical Memory                 %s MB
                Percent Memory Used by System:  %s
                Instructions / Time Slice:      %s
                
                ***** Cache Calculated Values *****
                Total # Blocks:                 %s
                Tag Size:                       %s bits
                Index Size:                     %s bits
                Total # Rows:                   %s
                Overhead Size:                  %s bytes
                Implementation Memory Size:     %s KB (%s bytes)
                Cost:                           $%s @ ($0.15 / KB)
                
                ***** Physical Memory Calculations *****
                Number of Physical Pages:       %s
                Number of Pages for System:     %s
                Size of Page Table Entry:       %s bits
                Total RAM for Page Table(s):    %s bytes
                """, (traces.isEmpty()) ? "" : "\n",
                //First Block
                cache.getSize(), cache.getBlockSize(), cache.getAssociativity(), replacement.toString(),
                physicalMemory.getSize() / (1024 * 1024), physicalMemory.getPercentMemoryUnused(), cache.getInstructionPerTimeSlice(),
                //Second Block
                cache.getNumBlocks(), cache.getNumTagBits(), cache.getNumIndexBits(), cache.getNumSets(),
                cache.getOverHeadSize(), String.format("%.2f", cache.getImplementationSizeKB()),
                cache.getImplementationSize(), String.format("%.2f", cache.getImplementationSizeKB() * 0.15),
                //Final Block
                physicalMemory.getNumberOfPhysicalPages(), physicalMemory.getNumberOfSystemPhysicalPages(),
                physicalMemory.getPageTableBits(), physicalMemory.getPageTableRAM()
                ));

        return sb.toString();
    }
}
