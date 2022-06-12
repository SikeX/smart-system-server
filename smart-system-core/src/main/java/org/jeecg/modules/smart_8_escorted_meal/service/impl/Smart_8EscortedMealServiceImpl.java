package org.jeecg.modules.smart_8_escorted_meal.service.impl;

import org.jeecg.modules.smart_8_escorted_meal.entity.Smart_8EscortedMeal;
import org.jeecg.modules.smart_8_escorted_meal.mapper.Smart_8EscortedMealMapper;
import org.jeecg.modules.smart_8_escorted_meal.service.ISmart_8EscortedMealService;
import org.jeecg.modules.smart_reception.mapper.Smart_8VisitorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 陪同用餐人员表
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
@Service
public class Smart_8EscortedMealServiceImpl extends ServiceImpl<Smart_8EscortedMealMapper, Smart_8EscortedMeal> implements ISmart_8EscortedMealService {
    @Autowired
    private Smart_8EscortedMealMapper smart_8EscortedMealMapper;

    @Override
    public  String getMainIdById(String id){
        return smart_8EscortedMealMapper.getMainIdById(id);
    }
}
