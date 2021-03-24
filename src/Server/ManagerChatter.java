package Server;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Client_Tool.ClientControler;
import Client_Tool.ClientList;
import Client_Tool.ClientControlList;
import Client_Tool.Client;
import Client_Tool.Client;
import Client_Tool.ClientControlList;
import Client_Tool.ClientControler;
import Client_Tool.ClientList;
import Common.Message;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author DELL
 */
public class ManagerChatter extends JFrame implements Runnable{
    private JLabel lblPort;
    private JPanel pnlInfo;
    private JTextField txtServerPort;
    
    ServerSocket srvSocket = null;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    Thread t; // thread for exploring connections from staffs

    GridBagConstraints gbc = new GridBagConstraints();
    ClientList listClient;
    ClientControlList listControl;
    LinkedList<Message> queueMess;
    
    public ManagerChatter() {
        initComponents();
        listClient = new ClientList();
        listControl = new ClientControlList();
        queueMess = new LinkedList<>();
        int serverPort = Integer.parseInt(txtServerPort.getText());
        try {
            srvSocket = new ServerSocket(serverPort);
        } catch (Exception e) {
        }
        t = new Thread(this);
        t.start();
        Thread thD = new ThreadDistributeMessage(listControl, queueMess);
        thD.start();
    }

    @Override
    public void run() {
        while(true){
            try { // wait for client
                Socket aStaffSocket = srvSocket.accept();
                if(aStaffSocket != null){ //if there is a connection
                    //get staff name
                    //when a staff inits a connection, he/she send his/her name first
                    oos = new ObjectOutputStream(aStaffSocket.getOutputStream());
                    ois = new ObjectInputStream(aStaffSocket.getInputStream());
                    
                    Client newClient = (Client)ois.readObject();
                    ClientControler newControler = new ClientControler(newClient, ois, oos);
       
                    listClient.add(newClient);
                    listControl.add(newControler);
                    Thread receiveTh = new ThreadReceiveMess(queueMess, newControler);
                    receiveTh.start();
                    updateOnlineClient();
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public synchronized void updateOnlineClient() throws IOException{
        for (ClientControler clientControler : listControl) {
            (clientControler.getOos()).reset();
            (clientControler.getOos()).writeObject(listClient);
            (clientControler.getOos()).flush();
        }
    }
    
    public void initComponents(){
        this.setTitle("Boss");
        txtServerPort = new JTextField("12340");
        txtServerPort.setEditable(false);
        pnlInfo = new JPanel();
        this.setLayout(new FlowLayout(3));
        pnlInfo.setLayout(new GridBagLayout());
        pnlInfo.setPreferredSize(new Dimension(573, 100));
        pnlInfo.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1), "Manager Info", 1, 2, new Font("Arial", 1, 12), Color.BLUE));
        this.add(pnlInfo);
        
        lblPort = new JLabel("Mng.Server running at port: ");
        gbc.gridx = 0; gbc.gridy = 0;
        pnlInfo.add(lblPort, gbc);
        txtServerPort.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 1;
        pnlInfo.add(txtServerPort, gbc);
        
    }
    
    public static void main(String[] args) {
        try{
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
                if("Nimbus".equals(info.getName())){
                     UIManager.setLookAndFeel(info.getClassName());
                     break;
                }
                
            }
           
        }catch(Exception e){   
        }
        ManagerChatter mc = new ManagerChatter();
        mc.setSize(600, 500);
        mc.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mc.setLocationRelativeTo(null);
        mc.setVisible(true);
    }
}
