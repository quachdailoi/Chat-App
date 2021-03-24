package Client_Tool;


import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JLabel;
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
public class ButtonClient extends JButton{
    public ButtonClient(String clientName){
        super(clientName);
        this.setOpaque(true);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setPreferredSize(new Dimension(180, 25));
        this.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
    }
}
