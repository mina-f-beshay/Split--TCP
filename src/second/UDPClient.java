package second;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import packet.ControlPck;
import packet.DataPck;
import packet.Packet;

public class UDPClient
{
	private int serverPort;
	private int clientPort;
	private int buffer_size;
	private DatagramSocket ds;
	private byte buffer[]; 
	private DataPck y;
	private ControlPck control;
	private Packet pck;
	private ObjectInputStream in;
	
	//constant
	public static final int packetGuardSize = 256;
	//end constant
	
	public UDPClient(int serverPort,int clientPort, int buf_size) throws SocketException
	{
		this.serverPort = serverPort;
		this.clientPort = clientPort;
		this.buffer_size = buf_size+packetGuardSize; //hard coded
		
		buffer = new byte[buffer_size];
		
		ds = new DatagramSocket(clientPort); 
	}
	
	public void TheClient() throws Exception
	{
		byte outTotal[] = null;
		int currentSeq = -1;
		int pos = 0;
		while (true)
		{
			DatagramPacket p = new DatagramPacket(buffer, buffer_size);
			ds.receive(p); //block

			in = new ObjectInputStream(new ByteArrayInputStream(buffer));
			y = (DataPck) in.readObject();
			
			if (!y.isEnd() && y.isStart())
			{
				currentSeq = y.getSeq();
				outTotal = new byte[y.getTotalMsgLen()];
				for (int i=0 ; i < y.getMsg().length ; i++)
				{
					outTotal[pos++] = y.getMsg()[i];
				}
			}
			else if (!y.isEnd() && !y.isStart())
			{
				if (y.getSeq() == currentSeq)
				{
					for (int i=0 ; i < y.getMsg().length ; i++)
					{
						outTotal[pos++] = y.getMsg()[i];
					}
				}
				else
					System.out.println("Discarding  packet with seq: "+ y.getSeq());
			}
			else if (y.isEnd() && !y.isStart())
			{
				for (int i=0 ; i < y.getEndSeq()-y.getStartSeq() ; i++)
				{
					outTotal[pos++] = y.getMsg()[i];
				}
				System.out.println("Packet info: "+ "file seq=" + y.getSeq() + ", lenght of the total message after concatinate=" + pos);
				System.out.println(new String(outTotal, 0, pos));
				outTotal = null;
				currentSeq = -1;
				pos = 0;
			}
			else
			{
				System.out.println("Packet info: " + y.toString());
				System.out.println(new String(y.getMsg(), 0, y.getMsg().length));
			}
			

			//return;
		}
	}
	/*
	collect the file packets and save it.
	*/
	public void TheClientFile() throws Exception
	{
		String name = "";
		byte outTotal[] = null;
		int currentSeq = -1;
		int pos = 0;
		while (true)
		{
			DatagramPacket p = new DatagramPacket(buffer, buffer_size);
			ds.receive(p); //block

			in = new ObjectInputStream(new ByteArrayInputStream(buffer));
			pck = (Packet) in.readObject();
			
			if (pck.getClass().toString().equals("class packet.ControlPck"))
			{
			    // 	if (y.getSeq() != currentSeq)
				System.out.println("Control");
				control = (ControlPck) pck;
				System.out.println(control);
				currentSeq = control.getSeq();
				outTotal = new byte[control.getTotalMsgLen()];
				name = new String(control.getMsg(), 0, control.getMsg().length);
				
				continue;
			}	
			else if (pck.getClass().toString().equals("class packet.DataPck"))
			{
				System.out.println("Data");
			}
			else
			{
				System.out.println("Packet Error ! Supported types only(Data, Control)");
				continue;
			}
				
			
				
			y = (DataPck) pck;
			
			if (!y.isEnd() && y.isStart()) //first
			{
				if (y.getSeq() != currentSeq)
					throw new Exception("First packet received without control packet before it !");
				
				for (int i=0 ; i < y.getMsg().length ; i++)
				{
					outTotal[pos++] = y.getMsg()[i];
				}
			}
			else if (!y.isEnd() && !y.isStart()) //Intermediate
			{
				if (y.getSeq() == currentSeq)
				{
					for (int i=0 ; i < y.getMsg().length ; i++)
					{
						outTotal[pos++] = y.getMsg()[i];
					}
				}
				else
					System.out.println("[Out Of Order] Discarding  packet with seq: "+ y.getSeq() + ", As it is not the currsnt seq !");
			}
			else if (y.isEnd() && !y.isStart()) //last
			{
				if (y.getSeq() == currentSeq)
				{
					for (int i=0 ; i < y.getEndSeq()-y.getStartSeq() ; i++)
					{
						outTotal[pos++] = y.getMsg()[i];
					}
					System.out.println("Packet info: "+ "file seq=" + y.getSeq() + ", lenght of the total file after concatinate=" + pos);
					File someFile = new File(name);
					FileOutputStream fos = new FileOutputStream(someFile);
					fos.write(outTotal);
					fos.flush();
					fos.close();
					
					name = "";
					outTotal = null;
					currentSeq = -1;
					pos = 0;
				}
				else
					System.out.println("[Out Of Order] Discarding  packet with seq: "+ y.getSeq() + ", As it is not the currsnt seq !");
			}
			else //the file only 1 packet :)
			{
				System.out.println("Packet info: " + y.toString());
				File someFile = new File(name);
				FileOutputStream fos = new FileOutputStream(someFile);
				fos.write(outTotal);
				fos.flush();
				fos.close();
				
				name = "";
				outTotal = null;
				currentSeq = -1;
				pos = 0;

			}
			

			//return;
			//*/
		}
		
	}
	
}
