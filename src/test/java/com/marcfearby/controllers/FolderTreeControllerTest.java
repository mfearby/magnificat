package com.marcfearby.controllers;

import com.marcfearby.Testing;
import com.marcfearby.interfaces.FolderTreeHandler;
import com.marcfearby.interfaces.PlainTabHandler;
import com.marcfearby.models.FileTreeItem;
import com.marcfearby.utils.Global;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import static org.junit.Assert.*;

public class FolderTreeControllerTest extends ApplicationTest {

    private TreeView<Path> tree;
    private FolderTreeController ctrl;
    private static Path receivedPath = null;

    @BeforeClass
    public static void beforeClass() {
        Testing.setupTestFileSystem(false); // load mp3 blobs into file handles
    }


    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(Stage::new);
    }


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/widgets/FolderTreeView.fxml"));
        tree = loader.load();
        ctrl = loader.getController();

        class DummyTreeHandler implements FolderTreeHandler {
            @Override
            public void treePathSelected(TreeItem<Path> item) {
                receivedPath = item.getValue();
            }
            @Override
            public void toggleSelectedTreePath(TreeItem<Path> item) {

            }
        }

        class DummyTabHandler implements PlainTabHandler {
            @Override
            public void changeTabRoot(Path path) { }
            @Override
            public void addTab(Path path) {
                receivedPath = path;
            }
            @Override
            public void becomePlaylistProvider(boolean startPlaying) { }
            @Override
            public void togglePlayPause() { }
            @Override
            public void saveCurrentTrack(Path track) { }
        }

        // Call to beforeClass() above will make sure this is the Jimfs file system
        FileSystem fs = Global.getFileSystem();

        Path home = fs.getPath(Global.TESTING_PATH_HOME);
        ctrl.init(home, new DummyTreeHandler(), new DummyTabHandler());

        stage.setScene(new Scene(tree, 800, 600));
        stage.show();
        // Bring to front so that any test robots will work with this window
        stage.toFront();
        WaitForAsyncUtils.waitForFxEvents();
    }


    @Before
    public void setUp() {
        receivedPath = null;
    }


    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }


    @Test
    public void tree_not_null() {
        assertNotNull(tree);
    }


    @Test
    public void tree_root_path_is_correct() {
        TreeItem<Path> root = tree.getRoot();
        assertEquals("Tree root path has an unexpected value", "/Users/Magnificat", root.getValue().toString());
    }


    @Test
    public void select_root_node() {
        // Select Music first, then select the root note (since it's already selected
        // by default, clicking it again won't trigger the treePathSelected event)
        clickOn("Music");
        clickOn("Magnificat");
        assertNotNull(receivedPath);
        String expected = Testing.TESTING_PATH_HOME;
        assertEquals("Path received for selected tree node is incorrect", expected, receivedPath.toString());
    }


    @Test
    public void right_click_open_in_new_tab() {
        clickOn("Other", MouseButton.SECONDARY);
        clickOn("Open in new tab");
        assertNotNull(receivedPath);
        String expected = Testing.TESTING_PATH_OTHER;
        assertEquals("Path received for the new tab is incorrect", expected, receivedPath.toString());
    }


    @Test
    public void expand_tree_path() {
        String expected = Testing.TESTING_PATH_WHATEVER;
        ctrl.expandPath(expected, false);
        // I could probably have just checked receivedPath, but this makes me feel better ;-)
        FileTreeItem<Path> root = (FileTreeItem<Path>)tree.getSelectionModel().getSelectedItem();
        assertNotNull(root);
        assertEquals(expected, root.getValue().toString());
    }


    @Test
    public void expand_nonexistent_tree_path_as_far_as_possible() {
        String expected = Testing.TESTING_PATH_WHATEVER;
        ctrl.expandPath(expected + "/NonexistentFolder", false);
        assertEquals(expected, receivedPath.toString());
    }


    @Test
    public void expand_tree_path_in_an_expanded_state() {
        ctrl.expandPath(Testing.TESTING_PATH_OTHER, true);
        FileTreeItem<Path> root = (FileTreeItem<Path>)tree.getSelectionModel().getSelectedItem();
        assertTrue(root.isExpanded());
    }


    @Test
    public void expand_tree_path_in_a_collapsed_state() {
        ctrl.expandPath(Testing.TESTING_PATH_OTHER, false);
        FileTreeItem<Path> root = (FileTreeItem<Path>)tree.getSelectionModel().getSelectedItem();
        assertFalse(root.isExpanded());
    }


//    // Not sure how to test this yet since it uses a javafx.stage.DirectoryChooser
//    @Test
//    public void change_root_node() {
//        clickOn("Music", MouseButton.SECONDARY);
//        clickOn("Select folder...");
//        assertNotNull(receivedPath);
//        assertEquals("Path received for new tree root is incorrect", "/Users/Magnificat/Music", receivedPath.toString());
//    }

}