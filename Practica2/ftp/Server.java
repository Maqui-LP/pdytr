import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.FileDescriptor;
import java.io.FileInputStream;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.RandomAccessFile;
import java.math.BigInteger;

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
    public int write(WriteRequest request)throws RemoteException{

        Path path = Paths.get(this.outPath + request.getFilename());
        int totalBytesBeforesWrite = 0 ;
        try {
            if(Files.exists(path)){
                totalBytesBeforesWrite = Files.readAllBytes(path).length;
            }
            
            Files.write(path, request.getData(), StandardOpenOption.CREATE,StandardOpenOption.APPEND);            
            
            if(totalBytesBeforesWrite > 0){
                totalBytesBeforesWrite = Files.readAllBytes(path).length - totalBytesBeforesWrite;
            }else {
                totalBytesBeforesWrite = Files.readAllBytes(path).length;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        } 
        
        return totalBytesBeforesWrite;        
    }

    @Override
    public boolean giveMeABoolean() throws Exception{
        return true;
    };

    @Override
    public void timeout() throws Exception{
        BigInteger  count = BigInteger.ONE;
        while(true){
            if(count == BigInteger.valueOf(99999999999999999L)){
                break;
            }
        };
    }
   
}