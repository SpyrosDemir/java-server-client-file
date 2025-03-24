import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {
	
    private static final int PORT = 8080;
    private static final String FILES_DIRECTORY = ""; //you replace with your path here

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // create server
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); //establish connection with client continuously
                new Thread(() -> handleClient(clientSocket)).start(); //create thread for each client
            }
        } catch (IOException e) {
			System.err.println("Could not create server socket: " + e.getMessage()); //print error message if server failed to open
            System.exit(1);
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
			) 
		{
            System.out.println("New connection from " + clientSocket.getInetAddress());

            List<String> files = getAvailableFileNames(); // get the list of available files
            out.println(String.join(",", files)); // print them 
			
            String request; // receive client requests
            while ((request = in.readLine()) != null) {
                if (request.equals("exit")) {
                    break;
                } else if (request.startsWith("open")) {
                    String fileName = request.substring(5); // extract the filename from the "open" command
                    sendFileContents(fileName, out); //
                    logEntry(clientSocket.getInetAddress().getHostAddress(), fileName); //send information for the log file
                }
            }

            System.out.println("Connection from " + clientSocket.getInetAddress() + " closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getAvailableFileNames() {
        List<String> files = new ArrayList<>(); //create an array to store file names
        File directory = new File(FILES_DIRECTORY);

        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".txt")) {  //check if a file is a txt file
                    files.add(file.getName()); //if yes put name in the list
                }
            }
        }

        return files; //return the list of names
    }

    private static void sendFileContents(String fileName, PrintWriter out) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(FILES_DIRECTORY + fileName))) { //open file
            String line; 	
            while ((line = fileReader.readLine()) != null) { //read file
                out.println(line); //print file contents
            }
        } catch (IOException e) {
            out.println("file not found"); //print error message if file not found
        }
    }

	    private static void logEntry(String clientIP, String fileName) {
        try (PrintWriter logWriter = new PrintWriter(new FileWriter("log.txt", true))) {
            logWriter.println(clientIP + "," + fileName + "," + new Date()); //write to the file
        } catch (IOException e) {
            System.out.println("Error 2");
        }
	}
}
