import java.rmi.RemoteException;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;


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
           
            ReadRequest request = new ReadRequest(sourceFilename, sourcePath, bytesToRead, position);

            ReadResponse response = remote.read(request);

            byte[] data = response.getData();
            int bytesRead = response.getBytesEffectivelyRead();
            isEOF = response.getIsEOF();
            if (data.length > bytesRead) {
                data = Arrays.copyOf(data, bytesRead);
            }
            position = position + bytesRead;
            Files.write(Paths.get(outputPath + outputFilename), data, StandardOpenOption.CREATE,StandardOpenOption.APPEND);                    
                
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

    public void write(FtpServerInterface remote, String filename,String sourceFile){
        try {

            RandomAccessFile file = new RandomAccessFile(Paths.get(sourceFile).toString(),"r");
            FileDescriptor fd = file.getFD();
            FileInputStream fis = new FileInputStream(fd);
            byte[] partialData = new byte[1024];

            int bytesReaded;
            int totalBytesWritten = 0;

            while(fis.available() > 0){
                bytesReaded = fis.read(partialData,0,Math.min(1024, fis.available()));
                byte[]cleanArray = Arrays.copyOf(partialData, bytesReaded);
                totalBytesWritten += remote.write(new WriteRequest(filename, bytesReaded, cleanArray));
            }

            fis.close();
            System.out.println("Operacion write finalizada.");
            String outputMsj = String.format("Se escribieron efectivamente %d bytes en total.",totalBytesWritten);
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
        int bytes = 0;
        try {
            bytes = Files.readAllBytes(Paths.get("/pdytr/archivos/ejercicio-b")).length;
        } catch(IOException e) {
            e.printStackTrace();
        }
        Client clientToRead = new Client("ejercicio-b","/pdytr/archivos/","outputEjercicio-b","/pdytr/archivos/", bytes, 0);
        System.out.println("Copiando en filesystem del cliente un archivo del servidor...");
        clientToRead.read(remote);
        System.out.println("Fin copia... se genero el archivo: outputEjercicio-b");
        Client clientToWrite = new Client.Builder().build();
        try {
            clientToWrite.write(remote, "nuevo-archivo-ejercicio-b","/pdytr/archivos/outputEjercicio-b");
            System.out.println("Se genero el archivo: nuevo-archivo-ejercicio-b");
        }catch(Exception e){
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
