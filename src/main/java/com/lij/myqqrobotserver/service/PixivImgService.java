package com.lij.myqqrobotserver.service;

import com.lij.myqqrobotserver.entity.PixivImg;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Celphis
 */
@Service
public interface PixivImgService {

    @Select("SELECT * FROM PixivArtWorks WHERE  tags LIKE '%'||#{tag}||'%' AND nsfw_tag=#{level} ORDER BY RANDOM() LIMIT #{length}")
    List<PixivImg> getPixivImgRandomList(String tag,int level, int length);

    @Update("UPDATE PixivArtWorks SET page_count=#{0} WHERE pid=#{1}")
    int updatePixivImgPageCount(Integer pageCount, Long pid);
}
