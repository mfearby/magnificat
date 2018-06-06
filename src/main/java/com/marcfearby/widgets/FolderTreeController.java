package com.marcfearby.widgets;

import com.marcfearby.components.PlainTabController;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import com.marcfearby.models.FileTreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

/**
 * Controller for the folder tree browser
 *
 * @author Marc Fearby
 * @see <a href="https://docs.oracle.com/javafx/2/ui_controls/tree-view.htm">Using JavaFX UI Controls - 13 Tree View</a>
 */
public class FolderTreeController {

    @FXML private TreeView<File> tree;
    private PlainTabController tab;
    private final Image closedImage = new Image(getClass().getResourceAsStream("/icons/tango/folder.png"));
    private final Image openImage = new Image(getClass().getResourceAsStream("/icons/tango/folder-open.png"));
    private final Image refreshImage = new Image(getClass().getResourceAsStream("/icons/tango/view-refresh.png"));
    private final Image newTabImage = new Image(getClass().getResourceAsStream("/icons/tango/tab-new.png"));


    public FolderTreeController() {

    }


    public void init(PlainTabController tab, File directory) {
        this.tab = tab;
        setupTree(directory);
    }


    private void setupTree(File directory) {
        tree.setCellFactory(param -> new TreeCell<File>() {
            @Override
            public void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText("");
                    setGraphic(null);

                } else {
                    setText(item.getName());
                    Image img = this.getTreeItem().isExpanded() ? openImage : closedImage;
                    ImageView imageView = new ImageView(img);
                    setGraphic(imageView);

                    MenuItem m = new MenuItem("Refresh", new ImageView(refreshImage));
                    m.setOnAction(event -> {
                        FileTreeItem fi = (FileTreeItem)getTreeItem();
                        fi.refresh();
                    });
                    MenuItem o = new MenuItem("Open in new tab", new ImageView(newTabImage));
                    o.setOnAction(event -> addTab(item) );
                    setContextMenu(new ContextMenu(m, o));
                }
            }
        });

        tree.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        File item = newValue.getValue();
                        selectFolder(item);
                    }
                });

        try {
            FileTreeItem<File> root = new FileTreeItem<>(directory);
            tree.setRoot(root);
            root.setExpanded(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addTab(File path) {
        tab.addTab(path);
    }


    private void selectFolder(File directory) {
        tab.selectFolder(directory);
    }

}
