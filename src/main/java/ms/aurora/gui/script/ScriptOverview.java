package ms.aurora.gui.script;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ScriptOverview extends JFrame {

    private JPanel contentPane;
    private JTextField txtKeywords;
    private JPanel panel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ScriptOverview frame = new ScriptOverview();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ScriptOverview() {
        setTitle("Script Overview");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 660, 480);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnOk = new JButton("OK");
        btnOk.setBounds(573, 423, 81, 29);
        contentPane.add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(454, 423, 117, 29);
        contentPane.add(btnCancel);

        JLabel lblKeywords = new JLabel("Keywords:");
        lblKeywords.setBounds(6, 17, 81, 16);
        contentPane.add(lblKeywords);

        txtKeywords = new JTextField();
        txtKeywords.setBounds(80, 11, 230, 28);
        contentPane.add(txtKeywords);
        txtKeywords.setColumns(10);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setBounds(322, 17, 61, 16);
        contentPane.add(lblCategory);

        JComboBox cbxCategory = new JComboBox();
        cbxCategory.setBounds(395, 13, 162, 27);
        contentPane.add(cbxCategory);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(569, 12, 85, 29);
        contentPane.add(btnSearch);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 45, 660, 366);
        contentPane.add(scrollPane);

        panel = new JPanel();
        scrollPane.setViewportView(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        addScripts();
    }

    public void addScripts() {
        for (int i = 0; i < 20; i++) {
            ScriptWidget widget = new ScriptWidget(new SampleScript());
            panel.add(widget);

        }
    }
}
