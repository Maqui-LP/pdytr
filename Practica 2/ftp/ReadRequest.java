package ftp;

public class ReadRequest {
    
    private String filename;
    private String path;
    private int bytesToRead;

    public ReadRequest(String filename, String path, int bytesToRead){
        this.filename = filename;
        this.path = path;
        this.bytesToRead = bytesToRead;
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
    
}
