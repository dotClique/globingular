package globingular.ui;

public enum Colors {
    /**
     * Color on a map of a country which is not visited.
     */
    COUNTRY_NOT_VISITED("#b9b9b9"),
    /**
     * Color on a map of a visited country.
     */
    COUNTRY_VISITED("#7fe5f0");

    /**
     * Standard '#'-plus-6-digits web hexadecimal color string.
     */
    private String hex;

    Colors(final String hex) {
        this.hex = hex;
    }

    /**
     * Get standard '#'-plus-6-digits web hexadecimal color string.
     * @return Standard '#'-plus-6-digits web hexadecimal color string.
     */
    public String getHex() {
        return this.hex;
    }
}
