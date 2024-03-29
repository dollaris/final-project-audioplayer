package hi.verkefni.mediaplayer.vidmot;

public enum LeftMenu {
    EXPLORE("Explore"),
    ARTISTS("Artists"),
    ALBUMS("ALBUMS"),
    SONGS("SONGS"),
    LISTEN_AND_WATCH("Listen and Watch"),
    SETTINGS("Settings"),
    PLAYLISTS("Playlists"),
    CHATS("Chats");

    private String menuOption;

    LeftMenu(String menuOption) {
        this.menuOption = menuOption;
    }

    public String getMenuOption() {
        return menuOption;
    }
}
