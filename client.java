import java.io.*;
import java.net.*;

public class Client {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", PORT); //since they are on the same machine i use localhost instead of an ip
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //create input streams
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); //create output streams
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in)) //create console streams
        ) {
            String fileList = in.readLine();  // receive and print the list of files
            System.out.println("Files: " + fileList);

            String userInput;
            do {
                System.out.print("Enter open and the name of the file to read, or 'exit' to close: ");
                userInput = consoleReader.readLine(); //send the name of the file you want to open
                out.println(userInput);

                if (userInput.startsWith("open")) {
                    String line; // receive and print the file contents
                    while ((line = in.readLine()) != null && !line.equals("file not found")) {
                        System.out.println(line);
                    }
                }
            } while (!userInput.equals("exit")); //stop only when user input is exit

        } catch (IOException e) {
            System.out.println("Error 1");
        }
    }
}
