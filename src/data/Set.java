package data;

import core.utils.AddressSplit;

import java.util.concurrent.ThreadLocalRandom;

public class Set
{
    private int associativity;
    private int blockSize;
    private int accesses;
    private Block[] blocks;

    /**
     * Constructs a Set object with the specified associativity and block size.
     * @param associativity the associativity of the set
     * @param blockSize the size of the block
     */
    public Set(int associativity, int blockSize)
    {
        this.associativity = associativity;
        this.blockSize = blockSize;
        this.accesses = 0;

        blocks = new Block[associativity];
        for(int i = 0; i < associativity; i++) blocks[i] = new Block();
    }

    /**
     * Returns the blocks in the set.
     * @return the blocks in the set
     */
    public Block[] getBlocks()
    {
        return blocks;
    }

    /**
     * Returns the valid block at the specified address.
     * @param address the address to get the block from
     * @return the valid block at the specified address
     */
    public Block getValidBlock(AddressSplit address)
    {
        for(Block block : blocks) if (block.getTag() == address.getTag() && block.isValid()) return block;
        return null;
    }

    /**
     * Returns the first block in the queue.
     * @return the first block in the queue
     */
    public Block getFirstInQueue()
    {
        //First choice: Pick a block that is not yet valid
        for(Block block : blocks) if(!block.isValid()) return block;

        //Second choice: Pick a block with the lowest queueNumber, as that means it was accessed first
        int min = 0;
        for(int i = 1; i < associativity; i++)
        {
            if(blocks[i].getQueueNumber() >= 1) min = Math.min(min, blocks[i].getQueueNumber());
        }

        return blocks[min];
    }

    /**
     * Returns a random block from the set.
     * @return a random block from the set
     */
    public Block getRandomBlock()
    {
        for(Block b : blocks) if(!b.isValid()) return b;

        return blocks[ThreadLocalRandom.current().nextInt(0, associativity)];
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Reading and writing
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Reads a byte from the block at the specified address.
     * @param address the address to read the byte from
     * @return the byte read from the block
     */
    public int readByte(AddressSplit address)
    {
        Block current = getValidBlock(address);
        return current.read(address, ++accesses);
    }

    /**
     * Reads bytes from the block at the specified address.
     * @param address the address to read the bytes from
     * @param bytes the number of bytes to read
     * @return the bytes read from the block
     */
    public int[] readBytes(AddressSplit address, int bytes)
    {
        int[] data = new int[bytes];

        for(int i = 0; i < bytes; i++)
        {
            AddressSplit currentAddress = new AddressSplit(address, i);

            Block current = getValidBlock(address);

            data[i] = current.read(address, ++accesses);
        }

        return data;
    }

    /**
     * Writes a byte to the block at the specified address.
     * @param address the address to write the byte to
     * @param data the byte to write
     */
    public void writeByte(AddressSplit address, int data)
    {
        Block current = getValidBlock(address);
        current.write(address, data, ++accesses);
    }

    /**
     * Writes bytes to the block at the specified address.
     * @param address the address to write the bytes to
     * @param bytes the number of bytes to write
     */
    public void writeBytes(AddressSplit address, int bytes)
    {
        int[] data = readBytes(address, bytes);

        for(int i = 0; i < bytes; i++)
        {
            Block current = getValidBlock(new AddressSplit(address, i));
            current.write(address, data[i], ++accesses);
        }
    }
}
