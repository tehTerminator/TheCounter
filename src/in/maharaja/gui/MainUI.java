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

        createMenuItems("Main Menu > File, Report, Tasks, Misc, Help");
        createMenuItems("File > Sync, Working Dir, Exit");
        createMenuItems("Report > Daily Report, Inventory Report, Charts");
        createMenuItems("Tasks > Get Products, Get Last Entries");
        createMenuItems("Misc > Transfer, Products");
        createMenuItems("Help > About");
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
