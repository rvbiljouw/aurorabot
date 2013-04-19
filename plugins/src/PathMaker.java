import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.Minimap;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Viewport;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.event.listeners.PaintListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

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
    private boolean record = false;
    private Thread tileThread;

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

        JButton button = new JButton("Start");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                record = true;
                if (tileThread != null && tileThread.isAlive()){
                    tileThread.interrupt();
                }
                tileThread = new Thread(ctx.getThreadGroup(), new Runnable() {
                    @Override
                    public void run() {
                        RSTile last = Players.getLocal().getLocation();
                        tileList.add(last);
                        updateTextArea();
                        while (record) {
                            RSTile current = Players.getLocal().getLocation();
                            if (Calculations.distance(current, last) > 6) {
                                tileList.add(current);
                                last = current;
                                updateTextArea();
                            }
                        }
                    }
                });
                tileThread.start();
                tileList.add(Players.getLocal().getLocation());
                updateTextArea();
            }
        });
        this.add(button, gbc);

        gbc.gridx++;

        button = new JButton("Stop");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                record = false;
                tileList.add(Players.getLocal().getLocation());
                if (tileThread != null && tileThread.isAlive()){
                    tileThread.interrupt();
                }
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

        builder.append("new RSTile[] {\n");
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
            Point last = null;
            for (RSTile tile: this.tileList) {
                Point current = Minimap.convert(tile);
                if (current.x != -1) {
                    graphics.drawRect(current.x - 1, current.y - 1, 3, 3);
                    if (last != null) {
                        graphics.drawLine(last.x, last.y, current.x, current.y);
                    }
                    last = current;
                }
            }
        }
    }

}
