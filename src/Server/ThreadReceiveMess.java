package Server;


import Client_Tool.ClientControler;
import Client_Tool.ClientControler;
import Common.Message;
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DELL
 */
public class ThreadReceiveMess extends Thread {

    ClientControler clientControler;
    LinkedList<Message> queueMess;

    public ThreadReceiveMess(LinkedList<Message> queueMess, ClientControler clientControler) {
        this.queueMess = queueMess;
        this.clientControler = clientControler;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object obj = (clientControler.getOis()).readObject();
                if (obj instanceof Message && ((Message) obj).getMsg().getLength() > 0) {
                    System.out.println("receive a mess");
                    Message msg = (Message) obj;
                    addMess(msg);
                    System.out.println("queue mess size: " + queueMess.size());
                }
                this.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addMess(Message msg) {
        queueMess.addLast(msg);
    }
}
