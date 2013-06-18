package ms.aurora.api.plugin.internal;

import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;
import ms.aurora.core.Session;

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
    private GroundItemPaint groundItemPaint = new GroundItemPaint();
    private BankPaint bankPaint = new BankPaint();
    private Session session;
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
    private boolean groundItemPaintActive = false;
    private boolean bankItemPaintActive = false;
    private Menu paint;

    @Override
    public void startup() {
        paint = new Menu("Debugging");
        CheckMenuItem npcs = new CheckMenuItem("Draw NPCs");
        npcs.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!npcPaintActive) {
                    session.getEventBus().register(npcPaint);
                } else {
                    session.getEventBus().deregister(npcPaint);
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
                    session.getEventBus().register(groundObjectPaint);
                } else {
                    session.getEventBus().deregister(groundObjectPaint);
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
                    session.getEventBus().register(animableObjectPaint);
                } else {
                    session.getEventBus().deregister(animableObjectPaint);
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
                    session.getEventBus().register(wallDecorationPaint);
                } else {
                    session.getEventBus().deregister(wallDecorationPaint);
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
                    session.getEventBus().register(wallObjectPaint);
                } else {
                    session.getEventBus().deregister(wallObjectPaint);
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
                    session.getEventBus().register(inventoryPaint);
                } else {
                    session.getEventBus().deregister(inventoryPaint);
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
                    session.getEventBus().register(animationPaint);
                } else {
                    session.getEventBus().deregister(animationPaint);
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
                    session.getEventBus().register(positionPaint);
                } else {
                    session.getEventBus().deregister(positionPaint);
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
                    session.getEventBus().register(mousePaint);
                } else {
                    session.getEventBus().deregister(mousePaint);
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
                    session.getEventBus().register(minimapPaint);
                } else {
                    session.getEventBus().deregister(minimapPaint);
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
                    session.getEventBus().register(shopPaint);
                } else {
                    session.getEventBus().deregister(shopPaint);
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
                    session.getEventBus().register(cameraPaint);
                } else {
                    session.getEventBus().deregister(cameraPaint);
                }
                cameraPaintActive = !cameraPaintActive;
            }
        });
        paint.getItems().add(camera);

        CheckMenuItem groundItem = new CheckMenuItem("Draw GroundItems");
        groundItem.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!groundItemPaintActive) {
                    session.getEventBus().register(groundItemPaint);
                } else {
                    session.getEventBus().deregister(groundItemPaint);
                }
                groundItemPaintActive = !groundItemPaintActive;
            }
        });
        paint.getItems().add(groundItem);

        CheckMenuItem bankItem = new CheckMenuItem("Draw Bank");
        bankItem.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!bankItemPaintActive) {
                    session.getEventBus().register(bankPaint);
                } else {
                    session.getEventBus().deregister(bankPaint);
                }
                bankItemPaintActive = !bankItemPaintActive;
            }
        });
        paint.getItems().add(bankItem);

        registerMenu(paint);
        session = getSession();
    }

    @Override
    public void execute() {
    }

    @Override
    public void cleanup() {
        deregisterMenu(paint);
    }


}
