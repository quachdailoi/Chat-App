package Client_View;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Client_Tool.Client;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author DELL
 */
public class ClientChatter extends JFrame {

    private JButton btnConnect;
    private JLabel lblStaff;
    private JLabel lblIP;
    private JLabel lblPort;
    private JPanel pnlInformation;
    private JLayeredPane pnlChat;
    private JTextField txtStaff;
    private JTextField txtServerIP;
    private JTextField txtServerPort;
    private GridBagConstraints gbc = new GridBagConstraints();

    Socket mngSocket = null;
    String mngIP = "";
    int mngPort = 0;
    String staffName = "";
    ObjectInputStream ois;
    ObjectOutputStream oos;
    PanelControl panControl;
    
    public ClientChatter() {
        initComponents();
    }

    public void initComponents() {
        this.setTitle("Staff");
        this.setLayout(new FlowLayout(3));
        pnlInformation = new JPanel(new GridBagLayout());
        pnlInformation.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1), "Staff And Server Info", 1, 2, new Font("Arial", 1, 12), Color.BLUE));
        pnlInformation.setPreferredSize(new Dimension(573, 100));
        this.add(pnlInformation);

        lblStaff = new JLabel("Staff:  ");
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlInformation.add(lblStaff, gbc);
        txtStaff = new JTextField();
        txtStaff.setPreferredSize(new Dimension(60, 30));
        gbc.gridx = 1;
        pnlInformation.add(txtStaff, gbc);

        lblIP = new JLabel("    Mng IP:   ");
        gbc.gridx = 2;
        pnlInformation.add(lblIP, gbc);
        txtServerIP = new JTextField();
        txtServerIP.setPreferredSize(new Dimension(90, 30));
        gbc.gridx = 3;
        pnlInformation.add(txtServerIP, gbc);

        lblPort = new JLabel("   Mng Port:  ");
        gbc.gridx = 4;
        pnlInformation.add(lblPort, gbc);
        txtServerPort = new JTextField();
        txtServerPort.setPreferredSize(new Dimension(90, 30));
        gbc.gridx = 5;
        pnlInformation.add(txtServerPort, gbc);

        btnConnect = new JButton("Connect");
        gbc.gridx = 6;
        pnlInformation.add(btnConnect, gbc);
        
        

        pnlChat = new JLayeredPane();
        pnlChat.setPreferredSize(new Dimension(573, 325));
        pnlChat.setBorder(new LineBorder(Color.BLACK, 1));
        
        
        this.add(pnlChat);
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnConnectClick();
            }
        });
        
        
        txtStaff.setText("Hoa");
        txtServerIP.setText("localhost");
        txtServerPort.setText("12340");
    }
    

    private void btnConnectClick() {
        mngIP = this.txtServerIP.getText();
        if(this.txtServerPort.getText().matches("\\d+")){
            mngPort = Integer.parseInt(this.txtServerPort.getText());
            staffName = this.txtStaff.getText();
            try {
                mngSocket = new Socket(mngIP, mngPort);
                if (mngSocket != null) {
                    
                    ois = new ObjectInputStream(mngSocket.getInputStream());
                    oos = new ObjectOutputStream(mngSocket.getOutputStream());
                    panControl = new PanelControl(mngSocket, ois, oos, staffName);
                    panControl.setBounds(0, 0, 573, 325);
                    pnlChat.add(panControl);
                    JTextPane tmp = new JTextPane();
                    tmp.setText("Manager is running...\n");
                    //Message msg = new Message(tmp.getDocument(), staffName);
                    //pan.add(msg);
                    //get the socket input stream and ouput stream
                    //announce to manager:
                    Client client = new Client(staffName);
                    oos.writeObject(client);
                    
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Manager is not working!");
                e.printStackTrace();
                System.exit(0);
            }
            btnConnect.setEnabled(false);
        }
        else JOptionPane.showMessageDialog(btnConnect, "Invalid Port!");
        
    }
    

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }

            }

        } catch (Exception e) {

        }
        ClientChatter fr = new ClientChatter();
        fr.setSize(600, 475);
        fr.setLocationRelativeTo(null);
        fr.setDefaultCloseOperation(EXIT_ON_CLOSE);
        fr.setVisible(true);
    }
}
