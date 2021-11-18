import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;

public class FTPCommand {
    public static byte[] read(String path, int position, int currentSize, long fileSize)
        {
            try {
                //int chunk = 1024;
                int chunk = 200_000;
                int noBytes = ((int) fileSize - currentSize) < chunk
                        ? (int) (fileSize - currentSize)
                        : chunk; 
                
                byte[] contents = new byte[noBytes];

                System.out.printf("Reading %d bytes from %d\n", noBytes, currentSize);

                InputStream in = new FileInputStream(path);
                in.skip(currentSize);
                in.read(contents, 0, noBytes);
                in.close();

                return contents;
            } catch(IOException e) {
                System.out.println(e);
                return new byte[0];
            }
            
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
