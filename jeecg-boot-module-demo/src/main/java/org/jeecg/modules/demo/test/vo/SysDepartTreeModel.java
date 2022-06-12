package org.jeecg.modules.demo.test.vo;

import org.jeecg.modules.demo.test.entity.SysDepart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 部门表 存储树结构数据的实体类
 * <p>
 * 
 * @Author Steve
 * @Since 2019-01-22 
 */
public class SysDepartTreeModel implements Serializable{
	
    private static final long serialVersionUID = 1L;
    
    /** 对应SysDepart中的id字段,前端数据树中的key*/
    private String id;

    /** 对应depart_name字段,前端数据树中的title*/
    private String name;

    private String parentId;


    /**
     * 将SysDepart对象转换成SysDepartTreeModel对象
     * @param sysDepart
     */
	public SysDepartTreeModel(SysDepart sysDepart) {
		this.id = sysDepart.getId();
        this.name = sysDepart.getDepartName();
        this.parentId = sysDepart.getBusinessParentId();
    }


	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId= parentId;
    }

    public SysDepartTreeModel() { }

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        SysDepartTreeModel model = (SysDepartTreeModel) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(name, model.name) &&
                Objects.equals(parentId, model.parentId);
    }
    
    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentId);
    }

}
