package com.edufocus.edufocus.board.controller;

import com.edufocus.edufocus.board.entity.dto.*;
import com.edufocus.edufocus.board.entity.vo.Board;
import com.edufocus.edufocus.board.entity.vo.Comment;
import com.edufocus.edufocus.board.service.BoardService;
import com.edufocus.edufocus.user.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Haneol Kim
 */
@RestController
@RequestMapping("/board")
public class BoardController {

    private final JWTUtil jwtUtil;
    private final BoardService boardService;

    public BoardController(BoardService boardService, JWTUtil jwtUtil){
        this.boardService = boardService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping()
    public ResponseEntity<List<ResponseBoardSummaryDto>> searchBoards(
            @RequestParam(value = "category", required = false, defaultValue = "announcement") String category,
            @RequestParam(value = "lectureId") long lectureId,
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo
    ){
        List<ResponseBoardSummaryDto> boardSummaries = boardService.findBoards(pageNo, category, lectureId);

        if(boardSummaries.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(boardSummaries, HttpStatus.OK);
    }

    @GetMapping(value = "/{boardId}")
    public ResponseEntity<ResponseBoardDetailDto> getBoardDetail(
            @PathVariable int boardId
    ){
        ResponseBoardDetailDto responseBoardDetailDto = boardService.findBoardDetail(boardId);

        return new ResponseEntity<>(responseBoardDetailDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addBoard(
            @RequestBody RequestBoardDto requestBoardDto,
            HttpServletRequest request
    ){
        String token = request.getHeader("Authorization");
        long userId = Long.parseLong(jwtUtil.getUserId(token));

        boardService.createBoard(userId, requestBoardDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{boardId}")
    public ResponseEntity<?> updateBoard(
            @PathVariable long boardId,
            @RequestBody RequestBoardUpdateDto requestBoardUpdateDto
    ){
        boardService.updateBoard(boardId, requestBoardUpdateDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{boardId}")
    public ResponseEntity<?> deleteBoard(
            @PathVariable int boardId,
            HttpServletRequest request
    ){
        boardService.deleteBoard(boardId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/comment/{boardId}")
    public ResponseEntity<List<ResponseCommentDto>> getComments(
            @PathVariable int boardId
    ){
        List<ResponseCommentDto> comments = boardService.findComments(boardId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping(value = "/comment/{boardId}")
    public ResponseEntity<?> addComment(
            @PathVariable int boardId,
            @RequestBody RequestCommentDto requestCommentDto,
            HttpServletRequest request
    ){
        String token = request.getHeader("Authorization");
        long userId = Long.parseLong(jwtUtil.getUserId(token));

        boardService.createComment(userId, boardId, requestCommentDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/comment/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable int commentId,
            @RequestBody RequestCommentDto requestCommentDto
    ){
        boardService.updateComment(commentId, requestCommentDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/comment/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable int commentId
    ){
        boardService.deleteComment(commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ExceptionHandler()
    public ResponseEntity<?> NoContentException(Exception exception){
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
