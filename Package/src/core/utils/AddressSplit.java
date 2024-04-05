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

    public AddressSplit(AddressSplit copy, int step)
    {
        this(copy.getAddress() + step, copy.getIndexBits(), copy.getOffsetBits());
    }

    public AddressSplit(int address, int indexBits, int blockOffsetBits)
    {
        this.indexBits = indexBits;
        this.offsetBits = blockOffsetBits;
        this.address = address;
        this.tag = address >>> (indexBits + blockOffsetBits);
        this.index = (address >>> blockOffsetBits) & ((1 << indexBits) - 1);
        this.offset = address & ((1 << blockOffsetBits) - 1);
    }

    public int getIndexBits()
    {
        return indexBits;
    }

    public int getOffsetBits()
    {
        return offsetBits;
    }

    public int getTag()
    {
        return tag;
    }

    public int getIndex()
    {
        return index;
    }

    public int getOffset()
    {
        return offset;
    }

    public int getAddress()
    {
        return address;
    }

    public void setAddress(int address)
    {
        this.address = address;
    }
}
