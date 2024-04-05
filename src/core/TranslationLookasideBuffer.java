package core;

public class TranslationLookasideBuffer
{
    private Cache cache;
    private PhysicalMemory physicalMemory;

    int tag;
    int index;
    int offset;
    int[] table;

    public TranslationLookasideBuffer(Cache cache, PhysicalMemory physicalMemory)
    {
        this.cache = cache;
        this.physicalMemory = physicalMemory;
    }

}
