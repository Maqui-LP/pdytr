import java.io.Serializable;

public class ReadRequest implements Serializable {
    
    private String filename;
    private String path;
    private int bytesToRead;
    private int initialPosition;

    public ReadRequest(String filename, String path, int bytesToRead, int initialPosition){
        this.filename = filename;
        this.path = path;
        this.bytesToRead = bytesToRead;
        this.initialPosition = initialPosition;
    }

    public String getFilename(){
        return this.filename;
    }

    public String getPath(){
        return this.path;
    }

    public int getBytesToRead(){
        return this.bytesToRead;
    }
    
    public int getInitialPosition() {
        return this.initialPosition;
    }
}
