public class Settings {
    public String[] renderNames;
    public String[] generateNames;
    public boolean renderFinal;
    public boolean optimize;
    public boolean colored;

    public Settings(String[] renderNames, String[] generateNames, boolean renderFinal, boolean optimize, boolean colored) {
        this.renderNames = renderNames;
        this.generateNames = generateNames;
        this.renderFinal = renderFinal;
        this.optimize = optimize;
        this.colored = colored;
    }
}
