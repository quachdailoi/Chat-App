package Common;


import java.io.IOException;
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
public class Stream {
    ObjectInputStream ois;
    ObjectOutputStream oos;
    
    public Stream(ObjectInputStream ois, ObjectOutputStream oos){
        this.ois = ois;
        this.oos = oos;
    }
    
    public synchronized Object readObject() throws IOException, ClassNotFoundException{
        return ois.readObject();
    }
    
    public synchronized  void writeObject(Object obj) throws IOException{
        oos.writeObject(obj);
        oos.flush();
    }
}
