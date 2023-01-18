import java.io.*;
import java.net.*;
import java.sql.SQLOutput;
import java.util.Arrays;

public class Client {
	private Socket socket = null;
	private PrintWriter socketOutput = null;
	private BufferedReader socketInput = null;

	public void run(String[] args) {
		try {
			// Try and create the socket. The server is assumed to be running on the same host ('localhost'),
			// so first run 'KnockKnockServer' in another shell.
			socket = new Socket( "localhost", 8500 );

			// Chain a writing stream
			socketOutput = new PrintWriter(socket.getOutputStream(), true);

			// Chain a reading stream
			socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (UnknownHostException e) {
			System.err.println("Don't know about host.\n");
			System.exit(1);
		}
		catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to host.\n");
			System.exit(1);
		}

		if (args.length <= 2 && args.length>=1) {
			//runs this if it's "list"
			if (args[0].equals("list") && args.length == 1) {
				socketOutput.println(args[0]);
				//HERE I WILL create a loop that will read in lines from the server and print each line as a filename

				try {
					//won't go past this line until it reads something
					String line = socketInput.readLine();
					while (line != null) {
						System.out.println("Filename: " + line);
						line = socketInput.readLine();
					}
				} catch (IOException e) {
					System.out.println("IO Exception when reading from server.");
				}
				System.exit(1);
			}
			// runs this if it's 'get _filename_'
			else if (args[0].equals("get") && args.length == 2) {
				//HERE I WILL create a loop that will read in lines from the server and append each one to a file
				// in my local directory with name args[1]
				socketOutput.println(args[1]);
				try {
					//won't go past this line until it reads something
					String line = socketInput.readLine();
					//if file doesn't exist
					if (line.equals("The file is empty or does not exist.")) {
						System.out.println("The file is empty or does not exist.");
					} else {
						//create a new writer into location 'args[1]' which is the filename.
						FileWriter myWriter = new FileWriter(args[1], true);
						myWriter.write(line + "\n");
						while ((line = socketInput.readLine()) != null) {
							//System.out.println(line);
							myWriter.write(line + "\n");
						}
						//closes writer
						myWriter.close();
					}
				} catch (IOException e) {
					System.out.println("Error when receiving data from server.");
				}
				System.exit(1);
			}
			else {
				//log the request by the client into the server's log file
				try {
					socketOutput.println("log"+ Arrays.toString(args));
					System.out.println(socketInput.readLine());
				}
				catch (IOException e)
				{
					System.out.println("Can't read line from server.");
				}
				System.exit(1);
			}
		}
		else
		{
			//log the request by the client into the server's log file
			try {
				socketOutput.println("log"+ Arrays.toString(args));
				System.out.println(socketInput.readLine());
			}
			catch (IOException e)
			{
				System.out.println("Can't read line from server.");
			}
			System.exit(1);
		}
	}

	public static void main( String[] args ) {
		Client client = new Client();
		client.run(args);
	}
}