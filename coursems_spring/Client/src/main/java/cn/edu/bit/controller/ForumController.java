package cn.edu.bit.controller;

import cn.edu.bit.common.dto.CommentDto;
import cn.edu.bit.common.dto.RepliesDto;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.entity.ForumPost;
import cn.edu.bit.feign.ForumFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/client")
public class ForumController {
//    @Qualifier("cn.edu.bit.feign.ForumFeign")
    @Autowired
    ForumFeign feignService;

    @GetMapping("/forumPost/posts")
    public Result postList(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage){
        return feignService.postList(currentPage);
    }

    @GetMapping("/forumPost/post/{id}")
    public Result postDetail(@PathVariable(name = "id") Long id){
        return feignService.postDetail(id);
    }

    @PostMapping("/forumPost/editPost")
    public Result editPost(@RequestBody ForumPost forumPost){
        return feignService.editPost(forumPost);
    }

    @GetMapping("/forumPost/deletePost")
    public Result deletePost(@RequestParam("postId") Long postId){
        return feignService.deletePost(postId);
    }

    @GetMapping("/commentPost/comments")
    public Result commentsList(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage, @RequestParam("postId") Integer postId){
        return feignService.commentsList(currentPage,postId);
    }

    @GetMapping("/commentPost/replies")
    public Result repliesList(@RequestParam("commentId") Integer commentId){
        return feignService.repliesList(commentId);
    }

    @PostMapping("/commentPost/postComment")
    public Result postComment(@RequestBody CommentDto commentDto){
        return feignService.postComment(commentDto);
    }

    @GetMapping("/commentPost/deleteComment")
    public Result deleteComment(@RequestParam("commentId") Long commentId){
        return feignService.deleteComment(commentId);
    }

    @PostMapping("/commentPost/postReply")
    public Result postReply(@RequestBody RepliesDto repliesDto){
        return feignService.postReply(repliesDto);
    }

    @GetMapping("/commentPost/deleteReply")
    public Result deleteReply(@RequestParam("replyId") Long replyId){
        return feignService.deleteReply(replyId);
    }

}
