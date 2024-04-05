package core.utils;

import core.CPU;

public class Statistics
{
    //Access = Hits + Misses
    //Miss -> Either the valid bit wasn't on, and it was accessed, or the tag didn't match.
    private int hits;                       //Valid bit was on and the tag matched
    private int replacements;               //Counting how many times something was replaced
    private int compulsoryMisses;           //Valid bit wasn't on.
    private int conflictMisses;             //Valid bit was on, but the tag did not match in the set
    private int cycles;                     //Cycles per cache read/write
    private int instructions;               //Instructions executed in trace file
    private int bytesRead;
    private int unusedBlocks;

    public void incHits()
    {
        hits++;
    }

    public void incReplacements()
    {
        replacements++;
    }

    public void incCompulsoryMisses()
    {
        compulsoryMisses++;
    }

    public void incConflictMisses()
    {
        conflictMisses++;
    }

    public void incCycles()
    {
        cycles++;
    }

    public void incCycle2()
    {
        cycles += 2;
    }

    public void incCycles(CPU cpu, boolean hit, int bytes)
    {
        bytesRead += bytes;
        cycles = hit ? cycles++ : (cycles += (4 * (int) Math.ceil((double) cpu.getCache().getBlockSize() / 4)));
    }

    public void incUnusedBlocks()
    {
        unusedBlocks++;
    }

    public void incInstructions()
    {
        instructions++;
    }

    public int getAccesses()
    {
        return getHits() + getMisses();
    }

    public int getHits()
    {
        return hits;
    }

    public int getMisses()
    {
        return getCompulsoryMisses() + getConflictMisses();
    }

    public int getReplacements()
    {
        return replacements;
    }

    public int getUnusedBlocks()
    {
        return unusedBlocks;
    }

    public int getBytesRead()
    {
        return bytesRead;
    }

    public int getCompulsoryMisses()
    {
        return compulsoryMisses;
    }

    public int getConflictMisses()
    {
        return conflictMisses;
    }

    public double getHitRate()
    {
        return getRate(getHits());
    }

    public double getMissRate()
    {
        return getRate(getMisses());
    }

    public double getRate(int var)
    {
        return (getAccesses() != 0 ? (double) var / getAccesses() : 0) * 100;
    }

    public double CPI()
    {
        return (double) cycles / (double) instructions;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("***** CACHE SIMULATION RESULTS *****\n");
        sb.append(String.format("Total Cache Accesses:          %s (%s addresses)\n", getAccesses(), "Placeholder"));
        sb.append(String.format("Instruction Bytes:             %s SrcDst Bytes: %s\n", getBytesRead(), "Placeholder"));
        sb.append(String.format("Cache Hits:                    %s\n", getHits()));
        sb.append(String.format("Cache Misses:                  %s\n", getMisses()));
        sb.append(String.format("--- Compulsory Misses:         %s\n", getCompulsoryMisses()));
        sb.append(String.format("--- Conflict Misses:           %s\n\n", getConflictMisses()));

        sb.append("***** *****  CACHE HIT & MISS RATE:  ***** *****\n");
        sb.append(String.format("Hit Rate:                      %s%%\n", String.format("%.4f", getHitRate())));
        sb.append(String.format("Miss Rate:                     %s%%\n", String.format("%.4f", getMissRate())));
        sb.append(String.format("CPI:                           %s Cycles/Instruction\n", String.format("%.2f", CPI())));

        return sb.toString();
    }
}
