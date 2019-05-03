package model;

/**
 * Created by David on 2017/7/26.
 * 教育部認可證照
 */
public class Licenses {
    private int id;
    private String name;
    private String located;
    private String level;
    private String type;
    private String unit;

    public Licenses(int id, String name, String located, String level, String type, String unit) {
        this.id = id;
        this.name = name;
        this.located = located;
        this.level = level;
        this.type = type;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocated() {
        return located;
    }

    public void setLocated(String located) {
        this.located = located;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String toString() {
        return "{\"id\":\"" + id + "\", \"name\":\"" + name + "\", \"located\":\"" + located + "\", \"level\":\"" + level + "\", \"type\": \"" + type + "\", \"unit\": \"" + unit + "\"}";
    }
}
