package Server;


import Client_Tool.ClientControler;
import Client_Tool.ClientControlList;
import Client_Tool.ClientControlList;
import Client_Tool.ClientControler;
import Common.Message;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DELL
 */
public class ThreadDistributeMessage extends Thread {

    ClientControlList listControl;
    LinkedList<Message> queueMess;

    public ThreadDistributeMessage(ClientControlList listControl, LinkedList<Message> queueMess) {
        this.listControl = listControl;
        this.queueMess = queueMess;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (queueMess != null && !queueMess.isEmpty()) {
                    System.out.println("flaggg");
                    if (listControl != null && !listControl.isEmpty()) {
                        System.out.println("size before dequeuing of queue mess: " + queueMess.size());
                        System.out.println("---find a mess");
                        Message msg = dequeue();
                        String receiver = msg.getWhoReceive();
                        System.out.println("--->receiver: " + receiver);
                        for (ClientControler client : listControl) {
                            System.out.println(client.getClient().getClientName());
                            if (((client.getClient()).getClientName()).equals(receiver)) {
                                System.out.println("find one");
                                System.out.println("---" + client.getClient().getClientName());
                                (client.getOos()).writeObject(msg);
                            }
                        }
                        System.out.println("flagggg");
                    }
                }
                this.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized Message dequeue() {
        return queueMess.removeFirst();
    }

}
