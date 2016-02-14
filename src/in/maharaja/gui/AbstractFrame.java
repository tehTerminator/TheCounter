package in.maharaja.gui;

import in.maharaja.main.MainApp;

import javax.swing.*;
import java.awt.*;
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
    }

    /**
     * @param name any String
     * @param component any JComponent
     */
    public final void registerVariable(String name, Object component){
        variableMap.put(name, component);
    }

    /**
     * Retrieves the value from JTextField, Combo Boxes and Text Area.
     * @param name Name of Variable
     * @return String stored in JComponent
     */
    public final String getVariable(String name){
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

    public final void setVariable(String name, String value) {
        if (variableMap.containsKey(name)) {
            if (variableMap.get(name) instanceof JLabel)
                ((JLabel) variableMap.get(name)).setText(value);
            else if (variableMap.get(name) instanceof JTextField)
                ((JTextField) variableMap.get(name)).setText(value);
            else if (variableMap.get(name) instanceof JTextArea)
                ((JTextArea) variableMap.get(name)).setText(value);
        }
    }

    public final Object getObject(String name){
        if( variableMap.containsKey(name) ) return variableMap.get(name);
        else return new Object();
    }

    public Boolean contains(String name){
        return variableMap.containsKey(name);
    }

    public abstract void createGUI();
    public final void showGUI(){
        pack();
        setLocationRelativeTo( null );
        setVisible( true );
    };

    public final void showNotice(String caption, String message){
        MainApp.showNotice(caption, message);
    }

    public final void showError(String caption, String message){
        MainApp.showNotice(caption, message, TrayIcon.MessageType.ERROR);
    }

    public final void showWarning(String caption, String message){
        MainApp.showNotice(caption, message, TrayIcon.MessageType.WARNING);
    }
}
