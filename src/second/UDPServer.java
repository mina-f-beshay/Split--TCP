package second;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import packet.ControlPck;
import packet.DataPck;
import packet.Packet;

public class UDPServer
{
	private int serverPort;
	private int clientPort;
	private int buffer_size;
	private DatagramSocket ds;
	private byte buffer[];
	private DataPck x;
	private ControlPck control;
	private ByteArrayOutputStream bos;
	private ObjectOutput out;
	public static int seq;
	
	
	public UDPServer(int serverPort, int clientPort, int buffer_size) throws SocketException
	{
		this.serverPort = serverPort;
		this.clientPort = clientPort;
		this.buffer_size = buffer_size;
		seq = 0;
		
		buffer = new byte[buffer_size];
		
		ds = new DatagramSocket(serverPort); 
	}
	
	public void TheServer(String msg) throws Exception
	{
		
		if (msg.length() <= buffer_size)
		{
			buffer = msg.getBytes();
			x = new DataPck(buffer, true, true, 0, msg.length(), seq, msg.length());
			bos = new ByteArrayOutputStream() ;
			out = new ObjectOutputStream(bos);
			out.writeObject(x);
			out.close();
			ds.send(new DatagramPacket(bos.toByteArray(), bos.toByteArray().length, InetAddress.getLocalHost(), clientPort));

			System.out.println("Sending done !");
		}
		else
		{
			System.out.println("Multi packets");
			int itr1 = msg.length() / buffer_size;
			int itr2 = msg.length() % buffer_size;
			System.out.println("itr1: "+itr1 + ", itr2: "+itr2);
			
			int overall = 0;
			int pos;
			for (int i=0 ; i < itr1 ; i++)
			{
				pos = 0;
				while (true)
				{
					if (pos >= buffer_size)
					{
						if (i == 0)
							x = new DataPck(buffer, true, false, 0, pos, seq, msg.length());
						else if ( (i == itr1-1) && itr2 == 0 )
							x = new DataPck(buffer, false, true, overall, pos+overall, seq, msg.length());
						else
							x = new DataPck(buffer, false, false, overall, pos+overall, seq, msg.length());
						
						bos = new ByteArrayOutputStream() ;
						out = new ObjectOutputStream(bos);
						out.writeObject(x);
						out.close();
						ds.send(new DatagramPacket(bos.toByteArray(), bos.toByteArray().length, InetAddress.getLocalHost(), clientPort));

						System.out.println("Sending done !");
						overall += pos;
						System.out.println("itr1: i=" + i + ", overall=" + overall);
						
						
						pos = 0;
						break;
					}
					
					
					int c = msg.charAt(pos+overall);
					buffer[pos++] = (byte) c;
					
				}
			}
			
			System.out.println("======================================");
			
			if (itr2 != 0)
			{
				System.out.println("in itr2");
				System.out.println("itr2 overall=" + overall);
				
				
				buffer = new byte[buffer_size];
				pos = 0;
				for (int i=overall ; i < msg.length() ; i++)
				{	
					int c = msg.charAt(i);
					buffer[pos++] = (byte) c;
				}
				
				x = new DataPck(buffer, false, true, overall, pos+overall, seq, msg.length());
				bos = new ByteArrayOutputStream() ;
				out = new ObjectOutputStream(bos);
				out.writeObject(x);
				out.close();
				ds.send(new DatagramPacket(bos.toByteArray(), bos.toByteArray().length, InetAddress.getLocalHost(), clientPort));
				System.out.println("Sending done !");
					
			}
			
			
		}
		
		seq++;

	}
	
	public void TheServerFile(String name, byte[] msg) throws Exception
	{
		//sending the file details:
		buffer = name.getBytes();
		control = new ControlPck(buffer, seq, msg.length);
		bos = new ByteArrayOutputStream() ;
		out = new ObjectOutputStream(bos);
		out.writeObject(control);
		out.close();
		ds.send(new DatagramPacket(bos.toByteArray(), bos.toByteArray().length, InetAddress.getLocalHost(), clientPort));
		
		//======================================
		
		
		if (msg.length <= buffer_size)
		{
			buffer = msg;
			x = new DataPck(buffer, true, true, 0, msg.length, seq, msg.length);
			bos = new ByteArrayOutputStream() ;
			out = new ObjectOutputStream(bos);
			out.writeObject(x);
			out.close();
			ds.send(new DatagramPacket(bos.toByteArray(), bos.toByteArray().length, InetAddress.getLocalHost(), clientPort));

			System.out.println("Sending done !");
		}
		else
		{
			buffer = new byte[buffer_size];
			
			System.out.println("Multi packets");
			int itr1 = msg.length / buffer_size;
			int itr2 = msg.length % buffer_size;
			System.out.println("itr1: "+itr1 + ", itr2: "+itr2);
			
			int overall = 0;
			int pos = 0;
			for (int i=0 ; i < itr1 ; i++)
			{
				pos = 0;
				while (true)
				{
					if (pos >= buffer_size)
					{
						if (i == 0)
							x = new DataPck(buffer, true, false, 0, pos, seq, msg.length);
						else if ( (i == itr1-1) && itr2 == 0 )
							x = new DataPck(buffer, false, true, overall, pos+overall, seq, msg.length);
						else
							x = new DataPck(buffer, false, false, overall, pos+overall, seq, msg.length);
						
						bos = new ByteArrayOutputStream() ;
						out = new ObjectOutputStream(bos);
						out.writeObject(x);
						out.close();
						ds.send(new DatagramPacket(bos.toByteArray(), bos.toByteArray().length, InetAddress.getLocalHost(), clientPort));

						System.out.println("Sending done !");
						overall += pos;
						System.out.println("itr1: i=" + i + ", overall=" + overall);
						
						
						pos = 0;
						break;
					}
					
					
//					int c = msg.charAt(pos+overall);
//					buffer[pos++] = (byte) c;
					//System.out.println("itr1: i=" + i + ", overall=" + overall + ", pos=" + pos);
					buffer[pos] = msg[pos+overall];
					pos++;
					
				}
			}
			
			System.out.println("======================================");
			
			if (itr2 != 0)
			{
				System.out.println("in itr2");
				
				buffer = new byte[buffer_size];
				
				pos = 0;
				for (int i=overall ; i < msg.length ; i++)
				{	
//					int c = msg.charAt(i);
//					buffer[pos++] = (byte) c;
					buffer[pos] = msg[i];
					pos++;
				}
				
				
				x = new DataPck(buffer, false, true, overall, pos+overall, seq, msg.length);
				bos = new ByteArrayOutputStream() ;
				out = new ObjectOutputStream(bos);
				out.writeObject(x);
				out.close();
				ds.send(new DatagramPacket(bos.toByteArray(), bos.toByteArray().length, InetAddress.getLocalHost(), clientPort));
				System.out.println("Sending done !");
				overall += pos;
				System.out.println("itr2 overall=" + overall);
					
			}
			
			
		}
		
		seq++;
		//*/

	}
	
	}
