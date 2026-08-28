package com.example.fullstacklab.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.fullstacklab.domain.LearningNote;

public interface LearningNoteMapper extends BaseMapper<LearningNote> {

	List<LearningNote> selectRecent(@Param("limit") int limit);
}
