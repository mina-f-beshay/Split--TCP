package packet;

public class Ack extends Packet
{
	protected int ack;

	public Ack(byte[] msg, int totalMsgLen)
	{
		super(msg, totalMsgLen);
		this.ack = 1;
	}
	

	
	
}
