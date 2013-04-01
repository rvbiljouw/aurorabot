import ms.aurora.api.Context;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;
import ms.aurora.event.listeners.PaintListener;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author tobiewarburton
 */
public class InterfaceExplorer implements PaintListener {
    private final Context ctx;

    public InterfaceExplorer(Context ctx) {
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
            tablePanel = new JPanel();

            init();
            reload();
        }

        private void init() {
            setSize(1280, 768);
            setResizable(false);
            setVisible(false);
            setLayout(new BorderLayout());

            tablePanel.setLayout(new BorderLayout());
            tablePanel.setMaximumSize(new Dimension(640, 738));
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
            tree.setMinimumSize(new Dimension(640, 738));

            JScrollPane scrollPane = new JScrollPane(tree);
            scrollPane.setMinimumSize(new Dimension(640, 738));
            scrollPane.setPreferredSize(new Dimension(640, 738));
            add(scrollPane, BorderLayout.WEST);

            tablePanel.setMinimumSize(new Dimension(640, 738));
            tablePanel.setPreferredSize(new Dimension(640, 738));
            add(tablePanel, BorderLayout.EAST);

            JButton button = new JButton("Reload");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    reload();
                }
            });
            add(button, BorderLayout.SOUTH);
        }

        public void reload() {
            root = new DefaultMutableTreeNode("root");
            for (RSWidgetGroup group : Widgets.getAll()) {
                if (group == null) continue;
                RSWidget[] groupItems = group.getWidgets();


                DefaultMutableTreeNode iface = new DefaultMutableTreeNode("[" + group.getIndex() + "]");
                for (int i = 0; i < groupItems.length; i++) {
                    if (groupItems[i] != null) {
                        DefaultMutableTreeNode groupItem = new DefaultMutableTreeNode(groupItems[i]);
                        for (RSWidget actualChild : groupItems[i].getChildren()) {
                            if (actualChild != null) {
                                groupItem.add(new DefaultMutableTreeNode(actualChild));
                            }
                        }
                        iface.add(groupItem);
                    }
                }
                root.add(iface);
            }
            tree.setModel(new DefaultTreeModel(root));
            tree.clearSelection();
        }

        public void display(RSWidget widget) {
            current = widget;
            Object[][] model = new Object[][]{
                    {"parent", widget.getGroup()},
                    {"id", widget.getIndex()},
                    {"x", widget.getX()},
                    {"y", widget.getY()},
                    {"width", widget.getWidth()},
                    {"height", widget.getHeight()},
                    {"items", widget.getInventoryItems()},
                    {"item id", widget.getItemId()},
                    {"item stack size", widget.getItemStackSize()},
                    {"text", widget.getText()}
            };
            table.setModel(new DefaultTableModel(model, new String[]{"key", "value"}));
        }
    }
}
