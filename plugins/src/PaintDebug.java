import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;

/**
 * @author rvbiljouw
 */
@PluginManifest(name = "Paint Debug", author = "rvbiljouw", version = 1.0)
public class PaintDebug extends Plugin {
    private NpcPaint npcPaint = new NpcPaint();
    private MousePaint mousePaint = new MousePaint();
    private ObjectPaint objectPaint = new ObjectPaint();
    private PositionPaint positionPaint = new PositionPaint();
    private AnimationPaint animationPaint = new AnimationPaint();
    private InventoryPaint inventoryPaint = new InventoryPaint();
    private MinimapPaint minimapPaint = new MinimapPaint();

    private boolean npcPaintActive = false;
    private boolean mousePaintActive = false;
    private boolean objectPaintActive = false;
    private boolean positionPaintActive = false;
    private boolean animationPaintActive = false;
    private boolean inventoryPaintActive = false;
    private boolean minimapPaintActive = false;


    private Menu paint;

    @Override
    public void startup() {
        paint = new Menu("Debugging");
        CheckMenuItem npcs = new CheckMenuItem("Draw NPCs");
        npcs.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!npcPaintActive) {
                    getSession().getPaintManager().register(npcPaint);
                } else {
                    getSession().getPaintManager().deregister(npcPaint);
                }
                npcPaintActive = !npcPaintActive;
            }
        });
        paint.getItems().add(npcs);

        CheckMenuItem objects = new CheckMenuItem("Draw Objects");
        objects.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!objectPaintActive) {
                    getSession().getPaintManager().register(objectPaint);
                } else {
                    getSession().getPaintManager().deregister(objectPaint);
                }
                objectPaintActive = !objectPaintActive;
            }
        });
        paint.getItems().add(objects);

        CheckMenuItem inventory = new CheckMenuItem("Draw Inventory");
        inventory.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!inventoryPaintActive) {
                    getSession().getPaintManager().register(inventoryPaint);
                } else {
                    getSession().getPaintManager().deregister(inventoryPaint);
                }
                inventoryPaintActive = !inventoryPaintActive;
            }
        });
        paint.getItems().add(inventory);

        CheckMenuItem animation = new CheckMenuItem("Draw Animation");
        animation.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!animationPaintActive) {
                    getSession().getPaintManager().register(animationPaint);
                } else {
                    getSession().getPaintManager().deregister(animationPaint);
                }
                animationPaintActive = !animationPaintActive;
            }
        });
        paint.getItems().add(animation);

        CheckMenuItem position = new CheckMenuItem("Draw Position");
        position.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!positionPaintActive) {
                    getSession().getPaintManager().register(positionPaint);
                } else {
                    getSession().getPaintManager().deregister(positionPaint);
                }
                positionPaintActive = !positionPaintActive;
            }
        });
        paint.getItems().add(position);

        CheckMenuItem mouse = new CheckMenuItem("Draw Mouse");
        mouse.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!mousePaintActive) {
                    getSession().getPaintManager().register(mousePaint);
                } else {
                    getSession().getPaintManager().deregister(mousePaint);
                }
                mousePaintActive = !mousePaintActive;
            }
        });
        paint.getItems().add(mouse);

        CheckMenuItem minimap = new CheckMenuItem("Draw Minimap");
        minimap.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!minimapPaintActive) {
                    getSession().getPaintManager().register(minimapPaint);
                } else {
                    getSession().getPaintManager().deregister(minimapPaint);
                }
                minimapPaintActive = !minimapPaintActive;
            }
        });
        paint.getItems().add(minimap);
        getSession().registerMenu(paint);
    }

    @Override
    public void execute() {
    }

    @Override
    public void cleanup() {
        getSession().deregisterMenu(paint);
    }


}
