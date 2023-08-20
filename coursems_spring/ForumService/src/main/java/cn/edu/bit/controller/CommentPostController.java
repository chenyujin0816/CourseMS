package cn.edu.bit.controller;

import cn.edu.bit.common.dto.CommentDto;
import cn.edu.bit.common.dto.RepliesDto;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.entity.CommentPost;
import cn.edu.bit.entity.ForumPost;
import cn.edu.bit.entity.Replies;
import cn.edu.bit.feign.FeignService;
import cn.edu.bit.service.CommentPostService;
import cn.edu.bit.service.ForumPostService;
import cn.edu.bit.service.RepliesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/commentPost")
public class CommentPostController {
    @Autowired
    ForumPostService forumPostService;
    @Autowired
    CommentPostService commentPostService;
    @Autowired
    RepliesService repliesService;
    @Autowired
    FeignService feignService;

    //查看所有楼层（返回Page）
    @GetMapping("/comments")
    public Result commentList(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam Integer postId) {

        Page page = new Page(currentPage,99);
        IPage pageData = commentPostService.page(page,new QueryWrapper<CommentPost>()
                .eq("root_id",postId)
                .orderByAsc("create_time"));
        return Result.succ(pageData);
    }

    //查看楼层所有回复
    @GetMapping("/replies")
    public Result repliesList(@RequestParam Integer commentId) {

        List replies = repliesService.list(new QueryWrapper<Replies>()
                .eq("parent_id",commentId)
                .orderByAsc("create_time"));
        return Result.succ(replies);
    }

    //跟帖
    @PostMapping("/postComment")
    public Result postComment(@Validated @RequestBody CommentDto commentDto) {

        CommentPost newCommentPost=new CommentPost();
        newCommentPost.setRootId(commentDto.getRootId());
        newCommentPost.setContent(commentDto.getContent());
        newCommentPost.setCommentatorId(commentDto.getCommentatorId());
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        newCommentPost.setCreateTime(timestamp);

        commentPostService.save(newCommentPost);

        ForumPost forumPost=forumPostService.getOne(new QueryWrapper<ForumPost>().eq("id",commentDto.getRootId()));
        forumPost.setUpdateTime(timestamp);
        forumPostService.update(forumPost,new QueryWrapper<ForumPost>().eq("id",forumPost.getId()));

        return Result.succ(null);
    }

    //删楼
    @GetMapping("/deleteComment")
    public Result deleteComment(@RequestParam Long commentId) {
        CommentPost commentPost=commentPostService.getOne(new QueryWrapper<CommentPost>().eq("id",commentId));

        if(this.getCurUserId()!=commentPost.getCommentatorId()&&!this.isAdmin())
            return Result.fail("没有权限");

        commentPostService.remove(new QueryWrapper<CommentPost>().eq("id",commentId));
        return Result.succ(null);
    }

    //回复
    @PostMapping("/postReply")
    public Result postReply(@Validated @RequestBody RepliesDto repliesDto) {

        Replies newReplies=new Replies();
        newReplies.setFromId((long) this.getCurUserId());
        newReplies.setToId(repliesDto.getToId());
        newReplies.setContent(repliesDto.getContent());
        newReplies.setParentId(repliesDto.getParentId());
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        newReplies.setCreateTime(timestamp);

        repliesService.save(newReplies);

        CommentPost commentPost=commentPostService.getOne(new QueryWrapper<CommentPost>().eq("id",repliesDto.getParentId()));
        ForumPost forumPost=forumPostService.getOne(new QueryWrapper<ForumPost>().eq("id",commentPost.getRootId()));
        forumPost.setUpdateTime(timestamp);
        forumPostService.update(forumPost,new QueryWrapper<ForumPost>().eq("id",forumPost.getId()));

        return Result.succ(null);
    }

    //删回复
    @GetMapping("/deleteReply")
    public Result deleteReply(@RequestParam Long replyId) {
        Replies reply=repliesService.getOne(new QueryWrapper<Replies>().eq("id",replyId));
        if(this.getCurUserId()!=reply.getFromId()&&!this.isAdmin())
            return Result.fail("没有权限");
        repliesService.remove(new QueryWrapper<Replies>().eq("id",replyId));
        return Result.succ(null);
    }

    @GetMapping("/isAdmin")
    public boolean isAdmin(){
        return feignService.isAdmin();
    }

    @GetMapping("/getCurUserId")
    public int getCurUserId(){
        return feignService.getCurUserId();
    }

    @GetMapping("/getCurUserType")
    public char getCurUserType(){
        return feignService.getCurUserType();
    }
}
