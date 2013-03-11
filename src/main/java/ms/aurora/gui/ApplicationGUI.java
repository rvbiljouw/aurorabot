package ms.aurora.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ApplicationGUI extends JFrame {
    private static final long serialVersionUID = 2133982906411134266L;
    private JPanel contentPane;
    private JTabbedPane tabbedPane;

    /**
     * Create the frame.
     */
    public ApplicationGUI(final ApplicationController controller) {
        setResizable(false);
        setTitle("Project Aurora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 765, 600);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmNewClient = new JMenuItem("New client");
        mntmNewClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.onNewClient();
            }
        });
        mnFile.add(mntmNewClient);

        JMenuItem mntmExistingSession = new JMenuItem("Stored client");
        mnFile.add(mntmExistingSession);

        JSeparator cliCloSeperator = new JSeparator();
        mnFile.add(cliCloSeperator);

        JMenuItem mntmClose = new JMenuItem("Close");
        mnFile.add(mntmClose);

        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);

        JMenuItem mntmAccounts = new JMenuItem("Accounts");
        mnEdit.add(mntmAccounts);

        JSeparator accSettSeperator = new JSeparator();
        mnEdit.add(accSettSeperator);

        JMenuItem mntmSettings = new JMenuItem("Settings");
        mnEdit.add(mntmSettings);

        JMenu mnRun = new JMenu("Run");
        menuBar.add(mnRun);

        JMenuItem mntmScripts = new JMenuItem("Select script");
        mntmScripts.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.onSelectScript();
            }
        });
        mnRun.add(mntmScripts);

        JSeparator scriptSeperator = new JSeparator();
        mnRun.add(scriptSeperator);

        JMenuItem mntmPauseScript = new JMenuItem("Pause script");
        mnRun.add(mntmPauseScript);

        JMenuItem mntmResumeScript = new JMenuItem("Resume script");
        mnRun.add(mntmResumeScript);

        JMenuItem mntmStopScript = new JMenuItem("Stop script");
        mnRun.add(mntmStopScript);

        JMenu mnTools = new JMenu("Tools");
        menuBar.add(mnTools);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 765, 556);
        contentPane.add(tabbedPane);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}
