package in.maharaja.controllers;

import in.maharaja.gui.AddProduct;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Controller for AddProduct Form
 */
public class AddProductController extends Controller<AddProduct> {


    public AddProductController(AddProduct app) {
        super(app);
        app.createGUI();
        registerEvents();
    }

    @Override
    public void registerEvents() {
        getApp().registerEvent("Type", (ActionListener) e -> {
            JTextField qty = (JTextField) getApp().getComponent("Quantity");
            JComboBox type = (JComboBox) getApp().getComponent("Type");

            if( type.getSelectedIndex() == 0 )
                qty.setEnabled( false );
            else qty.setEnabled( true );
        });

        getApp().registerEvent("Submit", (ActionListener) e->{
            AddProduct app = getApp();
            String name = app.getVariable("Product Name"),
                    rate = app.getVariable("Rate"),
                    type = app.getVariable("Type"),
                    desc = app.getVariable("Description"),
                    qty = app.getVariable("Quantity");

            if( type.compareTo("LIMITED") == 1 ){
                String query = String.format("INSERT INTO COUNTER.PRODUCTS(TITLE, DESCRIPTION, RATE, IS_LIMITED) VALUES('%s', '%s', '%s', True)", name, desc, rate);


            }
        });
    }
}
