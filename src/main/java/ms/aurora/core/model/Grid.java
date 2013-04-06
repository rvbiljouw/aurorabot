package ms.aurora.core.model;

import ms.aurora.api.wrappers.RSTile;

import javax.persistence.*;
import java.awt.*;
import java.util.List;

/**
 * @author rvbiljouw
 */
@NamedQueries({
        @NamedQuery(name = "grid.findByBase", query = "select g from Grid g where g.baseX = :baseX and g.baseY = :baseY"),
        @NamedQuery(name = "grid.findAll", query = "select g from Grid g")
})
@Entity
public class Grid extends AbstractModel {

    @Id
    @GeneratedValue
    private Long id;

    private int baseX;
    private int baseY;

    @OneToMany(mappedBy = "grid")
    private List<GridNode> gridNodes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBaseX() {
        return baseX;
    }

    public void setBaseX(int baseX) {
        this.baseX = baseX;
    }

    public int getBaseY() {
        return baseY;
    }

    public void setBaseY(int baseY) {
        this.baseY = baseY;
    }

    public List<GridNode> getGridNodes() {
        return gridNodes;
    }

    public void setGridNodes(List<GridNode> gridNodes) {
        this.gridNodes = gridNodes;
    }

    public boolean contains(RSTile tile) {
        Rectangle rectangle = new Rectangle(baseX, baseY, 104, 104);
        return rectangle.contains(tile.getX(), tile.getY());
    }

    public static List<Grid> getAll() {
        return getEm().createNamedQuery("grid.findAll", Grid.class).getResultList();
    }

    public static Grid getByBase(int x, int y) {
        List<Grid> grids = getEm().createNamedQuery("grid.findByBase", Grid.class).setParameter("baseX", x).
                setParameter("baseY", y).getResultList();
        if (grids.size() != 1) {
            return null;
        }
        return grids.get(0);
    }
}
