package in.maharaja.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Main UI that stays infront
 */
public class MainUI extends AbstractFrame {
    private JTextField amountField;
    private JComboBox<String> typeField;
    private JButton submitButton;
    private JTextArea status;
    private JPanel contentPane;
    public static final String[] accountList = {"Photocopy", "BW Print", "Scanning", "Email", "Color Print", "Internet", "MPOnline", "Online Payment", "Lamination", "Aadhaar Print", "PVC Print"};
    private String clientName;
    private int index;


    public MainUI() {
        super("The Counter");
        setSize(400, 60);
        setResizable(false);

        status = new JTextArea();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(80, 27));
        typeField = new JComboBox<>(accountList);
        submitButton = new JButton("Submit");

        contentPane = new JPanel();
        add(contentPane);
        setContentPane(contentPane);
        getContentPane().setLayout(new GridLayout(2, 1, 1, 1));

        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
            clientName = address.getHostName();
        } catch (UnknownHostException e) {
            showError("Unknown Host", e.toString());
            clientName = "Unknown";
        }

        registerVariable("clientName", clientName);

        createMenu();

        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch(Exception ex){
            System.out.println( ex.toString() );
        }
    }

    public void createGUI() {

        setLayout( new MigLayout() );

        add(new JLabel("Amount : "));
        add(amountField);
        add(typeField);
        add(submitButton);

        registerVariable("Amount", amountField);
        registerVariable("Product", typeField );
        registerVariable("Main Submit", submitButton);

        this.getRootPane().setDefaultButton( submitButton );
    }


    private void createMenu(){
        JMenuBar mainMenu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu report = new JMenu("Report");
        JMenu task = new JMenu("Tasks");
        JMenu help = new JMenu("Help");
        JMenu misc = new JMenu("Misc");

        mainMenu.add(file);
        mainMenu.add(report);
        mainMenu.add(task);
        mainMenu.add(misc);
        mainMenu.add(help);

        addMenuItem(file, "Sync", "res/sync.png", KeyEvent.VK_S);
        addMenuItem(file, "Working Dir", "res/dir.png", KeyEvent.VK_W);
        addMenuItem(file, "Exit", "res/exit.png", KeyEvent.VK_X);
        addMenuItem(report, "Daily Report", "res/calendar.png", KeyEvent.VK_D);
        addMenuItem(report, "Inventory Report", "res/box.png", KeyEvent.VK_I);
        addMenuItem(report, "Charts", "res/chart.png", KeyEvent.VK_C);
        addMenuItem(task, "Get Product", "res/products.png", KeyEvent.VK_G);
        addMenuItem(task, "Get Last Entries", "res/read.png", KeyEvent.VK_E);
        addMenuItem(misc, "Transfer", "res/transfer.png", KeyEvent.VK_T);
        addMenuItem(misc, "Products", "res/box.png", KeyEvent.VK_P);
        addMenuItem(help, "About", "res/about.png", KeyEvent.VK_A);

        setJMenuBar(mainMenu);

    }

    private void addMenuItem(JMenu parent, String name, String iconPath, int keycode){
        JMenuItem jMenuItem = new JMenuItem(name, new ImageIcon(iconPath));
        parent.add(jMenuItem);
        setShortcut(jMenuItem, keycode);
        registerVariable(name, jMenuItem);
    }

    private void setShortcut(JMenuItem menuItem, int keycode){
        menuItem.setMnemonic( keycode );
        menuItem.setAccelerator(KeyStroke.getKeyStroke(keycode, InputEvent.ALT_MASK));
    }

    public boolean isEmpty() {
        return  amountField.getText().compareTo("") == 0;
    }

    public void populateComboBox( String[] data){
        typeField.removeAllItems();
        for(String item : data){
            typeField.addItem(item);
        }
    }

    public void reset() {
        amountField.setText("");
        amountField.requestFocus();
    }

}
