import java.io.*;
import java.net.*;
import java.util.*;


public class ClientHandler extends Thread{
    private Socket socket = null;

    public ClientHandler(Socket socket) {
        super("ClientHandler");
        this.socket=socket;
    }

    //logs all the requests
    public void log(String inputLine)
    {
        try {
            //Logging access
            InetAddress inet = socket.getInetAddress();
            Date date = new Date();
            FileWriter myWriter = new FileWriter("log.txt", true);

            //date:time:clientIPAddress:request
            myWriter.write("\n"+java.time.LocalDate.now() + ":" + java.time.LocalTime.now() + ":" + inet.getHostAddress()+":"+inputLine);
            myWriter.close();

        }
        catch (IOException e)
        {
            System.out.println("Failed to log.");
        }

    }

    public void run() {
        try {
            // Input and output streams to/from the client.
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //won't go past this line until it reads something
            String inputLine = in.readLine();

            if(inputLine.equals("list")){
                //adds things into the log
                log(inputLine);
                //list the files in the serverFiles directory
                String[] filenames;
                File f = new File("serverFiles");
                filenames = f.list();

                for (String name: filenames) {
                    //write each filename to client
                    out.println(name);
                }
            }
            //if the input line isn't 'get _filename_' or 'list'
            else if (inputLine.startsWith("log"))
            {
                //adds things into the log
                log(inputLine.substring(4, inputLine.length()-1));
                out.println("You must enter 'list' or 'get _filename_'.");
            }
            //else the inputLine is 'filename'
            else {
                //adds things into the log
                log(inputLine);
                BufferedReader reader;
                try {
                    //file object with name filename
                    File f = new File("serverFiles/"+inputLine);
                    //check if the file exists
                    if (!f.exists()) {
                        out.println("The file is empty or does not exist.");
                    }
                    //if it exists
                    else{
                        //create a new reader
                        reader = new BufferedReader(new FileReader(f));
                        //read each line in the file
                        String line = reader.readLine();
                        while (line != null) {
                            //print it to the client
                            out.println(line);
                            line = reader.readLine();
                        }
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //close everything
            in.close();
            out.close();
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
