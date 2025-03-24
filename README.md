# java-server-client-file
A simple client server interaction in java.


**Server.java**
Listens for incoming client connections on port 8080.
Sends a list of .txt files in FILES_DIRECTORY to the client.
Responds to client requests to open files by sending the file contents.
Logs client requests in log.txt.

**Client.java**
Connects to the server and retrieves the list of available files.
Allows users to request files using the open <filename> command.
Displays the contents of the requested file.
Terminates the connection when exit is entered.
