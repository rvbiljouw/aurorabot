import ms.aurora.api.methods.Players;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.event.listeners.PaintListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Cov
 * Date: 15/04/13
 * Time: 23:52
 * To change this template use File | Settings | File Templates.
 */
public class PathMaker extends JFrame implements PaintListener {

    private final ArrayList<RSTile> tileList;
    private final JTextArea tileTextArea;
    private Plugin ctx;

    public PathMaker(Plugin ctx) {
        this.tileList = new ArrayList<RSTile>();
        this.tileTextArea = new JTextArea();
        this.ctx = ctx;
        this.initComponents();
    }

    private void initComponents() {
        this.setTitle("Cov's Path Maker");
        this.setResizable(false);
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;

        this.tileTextArea.setPreferredSize(new Dimension(550, 300));
        this.tileTextArea.setMinimumSize(new Dimension(550, 300));
        this.add(this.tileTextArea, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;

        JButton button = new JButton("Add Tile");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tileList.add(Players.getLocal().getLocation());
                updateTextArea();
            }
        });
        this.add(button, gbc);

        gbc.gridx++;

        button = new JButton("Remove Last");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tileList.remove(tileList.size() - 1);
                updateTextArea();
            }
        });
        this.add(button, gbc);

        gbc.gridx++;

        button = new JButton("Clear");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tileList.clear();
                updateTextArea();
            }
        });
        this.add(button, gbc);

        this.setPreferredSize(new Dimension(550, 355));
        this.pack();
    }

    private void updateTextArea() {
        final StringBuilder builder = new StringBuilder();

        builder.append("new RSTile[] {\n\t");
        int i = 0;
        for (RSTile tile: this.tileList) {
            if ((i++ % 3) == 0) {
                builder.append("\n\t");
            }
            builder.append(String.format("new RSTile(%s, %s), ", tile.getX(), tile.getY()));
        }
        builder.append("\n}");

        this.ctx.invokeLater(new Runnable() {
            @Override
            public void run() {
                tileTextArea.setText(builder.toString());
            }
        });
    }

    @Override
    public void onRepaint(Graphics graphics) {
        synchronized (this) {
            for (RSTile tile: this.tileList) {

            }
        }
    }

}
