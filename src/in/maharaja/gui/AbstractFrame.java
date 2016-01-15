package in.maharaja.gui;

import in.maharaja.main.MainApp;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prateek on 29-10-2015.
 * @apiNote Creates a normal JFrame that with addition HashMap so as to be used later.
 */
public abstract class AbstractFrame extends JFrame {
    private Map<String, Object> variableMap;

    AbstractFrame(String title){
        super(title);
        variableMap = new HashMap<>();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2); //Center JFrame WRT Screen
    }

    /**
     * @param name as Any String
     * @param eventListener Some EventListener to be attached to JComponent
     */
    public void registerEvent(String name, EventListener eventListener){
        try {
            if (variableMap.containsKey(name)) {
                Object obj = variableMap.get(name);
                if (obj instanceof JButton || obj instanceof JComboBox) {
                    ((JButton) obj).addActionListener((ActionListener) eventListener);
                } else if (obj instanceof JMenuItem) {
                    ((JMenuItem) obj).addActionListener((ActionListener) eventListener);
                } else if (obj instanceof JList) {
                    ListSelectionModel model = ((JList) obj).getSelectionModel();
                    model.addListSelectionListener((ListSelectionListener) eventListener);
                }
            }
        } catch (ClassCastException e1){
            MainApp.showNotice("ClassCastException", e1.toString());
        }
    }

    public void registerKeyAdapter(String name, KeyAdapter adapter){
        if( variableMap.containsKey(name) ){
            if( variableMap.get(name) instanceof JTextArea){
                ((JTextArea) variableMap.get(name)).addKeyListener( adapter );
            } else if( variableMap.get(name) instanceof JTextField ){
                ((JTextField) variableMap.get(name)).addKeyListener(adapter);
            }
        }
    }

    /**
     * @param name any String
     * @param component any JComponent
     */
    void registerVariable(String name, Object component){
        variableMap.put(name, component);
    }

    /**
     * Retrieves the value from JTextField, Combo Boxes and Text Area.
     * @param name Name of Variable
     * @return String stored in JComponent
     */
    public String getVariable(String name){

        if( variableMap.containsKey(name) ){
            if( variableMap.get(name) instanceof JTextField){
                return ((JTextField) variableMap.get(name)).getText();
            } else if( variableMap.get(name) instanceof JComboBox ){
                return ((JComboBox) variableMap.get(name)).getSelectedItem().toString();
            } else if( variableMap.get(name) instanceof JTextArea ){
                return ((JTextArea) variableMap.get(name)).getText();
            } else if( variableMap.get(name) instanceof String )
                return (String) variableMap.get(name);
        }
        return "Variable Not Registered";
    }

    public Object getComponent(String name){
        if( variableMap.containsKey(name) ) return variableMap.get(name);
        return null;
    }

    public void setVariable(String name, String value){
        if( variableMap.containsKey(name) ){
            if( variableMap.get(name) instanceof JLabel )
                ((JLabel) variableMap.get(name)).setText(value);
            else if( variableMap.get(name) instanceof JTextField )
                ((JTextField) variableMap.get(name)).setText(value);
            else if( variableMap.get(name) instanceof JTextArea )
                ((JTextArea) variableMap.get(name)).setText(value);
        }
    }

    public Boolean contains(String name){
        return variableMap.containsKey(name);
    }

    public abstract void createGUI();
    public abstract void showGUI();

    public void showNotice(String caption, String message){
        MainApp.showNotice(caption, message);
    }

    public void showError(String caption, String message){
        MainApp.showNotice(caption, message, TrayIcon.MessageType.ERROR);
    }

    public void showWarning(String caption, String message){
        MainApp.showNotice(caption, message, TrayIcon.MessageType.WARNING);
    }

}
