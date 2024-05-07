import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Objects;

public class Laptop {

    @ParamsToFilter(russianTitle = "Операционная система", filterType = FilterType.EXACT_MATCH)
    private OperatingSystem operatingSystem;

    @ParamsToFilter(russianTitle = "Цвет", filterType = FilterType.EXACT_MATCH)
    private Color color;

    @ParamsToFilter(russianTitle = "Объем жесткого диска", filterType = FilterType.COMPARE)
    private Integer SSD;

    @ParamsToFilter(russianTitle = "Объем оперативной памяти", filterType = FilterType.COMPARE)
    private Integer RAM;

    @ParamsToFilter(russianTitle = "Диагональ экрана", filterType = FilterType.COMPARE)
    private Double screenDiagonal;

    public Laptop(OperatingSystem operatingSystem, Color color, Integer SSD, Integer RAM, Double screenDiagonal) {
        this.operatingSystem = operatingSystem;
        this.color = color;
        this.SSD = SSD;
        this.RAM = RAM;
        this.screenDiagonal = screenDiagonal;
    }


    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getSSD() {
        return SSD;
    }

    public void setSSD(Integer SSD) {
        this.SSD = SSD;
    }

    public Integer getRAM() {
        return RAM;
    }

    public void setRAM(Integer RAM) {
        this.RAM = RAM;
    }

    public Double getScreenDiagonal() {
        return screenDiagonal;
    }

    public void setScreenDiagonal(Double screenDiagonal) {
        this.screenDiagonal = screenDiagonal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Laptop)) return false;
        Laptop laptop = (Laptop) o;
        return getOperatingSystem() == laptop.getOperatingSystem() && getColor() == laptop.getColor() && getSSD().equals(laptop.getSSD()) && getRAM().equals(laptop.getRAM()) && getScreenDiagonal().equals(laptop.getScreenDiagonal());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOperatingSystem(), getColor(), getSSD(), getRAM(), getScreenDiagonal());
    }

    @Override
    public String toString() {
        return "Ноутбук: " +
                "Операционная система = " + operatingSystem +
                ", Цвет = " + color +
                ", Объем жесткого диска = " + SSD +
                " Гб, Объем оперативной памяти = " + RAM +
                " Гб, диагональ экрана = " + screenDiagonal +
                ".";
    }

    public static HashMap<Integer, ParamsToFilter> getParams() {
        HashMap<Integer, ParamsToFilter> params = new HashMap<>();
        Field[] laptopFields = Laptop.class.getDeclaredFields();
        int i = 1;
        for (Field field : laptopFields) {
            if (field.isAnnotationPresent(ParamsToFilter.class)) {
                ParamsToFilter[] p = field.getAnnotationsByType(ParamsToFilter.class);
                for (ParamsToFilter paramsToFilter : p) {
                    params.put(i, paramsToFilter);
                    i++;
                }
            }
        }
        return params;
    }
}
