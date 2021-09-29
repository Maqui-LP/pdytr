package ftp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements FtpServerInterface {

    public Server() throws RemoteException{

    }

    @Override
    public ReadResponse read(ReadRequest request) throws RemoteException {
       /**
        * Implementación
        */
        return null;
    }

    @Override
    public int write(WriteRequest request) throws RemoteException {
       /**
        * Implementación
        */
        return 0;
    }

}