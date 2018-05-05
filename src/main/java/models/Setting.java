package models;

import javax.persistence.*;

/**
 * Setting is class of preference's model.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */
@Entity
@Table(name = "preferences")
public class Setting {

    /**
     * ID of setting.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * Key of config (name of config).
     */
    @Column(name = "configKey")
    @Basic
    private String configKey;

    /**
     * Value of setting.
     */
    @Column(name = "configValue")
    @Basic
    private String configValue;

    /**
     * Constructor of setting class.
     */
    public Setting() {
    }

    /**
     * Constructor of setting of class.
     * @param config_key is key of config (name of setting).
     * @param config_value is value of config (value of setting).
     */
    public Setting(String config_key, String config_value) {
        this.configKey = config_key;
        this.configValue = config_value;
    }

    /**
     * getter of id.
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter of id.
     * @param id is ID of setting.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter of config_key.
     * @return config_key.
     */
    public String getConfigKey() {
        return configKey;
    }

    /**
     * Setter of config_key.
     * @param configKey is key of setting.
     */
    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    /**
     * Getter of configValue.
     * @return configValue.
     */
    public String getConfigValue() {
        return configValue;
    }

    /**
     * Setter of configValue.
     * @param configValue is value of setting.
     */
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
}
