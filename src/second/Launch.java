package second;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Launch
{
	public static int serverPort = 666;
	public static int clientPort = 999;
	public static int buffer_size = 128; //the msg length in the packet 
	
	public static void main(String[] args) throws Exception
	{
		//h (client) & f (server) for file sending
		char ch = 'f';
		switch (ch)
		{
		case 's':
			UDPServer x = new UDPServer(serverPort, clientPort, buffer_size);
			x.TheServer("How are you?");
		break;
		case 'c':
			UDPClient y = new UDPClient(serverPort, clientPort, buffer_size);
			y.TheClient();
		break;
		case 'k':
			UDPServer k = new UDPServer(serverPort, clientPort, buffer_size);
			for (int i=0 ; i<10 ; i++ )
				k.TheServer("How are you? > " + i);
		break;
		case 'f':
			UDPServer f = new UDPServer(serverPort, clientPort, buffer_size);
			String path = "F:\\CSD\\eclipse\\WorkSpace\\Networks2\\My Files\\Birth and Death of Nebula.jpg";
			f.TheServerFile(path.substring(path.lastIndexOf("\\")+1), getBytesFromFile(new File(path)));
		break;	
		case 'h':
			UDPClient h = new UDPClient(serverPort, clientPort, buffer_size);
			h.TheClientFile();
		break;	
		}	
	}
	
	public static byte[] getBytesFromFile(File file) throws IOException
	{
		InputStream inStream = new FileInputStream(file);

		int flength;

		if (file.length() > Integer.MAX_VALUE)
		{
			System.out.println("file too big, can't process !");
			return null;
		}

		flength = (int) file.length();

		// Create the byte array to hold the data
		byte[] bytes = new byte[flength];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = inStream.read(bytes, offset, bytes.length - offset)) >= 0)
		{
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length)
		{
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		inStream.close();
		return bytes;
	}
}
