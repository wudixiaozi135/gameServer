package com.xiaoding.data;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by xiaoding on 2016/5/20.
 * 网络包用于读写
 */
public class NetPacket
{
    public static final int defaultSize = 512;
    private ByteBuffer byteBuffer;
    private int position = 0;

    public NetPacket()
    {
        byteBuffer = ByteBuffer.allocate(defaultSize);
    }

    public NetPacket writeLong(long value)
    {
        position += 8;
        int isOverflow = isOverflow();
        if (isOverflow > 0)
        {
            this.reAllocate();
            byteBuffer.putLong(isOverflow, value);
        } else
        {
            byteBuffer.putLong(value);
        }
        return this;
    }

    /*
    * 写完再调用此方法，相当于有多少字节传送多少字节
    * */
    public void limit()
    {
        byteBuffer.limit(byteBuffer.position());
    }

    /*
    * 重新分配
    * */
    private void reAllocate()
    {
        byteBuffer.position(0);
        ByteBuffer temp = byteBuffer.duplicate();
        int size = temp.capacity();
        byteBuffer = ByteBuffer.allocate(size + defaultSize);
        byte[] data = new byte[size];
        temp.get(data);
        byteBuffer.put(data);
    }

    /*
    * >0溢出 <=0正常
    * */
    private int isOverflow()
    {
        int pos = position - byteBuffer.capacity();
        return pos;
    }

    public NetPacket writeInt(int value)
    {
        position += 4;
        int isOverflow = isOverflow();
        if (isOverflow > 0)
        {
            this.reAllocate();
            byteBuffer.putInt(isOverflow, value);
        } else
        {
            byteBuffer.putInt(value);
        }
        return this;
    }


    public NetPacket writeShort(int value)
    {
        position += 2;
        int isOverflow = isOverflow();
        if (isOverflow > 0)
        {
            this.reAllocate();
            byteBuffer.putShort(isOverflow, (short) value);
        } else
        {
            byteBuffer.putShort((short) value);
        }
        return this;
    }


    public NetPacket writeByte(int value)
    {
        position += 1;
        int isOverflow = isOverflow();
        if (isOverflow > 0)
        {
            this.reAllocate();
            byteBuffer.put(isOverflow, (byte) value);
        } else
        {
            byteBuffer.put((byte) value);
        }
        return this;
    }


    public NetPacket writeUTF8(String value)
    {
        byte[] bytes = new byte[0];
        try
        {
            bytes = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        int size = bytes.length;
        this.writeShort(size);
        position += size;

        int isOverflow = isOverflow();
        if (isOverflow > 0)
        {
            this.reAllocate();
            byteBuffer.position(isOverflow);
            byteBuffer.put(bytes);
        } else
        {
            byteBuffer.put(bytes);
        }
        return this;
    }

    public static String readUTF8(ByteBuffer buffer)
    {
        int len = buffer.getShort();
        byte[] bytes = new byte[len];
        buffer.get(bytes);
        try
        {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public ByteBuffer getByteBuffer()
    {
        if (byteBuffer != null)
        {
            byteBuffer.position(0);
        }
        return byteBuffer;
    }

    public void dispose()
    {
        this.position = 0;
        if (this.byteBuffer != null)
        {
            this.byteBuffer.clear();
            this.byteBuffer = null;
        }
    }
}
