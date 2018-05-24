package cn.sd.core.config.plugin.defaultPlug;
/**
 * 默认配置装载器用，适用于一个Key对应一个Value的配置
 * @author Rex
 */
public class ConfigDetail {
    private String configName="";
    private String configValue=null;
    
    public ConfigDetail() {
    }
    
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigValue() {
        return configValue;
    }
    
    public boolean getBooleanValue() {
        boolean v = false;
        if (configValue!=null){
            v = Boolean.parseBoolean(configValue);
        }
        return v;
    }
    public int getIntValue() {
        int v = 0;
        if (configValue!=null){
            v = Integer.parseInt(configValue);
        }
        return v;
    }
    public double getDoubleValue() {
        double v = 0d;
        if (configValue!=null){
             v = Double.parseDouble(configValue);
        }
        return v;
    }
}

