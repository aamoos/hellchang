package com.hellzzang.service;

import com.hellzzang.dto.*;
import com.hellzzang.entity.*;
import com.hellzzang.repository.BoardRepository;
import com.hellzzang.repository.CommentRepository;
import com.hellzzang.repository.PostRepository;
import com.hellzzang.repository.UserRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hellzzang.entity.QComment.comment;
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
    private final UserRepository userRepository;

    /**
    * @methodName : boardList
    * @date : 2023-06-16 오전 9:05
    * @author : hj
    * @Description: 커뮤니티 상단 게시판 목록 출력
    **/
    public List<Board> boardList(){
        List<Board> result = boardRepository.findAll();
        return result;
    }

    /**
    * @methodName : postLists
    * @date : 2023-06-16 오전 9:05
    * @author : hj
    * @Description: 게시글 목록 출력
    **/
    public List<BoardDto> postLists(Long index){

        List<BoardDto> result = queryFactory
                .select(new QBoardDto(
                        post.id,
                        post.title,
                        post.content,
                        post.likes,
                        user.nickname,
                        JPAExpressions
                                .select(comment.count())
                                .from(comment)
                                .where(comment.post.eq(post))
                ))
                .from(post)
                .where(post.board.id.eq(index))
                .fetch();


        return result;
    }

    /**
    * @methodName : detailContent
    * @date : 2023-06-16 오전 9:06
    * @author : hj
    * @Description: 게시글 상세 출력 
    **/
    public DetailContentDto detailContent(Long postId){

        DetailContentDto result = queryFactory
                .select(new QDetailContentDto(
                        post.id,
                        post.title,
                        post.content,
                        post.likes,
                        post.postDate,
                        user.nickname,
                        user.id
                ))
                .from(post)
                .where(post.id.eq(postId))
                .fetchOne();

        return result;
    }

    
    /**
    * @methodName : commentList
    * @date : 2023-06-16 오전 9:06
    * @author : hj
    * @Description: 게시글의 댓글 리스트 출력
    **/
    public List<CommentListDto> commentList(Long postId){
        QComment parent = comment;

        List<Tuple> tuples = queryFactory
                .select(parent,
                        user.nickname)
                .from(parent)
                .where(parent.parentComment.isNull().and(parent.post.id.eq(postId)))
                .orderBy(parent.id.asc())
                .fetch();

        List<CommentListDto> result = tuples.stream()
                .map(tuple -> new CommentListDto(
                        tuple.get(parent)
                ))
                .collect(Collectors.toList());

        return result;
    }

    /**
    * @methodName : writeComment
    * @date : 2023-06-20 오후 5:04
    * @author : hj
    * @Description: 댓글 및 대댓글 작성
    **/
    public void writeReply(Long postId, Long userId, Long parentId, String comment){
        Comment commentInsert = new Comment();

        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Comment parentComment = parentId != null ? commentRepository.findById(parentId).orElseThrow(() -> new IllegalArgumentException("Parent comment not found")) : null;

        commentInsert.setPost(post);
        commentInsert.setUser(user);
        commentInsert.setParentComment(parentComment);
        commentInsert.setContent(comment);
        commentInsert.setCommentDate(LocalDateTime.now());

        commentRepository.save(commentInsert);
    }

    /**
    * @methodName : replyInfo
    * @date : 2023-06-21 오후 2:04
    * @author : hj
    * @Description: 댓글 수정 시 기존 댓글 가져옴
    **/
    public Optional<Comment> replyInfo(Long id){
        return commentRepository.findById(id);
    }
    
    /**
    * @methodName : replyUpdate
    * @date : 2023-06-21 오후 5:08
    * @author : hj
    * @Description: 댓글 수정
    **/
    public void replyUpdate(Long id, String reply){
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isPresent()){
            Comment commentSet = comment.get();
            commentSet.setContent(reply);
            commentRepository.save(commentSet);
        }
    }
    
    /**
    * @methodName : writePost
    * @date : 2023-06-22 오후 4:55
    * @author : hj
    * @Description: 게시글 작성
    **/
    public void writePost(Long boardId, Long userId, String title, String content, int likes){
        Post post = new Post();
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        post.setBoard(board);
        post.setUser(user);
        post.setTitle(title);
        post.setContent(content);
        post.setLikes(likes);
        post.setPostDate(LocalDateTime.now());

        postRepository.save(post);
    }
}
