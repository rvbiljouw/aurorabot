package ms.aurora.gui;

import ms.aurora.gui.plugin.PluginController;
import ms.aurora.gui.script.ScriptController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ApplicationGUI extends JFrame {
    private static final long serialVersionUID = 2133982906411134266L;
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JMenuBar menuBar;
    private JMenu mnPlugins;

    /**
     * Create the frame.
     */
    public ApplicationGUI() {
        setResizable(false);
        setTitle("Project Aurora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 771, 600);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmNewClient = new JMenuItem("New client");
        mntmNewClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ApplicationController.onNewClient();
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
                ScriptController.onScriptOverview();
            }
        });
        mnRun.add(mntmScripts);

        JSeparator scriptSeperator = new JSeparator();
        mnRun.add(scriptSeperator);

        JMenuItem mntmPauseScript = new JMenuItem("Pause script");
        mntmPauseScript.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScriptController.pauseScript();
            }
        });
        mnRun.add(mntmPauseScript);

        JMenuItem mntmResumeScript = new JMenuItem("Resume script");
        mntmResumeScript.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScriptController.resumeScript();
            }
        });
        mnRun.add(mntmResumeScript);

        JMenuItem mntmStopScript = new JMenuItem("Stop script");
        mntmStopScript.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScriptController.stopScript();
            }
        });
        mnRun.add(mntmStopScript);

        mnPlugins = new JMenu("Plug-ins");
        menuBar.add(mnPlugins);

        JMenuItem mntmPluginOverview = new JMenuItem("Plugin Overview");
        mntmPluginOverview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PluginController.onPluginOverview();
            }
        });
        mnPlugins.add(mntmPluginOverview);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 770, 556);
        contentPane.add(tabbedPane);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void setTools(JMenu mnTools) {
        menuBar.remove(this.mnPlugins);
        if (mnTools != null) {
            this.mnPlugins = mnTools;
            menuBar.add(this.mnPlugins);
        } else {
            this.mnPlugins = new JMenu("Tools");
            menuBar.add(mnTools);

            JMenuItem mntmPluginOverview = new JMenuItem("Plugin Overview");
            mntmPluginOverview.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PluginController.onPluginOverview();
                }
            });
            this.mnPlugins.add(mntmPluginOverview);
        }
        repaint();
    }
}
