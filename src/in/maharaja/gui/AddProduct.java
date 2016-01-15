package in.maharaja.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * AddProduct Form, used for Updating and Adding New Products
 */
public class AddProduct extends AbstractFrame {

    private int type;
    private static final int ADD = 0;
    private static final int EDIT = 1;

    /**
     *
     * @param type 0 for Adding New data, 1 for Editing Old Data
     */
    public AddProduct(int type){
        super("Add New Product");
        this.type = type;
        setSize(new Dimension(500, 300) );
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
    }

    /**
     * Creates Default Form Elements
     */
    public void createGUI(){

        setLayout( new MigLayout("wrap 2") );

        if( this.type == AddProduct.EDIT ){
            add( new JLabel("Product ID") );
            JTextField pid = new JTextField(10);
            registerVariable("Product Id", pid);
        }

        add(new JLabel("Name"));

        JTextField nameField = new JTextField(30);
        add(nameField);
        add(new JLabel("Rate"));

        JTextField rateField = new JTextField(30);
        add(rateField);
        add(new JLabel("Type"));

        add(new JLabel("Initial Quantity") );
        JTextField initialQty = new JTextField(30);
        add( initialQty );

        add(new JLabel("Description") );
        JTextField desc = new JTextField(30);
        add(desc);

        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Limited", "Unlimited"});
        add(typeComboBox);

        JButton submit = new JButton("Submit");
        add(submit, "span");

        registerVariable("Product Name", nameField);
        registerVariable("Rate", rateField);
        registerVariable("Type", typeComboBox);
        registerVariable("Quantity", initialQty);
        registerVariable("Description", desc);
        registerVariable("Submit", submit);

        getRootPane().setDefaultButton(submit);
    }


    /**
     * Sets Visibility to true
     */
    public void showGUI(){
        setVisible( true );
    }
}
