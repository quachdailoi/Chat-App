package Common;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Common.FileInfo;
import java.awt.event.MouseAdapter;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Document;
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
public class Message implements Serializable{
    Document msg;
    String whoSend;
    String whoReceive;
    boolean isSendFile = false;
    public FileInfo fileInfo;
    File file;
    
    public Message(String sender, String receiver, Document msg) throws BadLocationException {
        this.msg = msg;
        this.whoSend = sender;
        this.whoReceive = receiver;
        msg.insertString(0, whoSend+": ", null);
    }

    public Message(String whoSend, String whoReceive, Document msg, File file){
        try {
            this.msg = msg;
            this.whoSend = whoSend;
            this.whoReceive = whoReceive;
            msg.insertString(0, this.whoSend+": ", null);
            this.file = file;
            isSendFile = true;
            fileInfo = getFileInfo(file.getPath(), "staff\\");
        } catch (BadLocationException ex) {
            System.out.println("EEEE");
        }
    }

    
    
    public String getWhoReceive() {
        return whoReceive;
    }

    public void setWhoReceive(String whoReceive) {
        this.whoReceive = whoReceive;
    }
    
    
    
    public Document getMsg() {
        return msg;
    }
    

    public void setMsg(Document msg) {
        this.msg = msg;
    }

    public boolean isIsSendFile() {
        return isSendFile;
    }

    public void setIsSendFile(boolean isSendFile) {
        this.isSendFile = isSendFile;
    }

    public String getWhoSend() {
        return whoSend;
    }

    public void setWhoSend(String whoSend) {
        this.whoSend = whoSend;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
    
    private FileInfo getFileInfo(String sourceFilePath, String destinationDir) {
        FileInfo fileInfoReturn = null;
        BufferedInputStream bis = null;
        try {
            File sourceFile = new File(sourceFilePath);
            bis = new BufferedInputStream(new FileInputStream(sourceFile));
            fileInfoReturn = new FileInfo();
            byte[] fileBytes = new byte[(int) sourceFile.length()];
            // get file info
            bis.read(fileBytes, 0, fileBytes.length);
            fileInfoReturn.setFilename(sourceFile.getName());
            fileInfoReturn.setDataBytes(fileBytes);
            fileInfoReturn.setDestinationDirectory(destinationDir);
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
        return fileInfoReturn;
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
