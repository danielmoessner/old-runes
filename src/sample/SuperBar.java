package sample;

import javafx.animation.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;

import java.util.*;

class SuperBar {

    private final MenuBar MENUBAR = new MenuBar();
    private final BorderPane ROOT = new BorderPane();
    private final StackPane STACK_PANE = new StackPane();
    private final ArrayList<SuperMenu> SUPER_MENUS = new ArrayList<>();
    private final ArrayList<SuperMenu.SuperItem> KEYS = new ArrayList<>();
    private final ArrayList<AnimationTimer> ANIMATION_TIMERS = new ArrayList<>();
    private final ArrayList<SuperMenu.SuperItem> CLICK_HISTORY = new ArrayList<>();

    SuperBar() {
        STACK_PANE.setMinSize(0, 0);
        STACK_PANE.setPrefHeight(Integer.MAX_VALUE);
        ROOT.setCenter(STACK_PANE);
        ROOT.setTop(MENUBAR);
    }

    void addKey(Collection<SuperMenu.SuperItem> key, SuperMenu.SuperItem secretItem) {
        this.KEYS.addAll(key);
        KEYS.add(secretItem);
    }

    MenuBar getMENUBAR() {
        return MENUBAR;
    }

    Pane getSTACK_PANE() {
        return STACK_PANE;
    }

    BorderPane getROOT() {
        return ROOT;
    }

    class SuperMenu {

        private final Menu MENU = new Menu();
        private final ToggleGroup TOGGLE_GROUP = new ToggleGroup();
        private final ArrayList<SuperItem> SUPER_ITEMS = new ArrayList<>();
        private SuperItem membership;

        SuperMenu(String name) {
            MENU.setText(name);
            SUPER_MENUS.add(this);
        }

        SuperMenu(String name, SuperItem membership) {
            if (membership.LOADED.getValue())
                return;
            this.membership = membership;
            MENU.setText(name);
            membership.SUPER_ITEM_MENUS.add(0, this);
        }

        SuperItem getSelectedSuperItem() {
            Optional<SuperItem> result = SUPER_ITEMS.stream().filter(superItem -> superItem.RADIO_MENU_ITEM.isSelected()).findFirst();
            if (result.isPresent())
                return result.get();
            else
                return null;
        }

        Menu getMENU() {
            return MENU;
        }

        class SuperItem {

            private final MenuItem MENU_ITEM = new MenuItem();
            private final RadioMenuItem RADIO_MENU_ITEM = new RadioMenuItem();
            private final ArrayList<SuperMenu> SUPER_ITEM_MENUS = new ArrayList<>();
            private final BooleanProperty LOADED = new SimpleBooleanProperty();
            private ImageView imageView;
            private SubScene subScene;
            private AnimationTimer animationTimer;
            private Region region;

            // Constructor for the RadioMenuItems
            private SuperItem() {
                SUPER_ITEMS.add(this);
                if (membership == null && MENU.getItems().size() > 0)
                    MENU.getItems().add(new SeparatorMenuItem());
                MENU.getItems().add(RADIO_MENU_ITEM);
                RADIO_MENU_ITEM.setToggleGroup(TOGGLE_GROUP);
                RADIO_MENU_ITEM.setOnAction(e -> {
                    clicked();
                    secretAlgorithm();
                });
            }

            SuperItem(String name, ImageView imageView) {
                this();
                RADIO_MENU_ITEM.setText(name);
                imageView.setFocusTraversable(true);
                imageView.fitWidthProperty().bind(STACK_PANE.widthProperty());
                imageView.fitHeightProperty().bind(STACK_PANE.heightProperty());
                this.imageView = imageView;
            }

            SuperItem(String name, SubScene subScene) {
                this();
                RADIO_MENU_ITEM.setText(name);
                subScene.setFocusTraversable(true);
                subScene.widthProperty().bind(STACK_PANE.widthProperty());
                subScene.heightProperty().bind(STACK_PANE.heightProperty());
                this.subScene = subScene;
            }

            SuperItem(String name, Region region) {
                this();
                RADIO_MENU_ITEM.setText(name);
                region.setFocusTraversable(true);
                region.setPrefSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                this.region = region;
            }

            SuperItem(String name, SubScene subScene, AnimationTimer animationTimer) {
                this(name, subScene);
                ANIMATION_TIMERS.add(animationTimer);
                this.animationTimer = animationTimer;
            }

            // Constructor for the usual MenuItems
            SuperItem(String name) {
                SUPER_ITEMS.add(this);
                MENU.getItems().add(MENU_ITEM);
                MENU_ITEM.setText(name);
                MENU_ITEM.addEventHandler(ActionEvent.ANY, e -> secretAlgorithm());
            }

