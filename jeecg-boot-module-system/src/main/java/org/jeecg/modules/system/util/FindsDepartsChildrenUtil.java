package org.jeecg.modules.system.util;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * <P>
 * 对应部门的表,处理并查找树级数据
 * <P>
 * 
 * @Author: Steve
 * @Date: 2019-01-22
 */
public class FindsDepartsChildrenUtil {

	//部门树信息-树结构
	//private static List<SysDepartTreeModel> sysDepartTreeList = new ArrayList<SysDepartTreeModel>();
	
	//部门树id-树结构
    //private static List<DepartIdModel> idList = new ArrayList<>();


    /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将SysDepart类型的list集合转换成SysDepartTreeModel类型的集合
     */
    public static List<SysDepartTreeModel> wrapTreeDataToTreeList(List<SysDepart> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
//        idList.clear();
    	List<DepartIdModel> idList = new ArrayList<DepartIdModel>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepart depart = recordList.get(i);
            records.add(new SysDepartTreeModel(depart));
        }
        List<SysDepartTreeModel> tree = findChildren(records, idList);
        setEmptyChildrenAsNull(tree);
        return tree;
    }

    /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将SysDepart类型的list集合转换成SysDepartTreeModel类型的集合
     */
    public static List<SysDepartTreeModel> wrapTreeDataToFuzeList(List<SysDepart> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
//        idList.clear();
        List<DepartIdModel> idList = new ArrayList<DepartIdModel>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepart depart = recordList.get(i);
            records.add(new SysDepartTreeModel(depart));
        }
        List<SysDepartTreeModel> tree = findFuzeChildren(records, idList);
        setEmptyChildrenAsNull(tree);
        return tree;
    }

    /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将SysDepart类型的list集合转换成SysDepartTreeModel类型的集合,自然层级
     */
    public static List<SysDepartTreeModel> wrapNaturalTreeDataToTreeList(List<SysDepart> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
//        idList.clear();
        List<DepartIdModel> idList = new ArrayList<DepartIdModel>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepart depart = recordList.get(i);
            records.add(new SysDepartTreeModel(depart));
        }
        List<SysDepartTreeModel> tree = findNaturalChildren(records, idList);
        setEmptyChildrenAsNull(tree);
        return tree;
    }

    /**
     * 获取 DepartIdModel
     * @param recordList
     * @return
     */
    public static List<DepartIdModel> wrapTreeDataToDepartIdTreeList(List<SysDepart> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<DepartIdModel> idList = new ArrayList<DepartIdModel>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepart depart = recordList.get(i);
            records.add(new SysDepartTreeModel(depart));
        }
        findChildren(records, idList);
        return idList;
    }

    /**
     * 获取 DepartIdModel
     * @param recordList
     * @return
     */
    public static List<DepartIdModel> wrapTreeDataToDepartIdFuzeTreeList(List<SysDepart> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<DepartIdModel> idList = new ArrayList<DepartIdModel>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepart depart = recordList.get(i);
            records.add(new SysDepartTreeModel(depart));
        }
        findFuzeChildren(records, idList);
        return idList;
    }

    /**
     * 获取 DepartIdModel,自然层级
     * @param recordList
     * @return
     */
    public static List<DepartIdModel> wrapNaturalTreeDataToDepartIdTreeList(List<SysDepart> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<DepartIdModel> idList = new ArrayList<DepartIdModel>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepart depart = recordList.get(i);
            records.add(new SysDepartTreeModel(depart));
        }
        findNaturalChildren(records, idList);
        return idList;
    }


    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合，业务层级
     */
    private static List<SysDepartTreeModel> findChildren(List<SysDepartTreeModel> recordList,
                                                         List<DepartIdModel> departIdList) {

        List<SysDepartTreeModel> treeList = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepartTreeModel branch = recordList.get(i);
            if(oConvertUtils.isEmpty(branch.getParentId()) && branch.getDepartName().equals("哈尔滨道里区纪委")){
                treeList.add(branch);
                DepartIdModel departIdModel = new DepartIdModel().convert(branch);
                departIdList.add(departIdModel);
            }
//            if (oConvertUtils.isEmpty(branch.getParentId())) {
//                treeList.add(branch);
//                DepartIdModel departIdModel = new DepartIdModel().convert(branch);
//                departIdList.add(departIdModel);
//            }
        }
        getGrandChildren(treeList,recordList,departIdList);
        
        //idList = departIdList;
        return treeList;
    }

    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合，业务层级
     */
    private static List<SysDepartTreeModel> findFuzeChildren(List<SysDepartTreeModel> recordList,
                                                         List<DepartIdModel> departIdList) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        System.out.println(sysUser);
        List<SysDepartTreeModel> treeList = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepartTreeModel branch = recordList.get(i);
            if( branch.getValue().equals(sysUser.getDepartId())){
                treeList.add(branch);
                DepartIdModel departIdModel = new DepartIdModel().convert(branch);
                departIdList.add(departIdModel);
            }
