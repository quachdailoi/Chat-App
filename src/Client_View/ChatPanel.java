package Client_View;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Common.FileInfo;
import Common.Message;
import Common.MyIcon;
import Common.IconList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
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

/**
 *
 * @author DELL
 */
public class ChatPanel extends JPanel {

    private JTextPane chatContent;
    private JButton btnSend;
    private JButton btnIcon;
    private JButton btnFile;
    private JScrollPane scrPanChatContent;
    private JScrollPane scrPanMsg;
    private JTextPane txtMessage;
    private JLayeredPane layedPan;
    private JPanel panI;
    private JInternalFrame frIcon = new JInternalFrame("iCon", false, false, false, false);
    //private JInternalFrame frFile = new JInternalFrame("File Chooser", false, false, false, false);
    private JFileChooser fileChos = new JFileChooser(new File(System.getProperty("user.home")));
    private boolean showIcon;
    private boolean showFile;
    private JPanel panelButton;
    private JPanel panelMsg;
    private MyIcon curIcon;
    private JLabel lblReceiver;

    Socket socket = null;
    //Stream stream;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    String sender;
    String receiver;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public ChatPanel(Socket s, String sender, String receiver, ObjectInputStream ois, ObjectOutputStream oos) throws PropertyVetoException {
        socket = s;
        this.sender = sender;
        this.receiver = receiver;
        System.out.println("flag1");
        System.out.println("flag2");
        this.ois = ois;
        this.oos = oos;
        System.out.println("flag3");
        System.out.println("flag4");
        initComponents();
    }

    public void stopThread() {
        //ot.stop();
    }

    public JTextPane getChatContent() {
        return chatContent;
    }

    public void setChatContent(String content) {
        chatContent.setText(chatContent.getText().concat(content));
    }

    public ChatPanel() throws PropertyVetoException {
        initComponents();
    }

    public void updateLblClient() {
        lblReceiver.updateUI();
    }

