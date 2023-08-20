package cn.edu.bit.feign.impl;

import cn.edu.bit.common.dto.CommentDto;
import cn.edu.bit.common.dto.RepliesDto;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.entity.ForumPost;
import cn.edu.bit.feign.ForumFeign;
import org.springframework.stereotype.Component;

@Component
public class ForumFeignCallBack implements ForumFeign {
    @Override
    public Result commentsList(Integer currentPage, Integer postId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result repliesList(Integer commentId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result postComment(CommentDto commentDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result deleteComment(Long commentId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result postReply(RepliesDto repliesDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result deleteReply(Long replyId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result postList(Integer currentPage) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result postDetail(Long id) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result editPost(ForumPost forumPost) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result deletePost(Long postId) {
        return Result.fail("服务维护中，请稍后。。。");
    }
}
