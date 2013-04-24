import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;

/**
 * @author rvbiljouw
 */
@PluginManifest(name = "Paint Debug", author = "_override", version = 1.0)
public class PaintDebug extends Plugin {
    private NpcPaint npcPaint = new NpcPaint();
    private MousePaint mousePaint = new MousePaint();
    private GroundObjectPaint groundObjectPaint = new GroundObjectPaint();
    private AnimableObjectPaint animableObjectPaint = new AnimableObjectPaint();
    private WallDecorationPaint wallDecorationPaint = new WallDecorationPaint();
    private WallObjectPaint wallObjectPaint = new WallObjectPaint();
    private PositionPaint positionPaint = new PositionPaint();
    private AnimationPaint animationPaint = new AnimationPaint();
    private InventoryPaint inventoryPaint = new InventoryPaint();
    private MinimapPaint minimapPaint = new MinimapPaint();
    private ShopPaint shopPaint = new ShopPaint();
    private CameraPaint cameraPaint = new CameraPaint();

    private boolean npcPaintActive = false;
    private boolean mousePaintActive = false;
    private boolean groundObjectPaintActive = false;
    private boolean animableObjectPaintActive = false;
    private boolean wallDecorationPaintActive = false;
    private boolean wallObjectPaintActive = false;
    private boolean positionPaintActive = false;
    private boolean animationPaintActive = false;
    private boolean inventoryPaintActive = false;
    private boolean minimapPaintActive = false;
    private boolean shopPaintActive = false;
    private boolean cameraPaintActive = false;


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

        CheckMenuItem groundObjects = new CheckMenuItem("Draw Ground Objects");
        groundObjects.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!groundObjectPaintActive) {
                    getSession().getPaintManager().register(groundObjectPaint);
                } else {
                    getSession().getPaintManager().deregister(groundObjectPaint);
                }
                groundObjectPaintActive = !groundObjectPaintActive;
            }
        });
        paint.getItems().add(groundObjects);

        CheckMenuItem animableObjects = new CheckMenuItem("Draw Animable Objects");
        animableObjects.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!animableObjectPaintActive) {
                    getSession().getPaintManager().register(animableObjectPaint);
                } else {
                    getSession().getPaintManager().deregister(animableObjectPaint);
                }
                animableObjectPaintActive = !animableObjectPaintActive;
            }
        });
        paint.getItems().add(animableObjects);

        CheckMenuItem wallDecoration = new CheckMenuItem("Draw Wall Decorations");
        wallDecoration.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!wallDecorationPaintActive) {
                    getSession().getPaintManager().register(wallDecorationPaint);
                } else {
                    getSession().getPaintManager().deregister(wallDecorationPaint);
                }
                wallDecorationPaintActive = !wallDecorationPaintActive;
            }
        });
        paint.getItems().add(wallDecoration);

        CheckMenuItem wallObjects = new CheckMenuItem("Draw Wall Objects");
        wallObjects.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!wallObjectPaintActive) {
                    getSession().getPaintManager().register(wallObjectPaint);
                } else {
                    getSession().getPaintManager().deregister(wallObjectPaint);
                }
                wallObjectPaintActive = !wallObjectPaintActive;
            }
        });
        paint.getItems().add(wallObjects);

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

        CheckMenuItem shop = new CheckMenuItem("Draw Shop");
        shop.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!shopPaintActive) {
                    getSession().getPaintManager().register(shopPaint);
                } else {
                    getSession().getPaintManager().deregister(shopPaint);
                }
                shopPaintActive = !shopPaintActive;
            }
        });
        paint.getItems().add(shop);

        CheckMenuItem camera = new CheckMenuItem("Draw Camera");
        camera.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!cameraPaintActive) {
                    getSession().getPaintManager().register(cameraPaint);
                } else {
                    getSession().getPaintManager().deregister(cameraPaint);
                }
                cameraPaintActive = !cameraPaintActive;
            }
        });
        paint.getItems().add(camera);

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
