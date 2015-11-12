package in.maharaja.gui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
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
     *
     * @param name as Any String
     * @param actionListener Some ActionListener to be attached to JButton or JMenuItem
     */
    public void registerActionEvent(String name, ActionListener actionListener){
        if( variableMap.containsKey(name) ){
            if( variableMap.get(name) instanceof JButton )
                ((JButton) variableMap.get(name)).addActionListener(actionListener);
            else if( variableMap.get(name) instanceof JMenuItem )
                ((JMenuItem) variableMap.get(name)).addActionListener(actionListener);
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
     *
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

}
