package core;

public class TranslationLookasideBuffer
{
    private Cache cache;
    private PhysicalMemory physicalMemory;

    int tag;
    int index;
    int offset;
    int[] table;

    /**
     * Constructs a TranslationLookasideBuffer object with the specified cache and physical memory.
     * @param cache the cache
     * @param physicalMemory the physical memory
     */
    public TranslationLookasideBuffer(Cache cache, PhysicalMemory physicalMemory)
    {
        this.cache = cache;
        this.physicalMemory = physicalMemory;
    }

}
