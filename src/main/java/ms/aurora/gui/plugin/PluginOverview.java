package ms.aurora.gui.plugin;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.core.model.PluginConfig;
import ms.aurora.core.plugin.PluginLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class PluginOverview extends JFrame {
	private JPanel contentPane;
	private JTextField txtKeywords;
	private JPanel panel;

	/**
	 * Create the frame.
	 */
	public PluginOverview() {
		setTitle("Plugin Overview");
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

        for(Plugin plugin : PluginLoader.getPlugins()) {
            PluginConfig config = PluginConfig.getByName(plugin.getClass().getName());
            PluginWidget widget = new PluginWidget(plugin, config.isEnabled());
            panel.add(widget);
        }
	}
}
