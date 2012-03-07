/*
 * ViewportLayoutEx.java
 *
 * Created on 16 јпрель 2007 г., 12:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.io.Serializable;
import javax.swing.JViewport;
import javax.swing.ViewportLayout;

/**
 *
 * @author valery
 */
public class ViewportLayoutEx extends ViewportLayout implements Serializable{
    
    @Override
    public void layoutContainer(Container parent) {
        super.layoutContainer(parent);
        JViewport vp = (JViewport)parent;
	Component view = vp.getView();  
        vp.setViewPosition( new Point(vp.getViewPosition().x, vp.getViewPosition().y) );
    }
}//class
