package com.sidney.framelib.mvvm;

/**
 * 描 述：定义livedata命令数据格式
 * 作 者：hxl 2022/1/17 17:32
 * 修改描述： XXX
 * 修 改 人： XXX  2022/1/17 17:32
 * 修改版本： XXX
 */
public class VmCommand {

    private String id;//命令id
    private Object data;//具体数据

    public VmCommand(String id, Object data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
