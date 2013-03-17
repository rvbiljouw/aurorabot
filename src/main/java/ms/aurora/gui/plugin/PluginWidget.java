package ms.aurora.gui.plugin;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PluginWidget extends JPanel {
    private static final long serialVersionUID = 414689352878859224L;
    private boolean m_enabled = false;

    /**
     * Create the panel.
     */
    public PluginWidget(final Plugin plugin, final boolean enabled) {
        this.m_enabled = enabled;

        setBackground(UIManager.getColor("Panel.background"));
        setBorder(BorderFactory.createLineBorder(Color.black));
        // 630, 150
        setMinimumSize(new Dimension(630, 150));
        setPreferredSize(getMinimumSize());
        setMaximumSize(getPreferredSize());
        setLayout(null);

        PluginManifest manifest = plugin.getManifest();
        final JButton btnStart = new JButton(!enabled ? "Enable" : "Disable");
        btnStart.setBounds(538, 115, 84, 29);
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!m_enabled) {
                    PluginController.enablePlugin(plugin);
                    btnStart.setText("Disable");
                    m_enabled = true;
                } else {
                    PluginController.disablePlugin(plugin);
                    btnStart.setText("Enable");
                    m_enabled = false;
                }
            }
        });
        add(btnStart);

        JButton btnMoreInfo = new JButton("More info");
        btnMoreInfo.setBounds(428, 115, 117, 29);
        add(btnMoreInfo);

        JLabel lblScriptHeader = new JLabel(manifest.name() + " by " + manifest.author());
        lblScriptHeader.setForeground(new Color(0, 153, 204));
        lblScriptHeader.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
        lblScriptHeader.setBounds(19, 6, 603, 23);
        add(lblScriptHeader);

        JLabel lblDescription = new JLabel(manifest.shortDescription());
        lblDescription.setVerticalAlignment(SwingConstants.TOP);
        lblDescription.setBounds(29, 41, 581, 71);
        add(lblDescription);
    }
}
