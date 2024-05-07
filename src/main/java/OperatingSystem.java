public enum OperatingSystem {
    WINDOWS_10_HOME ("Windows 10 HOME"),
    WINDOWS_10_PR0("Windows 10 PRO"),
    WINDOWS_11_HOME("Windows 11 HOME"),
    WINDOWS_11_PRO ("Windows 11 PRO"),
    LINUX ("Linux"),
    ASTRA_LINUX ("Astra Linux"),
    UBUNTU ( "Ubuntu"),
    MAC_OS("Mac OS");

    public String title;

    OperatingSystem(String title) {
        this.title = title;
    }
}
