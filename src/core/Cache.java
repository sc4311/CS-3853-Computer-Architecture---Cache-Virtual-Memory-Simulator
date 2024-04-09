package core;

import core.utils.AddressSplit;
import core.utils.Calculator;
import data.Block;
import data.Policy;
import data.Set;

import java.util.Objects;

public class Cache
{
    //Standard parameters
    private final int size;
    private final int blockSize;
    private final int associativity;
    private final int numBlocks;
    private final int numSets;
    private final int overHeadSize;
    private final long implementationSize;
    private final double implementationSizeKB;
    private final int instructionPerTimeSlice;

    //Cache memory
    Set[] sets;

    //CPU
    CPU referenceCPU;

    //Bitties
    private final int tagBits;
    private final int indexBits;
    private final int blockOffsetBits;


    /**
     * Constructs a Cache object with the specified CPU, size, block size, associativity, and instructions per time slice.
     * @param cpu the CPU
     * @param size the size
     * @param blockSize the block size
     * @param associativity the associativity
     * @param instructionPerTimeSlice the instructions per time slice
     */
    public Cache(CPU cpu, int size, int blockSize, int associativity, int instructionPerTimeSlice)
    {
        referenceCPU = cpu;
        long sizeBytes = Calculator.getBytes(size + "KB");
        this.size = size;
        this.blockSize = blockSize;
        this.associativity = associativity;
        this.instructionPerTimeSlice = instructionPerTimeSlice;

        numBlocks = (int) sizeBytes / blockSize;
        numSets = numBlocks / associativity;
        sets = new Set[numSets];

        //Block offset bits = log_2(blockSize)
        //Index bits = log_2(size) - log_2(blockSize * associativity) -> Alternative: log_2(Cache Size / Block Size / Associativity)
        //Tag bits = 32 - index - offset
        blockOffsetBits = Calculator.getBasePower(blockSize, 2);
        indexBits = Calculator.getBasePower(sizeBytes, 2) - Calculator.getBasePower(blockSize * associativity, 2);
        tagBits = 32 - indexBits - blockOffsetBits;

        implementationSize = (long) (sizeBytes + Math.pow(2, tagBits) + numBlocks);
        implementationSizeKB = implementationSize /1024d;

        overHeadSize = (int) (implementationSize - sizeBytes);

        for(int i = 0; i < sets.length; i++) sets[i] = new Set(associativity, numBlocks);
    }


    /**
     * Checks if the address is not in memory.
     * @param address the address to check
     * @return true if the address is not in memory, false otherwise
     */
    private boolean isAddressNotInMemory(AddressSplit address)
    {
        return Objects.isNull(sets[address.getIndex()].getValidBlock(address));
    }


    /**
     * Inserts the address into the cache.
     * @param address the address to insert
     */
    private void insertAddress(AddressSplit address)
    {
        Set set = sets[address.getIndex()];
        Block removable;

        if(getCPU().getReplacementPolicy() == Policy.RoundRobin) removable = set.getFirstInQueue();
        else removable = set.getRandomBlock();

        if(Objects.isNull(removable))
        {
            System.err.println("ERROR::Unaccounted error - Block should not be null");
            System.exit(0);
        }

        if(removable.isDirty())
        {
            int[] removedData = removable.getData();
            int removedAddr = (removable.getTag() * sets.length + address.getIndex()) * blockSize;
            AddressSplit removedAddress = new AddressSplit(removedAddr, indexBits, blockOffsetBits);
            for(int i = 0; i < blockSize; i++) getCPU().getPhysicalMemory().write(new AddressSplit(removedAddress, i), removedData[i]);
            getCPU().getStatistics().incCompulsoryMisses();
        }

        //Record new data block
        int[] data = new int[blockSize];
        for(int i = 0; i < blockSize; i++) data[i] = getCPU().getPhysicalMemory().read(new AddressSplit(address, i));
        getCPU().getStatistics().incCompulsoryMisses();
        removable.fillBlock(address, data);
    }


    /**
     * Reads data from the cache at the specified address.
     * @param addr the address to read from
     * @return true if the read was successful, false otherwise
     */
    protected boolean read(int addr)
    {
        AddressSplit address = new AddressSplit(addr, indexBits, blockOffsetBits);

        if(isAddressNotInMemory(address))
        {
            insertAddress(address);
            return false;
        }

        Set set = sets[address.getIndex()];
        getCPU().getStatistics().incHits();
        int data = set.readByte(address);
        return true;
    }

    /**
     * Writes data to the cache at the specified address.
     * @param addr the address to write to
     * @param data the data to write
     * @return true if the write was successful, false otherwise
     */
    protected boolean write(int addr, int data)
    {
        AddressSplit address = new AddressSplit(addr, indexBits, blockOffsetBits);

        if(isAddressNotInMemory(address))
        {
            insertAddress(address);
            return false;
        }

        Set set = sets[address.getIndex()];
        getCPU().getStatistics().incHits();
        set.writeByte(address, data);
        return true;
    }

    /**
     * Returns the size of the cache.
     * @return the size of the cache
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Returns the block size of the cache.
     * @return the block size of the cache
     */
    public int getBlockSize()
    {
        return blockSize;
    }

    /**
     * Returns the associativity of the cache.
     * @return the associativity of the cache
     */
    public int getAssociativity()
    {
        return associativity;
    }

    /**
     * Returns the instructions per time slice.
     * @return the instructions per time slice
     */
    public int getInstructionPerTimeSlice()
    {
        return instructionPerTimeSlice;
    }

    /**
     * Returns the number of blocks.
     * @return the number of blocks
     */
    public int getNumBlocks()
    {
        return numBlocks;
    }

    /**
     * Returns the number of sets.
     * @return the number of sets
     */
    public int getNumSets()
    {
        return numSets;
    }

    /**
     * Returns the overhead size.
     * @return the overhead size
     */
    public int getOverHeadSize()
    {
        return overHeadSize;
    }

    /**
     * Returns the implementation size.
     * @return the implementation size
     */
    public long getImplementationSize()
    {
        return implementationSize;
    }

    /**
     * Returns the implementation size in KB.
     * @return the implementation size in KB
     */
    public double getImplementationSizeKB()
    {
        return implementationSizeKB;
    }

    /**
     * Returns the CPU.
     * @return the CPU
     */
    public CPU getCPU()
    {
        return referenceCPU;
    }

    /**
     * Returns the sets.
     * @return the sets
     */
    public Set[] getSets()
    {
        return sets;
    }

    /**
     * Returns the tag bits.
     * @return the tag bits
     */
    public int getNumTagBits()
    {
        return tagBits;
    }

    /**
     * Returns the index bits.
     * @return the index bits
     */
    public int getNumIndexBits()
    {
        return indexBits;
    }

    /**
     * Returns the block offset bits.
     * @return the block offset bits
     */
    public int getNumBlockOffsetBits()
    {
        return blockOffsetBits;
    }
}
