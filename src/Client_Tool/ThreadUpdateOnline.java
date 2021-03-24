package Client_Tool;


import Common.Message;
import Common.FileInfo;
import Client_Tool.ButtonClient;
import Client_Tool.ClientList;
import Client_Tool.Client;
import Client_View.ChatPanel;
import Common.Stream;
import com.sun.imageio.plugins.jpeg.JPEG;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
public class ThreadUpdateOnline extends Thread {

    Socket socket;
    ClientList listClient;
    //Stream stream;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Stream stream;
    JPanel pnlClient;
    ArrayList<ChatPanel> listChatPane;
    String sender;
    JPanel panPeer;

    public ThreadUpdateOnline(Socket socket, ClientList listClient, ObjectInputStream ois, ObjectOutputStream oos, JPanel pnlClient, String sender, JPanel panPeer) {
        this.listChatPane = new ArrayList<>();
        this.sender = sender;
        this.panPeer = panPeer;
        this.listClient = listClient;
        this.socket = socket;
        this.pnlClient = pnlClient;
        this.oos = oos;
        this.ois = ois;
        stream = new Stream(ois, oos);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (socket != null) {
                    Object obj = stream.readObject();
                    if (obj instanceof ClientList) {
                        this.listClient = (ClientList) obj;
                        pnlClient.removeAll();
                        for (ChatPanel chatPanel : listChatPane) {
                            chatPanel.stopThread();
                        }
                        listChatPane.clear();
                        System.out.println("client list:" + listClient.size());
                        for (Client client : listClient) {
                            ChatPanel chatPane = new ChatPanel(socket, sender, client.getClientName(), ois, oos);
                            listChatPane.add(chatPane);
                            System.out.println("list chat pan:" + listChatPane.size());
                            System.out.println("REC: " + client.getClientName());
                            ButtonClient btnClient = new ButtonClient(client.getClientName());
                            btnClient.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    panPeer.removeAll();
                                    for (ChatPanel chatPanel : listChatPane) {
                                        if (chatPanel.getReceiver().equals(btnClient.getText())) {
                                            panPeer.add(chatPanel);
                                        }
                                    }
                                    panPeer.updateUI();
                                }
                            });
                            pnlClient.add(btnClient);
                        }
                        pnlClient.updateUI();
                    }
                    else if (obj instanceof Message) {
                        Message msg = (Message) obj;
                        String sender = msg.getWhoSend();
                        for (ChatPanel chatPanel : listChatPane) {
                            JTextPane chatContent = chatPanel.getChatContent();
                            if ((chatPanel.getReceiver()).equals(sender)) {
                                if (msg != null && msg.getMsg().getLength() > 0) {
                                    JTextPane msgPane = new JTextPane();
                                    msgPane.setEditorKit(new WrapEditorKit());
                                    msgPane.setStyledDocument((StyledDocument) msg.getMsg());
                                    // msgPane.setPreferredSize(new Dimension(550, 20));
                                    msgPane.setEditable(false);
                                    if (msg.isIsSendFile()){
                                        
                                        msgPane.addMouseListener(new MouseAdapter() {
                                            @Override
                                            public void mouseClicked(java.awt.event.MouseEvent evt){
                                                clickToDownFile(msg.getFileInfo(), chatContent);
                                            }
                                        });
                                    }
                                    chatContent.setSelectionStart(chatContent.getDocument().getLength());
                                    chatContent.insertComponent(msgPane);
                                    chatContent.getDocument().insertString(chatContent.getDocument().getLength(), "\n", null);
                                    
                                }
                            }
                        }
                    }
                }
                this.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void clickToDownFile(FileInfo fileInfo, JTextPane chatContent) {
        int conf = JOptionPane.showConfirmDialog(chatContent, "Download this file?", "File Download?", JOptionPane.OK_CANCEL_OPTION);
        if(conf == JOptionPane.OK_OPTION){
            createFile(fileInfo);
            JOptionPane.showMessageDialog(chatContent, "Downloaded!!!");
        }
    }
    
    public FileInfo getFileInfo(String sourceFilePath, String destinationDir) {
        FileInfo fileInfo = null;
        BufferedInputStream bis = null;
        try {
            File sourceFile = new File(sourceFilePath);
            bis = new BufferedInputStream(new FileInputStream(sourceFile));
            fileInfo = new FileInfo();
            byte[] fileBytes = new byte[(int) sourceFile.length()];
            // get file info
            bis.read(fileBytes, 0, fileBytes.length);
            fileInfo.setFilename(sourceFile.getName());
            fileInfo.setDataBytes(fileBytes);
            fileInfo.setDestinationDirectory(destinationDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return fileInfo;
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
