package in.maharaja.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by MPONS040401 on 11/29/2015.
 */
public class AddProduct extends AbstractFrame {

    public AddProduct(){
        super("Add New Product");

        setSize(new Dimension(300, 500) );
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
    }

    public void createGUI(){
        setLayout( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();

        setConstraints(c, 0, 0, 1, 1);
        add(new JLabel("Name"), c);

        JTextField nameField = new JTextField(30);
        setConstraints(c, 0, 1, 3, 1);
        add( nameField, c );

        setConstraints(c, 1, 0, 1, 1);
        add(new JLabel("Rate"), c);

        JTextField rateField = new JTextField(30);
        setConstraints(c, 1, 1, 3, 1);
        add(rateField, c);

        setConstraints(c, 2, 0, 1, 1);
        add(new JLabel("Limited"), c);

        JCheckBox limited = new JCheckBox("Is Limited");
        setConstraints(c, 2, 1, 3, 1);
        add(limited, c);

        registerVariable("Product Name", nameField);
        registerVariable("Rate", rateField);
    }

    public void showGUI(){
        setVisible( true );
    }

    public void setConstraints(GridBagConstraints c, int x, int y, int spanx, int spany){
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = x;
        c.gridheight = y;
    }
}
