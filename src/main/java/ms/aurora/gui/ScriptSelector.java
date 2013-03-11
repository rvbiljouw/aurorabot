package ms.aurora.gui;

import ms.aurora.api.script.ScriptMetadata;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ScriptSelector extends JDialog {
    private static final long serialVersionUID = 3550665113414765521L;
    private final JPanel contentPanel = new JPanel();

    /**
     * Create the dialog.
     */
    public ScriptSelector(final List<ScriptMetadata> metadatas,
                          final ScriptSelectionListener listener) {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(null);
        contentPanel.setBounds(0, 0, 450, 1);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel);
        contentPanel.setLayout(null);
        JPanel buttonPane = new JPanel();
        buttonPane.setBounds(0, 239, 450, 39);
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane);
        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

        String[] items = new String[metadatas.size()];
        for (ScriptMetadata metadata : metadatas) {
            items[metadatas.indexOf(metadata)] = metadata.name();
        }
        final JList<String> list = new JList<String>();
        list.setBounds(0, 0, 450, 239);
        list.setListData(items);
        getContentPane().add(list);
        final ScriptSelector _this = this;
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (list.getSelectedIndex() != -1) {
                    listener.onSelected(metadatas.get(list.getSelectedIndex()));
                } else {
                    JOptionPane.showMessageDialog(_this,
                            "You dun fucked up now!");
                }
            }
        });
    }

    public static interface ScriptSelectionListener {

        public void onSelected(ScriptMetadata metadata);

    }
}
