package com.example.fullstacklab;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.fullstacklab.domain.LearningNote;
import com.example.fullstacklab.mapper.LearningNoteMapper;

@SpringBootTest
@ActiveProfiles("test")
class FullstackLabBackendApplicationTests {

	@Autowired
	private LearningNoteMapper learningNoteMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void mybatisPlusAndXmlMapperWorkTogether() {
		OffsetDateTime now = OffsetDateTime.now();
		LearningNote note = new LearningNote();
		note.setTitle("MyBatis-Plus learning note");
		note.setContent("BaseMapper insert and XML select");
		note.setCreatedAt(now);
		note.setUpdatedAt(now);

		int inserted = learningNoteMapper.insert(note);
		List<LearningNote> notes = learningNoteMapper.selectRecent(10);

		assertThat(inserted).isEqualTo(1);
		assertThat(note.getId()).isNotNull();
		assertThat(notes).extracting(LearningNote::getTitle)
				.contains("MyBatis-Plus learning note");
	}

}
