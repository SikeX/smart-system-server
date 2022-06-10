package org.jeecg.modules.smart_8_escorted_meal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smart_8_escorted_meal.entity.Smart_8EscortedMeal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 陪同用餐人员表
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
public interface Smart_8EscortedMealMapper extends BaseMapper<Smart_8EscortedMeal> {

    String getMainIdById(String id);

}
