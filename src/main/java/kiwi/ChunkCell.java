package kiwi;

import java.util.SortedMap;

public class ChunkCell extends Chunk<Cell, Cell>
{
	private static final int DATA_SIZE = 100;		// average # of BYTES of item in data array (guesstimate)
	
	public ChunkCell()
	{
		this(Cell.Empty, null);
	}
	public ChunkCell(Cell minKey, ChunkCell creator)
	{
		super(minKey, DATA_SIZE, creator);
	}
	@Override
	public Chunk<Cell,Cell> newChunk(Cell minKey)
	{
		return new ChunkCell(minKey.clone(), this);
	}
	
	
	@Override
	public Cell readKey(int orderIndex)
	{
		return null;
	}
	@Override
	public Object readData(int oi, int di)
	{
		return null;
	}

	@Override
	public int copyValues(Object[] keys, Object[] values, int idx, int myVer, Cell min, Cell max, SortedMap<Cell, ThreadData.PutData<Cell, Cell>> items) {
		return -1;
	}

	@Override
	public int allocate(Cell key, Cell data)
	{
		return -1;
	}

	@Override
	public int allocateSerial(int key, Cell data) {
		return -1;
	}
}
