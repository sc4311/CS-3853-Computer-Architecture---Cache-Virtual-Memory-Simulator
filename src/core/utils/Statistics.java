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


    /**
     * Increments the count of cache hits.
     */
    public void incHits()
    {
        hits++;
    }

    /**
     * Increments the count of cache replacements.
     */
    public void incReplacements()
    {
        replacements++;
    }

    /**
     * Increments the count of compulsory cache misses.
     */
    public void incCompulsoryMisses()
    {
        compulsoryMisses++;
    }

    /**
     * Increments the count of conflict cache misses.
     */
    public void incConflictMisses()
    {
        conflictMisses++;
    }

    /**
     * Increments the count of cycles.
     */
    public void incCycles()
    {
        cycles++;
    }

    /**
     * Increments the count of cycles by 2.
     */
    public void incCycle2()
    {
        cycles += 2;
    }

    /**
     * Increments the count of cycles based on cache hit and bytes.
     * @param cpu the CPU object
     * @param hit whether there was a cache hit
     * @param bytes the number of bytes
     */
    public void incCycles(CPU cpu, boolean hit, int bytes)
    {
        bytesRead += bytes;
        cycles = hit ? cycles++ : (cycles += (4 * (int) Math.ceil((double) cpu.getCache().getBlockSize() / 4)));
    }

    /**
     * Increments the count of unused blocks.
     */
    public void incUnusedBlocks()
    {
        unusedBlocks++;
    }

    /**
     * Increments the count of instructions.
     */
    public void incInstructions()
    {
        instructions++;
    }

    /**
     * Gets the total number of cache accesses.
     * @return the total number of cache accesses
     */
    public int getAccesses()
    {
        return getHits() + getMisses();
    }

    /**
     * Gets the total number of cache hits.
     * @return the total number of cache hits
     */
    public int getHits()
    {
        return hits;
    }

    /**
     * Gets the total number of cache misses.
     * @return the total number of cache misses
     */
    public int getMisses()
    {
        return getCompulsoryMisses() + getConflictMisses();
    }

    /**
     * Gets the total number of replacements.
     * @return the total number of replacements
     */
    public int getReplacements()
    {
        return replacements;
    }

    /**
     * Gets the total number of unused blocks.
     * @return the total number of unused blocks
     */
    public int getUnusedBlocks()
    {
        return unusedBlocks;
    }

    /**
     * Gets the total number of bytes read.
     * @return the total number of bytes read
     */
    public int getBytesRead()
    {
        return bytesRead;
    }

    /**
     * Gets the total number of compulsory misses.
     * @return the total number of compulsory misses
     */
    public int getCompulsoryMisses()
    {
        return compulsoryMisses;
    }

    /**
     * Gets the total number of conflict misses.
     * @return the total number of conflict misses
     */
    public int getConflictMisses()
    {
        return conflictMisses;
    }

    /**
     * Returns the hit rate.
     * @return the hit rate
     */
    public double getHitRate()
    {
        return getRate(getHits());
    }

    /**
     * Returns the miss rate.
     * @return the miss rate
     */
    public double getMissRate()
    {
        return getRate(getMisses());
    }

    /**
     * Returns the rate for a given variable.
     * @param var the variable
     * @return the rate
     */
    public double getRate(int var)
    {
        return (getAccesses() != 0 ? (double) var / getAccesses() : 0) * 100;
    }

    /**
     * Returns the cycles per instruction (CPI).
     * @return the CPI
     */
    public double CPI()
    {
        return (double) cycles / (double) instructions;
    }

    /**
     * Returns a string representation of the statistics.
     * @return a string representation of the statistics
     */
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