//            if (oConvertUtils.isEmpty(branch.getParentId())) {
//                treeList.add(branch);
//                DepartIdModel departIdModel = new DepartIdModel().convert(branch);
//                departIdList.add(departIdModel);
//            }
        }
        getGrandChildren(treeList,recordList,departIdList);

        //idList = departIdList;
        return treeList;
    }

    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合,自然层级
     */
    private static List<SysDepartTreeModel> findNaturalChildren(List<SysDepartTreeModel> recordList,
                                                         List<DepartIdModel> departIdList) {

        List<SysDepartTreeModel> treeList = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepartTreeModel branch = recordList.get(i);
            if(branch.getDepartName().equals("部门类型")){
                treeList.add(branch);
                DepartIdModel departIdModel = new DepartIdModel().convert(branch);
                departIdList.add(departIdModel);
            }
//            if (oConvertUtils.isEmpty(branch.getParentId())) {
//                treeList.add(branch);
//                DepartIdModel departIdModel = new DepartIdModel().convert(branch);
//                departIdList.add(departIdModel);
//            }
        }
        getNaturalGrandChildren(treeList,recordList,departIdList);

        //idList = departIdList;
        return treeList;
    }

    /**
     * queryTreeList的子方法====3====
     *该方法是找到顶级父类下的所有子节点集合并封装到TreeList集合,业务层级
     */
    private static void getGrandChildren(List<SysDepartTreeModel> treeList,List<SysDepartTreeModel> recordList,List<DepartIdModel> idList) {

        for (int i = 0; i < treeList.size(); i++) {
            SysDepartTreeModel model = treeList.get(i);
            DepartIdModel idModel = idList.get(i);
            for (int i1 = 0; i1 < recordList.size(); i1++) {
                SysDepartTreeModel m = recordList.get(i1);
                if (m.getParentId()!=null && m.getParentId().equals(model.getId())) {
                    model.getChildren().add(m);
                    DepartIdModel dim = new DepartIdModel().convert(m);
                    idModel.getChildren().add(dim);
                }
            }
            getGrandChildren(treeList.get(i).getChildren(), recordList, idList.get(i).getChildren());
        }

    }

    /**
     * queryTreeList的子方法====3====
     *该方法是找到顶级父类下的所有子节点集合并封装到TreeList集合,自然层级
     */
    private static void getNaturalGrandChildren(List<SysDepartTreeModel> treeList,List<SysDepartTreeModel> recordList,List<DepartIdModel> idList) {

        for (int i = 0; i < treeList.size(); i++) {
            SysDepartTreeModel model = treeList.get(i);
            DepartIdModel idModel = idList.get(i);
            for (int i1 = 0; i1 < recordList.size(); i1++) {
                SysDepartTreeModel m = recordList.get(i1);
                if (m.getBusinessParentId()!=null && m.getBusinessParentId().equals(model.getId())) {
                    model.getChildren().add(m);
                    DepartIdModel dim = new DepartIdModel().convert(m);
                    idModel.getChildren().add(dim);
                }
            }
            getNaturalGrandChildren(treeList.get(i).getChildren(), recordList, idList.get(i).getChildren());
        }

    }
    

    /**
     * queryTreeList的子方法 ====4====
     * 该方法是将子节点为空的List集合设置为Null值
     */
    private static void setEmptyChildrenAsNull(List<SysDepartTreeModel> treeList) {

        for (int i = 0; i < treeList.size(); i++) {
            SysDepartTreeModel model = treeList.get(i);
            if (model.getChildren().size() == 0) {
                model.setChildren(null);
                model.setIsLeaf(true);
            }else{
                setEmptyChildrenAsNull(model.getChildren());
                model.setIsLeaf(false);
            }
        }
        // sysDepartTreeList = treeList;
    }
}
