package Common;




import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DELL
 */
public class IconList extends Vector<MyIcon>{
    
    public void add(MyIcon... icon){
        for (MyIcon myIcon : icon) {
            this.add(myIcon);
        }
    }
}