            // RadioMenuItemClicked
            private void clicked() {
                // stop all Animation Timers
                ANIMATION_TIMERS.stream().forEach(AnimationTimer::stop);
                // update
                deselectItems(SUPER_MENUS);
                selectItems();
                // remove old/add new menus
                MENUBAR.getMenus().removeAll(MENUBAR.getMenus());
                addMenus();
                // remove old/add new content
                STACK_PANE.getChildren().removeAll(STACK_PANE.getChildren());
                STACK_PANE.getChildren().add(getContent());
                if (animationTimer != null)
                    animationTimer.start();
                // menu item content loaded
                LOADED.setValue(true);
            }

            // important for clicked()
            private void selectItems() {
                RADIO_MENU_ITEM.setSelected(true);
                if (membership != null)
                    membership.selectItems();
            }

            private void deselectItems(ArrayList<SuperMenu> list) {
                list.forEach(superMenu -> superMenu.SUPER_ITEMS.
                        forEach(superItem -> superItem.RADIO_MENU_ITEM.setSelected(false)));
                list.forEach(superMenu -> superMenu.SUPER_ITEMS.
                        forEach(superItem -> deselectItems(superItem.SUPER_ITEM_MENUS)));
            }

            private void addMenus() {
                SUPER_ITEM_MENUS.stream().forEach(superMenu -> MENUBAR.getMenus().add(0, superMenu.MENU));
                if (membership == null) {
                    SUPER_MENUS.stream().forEach(superMenu -> MENUBAR.getMenus().add(0, superMenu.MENU));
                    return;
                }
                membership.addMenus();
            }

            // adds a HelpMenu
            void addHelp(String helpText) {
                final Text t = new Text(helpText);
                t.setFont(Font.font("Times New Roman", 18));
                t.setTextAlignment(TextAlignment.CENTER);
                t.setTextOrigin(VPos.TOP);
                final Rectangle r = new Rectangle(t.maxWidth(0), t.maxHeight(0), Color.color(0.2, 0.5, 0.3, 0.8));
                final StackPane sp = new StackPane(r, t);
                sp.setAlignment(Pos.BOTTOM_CENTER);
                sp.setPrefSize(STACK_PANE.getWidth(), STACK_PANE.getHeight());
                STACK_PANE.widthProperty().addListener(change -> {
                    sp.setPrefSize(STACK_PANE.getWidth(), STACK_PANE.getHeight());
                });
                STACK_PANE.heightProperty().addListener(change -> {
                    sp.setPrefSize(STACK_PANE.getWidth(), STACK_PANE.getHeight());
                });

                final SuperMenu sm = new SuperMenu("?", this);
                final SuperItem si1 = sm.new SuperItem("show and hide help");
                si1.MENU_ITEM.addEventHandler(ActionEvent.ANY, action -> {
                    if (STACK_PANE.getChildren().size() == 1)
                        STACK_PANE.getChildren().addAll(sp);
                    else {
                        ArrayList<Node> rl = new ArrayList<>();
                        STACK_PANE.getChildren().stream().filter(child -> child == sp).forEach(rl::add);
                        STACK_PANE.getChildren().removeAll(rl);
                    }
                });
                MENUBAR.getMenus().add(sm.MENU);
            }

            // secret - don't look at it
            private void secretAlgorithm() {
                CLICK_HISTORY.add(this);
                for (int k = 0; k < KEYS.size(); k++) {

                    ArrayList<SuperItem> key = new ArrayList<>();
                    SuperItem secretItem;
                    while (KEYS.get(k).getMENU_ITEM().getParentMenu().isVisible()) {
                        key.add(KEYS.get(k));
                        k++;
                    }
                    secretItem = KEYS.get(k);
                    k++;

                    for (int i = 0; i < CLICK_HISTORY.size(); i++) {
                        for (int j = 0; j < key.size(); j++) {
                            if ((i + j) < CLICK_HISTORY.size() && CLICK_HISTORY.get(i + j) == key.get(j)) {
                                if (j == key.size() - 1) {
                                    ArrayList<SuperItem> removeList = new ArrayList<>();
                                    CLICK_HISTORY.stream().forEach(removeList::add);
                                    CLICK_HISTORY.removeAll(removeList);
                                    secretItem.getMENU_ITEM().fire();
                                }
                            } else
                                break;
                        }
                    }
                }
            }

            // Getter
            MenuItem getMENU_ITEM() {
                if (imageView != null || subScene != null || region != null)
                    return RADIO_MENU_ITEM;
                else
                    return MENU_ITEM;
            }

            Node getContent() {
                if (region != null)
                    return region;
                else if (subScene != null)
                    return subScene;
                else if (imageView != null)
                    return imageView;
                else
                    return null;
            }

            ImageView getImageView() {
                return imageView;
            }

            Region getRegion() {
                return region;
            }
        }
    }
}