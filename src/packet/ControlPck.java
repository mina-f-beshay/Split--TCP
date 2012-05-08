package packet;

import java.util.Arrays;

public class ControlPck extends Packet
{
	//the same seq of the data packet
	private int seq;

	public ControlPck(byte[] msg, int seq, int totalMsgLen)
	{
		super(msg, totalMsgLen);
		this.seq = seq;
	}
	
	public int getSeq()
	{
		return seq;
	}
	public void setSeq(int seq)
	{
		this.seq = seq;
	}
	public byte[] getMsg()
	{
		return msg;
	}
	public void setMsg(byte[] msg)
	{
		this.msg = msg;
	}
	public int getTotalMsgLen()
	{
		return totalMsgLen;
	}
	public void setTotalMsgLen(int totalMsgLen)
	{
		this.totalMsgLen = totalMsgLen;
	}

	@Override
	public String toString()
	{
		return "ControlPck [seq=" + seq + ", totalMsgLen=" + totalMsgLen
				+ ", msg=" + new String(msg, 0, msg.length) + "]";
	}


	
	
}
