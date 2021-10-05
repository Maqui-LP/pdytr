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

    private Client(Builder builder){
        this.sourceFilename = builder.sourceFilename;
        this.sourcePath = builder.sourcePath;
        this.outputFilename = builder.outputFilename;
        this.outputPath = builder.outputPath;
        this.bytesToRead = builder.bytesToRead;
        this.initialPosition = builder.initialPosition;        
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
                Files.write(Paths.get(outputPath + outputFilename), data, StandardOpenOption.CREATE,StandardOpenOption.APPEND);                    
                
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

    public void write(FtpServerInterface remote, String filename, byte[] data){
        try {

            WriteRequest request = new WriteRequest(filename, data.length, data);
            int response = remote.write(request);
            
            String outputMsj = String.format("Se enviaron %d bytes y se escribieron efectivamente %d bytes.",data.length, response);
            System.out.println(outputMsj);

        }catch(RemoteException e){
            System.err.println("Error de conexion.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error");
            e.printStackTrace();
        }
    }

    public static void resolucionTresB(FtpServerInterface remote){
        Client clientToRead = new Client("ejercicio-b","/pdytr/archivos/","outputEjercicio-b","/pdytr/archivos/", 100, 0);
        System.out.println("Copiando en filesystem del cliente un archivo del servidor...");
        clientToRead.read(remote);
        System.out.println("Fin copia..");
        Client clientToWrite = new Client.Builder().build();
        try {
            clientToWrite.write(remote, "nuevo-archivo-ejercicio-b", Files.readAllBytes(Paths.get("/pdytr/archivos/outputEjercicio-b")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Fin ejercicio 2.B");
    }
    
    public static void timeout(FtpServerInterface remote){
        try {
           remote.timeout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Builder{
        private String sourceFilename = "";
        private String sourcePath = "";
        private String outputFilename = "";
        private String outputPath = "";
        private int bytesToRead = 0;
        private int initialPosition = 0;        

        public Builder(){};

        public Builder sourceFilename(String sourceFilename){
            this.sourceFilename = sourceFilename;
            return this;
        }

        public Builder sourcePath(String sourcePath){
            this.sourcePath = sourcePath;
            return this;
        }

        public Builder outputFilename(String outputFilename){
            this.outputFilename = outputFilename;
            return this;
        }

        public Builder outputPath(String outputPath){
            this.outputPath = outputPath;
            return this;
        }

        public Builder bytesToRead(int bytesToRead){
            this.bytesToRead = bytesToRead;
            return this;
        }

        public Builder initialPositio(int initialPosition){
            this.initialPosition = initialPosition;
            return this;
        }

        public Client build(){
            return new Client(this);
        }
    }
}