    public void initComponents() throws PropertyVetoException {
        lblReceiver = new JLabel(receiver);
        lblReceiver.setOpaque(true);
        lblReceiver.setBackground(Color.LIGHT_GRAY);
        lblReceiver.setFont(new Font("Arial", 1, 10));
        lblReceiver.setBounds(403, 0, 100, 25);
        lblReceiver.setHorizontalAlignment(JLabel.CENTER);
        showIcon = false;
        scrPanChatContent = new JScrollPane();
        scrPanMsg = new JScrollPane();

        layedPan = new JLayeredPane();
        layedPan.setBorder(new LineBorder(Color.BLUE, 1));
        layedPan.setPreferredSize(new Dimension(510, 300));
        this.add(layedPan);
        layedPan.add(lblReceiver, new Integer(10));

        chatContent = new JTextPane();
        chatContent.setPreferredSize(new Dimension(550, 220));
        chatContent.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        chatContent.setEditable(false);

        scrPanChatContent.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrPanChatContent.setPreferredSize(new Dimension(550, 220));
        scrPanChatContent.setViewportView(chatContent);
        scrPanChatContent.setBounds(5, 0, 500, 220);
        layedPan.add(scrPanChatContent);

        panelMsg = new JPanel(new FlowLayout(3));
        panelMsg.setPreferredSize(new Dimension(355, 90));
        //panelMsg.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY, 1), "Message", 1, 2, new Font("Arial", 1, 10), Color.BLUE));
        panelMsg.setBounds(1, 220, 360, 80);
        panelMsg.setBorder(new LineBorder(Color.BLACK, 1));

        panelButton = new JPanel(new FlowLayout(3));
        panelButton.setPreferredSize(new Dimension(125, 90));
        panelButton.setBounds(355, 229, 150, 60);

        txtMessage = new JTextPane();
        txtMessage.setPreferredSize(new Dimension(350, 60));
        txtMessage.setEditorKit(new WrapEditorKit());
        //txtMessage.setEditorKit(new WrapEditorKit());
        scrPanMsg.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrPanMsg.setPreferredSize(new Dimension(340, 70));
        scrPanMsg.setViewportView(txtMessage);
        scrPanMsg.setBorder(new LineBorder(Color.RED, 1));
        panelMsg.add(scrPanMsg);
        layedPan.add(panelMsg, JLayeredPane.DEFAULT_LAYER);
        layedPan.add(panelButton, JLayeredPane.DEFAULT_LAYER);

        ImageIcon iiSend = new ImageIcon("icon\\iconSend.png");
        ImageIcon iiFile = new ImageIcon("icon\\iconFile.png");
        ImageIcon iiEmoji = new ImageIcon("icon\\icon3.png");
        btnIcon = new JButton(iiEmoji);
        btnFile = new JButton(iiFile);
        btnSend = new JButton(iiSend);

        btnIcon.setPreferredSize(new Dimension(45, 45));
        btnFile.setPreferredSize(new Dimension(45, 45));
        btnSend.setPreferredSize(new Dimension(45, 45));

        panelButton.add(btnIcon);
        panelButton.add(btnFile);
        panelButton.add(btnSend);

        panI = new JPanel();
        panI.setBounds(325, 25, 200, 200);
        frIcon.setIcon(true);
        frIcon.setBackground(Color.lightGray);
        frIcon.setFrameIcon(new ImageIcon("icon\\icon3.png"));
        frIcon.setBounds(325, 25, 200, 200);
        frIcon.setSize(200, 200);
        frIcon.setOpaque(true);
        frIcon.setBorder(new LineBorder(Color.BLACK, 1));
        panI.setBackground(Color.LIGHT_GRAY);
        panI.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1), "iCon", 2, 2, new Font("Arial", 1, 12), Color.RED));
        layedPan.add(frIcon, new Integer(10));
        frIcon.setVisible(false);

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSendClick();
            }
        });

        btnIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnIconClick();
            }
        });

        btnFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnFileClick();
            }
        });
        //*******ICON***********
        frIcon.setLayout(new FlowLayout(1));
        IconList listIcon = new IconList();
        MyIcon icon1 = new MyIcon("icon\\icon1.png");
        MyIcon icon2 = new MyIcon("icon\\icon2.png");
        MyIcon icon3 = new MyIcon("icon\\icon3.png");
        MyIcon icon4 = new MyIcon("icon\\icon4.png");
        MyIcon icon5 = new MyIcon("icon\\icon5.png");
        MyIcon icon6 = new MyIcon("icon\\icon6.png");
        MyIcon icon7 = new MyIcon("icon\\icon7.png");
        MyIcon icon8 = new MyIcon("icon\\icon8.png");
        MyIcon icon9 = new MyIcon("icon\\icon9.png");
        MyIcon icon10 = new MyIcon("icon\\icon10.png");
        MyIcon icon11 = new MyIcon("icon\\icon11.png");
        MyIcon icon12 = new MyIcon("icon\\icon12.png");
        MyIcon icon13 = new MyIcon("icon\\icon13.png");
        MyIcon icon14 = new MyIcon("icon\\icon14.png");
        MyIcon icon15 = new MyIcon("icon\\icon15.png");
        MyIcon icon16 = new MyIcon("icon\\icon16.png");
        MyIcon icon17 = new MyIcon("icon\\icon17.png");
        MyIcon icon18 = new MyIcon("icon\\icon18.png");
        MyIcon icon19 = new MyIcon("icon\\icon19.png");
        MyIcon icon20 = new MyIcon("icon\\icon20.png");
        MyIcon icon21 = new MyIcon("icon\\icon21.png");
        MyIcon icon22 = new MyIcon("icon\\icon22.png");
        MyIcon icon23 = new MyIcon("icon\\icon23.png");
        MyIcon icon24 = new MyIcon("icon\\icon24.png");
        MyIcon icon25 = new MyIcon("icon\\icon25.png");
        listIcon.add(icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8, icon9, icon10, icon11, icon12, icon13, icon14, icon15, icon16, icon17, icon18, icon19, icon20, icon21, icon22, icon23, icon24, icon25);
        for (int i = 0; i < listIcon.size(); i++) {
            JButton btnI = new JButton(listIcon.get(i).getImgIcon());
            btnI.setBorder(new EmptyBorder(1, 1, 1, 1));
            btnI.setBackground(Color.LIGHT_GRAY);
            btnI.setPreferredSize(new Dimension(30, 30));
            btnI.setActionCommand(String.valueOf(i));
            btnI.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    txtMessage.setSelectionStart(txtMessage.getDocument().getLength());
                    curIcon = listIcon.get(Integer.parseInt(btnI.getActionCommand()));
                    ImageIcon tmpI = new ImageIcon(curIcon.getPath());
                    txtMessage.insertIcon(tmpI);
                    txtMessage.updateUI();
                }
            });
            frIcon.add(btnI);
        }
        
       frIcon.addFocusListener(new FocusListener() {
            private boolean gained = false;
            @Override
            public void focusGained(FocusEvent e) {
                gained = true;
            }   
            @Override
            public void focusLost(FocusEvent e) {
                JOptionPane.showMessageDialog(chatContent, "aaaa");
                if(gained){
                    btnIcon.doClick();
                }
            }
        });
    }

    public void closeStream(BufferedOutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void closeStream(BufferedInputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void btnSendClick() {
        if (txtMessage.getText().length() == 0) {
            return;
        }
        try {
            System.out.println("mess from " + sender + " to " + receiver);
            Message msg = new Message(sender, receiver, txtMessage.getDocument());
            System.out.println("m1ss from " + sender + " to " + receiver);
            JTextPane msgPan = new JTextPane();
            msgPan.setEditorKit(new WrapEditorKit());
            msgPan.setEditable(false);
            msgPan.setStyledDocument((StyledDocument) msg.getMsg());
            
            if(!msg.getWhoReceive().equals(msg.getWhoSend())){
                oos.writeObject(msg);
                oos.flush();
            }
            
            chatContent.setSelectionStart(chatContent.getDocument().getLength());
            chatContent.insertComponent(msgPan);
            chatContent.setSelectionStart(chatContent.getDocument().getLength());
            chatContent.getDocument().insertString(chatContent.getDocument().getLength(), "\n", null);
            chatContent.updateUI();
            panelMsg.remove(txtMessage);
            panelMsg.updateUI();
            txtMessage = new JTextPane();
            txtMessage.setEditorKit(new WrapEditorKit());
            txtMessage.setPreferredSize(new Dimension(350, 100));
            txtMessage.setText("");
            scrPanMsg.setViewportView(txtMessage);

        } catch (Exception e) {
        }
    }

    public void btnIconClick() {
        if (showIcon) {
            frIcon.setVisible(false);
            showIcon = false;
        } else {
            frIcon.setFocusable(true);
            frIcon.setVisible(true);
            showIcon = true;
        }
    }

    public void btnFileClick() {
        if (showFile) {
            showFile = false;
        } else {
            showFile = true;
            int result = fileChos.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    File selectedFile = fileChos.getSelectedFile();
                    JTextPane tmp = new JTextPane();
                    //tmp.setPreferredSize(new Dimension(550, 25));
                    tmp.setEditable(false);
                    tmp.setToolTipText("Click to download this file");
                    tmp.setSelectionStart(0);
                    ImageIcon tmpI = new ImageIcon("icon\\file_icon.png");
                    
                    Message msg = new Message(sender, receiver, tmp.getDocument(), selectedFile);
                    tmp.setSelectionStart(tmp.getDocument().getLength());
                    tmp.insertIcon(tmpI);
                    tmp.getDocument().insertString(tmp.getDocument().getLength(), " " + selectedFile.getName(), null);
                    
                    if(!msg.getWhoReceive().equals(msg.getWhoSend())){
                        oos.writeObject(msg);
                        oos.flush();
                    }
                    chatContent.setSelectionStart(chatContent.getDocument().getLength());
                    chatContent.insertComponent(tmp);
                    chatContent.getDocument().insertString(chatContent.getDocument().getLength(), "\n", null);
                    tmp.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            clickToDownFile(msg.getFileInfo());
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        //fileChos.setSelectedFile(null);
    }

    public void clickToDownFile(FileInfo fileInfo) {
        int conf = JOptionPane.showConfirmDialog(chatContent, "Download this file?", "File Download?", JOptionPane.OK_CANCEL_OPTION);
        if(conf == JOptionPane.OK_OPTION){
            createFile(fileInfo);
            JOptionPane.showMessageDialog(chatContent, "Downloaded!!!");
        }
    }
    
    public boolean createFile(FileInfo fileInfo) {
        BufferedOutputStream bos = null;

        try {
            if (fileInfo != null) {
                File fileReceive = new File(fileInfo.getDestinationDirectory()
                        + fileInfo.getFilename());
                bos = new BufferedOutputStream(
                        new FileOutputStream(fileReceive));
                // write file content
                bos.write(fileInfo.getDataBytes());
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    public static void main(String[] args) throws PropertyVetoException {
        ChatPanel p = new ChatPanel();
        JFrame f = new JFrame();
        f.setSize(500, 380);
        f.add(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    class WrapEditorKit extends StyledEditorKit {

        ViewFactory defaultFactory = new WrapColumnFactory();

        public ViewFactory getViewFactory() {
            return defaultFactory;
        }

    }

    class WrapColumnFactory implements ViewFactory {

        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new WrapLabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            // default to text display
            return new LabelView(elem);
        }
    }

    class WrapLabelView extends LabelView {

        public WrapLabelView(Element elem) {
            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }

    }
}
