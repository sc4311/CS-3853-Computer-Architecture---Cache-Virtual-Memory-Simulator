package core.utils;

public class AddressSplit
{
    //Bitties
    int indexBits;
    int offsetBits;

    int tag;
    int index;
    int offset;
    int address;


    /**
     * Constructs an AddressSplit object by copying another and adding a step to the address.
     * @param copy the AddressSplit object to copy
     * @param step the step to add to the address
     */
    public AddressSplit(AddressSplit copy, int step)
    {
        this(copy.getAddress() + step, copy.getIndexBits(), copy.getOffsetBits());
    }


    /**
     * Constructs an AddressSplit object with the specified address, index bits, and block offset bits.
     * @param address the address
     * @param indexBits the number of index bits
     * @param blockOffsetBits the number of block offset bits
     */
    public AddressSplit(int address, int indexBits, int blockOffsetBits)
    {
        this.indexBits = indexBits;
        this.offsetBits = blockOffsetBits;
        this.address = address;
        this.tag = address >>> (indexBits + blockOffsetBits);
        this.index = (address >>> blockOffsetBits) & ((1 << indexBits) - 1);
        this.offset = address & ((1 << blockOffsetBits) - 1);
    }

    /**
     * Returns the number of index bits.
     * @return the number of index bits
     */
    public int getIndexBits()
    {
        return indexBits;
    }

    /**
     * Returns the number of offset bits.
     * @return the number of offset bits
     */
    public int getOffsetBits()
    {
        return offsetBits;
    }

    /**
     * Returns the tag.
     * @return the tag
     */
    public int getTag()
    {
        return tag;
    }

    /**
     * Returns the index.
     * @return the index
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Returns the offset.
     * @return the offset
     */
    public int getOffset()
    {
        return offset;
    }

    /**
     * Returns the address.
     * @return the address
     */
    public int getAddress()
    {
        return address;
    }

    /**
     * Sets the address.
     * @param address the address
     */
    public void setAddress(int address)
    {
        this.address = address;
    }
}
