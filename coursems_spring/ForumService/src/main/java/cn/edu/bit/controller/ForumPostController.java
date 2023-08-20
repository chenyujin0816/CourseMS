package cn.edu.bit.controller;

import cn.edu.bit.common.lang.Result;
import cn.edu.bit.entity.CommentPost;
import cn.edu.bit.entity.ForumPost;
import cn.edu.bit.entity.Replies;
import cn.edu.bit.feign.FeignService;
import cn.edu.bit.service.*;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/forumPost")
public class ForumPostController {
    @Autowired
    ForumPostService forumPostService;
    @Autowired
    CommentPostService commentPostService;
    @Autowired
    RepliesService repliesService;
    @Autowired
    FeignService feignService;

    //查看所有帖子（返回Page）
    @GetMapping("/posts")
    public Result postList(@RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage,99);
        IPage pageData = forumPostService.page(page,new QueryWrapper<ForumPost>().orderByDesc("update_time"));
        return Result.succ(pageData);
    }

    //查看帖子详情
    @GetMapping("/post/{id}")
    public Result postDetail(@PathVariable(name = "id") Long id) {
        ForumPost forumPost = forumPostService.getOne(new QueryWrapper<ForumPost>().eq("id",id));
        Assert.notNull(forumPost,"该贴已被删除！");
        return Result.succ(forumPost);
    }

    //编辑帖子(发帖)
    @PostMapping("/editPost")
    public Result editPost(@Validated @RequestBody ForumPost forumPost) {

        int curUserId = this.getCurUserId();
        ForumPost temp=null;
        if (forumPost.getId()!=null){
            temp=forumPostService.getOne(new QueryWrapper<ForumPost>().eq("id",forumPost.getId()));
            if(temp.getCreatorId()!=curUserId)
                return Result.fail("没有权限编辑");
            temp.setTitle(forumPost.getTitle());
            temp.setDescription(forumPost.getDescription());
            temp.setVisibility(forumPost.getVisibility());
            temp.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            forumPostService.update(temp,new QueryWrapper<ForumPost>().eq("id",temp.getId()));
        }else{
            temp=new ForumPost();
            temp.setCreatorId((long) curUserId);
            temp.setCreateTime(new Timestamp(System.currentTimeMillis()));
            temp.setUpdateTime(temp.getCreateTime());
            temp.setCreatorType(this.getCurUserType());
            temp.setTitle(forumPost.getTitle());
            temp.setDescription(forumPost.getDescription());
            temp.setVisibility(forumPost.getVisibility());
            forumPostService.save(temp);
        }
        return Result.succ(null);
    }

    //删帖
    @GetMapping("/deletePost")
    public Result deletePost(@RequestParam Long postId) {
        ForumPost forumPost=forumPostService.getOne(new QueryWrapper<ForumPost>().eq("id",postId));
        if(!this.isAdmin()&&this.getCurUserId()!=forumPost.getCreatorId())
            return Result.fail("没有权限");
        forumPostService.remove(new QueryWrapper<ForumPost>().eq("id",postId));
        List<CommentPost> comments = commentPostService.list(new QueryWrapper<CommentPost>().eq("root_id",postId));
        for (CommentPost comment : comments) {
            repliesService.remove(new QueryWrapper<Replies>().eq("parent_id", comment.getId()));
        }
        commentPostService.remove(new QueryWrapper<CommentPost>().eq("root_id",postId));
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
