package com.hellzzang.service;

import com.hellzzang.dto.BoardDto;
import com.hellzzang.entity.Board;
import com.hellzzang.repository.BoardRepository;
import com.hellzzang.repository.CommentRepository;
import com.hellzzang.repository.PostRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.hellzzang.entity.QPost.post;
import static com.hellzzang.entity.QUser.user;

/**
 * packageName    : com.hellzzang.service
 * fileName       : BoardService
 * author         : hj
 * date           : 2023-06-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-13        hj       최초 생성
 */
@Service
@RequiredArgsConstructor
public class BoardService {

    private final JPAQueryFactory queryFactory;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public List<Board> boardList(){
        List<Board> result = boardRepository.findAll();
        return result;
    }

    public List<BoardDto> postLists(Long index){

        List<Tuple> tuples = queryFactory
                .select(post.id,
                        post.title,
                        post.content,
                        post.likes,
                        user.nickname)
                .from(post)
                .where(post.board.id.eq(index))
                .fetch();

        List<BoardDto> result = tuples.stream()
                .map(tuple -> new BoardDto(
                        tuple.get(post.id),
                        tuple.get(post.title),
                        tuple.get(post.content),
                        tuple.get(post.likes),
                        tuple.get(user.nickname)
                ))
                .collect(Collectors.toList());

        return result;
    }

}
