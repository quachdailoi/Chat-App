package Client_Tool;


import Client_Tool.Client;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DELL
 */
public class ClientControler {
    Client client;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public ClientControler(Client client, ObjectInputStream ois, ObjectOutputStream oos) {
        this.client = client;
        this.ois = ois;
        this.oos = oos;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }
    
    
}
