package org.jeecg.modules.calendarEvent.service.impl;

import org.jeecg.modules.calendarEvent.entity.CalendarEvent;
import org.jeecg.modules.calendarEvent.mapper.CalendarEventMapper;
import org.jeecg.modules.calendarEvent.service.ICalendarEventService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 日历事件
 * @Author: jeecg-boot
 * @Date:   2021-12-04
 * @Version: V1.0
 */
@Service
public class CalendarEventServiceImpl extends ServiceImpl<CalendarEventMapper, CalendarEvent> implements ICalendarEventService {

}
