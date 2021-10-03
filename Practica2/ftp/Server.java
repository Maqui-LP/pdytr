import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.io.RandomAccessFile;

public class Server extends UnicastRemoteObject implements FtpServerInterface {

    public Server() throws RemoteException{

    }

    @Override
    public ReadResponse read(ReadRequest request) throws Exception {

        RandomAccessFile file = new RandomAccessFile(request.getPath(), "r");
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
       /**
        * Implementacion
        */
        return 0;
    }

}