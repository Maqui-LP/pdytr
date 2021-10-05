import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FtpServerInterface extends Remote {
    
    /**
     * El parametro recibido indica nombre del archivo a leer,
     * ubicacion del mismo dentro del servidor y cantidad de
     * bytes a ser leidos.
     * @param ReadRequest
     * @return ReadResponse
     * @throws RemoteException
     */
    public ReadResponse read(ReadRequest request) throws Exception;

    /**
     * El parametro recibido indica nombre del archivo a crear,
     * cantidad de bytes determinada y un buffer del cual extraer
     * los datos a escribir en el servidor.
     * Si el archivo existe los datos se escrbien al final del mismo,
     * si no existe se crea y se almacenan alli los datos.
     * @param WriteRequest
     * @return int
     * @throws RemoteException
     */
    public int write(WriteRequest request) throws Exception;

    public boolean giveMeABoolean() throws Exception;

    public void timeout()throws Exception;

}
