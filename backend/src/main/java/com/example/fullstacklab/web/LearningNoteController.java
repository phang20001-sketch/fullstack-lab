package com.example.fullstacklab.web;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.fullstacklab.domain.LearningNote;
import com.example.fullstacklab.mapper.LearningNoteMapper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Validated
@RestController
@RequestMapping("/api/notes")
public class LearningNoteController {

	private final LearningNoteMapper learningNoteMapper;

	public LearningNoteController(LearningNoteMapper learningNoteMapper) {
		this.learningNoteMapper = learningNoteMapper;
	}

	@GetMapping
	public List<LearningNote> listRecent(
			@RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit) {
		return learningNoteMapper.selectRecent(limit);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LearningNote create(@Valid @RequestBody CreateLearningNoteRequest request) {
		OffsetDateTime now = OffsetDateTime.now();
		LearningNote note = new LearningNote();
		note.setTitle(request.title());
		note.setContent(request.content());
		note.setCreatedAt(now);
		note.setUpdatedAt(now);
		learningNoteMapper.insert(note);
		return note;
	}

	public record CreateLearningNoteRequest(
			@NotBlank @Size(max = 200) String title,
			@Size(max = 5000) String content) {
	}
}
