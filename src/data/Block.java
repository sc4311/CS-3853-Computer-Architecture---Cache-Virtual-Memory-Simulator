package data;

import core.utils.AddressSplit;

public class Block
{
    boolean valid;
    boolean dirty;
    int tag;
    int blockSize;
    int queueNumber;

    int[] data;

    /**
     * Default constructor that initializes a block with size 0.
     */
    public Block()
    {
        this(0);
    }

    /**
     * Constructor that initializes a block with the specified size.
     * @param blockSize the size of the block
     */
    public Block(int blockSize)
    {
        this.blockSize = blockSize;
        this.data = new int[blockSize];
        queueNumber = 0;
        this.dirty = false;
        this.valid = false;
    }

    /**
     * Reads data from the block at the specified address.
     * @param address the address to read from
     * @param access the access number
     * @return the data read from the block
     */
    public int read(AddressSplit address, int access)
    {
        if(queueNumber == 0) queueNumber = access;
        return data[address.getOffset()];
    }

    /**
     * Writes data to the block at the specified address.
     * @param address the address to write to
     * @param data the data to write
     * @param access the access number
     */
    public void write(AddressSplit address, int data, int access)
    {
        if(queueNumber == 0) queueNumber = access;
        this.data[address.getOffset()] = data;
        setValid(true);
        setDirty(true);
    }

    /**
     * Fills the block with the specified data.
     * @param address the address of the block
     * @param data the data to fill the block with
     */
    public void fillBlock(AddressSplit address, int[] data)
    {
        this.tag = address.getTag();
        this.data = data;
        setValid(true);
        setDirty(false);
    }

    public boolean isValid()
    {
        return valid;
    }

    public void setValid(boolean valid)
    {
        this.valid = valid;
    }

    public boolean isDirty()
    {
        return dirty;
    }

    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
    }

    public int getTag()
    {
        return tag;
    }

    public void setTag(int tag)
    {
        this.tag = tag;
    }

    public int[] getData()
    {
        return this.data;
    }

    public int getQueueNumber()
    {
        return queueNumber;
    }


    /**
     * Returns a string representation of the block.
     * @return a string representation of the block
     */
    public String toString()
    {
        return String.format("""
                Tag:        %s
                Queue:      %s
                Valid?      %b
                Dirty?      %b
                """,
                getTag(), getQueueNumber(),
                isValid(), isDirty());
    }
}
