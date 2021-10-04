import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.RandomAccessFile;

public class Server extends UnicastRemoteObject implements FtpServerInterface {

    private final String outPath = "/pdytr/archivos/";

    public Server() throws RemoteException{

    }

    @Override
    public ReadResponse read(ReadRequest request) throws Exception {

        RandomAccessFile file = new RandomAccessFile(request.getPath() + request.getFilename(), "r");
        FileDescriptor fileDescriptor = file.getFD();
        FileInputStream fileInputStream = new FileInputStream(fileDescriptor);
        int bytesAskedToRead = request.getBytesToRead();
        int pos = request.getInitialPosition();
        // si debo leer mas de los disponibles, me quedo con los disponibles
        int bytesToRead = Math.min(bytesAskedToRead, fileInputStream.available());

        byte[] data = new byte[bytesToRead];
        file.seek(pos);

        fileInputStream.read(data, 0, bytesToRead);
        boolean isEOF = (fileInputStream.available() == 0);
        fileInputStream.close();

        ReadResponse response = new ReadResponse(data, data.length, isEOF);

        return response;
    }

    @Override
    public int write(WriteRequest request) throws Exception {

        Path path = Paths.get(this.outPath + request.getFilename());
        int totalBytesBeforesWrite = Files.readAllBytes(path).length;
        //OutputStream out = Files.newOutputStream(path, StandardOpenOption.APPEND,StandardOpenOption.CREATE);        
        //out.write(request.getData(), 0, request.getData().length);
        System.out.println("es ejecutable: " + Files.isExecutable(path));
        System.out.println("es escribible: " + Files.isWritable(path));
          try {
            Files.write(path, request.getData(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            Files.createFile(path);
                Files.write(path, request.getData(), StandardOpenOption.APPEND);
        }
        //Files.write(path, request.getData(),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        return Files.readAllBytes(path).length - totalBytesBeforesWrite;
        
    }

}