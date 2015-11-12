package in.maharaja.gui;

import in.maharaja.controls.TablePane;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Prateek on 10-11-2015.
 */
public class DayReport extends AbstractFrame {

    private JTree fileTree;
    private File[] allFiles;
    private JSlider reportView;
    private static final int DETAILED = 0;
    private static final int NORMAL = 1;
    private Map<String, ArrayList<File>> monthWise;
    private DefaultMutableTreeNode root;
    public TablePane tablePane;

    public DayReport(){
        super("Day Report");
        setSize( new Dimension(800, 600));
        root = new DefaultMutableTreeNode("Month Wise Files");
        fileTree = new JTree(root);
        reportView = new JSlider( JSlider.HORIZONTAL, DETAILED, NORMAL, DETAILED);
        reportView.setMaximum(1);
        reportView.setMinimum(0);
        reportView.setMajorTickSpacing(1);
        reportView.setPaintLabels(true);
    }

    @Override
    public void createGUI() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Report Type", JLabel.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel listPane = new JPanel();
        listPane.setLayout( new BorderLayout() );

        listPane.add( new JScrollPane(fileTree), BorderLayout.CENTER );

        tablePane = new TablePane(300, 300);
        JPanel n = new JPanel();
        n.setLayout( new BoxLayout(n, BoxLayout.PAGE_AXIS) );
        n.add( title );
        n.add(reportView);
        n.add(tablePane);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listPane, n);
        add(splitPane, BorderLayout.CENTER);

        Hashtable labelHash = new Hashtable();
        labelHash.put(0, new JLabel("Detailed") );
        labelHash.put(1, new JLabel("Summarised") );

        reportView.setLabelTable( labelHash );

        Dimension minimumSize = new Dimension(300, 600);
        listPane.setMinimumSize( new Dimension(250, 600) );
        n.setMinimumSize( minimumSize );

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }

    @Override
    public void showGUI() {
        setVisible( true );
    }

    public DefaultMutableTreeNode getRootNode(){ return root; }

    public void registerTreeSelectionListener(TreeSelectionListener t){
        fileTree.addTreeSelectionListener(t);
    }

    public void registerChangeListener(ChangeListener changeListener){
        reportView.addChangeListener( changeListener );
    }

    public int getReportType(){
        return reportView.getValue();
    }

}
