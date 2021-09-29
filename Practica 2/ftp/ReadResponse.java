package ftp;

public class ReadResponse {
    
    private byte[] data;
    private int bytesEffectivelyRead;

    public ReadResponse(byte[]data, int bytesEffectivelyRead){
        this.data = data;
        this.bytesEffectivelyRead = bytesEffectivelyRead;
    }

    public byte[] getData(){
        return this.data;
    }

    public int getBytesEffectivelyRead(){
        return this.bytesEffectivelyRead;
    }
    
}
