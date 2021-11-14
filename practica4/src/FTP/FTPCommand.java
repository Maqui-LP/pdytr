import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FTPCommand {
    public static byte[] read(String path, int position, int currentSize, long fileSize){
        int chunck = 1024;
        int noBytes = ((int)fileSize - currentSize) < chunck
                ?(int)(fileSize - currentSize)
                : chunck;
        byte[] content = new byte[noBytes];
        try{
            System.out.println("Reading " + noBytes + " bytes from " + currentSize);
            InputStream in = new FileInputStream(path);
            in.skip(currentSize);
            in.read(content,0,noBytes);
            in.close();
        }catch(Exception e){
            e.printStackTrace();
            return new byte[0];
        }
        return content;
    }

    public static int write(String path, byte[] data, int currentSize, long fileSize){
        try{
            Files.write(Paths.get(path),data, StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        }catch(Exception e){
            System.out.println("ERROR al crear/escribir el archivo, causa: " + e.getCause());
            return -1;
        }
        return data.length;
    }
}
