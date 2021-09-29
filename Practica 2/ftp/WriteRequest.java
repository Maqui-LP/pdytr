package ftp;

public class WriteRequest {
    
    private String filename;
    private int bytes;
    private byte[] data;

    public WriteRequest(String filename, int bytes, byte[] data){
        this.filename = filename;
        this.bytes = bytes;
        this.data = data;
    }

    public String getFilename(){
        return this.filename;
    }

    public int getBytes(){
        return this.bytes;
    }

    public byte[] getData(){
        return this.data;
    }
    
}
