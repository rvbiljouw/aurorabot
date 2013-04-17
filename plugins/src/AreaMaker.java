import ms.aurora.api.methods.Minimap;
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
 * Date: 16/04/13
 * Time: 11:43
 *
 * @author A_C/Cov
 */
public class AreaMaker extends JFrame implements PaintListener {

    private final JTextArea tileTextArea;
    private Plugin ctx;
    private RSTile neTile, swTile;

    public AreaMaker(Plugin ctx) {
        this.tileTextArea = new JTextArea();
        this.ctx = ctx;
        this.initComponents();
    }

    private void initComponents() {
        this.setTitle("Cov's Area Maker");
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

        JButton button = new JButton("Add NE Tile");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                neTile = Players.getLocal().getLocation();
                updateTextArea();
            }
        });
        this.add(button, gbc);

        gbc.gridx++;

        button = new JButton("Add SW Tile");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                swTile = Players.getLocal().getLocation();
                updateTextArea();
            }
        });
        this.add(button, gbc);
        gbc.gridx++;

        button = new JButton("Clear");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                neTile = null;
                swTile = null;
                updateTextArea();
            }
        });
        this.add(button, gbc);

        this.setPreferredSize(new Dimension(550, 355));
        this.pack();
    }

    private void updateTextArea() {
        final StringBuilder builder = new StringBuilder();
        builder.append("new RSArea( ");
        builder.append(neTile == null ? "" : String.format("new RSTile( %s, %s)", neTile.getX(), neTile.getY()));
        builder.append(", " + (swTile == null ? "" : String.format("new RSTile( %s, %s)", swTile.getX(), swTile.getY())));
        builder.append(");");
        this.ctx.invokeLater(new Runnable() {
            @Override
            public void run() {
                tileTextArea.setText(builder.toString());
            }
        });
    }

    @Override
    public void onRepaint(Graphics graphics) {
        /*if (neTile != null && swTile != null) {
            Point ne = Minimap.convert(neTile);
            Point sw = Minimap.convert(swTile);
            graphics.drawRect(sw.x, sw.y, (ne.x - sw.x), (sw.y - ne.y));
        } */
    }
}
