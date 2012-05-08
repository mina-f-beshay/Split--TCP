package packet;

import java.util.Arrays;

public class DataPck extends Packet
{
	private boolean start;
	private boolean end;
	/**
	 * First byte position 
	 */
	private int startSeq;
	
	/**
	 * message length (last byte position + 1) 
	 */
	private int endSeq;
	
	/**
	 * sequence of the file </br>
	 * if the file divided into 3 packets as example, so the three packets must have the same seq 
	 */
	private int seq;
	
	

	public DataPck(byte msg[], boolean start, boolean end, int startSeq, int endSeq, int seq, int totalMsgLen)
	{
		super(msg, totalMsgLen);
		this.start = start;
		this.end = end;
		this.startSeq = startSeq;
		this.endSeq = endSeq;
		this.seq = seq;
	}
	
	//getter and setter
	public boolean isStart()
	{
		return start;
	}
	public void setStart(boolean start)
	{
		this.start = start;
	}
	public boolean isEnd()
	{
		return end;
	}
	public void setEnd(boolean end)
	{
		this.end = end;
	}
	public int getStartSeq()
	{
		return startSeq;
	}
	public void setStartSeq(int startSeq)
	{
		this.startSeq = startSeq;
	}
	public int getEndSeq()
	{
		return endSeq;
	}
	public void setEndSeq(int endSeq)
	{
		this.endSeq = endSeq;
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
		return "DataPck [start=" + start + ", end=" + end + ", startSeq="
				+ startSeq + ", endSeq=" + endSeq + ", seq=" + seq + ", msg:::: you need to customize reading it by getMsg"
				+ "]";
	}
	
	
}
