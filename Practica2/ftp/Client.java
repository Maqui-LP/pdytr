import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Client {

    private String sourceFilename;
    private String sourcePath;
    private String outputFilename;
    private String outputPath;
    private int bytesToRead;
    private int initialPosition;

    public Client(String sourceFilename, String sourcePath, String outputFilename, String outputPath, int bytesToRead, int initialPosition){
        this.sourceFilename = sourceFilename;
        this.sourcePath = sourcePath;
        this.outputFilename = outputFilename;
        this.outputPath = outputPath;
        this.bytesToRead = bytesToRead;
        this.initialPosition = initialPosition;
    }

    public void read(FtpServerInterface remote) {
        try {
            boolean isEOF = false;
            int position = initialPosition;
            // Se tienen que actualizar initialPosition, isEOF(empieza con false)
            while(!isEOF){
                ReadRequest request = new ReadRequest(sourceFilename, sourcePath, bytesToRead, position);

                ReadResponse response = remote.read(request);

                byte[] data = response.getData();
                int bytesRead = response.getBytesEffectivelyRead();
                isEOF = response.getIsEOF();
                position = position + bytesRead;

                try {
                    Files.write(Paths.get(outputPath), data, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    Files.createFile(Paths.get(outputPath));
                    Files.write(Paths.get(outputPath), data, StandardOpenOption.APPEND);
                }
            }
        } catch(RemoteException e) {
            System.err.println("Error de conexion");
            e.printStackTrace();
        } catch(IOException e) {
            System.err.println("Error de manejo de archivos");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error");
            e.printStackTrace();
        }
    }
}
