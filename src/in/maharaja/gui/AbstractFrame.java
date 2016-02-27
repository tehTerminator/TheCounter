package in.maharaja.gui;

import in.maharaja.main.MainApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Prateek on 29-10-2015.
 * @apiNote Creates a normal JFrame that with addition HashMap so as to be used later.
 */
public abstract class AbstractFrame extends JFrame {
    private Map<String, Object> variableMap;
    private Set<Character> registeredKeyEvents;

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

    /**
     * @param name  Object to retrieve
     *              Object must be JLabel || JTextField || JTextArea
     * @param value Value that is to be saved in Object
     */
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

    /**
     *
     * @param name is String Key in VariableMap
     * @return Object that is stored in VariableMap
     *          Returns new Object() if Object Not Available
     */
    public final Object getObject(String name){
        if( variableMap.containsKey(name) ) return variableMap.get(name);
        else return new Object();
    }

    /**
     *
     * @param name name of Object that is to be searched
     * @return True or False
     */
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

    public final void showError(String caption, String message) {
        MainApp.showNotice(caption, message, TrayIcon.MessageType.ERROR);
    }

    public final void showWarning(String caption, String message) {
        MainApp.showNotice(caption, message, TrayIcon.MessageType.WARNING);
    }

    /**
     * Creates a Menubar if Not present in Frame
     * Menubar can be accessed using getObject("Main Menu") function;
     */
    public final void createMenu() {
        JMenuBar menuBar = getJMenuBar();
        if (menuBar == null) {
            menuBar = new JMenuBar();
            registerVariable("Main Menu", menuBar);
            registeredKeyEvents = new HashSet<>();
            setJMenuBar(menuBar);
        }
    }

    /**
     *
     * @param name is String name of Menu Item
     * @param parent is String name of Parent Item
     * @param keycode is KeyEvent or Shortcut Key
     */
    public final void registerMenuItem(String name, Object parent, int keycode) {
        registerMenuItem(name, parent);
        setMenuShortcut(name, keycode);
    }

    /**
     * @param name   is String name of Menu Item can be JMenu or JMenuItem
     * @param parent is String name of Parent Item can be JMenuBar or JMenu
     */
    public final void registerMenuItem(String name, Object parent) {
        assert parent instanceof JMenu || parent instanceof JMenuBar;

        if (parent instanceof JMenuBar) {
            JMenu menu = new JMenu(name);
            ((JMenuBar) parent).add(menu);
            registerVariable(name, menu);
        } else if (parent instanceof JMenu) {
            JMenuItem item = new JMenuItem(name);
            ((JMenu) parent).add(item);
            registerVariable(name, item);
            setImage(name, "res/" + name + ".png");
        }

        int len = name.length();
        for (int i = 0; i < len; i++) {
            char c;
            c = name.toLowerCase().charAt(i);

            if ((int) c >= 97 && (int) c <= 122) {
                if (registeredKeyEvents.contains(c)) continue;
                else {
                    setMenuShortcut(name, getKeyEvent(c));
                    registeredKeyEvents.add(c);
                    break;
                }
            } else continue;
        }

    }

    /**
     * @param name    is String name of JMenu
     * @param keycode is KeyEvent shortcut key for JMenu
     */
    public final void setMenuShortcut(String name, int keycode) {
        if (contains(name)) {
            assert getObject(name) instanceof JMenu || getObject(name) instanceof JMenuItem;

            if (getObject(name) instanceof JMenu) {
                ((JMenu) getObject(name)).setMnemonic(keycode);
            } else if (getObject(name) instanceof JMenuItem) {
                ((JMenuItem) getObject(name)).setMnemonic(keycode);
                ((JMenuItem) getObject(name)).setAccelerator(KeyStroke.getKeyStroke(keycode, InputEvent.ALT_MASK));
            }
        }
    }

    /**
     * @param name    is String name of JMenu or JMenuItem
     * @param parent  is String name of Parent
     * @param keycode is KeyEvent is Shortcut key
     */
    public final void registerMenuItem(String name, String parent, int keycode) {
        registerMenuItem(name, getObject(parent), keycode);
    }

    /**
     * @param name   is String Name of Menu Item
     * @param parent is String Name of Parent JMenu or JMenuItem
     */
    public final void registerMenuItem(String name, String parent) {
        registerMenuItem(name, getObject(parent));
    }

    /**
     * @param menuPattern is String pattern in which Menu is to be created
     * @throws IllegalArgumentException if pattern is not of form
     *                                  Parent > Child1, Child2, Child3
     */
    public final void createMenuItems(String menuPattern) throws IllegalArgumentException {
        if (menuPattern.indexOf(">") > 0) {
            String[] p1 = menuPattern.split(">");
            if (contains(p1[0].trim())) {
                String[] items = p1[1].split(",");
                for (String item : items) {
                    registerMenuItem(item.trim(), p1[0].trim());
                }
            }
        } else {
            throw new IllegalArgumentException("Please use Format Parent > Child1, Child2, Child3");
        }
    }

    /**
     * @param name  is String name of Menu Item
     * @param image is String name toward path of Image for Menu Item
     *              <p>
     *              This method is automatically called in <br/><b>registerMenuItem(String name, String parent)</b>
     *              and <b>registerMenuItem(String name, Object parent)</b><br/>
     *              image name in res/ folder should be same as name
     */
    public final void setImage(String name, String image) {
        if (contains(name)) {
            if (getObject(name) instanceof JMenu) {
                ((JMenu) getObject(name)).setIcon(new ImageIcon(image));
            } else if (getObject(name) instanceof JMenuItem) {
                ((JMenuItem) getObject(name)).setIcon(new ImageIcon(image));
            }
        }
    }

    /**
     * @param s is Character between a to z
     * @return KeyEvent for specific character to be used as shortcut key.
     */
    private final int getKeyEvent(char s) {
        assert (int) s >= 97 && (int) s <= 122;
        switch (s) {
            case 'a':
                return KeyEvent.VK_A;
            case 'b':
                return KeyEvent.VK_B;
            case 'c':
                return KeyEvent.VK_C;
            case 'd':
                return KeyEvent.VK_D;
            case 'e':
                return KeyEvent.VK_E;
            case 'f':
                return KeyEvent.VK_F;
            case 'g':
                return KeyEvent.VK_G;
            case 'h':
                return KeyEvent.VK_H;
            case 'i':
                return KeyEvent.VK_I;
            case 'j':
                return KeyEvent.VK_J;
            case 'k':
                return KeyEvent.VK_K;
            case 'l':
                return KeyEvent.VK_L;
            case 'm':
                return KeyEvent.VK_M;
            case 'n':
                return KeyEvent.VK_N;
            case 'o':
                return KeyEvent.VK_O;
            case 'p':
                return KeyEvent.VK_P;
            case 'q':
                return KeyEvent.VK_Q;
            case 'r':
                return KeyEvent.VK_R;
            case 's':
                return KeyEvent.VK_S;
            case 't':
                return KeyEvent.VK_T;
            case 'u':
                return KeyEvent.VK_U;
            case 'v':
                return KeyEvent.VK_V;
            case 'w':
                return KeyEvent.VK_W;
            case 'x':
                return KeyEvent.VK_X;
            case 'y':
                return KeyEvent.VK_Y;
            case 'z':
                return KeyEvent.VK_Z;
        }
        return KeyEvent.VK_0; //Will never reach here.
    }
}
