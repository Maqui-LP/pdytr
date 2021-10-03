import java.io.Serializable;
public class ReadResponse implements Serializable {
    
    private byte[] data;
    private int bytesEffectivelyRead;
    private boolean isEOF;

    public ReadResponse(byte[]data, int bytesEffectivelyRead, boolean isEOF){
        this.data = data;
        this.bytesEffectivelyRead = bytesEffectivelyRead;
        this.isEOF = isEOF;
    }

    public byte[] getData(){
        return this.data;
    }

    public int getBytesEffectivelyRead(){
        return this.bytesEffectivelyRead;
    }

    public boolean getIsEOF() {
        return this.isEOF;
    }
    
}
