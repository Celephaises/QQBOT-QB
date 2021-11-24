package com.lij.myqqrobotserver.service;

import com.lij.myqqrobotserver.entity.Clock;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClockService {
    @Insert("INSERT INTO CLOCK (qq_num,from_group,content,date) VALUES (#{qqNum},#{fromGroup},#{content},#{date})")
    int insertClock(String qqNum, String fromGroup, String content, String date);

    @Select("SELECT * FROM CLOCK WHERE qq_num=#{qqNum} AND from_group=#{fromGroup} ORDER BY date DESC LIMIT 10")
    List<Clock> selectClock(String qqNum, String fromGroup);

    @Select("SELECT * FROM CLOCK WHERE qq_num=#{qqNum} AND from_group=#{fromGroup} AND content =#{content} ORDER BY date DESC LIMIT 10")
    List<Clock> selectClockWithContent(String qqNum, String fromGroup, String content);

    @Select("SELECT content,COUNT(date) count FROM `clock` WHERE qq_num =#{qqNum}  AND from_group=#{fromGroup} GROUP BY content")
    List<Clock> selectClockContentType(String qqNum, String fromGroup);
}
