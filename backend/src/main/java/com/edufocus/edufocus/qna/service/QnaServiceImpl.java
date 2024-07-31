package com.edufocus.edufocus.qna.service;

import com.edufocus.edufocus.lecture.entity.Lecture;
import com.edufocus.edufocus.lecture.repository.LectureRepository;
import com.edufocus.edufocus.qna.entity.Qna;
import com.edufocus.edufocus.qna.entity.QnaRequestDto;
import com.edufocus.edufocus.qna.entity.QnaResponseDto;
import com.edufocus.edufocus.qna.repository.QnaRepository;
import com.edufocus.edufocus.user.model.entity.User;
import com.edufocus.edufocus.user.model.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class QnaServiceImpl implements QnaService{

    private final QnaRepository qnaRepository;
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;



    @Override
    public QnaResponseDto createQna(Long id, QnaRequestDto qnaRequestDto, Long lecture_id) {


        Lecture lecture = lectureRepository.findById(lecture_id).orElse(null);

        User user = userRepository.findById(id).orElse(null);



        Qna qna = QnaRequestDto.toEntity(qnaRequestDto);
        qna.setLecture(lecture);
        qna.setUser(user);

        qna.setCreatedAt(new Date());

        qnaRepository.save(qna);
    return QnaResponseDto.toEntity(qna);
    }

    @Override
    public QnaResponseDto  updateQna(Long id,QnaRequestDto qnaRequestDto) {


        Qna findQna = qnaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QnA not found"));

        findQna.setModifiedAt(new Date());
        findQna.setTitle(qnaRequestDto.getTitle());
        findQna.setContent(qnaRequestDto.getContent());

        qnaRepository.save(findQna);

            return QnaResponseDto.toEntity(findQna);


    }

    @Override
    public void deleteQna(Long id) {
qnaRepository.deleteById(id);
    }

    @Override
    public QnaResponseDto getQna(Long id) {
        Optional<Qna> qna;
        try {

            qna= qnaRepository.findById(id);


        } catch (Exception e) {

            throw new RuntimeException("Qna 없음 " + id, e);
        }



            return QnaResponseDto.toEntity(qna.get());

    }

    @Override
    public List<QnaResponseDto> getAllQnasByLecture(Long lectureId,int pageSize)
    {

        Pageable pageable = PageRequest.of(0, pageSize);

        Page<Qna> qnaPage = qnaRepository.findByLectureId(lectureId, pageable);


        return qnaPage.getContent().stream()
                .map(QnaResponseDto::toEntity)
                .collect(Collectors.toList());

    }

    @Override
    public QnaResponseDto createAnswer(Long id, QnaRequestDto qnaRequestDto) throws SQLException {

        Qna findQna = qnaRepository.findById(id).orElse(null);
        findQna.setAnswer(qnaRequestDto.getAnswer());

        qnaRepository.save(findQna);

        return QnaResponseDto.toEntity(findQna);

    }

    @Override
    public QnaResponseDto updateAnswer(Long id, QnaRequestDto qnaRequestDto) throws SQLException {

        Qna findQna = qnaRepository.findById(id).orElse(null);
        findQna.setAnswer(qnaRequestDto.getAnswer());

        qnaRepository.save(findQna);

        return QnaResponseDto.toEntity(findQna);
    }

    @Override
    public void deleteAnswer(Long id) throws SQLException {

        qnaRepository.deleteById(id);
    }
}
