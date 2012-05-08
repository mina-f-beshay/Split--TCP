package packet;

import java.io.Serializable;

public class Packet implements Serializable
{ 
	protected byte msg[];
	/**
	 * This is the total Message Length >> the full file length that is divided into small msg(s) </br>
	 * It is not the msg instance variable in Packet class length.
	 */
	protected int totalMsgLen;
	protected Packet(byte msg[], int totalMsgLen)
	{
		this.msg = msg;
		this.totalMsgLen = totalMsgLen;
	}
	
}
