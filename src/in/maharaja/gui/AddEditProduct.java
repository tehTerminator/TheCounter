package in.maharaja.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * AddEditProduct Form, used for Updating and Adding New Products
 */
public class AddEditProduct extends AbstractFrame {

    private int type;
    public static final int ADD = 0;
    public static final int EDIT = 1;

    /**
     *
     * @param type 0 for Adding New data, 1 for Editing Old Data
     */
    public AddEditProduct(int type, String title){
        super( title );
        this.type = type;
        setSize(new Dimension(350, 300) );
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
    }

    /**
     * Creates Default Form Elements
     */
    public void createGUI(){

        setLayout( new MigLayout("wrap 2") );

        if( this.type == AddEditProduct.EDIT ){
            add( new JLabel("Product ID") );
            JTextField pid = new JTextField(10);
            add( pid );
            registerVariable("Product Id", pid);
            pid.setEnabled( false );
        }

        add(new JLabel("Name"));

        JTextField nameField = new JTextField(30);
        add(nameField);
        add(new JLabel("Rate"));

        JTextField rateField = new JTextField(30);
        add(rateField);

        add(new JLabel("Type"));
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Limited", "Unlimited"});
        add(typeComboBox);

        add(new JLabel("Initial Quantity") );
        JTextField initialQty = new JTextField(30);
        add( initialQty );

        add(new JLabel("Description") );
        JTextField desc = new JTextField(30);
        add(desc);


        JButton submit = new JButton("Submit");
        add(submit, "skip");

        registerVariable("Product Name", nameField);
        registerVariable("Rate", rateField);
        registerVariable("Type", typeComboBox);
        registerVariable("Quantity", initialQty);
        registerVariable("Description", desc);
        registerVariable("Submit", submit);

        getRootPane().setDefaultButton(submit);
    }
}
