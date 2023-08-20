package cn.edu.bit.feign;

import cn.edu.bit.common.dto.CommentDto;
import cn.edu.bit.common.dto.RepliesDto;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.entity.ForumPost;
import cn.edu.bit.feign.impl.ForumFeignCallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "forum-service",fallback = ForumFeignCallBack.class)
public interface ForumFeign {
    @GetMapping("/commentPost/comments")
    Result commentsList(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage, @RequestParam("postId") Integer postId);

    @GetMapping("/commentPost/replies")
    Result repliesList(@RequestParam("commentId") Integer commentId);

    @PostMapping("/commentPost/postComment")
    Result postComment(@RequestBody CommentDto commentDto);

    @GetMapping("/commentPost/deleteComment")
    Result deleteComment(@RequestParam("commentId") Long commentId);

    @PostMapping("/commentPost/postReply")
    Result postReply(@RequestBody RepliesDto repliesDto);

    @GetMapping("/commentPost/deleteReply")
    Result deleteReply(@RequestParam("replyId") Long replyId);

    @GetMapping("/forumPost/posts")
    Result postList(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage);

    @GetMapping("/forumPost/post/{id}")
    Result postDetail(@PathVariable(name = "id") Long id);

    @PostMapping("/forumPost/editPost")
    Result editPost(@RequestBody ForumPost forumPost);

    @GetMapping("/forumPost/deletePost")
    Result deletePost(@RequestParam("postId") Long postId);
}
