package com.marcfearby.models;

import com.marcfearby.controllers.AbstractTabController;

import java.nio.file.Path;

public class TabInfo {

    public enum TabType { PLAIN, FANCY }

    private TabType type;
    private Path root;
    private boolean active;
    private String selectedTreePath = null;
    private boolean expanded = false;
    private boolean isPlaylistProvider = false;
    private AbstractTabController controller = null;
    private Double div1Position = 0.25;
    private Path currentTrack = null;


    public TabInfo(TabType type, Path root, boolean active) {
        this.type = type;
        this.root = root;
        this.active = active;
    }


    public TabType getType() {
        return this.type;
    }


    public AbstractTabController getController() {
        return this.controller;
    }
    public void setController(AbstractTabController controller) {
        this.controller = controller;
    }


    public Path getRoot() {
        return this.root;
    }
    public void setRoot(Path root) {
        this.root = root;
    }


    public boolean getActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }


    public String getSelectedTreePath() {
        return this.selectedTreePath;
    }
    public void setSelectedTreePath(String path) {
        this.selectedTreePath = path;
    }


    public boolean getExpanded() {
        return this.expanded;
    }
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }


    public boolean getIsPlaylistProvider() {
        return this.isPlaylistProvider;
    }
    public void setIsPlaylistProvider(boolean isProvider) {
        this.isPlaylistProvider = isProvider;
    }


    public Double getDiv1Position() {
        return div1Position;
    }
    public void setDiv1Position(Double div1Position) {
        this.div1Position = div1Position;
    }


    public Path getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Path currentTrack) {
        this.currentTrack = currentTrack;
    }


}
