import ms.aurora.api.ClientContext;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.event.listeners.PaintListener;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

/**
 * @author tobiewarburton
 */
public class InterfaceExplorer implements PaintListener {
    private final ClientContext ctx;

    public InterfaceExplorer(ClientContext ctx) {
        this.ctx = ctx;
    }

    private Explorer explorer;
    private long last = -1;
    public RSWidget current;


    public void init() {
        explorer = new Explorer();
    }

    @Override
    public void onRepaint(Graphics graphics) {
        if (System.currentTimeMillis() - last > 1000 * 60) {
            last = System.currentTimeMillis();
            explorer.reload();
        }
        if (current != null) {
            graphics.drawRect(current.getX(), current.getY(), current.getWidth(), current.getHeight());
        }
    }

    public void toggle() {
        explorer.setVisible(!explorer.isVisible());
    }

    class Explorer extends JFrame {
        private JTree tree;
        private JTable table;
        private JPanel treePanel;
        private JPanel tablePanel;
        private DefaultMutableTreeNode root;

        public Explorer() {
            tree = new JTree();
            table = new JTable() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            treePanel = new JPanel();
            tablePanel = new JPanel();

            init();
            reload();
        }

        private void init() {
            setSize(600, 500);
            setResizable(false);
            setVisible(false);
            setLayout(new BorderLayout());

            treePanel.setLayout(new BorderLayout());
            treePanel.setMaximumSize(new Dimension(300, 500));
            treePanel.add(tree);

            tablePanel.setLayout(new BorderLayout());
            tablePanel.setMaximumSize(new Dimension(300, 500));
            tablePanel.add(table);

            tree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    if (node == null) return;
                    if (!(node.getUserObject() instanceof RSWidget)) return;
                    display((RSWidget) node.getUserObject());
                }
            });
            add(treePanel, BorderLayout.WEST);
            add(tablePanel, BorderLayout.EAST);
        }

        public void reload() {
            root = new DefaultMutableTreeNode("root");
            for (RSWidget[] widgets : ctx.widgets.getAll()) {
                if (widgets == null) continue;
                if (widgets.length == 0) continue;
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(widgets[0]);
                for (int i = 1; i < widgets.length; i++) {
                    child.add(new DefaultMutableTreeNode(widgets[i]));
                }
                root.add(child);
            }
            tree.setModel(new DefaultTreeModel(root));
            tree.clearSelection();
        }

        public void display(RSWidget widget) {
            current = widget;
            Object[][] model = new Object[][]{
                    {"parent", widget.getParentId()},
                    {"id", widget.getId()},
                    {"x", widget.getX()},
                    {"y", widget.getY()},
                    {"width", widget.getWidth()},
                    {"height", widget.getHeight()},
                    {"items", widget.getInventoryItems()},
                    {"item id", widget.getItemId()},
                    {"item stack size", widget.getItemStackSize()}
            };
            table.setModel(new DefaultTableModel(model, new String[]{"key", "value"}));
        }
    }
}
