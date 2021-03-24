package Client_View;


import Client_Tool.ThreadUpdateOnline;
import Client_Tool.ClientList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DELL
 */
public class PanelControl extends JLayeredPane{
    private JButton btnShowOnlineClient;
    private JPanel pnlOnlineClient;
    private JButton btnHideOnlineClient;
    private JScrollPane scrOnlineClient;
    private ClientList listClient;
    private JPanel pnlClient;
    private JPanel panPeer;
    private ThreadUpdateOnline th;
    
    public PanelControl(Socket mngSocket, ObjectInputStream ois, ObjectOutputStream oos, String sender) {
        listClient = new ClientList();
        initComponent();
        /*System.out.println("h1");
        try {
            ois  = new ObjectInputStream(mngSocket.getInputStream());
            oos = new ObjectOutputStream(mngSocket.getOutputStream());
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        System.out.println("h2");*/
        th = new ThreadUpdateOnline(mngSocket, listClient, ois, oos, pnlClient, sender, panPeer);
        th.start();
    }
    
    
    public void initComponent(){
        this.setPreferredSize(new Dimension(573, 325));
        this.setBorder(new LineBorder(Color.BLACK, 1));
        panPeer = new JPanel();
        panPeer.setBorder(new LineBorder(Color.RED, 1));
        panPeer.setBounds(0, 0, 539, 325);
        this.add(panPeer);
        btnShowOnlineClient = new JButton("<");
        btnShowOnlineClient.setBounds(537, 70, 35, 120);
        this.add(btnShowOnlineClient, new Integer(10));
        btnShowOnlineClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnShowOnlineClientClick();
            }
        });
        
        pnlOnlineClient = new JPanel();
        pnlOnlineClient.setLayout(new FlowLayout());
        pnlOnlineClient.setBackground(Color.WHITE);
        JLabel lblOnline = new JLabel("Current Online Client");
        lblOnline.setHorizontalAlignment(JLabel.CENTER);
        lblOnline.setPreferredSize(new Dimension(185, 50));
        lblOnline.setOpaque(true);
        lblOnline.setBackground(Color.LIGHT_GRAY);
        pnlOnlineClient.add(lblOnline);
        
        pnlClient = new JPanel();
        pnlClient.setPreferredSize(new Dimension(200, 190));
        pnlClient.setBackground(Color.WHITE);
        pnlClient.setLayout(new FlowLayout());
        
        pnlOnlineClient = new JPanel();
        pnlOnlineClient.setLayout(new FlowLayout());
        pnlOnlineClient.setBorder(new LineBorder(Color.BLACK, 1));
        pnlOnlineClient.setBackground(Color.WHITE);
        
        pnlOnlineClient.setBounds(373, 0, 200, 250);
        scrOnlineClient = new JScrollPane(pnlClient);
        scrOnlineClient.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrOnlineClient.setPreferredSize(new Dimension(200, 200));
        scrOnlineClient.setBorder(new LineBorder(Color.BLACK, 1));
        pnlOnlineClient.add(lblOnline);
        pnlOnlineClient.add(scrOnlineClient);
        
        this.add(pnlOnlineClient, new Integer(20));
        pnlOnlineClient.setVisible(false);
        scrOnlineClient.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        
        
        btnHideOnlineClient = new JButton(">");
        btnHideOnlineClient.setBounds(342, 70, 35, 120);
        this.add(btnHideOnlineClient, new Integer(10));
        btnHideOnlineClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnHideOnlineClientClick();
            }
        });
        btnHideOnlineClient.setVisible(false);
    }
    
    private void btnShowOnlineClientClick(){
        pnlOnlineClient.setVisible(true);
        btnHideOnlineClient.setVisible(true);
    }
    private void btnHideOnlineClientClick(){
        pnlOnlineClient.setVisible(false);
        btnHideOnlineClient.setVisible(false);
    }
    
    public static void main(String[] args) {
        
        JFrame f = new JFrame("Control Panel");
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
